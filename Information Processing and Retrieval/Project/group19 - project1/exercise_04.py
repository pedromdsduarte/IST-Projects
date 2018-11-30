from exercise_02 import *
from exercise_03 import *
import operator
import numpy as np
import csv
import pprint
import sys

def new_method(document, n, top, bm25=False, lam=0.5):

    original_sentences = get_sentences(document)

    document = pre_process(document)
    sentences = remove_punct(get_sentences(document))

    sent_model = vector_space_model(sentences, n, bm25=bm25)
    doc_model = vector_space_model([document], n, is_doc=True, bm25=bm25)
    similarity = cosine_similarity(sent_model, doc_model)

    S = []   # indices das frases que ja foram escolhidas

    for rank in range(0,top):
        mmr = {}
        for s in range(0,len(sentences)):
            sum_sim = 0
            for v in S:
                sum_sim += cosine_similarity([sent_model[s]], [sent_model[v]])[0]
            mmr[s] = (1-lam) * similarity[s] - lam * sum_sim
        sentence = max(mmr, key=mmr.get)
        S.append(sentence)
    summary = [original_sentences[sentence] for sentence in S]
    return summary

def choose_first_sentences(document, top):
    return get_sentences(document)[:top]

def search(initial_parameter=0.0, n=1, bm25=False, step=0.1, doc_limit=None):

    top = 5
    limit = doc_limit
    use_bm25 = bm25
    n = n

    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'
    documents = import_texts(collection_folder, limit=limit)
    targets = import_texts(target_folder, limit=limit)

    values_to_search = np.round(np.arange(0,1+step,step),2).tolist()
    results = {}

    print("[SEARCH] Executing search on values", values_to_search,
                "with step =",step,
                "and", len(documents),"documents")
    for i in range(0,len(values_to_search)):
        value = values_to_search[i]
        print("\t["+str(round(100*i*step))+"%] Using lambda = " + "{0:.2f}".format(value),end='')
        mmr_eval = evaluate_collection(
                            get_summaries(documents, n, top, bm25=use_bm25, lam=value),
                            targets)
        results[value] = round(mmr_eval['map'],5)
        print(" ->", results[value])

    base_name = 'search-results'
    score_name = '-bm25' if use_bm25 else '-tfidf'
    ndocs_name  = '-' + str(len(documents)) + 'docs'
    step_name  = '-' + str(step) + 'step'
    filename = base_name + score_name + ndocs_name + step_name + ".csv"
    with open(filename,'w', newline='') as f:
        f.write('lambda,MAP\n')
        writer = csv.writer(f)
        writer.writerows(results.items())

    return max(results, key=results.get)

def get_summaries(documents, n, top, bm25=False, lam=0.5):
    summaries = []
    for i in range(0, len(documents)):
        document = documents[i]
        #print("[MMR] Document " + str(i+1) + " of " + str(len(documents)), '\r',end='')
        summaries.append(new_method(document, n, top, bm25=bm25, lam=lam))
    #print()
    return summaries

if __name__ == "__main__":
    top = 5
    limit = None
    use_bm25 = False

    # Previously found to be the optimal value.
    # To redefine, use the option -search
    lam = 0.1
    n = 1 # Use 1 for unigrams, 2 for bigrams, ...


    collection_folder = 'TeMario\Textos-fonte\Textos-fonte com titulo'
    target_folder = 'TeMario\Sumarios\Extratos ideais automaticos'
    documents = import_texts(collection_folder, limit=limit)
    targets = import_texts(target_folder, limit=limit)

    first_top_summaries = []
    for i in range(0, len(documents)):
        print("[FIRST-"+str(top)+"] Document " + str(i+1) + " of " + str(len(documents)), '\r',end='')
        document = documents[i]
        first_top_summaries.append(choose_first_sentences(document, top))
    print()

    print("Simple procedure that selects the first " + str(top) + " sentences")
    first_top_eval = evaluate_collection(first_top_summaries, targets)
    print_metrics(first_top_eval)
    print()

    if "-search" in sys.argv[1:]:
        step = 0.1
        doc_limit = None
        try:
            if sys.argv[2].startswith("-step"):
                step = float(sys.argv[2].split("=")[1])
        except IndexError:
            pass

        try:
            if sys.argv[3].startswith("-docs"):
                doc_limit = int(sys.argv[3].split("=")[1])
        except IndexError:
            pass
        lam = search(step=step, n=1, bm25=use_bm25, doc_limit=doc_limit)
        print("[SEARCH] Found optimal lambda:", lam)


    mmr_summaries = get_summaries(documents, n, top, bm25=use_bm25, lam=lam)
    print("Sophisticated procedure that maximizes the MMR function (lambda="+str(lam)+"), using (1," \
            + str(n) + ")-grams and " + ("BM25" if use_bm25 else "TF-IDF"))
    mmr_eval = evaluate_collection(mmr_summaries, targets)
    print_metrics(mmr_eval)
