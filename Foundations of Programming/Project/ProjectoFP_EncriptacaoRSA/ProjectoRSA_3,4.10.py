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
    C=N**e % m
    if N>=m:
        raise ValueError('encripta: a mensagem tem de ser inferior a',m)
    return C

#alinea 4, decifra a mensagem C
def decifra(C,i,j):
    i=x_esimo_primo(i)
    j=x_esimo_primo(j)
    m=i*j
    N=C**d % m
    if C>=m:
        raise ValueError('decifra: a mensagem tem de ser inferior a',m)
    return N


    