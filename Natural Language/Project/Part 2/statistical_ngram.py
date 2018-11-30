#!/usr/bin/python3

import os
from prettytable import PrettyTable
TEST_FOLDER = "teste-ngram-probs/"
teste = {}
for folder in os.listdir("teste-ngram-probs/"):
    if not folder.startswith('.'):
        teste[folder] = {}
        #print("folder",folder)
        folder_nwords = TEST_FOLDER+"/"+folder
        for nwords_dir in os.listdir(folder_nwords ):
            if not nwords_dir.startswith('.'):
                teste[folder][nwords_dir] = {}
                #print("\tnwords_dir",nwords_dir)
                author_dir = folder_nwords + "/" + nwords_dir
                for author in os.listdir(author_dir):
                    if not author.startswith('.'):
                        teste[folder][nwords_dir][author.replace("-normalizado", "")] = {}
                        fileprob = author_dir + "/" + author
                        for probfile in os.listdir(fileprob):
                            if not probfile.startswith('.'):
                                fp = open(fileprob+"/"+probfile,'r')
                                content = fp.readlines()[1]
                                teste[folder][nwords_dir][author.replace("-normalizado", "")][probfile] = float(content.split(' ')[3])
                                fp.close()
                          
res = {}
for ngram in ['bigram-smooth.txt','bigram-no_smooth.txt','unigram-smooth.txt','unigram-no_smooth.txt']:
    val = None
    aut = None
    res[ngram] = {}
    for k,v in teste.items():
        res[ngram][k] = {}
        #print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$\nWords: ",k)
        for p,text in v.items():
            res[ngram][k][p] = {}
            #print("############################\nText: ",p)
            val = None
            aut = None
            for z,author in text.items():
                #print("\tkey ",z, "dic", author)
                #print("\tv: ",val, "\tauthor val: ",author[ngram],"\tauthor: ",z)
                if val == None :
                    val = author[ngram]
                    aut = z
                    continue
                elif author[ngram] > val:
                    val = author[ngram]
                    aut = z
                    continue
            #print("\n\t\tMenor valor: ",val,"\tAutor: ",aut)
            res[ngram][k][p][aut] = val


t = PrettyTable(['Texto', '500 Palavras', '1000 Palavras'])
print('[-]Bigram No Smooth')
for (k1,dic1), (k2,dic2) in zip(res['bigram-no_smooth.txt']['500Palavras'].items(),res['bigram-no_smooth.txt']['1000Palavras'].items()):
    t.add_row([k1,list(dic1.keys())[0],list(dic2.keys())[0]])
print(t,"\n")

t = PrettyTable(['Texto', '500 Palavras', '1000 Palavras'])
print('[-]Unigram No Smooth')
for (k1,dic1), (k2,dic2) in zip(res['unigram-no_smooth.txt']['500Palavras'].items(),res['unigram-no_smooth.txt']['1000Palavras'].items()):
    t.add_row([k1,list(dic1.keys())[0],list(dic2.keys())[0]])
print(t,"\n")

t = PrettyTable(['Texto', '500 Palavras', '1000 Palavras'])
print('[-]Bigram Smooth')
for (k1,dic1), (k2,dic2) in zip(res['bigram-smooth.txt']['500Palavras'].items(),res['bigram-smooth.txt']['1000Palavras'].items()):
    t.add_row([k1,list(dic1.keys())[0],list(dic2.keys())[0]])
print(t,"\n")

t = PrettyTable(['Texto', '500 Palavras', '1000 Palavras'])
print('[-]Unigram Smooth')
for (k1,dic1), (k2,dic2) in zip(res['unigram-smooth.txt']['500Palavras'].items(),res['unigram-smooth.txt']['1000Palavras'].items()):
    t.add_row([k1,list(dic1.keys())[0],list(dic2.keys())[0]])
print(t)