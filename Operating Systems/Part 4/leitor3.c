#include "header.h"
#include <pthread.h>
#include <string.h>
#define NUM_THREADS 10

int errado = 0;

/* Estrutura para passar os argumentos a pthread_create*/
struct arg_struct{
	/* num de caracteres do nome do ficheiro + '\n'*/
	char file_name[13];
	char comp_string[NUM_CHARS];
	int parte;
};


int open_lock_file(char *filename){
	
	/*Abre um ficheiro aleatoriamente*/
	
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
		perror("Error locking");
		close(file);
		exit(-1);
	}
	
	return file;
}

void close_unlock_file(file){
	flock(file,LOCK_UN);
	close(file);
}



void* read_file(void* argv){
	int i, file;
	char buffer[NUM_CHARS] = "";
	struct arg_struct *argumentos = (struct arg_struct *)argv; 
	
	file = open_lock_file(argumentos->file_name);

	
	lseek(file, NUM_CHARS-1 + ((argumentos->parte * (NUM_LINES-1) / NUM_THREADS) * (NUM_CHARS-1)) ,SEEK_SET);
	
	
	for(i = 0; i < (NUM_LINES-1) / NUM_THREADS; i++){
		read(file,buffer,NUM_CHARS-1);
		if(strcmp(buffer,argumentos->comp_string)!=0){
			/*Retorna -1*/
			printf("Parte: %d ERROR\n",argumentos -> parte );
			close(file);
			errado = 1;
			pthread_exit((void*) -1);	
		}
	}
	/* SUCESS*/
	printf("Parte: %d SUCESS\n",argumentos -> parte);
	close(file);
	pthread_exit((void*)0);
	
}


int main(int argc, char *argv[]) {
    struct timeval tvstart;
	

	int  lock_status, file, status, i,j, retvalue;
	char buffer[NUM_CHARS] = "";
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};

	/*Cria a estrutura com argumentos*/
	struct arg_struct *argumentos[NUM_THREADS];

    pthread_t threads[NUM_THREADS];
	
	
    if (argc == 1) {
    	printf("Por favor especifique o numero do ficheiro como argumento.\n");
    	return -1;
    }
	
	/*Faz o seed da funcao random*/
    gettimeofday(&tvstart, NULL);
	srand ( (tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000 );

	filename[X_POS] = argv[1][0];
	
	for(i=0;i<NUM_THREADS;i++){
		argumentos[i] = malloc(sizeof(struct arg_struct));
		strcpy(argumentos[i]->file_name,filename);
	}


	
	/*Abre o ficheiro*/
	file = open(filename, O_RDONLY);
	lock_status = flock(file, LOCK_SH);
	if(lock_status == -1){
		perror("Error locking");
		close(file);
		exit(-1);
	}
	
	/*Verifica 1 linha*/
	read(file, buffer, NUM_CHARS-1);
	for(i = 0; i <= NUM_STRINGS; i++){
		if(i == NUM_STRINGS){
			/*Nao e valida */
			flock(file,LOCK_UN);
			close(file);
			return -1;
		}
		if(strcmp(letters[i],buffer)==0){
			break;
		}
	}
	/*Coloca a String a comparar na estrutura argv*/

	for(j=0;j<NUM_THREADS;j++){
		strcpy(argumentos[j] -> comp_string, letters[i]);
	}
	
	
	/*Fecha o ficheiro*/
	flock(file,LOCK_UN);
	close(file);
    
    
    
    for(i=0; i < NUM_THREADS; i++) { 
        
        /*Cria NUM_THREADS threads que executam a funcao read_file
        com o argumento file_to_open (array de caracteres [X | \0], 
        em que X e o numero do ficheiro a abrir)*/
        
        printf("Main function here. Creating thread %d\n", i);

        /*
        argumento 1: sitio onde fica guardado o id da thread
        argumento 2: atributos
        argumento 3: nome da funcao que a thread vai correr
        argumento 4: argumento que queremos passar a funcao 
        */
        argumentos[i]->parte = i;
        status = pthread_create(&threads[i], NULL, read_file, (void*)argumentos[i]);
        
        
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
    for(i = 0; i < NUM_THREADS; i++) {
    	free(argumentos[i]);
    }
    if(errado == 1) {
    	printf("O FICHEIRO NAO ESTA COERENTE!\n");
    } else printf("O FICHEIRO ESTA COERENTE!\n");
    exit(0); 
}