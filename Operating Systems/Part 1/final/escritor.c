#include "escritor.h"


int randomNumber(int max){
	static int flag = 0;
	if (!flag){
		srand ( time(NULL) );
		flag = 1;
	}
	
	return rand() % max;
}

int choose_file(char *filename){
	char rep;
	int file, rfile = randomNumber(NUM_FILES);
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	file = open(filename, O_RDWR | O_CREAT | O_TRUNC, S_IRWXU | S_IROTH );

	return file;
}

int choose_letter() {
	return randomNumber(NUM_CHARS);
}



int main(){
	char filename[] = "SO2014-x.txt";
	char *letters[10] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	int i, j, file, letter;
	for(i=0;i<5120;i++){
		file = choose_file(filename);
		letter = choose_letter();
		for(j=0;j<1024;j++) {
			write(file,letters[letter],10);
		}
		close(file);		
	}
	return 0;
}
