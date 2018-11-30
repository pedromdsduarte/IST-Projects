#include "header.h"
#include <pthread.h>
#include <string.h>
#define NUM_THREADS 3



int open_lock_file(char *filename, char * rep){
	
	/*Abre um ficheiro aleatoriamente*/
	
	int lock_status, file;
	filename[X_POS] = rep[0];
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
		perror("Error locking");
		close(file);
		exit(-1);
	}
	
	printf("--> Ficheiro: %d...\n",atoi(rep));
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

void* read_file(void* fnptr) {
    char buffer[NUM_CHARS] = "";
	char first[NUM_CHARS]= "";
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};

	int fline, contador = 0, file;
    char* file_number = "";
    file_number = (char*)fnptr;     /*converte o argumento que foi passado (void*) --> (char*)*/
    
    file = open_lock_file(filename, file_number);       /*abre-se o ficheiro com o lock*/
    read(file, buffer, NUM_CHARS-1);
    strcpy(first,buffer);                           

    
    
    /* Verifica se a primeira linha e valida*/
	if((confirma_string(first, letters)) != 0){
		close_unlock_file(file);
		printf("Something went wrong (Confirma String)...\n");
		pthread_exit((void*)-1);
	}

	
	/*Verifica as restantes linhas do ficheiro*/
	while(strcmp(buffer,first) == 0) {
	    fline = read(file, buffer,NUM_CHARS-1);   
        contador++;
		if(fline == -1) {                               /*se o read der erro*/
		    perror("");   
		    printf("Error in read (contador em %d)\n",contador);
		    pthread_exit((void*)EXIT_FAILURE);              
		}
		if(fline == 0){                        
			if (contador != NUM_LINES) {
			    close_unlock_file(file);
			    printf("Check!\n");
			    pthread_exit((void*)0);
			}
		}

	}

	printf("Something went wrong (Durante a leitura do ficheiro)...\n");
	close_unlock_file(file);
	pthread_exit((void*)-1);

}

int main() {
    struct timeval tvstart;
	int retvalue;
	int status, i, j, filenumber;
	char file_to_open[2];


    pthread_t threads[NUM_THREADS];
	
	/*Faz o seed da funcao random*/
    gettimeofday(&tvstart, NULL);
	srand ( (tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000 );


    for(i=0; i < NUM_THREADS; i++) { 
        
        /*Cria 3 threads que executam a funcao read_file
        com o argumento file_to_open (array de caracteres [X | \0], 
        em que X e o numero do ficheiro a abrir)*/
        
        printf("Main function here. Creating thread %d\n", i);
        filenumber = rand() % NUM_FILES;        
        sprintf(file_to_open, "%d", filenumber);
        
        /*
        argumento 1: sitio onde fica guardado o id da thread
        argumento 2: atributos
        argumento 3: nome da funcao que a thread vai correr
        argumento 4: argumento que queremos passar a funcao
        */
        status = pthread_create(&threads[i], NULL, read_file, (void*)file_to_open);
         
        
        if (status != 0) {
            printf("Oops. pthread create returned error code %d\n", status);
            exit(-1);
        }
    }

    for(j = 0; j < NUM_THREADS; j++) {  
        
        /*Faz o retorno das threads*/
        
        pthread_join(threads[j],(void**)&retvalue);
        printf("Thread[%d] returned with value %d.\n",j,retvalue);
    }
    exit(0);
}