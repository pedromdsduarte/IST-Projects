#Pedro Duarte - 78328; Joao Serra - 78611; Joao Palmeiro - 79114; Grupo - al063

"""Tipo direcao"""

ldirecao = ['N', 'S', 'E', 'W', 'NE', 'NW', 'SE', 'SW']
ldirecao_oposta = ['S', 'N', 'W', 'E', 'SW', 'SE', 'NW', 'NE']

#RECONHECEDORES:
def e_direcao(arg):
    return arg in ldirecao 
def e_norte(arg):
    return arg == 'N'
def e_sul(arg):
    return arg == 'S'
def e_leste(arg):
    return arg == 'E'
def e_oeste(arg):
    return arg == 'W'
def e_nordeste(arg):
    return arg == 'NE'
def e_noroeste(arg):
    return arg == 'NW'
def e_sudeste(arg):
    return arg == 'SE'
def e_sudoeste(arg):
    return arg == 'SW'

#TESTES:
def direcoes_iguais(d1,d2):
    return d1 == d2

#OUTRAS OPERACOES:
def direcao_oposta(d):
    if d in ldirecao:
        for e in range(len(ldirecao)):
            if d == ldirecao[e]:
                return ldirecao_oposta[e]
    else:
        raise ValueError('direcao_oposta: argumentos invalidos')

"""Tipo coordenada"""

#CONSTRUTOR:
def coordenada(l,c,d):
    if isinstance (l, int) and l >= 0 and isinstance (c, int) and c >= 0 and d in ldirecao:
        return (l,c,d)
    else:
        raise ValueError ('coordenada: argumentos invalidos')

#SELETORES:
def coord_linha(c):
    return c[0]

def coord_coluna(c):
    return c[1]

def coord_direcao(c):
    return c[2]

#RECONHECEDOR:
def e_coordenada(arg):
    return (isinstance (arg, tuple) and 3 == len(arg) and isinstance (arg[0], int) and arg[0] >= 0 and isinstance (arg[1], int) and arg[1] >= 0 \
            and arg[2] in ldirecao)

#TESTES:
def coordenadas_iguais(c1,c2):
    return c1 == c2

#OUTRAS OPERACOES:
def coordenada_string(c):
    return ('(' + str(c[0]) + ', ' + str(c[1]) + ')-' + c[2]) 

"""Tipo grelha"""

#CONSTRUTOR:
def grelha(lst):
    if isinstance (lst, list) and isinstance (lst[0],str):
        n = len(lst[0])
        grelha_valida = []
        for string in lst:
            if isinstance (string,str) and len(string) == n:
                grelha_valida = grelha_valida + [[string]]
            else:
                raise ValueError('grelha: argumentos invalidos')
        return grelha_valida
    raise ValueError('grelha: argumentos invalidos')

#SELETORES:
def grelha_nr_linhas(g):
    return len(g)

def grelha_nr_colunas(g):
    return len(g[0][0])

def grelha_elemento(g,l,c):
    if (linha_valida(g,l) and coluna_valida(g,c)):
        return g[l][0][c]
    else:
        raise ValueError('grelha_elemento: argumentos invalidos')
    
def coluna_valida(g,c):
    return (0 <= c <= grelha_nr_colunas(g)-1)
def linha_valida(g,l):
    return (0 <= l <= grelha_nr_linhas(g)-1)
    
def grelha_linha(g,c):
    if e_coordenada(c):
        if e_leste(coord_direcao(c)):
            if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
                return g[coord_linha(c)][0]
            else:
                raise ValueError('grelha_linha: argumentos invalidos')
        
        elif e_oeste(coord_direcao(c)):
            if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
                return g[coord_linha(c)][0][::-1]
            else:
                raise ValueError('grelha_linha: argumentos invalidos')            
        
        elif e_sul(coord_direcao(c)):
            return retorna_sul(g,c)
        
        elif e_norte(coord_direcao(c)):
            return retorna_norte(g,c)  
        
        elif e_sudeste(coord_direcao(c)):
            return retorna_nw(g,c)[1:] + retorna_se(g,c) 
        
        elif e_noroeste(coord_direcao(c)):
            return retorna_se(g,c)[1:] + retorna_nw(g,c)
        
        elif e_nordeste(coord_direcao(c)):
            return retorna_sw(g,c)[1:] + retorna_ne(g,c)
        
        elif e_sudoeste(coord_direcao(c)):
            return retorna_ne(g,c)[1:] + retorna_sw(g,c)
    else:
        raise ValueError('grelha_linha: argumentos invalidos')
    

def retorna_sul(g,c):
    res=''
    for linha in range(len(g)):
        if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
            res=res+g[linha][0][coord_coluna(c)]
        else:
            raise ValueError('grelha_linha: argumentos invalidos')
    return res
    
def retorna_norte(g,c):
    res=''
    for linha in range(len(g)-1,-1,-1):
        if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
            res=res+g[linha][0][coord_coluna(c)]
        else:    
            raise ValueError('grelha_linha: argumentos invalidos')       
    return res  
            
def retorna_se(g,c):
    res = ''
    for linha in range(len(g)):
        if linha == c[0]:
            for coluna in range(len(g[coord_linha(c)][0])):
                if coluna == c[1]:
                    while coluna_valida(g,coluna) and linha_valida(g,linha):
                        res = res + g[linha][0][coluna] 
                        linha = linha + 1
                        coluna = coluna + 1
                    return res

def retorna_nw(g,c):
    res = ''
    for linha in range(len(g)-1,-1,-1):
        if linha == c[0]:
            for coluna in range(len(g[coord_linha(c)][0][::-1])):
                if coluna == c[1]:
                    while coluna_valida(g,coluna) and linha_valida(g,linha):
                        res = res + g[linha][0][coluna] 
                        linha = linha - 1
                        coluna = coluna - 1
                    return res

def retorna_ne(g,c):
    res = ''
    for linha in range(len(g)-1,-1,-1):
        if linha == c[0]:
            for coluna in range(len(g[coord_linha(c)][0][::-1])):
                if coluna == c[1]:
                    while coluna_valida(g,coluna) and linha_valida(g,linha):
                        res = res + g[linha][0][coluna] 
                        linha = linha - 1
                        coluna = coluna + 1
                    return res

def retorna_sw(g,c):
    res = ''
    for linha in range(len(g)):
        if linha == c[0]:
            for coluna in range(len(g[coord_linha(c)][0])):
                if coluna == c[1]:
                    while coluna_valida(g,coluna) and linha_valida(g,linha):
                        res = res + g[linha][0][coluna] 
                        linha = linha + 1
                        coluna = coluna - 1
                    return res 

#RECONHECEDOR:
def e_grelha(arg):
    if isinstance(arg, list) and len(arg) > 0:
        for e in arg:
            if isinstance (e,list):
                for string in [e][0]:
                    if isinstance(string,str) and len(string) == len(arg[0][0]):
                        return True
                    else:
                        return False
            else:
                return False
    else:
        return False

#TESTES:
def grelhas_iguais(g1,g2):
    return g1 == g2
        
"""Tipo resposta"""          
#CONSTRUTOR:  
def resposta(lst):
    if isinstance(lst,list):
        for t in lst:
            if not isinstance(t,tuple) or not isinstance(t[0],str) or not e_coordenada(t[1]):
                raise ValueError('resposta: argumentos invalidos')                    
        return lst
    else:
        raise ValueError('resposta: argumentos invalidos')

#SELETORES:
def resposta_elemento(res,n):
    if 0 <= n < len(res):
        return res[n]
    else:
        raise ValueError('resposta_elemento: argumentos invalidos')
        
def resposta_tamanho(res):
    return len(res)

#MODIFICADOR:
def acrescenta_elemento(r,s,c):
    return r + [(s,c)]

#RECONHECEDOR:   
def resposta(lst):
    if isinstance(lst,list):
        for t in lst:
            if not isinstance(t,tuple) or not isinstance(t[0],str) or not e_coordenada(t[1]):
                raise ValueError('resposta: argumentos invalidos')                    
        return lst
    else:
        raise ValueError('resposta: argumentos invalidos')

def e_resposta(arg):
    if isinstance(arg,list):
        for e in arg:
            if not isinstance(e,tuple) or not isinstance(e[0],str) or not e_coordenada(e[1]):
                return False
        return True
    else:
        return False
           

#TESTES:
def respostas_iguais(r1,r2):
    if r1 == r2:
        return True
    else:
        for t in r1:
            if t in r2:
                return True
            else:
                return False
       
#OUTRAS OPERACOES:
#def ordena_res(res): #Ordena os elementos da resposta por ordem alfabetica de acordo com o seu elemento palavra
    #medidor = 0 #Estabelece se o proximo elemento aparece a esquerda ou a direita do elemento anterior
    #ordenado = []
    #for t in res:
        #if ord(t[0][0]) > medidor:
            #ordenado = ordenado + [t]
            #medidor = ord(t[0][0])
        #else:
            #ordenado = [t] + ordenado
            #medidor = ord(t[0][0])
    #return ordenado
    
def ordena_res(res):
    maior_indice = len(res) - 1 
    nenhuma_troca = False
    while not nenhuma_troca:
        nenhuma_troca = True
        for i in range(maior_indice):
            if res[i] > res[i+1]:
                res[i], res[i+1] = res[i+1], res[i]
                nenhuma_troca = False
        maior_indice = maior_indice - 1
    return res
        
    
                
def resposta_string(res):
    res_ordenado = ordena_res(res)
    res_str = ''                
    if len(res_ordenado) == 1:
        return '[' + '<' + res_ordenado[0][0] + ':' + '(' + str(res_ordenado[0][1][0]) + ', ' + str(res_ordenado[0][1][1]) + ')' + '-' + res_ordenado[0][1][2] + '>' + ']' 
    else:
        for t in range(len(res_ordenado)):
            if t == 0:
                res_str = res_str + '[' + '<' + res_ordenado[t][0] + ':' + '(' + str(res_ordenado[t][1][0]) + ', ' + str(res_ordenado[t][1][1]) + ')' + '-' + res_ordenado[t][1][2] + '>, '
            elif t == (len(res_ordenado)-1):
                res_str = res_str + '<' + res_ordenado[t][0] + ':' + '(' + str(res_ordenado[t][1][0]) + ', ' + str(res_ordenado[t][1][1]) + ')' + '-' + res_ordenado[t][1][2] + '>' + ']'
            else:
                res_str = res_str + '<' + res_ordenado[t][0] + ':' + '(' + str(res_ordenado[t][1][0]) + ', ' + str(res_ordenado[t][1][1]) + ')' + '-' + res_ordenado[t][1][2] + '>, '
        return res_str
    
#FUNCOES A IMPLEMENTAR:                           
def procura_palavras_numa_direcao(grelha,palavras,direcao):
    res = resposta([])
    for p in palavras:
        for l in range(grelha_nr_linhas(grelha)):
            for c in range(grelha_nr_colunas(grelha)):
                if grelha_elemento(grelha,l,c) == p[0]:
                    linha = grelha_linha(grelha, coordenada(l,c,direcao))
                    if p == linha[:c+len(p)] or p in linha:
                        res = acrescenta_elemento(res, p, coordenada(l,c,direcao))
    return res

from janela_sopa_letras import *

def sopa_letras(fich):
    res = resposta([])
    janela = janela_sopa_letras(fich)    
    fich = open(fich, 'r')
    lst_linhas = fich.readlines()
    grelha_lst_linhas = grelha(grelha_sp(lst_linhas[2:]))
    palavras_lst_linhas = palavras_sp(lst_linhas[1][10:-1])
    for direcao in ldirecao:
        res_dir = procura_palavras_numa_direcao(grelha_lst_linhas,palavras_lst_linhas,direcao)
        for el in res_dir:
            res = acrescenta_elemento(res,el[0],el[1])
    janela.mostra_palavras(res) #corresponde a um elemento do tipo resposta
    janela.termina_jogo()
    return res

def palavras_sp(palavras): #Transforma as palavras do ficheiro numa lista de strings
    res = []
    palavra = ''
    for car in palavras:
        if car != ',':
            if car != ' ':
                if e_minuscula(car):
                    car = poe_maiscula(car)
                palavra = palavra + car
        else:
            res = res + [palavra]
            palavra = ''
    return res + [palavra]

def e_minuscula(car):
    return 97 <= ord(car) <= 122
def poe_maiscula(car):
    car = chr(ord(car) - 32)
    return car

def grelha_sp(grelha): 
    tamanho = len(grelha[0])
    elementos = ['A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z']
    res = []
    linha = ''
    for e in grelha:
        for c in e:
            if c in elementos:
                linha = linha + c
        res = res + [linha]
        linha = ''
    return res