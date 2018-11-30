from exercise_02 import *
from utils import *
from nltk.corpus import floresta
from nltk.tokenize import word_tokenize
from nltk.tokenize import RegexpTokenizer
from nltk.tree import Tree
from nltk.chunk import RegexpParser
import pickle
import time
import sys
import os
from sklearn.feature_extraction.text import CountVectorizer


def simplify_tag(t):
    if "+" in t:
        return t[t.index("+")+1:]
    else:
        return t

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

def get_n_grams(text, n):
    # Recebe uma string e um inteiro
    # Devolve uma lista com tuplos
    text = remove_punct([text.lower()])
    words = nltk.word_tokenize(text[0])
    return list(nltk.ngrams(words,n))

def get_training():
    training = floresta.tagged_sents()[:100]
    target = floresta.tagged_sents()[100:]
    return training, target

# RECEIVES A TAGGED SENTENCE
def generate_chunks(tagged_sent, expression=r'CHUNK: {(<adj>* <n.*>+ <prp>)? <adj>* <n.*>+}'):
    chunks = []
    chunkParser = RegexpParser(expression)
    try:
        if len(tagged_sent) == 0:
            tree = Tree('S', [])
        else:
            tree = chunkParser.parse(tagged_sent, trace=0)
        for subtree in tree.subtrees():
            if subtree.label() == "CHUNK":
                chunks.append(subtree.leaves())
    except ValueError:
        chunks = []
    return chunks

# RECEIVES A LIST OF SENTENCES FROM A DOC
def tag_document(sentences, tagger):
    #RETURNS A LIST WITH SENTENCES TAGGED
    return [tagger.tag(text.split()) for text in sentences]

def create_tagger(tagger='Perceptron'):
    training,_ = get_training()
    return get_pos_tagger(training, tagger=tagger)

###############################################################################################################

def get_ngrams_count(documents, n):

    ngrams = []
    for document in documents:
        ngrams.append(get_n_grams(document,n))
    ngrams = flatten(ngrams)

    count = nltk.FreqDist(ngrams)

    return count


def global_count(documents, ngrams=(1, 1), chunks=False):
    ngrams_list = []
    for ngram in range(ngrams[0],ngrams[1]+1):
        for d in documents:
            ngrams_list.append(get_n_grams(d,ngram))

    if chunks != False:
        tagged_documents = tag_document(documents,chunks)
        doc_chunks = [generate_chunks(sent) for sent in tagged_documents]
        chunk_list = []
        for chunk in doc_chunks[0]:
            if len(chunk) > ngrams[1]:
                chunk_list.append(tuple([t[0] for t in chunk ]))
        if len(chunk_list) > 0:
            ngrams_list.append(chunk_list)

    count = nltk.FreqDist(flatten(ngrams_list))
    return count


def normalized_tf(documents, n, counts=None):
    tf = {}
    for doc in range(0,len(documents)):
        document = documents[doc]
        if counts == None:
            count = global_count([document], ngrams=(1,n), chunks=chunks)
        else:
            count = counts[doc]
        try:
            max_tf = count.most_common(1)[0][1]
        except IndexError:
            # Caso exista uma lista [], o que significa uma frase só com 1 palavra
            # (se se estiver a calcular bigramas, etc.)
            max_tf = 0

        for term in count:
            if (doc, term) not in tf:
                tf[(doc, term)] = 0
            if max_tf == 0:
                tf[(doc, term)] = 0
            else:
                tf[(doc, term)] = count[term] / max_tf
    return tf


def calculate_bm25_tf_formula(freq, avdl, num_words, k1=1.2, b=0.75):
    top = freq * ( k1 + 1 )
    bot = freq + ( k1 * ( 1 - b + (b * (num_words / avdl))))
    return top / bot

def avgdl(documents):
    av = 0
    for d in documents:
        av += len(word_tokenize(d))
    return ( av/len(documents) )

def bm25_tf_formula(documents, n, counts=None, chunks=False):
    tf = {}
    avdl = avgdl(documents)
    for doc in range(0,len(documents)):
        document = documents[doc]
        if counts == None:
            count = global_count([document], ngrams=(1,n), chunks=chunks)
        else:
            count = counts[doc]
        try:
            max_tf = count.most_common(1)[0][1]
        except IndexError:
            max_tf = 0

        num_words = 0
        for k,v in count.items():
            num_words += v

        for term in count:
            if (doc, term) not in tf:
                tf[(doc, term)] = 0
            if max_tf == 0:
                tf[(doc, term)] = 0
            else:
                freq = count[term]
                tf[(doc, term)] = calculate_bm25_tf_formula(count[term],avdl, num_words)
    return tf


def bm25_idf_formula(documents, n, counts=None, count=None, chunks=False):

    # count é a contagem de todos os termos de todos os documentos
    # counts são as contagens dos documentos individuais

    N = len(documents)

    if count == None:
        count = global_count(documents, ngrams=(1,n), chunks=chunks)
    idf = {}
    for term in count:
        ni = 0
        for doc in range(0, len(documents)):
            document = documents[doc]

            if counts == None:
                count_doc = global_count([document], ngrams=(1,n), chunks=chunks)
            else:
                try:
                    count_doc = counts[doc]
                except KeyError:
                    print("[ERROR!] Document with id " + str(doc) + " not found on counts")
                    print(doc)
                    print(counts)
                    sys.exit(-1)

            if term in count_doc.keys():
                ni += 1
        if ni == 0:
            idf[term] = 0
        else:
            idf[term] = math.log( (N - ni + 0.5) / (ni + 0.5) )

    return idf

def log_scaled_idf(documents, n, counts=None, count=None, chunks=False):

    # count é a contagem de todos os termos de todos os documentos
    # counts são as contagens dos documentos individuais

    N = len(documents)

    if count == None:
        count = global_count(documents, ngrams=(1,n), chunks=chunks)
    idf = {}
    for term in count:
        ni = 0
        for doc in range(0, len(documents)):
            document = documents[doc]

            if counts == None:
                count_doc = global_count([document], ngrams=(1,n), chunks=chunks)
            else:
                try:
                    count_doc = counts[doc]
                except KeyError:
                    print("[ERROR!] Document with id " + str(doc) + " not found on counts")
                    print(doc)
                    print(counts)
                    exit(-1)

            if term in count_doc.keys():
                ni += 1
        if ni == 0:
            idf[term] = 0
        else:
            idf[term] = math.log(N/ni)

    return idf

def vector_space_model(documents, n, is_doc=False, bm25=False, chunks=False):

    total_count = global_count(documents, ngrams=(1,n), chunks=chunks)

    counts = {}
    for doc in range(0, len(documents)):
        document = documents[doc]
        counts[doc] = global_count([document], ngrams=(1,n), chunks=chunks)

    if bm25:
        tf = bm25_tf_formula(documents, n)
    else:
        tf = normalized_tf(documents, n, counts=counts)


    if is_doc:
        documents = remove_punct(get_sentences(documents[0]))
        total_count = global_count(documents, ngrams=(1,n), chunks=chunks)
        for doc in range(0, len(documents)):
            document = documents[doc]
            counts[doc] = global_count([document], ngrams=(1,n), chunks=chunks)
    if bm25:
        idf = bm25_idf_formula(documents, n, count=total_count, counts=counts)
    else:
        idf = log_scaled_idf(documents, n, count=total_count, counts=counts)

    if is_doc:
        D = 1
    else:
        D = len(documents)

    T = len(total_count)
    model = np.zeros((D, T))


    for i in range(0, D):
        for j in range(0, T):
            ngram = list(total_count.keys())[j]
            if (i, ngram) not in tf:
                continue
            tf_i = tf[(i,ngram)]
            idf_i = idf[ngram]
            model[i,j] = tf_i * idf_i
    return model

def simple_approach(document, n, bm25=False, chunks=False):
    #Devolve uma lista ORDENADA com as 5 primeiras frases 'relevantes'

    original_sentences = get_sentences(document)

    document = pre_process(document)
    sentences = remove_punct(get_sentences(document))

    sent_model = vector_space_model(sentences, n, bm25=bm25, chunks=chunks)
    doc_model = vector_space_model([document], n, is_doc=True, bm25=bm25, chunks=chunks)
    similarity = cosine_similarity(sent_model, doc_model)
    top5 = get_top_N(similarity, 5)


    summary = []
    for (doc, sim) in top5:
        try:
            summary.append(original_sentences[doc])
        except IndexError:
            print("[ERROR!] Error in getting the correspondent summary sentence")
            exit()

    return summary

def test_simple_approach(n, limit=None, bm25=False, chunks=False):

    print("\nSimple approach using (1," + str(n) + ")-grams bm25="+str(bm25)+" chunks="+str(chunks))
    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'
    documents = import_texts(collection_folder, limit=limit)
    summaries = []

    tagger = False
    if chunks:
        print("Generating Tagger... (this can take a while)")
        tagger = create_tagger(tagger='Bigram')


    printProgressBar(0,len(documents), prefix='\nStatus:' , suffix='  DocID 0 of '+str(len(documents)), length=20 )
    for i in range(0,len(documents)):
        printProgressBar(i+1, len(documents), prefix='Status:',suffix='  DocID '+str(i+1)+' of '+str(len(documents)), length=20)
        document = documents[i]
        summaries.append(simple_approach(document, n, bm25, chunks=tagger))

    targets = import_texts(target_folder, limit=limit)
    evaluation = evaluate_collection(summaries, targets)
    print_metrics(evaluation)
###############################################################################################################


if __name__ == "__main__":
    test_simple_approach(2, limit=None, bm25=True, chunks=True)
