Para correr o projecto compilar usando o comando make

Qualquer comando para correr a aplicação deverá começar com a palavra java
exemplo:
"java user SID [-n ECPname] [-p ECPport]"
"java ECP [-p ECPport]"
"java TES [-f Topic] [-p TESport] [-n ECPname] [-e ECPport]"

/*****
Os ficheiros executáveis encontram-se dentro da pasta correspondente.
******/

O servidor TES tem uma flag obrigatória [-f Topic] que é um inteiro de 1 a 3 (número de tópicos que foram feitos).

A pasta Questionnaires contém cada um dos questionários,a que o utilizador deverá responder, e as
respectiva correcções.

O ficheiro scores.txt é o ficheiro onde vão ficar guardados os resultados de cada utilizador
O ficheiro topics.txt contém os topicos com as respectivas Portas e endereços.
A versão de Java utilizada para a implementação deste projecto foi 1.7.0_79

Grupo 15
78451	João Pedro de Barros Gomes Cruz Almeida
79112	Gonçalo Fialho Pires
78328	Pedro Miguel dos Santos Duarte