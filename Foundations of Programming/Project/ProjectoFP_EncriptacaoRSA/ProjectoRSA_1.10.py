def calcula_e(n):
    e=1
    def mdc(a,b):
        while b!=0:
            resto=a%b
            a,b=b,resto
        return a     
    while e!=n:
        e=e+1       
        if mdc(e,n)==1:
            return e