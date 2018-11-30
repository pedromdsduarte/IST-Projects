all: escritor leitor paralelo leitor1_pai leitor2 leitor3

escritor: escritor.c escritor.h header.h
	gcc -g -Wall -pedantic -ansi -o escritor escritor.c 

leitor: leitor.c leitor.h header.h
	gcc -Wall -pedantic -ansi -o leitor leitor.c 
	
paralelo: paralelo.c 
	gcc -Wall -pedantic -ansi -o paralelo paralelo.c
	
leitor1_pai: leitor1_pai.c
	gcc -Wall -pedantic -ansi -o leitor1_pai leitor1_pai.c
	
leitor2: leitor2.c
	gcc -ansi -pedantic -Wall -o leitor2 -pthread leitor2.c
	
leitor3: leitor3.c
	gcc -ansi -pedantic -Wall -o leitor3 -pthread leitor3.c
