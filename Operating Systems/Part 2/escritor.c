#include "header.h"
#include "escritor.h"

int randomNumber(int max){
	
	/*Devolve um numero entre 0 e max, aleatoriamente*/
	
	static int flag = 0;
	if (!flag){
		srand ( time(NULL) );
		flag = 1;
	}
	return rand() % max;
}

int choose_file(char *filename){
	
	/*Abre um ficheiro aleatoriamente*/
	
	char rep;
	int file, rfile = randomNumber(NUM_FILES);
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	file = open(filename, O_RDWR | O_CREAT | O_TRUNC, S_IRWXU | S_IROTH );

	return file;
}

int choose_letter() {
	
	/*Escolhe uma cadeia de caracteres aleatoriamente*/
	
	return randomNumber(NUM_STRINGS);
}

int main(){
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	int i, j, file, letter;
	printf("\tStarted escritor... \n");
	for(i = 0; i < NUM_CYCLES; i++){
		file = choose_file(filename);
		letter = choose_letter();
		for(j = 0; j < NUM_LINES; j++) {
			write(file,letters[letter],NUM_CHARS-1);
		}
		close(file);
	}
	printf("Finished escritor!\n");
	return 0;
}

