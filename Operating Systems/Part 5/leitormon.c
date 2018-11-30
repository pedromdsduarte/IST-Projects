#include "header.h"
#include <pthread.h>
#include <string.h>
#include <semaphore.h>


/* ################################################################################################## */

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


int open_lock_file(char *filename){
	
	/*Abre o ficheiro pedido*/
	
	int lock_status, file;
	file = open(filename, O_RDONLY);

	
	/*Verifica se o ficheiro esta a ser escrito*/
	lock_status = flock(file, LOCK_SH | LOCK_NB);
	if(lock_status == -1){
		if( errno == EWOULDBLOCK){
			printf("Ficheiro esta a ser usado\n");
		} 
	}
	
	/*Executa Shared Lock no ficheiro*/
	
	lock_status = flock(file, LOCK_SH);
	if(lock_status == -1){
		perror("Error locking @open_lock_file");
		close(file);
		exit(-1);
	}
	return file;
}

/* ################################################################################################## */

int index = 0;
char shbuffer[BUFFER_SIZE][FILE_NAME_SIZE];
pthread_mutex_t mutex;
sem_t sem_leitor;

void* read_file() {
	
	int contador = 0;
	int file, fline;
	char buffer[NUM_CHARS] = "";
	char first[NUM_CHARS] = "";
	char filename[FILE_NAME_SIZE];
	

	while(1) {
		sem_wait(&sem_leitor);
		pthread_mutex_lock(&mutex);

		file = open_lock_file(shbuffer[--index]);	/*"consome" uma string do buffer partilhado*/
		strcpy(filename,shbuffer[index]);			/*Guarda o nome do ficheiro*/	
		memset(shbuffer[index],0,FILE_NAME_SIZE); 	/*Limpa o que acabou de retirar*/
		
		pthread_mutex_unlock(&mutex);
		
		
		read(file, buffer, NUM_CHARS-1);
		strcpy(first,buffer);
		
		if((confirma_string(first, letters)) != 0){
			close_unlock_file(file);
			printf("Something went wrong (Confirma String @read_file...\n");
			pthread_exit((void*)-1);
		}
		
		contador = 0; 
		while(strcmp(buffer,first) == 0) {
			fline = read(file,buffer,NUM_CHARS-1);
			if(strcmp(buffer,first) != 0) {
				printf("Ficheiro %s incorrecto!\n",filename);
				break;
			}
			contador++;
			if(fline == -1) {                               
			    perror("");   
			    printf("Error in read (contador em %d)\n",contador);
			    break;
			}
			if(fline == 0){                        
				if (contador != NUM_LINES) {
				    close_unlock_file(file);
				    printf("Ficheiro %s... Check!\n",filename);
				    break;
				}
			}
		}
		
	}
	
	printf("Something went wrong (Durante a leitura do ficheiro)...\n");
	close_unlock_file(file);
	pthread_exit((void*)-1);
	
}

int inputIsNotValid(char input[]) {
	char checkfile[FILE_NAME_SIZE];
	strcpy(checkfile,input);
	checkfile[X_POS] = 'x';
	if((strcmp(checkfile,FILE_NAME) == 0) && input[X_POS] >= '0' && input[X_POS] < '0' + NUM_FILES)
		return 0;
	return 1;

}


/* ################################################################################################## */


int main() {
    struct timeval tvstart;
	int retvalue;
	int status, i, j, inp;
	char input;
	char buffer[FILE_NAME_SIZE];
	
	
    pthread_t threads[NUM_THREADS_LEITOR];
    gettimeofday(&tvstart, NULL);
    
    /*Inicializa o semaforo (partilhado por threads)*/
   	if (sem_init(&sem_leitor,THREAD_SHARED,0) != 0) {
   		perror("\nSemaphore init failed\n");
   		exit(-1);
   	}
   	/*Inicializa o mutex*/
	if (pthread_mutex_init(&mutex, NULL) != 0) {
        perror("\nMutex init failed\n");
        exit(-1);
    }



    for(i=0; i < NUM_THREADS_LEITOR; i++) { 
        
        printf("Main function here. Creating thread %d\n", i);
        status = pthread_create(&threads[i], NULL, read_file, NULL);
        if (status != 0) {
            printf("Oops. pthread create returned error code %d\n", status);
            exit(-1);
        }
    }
    
    i = 0;
   	while(1) {
   		inp = read(STDIN_FILENO, &input, sizeof(char));
   		if(i == FILE_NAME_SIZE-1) {				/*Quando chega a ultima posicao*/
   			buffer[i] = '\0';					/*"Fecha" a string*/
   			if(inputIsNotValid(buffer)) {
           		memset(buffer,0,FILE_NAME_SIZE);
           		i = 0;
           		printf("Input was not valid!\n");
           		continue;
           }
   			pthread_mutex_lock(&mutex);
   			strcpy(shbuffer[index],buffer);		/*Copia para o buffer partilhado*/
   			index = (index + 1) % BUFFER_SIZE;
   			sem_post(&sem_leitor);				/*Assinala o semaforo do leitor*/
   			memset(buffer,0,FILE_NAME_SIZE); 	/*Limpa o buffer*/
   			i = 0;								/*Faz reset no buffer*/
   			pthread_mutex_unlock(&mutex);
   		}
   		if(inp == END) {
   			for(i = 0; i < index; i++)
	   			printf("[%d] %s\n",i,shbuffer[i]);
	   		i = 0;
	   		break;
   		}
   		if(input == '\n' || input == ' ') {
   			i = 0;
   		}
   		else {
   			buffer[i++] = input;				/*Coloca o caracter lido no input*/
   		}
   	}
	

    for(j = 0; j < NUM_THREADS_LEITOR; j++) {  
        pthread_join(threads[j],(void**)&retvalue);
        printf("Thread[%d] returned with value %d.\n",j,retvalue);
    }
    exit(0);
}