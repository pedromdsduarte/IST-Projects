#include "header.h"
#include "escritormon.h"
#include <string.h>
#define NUM_THREADS 3
int lock, with_errors, terminate = 1;

void usr1_handler(){
	if(lock)
		lock=0;
	else
		lock=1;
	printf("\n\nLock Status: %s\n\n", lock ?"locked":"unlocked");
	
	
}

void usr2_handler(){
	if(with_errors)
		with_errors=0;
	else
		with_errors=1;
	printf("\n\nError Status: %s\n\n", with_errors ?"With Errors":"Without Errors");
}

void stop_handler(){
	if(terminate)
		terminate = 0;
	printf("\n\nTerminate Status: %s\n\n", terminate ?"Program is running. . .":"Program will terminate soon. . .");
}


void* write_file(){
	char filename[] = FILE_NAME;
	char letters[NUM_STRINGS][NUM_CHARS] = {AAA,BBB,CCC,DDD,EEE,FFF,GGG,HHH,III,JJJ};
	int i,file,lock_status,letter;
	int save_lock;
	printf("Executing write thread\n");
	while(terminate){
		save_lock = lock;
		filename[X_POS] = (char)(((int)'0')+ rand() % NUM_FILES);
		file = open(filename, O_RDWR | O_CREAT, S_IRWXU | S_IROTH);
		
		if(save_lock){
			lock_status = flock(file, LOCK_EX);
			if(lock_status == -1){
				perror("Error locking");
				close(file);
				exit(-1);
			}
		}else{
		}
		
		if(with_errors){
			for(i=0; i<NUM_LINES;i++){
				write(file,letters[rand()% NUM_STRINGS], NUM_CHARS-1);
			}
		}else{
			letter = rand()% NUM_STRINGS;
			for(i=0; i<NUM_LINES;i++){
				write(file,letters[letter], NUM_CHARS-1);
			}
		}
		
		if(save_lock)
			flock(file,LOCK_UN);
		
		close(file);
	}
	pthread_exit(NULL);
}


int main(){
	struct sigaction usr1, usr2, stop;
	struct timeval tvstart;
	int i, j, status;
	pthread_t threads[NUM_THREADS];
	gettimeofday(&tvstart, NULL);
	srand((tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000);

	usr1.sa_handler = usr1_handler;
	sigemptyset(&usr1.sa_mask);
	usr1.sa_flags = 0;
	sigaddset(&usr1.sa_mask,SIGUSR1);
	
	usr2.sa_handler = usr2_handler;
	sigemptyset(&usr2.sa_mask);
	usr2.sa_flags = 0;
	sigaddset(&usr2.sa_mask,SIGUSR2);
	
	
	stop.sa_handler = stop_handler;
	sigemptyset(&stop.sa_mask);
	sigaddset(&stop.sa_mask,SIGTSTP);
	stop.sa_flags = 0;


	sigaction(SIGUSR1, &usr1, NULL);
	sigaction(SIGUSR2, &usr2, NULL);
	sigaction(SIGTSTP, &stop, NULL);

	lock = 0;
	with_errors = 0;
	
	for(i=0; i < NUM_THREADS; i++){
		status = pthread_create(&threads[i],NULL, write_file, NULL);
		
		if(status != 0){
			printf("Oops. pthread create returned error code %d\n",status);
			exit(-1);
		}
	}

	for(j = 0; j < NUM_THREADS ; j++){
		pthread_join(threads[j],NULL);
		printf("Thread [%d] returned with value.\n",j);
	}
	return 0;
}