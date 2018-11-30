#include "paralelo.h"


int main() {
	pid_t pid = getpid();
	int i, status;


/*comeca timer*/
	struct timeval tvstart; /* data de inicio */
	struct timeval tvend; /* data de fim */
	struct timeval tvduration; /* diferenca entre as duas datas */
	unsigned int duration; /* diferenca entre as datas em microssegundos */

	gettimeofday(&tvstart, NULL); /* ler a data actual */
	/* converter e imprimir a data */
	


	printf("PARENT ID: %d\n",pid);
	for(i = 0; i < NUM_CHILDS; i++) {	/* create child processes */
		pid = fork();
		if (pid == 0) break;
	}

	if (pid == 0) { 	/* child code */
		printf("### Child executing... (%d)\n",getpid());	
		execl("escritor","escritor",NULL);
		printf("I (child #%d) did not execute escritor!\n",getpid());
	} 
	else if (pid < 0) {
		printf("Error on fork()!\n");
		exit(-1);
	} 
	else { 	/* parent code */										
		printf("=== PARENT WAITING ===\n\n");
		waitpid(0,&status,0);

	/*termina timer*/
	gettimeofday(&tvend, NULL); /* ler a data actual */
	/* converter e imprimir a data */
	
	/* calcular e imprimir a diferenca de datas */
	tvduration.tv_sec = tvend.tv_sec - tvstart.tv_sec;
	tvduration.tv_usec = tvend.tv_usec - tvstart.tv_usec;
	duration = tvduration.tv_sec * 1000000 + tvduration.tv_usec;
	printf("tempo em segundos: %f ",duration * 0.000001);
		
		sleep(1);
		printf("\n=== PARENT EXITING ===\n");
		return 0;
	}

	return 0;
}
