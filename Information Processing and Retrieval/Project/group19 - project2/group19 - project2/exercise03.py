from utils import *
from sklearn import svm
from sklearn.linear_model import perceptron
from sklearn.linear_model import SGDClassifier
from sklearn.neighbors import KNeighborsClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn import metrics
from exercise02 import *
from pprint import pprint
import networkx as nx
from nltk.corpus import stopwords
import pickle
import json



#np.set_printoptions(linewidth=500, precision=0, formatter={'all':lambda x: str(int(x))})
np.set_printoptions(linewidth=500, precision=1)

import warnings
import sklearn.exceptions

warnings.filterwarnings("ignore", category=sklearn.exceptions.UndefinedMetricWarning)
TOPNOUNS = 10


# ------------------------------------------------------------------------ #
# Features
# ------------------------------------------------------------------------ #

def feature_sentence_position(raw_training_data):
    sentences = get_sentences(raw_training_data)
    num_sentences = len(sentences)

    l = []
    for x in range(num_sentences,0,-1):
        l.append(x)
    return np.array(l)

def feature_cosine_similarity(raw_training_data):

    sentences = get_sentences(raw_training_data)

    document = ' '.join(sentences)
    N = len(sentences)
    prior = []

    vectorizer = TfidfVectorizer()
    sent_vectors = vectorizer.fit_transform(sentences)
    doc_vector = vectorizer.fit_transform([document])

    for i in range(N):
        sentence = sentences[i]
        sent_vector = sent_vectors[i]

        similarity = cosine_similarity(sent_vector, doc_vector)[0][0]

        prior.append(similarity)
    return np.array(prior)

def feature_page_rank(raw_training_data):
    sentences = get_sentences(raw_training_data)

    similarity = calculate_similarity(sentences)
    graph = create_graph(similarity, threshold=0.2)
    ranks = compute_page_rank2(graph)

    return np.array(ranks)

def feature_graph_centrality_score(raw_training_data):
    sentences = get_sentences(raw_training_data)


    similarity = calculate_similarity(sentences)
    graph = create_graph(similarity, threshold=0.2)

    N = len(graph.keys())
    # Graph é um dicionario node1 -> links
        # links é uma lista com tuplos (nodes, weights)
        # i.e. node1 liga com node2 com weight1, ...

    G = nx.Graph()

    for node1 in graph.keys():
        for (node2, weight) in graph[node1]:
            if node1 == -1 or node2 == -1:
                #G.add_edge(node1, node2, weight=0)
                pass
            else:
                G.add_edge(node1, node2, weight=weight)
    #degree_centrality_scores = nx.degree_centrality(G)
    degree_centrality_scores = nx.betweenness_centrality(G)

    c_scores = np.zeros((N), dtype=np.float32)
    for node in degree_centrality_scores.keys():
        c_scores[node] = degree_centrality_scores[node]

    return c_scores

def feature_title_similarity(raw_training_data):
    sentences = get_sentences(raw_training_data)
    N = len(sentences)
    features = np.zeros(N)
    vectorizer = TfidfVectorizer()
    sentences_weights = vectorizer.fit_transform(sentences)
    title_weight = sentences_weights[0]


    for i in range(len(sentences)):
        features[i] = cosine_similarity(sentences_weights[i],title_weight)
    return features

def feature_sentence_lenght_absolute(raw_training_data):
    sentences = get_sentences(raw_training_data)
    N = len(sentences)
    features = np.zeros(N)

    for i in range(N):
        features[i] = len(sentences[i])
    return features

def feature_sentence_lenght_relative(raw_training_data):
    sentences_len = feature_sentence_lenght_absolute(raw_training_data)
    doc_len = sum(sentences_len)
    features = np.divide(sentences_len, doc_len)
    return features

def feature_occurence_proper_names(raw_training_data):
    # binary feature

    sentences = get_sentences(raw_training_data)

    N = len(sentences)
    feature_vector = np.zeros(N)

    proper_nouns = []

    for sentence in sentences:
        text = nltk.word_tokenize(sentence)
        tagged_sentence = nltk.pos_tag(text)

        """
        Provavelmente ficaria melhor se a deteção de Nomes Próprios fosse melhor
        Falar disto no relatório (i.e. NER, POS tagger, ...)
        """
        new_nouns = [nn[0] for nn in tagged_sentence if nn[1] == "NNP" and nn[0].lower() not in STOPWORDS]
        proper_nouns += new_nouns

    proper_nouns = set(proper_nouns)

    for i in range(N):
        words = set(nltk.word_tokenize(sentences[i]))
        feature_vector[i] = 1 if len(proper_nouns & words) > 0 else 0

    return feature_vector

def feature_main_concepts(raw_training_data):

    """
    Vê quais os conceitos mais falados no documento (nomes)
    E conta quantos destes conceitos aparecem em cada frase

    A feature original apenas via se uma frase mencionava ou não um conceito
    Feature deixou de ser binária
    """

    sentences = get_sentences(raw_training_data)

    N = len(sentences)
    feature_vector = np.zeros(N)

    nouns = []


    for sentence in sentences:
        text = nltk.word_tokenize(sentence)
        tagged_sentence = nltk.pos_tag(text)
        new_nouns = [nn[0] for nn in tagged_sentence if nn[1] == "NN" and nn[0] not in STOPWORDS]
        nouns += new_nouns
    nouns = dict.fromkeys(nouns, 0)

    for sentence in sentences:
        words = set(nltk.word_tokenize(sentence))
        intersection = set(nouns.keys() & words)
        for NN in intersection:
            nouns[NN] += 1

    nouns = sorted(nouns.items(), key=itemgetter(1), reverse=True)
    topnouns = set([n[0] for n in nouns[:TOPNOUNS]])

    for i in range(N):
        words = set(nltk.word_tokenize(sentences[i].lower()))
        #feature_vector[i] = 1 if len(topnouns & words) > 0 else 0
        feature_vector[i] = len(topnouns & words)

    return feature_vector

# ------------------------------------------------------------------------ #

TRAINING_FOLDER = 'TeMário 2006\Originais'
TARGET_FOLDER = 'TeMário 2006\SumáriosExtractivos'

TESTING_FOLDER_INPUT = 'TeMario\Textos-fonte\Textos-fonte com titulo'
TESTING_FOLDER_OUTPUT = 'TeMario\Sumarios\Extratos ideais automaticos'


FEATURES = [feature_occurence_proper_names,
            feature_main_concepts,
            feature_sentence_lenght_relative,
            feature_sentence_lenght_absolute,
            feature_title_similarity,
            feature_sentence_position,
            feature_cosine_similarity,
            feature_page_rank,
            feature_graph_centrality_score]
"""
FEATURES = [feature_sentence_position,
            feature_page_rank,
            feature_graph_centrality_score]
"""

METRICS = [metrics.precision_score,
           metrics.recall_score,
           metrics.f1_score,
           metrics.average_precision_score,
           metrics.accuracy_score]

def import_data(root_folder):

    training_data = []
    print("[IMPORT] Importing data from", root_folder)
    for dir in os.listdir(root_folder):
        subdir = os.path.join(root_folder, dir)
        new_texts = import_texts(subdir)

        training_data += new_texts
    return training_data

def create_training_data():
    raw_training_data = import_data(TRAINING_FOLDER)

    # raw_training_data : lista com textos (151)

    #training_data = np.array([])
    training_data = []

    """
    X -> (n_samples, n_features)
    Y -> (n_samples, n_targets)
    """


    print("[IMPORT] Creating training data")
    for i in range(len(raw_training_data)):
        text = raw_training_data[i]

        text_features = np.stack((feature_func(text) for feature_func in FEATURES)).T




        #training_data.append(np.asarray(text_features))
        training_data += text_features.tolist()
        #training_data = np.stack((training_data, text_features))

        #print(training_data)

    # training_data : lista com, para cada texto, um vector de vectores de features

    training_data = np.array(training_data)

    return training_data

def create_target_data():

    # targets : lista com 151 elementos (1 por texto)
        # cada elemento contem uma lista com o tamanho = nº de frases

    targets = []

    texts = import_data(TRAINING_FOLDER)
    summaries = import_data(TARGET_FOLDER)

    for i in range(len(texts)):
        text = texts[i]
        summary = summaries[i]

        sentences = get_sentences(text)
        summary_sentences = get_sentences(summary)


        #target = np.zeros((len(sentences)),dtype=int)
        target = np.zeros((len(sentences)), dtype=int)

        for t in range(len(target)):
            sentence = sentences[t]
            if sentence in summary_sentences:
                #target[t] = summary_sentences.index(sentence) + 1
                target[t] = 1

        targets.append(target)


    targets = np.array(flatten(targets[::-1]))

    return targets

def compute_features(data):
    """
    Recebe o texto e
    retorna um vector com shape=(n_frases, n_features)
    """

    feature_vector = np.stack((feature_func(data) for feature_func in FEATURES))

    vec = np.rot90(feature_vector)
    vec = np.rot90(vec)
    vec = np.rot90(vec)


    return vec

def compute_page_rank2(graph, damping=0.15, iterations=50, convergenceFactor=0.0001):
        N = len(graph.keys())
        prestige0 = np.full(N,(1/N))
        prestige = np.zeros(N)

        out_degree = compute_out_degree(graph)

        for it in range(iterations):
            for i in range(N):
                if(graph[i][0][0] == -1):
                    prestige[i] = -1
                    continue
                connectors = []
                sums = 0
                connectors.append([el[0] for el in graph[i]])
                for el in connectors[0]:
                    sums += prestige0[el] / out_degree[el]

                prestige[i] = (damping/N) + (1 - damping) * sums


            if(np.sum(np.abs(prestige0 - prestige)) < convergenceFactor):
                break

            prestige0 = np.copy(prestige)

        num_iterations.append(it)

        return prestige

def get_metrics(prediction, target):
    #print("PREDICTION:",prediction)
    #print("TARGET:    ",target)

    evals = {}

    print("# -------------------------------------------- ")
    print("# Metrics:")
    print("# -------------------------------------------- ")
    for metric in METRICS:
        evals[metric.__name__.upper()] = str(metric(target, prediction))
        print("| " + metric.__name__.upper()+": " + str(metric(target, prediction)))
    print("# -------------------------------------------- \n")

    return evals

def predict(model, text_repr):
    """
    Recebe um modelo e um vector com vectores de features (representacoes)
    Devolve um vector de classificacoes ([0 1 0 ... 1])
    """

    return model.predict(text_repr)

def train_model(training_data, target_data):


    """
    X -> (n_samples, n_features)
    Y -> (n_samples, n_targets)
    """


    print("Training shape:", training_data.shape)
    print("Target shape:", target_data.shape)



    #tr90 = np.rot90(training_data)
    #tr90 = np.rot90(tr90)
    #tr90 = np.rot90(tr90)

    #model = DecisionTreeClassifier()
    #model = perceptron.Perceptron(n_iter=75)
    model = GaussianNB()
    #model = KNeighborsClassifier()
    print("Training the model")
    model.fit(training_data, target_data)

    return model

def test_algorithms():
    training_data = create_training_data()
    target_data = create_target_data()

    testing_input = [compute_features(text) for text in import_texts(TESTING_FOLDER_INPUT)]


    #summaries = [get_sentences(text) for text in import_texts(TESTING_FOLDER_OUTPUT)]
    summaries = [text for text in import_texts(TESTING_FOLDER_OUTPUT)]
    original_docs = [text for text in import_texts(TESTING_FOLDER_INPUT)]
    #original_doc_sentences = [get_sentences(text) for text in import_texts(TESTING_FOLDER_INPUT)]


    """
    X -> (n_samples, n_features)
    Y -> (n_samples, n_targets)
    """

    targets = []
    for i in range(len(original_docs)):
        text = original_docs[i]
        summary = summaries[i]

        sentences = get_sentences(text)
        summary_sentences = get_sentences(summary)

        target = np.zeros((len(sentences)), dtype=int)

        for t in range(len(target)):
            sentence = sentences[t]
            if sentence in summary_sentences:
                #target[t] = summary_sentences.index(sentence) + 1
                target[t] = 1

        targets.append(target)


    testing_output = np.array(targets)


    print("Training shape:", training_data.shape)
    print("Target shape:", target_data.shape)
    print("Testing output shape:", testing_output.shape)


    ALGORITHMS = [perceptron.Perceptron,
                  svm.SVC,
                  SGDClassifier,
                  KNeighborsClassifier,
                  GaussianNB,
                  DecisionTreeClassifier]

    results = {}

    for algorithm in ALGORITHMS:
        print("[TESTING] Testing", algorithm.__name__)
        model = algorithm()
        model.fit(training_data, target_data)


        all_predictions = []
        all_targets = []

        for i in range(len(testing_input)):
            test_input = testing_input[i]   # test_input : um texto, i.e., lista com "frases" (featurizadas)
            test_output = testing_output[i] # test_output: uma lista com tamanho = frases do test_input. elementos = classe

            prediction = model.predict(test_input)
            all_predictions += prediction.tolist()
            all_targets += test_output.tolist()


        results[algorithm.__name__.upper()] = get_metrics(all_predictions, all_targets)
    with open('testing_results.json', 'w') as file:
        file.write(json.dumps(results))
        file.write("\n")
        for feature in FEATURES:
            file.write(feature.__name__ + " ")

def remove_negative_ranks(tr,trg):
    result_tr = []
    result_trg = []
    for i in range(len(tr)):
        if -1 in tr[i]:
            continue
        result_tr.append(tr[i])
        result_trg.append(trg[i])

    return np.array(result_tr),np.array(result_trg)

def summarize_ex3(document, model, top=5):
    original_sentences = get_sentences(document)
    sentence_representation = compute_features(document)

    classification = predict(model, sentence_representation)

    summary = []

    #print("Classification:",classification)
    retrieved = []

    for i  in range(len(classification)):
        c = classification[i]
        if c == 1:
            retrieved.append(i)
    #print("Retrieved:", retrieved)


    p_rank_scores = feature_page_rank(document)
    similarity_scores = feature_cosine_similarity(document)

    scores = {}
    for i in retrieved:
        #scores[i] = 0.5 * position_scores[i] + 0.2 * p_rank_scores[i] + \
        #            0.2 * similarity_scores[i] + 0.1 * centrality_scores[i]
        scores[i] = 0.5 * similarity_scores[i] + 0.5 * p_rank_scores[i]

    ordered_rank = sorted(scores.items(), key=itemgetter(1), reverse=True)
    summary = [original_sentences[s[0]] for s in ordered_rank[:5]]
    #print(summary)
        #summary.append(original_sentences[np.where(classification==c)[0][0]])

    return summary

def test_exercise3():
    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'

    limit = None
    documents = import_texts(collection_folder, limit=limit)
    targets = import_texts(target_folder, limit=limit)

    summaries = []



    try:
        with open('model.pkl', 'rb') as f:
            model = pickle.load(f)
            print("[*] Model found")
    except:
        print("[*] Model not found. Training...")

        # training_data: vector com features de sentences
        training_data = create_training_data()

        # target_data: vector com classificacoes (vector de vectores)
            # classificacoes: vector com classes
        target_data = create_target_data()


        model = train_model(training_data,target_data)
        with open('model.pkl', 'wb') as f:
            pickle.dump(model, f)

    print("Classes:", model.classes_)
    print("[*] Summarizing...")
    printProgressBar(0,len(documents), prefix='\nStatus:' , suffix='  DocID 0 of '+str(len(documents)), length=20 )
    for i in range(len(documents)):
        printProgressBar(i+1, len(documents), prefix='Status:',suffix='  DocID '+str(i+1)+' of '+str(len(documents)), length=20)
        document = documents[i]
        summary = summarize_ex3(document, model)
        summaries.append(summary)


    evaluation = evaluate_collection(summaries, targets)
    print("Features used:")
    for feature in FEATURES:
        print("\t",feature.__name__)
    print('-----------------------------------------------')
    print("PRECISION =", evaluation['precision'])
    print("RECALL =", evaluation['recall'])
    print("F1 =", evaluation['f1'])
    print("MAP =", evaluation['map'])
    print('-----------------------------------------------')

    print("[*] Done")

def main():

    #test_algorithms()
    test_exercise3()





if __name__ == "__main__":
    main()
