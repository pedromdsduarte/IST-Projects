%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%
%
%       GRUPO NUM: 47
%       ALUNOS: 78093 - Goncalo Santos
%				78328 - Pedro Duarte
%				79112 - Goncalo Fialho
%
%               ATENCAO: NAO USAR ACENTOS OU CEDILHAS
%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%%%%%%%%%%%%%%% PECAS %%%%%%%%%%%%%%%

peca(triangulo,azul).
peca(triangulo,amarelo).
peca(triangulo,vermelho).
peca(quadrado,azul).
peca(quadrado,amarelo).
peca(quadrado,vermelho).
peca(circulo,azul).
peca(circulo,amarelo).
peca(circulo,vermelho).

%%%%%%%%%%%%%% PISTAS %%%%%%%%%%%%%%%

% Trios %
trioLeft(Peca,Linha,Coluna,Tabuleiro) :- coloca(Peca, Linha, Coluna, Tabuleiro); coloca(Peca, Linha, middle, Tabuleiro).
trioRight(Peca,Linha,Coluna,Tabuleiro) :- trioLeft(Peca,Linha,Coluna,Tabuleiro).

% Cobra %
cobra(Peca,_,left,Tabuleiro) :- coloca(Peca,top,left,Tabuleiro); coloca(Peca,top,middle,Tabuleiro).
cobra(Peca,Linha,right,Tabuleiro) :- coloca(Peca,Linha,middle,Tabuleiro); coloca(Peca,Linha,right,Tabuleiro).


% Triangulo Simples %
tSimples(Peca,top,Coluna,Tabuleiro) :- coloca(Peca,top,Coluna,Tabuleiro); coloca(Peca,center,Coluna,Tabuleiro).
tSimples(Peca,bottom,_,Tabuleiro) :- coloca(Peca,center,middle,Tabuleiro); coloca(Peca,bottom,middle,Tabuleiro).

% Triangulo Invertido %	
tInvertido(Peca,top,_,Tabuleiro) :- coloca(Peca,top,middle,Tabuleiro); coloca(Peca,center,middle,Tabuleiro).
tInvertido(Peca,bottom,Coluna,Tabuleiro) :- coloca(Peca,center,Coluna,Tabuleiro); coloca(Peca,bottom,Coluna,Tabuleiro).
	
% Triangulo Left %
tLeft(Peca,Linha,left,Tabuleiro) :- coloca(Peca,Linha,left,Tabuleiro); coloca(Peca,Linha,middle,Tabuleiro).
tLeft(Peca,center,right,Tabuleiro) :- coloca(Peca,center,middle,Tabuleiro); coloca(Peca,center,right,Tabuleiro).
	
% Triangulo Right %	
tRight(Peca,Linha,right,Tabuleiro) :- coloca(Peca,Linha,middle,Tabuleiro); coloca(Peca,Linha,right,Tabuleiro).
tRight(Peca,center,left,Tabuleiro) :- coloca(Peca,center,left,Tabuleiro); coloca(Peca,center,right,Tabuleiro).

% Diagonais %
	
diagonalGrave(Peca,Linha,Coluna,Tabuleiro) :- coloca(Peca,Linha,Coluna,Tabuleiro); coloca(Peca,Linha,middle,Tabuleiro);
												coloca(Peca,center,Coluna,Tabuleiro); coloca(Peca,center,middle,Tabuleiro).
diagonalAguda(Peca,Linha,Coluna,Tabuleiro) :- diagonalGrave(Peca,Linha,Coluna,Tabuleiro).


% Cantos %
cantoTopLeft(Peca, Linha, Coluna, Tabuleiro) :- quadrado(Peca, Linha, Coluna, Tabuleiro).
cantoTopRight(Peca,Linha,Coluna,Tabuleiro) :- quadrado(Peca, Linha, Coluna, Tabuleiro).
cantoBottomLeft(Peca,Linha,Coluna,Tabuleiro) :- quadrado(Peca, Linha, Coluna, Tabuleiro).
cantoBottomRight(Peca,Linha,Coluna,Tabuleiro) :- quadrado(Peca, Linha, Coluna, Tabuleiro).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%			

% PREDICADOS AUXILIARES %

% escolhe/3: devolve true se o terceiro argumento e igual ao primeiro argumento sem o segundo argumento

escolhe([P|R],P,R).
escolhe([P|R],E,[P|S]) :- escolhe(R,E,S).

% perm/2: devolve true se o primeiro e o segundo argumentos correspondem a listas com os mesmos elementos mas por ordem diferente

perm([],[]).
perm(L,[P|R]) :- escolhe(L,P,L1),
				 perm(L1,R).

% lista com todas as pecas do jogo %

pecas([peca(triangulo,azul),
		peca(triangulo,amarelo),
		peca(triangulo,vermelho),
		peca(quadrado,azul),
		peca(quadrado,amarelo),
		peca(quadrado,vermelho),
		peca(circulo,azul),
		peca(circulo,amarelo),
		peca(circulo,vermelho)]).


% check/2: devolve true se o segundo tabuleiro for igual ao primeiro tabuleiro mas com as pecas em falta
		
check(Tabuleiro,TabuleiroFinal) :- pecas(X), perm(X,Tabuleiro), TabuleiroFinal = Tabuleiro.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%	