#include "header.h"
#include "escritor.h"

int open_lock_file(char *filename, char * rep){
	
	/*Abre um ficheiro aleatoriamente*/
	
	
	int lock_status, file;
	filename[X_POS] = rep[0];
	file = open(filename, O_RDONLY);
	
	lock_status = flock(file, LOCK_SH | LOCK_NB);
	if(lock_status == -1){
		if( errno == EWOULDBLOCK){
			printf("Ficheiro esta a ser usado\n");
		} 
	}
	lock_status = flock(file, LOCK_SH);
	if(lock_status == -1){
		perror("Error locking");
		close(file);
		exit(-1);
	}
	
	printf("--> Ficheiro: %d... ",atoi(rep));
	return file;
}

void close_unlock_file(file){
	flock(file,LOCK_UN);
	close(file);
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

int main(int argc, char *argv[])
{
	struct timeval tvstart;
	int contador = 0 , i, file;
	char buffer[NUM_CHARS] = "";
	char first[NUM_CHARS]= "";
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	gettimeofday(&tvstart, NULL);
	srand((tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000 );

	file = open_lock_file(filename, argv[1]);

	
	read(file, buffer, NUM_CHARS-1);
	strcpy(first,buffer);
	
	if((confirma_string(first, letters)) != 0){
		close_unlock_file(file);
		printf("Something went wrong (Confirma String)...\n\n\n");
		return -1;
	}
	
	while(strcmp(buffer,first) == 0){
		i = read(file, buffer,NUM_CHARS-1);
		contador++;
		if(i == 0){
			close_unlock_file(file);
			printf("Check!\n");
			return !(contador == NUM_LINES);
		}
	}
	printf("Something went wrong (Durante a Leitura)...\n");
	close_unlock_file(file);
	return -1;
}
