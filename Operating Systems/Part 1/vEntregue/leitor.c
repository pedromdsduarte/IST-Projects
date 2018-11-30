#include "header.h"
#include "escritor.h"

int open_file(char *filename){
	
	/*Abre um ficheiro aleatoriamente*/
	
	char rep;
	int file, rfile = rand() % NUM_FILES;
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	file = open(filename, O_RDONLY);
	return file;
}

int confirma_string(char * buffer, char letters[NUM_STRINGS][NUM_CHARS]){
	
	/*Valida a primeira string do ficheiro*/
	
	int i;
	for(i = 0; i < NUM_STRINGS; i++){
		if(i == NUM_STRINGS)
			return -1;
		if(strcmp(letters[i],buffer)==0)
			return 0;	
	}
	return -1;
}

int main()
{
	int contador = 0 , i, file;
	char buffer[NUM_CHARS] = "";
	char first[NUM_CHARS]= "";
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	
	srand(time(NULL));
	file = open_file(filename);
	read(file, buffer, NUM_CHARS-1);
	strcpy(first,buffer);
	
	if((confirma_string(first, letters)) != 0){
		close(file);
		return -1;
	}
	
	while(strcmp(buffer,first) == 0){
		i = read(file, buffer,NUM_CHARS-1);
		contador++;
		if(i == 0){
			close(file);
			return !(contador == NUM_LINES);
		}
	}
	close(file);
	return -1;
}
