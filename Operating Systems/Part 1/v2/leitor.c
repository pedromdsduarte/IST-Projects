#include "leitor.h"


int open_file(char *filename){
	char rep;
	int file, rfile = rand() % NUM_FILES;
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	printf("Estou a ler o %s\n",filename);
	file = open(filename, O_RDONLY);

	return file;
}

int confirma_string(char * buffer, char letters[NUM_STRINGS][NUM_CHARS]){
	int i;
	for(i=0;i<NUM_STRINGS;i++){
		printf("Buffer: %sLetters: %s strcmp: %d\n", buffer, letters[i], strcmp(letters[i],buffer));
		if(i == NUM_STRINGS-1){
			printf("Retornou -1\n");
			return -1;
		}
		if(strcmp(letters[i],buffer)==0){
			printf("String existe, efectuou break e continuou o programa\n");
			return 0;
		}

	}
	return -1;
}


int main()
{
	int contador = 0 ,i, file;
	char buffer[NUM_CHARS],first[NUM_CHARS], filename[] = "SO2014-x.txt";
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	srand(time(NULL));
	file = open_file(filename);
	read(file, buffer, NUM_CHARS-1);
	printf("Buffer: %s\n",buffer);
	confirma_string(buffer, letters);
	
	
	while(strcmp(buffer,first) == 0){
		i = read(file, buffer,NUM_CHARS);
		contador++;
		if(i == 0){
		/*	printf("Retornou: %d\n", -!(contador == 1024)); */
			return !(contador == 1024);
		}
	}
	return -1;

}
