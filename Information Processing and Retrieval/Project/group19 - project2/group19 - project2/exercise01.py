####
#### DUVIDA PERGUNTAR AO STOR SE VALE A PENA LIGAR OS GRAFOS QUE TEM OUTLINKS DE 0
####
import nltk
import numpy as np
from utils import *
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.feature_extraction.text import TfidfVectorizer
from operator import itemgetter, attrgetter, methodcaller

num_iterations = []

def create_graph(matrix, threshold=0.2):
	# Recebe uma matrix com a similaridade entre as sentences
    # Retorna um dicionario em que a key e a sentence{i} e o par e uma lista com as ligacoes que sentence{i} faz com as outras sentences
    graph = {}
    aboveThresholdElements = np.argwhere(np.array(matrix) > threshold)

    for el in aboveThresholdElements:
        if(el[0] == el[1]):
            continue
        if el[0] not in graph:
            graph[el[0]] = [(el[1],matrix[el[0]][el[1]])]
        else:
            graph[el[0]].append((el[1],matrix[el[0]][el[1]]))

    for el in range(len(matrix)):
        if el not in graph:
            graph[el] = [(-1, None)]
    return graph

def compute_page_rank(graph, damping=0.15, iterations=50, convergenceFactor=0.0001):
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

def compute_out_degree(graph):
    N = len(graph.keys())
    out_degree = np.zeros(N)

    for e in range(N):
        out_degree[e] = len(graph[e])

    return out_degree

def calculate_similarity(sentences):
	# Recebe uma lista de sentences
    # Retorna uma matrix com a similaridade entre as sentences
    matrix = []

    vectorizer = TfidfVectorizer()
    weights_matrix = vectorizer.fit_transform(sentences)
    for i in range(0,len(sentences)):
        matrix.append(cosine_similarity(weights_matrix[i], weights_matrix).tolist()[0])

    return matrix

def summarize_ex1(document, top=5):
    #sentences = [line for line in nltk.sent_tokenize(document)]
    original_sentences = get_sentences(document)
    sentences = pre_process(original_sentences)

    """
    for i in range(len(original_sentences)):
        print("------------------------------------------------------------------")
        print(original_sentences[i])
        print(sentences[i])
        print("------------------------------------------------------------------")
    """
    similarity = calculate_similarity(sentences)
    graph = create_graph(similarity, threshold=0.2)
    ranks = compute_page_rank(graph)



    dic = {}
    for i in range(len(ranks)):
        if ranks[i] == -1: continue
        dic[original_sentences[i]] = ranks[i]
    ordered_rank = sorted(dic.items(), key=itemgetter(1), reverse=True)

    summary = [x[0] for x in ordered_rank[:top]]


    return summary, num_iterations

def main():
    print("Exercise 1\n")
    word_file = open("testSet.txt", 'r')
    document = word_file.read()
    summary,_ = summarize_ex1(document)
    pprint(summary)

    """
    sentences = [line for line in nltk.sent_tokenize(document)]
    similarity = calculate_similarity(sentences)
    graph = create_graph(similarity, threshold=0.2)
    ranks = compute_page_rank(graph)
    """



if __name__ == "__main__":
    main()
