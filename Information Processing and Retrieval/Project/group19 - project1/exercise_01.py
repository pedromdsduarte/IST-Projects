import nltk
import re
import numpy as np
import pprint
from sklearn.feature_extraction.text import TfidfVectorizer
from nltk.corpus import stopwords
from sklearn.metrics.pairwise import cosine_similarity
from operator import itemgetter, attrgetter, methodcaller
import string
import math
import scipy


np.set_printoptions(precision=3, linewidth=200)
#########################################################################

def get_terms(documents):
	count = {}
	terms = set()

	for i in range(0,len(documents)):
		document = documents[i].lower()
		#print(i, document)

		# Remove pontuacao
		doc_terms = nltk.word_tokenize(document.translate(str.maketrans('', '', string.punctuation)))
		terms = set.union(terms, set(doc_terms))
		count_doc = {}

		for term in doc_terms:
			if term not in count_doc:
				count_doc[term] = 0
			count_doc[term] += 1
		count[i] = count_doc
	return count, terms




def normalized_tf(documents):
	# Recebe uma lista de documentos
	# Retorna um dicionario
	#print("DOCUMENTS =",documents)
	tf = {}
	count,_ = get_terms(documents)
	#terms = set()

	for doc in range(0,len(count.keys())):
		#pprint.pprint(documents[doc], width=200)
		max_tf = max(count[doc].items(), key=itemgetter(1))[1]
		for term in count[doc].keys():
			if (doc, term) not in tf:
				tf[(doc, term)] = 0
			tf[(doc, term)] = count[doc][term] / max_tf
	return tf

def log_scaled_idf(documents):
	N = len(documents)
	count, terms = get_terms(documents)
	idf = {}

	for term in terms:
		n = 0
		for doc in count.keys():
			if term in count[doc].keys():
				n += 1
		if n == 0:
			idf[term] = 0
		else:
			idf[term] = math.log(N/n)
	return idf


def compute_model(documents, tf, idf):

	N = len(documents)
	model = {}
	_, terms = get_terms(documents)


	doc_weight = {}
	for i in range(0,N):
		doc_weight[i] = {}

		for term in terms:
			weight = 0


			if (i, term) in tf.keys():
				weight = tf[(i, term)] * idf[term]
			doc_weight[i][term] = weight

		model[i] = doc_weight[i]
	return model



def get_model_list(model):

	N = len(model.keys())

	terms = model[0].keys()
	terms_list = sorted(list(terms))
	result = np.zeros((N, len(terms_list)))

	for term_i in range(0,len(terms_list)):
		for doc_i in range(0,N):
			### ACEDER AO INDEX
			term = terms_list[term_i]
			result[doc_i, term_i] = model[doc_i][term]
	return result

def main():
	#########################################################################


	word_file = open("testSet.txt", 'r')
	document = word_file.read()

	print("DOCUMENT")
	print("---------------------------------------------")
	print(document)
	print("---------------------------------------------\n")



	###################################################
	## 1. Vector space model of the sentences
	###################################################

	sentences = [line for line in nltk.sent_tokenize(document)]

	print("SENTENCES")
	print("---------------------------------------------")
	pprint.pprint(sentences)
	print("---------------------------------------------\n")

	idf = log_scaled_idf(sentences)
	tf = normalized_tf(sentences)

	sent_model = compute_model(sentences, tf, idf)
	print("Model of the sentences")
	sent_model = get_model_list(sent_model)
	for doc in sent_model:
		print(doc)
	print()
	###################################################
	## 2. Vector space model of the document as a whole
	###################################################

	tf = normalized_tf([document])
	doc_model = compute_model([document], tf, idf)
	print("Model of the document")
	doc_model = get_model_list(doc_model)
	for doc in doc_model:
		print(doc)
	print()


	###################################################
	## 3. Scoring of each sentence against the document
	###################################################


	print("\nSIMILARITY")
	print("---------------------------------------------")
	similarity = cosine_similarity(sent_model, doc_model)
	print(similarity)
	print("---------------------------------------------\n")


	###################################################
	## 4. Ranking of the top3 sentences
	###################################################

	rank = {}
	for i in range(0,len(similarity)):
		rank[i] = similarity[i]

	ordered_rank = sorted(rank.items(), key=itemgetter(1), reverse = True)
	top3 = ordered_rank[:3]

	###################################################
	## 5. Return of the summary to the user
	###################################################


	print("\nSUMMARY (top3 sentences)")
	print("---------------------------------------------")
	for i in range(0, len(top3)):
		print("\"" + sentences[top3[i][0]] + "\"")
	print("---------------------------------------------\n")

#main()
if __name__ == "__main__":
       main()
