#include "escritor.h"


int open_file(char *filename){
	char rep;
	int file, rfile = rand() % NUM_FILES;
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	file = open(filename, O_RDONLY);

	return file;
}


int main()
{
	int contador = 0 ,i=0, file;
	char buffer[NUM_CHARS], filename[] = "SO2014-x.txt";
	srand(time(NULL));
	file = open_file(filename);
	while(1){
		i = read(file, buffer,NUM_CHARS);
		contador++;
		if(i == 0){
			printf("contador: %d\n",contador);
			return 0;
		}
	}
	printf("contador: %d\n",contador);
	return -1;
}

