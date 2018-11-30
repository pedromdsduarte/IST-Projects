#include <stdio.h>
#include <stdlib.h>


/*************ESTRUTURAS*************/

typedef struct mensagem {
}*Item;

typedef struct node {
	Item item;
	struct node *next;
}*link;


/**************FUNCOES**************/



Item NewItem(char *message, int sender, int receiver) { 
	/*cria um novo item*/
	
}

void deleteItem(Item x) {
	/*liberta memoria alocada*/
}

void showItem(Item x) {
	/*mostra o conteudo de um item*/
}

int cmpItem(Item a, Item b) {
	/*retorna um valor :
	<0 se a < b; 
	=0 se a = b;
	>0 se a > b.*/
}

void printMenu() {
	printf("\t*** Comandos ***\n\n");
	printf("send e r info - regista a mensagem info enviada pelo emissor e para o utilizador r. Limite 500 caracteres (incluindo espacos).\n");
	printf("process u - imprime a proxima mensagem para utilizador u.\n");
	printf("list u - lista as mensagens em espera para o utilizador u por ordem de chegada.\n");
	printf("listsorted u - list por ordem alfabetica.\n");
	printf("kill u - apaga todas as mensagens em espera do utilizador u.\n");
	printf("quit - apaga todas as mensagens em espera e sai do programa.\n");
}

int main() {
	int N_USERS;
	printf("Introduza o numero de utilizadores: ");
	scanf("%d",&N_USERS);
	
	printMenu();
	return 0;
}
