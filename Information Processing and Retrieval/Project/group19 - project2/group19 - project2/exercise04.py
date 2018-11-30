from urllib import request
import lxml.etree as etree
import xml.etree.ElementTree as ET
import re
from bs4 import BeautifulSoup
from exercise01 import summarize_ex1
from nltk.corpus import stopwords
from textblob import TextBlob
from textblob import Word
from operator import itemgetter, attrgetter, methodcaller
from dominate.util import raw
from utils import *

from exercise02 import summarize
from exercise03 import *

# python -m pip install dominate
import dominate
from dominate.tags import *

NEWS_SOURCES = {
    'New York Times' : 'http://www.nytimes.com/services/xml/rss/nyt/World.xml',
    'CNN' : 'http://rss.cnn.com/rss/edition_world.rss',
    'Washington Post' : 'http://feeds.washingtonpost.com/rss/world',
    'Los Angeles Times' : 'http://www.latimes.com/world/rss2.0.xml'

    # SPORTS FEEDS #
    #'Sports LA Times' : 'http://www.latimes.com/sports/rss2.0.xml',
    #'Sports CNN' : 'http://rss.cnn.com/rss/edition_sport.rss',
    #'Sports NY Times' : 'http://rss.nytimes.com/services/xml/rss/nyt/Sports.xml'
}

STOPWORDS = set(stopwords.words('english'))

# ----------------------------------------------------------------------------#
def clean_xml_text(xml_text):
    if xml_text == None:
        return xml_text

    cleaned_text = xml_text

    # ------------------------------------------------------------------------ #
    # Removing paragraph tags

    soup = BeautifulSoup(cleaned_text, "html.parser")
    if len(soup.select("p")) > 0:
        cleaned_text = ' '.join([link.get_text() for link in soup.select("p")])

    # ------------------------------------------------------------------------ #
    # Removing image tags

    soup = BeautifulSoup(cleaned_text, "html.parser")
    p = re.compile(r'<img.*?/>')
    cleaned_text = p.sub('', cleaned_text)

    # ------------------------------------------------------------------------ #
    # Remove extra whitespaces

    cleaned_text = cleaned_text.strip()

    if cleaned_text == None:
        return ''


    #print("-------------------------")
    #print(xml_text)
    #print(cleaned_text)
    #print("--------------------------\n")
    return cleaned_text

def pre_process(text):
    processed_text = text

    # Remove stopwords
    #processed_text = ' '.join([word for word in processed_text if word not in STOPWORDS])

    # Normalize (U.S -> US)
    processed_text = re.sub(r'(?<!\w)([A-Za-z])\.', r'\1', processed_text)

    #print("-----------------------------------------------------")
    #print(text)
    #print()
    #print(processed_text)
    #print("-----------------------------------------------------\n")

    return processed_text

def request_file(url):
    response = request.urlopen(url)
    return response

def quick_test(summarize_algorithm):
    references = {}
    final_document = ""

    #training_data = create_training_data()
    #target_data = create_target_data()
    #model = train_model(training_data,target_data)

    for source, url in NEWS_SOURCES.items():
        xml = request_file(url)
        tree = ET.parse(xml)
        root = tree.getroot()
        for item in root.iter('item'):
            title = clean_xml_text(item.find('title').text if item.find('title').text != None else '')
            description = clean_xml_text(item.find('description').text if item.find('description') != None else '')
            link = item.find('link').text

            # Ensure there is no None
            title = '' if title == None else title
            description = '' if description == None else description

            #print()
            #print(title , len(title))
            #print(description , len(description))
            title = title[:len(title)-1] if (len(title) > 0 and title[len(title)-1] == '.') else title
            description = description[:len(description)-1] if (len(description) > 0 and description[len(description)-1] == '.' ) else description
            #print(title , len(title))
            #print(description , len(description))
            #print()

            if title == '' and description != '' :
                document = description + " ."

            elif title != '' and description == '' :
                document = title + " ."

            elif title != '' and description != '' :
                document = title + ". " + description + " ."

            elif title == '' and description == '' :
                continue

            for sentence in get_sentences(pre_process(document)):
                references[sentence] = [source, link]


            #print("-----------------------------------------------------------")
            #print("Source:",source)
            #print()
            #print(title)
            #print(description)
            #print()
            #print(document)
            #print("-----------------------------------------------------------\n")

            final_document += " " + document



    document = pre_process(final_document)
    if summarize_algorithm != summarize_ex3:
        summary, _ = summarize_algorithm(document, top=5)
    else:
        try:
            with open('model.pkl', 'rb') as f:
                model = pickle.load(f)
                print("[*] Model found")
        except:
            print("[*] Model not found. Training...")

            # training_data: vector com features de sentences
            training_data = create_training_data()

            # target_data: vector com classificacoes (vector de vectores)
                # classificacoes: vector com classes
            target_data = create_target_data()


            model = train_model(training_data,target_data)
        summary = summarize_algorithm(document, model, top=5)

    print("SUMMARY")
    print("----------------------------------------------------------------")
    for sent in summary:
        print(sent,"(from " + references[sent][0] +")")
        #print(sent)
    return summary, references

#document - "The US President say. President trump has recognized jerusalem as the capital of israel."
#references["The US President say. President trump has recognized jerusalem as the capital of israel."] = "CNN"
#sumario - "President trump has recognized jerusalem as the capital of israel."



def generate_html(global_summaries):
    print("----------------------------------------------------------------")
    print("\nGENERATING HTML")

    doc = dominate.document(title='RSS Summaries')
    with doc:
        for summary , vals in global_summaries.items():
            dic = {}
            for e in vals[0]:
                blob = TextBlob(e)
                for l in blob.noun_phrases:
                    if l not in dic:
                        dic[l] = 1
                    else:
                        dic[l] = dic[l] + 1

            order_phrase = sorted(dic.items(), key=itemgetter(1), reverse = True)


            correlated_terms = union_similiar_terms(dic)
            highlight = 3
            for e in order_phrase[:highlight]:
                for x in correlated_terms:
                    if e[0] in x:
                        if x.index(e[0]) == 0:
                            search_for = x[1]
                        else :
                            search_for = x[0]
                        dic[search_for] = dic[search_for] + dic[e[0]]
                        dic[e[0]] = dic[search_for]
            order_phrase = sorted(dic.items(), key=itemgetter(1), reverse = True)

            top = []
            for e in order_phrase[:highlight]:
                top.append(e[0])


            for e in correlated_terms:
                if (e[0] in top) and (e[1] in top):
                    highlight += 1

            top = []
            for e in order_phrase[:highlight]:
                top.append(e[0])
            top = sorted(top, key=len, reverse = True)
            print("top phrases: " , top)

            list_colors = ["#FF8C00","#CD5C5C","#32CD32","#CD853F"]
            with div(id = summary):
                with table(style="border:solid 1px; width:100%; float:left;border-collapse: collapse; margin-bottom:10px;").add(tbody()):
                    l = tr(style="border: 1px solid black;")
                    l += td(p("Using: "+ summary, style="margin-top:5px; margin-bottom:5px; margin-left:15px;"), style="border: solid 1px;", width="70%")
                    l.add(td(p("From:",style="margin-top:5px; margin-bottom:5px; margin-left:15px;" ),style="border: solid 1px;", width="30%"))
                    for e in vals[0]:
                        l = tr(style="border: 1px solid black;")
                        print(vals[1][e])
                        url = vals[1][e][1]
                        site = vals[1][e][0]
                        e = e.lower()
                        for i in range(len(top)):
                            e = e.replace(top[i],'<label style="text-decoration:underline; background:'+list_colors[i]+'; ">'+top[i]+'</label>')
                        e = e.replace(e,'<a href="'+url+'">'+e+'</a>')
                        l += td(p(raw(e), style="margin-top:5px; margin-bottom:5px; margin-left:15px;"), style="border: solid 1px;",width="70%")
                        l.add(td(p(site, style="margin-left:15px;"), style="border: solid 1px;",width="30%"))

                    words = ''
                    for e in range(len(top)):
                        words += ' <label style="background:'+list_colors[e]+';">' + top[e] + '</label> '
                    words += ''
                    words = words.replace(words , 'Top Topics: [<label style="font-style:italic;">'+words+'</label> ]')
                    l = tr(style="border: 1px solid black;")
                    l += td(p(raw(words), style="text-align:center; margin-top:10px; margin-bottom:10px;"), colspan="2" ,style="border: solid 1px;")



    file = open('summaries.html', 'w')
    file.write(str(doc))
    file.close()

    print("----------------------------------------------------------------")

def union_similiar_terms(dic):
    correlated_terms = []
    already_compared = []
    for phrase, value in dic.items():
        for comparing_phrase, val in dic.items():
            if ((phrase,comparing_phrase) in already_compared) or ((comparing_phrase,phrase) in already_compared) :
                continue
            else :
                if levenshteinDistance(phrase,comparing_phrase) == 1 :
                    correlated_terms.append((phrase, comparing_phrase))

            already_compared.append((phrase,comparing_phrase))
            already_compared.append((comparing_phrase,phrase))

    return correlated_terms

def levenshteinDistance(s1, s2):
    if len(s1) > len(s2):
        s1, s2 = s2, s1

    distances = range(len(s1) + 1)
    for i2, c2 in enumerate(s2):
        distances_ = [i2+1]
        for i1, c1 in enumerate(s1):
            if c1 == c2:
                distances_.append(distances[i1])
            else:
                distances_.append(1 + min((distances[i1], distances[i1 + 1], distances_[-1])))
        distances = distances_
    return distances[-1]

def main():
    dic = {}
    #for source, url in NEWS_SOURCES.items():
    summaries, references = quick_test(summarize_ex1)
    dic["Exercise 1"] = [summaries, references]


    summaries, references = quick_test(summarize)
    dic["Exercise 2"] = [summaries, references]

    summaries, references = quick_test(summarize_ex3)
    dic["Exercise 3"] = [summaries, references]

    generate_html(dic)

if __name__ == "__main__":
    main()
