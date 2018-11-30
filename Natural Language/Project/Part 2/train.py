#!/usr/bin/python3

import nltk
import os
import re
import numpy as np
import pandas as pd
nltk.download('punkt')
nltk.download('stopwords')


texts_avg = {}
texts_std = {}
texts_punct = {}
texts_author = {}


metric_dict = {"Avg Sentence Length" : texts_avg, \
                "Sentence Std" : texts_std, \
                "Avg Punctuation" : texts_punct}

metrics = ["Avg Sentence Length","Sentence Std","Avg Punctuation"]


def computeFeatures(id, text, author):
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
        texts_author[id] = author

def exportCsv(output):
    dict = {}
    authorlist = []
    autcompleted = 0
    for metric_name in metrics:
        id_list = sorted(metric_dict[metric_name])
        metric = metric_dict[metric_name]
        datalist = []
        for id in id_list:
            datalist.append(metric[id])
            if not autcompleted:
                authorlist.append(texts_author[id])
        autcompleted = 1
        dict[metric_name] = datalist

    dict["Author"] = authorlist

    dataframe = pd.DataFrame(dict)
    dataframe.to_csv(output,index=False,columns=metrics+['Author'])

###############################################################################

for authordir in os.listdir("treino-normalizado/"):
    author = authordir.replace("-normalizado", "")
    for textfile in os.listdir("treino-normalizado"+"/"+authordir):
        if textfile.endswith("-normalizado"):
            text = open("treino-normalizado/"+authordir+"/"+textfile,'r')
            computeFeatures(textfile, text, author)

exportCsv("training-data.csv")
