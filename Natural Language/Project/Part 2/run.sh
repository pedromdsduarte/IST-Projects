#!/bin/bash
VERBOSE=false
while getopts 'v' flag; do
    case "${flag}" in
        v)
            VERBOSE=true
            ;;
    esac
done
##CREATE NORMALIZED TEXT
mkdir -p treino-normalizado
rm -fr treino-normalizado/*
echo "[-] TREINO: "
echo -e "\t[-] A normalizar treino"
for folder in treino/*; do
    if $VERBOSE; then echo -e "-\t[-] A criar pasta ${folder##*/}-normalizado"; fi
    mkdir -p treino-normalizado/${folder##*/}-normalizado

    for file in $folder/*; do
        if $VERBOSE; then echo -e "\t\t[-] A normalizar: ${file##*/}-normalizado" ; fi
        sed 's/, */ , /g;s/\.\.\.\(.*\)$/ ... \1 /g;s/\([^.]\)\. /\1 . /g;s/(\(.*\))/( \1 )/g;s/! */ ! /g;s/\? */ ? /g;s/; */ ; /g;s/: */ : /g;s/  */ /g' "$file" >> treino-normalizado/"${folder##*/}"-normalizado/"${file##*/}"-normalizado
    done
done
if $VERBOSE; then echo "\t[O] Normalização concluída";fi
if $VERBOSE; then echo -e "\n\n";fi

##CONCATENATE FILES PER AUTHOR
echo -e "\t[-] A concatenar ficheiros normalizados por autor"
for folder in treino-normalizado/*; do
    if $VERBOSE; then echo -e "\t[-] A concaternar ficheiros em ${folder##*/}";fi
    cat $folder/* >> ${folder}/concatenated.txt
done
if $VERBOSE; then echo -e "\t[0] Concatenação concluída";fi
if $VERBOSE; then echo -e "\n\n";fi

echo -e "\t[-] A calcular 50 palavras mais usadas"
for folder in treino-normalizado/*; do
    if $VERBOSE; then echo -e "\t\t[-] A calcular 50 palavras mais usadas de ${folder##*/}";fi
    tr -c '[:alnum:]' '[\n*]' < treino-normalizado/"${folder##*/}"/concatenated.txt | sort | uniq -c | sort -nr | head -50 | tail -n +2 | sed -e 's/^[ ]*//' >> ${folder}/50mostfrequentwords.txt
done
if $VERBOSE; then echo -e "\n\n";fi

##CALCULATE N-GRAMS PER AUTHOR
echo -e "\t[-] A calcular n-gramas por autor"
for folder in treino-normalizado/*; do
    if $VERBOSE; then echo -e "\t\t[-] Directoria: ${folder##*/}";fi
    if $VERBOSE; then echo -e "\t\t\t[-] A calcular n-gramas sem alisamento ${folder##*/}";fi
    ngram-count -text ${folder}/concatenated.txt -order 1 -unk -lm ${folder}/corpus-unigram-no_smooth.arpa &> /dev/null
    ngram-count -text ${folder}/concatenated.txt -order 2 -unk -lm ${folder}/corpus-bigram-no_smooth.arpa &> /dev/null
    if $VERBOSE; then echo -e "\t\t\t[-] A calcular n-gramas com alisamento ${folder##*/}";fi
    ngram-count -text ${folder}/concatenated.txt -order 1 -unk -addsmooth 1 -lm ${folder}/corpus-unigram-smooth.arpa &> /dev/null
    ngram-count -text ${folder}/concatenated.txt -order 2 -unk -addsmooth 1 -lm ${folder}/corpus-bigram-smooth.arpa &> /dev/null
done

if $VERBOSE; then echo -e "\t[0] N-gramas calculado" ;fi
if $VERBOSE; then echo -e "\n\n";fi
##CALCULATE NGRAM PROBS
echo "[-] TESTE: "
echo -e "\t[-] A calcular probabilidades dos ngramas com os ficheiros de teste."
mkdir -p teste-ngram-probs
for testefolder in teste/*; do
    mkdir -p teste-ngram-probs/${testefolder##*/}
    if $VERBOSE; then echo -e "\t\t[-] "${testefolder##*/}":";fi
    for testedoc in ${testefolder}/*; do
        if $VERBOSE; then echo -e "\t\t\t[-] "${testedoc##*/}":";fi
        mkdir -p teste-ngram-probs/${testefolder##*/}/${testedoc##*/}
        for authorfolder in treino-normalizado/*; do
            if $VERBOSE; then echo -e "\t\t\t\tAutor: "${authorfolder##*/};fi
            mkdir -p teste-ngram-probs/${testefolder##*/}/${testedoc##*/}/${authorfolder##*/}
            #echo -e "\t\t\t\t Probabilidade com unigramas"
            ngram -lm ${authorfolder}/corpus-unigram-no_smooth.arpa -ppl $testedoc -unk> teste-ngram-probs/${testefolder##*/}/${testedoc##*/}/${authorfolder##*/}/unigram-no_smooth.txt
            ngram -lm ${authorfolder}/corpus-unigram-smooth.arpa -ppl $testedoc -unk> teste-ngram-probs/${testefolder##*/}/${testedoc##*/}/${authorfolder##*/}/unigram-smooth.txt
            #echo -e "\t\t\t\t Probabilidade com bigramas"
            ngram -lm ${authorfolder}/corpus-bigram-no_smooth.arpa -ppl $testedoc -unk> teste-ngram-probs/${testefolder##*/}/${testedoc##*/}/${authorfolder##*/}/bigram-no_smooth.txt
            ngram -lm ${authorfolder}/corpus-bigram-smooth.arpa -ppl $testedoc -unk> teste-ngram-probs/${testefolder##*/}/${testedoc##*/}/${authorfolder##*/}/bigram-smooth.txt
        done
    done
done

echo -e "\t[1º] - Metodo N-gramas"
    ./statistical_ngram.py
echo -e "\t[2º] - Metodo das Palavras Mais Frequentes"
    ./mostfreqwords.py
echo -e "\t[3º] - Metodo Árvore de Decisão"
    ./train.py
    ./decisiontree.py
