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
    if d == 'N':
        return 'S'
    elif d == 'S':
        return 'N'
    elif d == 'E':
        return 'W'
    elif d == 'W':
        return 'E'
    elif d == 'NE':
        return 'SW'
    elif d == 'SW':
        return 'NE'
    elif d == 'NW':
        return 'SE'
    elif d == 'SE':
        return 'NW'
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
    def escreve_linha(l): #funcao auxiliar que escreve uma linha
        linha=''
        for e in l:
            linha=linha+' '+str(e)
        print(linha)

    if lst != []:
        n = len(lst[0]) #numero de caracteres da string
        for e in lst:
            if len(e) == n: #verifica que o numero de caracteres e igual em todos os casos
                escreve_linha(e)
            else:
                raise ValueError('grelha: argumentos invalidos')
    else:
        raise ValueError('grelha: argumentos invalidos') 
            
            
#SELETORES

def grelha_nr_linhas(g):
    