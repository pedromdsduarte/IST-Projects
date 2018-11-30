def calcula_d(e,n):
    k=0
    d=1.1
    while int(d)!=d:
        k=k+1
        d=(1+k*n)/e
    return int(d)
        