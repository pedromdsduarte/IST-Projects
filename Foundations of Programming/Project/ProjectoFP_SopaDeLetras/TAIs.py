#### TIPO DIRECAO ####

#RECONHECEDORES

def e_direcao(arg):
    return isinstance(arg,str) and \
           (arg == 'N' or arg == 'S' or arg == 'E' or arg == 'W' or arg == 'NW' or arg == 'NE' or arg == 'SE' or arg == 'SW')

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

#TESTES
    
def direcoes_iguais(d1,d2):
    if e_direcao(d1) and e_direcao(d2):
        return d1 == d2
    else:
        return False

#OUTRAS OPERACOES

def direcao_oposta(d):
    direcoes=['N','S','E','W','NE','SE','NW','SW']      #a cada elemento da lista "direcoes" corresponde o elemento contrario da lisat "direcoes opostas"
    dir_oposta=['S','N','W','E','SW','NW','SE','NE']
    if d in direcoes:
        for e in range(len(direcoes)-1):
            if d == direcoes[e]:
                return dir_oposta[e]
    else:
        raise ValueError('direcao_oposta: o argumento deve ser uma direcao')
    
### TIPO COORDENADA ###

#CONSTRUTOR

def coordenada(l,c,d):
    if e_direcao(d) and isinstance(l,int) and isinstance(c,int):
        return (l,c,d)
    else:
        raise ValueError('coordenada: argumentos invalidos')

#SELETOR

def coord_linha(c):
    return c[0]

def coord_coluna(c):
    return c[1]

def coord_direcao(c):
    return c[2]

#RECONHECEDOR

def e_coordenada(arg):
    if isinstance(arg,tuple) and len(arg) == 3 and \
       isinstance(arg[0],int) and isinstance(arg[1],int) and e_direcao(arg[2]):
        return True
    else:
        return False
    
#TESTES

def coordenadas_iguais(c1,c2):
    if e_coordenada(c1) and e_coordenada(c2):
        return c1 == c2
    else:
        return False
    
#OUTRAS OPERACOES

def coordenada_string(c):
    #Representacao externa da coordenada c: (linha, coluna)-direcao; exemplo: (5, 29)-NE
    print('(' + str(c[0]) + ', ' + str(c[1]) + ')-' + str(c[2]))
    

### TIPO GRELHA ###

#CONSTRUTOR

def grelha(lst):
    if lst != []:
        n = len(lst[0]) #numero de caracteres da string
        grelha=[]
        for e in lst:
            if len(e) == n: #verifica que o numero de caracteres e igual em todos os casos
                grelha=grelha+[[e]]
            else:
                raise ValueError('grelha: argumentos invalidos')
        return grelha
    else:
        raise ValueError('grelha: argumentos invalidos') 
            
            
#SELETORES

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
            return g[coord_linha(c)][0]
        
        elif e_oeste(coord_direcao(c)):
            return g[coord_linha(c)][0][::-1][-1:]
        
        elif e_sul(coord_direcao(c)):
            return retorna_sul(g,c)
        
        elif e_norte(coord_direcao(c)):
            return retorna_norte(g,c)   
        
        elif e_nordeste(coord_direcao(c)):
            return retorna_nordeste(g,c)
        elif e_noroeste(coord_direcao(c)):
            return retorna_noroeste(g,c)
        elif e_sudeste(coord_direcao(c)):
            return retorna_sudeste(g,c)
        else:
            return retorna_sudoeste(g,c)
    else:
        raise ValueError('grelha_linha: argumentos invalidos')
    

def retorna_sul(g,c):
    res=''
    for linha in range(len(g)):
        res=res+g[linha][0][coord_coluna(c)]
    return res
    
def retorna_norte(g,c):
    res=''
    for linha in range(len(g)-1,-1,-1):
        res=res+g[linha][0][coord_coluna(c)]
    return res 



#RECONHECEDOR

