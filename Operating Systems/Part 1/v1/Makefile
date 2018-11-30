main: escritor.o leitor.o
	gcc -o main escritor.o leitor.o

escritor.o: escritor.c escritor.h
	gcc -g -Wall -pedantic -ansi -o escritor escritor.o escritor.c 

#leitor.o: leitor.c leitor.h
#	gcc -Wall -pedantic -ansi -o leitor leitor.o 