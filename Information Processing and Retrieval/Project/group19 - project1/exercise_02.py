from utils import *
import sys
import os
from sklearn.feature_extraction.text import CountVectorizer
import argparse
import re
import nltk
import math
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from operator import itemgetter, attrgetter, methodcaller
from pprint import pprint
import string
import time

def flatten(l): return [item for sublist in l for item in sublist]


def get_top_N(similarity, N):
    rank = {}
    for i in range(0, len(similarity)):
        rank[i] = similarity[i][0]

    ordered_rank = sorted(rank.items(), key=itemgetter(1), reverse=True)
    topN = ordered_rank[:N]

    return topN


def import_texts(folder, limit=None):
    # Devolve uma lista de documentos: ['texto_do_doc1', 'texto_do_doc2', ...]
    documents = []

    i = 0
    for filename in os.listdir(folder):
        file = open(folder + '\\' + filename, 'r')
        documents.append(file.read())
        file.close()

        # So para limitar o numero de documentos
        i += 1
        if (limit != None and i >= limit):
            break

    # print("Imported",len(documents),"documents")
    return documents


def remove_punct(texts):
    # Recebe lista de textos
    res = []
    for text in texts:
        punctuation = r"!\"#$&'()-*+,./:;<=>?@[\\]^_`{|}~"
        new_text = []
        for word in text.split(' '):
            if word not in punctuation:

                # Remove pontuacao no final da frase
                # Com while porque pode haver palavras do tipo 'será?"'
                while len(word) > 0 and word[-1] in punctuation:
                    word = word[:-1]

                # Remove pontuacao do inicio da frase
                # Mesma razão
                while len(word) > 0 and word[0] in punctuation:
                    word = word[1:]

                # Separa limites de anos em anos (e.g.: 1750-1900 -> 1750 1900)
                if re.match('\d\d\d\d ?- ?\d\d\d\d', word):
                    new_text.append(word[:4])
                    new_text.append(word[5:])
                    continue

                # Se a palavra tiver sido toda consumida, ignora
                if len(word) != 0:
                    new_text.append(word)
        #res.append(text.translate(str.maketrans('', '', string.punctuation)))
        res.append(' '.join(new_text))
    return res


def pre_process(document):
    # return document.replace('...','.').replace('.',' . ').replace('\n\n','\n')

    #document = document.lower()
    # Remove empty lines
    document = document.replace('\n\n', '\n')
    #document = document.replace('\n',' ')

    # Consolidate punctuation
    #document = re.sub('([.,!?])+',r'\1',document)

    # Put whitespace before and after punctuation
    #document = re.sub('([.,!?()"])', r' \1 ', document)

    return document


def get_sentences(document):

    proto_sentences = document.split('\n')
    sentences = []
    for sentence in proto_sentences:
        new_sentence = nltk.sent_tokenize(sentence, language='portuguese')
        if len(new_sentence) == 0:
            continue
        sentences += new_sentence
    return sentences

#####################################################################################


def get_precision(results, target):
    return len(target.intersection(results)) / len(results)


def get_recall(results, target):
    return len(target.intersection(results)) / len(target)


def get_f1(results, target):
    pr = get_precision(results, target)
    re = get_recall(results, target)
    if re + pr == 0:
        return 0
    return (2 * re * pr) / (re + pr)


def map(precision_values, recall_values, results, targets):

    numerator = 0
    Q = len(results)  # number of queries = number of summaries made
    N = len(results[0])  # number of elements in the top

    aps = []
    for q in range(0, Q):
        ap = []
        retrieved_documents = results[q]
        #relevant_documents = set(get_sentences(pre_process(targets[i])))
        relevant_documents = set(get_sentences(targets[q]))
        aux = set()

        for i in range(0, N):
            aux.add(retrieved_documents[i])
            if retrieved_documents[i] in relevant_documents:
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
    #target = set(get_sentences(pre_process(target)))
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
    avg_eval['precision'] = round(
        sum(precision_values) / len(precision_values), 7)
    avg_eval['recall'] = round(sum(recall_values) / len(recall_values), 7)
    avg_eval['f1'] = round(sum(f1_values) / len(f1_values), 7)
    avg_eval['map'] = map(precision_values, recall_values, summaries, targets)

    """
    print()
    print(precision_values)
    print(recall_values)
    print(f1_values)
    print()
    """
    return avg_eval


def print_metrics(metrics):
    print('-----------------------------------------------')
    print("PRECISION =", metrics['precision'])
    print("RECALL =", metrics['recall'])
    print("F1 =", metrics['f1'])
    print("MAP =", metrics['map'])

    print('-----------------------------------------------\n')

#####################################################################################


def get_word_count(document):
    document = remove_punct([document.lower()])[0]
    words = nltk.word_tokenize(document)
    if '.' in words or '?' in words:
        words = remove_punct(words)
        words = list(filter(None, words))
    """
    words = []
    sentences = get_sentences(document)
    for sentence in sentences:
        proto_words = document.split(" ")
        for w in proto_words:
            toks = w.split('\n')
            for tok in toks:
                if len(tok) != 0:
                    words.append([tok])
    #words = document.split(" ")
    """
    words = nltk.FreqDist(words)

    for k, v in words.items():
        if k in ['?', '.']:
            print("In get_word_count:", k, v)

    return words


def get_global_frequency(documents):
    # documents -> sentences
    words_list = []
    for d in documents:
        words_list.append(get_word_count(d))
    frequency = nltk.FreqDist(flatten(words_list))

    return frequency

def get_doc_frequencies(documents):
    freq_each_doc = {}
    for i in range(0, len(documents)):
        document = documents[i]
        freq_each_doc[i] = get_word_count(document)
    return freq_each_doc


def normalized_tf(documents, freq_each_doc):
    tf = {}
    for doc in range(0, len(documents)):
        document = documents[doc]
        freq = freq_each_doc[doc]
        try:
            max_tf = freq.most_common(1)[0][1]
        except IndexError:
            # Caso exista uma lista [], o que significa uma frase só com 1 palavra
            # (se se estiver a calcular bigramas, etc.)
            max_tf = 0

        for term in freq:
            if (doc, term) not in tf:
                tf[(doc, term)] = 0
            if max_tf == 0:
                tf[(doc, term)] = 0
            else:
                tf[(doc, term)] = freq[term] / max_tf
    return tf


def log_scaled_idf(documents, freq_each_doc, freq_all_docs):

    # count é a contagem de todos os termos de todos os documentos
    # counts são as contagens dos documentos individuais

    N = len(documents)
    idf = {}
    for term in freq_all_docs:
        ni = 0
        for doc in range(0, len(documents)):
            document = documents[doc]
            try:
                freq = freq_each_doc[doc]
            except KeyError:
                print("[ERROR!] Document with id " +
                      str(doc) + " not found on counts")
                print(doc)
                print(counts)
                exit(-1)

            if term in freq.keys():
                ni += 1
        if ni == 0:
            idf[term] = 0
        else:
            idf[term] = math.log(N / ni)

    return idf


def vector_space_model(documents, is_doc=False):

    # All terms in the entire collection
    freq_all_docs = get_global_frequency(documents)

    # Each entry is the count of the terms in that document
    freq_each_doc = {}
    for i in range(0, len(documents)):
        document = documents[i]
        freq_each_doc[i] = get_word_count(document)
    tf = normalized_tf(documents, freq_each_doc)
    if is_doc:
        # documents -> lista só com um documento
        documents = remove_punct(get_sentences(documents[0]))
        freq_all_docs = get_global_frequency(documents)
        freq_each_doc = {}
        for i in range(0, len(documents)):
            document = documents[i]
            freq_each_doc[i] = get_word_count(document)

    # for k,v in freq_each_doc.items():
    #    print(k,v)

    idf = log_scaled_idf(documents, freq_each_doc, freq_all_docs)
    # pprint.pprint(idf)

    D = 1 if is_doc else len(documents)
    T = len(freq_all_docs)
    model = np.zeros((D, T))

    for d in range(0, D):
        for t in range(0, T):
            term = list(freq_all_docs.keys())[t]
            if (d, term) not in tf:
                continue
            model[d, t] = tf[(d, term)] * idf[term]
    return model


def simple_approach(document):
    # Devolve uma lista ORDENADA com as 5 primeiras frases 'relevantes'

    original_sentences = get_sentences(document)
    document = pre_process(document)
    sentences = remove_punct(get_sentences(document))

    """
    for i in range(0, len(original_sentences)):
        print("---------------------------------------------------")
        print(i, '\n')
        print(original_sentences[i], '\n')
        print(sentences[i])
        print("---------------------------------------------------\n")
    print()
    """


    sent_model = vector_space_model(sentences)
    #print("Sentence model:", sent_model.shape[0],"sentences,",sent_model.shape[1],"terms")

    doc_model = vector_space_model([document], is_doc=True)
    #print("Document model:", doc_model.shape[0],"documents,",doc_model.shape[1],"terms")

    similarity = cosine_similarity(sent_model, doc_model)
    top5 = get_top_N(similarity, 5)

    summary = []
    for (doc, sim) in top5:
        try:
            summary.append(original_sentences[doc])
        except IndexError:
            for i in range(0, len(original_sentences)):
                #print(i, original_sentences[i], sentences[i])
                print("---------------------------------------------------")
                print(i, '\n')
                print(original_sentences[i], '\n')
                print(sentences[i])
                print("---------------------------------------------------\n")
            print()
            print(doc, sentences[doc])
            print("[ERROR!] Error in getting the correspondent summary sentence")
            exit(-1)

    return summary


def test_simple_approach(documents, targets):
    summaries = []
    printProgressBar(0, len(documents), prefix='\nStatus:',
                     suffix='  DocID 0 of ' + str(len(documents)), length=20)

    for i in range(0, len(documents)):
        printProgressBar(i + 1, len(documents), prefix='Status:',
                         suffix='  DocID ' + str(i + 1) + ' of ' + str(len(documents)), length=20)
        document = documents[i]
        summaries.append(simple_approach(document))

    metrics = evaluate_collection(summaries, targets)
    print("Simple approach")
    print_metrics(metrics)

#####################################################################################


def test_alternative_approach(documents, targets):

    summaries = []

    freq_all_docs = get_global_frequency(documents)
    freq_each_doc = get_doc_frequencies(documents)
    processed_docs = [pre_process(doc) for doc in documents]
    idf = log_scaled_idf(processed_docs, freq_each_doc, freq_all_docs)

    summaries = []
    printProgressBar(0, len(documents), prefix='\nStatus:',
                     suffix='  DocID 0 of ' + str(len(documents)), length=20)
    for i in range(0,len(documents)):

        printProgressBar(i + 1, len(documents), prefix='Status:',
                         suffix='  DocID ' + str(i + 1) + ' of ' + str(len(documents)), length=20)

        document = documents[i]
        original_sentences = get_sentences(document)
        document = processed_docs[i]
        sentences = remove_punct(get_sentences(document))

        freq = freq_each_doc[i]
        doc_tf = {}
        max_freq = freq.most_common(1)[0][1]
        for term, count in freq.items():
            doc_tf[(i, term)] = count / max_freq

        ##### Isto faz sentido???? --v

        D = 1
        #T = len(idf)
        T = len(freq)                   #
        doc_model = np.zeros((D, T))
        for d in range(0,D):
            for t in range(0,T):
                #term = list(idf.keys())[t]
                term = list(freq.keys())[t] #
                if (d, term) not in doc_tf:
                    continue
                doc_model[d, t] = doc_tf[(d, term)] * idf[term]

        freq_each_sentence = get_doc_frequencies(sentences)
        sent_tf = {}
        for s in range(0,len(sentences)):
            freq = freq_each_sentence[s]
            try:
                max_freq = freq.most_common(1)[0][1]
            except IndexError:  #
                max_freq = 0    #
            for term, count in freq.items():
                if max_freq == 0:   #
                    sent_tf[(s,term)] = 0 #
                else:
                    sent_tf[(s, term)] = count / max_freq


        D = len(sentences)
        T = len(freq_each_doc[i])   #
        sent_model = np.zeros((D, T))
        for d in range(0,D):
            for t in range(0,T):
                term = list(freq_each_doc[i])[t]    #
                if (d, term) not in sent_tf:
                    continue
                sent_model[d, t] = sent_tf[(d, term)] * idf[term]


        similarity = cosine_similarity(sent_model, doc_model)
        top5 = get_top_N(similarity, 5)

        summary = []
        for (doc, sim) in top5:
            try:
                summary.append(original_sentences[doc])
            except IndexError:
                for i in range(0, len(original_sentences)):
                    #print(i, original_sentences[i], sentences[i])
                    print("---------------------------------------------------")
                    print(i, '\n')
                    print(original_sentences[i], '\n')
                    print(sentences[i])
                    print("---------------------------------------------------\n")
                print()
                print(doc, sentences[doc])
                print("[ERROR!] Error in getting the correspondent summary sentence")
                exit(-1)
        summaries.append(summary)

    metrics = evaluate_collection(summaries, targets)
    print("Alternative approach")
    print_metrics(metrics)

    return summaries

#####################################################################################


if __name__ == "__main__":

    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-l", "--limit", type=int, help="How many documents to use")
    args = parser.parse_args()

    if args.limit != None:
        limit = int(args.limit)
    else:
        limit = None

    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'
    documents = import_texts(collection_folder, limit=limit)
    targets = import_texts(target_folder, limit=limit)

    test_simple_approach(documents, targets)
    test_alternative_approach(documents, targets)
