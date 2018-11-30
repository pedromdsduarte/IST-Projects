#!/usr/bin/python3

import os
import nltk
import re
from collections import OrderedDict
from random import randint
from prettytable import PrettyTable
import pandas as pd

STOPWORDS = []
TOP = 50
authors_mfw = {}
tests_mfw = {}
best_fit = {}
TEST_FOLDER = "teste/"

def normalize(id, text, isauthor=True):
    infile = text
    normtext = ''

    fin = open(infile)
    raw = fin.read()
    raw = re.sub(' ','\n',raw)
    raw = re.sub(' +',' ',raw) #remove extra whitespaces

    words = re.findall(r"[\w'-]+",raw)

    for word in words:
        word = word.lower()
        if word not in STOPWORDS:
            normtext += word+"\n"

    fin.close()
    return normtext

def computeMostFrequentWords(normtext,top,id,isauthor=True):


    words = re.findall(r"[\w'-]+",normtext)
    fd = nltk.FreqDist(words)
    mfw = {}

    for word in fd.most_common(top):
        mfw[word[0].lower()] = word[1]
    mfw = OrderedDict(sorted(mfw.items(), key=lambda t: t[1],reverse=True)) #Order by count

    if isauthor:
        authors_mfw[id] = mfw
    else:
        tests_mfw[id] = mfw



def readStopWords(file):
    stoplist = []
    fin = open(file,'r')
    for sw in fin.readlines():
        sw = sw.strip("\n\t\r ")
        stoplist.append(sw)
    return stoplist



STOPWORDS = readStopWords("stopwords.txt")
for authorfolder in os.listdir("treino-normalizado"):
    text = authorfolder+"/concatenated.txt"
    author = authorfolder.replace("-normalizado","")
    text_path = "treino-normalizado/"+text



    normalizedtext = normalize(author,text_path)
    computeMostFrequentWords(normalizedtext,TOP,author)


for folder in os.listdir(TEST_FOLDER):
    if not folder.startswith('.'):
        file = TEST_FOLDER+folder
        for text_file in os.listdir(file):
            text_path = file+"/"+text_file
            id = folder+"/"+text_file

            normalizedtext = normalize(id,text_path,isauthor=False)
            computeMostFrequentWords(normalizedtext,25,id,isauthor=False)


best_author = {}
for id in tests_mfw:
    best = 0
    for author in authors_mfw:
        ukn_text = set(tests_mfw[id])
        aut_text = set(authors_mfw[author])
        common_words = ukn_text.intersection(aut_text)
        number_common = len(common_words)

        if number_common > best:
            best = number_common
            best_author[id] = author
        elif number_common == best:
            if randint(0,1):
                best_author[id] = author


print("\n\t\t*RESULTADOS: PALAVRAS MAIS FREQUENTES*")
print("-------------------------------------------------------------------")
for id in sorted(best_author):
    print(id,"\t-->",best_author[id])
print("-------------------------------------------------------------------\n")
