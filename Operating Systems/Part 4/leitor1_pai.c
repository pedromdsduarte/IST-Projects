#include "leitor1.h"
#include "header.h"

int main() {
	pid_t pid = getpid();
	int i, status;
	int filenumber = 0;
	int runningChildren = NUM_CHILDS;
	char file_to_open[1];
	struct timeval tvstart;
	gettimeofday(&tvstart, NULL);
	srand ( (tvstart.tv_sec) * 1000 + (tvstart.tv_usec) / 1000 );
	
	printf("PARENT ID: %d\n",pid);
	printf("=== PARENT WAITING ===\n\n");
	for(i = 0; i < NUM_CHILDS; i++) {	/* create child processes */
		pid = fork();
		if (pid == 0) break;
	}
	
	if (pid == 0) { 	/* child code */
		filenumber = rand() % NUM_FILES;
		sprintf(file_to_open, "%d", filenumber);
		printf("### Child #%d is reading file #%d... \n",getpid(), filenumber);	
		execl("leitor","leitor",file_to_open,NULL);
		printf("I (child #%d) did not execute escritor!\n",getpid());
	} 
	
	else if (pid < 0) {
		printf("Error on fork()!\n");
		exit(-1);
	} 
	else { 	/* parent code */										
		while (runningChildren > 0) {
			while (wait(&status) == -1);	/*Espera*/
			printf("Child #%ld completed with status 0x%x.\n", (long)pid, status);
			runningChildren--;
		}

		printf("\n=== PARENT EXITING ===\n");
		return 0;
	}
	
	return 0;
}
