#include "leitor.h"


int open_file(char *filename){
	char rep;
	int file, rfile = rand() % NUM_FILES;
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	/*printf("Estou a ler o %s\n",filename);*/
	file = open(filename, O_RDONLY);

	return file;
}

int confirma_string(char * buffer){
	int i,j;
	char sub, string[] = "xxxxxxxxx\n";
	for(i=0;i<=10;i++){
		sub = (char)(((int)'a')+i);
		for(j=0;j<9;j++){
			string[j] = sub;
		}
		if(i==10){
		/*	printf("retornou -1\n");*/
			return -1;
		}else if(strcmp(buffer,string)==0){
		/*	printf("efectou um break e continou ciclo\n");*/
			break;
		}
	}
	return 0;
}


int main(){
	int contador = 0 ,i, file;
	char filename[] = "SO2014-x.txt";
	char *first = (char*) malloc(sizeof(char)*NUM_CHARS);
	char *buffer = (char*) malloc(sizeof(char)*NUM_CHARS);
	srand(time(NULL));
	file = open_file(filename);
	read(file, buffer, 10);
	strcpy(first,buffer);

	
	if(confirma_string(buffer)==0){
		while(strcmp(buffer,first) == 0){
			i = read(file, buffer,NUM_CHARS);
			contador++;
				if(i == 0){
				/*	printf("Retornou: %d\n", -!(contador == 1024)); */
					free(first);
					free(buffer);
					close(file);
					return !(contador == 1024);
			}
		}
	/*	printf("linha diferente!\n");*/
		free(first);
		free(buffer);
		close(file);
		return -1;
	}
	free(first);
	free(buffer);
	close(file);
	return -1;
	
	
}
