#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>

#define NUM_FILES 5
#define X_POS 7

char filename[] = "SO2014-x.txt";

int randomNumber(int max){
	static int flag = 0;
	if (!flag){
		srand ( time(NULL) );
		flag = 1;
	}
	
	return rand() % max;
}

char * choose_file(){
	char rep;
	int rfile = randomNumber(NUM_FILES);
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	
	return filename;
}

int main(){
	int i;
	for(i=0;i<5;i++)
		printf("%s \n",choose_file());
	return 0;
}




/* Escolhe um ficheiro aleatoriamente */
	/* Se nao existir cria ficheiro*/
/* Abre o ficheiro */
/* Escolhe uma letra de 10 */
/* Escreve 1024 vezes a string no ficheiro */
/* Repete o processo */
