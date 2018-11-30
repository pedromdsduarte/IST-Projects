#include "header.h"
#include "escritor.h"
int num_blocks = 0 ;

int randomNumber(int max){
	
	/*Devolve um numero entre 0 e max, aleatoriamente*/
	static int flag = 0; 
	struct timeval tvstart;
	gettimeofday(&tvstart, NULL);
	if (!flag){
		srand ( (tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000 ); /* transforma segundos em microsegundos */
		flag = 1;
	}
	return rand() % max;
}

int choose_lock_file(char *filename){
	
	/*Abre um ficheiro aleatoriamente*/
	char rep;
	int lock_status, file, rfile = randomNumber(NUM_FILES);
	rep = (char)(((int)'0')+rfile);
	filename[X_POS] = rep;
	file = open(filename, O_RDWR | O_CREAT , S_IRWXU | S_IROTH );
	
	lock_status = flock(file, LOCK_EX | LOCK_NB);
	if(lock_status == -1){
		if( errno == EWOULDBLOCK){
			/*regista aqui o numero de ocorrencias que nao acedeu*/
			num_blocks++;
		} 
	}
	lock_status = flock(file, LOCK_EX);
	if(lock_status == -1){
		perror("Error locking");
		close(file);
		exit(-1);
	}
	
	return file;
}

void close_unlock_file(int file){
	flock(file,LOCK_UN);
	close(file);
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
		file = choose_lock_file(filename);
		letter = choose_letter();
		
		for(j = 0; j < NUM_LINES; j++) {
			write(file,letters[letter],NUM_CHARS-1);
		}
		close_unlock_file(file);
	}
	printf("Escritor (%d)   De %d ciclos deste escritor, %d esperaram pelo ficheiro\n", getpid(), NUM_CYCLES, num_blocks);
	return 0;
}

