#include <stdio.h>

/* directivas de pre-compilador, definicao de constantes, etc. */

#define N_ESTADOS_MEMBROS 28

/* prototipos: */




/* MAIN */

int main()
{
    char command;  

    /* se for o caso, chamar as funcoes de inicializacao aqui */

    while (1) { 
		command = getchar();
        switch (command) {
        case '+':
            break;
            
        case 'm':
            break;
            
        case 'e':
            break;
            
        case 'x':
			return 0;
			
        default:
            printf("ERRO: Comando desconhecido\n");
        }
        getchar(); /* le o '\n' introduzido pelo utilizador */
    }
    return -1;
}

/* funcoes */

/* (...) */



