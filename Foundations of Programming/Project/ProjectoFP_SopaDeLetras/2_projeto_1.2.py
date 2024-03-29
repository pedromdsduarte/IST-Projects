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
    """resposta : lista de tuplos(string, coordenada) -> resposta
resposta(lst) tem como valor a resposta que contem cada um dos tuplos que
compoem a lista lst"""
    if isinstance(lst,list):
        for t in lst:
            if isinstance(t,tuple) and isinstance(t[0],str) and isinstance(t[1],tuple):
                    return lst
            else:
                raise ValueError('resposta: argumentos invalidos') 
    else:
        raise ValueError('resposta: argumentos invalidos')

#SELETORES:
def resposta_elemento(res,n):
    """resposta_elemento: resposta x natural -> tuplo(string,coordenada)
    resposta_elemento(res,n) devolve o enesimo elemento da resposta res"""
    if res != None:
        if 0 <= n < len(res):
            return res[n]
        else:
            raise ValueError('resposta_elemento: argumentos invalidos')
    else:
        raise ValueError('resposta_elemento: argumentos invalidos')
        
def resposta_tamanho(res):
    """resposta_tamanho: resposta -> natural
    resposta_tamanho(res) devolve o numero de elementos da resposta res"""
    if res == None:
        return 0
    else:
        return len(res)

#MODIFICADOR:
def acrescenta_elemento(r,s,c):
    """acrescenta_elemento: resposta x string x coordenada -> resposta
    acrescenta_elemento(r,s,c) devolve a resposta r com mais um elemento - o tuplo (s,c))"""
    if r == None:
        return [(s,c)]
    else:
        return r + [(s,c)]

#RECONHECEDOR:   
def e_resposta(arg):
    """e_resposta: universal -> logico
    e_resposta(arg) tem o valor verdadeiro se o arg for do tipo resposta e falso caso contrario"""
    if arg == None:
        return True
    elif isinstance(arg,list):
        for e in arg:
            if isinstance(e,tuple) and isinstance(e[0],str) and isinstance(e[1],tuple):
                    return True
            else:
                    return False
    else:
        return False
    
                

#TESTES:
def respostas_iguais(r1,r2):
    """respostas_iguais: resposta x resposta -> logico
    respostas_iguais(r1,r2) devolve o valor verdadeiro se as respostas r1 e r2 contiverem os mesmos tuplos e falso caso contrario"""
    if r1 == None and r2 == None:
        return True
    else:
        for t in r1:
            if t in r2:
                return True
            else:
                return False
       
#OUTRAS OPERACOES:
def ordena_res(res): 
    """ordena_res: resposta -> resposta
    ordena_res(res) ordena os elementos da resposta res por ordem alfabetica de acordo com o seu elemento palavra"""
    medidor = 0
    ordenado = []
    for tup in res:
        if ord(tup[0][0]) > medidor:
            ordenado = ordenado + [tup]
            medidor = ord(tup[0][0])
        else:
            ordenado = [tup] + ordenado
            medidor = ord(tup[0][0])
    return ordenado
                
def resposta_string(res):
    """resposta_string: resposta -> string
    resposta_string(res) devolve a representacao externa da resposta res: uma cadeia de caracteres iniciada pelo parentesis recto esquerdo '[' e que contem a descricao de cada elemento da resposta separados por virgulas e espaco ',', terminando com o parentesis recto direito ']'.
    Cada elemento e representado por '<'PALAVRA':'COORDENADA'>', em que PALAVRA e a palavra encontrada, e COORDENADA a coordenada onde se encontra a palavra. Os elementos estao necessariamente ordenados por ordem alfabetica das palavras."""
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
"""def procura_palavras_numa_direcao(grelha, palavras, direcao):
    coluna = len(grelha[0][0]) - 1
    linha = len(grelha) - 1
    for p in palavras:
        for d in ldirecao:
            if d == direcao:
                while linha_valida(grelha,linha):
                    if retorna_norte(grelha,coordenada(linha,coluna,d)) == p:
                        return [(retorna_norte(grelha,coordenada(linha,coluna,d)), coordenada(linha,coluna,d))]                        
                    else:
                        linha = linha 
                    while coluna_valida(grelha,coluna):
                        if retorna_norte(grelha,coordenada(linha,coluna,d)) == p:
                            return [(retorna_norte(grelha,coordenada(linha,coluna,d)), coordenada(linha,coluna,d))]
                        else: 
                            coluna = coluna - 1"""
                    
def procura_palavras_numa_direcao(grelha,palavras,direcao):
    """procura_paravras_numa_direcao: grelha x lista de strings x direcao -> resposta
    procura_paravras_numa_direcao(grelha,palavras,direcao) tem como resultado a resposta que representa as coordenadas das palavras encontradas na grelha. Para cada palavra, deve apresentar-se a coordenada do seu primeiro caracter seguindo a direcao em que se encontra a palavra"""
    res = []
    for p in palavras:
        for l in range(grelha_nr_linhas(grelha)):
            for c in range(grelha_nr_colunas(grelha)):
                if grelha_elemento(grelha,l,c) == p[0]:
                    if grelha_linha(grelha, coordenada(l,c,direcao)) in p:
                        res = res + [(p, coordenada(l,c,direcao))]
                    elif len(p) < len(grelha_linha(grelha, coordenada(l,c,direcao))):
                        if p in grelha_linha(grelha, coordenada(l,c,direcao)):
                            res = res + [(p, coordenada(l,c,direcao))]                        
    return res


from janela_sopa_letras import *

def sopa_letras(fich):
    res = []
    janela = janela_sopa_letras(fich)    
    fich = open(fich, 'r')
    lst_linhas = fich.readlines()
    grelha_lst_linhas = grelha(grelha_sp(lst_linhas[2:]))
    palavras_lst_linhas = palavras_sp(lst_linhas[1][10:-1])
    for direcao in ldirecao:
        res_dir = procura_palavras_numa_direcao(grelha_lst_linhas,palavras_lst_linhas,direcao)
        res = res + res_dir
    janela.mostra_palavras(res) #corresponde a um elemento do tipo resposta
    janela.termina_jogo() 

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