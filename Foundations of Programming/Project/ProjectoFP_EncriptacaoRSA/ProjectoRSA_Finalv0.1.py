def calcula_e(n):
    
    e=1
    def mdc(a,b):
        """MDC: int x int --> int
        MDC(a,b) devolve o maximo divisor comum entre dois inteiros a e b"""
        while b!=0:
            resto=a%b
            a,b=b,resto
        return a     
    while e!=n:
        e=e+1       
        if mdc(e,n)==1:
            return e


def calcula_d(e,n):
    k=0
    d=1.1
    while int(d)!=d:
        k=k+1
        d=(1+k*n)/e
    return int(d)

#verifica se o numero inteiro x e ou nao primo
def e_primo(x):
    divisor=2
    while divisor<x:
        if x%divisor==0:
            return False
        else:
            divisor=divisor+1
    return True

#determina o x-esimo numero primo
def x_esimo_primo(x2):
    inicial=2
    conta=0
    while conta<x2:
        if e_primo(inicial):
            conta=conta+1
        inicial=inicial+1
    return (inicial-1)

#encripta a mensagem N
def encripta(N,i,j):
    i=x_esimo_primo(i)
    j=x_esimo_primo(j)
    m=i*j
    n=(i-1)*(j-1)   
    e=calcula_e(n)
    C=N**e % m
    if N>=m:
        raise ValueError('encripta: a mensagem tem de ser inferior a '+str(m))
    return C

#alinea 4, decifra a mensagem C
def decifra(C,i,j):
    i=x_esimo_primo(i)
    j=x_esimo_primo(j)
    n=(i-1)*(j-1)
    m=i*j
    e=calcula_e(n)
    d=calcula_d(e,n)
    N=C**d % m
    if C>=m:
        raise ValueError('decifra: a mensagem tem de ser inferior a '+str(m))
    return N
        