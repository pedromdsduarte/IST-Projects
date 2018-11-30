#include "header.h"
#include <signal.h>

#define INPUT fd[0]
#define OUTPUT fd[1]


int inputIsNotValid(char input[]) {
	char checkfile[FILE_NAME_SIZE];
	strcpy(checkfile,input);
	checkfile[X_POS] = 'x';
	if((strcmp(checkfile,FILE_NAME) == 0) && input[X_POS] >= '0' && input[X_POS] < '0' + NUM_FILES)
		return 0;
	return 1;

}


int main(){
    int fd[2];
    int i, status, pid_esc, pid_leitor;
    char buffer[FILE_NAME_SIZE];
    char input;
    char send[1];
    
    if(pipe(fd) == -1) {
        perror("pipe");
        exit(EXIT_FAILURE);
    }

    
    pid_esc = fork();
    
    /* ######### ESCRITOR ########*/
    if(pid_esc == 0){ 
        execl("escritormon","escritormon",NULL); 
    }
    else{ 
        pid_leitor = fork();
        
        /* ####### LEITOR #######*/
        if(pid_leitor == 0) {
            close(OUTPUT);
            if(dup2(INPUT,0) == -1)
                perror("dup2");
            execl("leitormon","leitormon",NULL); 
        }
        
        /* ######## PAI ########*/
        else{ 
            i = 0;
            while(1) {
           		read(STDIN_FILENO, &input, sizeof(char));
           		send[0] = input;
           		write(OUTPUT,send,sizeof(char));
           		if(input == '\n') {
           		    buffer[i] = '\0';
           		    if(strcmp(buffer,IL) == 0) {
                        kill(pid_esc,SIGUSR1);
           		    }
                    else if(strcmp(buffer,IE) == 0) {
                        kill(pid_esc,SIGUSR2);
                    }
                    else if(strcmp(buffer,EXIT) == 0) {
                        printf("Exiting...\n");
                        kill(pid_esc,SIGTSTP);
                        kill(pid_leitor,SIGTSTP);
                        wait(&status);
                        return 0;
                    }
                    memset(buffer,0,FILE_NAME_SIZE);
                    i=0;
                    continue;
           		}
           		if(input == ' ') {
           		    i = 0;
           		    continue;
           		}
           		else {
           			buffer[i++] = input;				
           		}
       	    }
            wait(&status);
        }
    }
    return 0;
}