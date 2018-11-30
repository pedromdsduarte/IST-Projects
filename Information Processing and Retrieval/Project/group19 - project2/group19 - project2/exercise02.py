from exercise01 import *
import numpy as np
from math import pow
from scipy.stats import geom
import random
from exercise03 import *

from collections import defaultdict

POS_TAGGER = None

import warnings
warnings.filterwarnings(action='ignore', category=UserWarning, module='gensim')

from gensim import corpora, models, similarities

# ============================================================================ #

def prior_variant_sentence_position(documents, graph):
    sentences = documents
    num_sentences = len(sentences)

    l = []
    for x in range(num_sentences,0,-1):
        l.append(x)
    return l

def prior_variant_cosine_similarity(documents, graph):
    sentences = documents
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
    return prior

def prior_variant_title_similarity(documents,graph):
    sentences = documents
    N = len(sentences)
    features = np.zeros(N)
    vectorizer = TfidfVectorizer()
    sentences_weights = vectorizer.fit_transform(sentences)
    title_weight = sentences_weights[0]


    for i in range(len(sentences)):
        features[i] = cosine_similarity(sentences_weights[i],title_weight)
    return features


def prior_variant_sentence_lenght_absolute(documents,graph):
    sentences = documents
    N = len(sentences)
    features = np.zeros(N)

    for i in range(N):
        features[i] = len(sentences[i])
    return features

def prior_variant_sentence_lenght_relative(documents,graph):
    sentences_len =  prior_variant_sentence_lenght_absolute(documents,graph)
    doc_len = sum(sentences_len)
    features = np.divide(sentences_len, doc_len)
    return features


def prior_variant_graph_centrality_score(documents,graph):
    sentences = documents


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

# ============================================================================ #

def weight_variant_cosine_similarity(documents, graph):

    # Edge weights based on the cosine similarity between pairs of sentences,
    # leveraring TF-IDF or BM25 when building the representations.
    # In this case, it's using TF-IDF.
    sentences = documents
    N = len(sentences)
    weights = np.zeros((N,N))

    similarity = calculate_similarity(sentences)

    for i in range(N):
        for j in range(N):
            weights[i][j] = similarity[i][j]

    return weights


## Ver esta funcao, pode estar mal
# Possivelmente no get_noun_phrases?
def weight_variant_noun_phrases(documents, graph):

    # Edge weights beween the number of noun phrases shared between sentences,
    # extracted through a procedure similar to the one used in the
    # first project.

    sentences = documents

    N = len(sentences)
    weights = np.zeros((N,N))

    # If there isn't a tagger created yet, create one
    global POS_TAGGER
    if POS_TAGGER == None:
        POS_TAGGER = create_tagger(tagger='Bigram')

    noun_phrases = get_noun_phrases(sentences, POS_TAGGER)

    for i in range(N):
        for j in range(N):

            common_noun_phrases = set(noun_phrases[i]) & set(noun_phrases[j])
            weights[i][j] = len(common_noun_phrases)

    return weights

# ============================================================================ #

def weight_variant_naive_bayes(documents, graph):
    sentences = documents
    weights = calculate_similarity(sentences)

    #old = np.copy(weights)

    top5 = 1/5
    fact = 5 / len(weights[0])

    for line in range(len(weights[0])):
        for col in range(len(weights[0])):
            probability = weights[line][col] * top5 / fact
            weights[line][col] = probability

    """
    print("\n NEW:\n" + str(weights[0]) + "\n\n")
    print("\n OLD:\n" + str(old[0]) + "\n\n")
    """
    return weights

def weight_variant_dense_repr(documents, graph):
    sentences = documents

    # remove common words and tokenize
    stoplist = nltk.corpus.stopwords.words('portuguese')
    texts = [[word for word in sentence.lower().split() if word not in stoplist]
        for sentence in sentences]


    frequency = defaultdict(int)
    for text in texts:
        for token in text:
            frequency[token] += 1

    texts = [[token for token in text if frequency[token] > 1]
                for text in texts]

    dictionary = corpora.Dictionary(texts)
    corpus = [dictionary.doc2bow(text) for text in texts]

    print("\n\n")
    print(dictionary.token2id)

    print("\n")
    print(corpus)

    tfidf = models.TfidfModel(corpus)

    corpus_tfidf = tfidf[corpus]
    print("\n\ntfidf")
    for doc in corpus_tfidf:
        print(doc)

    lsi = models.LsiModel(corpus_tfidf, id2word=dictionary, num_topics=300)

    corpus_lsi = lsi[corpus_tfidf]

    print("\n\nlsi ", len(corpus_lsi), len(documents))
    for doc in corpus_lsi:
        print(doc)


    for i in range(0, lsi.num_topics-1):
        print("TOPIC ",i,"  " ,lsi.print_topic(i))

    print("num topics", lsi.num_topics)


    # Retornar um vector com pesos
    matrix = []

#    vectorizer = TfidfVectorizer()
#    weights_matrix = vectorizer.fit_transform(sentences)
#    print(weights_matrix)
#    for i in range(0,len(sentences)):
#        matrix.append(cosine_similarity(weights_matrix[i], weights_matrix).tolist()[0])
#
#    return matrix
    return random.random()


# ============================================================================ #

def test_exercise1():
    print("\nExercise 1 approach")
    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'

    limit = None

    documents = import_texts(collection_folder, limit=limit)
    summaries = []

    printProgressBar(0,len(documents), prefix='\nStatus:' , suffix='  DocID 0 of '+str(len(documents)), length=20 )
    for i in range(len(documents)):
        printProgressBar(i+1, len(documents), prefix='Status:',suffix='  DocID '+str(i+1)+' of '+str(len(documents)), length=20)
        document = documents[i]
        summary, num_iter = summarize_ex1(document)
        summaries.append(summary)



    avg_iter = sum(num_iter) / len(num_iter)
    targets = import_texts(target_folder, limit=limit)
    evaluation = evaluate_collection(summaries, targets)
    print_metrics(evaluation)
    print("AVG ITERATIONS =", avg_iter)


def test_exercise2(limit=None):

    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'

    limit = limit

    documents = import_texts(collection_folder, limit=limit)
    summaries = []


    # Para mudar variantes, mudar isto para o nome da variante a usar


    prior_variant = prior_variant_sentence_lenght_relative
    weight_variant = weight_variant_naive_bayes
    #weight_variant = weight_variant_dense_repr

    print()
    print("Using prior:", prior_variant.__name__)
    print("USing weights:", weight_variant.__name__)

    printProgressBar(0,len(documents), prefix='\nStatus:' , suffix='  DocID 0 of '+str(len(documents)), length=20 )
    for i in range(len(documents)):
        printProgressBar(i+1, len(documents), prefix='Status:',suffix='  DocID '+str(i+1)+' of '+str(len(documents)), length=20)
        document = documents[i]
        summary, num_iter = summarize(document, prior_variant=prior_variant, weight_variant=weight_variant)
        summaries.append(summary)

    avg_iter = sum(num_iter) / len(num_iter)
    targets = import_texts(target_folder, limit=limit)
    evaluation = evaluate_collection(summaries, targets)
    print_metrics(evaluation)
    print("AVG ITERATIONS =", avg_iter)

def summarize(document, top=5, prior_variant=prior_variant_sentence_position, weight_variant=weight_variant_cosine_similarity):
    #sentences = [line for line in nltk.sent_tokenize(document)]
    original_sentences = sentences = get_sentences(document)
    similarity = calculate_similarity(sentences)
    graph = create_graph(similarity, threshold=0.2)
    ranks = compute_page_rank(sentences, graph, prior_variant=prior_variant, weight_variant=weight_variant)

    dic = {}
    for i in range(len(ranks)):
        if ranks[i] == -1: continue
        dic[sentences[i]] = ranks[i]
    ordered_rank = sorted(dic.items(), key=itemgetter(1), reverse=True)

    return [x[0] for x in ordered_rank[:top]], num_iterations


# ============================================================================ #

def compute_page_rank(documents, graph, damping=0.15, iterations=50,
                        convergenceFactor=0.0001,
                        prior_variant=prior_variant_sentence_position,
                        weight_variant=weight_variant_cosine_similarity):

    N = len(graph.keys())
    prestige0 = np.full(N,(1/N))
    prestige = np.zeros(N)

    #out_degree = compute_out_degree(graph)

    for it in range(iterations):

        prior = prior_variant(documents, graph) # vector de inteiros
        weights = weight_variant(documents, graph)  # vector de pesos

        for i in range(N):
            if(graph[i][0][0] == -1):
                prestige[i] = -1
                continue
            connectors_i = [el[0] for el in graph[i]]
            sums = 0


            norm_prior = prior[i] / sum(prior)

            sum_links_i = 0
            for j in connectors_i:
                links_j = [el[0] for el in graph[j]]
                weight_j_i = weights[j][i]

                sum_weights_j_k = 0
                for k in links_j:
                    sum_weights_j_k += weights[j][k]


                # Alguma vez pode dar zero???
                """
                if sum_weights_j_k == 0:
                    sum_links_i = 0
                else:
                    sum_links_i += prestige0[j] * weight_j_i / sum_weights_j_k
                """

                sum_links_i += prestige0[j] * weight_j_i / sum_weights_j_k

            prestige[i] = damping * norm_prior + (1 - damping) * sum_links_i


        if(np.sum(np.abs(prestige0 - prestige)) < convergenceFactor):
            break

        prestige0 = np.copy(prestige)

    num_iterations.append(it)
    return prestige

# ============================================================================ #

def main():
    test_exercise1()
    test_exercise2()

if __name__ == "__main__":
    main()
