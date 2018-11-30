#include <stdio.h>

/* directivas de pre-compilador, definicao de constantes, etc. */
#define NUM_PARTIDOS 12
#define NUM_PAISES 28
#define VOTACAO 2

	
char paises[NUM_PAISES][3] = {"DE","AT","BE","BG","CY","HR","DK","SK","SI","ES","EE","FI","FR","GR","HU","IE","IT","LV","LT","LU","MT","NL","PL","PT","GB","CZ","RO","SE"};
char partidos[NUM_PARTIDOS][4] = {"EPP","PES","LDE","EGP","ECR","ELP","ELD","EDP","EAF","EMN","ECP","AED"};

int num_deputados[NUM_PAISES] = {96,18,21,17,6,11,13,13,8,54,6,13,74,21,21,11,73,8,11,6,6,26,51,21,73,21,32,20};


float votos[NUM_PAISES][NUM_PARTIDOS][VOTACAO];  /* 2 arrays (Votos iniciais e quocientes)   */
int eleitos[NUM_PAISES][NUM_PARTIDOS];





int num_votos, EM, PPE;


/* prototipos: */

void adicionaVotos(int EM, int PPE, int num_votos);
void deputadosEleitosEM(int EM);
void deputadosEleitosUE();
void inicializaPPE();
void metodo(int EM);


/* MAIN */

int main() {
    char command;  
	/*inicializaPPE();*/
    /* se for o caso, chamar as funcoes de inicializacao aqui */

    while (1) { 
		command = getchar();
        switch (command) {
        case '+':
			scanf(" %d %d %d",&EM, &PPE, &num_votos); 
			adicionaVotos(EM, PPE, num_votos);
			metodo(EM-1);
            break;
            
        case 'm':
			scanf(" %d", &EM);
			deputadosEleitosEM(EM-1);
            break;
            
        case 'e':
			deputadosEleitosUE();
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

void adicionaVotos(int EM, int PPE, int num_votos) {
	int i;

	for(i=0;i<NUM_PARTIDOS;i++){
		votos[EM-1][i][1] = votos[EM-1][i][0];
	}
	votos[EM-1][PPE-1][0] = votos[EM-1][PPE-1][0] + num_votos;
	votos[EM-1][PPE-1][1] = votos[EM-1][PPE-1][0] ;
	
	/*printf("Votos de %s em %s: %d\n",partidos[PPE-1],paises[EM-1],votos[EM-1][PPE-1][0]);*/
	
	
}

void deputadosEleitosEM(int EM){
	int i;
	printf("  ");
	for(i=0;i<NUM_PARTIDOS;i++)
		printf(" %s",partidos[i]);
	printf("\n");
	printf("%s",paises[EM]);
	for(i=0;i<NUM_PARTIDOS;i++){
		printf(" %3d",eleitos[EM][i]); 
	}
	printf("\n");

}


void deputadosEleitosUE(){
	int soma;
	int i,j;
	
	printf("  ");
	for(i=0;i<NUM_PARTIDOS;i++)
		printf(" %s",partidos[i]);
	
	printf("\n");
	
	for(i=0;i<NUM_PAISES;i++){
		printf("%s",paises[i]);
		for(j=0;j<NUM_PARTIDOS;j++){
			printf(" %3d",eleitos[i][j]);
		}
		printf("\n");
	}
	
	printf("TE");
		
	for(i=0;i<NUM_PARTIDOS;i++){
		soma = 0;
		for(j=0;j<NUM_PAISES;j++){
			soma = soma + eleitos[j][i];
		}
		printf(" %3d",soma);

	}
	printf("\n");
		

	
}

void metodo(int EM){
	int p = 0; /* m-> guarda o maior quociente de votos  ;  p-> indice do partido*/
	int i; 
	/*int j;*/
	
	int deputados;
	deputados = num_deputados[EM];
	
	
	for(i=0;i<NUM_PARTIDOS;i++){
		eleitos[EM][i] = 0;
	}
	
	while(deputados != 0){
		/*printf("Ciclo: %d\n",deputados);*/
		float m = 0;
		/*indice = 0  guarda o indice do partido com maior quociente de votos*/
		for(i=0;i<NUM_PARTIDOS;i++){
			if(votos[EM][i][1] == m){
				/*printf("Iguais em Ciclo %d, valor: %d , %d\n",13-deputados, eleitos[EM][p],eleitos[EM][i]);*/
				if(eleitos[EM][i] < eleitos[EM][p]){
					m = votos[EM][i][1] ;
					p = i;
			
				}else if(eleitos[EM][i]==eleitos[EM][p]){
					m = votos[EM][i][1];
					printf("%d\t%f\t%f\n",13-deputados,m,votos[EM][p][1]);
				}

			}else if(votos[EM][i][1] > m){				
				m = votos[EM][i][1] ;
				p = i;
			}
		}
		
		eleitos[EM][p] = eleitos[EM][p] + 1;
		
		deputados = deputados - 1;
		
		votos[EM][p][1] = votos[EM][p][0] / (eleitos[EM][p] + 1);
		/* printf("\tPartido Vencedor %s: %d\n\tDeputados do partido eleitos: %d\n",partidos[p],votos[EM][p][1],eleitos[EM][p]);*/


		
	}	
	
	
	/*printf("País %s: \n",paises[EM]);
	for(j=0;j<NUM_PARTIDOS;j++){
		printf(" ");
		printf("Votos iniciais partido %s: %d\t\t finais: %d\teleitos: %d\n",partidos[j],votos[EM][j][0],votos[EM][j][1],eleitos[EM][j]);
	}	*/
	
	
	
	
	
	
	
}
