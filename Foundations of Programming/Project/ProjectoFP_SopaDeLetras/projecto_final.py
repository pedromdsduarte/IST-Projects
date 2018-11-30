#Pedro Duarte - 78328; Joao Serra - 78611; Joao Palmeiro - 79114; Grupo - al063

#--------------------#
#--- Tipo direcao ---#
#--------------------#

ldirecao = ['N', 'S', 'E', 'W', 'NE', 'NW', 'SE', 'SW'] #Elementos do tipo direcao
ldirecao_oposta = ['S', 'N', 'W', 'E', 'SW', 'SE', 'NW', 'NE'] #Elementos opostos do tipo direcao

#RECONHECEDORES:
def e_direcao(arg):
    """e_direcao: universal -> logico
    e_direcao(arg) tem o valor verdadeiro se o arg for do tipo direcao e falso caso contrario."""
    return arg in ldirecao 
def e_norte(arg):
    """e_norte: direcao -> logico
    e_norte(arg) tem o valor verdadeiro se o arg for o elemento 'N' e falso caso contrario."""
    return arg == 'N'
def e_sul(arg):
    """e_sul: direcao -> logico
    e_sul(arg) tem o valor verdadeiro se o arg for o elemento 'S' e falso caso contrario."""
    return arg == 'S'
def e_leste(arg):
    """e_leste: direcao -> logico
    e_leste(arg) tem o valor verdadeiro se o arg for o elemento 'E' e falso caso contrario."""    
    return arg == 'E'
def e_oeste(arg):
    """e_oeste: direcao -> logico
    e_oeste(arg) tem o valor verdadeiro se o arg for o elemento 'W' e falso caso contrario."""       
    return arg == 'W'
def e_nordeste(arg):
    """e_nordeste: direcao -> logico
    e_nordeste(arg) tem o valor verdadeiro se o arg for o elemento 'NE' e falso caso contrario."""       
    return arg == 'NE'
def e_noroeste(arg):
    """e_noroeste: direcao -> logico
    e_noroeste(arg) tem o valor verdadeiro se o arg for o elemento 'NW' e falso caso contrario."""       
    return arg == 'NW'
def e_sudeste(arg):
    """e_sudeste: direcao -> logico
    e_sudeste(arg) tem o valor verdadeiro se o arg for o elemento 'SE' e falso caso contrario."""       
    return arg == 'SE'
def e_sudoeste(arg):
    """e_sudoeste: direcao -> logico
    e_sudoeste(arg) tem o valor verdadeiro se o arg for o elemento 'SW' e falso caso contrario."""       
    return arg == 'SW'

#TESTES:
def direcoes_iguais(d1,d2):
    """direcoes_iguais: direcao x direcao -> logico
    direcoes_iguais(d1,d2) devolve o valor verdadeiro se as direcoes d1 e d2 forem iguais e falso caso contrario."""
    return d1 == d2

#OUTRAS OPERACOES:
def direcao_oposta(d):
    """direcao_oposta: direcao -> direcao
    direcao_oposta(d) devolve a direcao oposta de d de acordo com a rosa dos ventos."""
    if d in ldirecao:
        for e in range(len(ldirecao)):
            if d == ldirecao[e]:
                return ldirecao_oposta[e]
    else:
        raise ValueError('direcao_oposta: argumentos invalidos')
    
#-----------------------#
#--- Tipo coordenada ---#
#-----------------------#

#CONSTRUTOR:
def coordenada(l,c,d): 
    """coordenada: N0 x N0 x direcao -> coordenada
    coordenada(l,c,d) tem como valor a coordenada referente a posicao (l,c) e direcao d."""
    if isinstance (l, int) and l >= 0 and isinstance (c, int) and c >= 0 and d in ldirecao:
        return (l,c,d)
    else:
        raise ValueError ('coordenada: argumentos invalidos')

#SELETORES:
def coord_linha(c):
    """coord_linha: coordenada -> N0
    coord_linha(c) tem como valor a linha da coordenada."""
    return c[0]

def coord_coluna(c):
    """coord_coluna: coordenada -> N0
    coord_coluna(c) tem como valor a coluna da coordenada."""
    return c[1]

def coord_direcao(c):
    """coord_direcao: coordenada -> direcao
    coord_direcao(c) tem como valor a direcao da coordenada."""
    return c[2]

#RECONHECEDOR:
def e_coordenada(arg):
    """e_coordenada: universal -> logico
    e_coordenada(arg) tem o valor verdadeiro se o arg for do tipo coordenada e falso caso contrario."""
    return (isinstance (arg, tuple) and 3 == len(arg) and isinstance (arg[0], int) and arg[0] >= 0 and isinstance (arg[1], int) and arg[1] >= 0 \
            and arg[2] in ldirecao)

#TESTES:
def coordenadas_iguais(c1,c2):
    """coordenadas_iguais: coordenada x coordenada -> logico
    coordenadas_iguais(c1,c2) devolve o valor verdadeiro se as coordenadas c1 e c2 forem iguais e falso caso contrario."""
    return c1 == c2

#OUTRAS OPERACOES:
def coordenada_string(c):
    """coordenada_string: coordenada -> string
    coordenada_string(c) devolve a representacao externa de c:
    uma cadeia de caracteres iniciada por parentesis esquerdo '(' seguido pelo numero da linha e da coluna,
    separados por virgula e um espaco ', ', seguido por parentesis direito e traco ')-', 
    apos os quais se apresenta a direcao."""
    return ('(' + str(c[0]) + ', ' + str(c[1]) + ')-' + c[2]) 

#-------------------#
#--- Tipo grelha ---#
#-------------------#

#CONSTRUTOR:
def grelha(lst):
    """grelha: lista de strings -> grelha
    grelha(lst) tem como valor uma grelha mxn, em que m e o numero de elementos da lista lst 
    e n o numero de caracteres de cada string na lista.
    A lista nao pode ser vazia, e todas as strings devem ter o mesmo comprimento."""
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
    """grelha_nr_linhas: grelha -> N0
    grelha_nr_linhas(g) devolve o numero de linhas da grelha g."""
    return len(g)

def grelha_nr_colunas(g):
    """grelha_nr_colunas: grelha -> N0
    grelha_nr_colunas(g) devolve o numero de colunas da grelha g."""
    return len(g[0][0])

def grelha_elemento(g,l,c):
    """grelha_elemento: grelha x N0 x N0 -> caracter
    grelha_elemento(g,l,c) devolve o caracter que esta na posicao (l,c) da grelha g."""
    if (linha_valida(g,l) and coluna_valida(g,c)):
        return g[l][0][c]
    else:
        raise ValueError('grelha_elemento: argumentos invalidos')
    
def coluna_valida(g,c):
    """coluna_valida: grelha x N0 -> logico
    coluna_valida(g,c) devolve o valor verdadeiro se o valor de c for um numero entre 0 e o numero de colunas da grelha g menos 1 
    e falso caso contrario."""
    return (0 <= c <= grelha_nr_colunas(g)-1)
def linha_valida(g,l):
    """linha_valida: grelha x N0 -> logico
    linha_valida(g,c) devolve o valor verdadeiro se o valor de l for um numero entre 0 e o numero de linhas da grelha g menos 1
    e falso caso contrario."""    
    return (0 <= l <= grelha_nr_linhas(g)-1)
    
def grelha_linha(g,c):
    """grelha_linha: grelha x coordenada -> string
    grelha_linha(g,c) devolve a cadeia de caracteres que corresponde a linha definida segundo a direcao dada pela coordenada c,
    e que inclui a posicao dada pela mesma coordenada."""
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
    """retorna_sul: grelha x coordenada -> string
    retorna_sul(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para sul."""
    res=''
    for linha in range(len(g)):
        if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
            res=res+g[linha][0][coord_coluna(c)]
        else:
            raise ValueError('grelha_linha: argumentos invalidos')
    return res
    
def retorna_norte(g,c):
    """retorna_norte: grelha x coordenada -> string
    retorna_sul(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para norte."""    
    res=''
    for linha in range(len(g)-1,-1,-1):
        if coluna_valida(g,c[1]) and linha_valida(g,c[0]):
            res=res+g[linha][0][coord_coluna(c)]
        else:    
            raise ValueError('grelha_linha: argumentos invalidos')       
    return res  
            
def retorna_se(g,c):
    """retorna_se: grelha x coordenada -> string
    retorna_se(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para sudeste."""    
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
    """retorna_nw: grelha x coordenada -> string
    retorna_nw(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para noroeste."""    
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
    """retorna_ne: grelha x coordenada -> string
    retorna_ne(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para nordeste."""    
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
    """retorna_sw: grelha x coordenada -> string
    retorna_sw(g,c) devolve a cadeia de caracteres que corresponde a uma linha da grelha g orientada para sudoeste."""    
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
    """e_grelha: universal -> logico
    e_grelha(arg) tem o valor verdadeiro se o arg for do tipo grelha e falso caso contrario."""
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
    """grelhas_iguais: grelha x grelha -> logico
    grelhas_iguais(g1,g2) devolve o valor verdadeiro se as grelhas forem iguais e falso caso contrario."""
    return g1 == g2
        
#---------------------#      
#--- Tipo resposta ---#  
#---------------------#

#CONSTRUTOR:  
def resposta(lst):
    """resposta: lista de tuplos (string, coordenada) -> resposta
    resposta(lst) tem como valor a resposta que contem cada um dos tuplos que compoem a lista lst."""
    if isinstance(lst,list):
        for t in lst:
            if not isinstance(t,tuple) or not isinstance(t[0],str) or not e_coordenada(t[1]):
                raise ValueError('resposta: argumentos invalidos')                    
        return lst
    else:
        raise ValueError('resposta: argumentos invalidos')

#SELETORES:
def resposta_elemento(res,n):
    """resposta_elemento: resposta x N0 -> tuplo(string, coordenada)
    resposta_elemento(res,n) devolve o enesimo elemento da resposta res."""
    if 0 <= n < len(res):
        return res[n]
    else:
        raise ValueError('resposta_elemento: argumentos invalidos')
        
def resposta_tamanho(res):
    """resposta_tamanho: resposta -> N0
    resposta_tamanho(res) devolve o numero de elementos da resposta res."""
    return len(res)

#MODIFICADOR:
def acrescenta_elemento(r,s,c):
    """acrescenta_elemento: resposta x string x coordenada -> resposta
    acrescenta_elemento(r,s,c) devolve a resposta r com mais um elemento - o tuplo (s,c)."""
    return r + [(s,c)]

#RECONHECEDOR:   
def e_resposta(arg):
    """e_resposta: universal -> logico
    e_resposta(arg) tem o valor verdadeiro se o arg for do tipo resposta e falso caso contrario."""
    if isinstance(arg,list):
        for e in arg:
            if not isinstance(e,tuple) or not isinstance(e[0],str) or not e_coordenada(e[1]):
                return False
        return True
    else:
        return False

#TESTES:
def respostas_iguais(r1,r2):
    """respostas_iguais: resposta x resposta -> logico
    respostas_iguais(r1,r2) devolve o valor verdadeiro se as respostas r1 e r2 contiverem os mesmos tuplos e falso caso contrario."""
    if r1 == r2:
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
    ordena_res(res) devolve a resposta res ordenada alfabeticamente segundo o primeiro elemento de cada tuplo (palavra)."""
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
    """resposta_string: resposta -> string
    resposta_string(res) devolve a representacao externa da resposta res:
    uma cadeia de caracteres iniciada pelo parentesis recto esquerdo '[' e que contem
    a descricao de cada elemento da resposta separados por virgulas e espaco ', ',
    terminando com o parentesis recto direito ']'.
    Cada elemento e representado por: '<'PALAVRA':'COORDENADA'>',
    em que PALAVRA e a palavra encontrada, e COORDENADA a coordenada onde se encontra a palavra.
    Os elementos estao necessariamente ordenados por ordem alfabetica das palavras."""
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
    
#-----------------------------#    
#--- FUNCOES A IMPLEMENTAR ---#
#-----------------------------#

def procura_palavras_numa_direcao(grelha,palavras,direcao):
    """procura_palavras_numa_direcao: grelha x lista de srings x direcao -> resposta
    procura_palavras_numa_direcao(grelha,palavras,direcao) tem como resultado a resposta que representa 
    as coordenadas das palavras encontradas na grelha. Para cada palavra, deve apresentar-se a coordenada
    do seu primeiro caracter seguindo a direcao em que se encontra a palavra."""
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

def sopa_letras(fich): #fich -> nome do ficheiro que contem a descricao do puzzle
    """sopa_letras: cadeia de caracteres -> resposta
    sopa_letras(fich) tem como resultado a resposta ao puzzle descrito no ficheiro fich."""
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

def palavras_sp(palavras):
    """palavras_sp: string -> lista de strings
    palavras_sp(palavras) devolve as palavras contidas no ficheiro no formato de uma lista de strings."""
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
    """e_minuscula: caracter -> logico
    e_minuscula(car) devolve o valor verdadeiro se o caracter car for uma letra minuscula e falso caso contrario."""
    return 97 <= ord(car) <= 122

def poe_maiscula(car): 
    """poe_maiuscula: caracter -> caracter
    poe_maiuscula(car) devolve o caracter maiusculo correspondente."""
    car = chr(ord(car) - 32)
    return car

def grelha_sp(grelha): 
    """grelha_sp: lista de strings -> grelha
    grelha_sp(grelha) devolve a lista de strings correspondentes as linhas e colunas da grelha contidas no ficheiro, 
    no formato de um elemento do tipo grelha."""
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