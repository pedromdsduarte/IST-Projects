import nltk
import os
from pprint import pprint
from nltk.corpus import floresta
from nltk.tree import Tree
from nltk.chunk import RegexpParser
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from collections import Counter


STOPWORDS = stopwords.words('english') + stopwords.words('portuguese')

with open('stopwords.txt','r', encoding='utf-8') as f:
    STOPWORDS += [line.strip() for line in f.readlines()]

STOPWORDS = set(STOPWORDS)
PUNCTUATION = r"!\"#$&'()-*+,./:;<=>?@[\\]^_`{|}~'`´"

def flatten(l): return [item for sublist in l for item in sublist]

def import_texts(folder, limit=None):
    documents = []

    i = 0
    for filename in os.listdir(folder):
        if not filename.endswith(".txt"):
            continue
        file = open(folder + '\\' + filename, 'r', encoding="latin-1")
        documents.append(file.read())
        file.close()

        # So para limitar o numero de documentos
        i += 1
        if (limit != None and i >= limit):
            break

    return documents

def get_sentences(document, language='portuguese'):

    # TODO: fazer pre-processing

    proto_sentences = document.split('\n')
    sentences = []
    for sentence in proto_sentences:
        new_sentence = nltk.sent_tokenize(sentence, language=language)
        if len(new_sentence) == 0:
            continue
        sentences += new_sentence
    return sentences

def printProgressBar (iteration, total, prefix = '', suffix = '', decimals = 1, length = 100, fill = '█'):
    """
    Call in a loop to create terminal progress bar
    @params:
        iteration   - Required  : current iteration (Int)
        total       - Required  : total iterations (Int)
        prefix      - Optional  : prefix string (Str)
        suffix      - Optional  : suffix string (Str)
        decimals    - Optional  : positive number of decimals in percent complete (Int)
        length      - Optional  : character length of bar (Int)
        fill        - Optional  : bar fill character (Str)
    """
    percent = ("{0:." + str(decimals) + "f}").format(100 * (iteration / float(total)))
    filledLength = int(length * iteration // total)
    bar = fill * filledLength + '-' * (length - filledLength)
    print('\r%s |%s| %s%% %s' % (prefix, bar, percent, suffix), end='\r')
    # Print New Line on Complete
    if iteration == total:
        print()

def print_metrics(metrics):
    print('-----------------------------------------------')
    print("PRECISION =", metrics['precision'])
    print("RECALL =", metrics['recall'])
    print("F1 =", metrics['f1'])
    print("MAP =", metrics['map'])

    print('-----------------------------------------------')

def get_precision(results, target):
    if len(results) == 0:
        return 0
    return len(target.intersection(results)) / len(results)

def get_recall(results, target):
    return len(target.intersection(results)) / len(target)

def get_f1(results, target):
    pr = get_precision(results, target)
    re = get_recall(results, target)
    if re + pr == 0:
        return 0
    return (2 * re * pr ) / (re + pr)

def map(precision_values, recall_values, results, targets):

    numerator = 0
    Q = len(results) # number of queries = number of summaries made
    N = len(results[0]) # number of elements in the top

    aps = []
    for q in range(0,Q):
        ap = []
        retrieved_documents = results[q]
        relevant_documents = set(get_sentences(targets[q]))
        aux = set()

        #for i in range(0, N):
        for retrieved_doc in retrieved_documents:
            #aux.add(retrieved_documents[i])
            aux.add(retrieved_doc)
            if retrieved_doc in relevant_documents:
                relevance = 1
            else:
                relevance = 0
            precision_i = get_precision(aux, relevant_documents)
            ap.append(precision_i * relevance)
        ap = sum(ap) / len(relevant_documents)
        aps.append(ap)
    return sum(aps) / Q

def evaluate(summary, target):

    summary = set(summary)
    target = set(get_sentences(target))

    evaluation = {}

    evaluation['precision'] = get_precision(summary, target)
    evaluation['recall'] = get_recall(summary, target)
    evaluation['f1'] = get_f1(summary, target)

    return evaluation

def evaluate_collection(summaries, targets):

    precision_values = []
    recall_values = []
    f1_values = []

    for i in range(0, len(summaries)):
        summary = summaries[i]
        target = targets[i]

        evaluation = evaluate(summary, target)
        precision_values.append(evaluation['precision'])
        recall_values.append(evaluation['recall'])
        f1_values.append(evaluation['f1'])

    avg_eval = {}
    avg_eval['precision'] = round(sum(precision_values) / len(precision_values),7)
    avg_eval['recall'] = round(sum(recall_values) / len(recall_values),7)
    avg_eval['f1'] = round(sum(f1_values) / len(f1_values),7)
    avg_eval['map'] = map(precision_values, recall_values, summaries, targets)
    return avg_eval

def get_noun_phrases(text_list, tagger):
    noun_phrases = []
    tagged_texts = [tagger.tag(text.split()) for text in text_list]

    expression=r'NOUN_PHRASE: {(<adj>* <n.*>+ <prp>)? <adj>* <n.*>+}'

    chunkParser = RegexpParser(expression)

    for tagged_sent in tagged_texts:
        try:
            if len(tagged_sent) == 0:
                tree = Tree('S', [])
            else:
                tree = chunkParser.parse(tagged_sent, trace=0)
            for subtree in tree.subtrees():
                if subtree.label() == "NOUN_PHRASE":
                    noun_phrases.append([el[0] for el in subtree.leaves()])
        except ValueError:
            noun_phrases = []
    return noun_phrases

def get_pos_tagger(training, tagger='Perceptron'):
    training = [[(w.lower(),simplify_tag(t)) for (w,t) in sent] for sent in training if sent]


    if tagger=='Perceptron':
        tagger = nltk.tag.PerceptronTagger(load=False)
        tagger.train(training)
    else:
        tagger0 = nltk.DefaultTagger('n')

        if tagger=='Unigram':
            tagger1 = nltk.UnigramTagger(training, backoff=tagger0)
        elif tagger=='Bigram':
            tagger1 = nltk.UnigramTagger(training, backoff=tagger0)
            tagger = nltk.BigramTagger(training, backoff=tagger1)

    return tagger

def create_tagger(tagger='Perceptron'):
    print("\n[TAGGER] Creating a POS tagger")
    training,_ = get_training()
    return get_pos_tagger(training, tagger=tagger)

def get_training():
    training = floresta.tagged_sents()[10:]
    target = floresta.tagged_sents()[:10]
    return training, target

def simplify_tag(t):
    if "+" in t:
        return t[t.index("+")+1:]
    else:
        return t


def pre_process(sentences):

    """
    Recebe uma lista de frases
    Retorna uma lista de frases pre-processadas
    ATENCAO: a cada frase original deve corresponder uma frase processada
    (i.e. o tamanho deve ser o mesmo, e nao deve haver nenhum offset)
    """
    stem = PorterStemmer()

    bow = flatten([nltk.word_tokenize(s.lower()) for s in sentences])
    wordcount = Counter(bow)


    processed_sentences = []

    for sentence in sentences:


        words = nltk.word_tokenize(sentence)


        #######################
        # Minúsculas
        #######################

        words = [word.lower() for word in words]


        #######################
        # Remover stopwords
        #######################

        words = [word for word in words if word not in STOPWORDS]


        ############################################
        # Remover palavras que só ocorrem uma vez
        ############################################

        words = [word for word in words if wordcount[word] > 1]

        ############################################
        # Remover palavras muito frequentes
        ############################################

        #words = [word for word in words if word not in wordcount.most_common(5)]

        #######################
        # Normalização
        #######################

        words = [stem.stem(word) for word in words]

        #######################
        # Remover pontuacao
        #######################

        words = [word.translate(str.maketrans('', '', PUNCTUATION) )for word in words]


        processed_sentence = " ".join(words)
        processed_sentences.append(processed_sentence)




    return processed_sentences
