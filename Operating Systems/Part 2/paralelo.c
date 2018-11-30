#include "paralelo.h"

int main() {
	pid_t pid = getpid();
	int i, status;
	time_t start, stop;

	time(&start); /* start the timer */
	printf("PARENT ID: %d\n",pid);
	for(i = 0; i < NUM_CHILDS; i++) {	/* create child processes */
		pid = fork();
		if (pid == 0) break;
	}

	if (pid == 0) { 	/* child code */
		printf("### Child executing... (%d)\n",getpid());	
		execl("escritor","",0);
		printf("I (child #%d) did not execute escritor!\n",getpid());
	} 
	else if (pid < 0) {
		printf("Error on fork()!\n");
		exit(-1);
	} 
	else { 	/* parent code */										
		printf("=== PARENT WAITING ===\n\n");
		waitpid(0,&status,0);
		time(&stop); /*stop the timer */
		sleep(1);
		printf("\n=== PARENT EXITING ===\n");
		printf("Execution time: ~%.1f seconds\n",difftime(stop,start));
		return 0;
	}

	return 0;
}