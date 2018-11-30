#Grupo n.59: 78328-Pedro Duarte; 78611-Joao Serra; 79114-Joao Palmeiro

def calcula_e(n):
    """calcula_e: int --> int
    calcula_e(n) devolve um inteiro e tal que: 1<e<n, n e co-primo com e"""
    e=1
    def mdc(a,b):
        """mdc: int x int --> int
        mdc(a,b) devolve o maximo divisor comum entre dois inteiros a e b"""
        while b!=0:
            resto=a%b
            a,b=b,resto
        return a     
    while e!=n:
        e=e+1       
        if mdc(e,n)==1: #calcula o mdc entre e e n; se o mdc for 1, achou-se o inteiro e correcto.
            return e


def calcula_d(e,n):
    """calcula_d: int x int --> int
    calcula_d(e,n) devolve o menor inteiro d que verifique a condicao d*e=1+k*n"""
    k=0
    d=1.1
    while int(d)!=d:
        k=k+1
        d=(1+k*n)/e
    return int(d)

def e_primo(x):
    """ e_primo: int --> bol
    e_primo(x) verifica se o inteiro x e primo e devolve True/False"""
    divisor=2
    while divisor<x:
        if x%divisor==0:
            return False
        else:
            divisor=divisor+1
    return True

def y_esimo_primo(y):
    """y_esimo_primo: int --> int
    y_esimo_primo(y) devolve o y-esimo numero primo"""
    int_inicial=2
    quant_primos=0
    while quant_primos<y:
        if e_primo(int_inicial):
            quant_primos=quant_primos+1
        int_inicial=int_inicial+1
    return (int_inicial-1)

def encripta(N,i,j):
    """encripta: int x int x int --> int
    encripta(N,i,j) devolve um inteiro C, correspondente a mensagem N encriptada"""
    i=y_esimo_primo(i)
    j=y_esimo_primo(j)
    m=i*j
    n=(i-1)*(j-1)   
    e=calcula_e(n)
    C=N**e % m
    if N>=m:
        raise ValueError('encripta: a mensagem tem de ser inferior a '+str(m))
    return C

def decifra(C,i,j):
    """decifra: int x int x int --> int
    decifra(C,i,j) devolve um inteiro N, correspondente a mensagem C decifrada"""
    i=y_esimo_primo(i)
    j=y_esimo_primo(j)
    n=(i-1)*(j-1)
    m=i*j
    e=calcula_e(n)
    d=calcula_d(e,n)
    N=C**d % m
    if C>=m:
        raise ValueError('decifra: a mensagem tem de ser inferior a '+str(m))
    return N
        