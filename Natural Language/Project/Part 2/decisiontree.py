#!/usr/bin/python3

import nltk
import os
import re
import numpy as np
import pandas as pd
import subprocess

import warnings
warnings.filterwarnings("ignore")

from sklearn.tree import DecisionTreeClassifier, export_graphviz
from sklearn.grid_search import GridSearchCV
from sklearn.grid_search import RandomizedSearchCV
from sklearn.cross_validation import  cross_val_score

from operator import itemgetter
from scipy.stats import randint

import warnings
warnings.filterwarnings("ignore")

##############################################################################

FEATURES_NUM = 3
TEST_FOLDER = "teste/"


texts_avg = {}
texts_std = {}
texts_punct = {}


metric_dict = {"Avg Sentence Length" : texts_avg, \
                "Sentence Std" : texts_std, \
                "Avg Punctuation" : texts_punct}

metrics = ["Avg Sentence Length","Sentence Std","Avg Punctuation"]

encoded_authors = {}
encoded_texts = {}

##############################################################################

def buildDataFrame(data):
    if os.path.exists(data):
        dataframe = pd.read_csv(data,index_col=False)
    else:
        print("[X] ERROR when building dataframe.Aborting.")
        exit()
    return dataframe

def encodeTarget(df, target_column, dict_enc):
    df_mod = df.copy()
    targets = df_mod[target_column].unique()
    map_to_int = {}
    for n, name in enumerate(targets):
        map_to_int[name] = n
        dict_enc[n] = name
    df_mod["Target"] = df_mod[target_column].replace(map_to_int)
    return (df_mod, targets)

def computeFeatures(id, text):
        raw_text = text.read()

        sent_tokenizer = nltk.data.load('tokenizers/punkt/portuguese.pickle')
        sentences = sent_tokenizer.tokenize(raw_text)

        word_num = []
        punct_num = []

        sent_num = 0
        avg_sent_length = 0
        avg_punct = 0
        std_sent = 0

        for sent in sentences:
                sent_words = re.findall(r"[\w'-]+",sent)
                word_num.append(len(sent_words))
                punct = sent.count(',') + sent.count(':') + sent.count(';')
                punct_num.append(punct)
        sent_num = len(word_num)

        if sent_num != 0:
                avg_sent_length = np.mean(word_num)
                std_sent = np.std(word_num)
                avg_punct = np.mean(punct)

        texts_avg[id] = avg_sent_length
        texts_std[id] = std_sent
        texts_punct[id] = avg_punct

def exportCsv(output):
    dict = {}
    for metric_name in metrics:
        text_list = sorted(metric_dict[metric_name])
        metric = metric_dict[metric_name]
        datalist = []
        for text in text_list:
            datalist.append(metric[text])
        dict[metric_name] = datalist

    dict["Text"] = text_list

    dataframe = pd.DataFrame(dict)
    dataframe.to_csv(output,index=False,columns=metrics+['Text'])

##############################################################################
                            ###EVALUATION FUNCTIONS###

def report(grid_scores, n_top=3):
    top_scores = sorted(grid_scores,
                        key=itemgetter(1),
                        reverse=True)[:n_top]
    return top_scores[0].parameters

def run_gridsearch(X, y, clf, param_grid, cv=5):
    grid_search = GridSearchCV(clf,
                               param_grid=param_grid,
                               cv=cv)
    grid_search.fit(X, y)

    top_params = report(grid_search.grid_scores_, 3)
    return top_params

def run_randomsearch(X, y, clf, para_dist, cv=5, n_iter_search=20):
    random_search = RandomizedSearchCV(clf,
                        param_distributions=param_dist,
                        n_iter=n_iter_search)

    random_search.fit(X, y)
    top_params = report(random_search.grid_scores_, 3)
    return top_params

##############################################################################

df_train = buildDataFrame("training-data.csv")
df2_train, targets = encodeTarget(df_train, "Author", encoded_authors)

features = list(df2_train.columns[:FEATURES_NUM])
y = df2_train["Target"]
x = df2_train[features]

dt = DecisionTreeClassifier()


#---------------------------------------------
                #EVALUATION#

param_grid = {"criterion": ["gini", "entropy"],
              "min_samples_split": [2, 10, 20],
              "max_depth": [None, 2, 5, 10],
              "min_samples_leaf": [1, 5, 10],
              "max_leaf_nodes": [None, 5, 10, 20],
              }
"""
param_dist = {"criterion": ["gini", "entropy"],
              "min_samples_split": randint(1, 20),
              "max_depth": randint(1, 20),
              "min_samples_leaf": randint(1, 20),
              "max_leaf_nodes": randint(2, 20)}
"""


test_dt = DecisionTreeClassifier()

#best_parameters = run_randomsearch(x, y, dt, param_dist, cv=10, n_iter_search=1000)
best_parameters = run_gridsearch(x, y, test_dt, param_grid, cv=10)

dt = DecisionTreeClassifier(**best_parameters)

#---------------------------------------------

dt.fit(x, y)

for folder in os.listdir(TEST_FOLDER):
    if not folder.startswith('.'):
        file = TEST_FOLDER+folder
        for text_file in os.listdir(file):
            text_path = file+"/"+text_file
            text = open(text_path,'r')
            id = folder+"/"+text_file
            computeFeatures(id, text)

exportCsv("samples.csv")

df = buildDataFrame("samples.csv")
df2, targets = encodeTarget(df, "Text", encoded_texts)
features = list(df2.columns[:FEATURES_NUM])

sample = df2[features]


res = dt.predict(sample)

#Print result
print("\n\t\t*RESULTADOS: ÁRVORE DE DECISÃO*")
print("-------------------------------------------------------------------")
for i in range(0,len(res)):
    print(sorted(texts_avg)[i],"\t-->", encoded_authors[res[i]])
print("-------------------------------------------------------------------\n")
