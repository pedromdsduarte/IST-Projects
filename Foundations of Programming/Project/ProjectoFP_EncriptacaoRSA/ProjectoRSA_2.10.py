def calcula_d(e,n):
    k=0
    d=1
    while d>0:
        k=k+1
        d=(1+k*n)/e
        if int(d)==d:
            return int(d)