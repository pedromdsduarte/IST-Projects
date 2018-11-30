; Instituicao   - Instituto Superior Tecnico
; Disciplina    - Inteligencia Artificial
; Grupo         - 3
; 
;Alunos         - Goncalo Fialho 79112
;               - Joao Pedro Almeida 78451
;               - Pedro Duarte 78328

;:: ______________________________
;;;|                              |
;;;|         TIPO ACCAO           |
;;;|______________________________|

;;; cria-accao: inteiro x array -> accao
(defun cria-accao (coluna array)
    (cons coluna array)
)

;;; accao-coluna : accao -> inteiro
(defun accao-coluna (accao)
    (car accao)
)

;;; accao-peca : accao -> peca
(defun accao-peca (accao)
    (cdr accao)
)

;:: ______________________________
;;;|                              |
;;;|       TIPO TABULEIRO         |
;;;|______________________________|

;;; cria-tabuleiro : {} -> tabuleiro
(defun cria-tabuleiro ()
    (make-array '(18 10))
)

;;; copia-tabuleiro : tabuleiro -> tabuleiro
(defun copia-tabuleiro (tabuleiro)
    (let ((novo_tabuleiro (cria-tabuleiro)))
        (dotimes (linha (array-dimension tabuleiro 0) novo_tabuleiro)
            (dotimes (coluna (array-dimension tabuleiro 1))
                (setf (aref novo_tabuleiro linha coluna) (aref tabuleiro linha coluna))
            )
        )
    )
)

;;; tabuleiro-preenchido-p : tabuleiro x inteiro x inteiro -> logico
(defun tabuleiro-preenchido-p (tabuleiro linha coluna)
    (aref tabuleiro linha coluna)
)

;;; tabuleiro-altura-coluna : tabuleiro x inteiro -> inteiro
(defun tabuleiro-altura-coluna (tabuleiro coluna)
    (let ((altura 0))
        (dotimes (linha (array-dimension tabuleiro 0) altura)
            (if (eql t (aref tabuleiro linha coluna))
                (setf altura (1+ linha))
            )
        )
    )
)

;;; tabuleiro-linha-completa-p : tabuleiro x inteiro -> logico
(defun tabuleiro-linha-completa-p (tabuleiro linha)
    (dotimes (coluna (array-dimension tabuleiro 1) t)
        (if (eql nil (aref tabuleiro linha coluna))
            (return nil)
        )
    )
)

;;; tabuleiro-preenche! : tabuleiro x inteiro x inteiro -> {}
(defun tabuleiro-preenche! (tabuleiro linha coluna)
    (if (and (< linha (array-dimension tabuleiro 0)) (>= linha 0) (< coluna (array-dimension tabuleiro 1)) (>= coluna 0))
        (setf (aref tabuleiro linha coluna) t)
    )
)

;;; tabuleiro-remove-linha! : tabuleiro x inteiro -> {}
(defun tabuleiro-remove-linha! (tabuleiro line)
    (if (= line (1- (array-dimension tabuleiro 0)))
        (dotimes (coluna (array-dimension tabuleiro 1) )
            (setf (aref tabuleiro line coluna) nil)
        )
        (progn 
            (loop
                for i from line to (1- (1- (array-dimension tabuleiro 0)))
            do
                (dotimes (coluna (array-dimension tabuleiro 1))
                    (setf (aref tabuleiro i coluna) (aref tabuleiro (1+ i) coluna))
                )
            )
            (dotimes (coluna (array-dimension tabuleiro 1) )
                (setf (aref tabuleiro (1- (array-dimension tabuleiro 0)) coluna) nil)
            )
        )
    )
)

;;; tabuleiro-topo-preenchido-p : tabuleiro -> logico
(defun tabuleiro-topo-preenchido-p (tabuleiro)
    (dotimes (coluna (array-dimension tabuleiro 1) nil)
        (if (eql t (aref tabuleiro (1- (array-dimension tabuleiro 0)) coluna))
            (return t)
        )
    )
)

;;; tabuleiros-iguais-p : tabuleiro x tabuleiro -> logico
(defun tabuleiros-iguais-p (tabuleiro1 tabuleiro2)
    (let ((iguais t))
        (dotimes (linha (array-dimension tabuleiro1 0) iguais)
            (dotimes (coluna (array-dimension tabuleiro1 1))
                (if (not (eql (aref tabuleiro1 linha coluna) (aref tabuleiro2 linha coluna)))
                    ;(setf iguais nil)
                    (nil) ;nao basta isto?
                )
            )
        )
    )
)

;;; tabuleiro->array : tabuleiro -> array
(defun tabuleiro->array (tabuleiro)
    tabuleiro
)

;;; array->tabuleiro : array -> tabuleiro
(defun array->tabuleiro (array)
    (let ((aux (make-array (list (array-dimension array 0) (array-dimension array 1)))))
        (dotimes (linha (array-dimension array 0) aux)
            (dotimes (coluna (array-dimension array 1))
                (setf (aref aux linha coluna) (aref array linha coluna))
            )
        )
    )
)

;:: ______________________________
;;;|                              |
;;;|         TIPO ESTADO          |
;;;|______________________________|

(defstruct estado pontos pecas-por-colocar pecas-colocadas tabuleiro)

;;; copia-estado : estado -> estado
(defun copia-estado (state)
    (make-estado 
        :pontos (estado-pontos state) 
        :pecas-por-colocar (copy-list (estado-pecas-por-colocar state))
        :pecas-colocadas (copy-list (estado-pecas-colocadas state))
        :tabuleiro (copia-tabuleiro (estado-tabuleiro state))
    )
)

;;; estados-iguais-p : estado x estado -> logico
(defun estados-iguais-p (estado1 estado2)
    (cond
        ((not (eql (estado-pontos estado1) (estado-pontos estado2))) nil)
        ((not (equal (estado-pecas-por-colocar estado1) (estado-pecas-por-colocar estado2))) nil)
        ((not (equal (estado-pecas-colocadas estado1) (estado-pecas-colocadas estado2))) nil)
        ((not (tabuleiros-iguais-p (estado-tabuleiro estado1) (estado-tabuleiro estado2))) nil)
        (t t)
    )
)

;;; estado-final-p : estado -> logico
(defun estado-final-p (state)
    (cond
        ((null (estado-pecas-por-colocar state)) t)
        ((tabuleiro-topo-preenchido-p (estado-tabuleiro state)) t)
        (t nil)
    )
)

;:: ______________________________
;;;|                              |
;;;|        TIPO PROBLEMA         |
;;;|______________________________|

(defstruct problema estado-inicial solucao accoes resultado custo-caminho)

;:: ______________________________
;;;|                              |
;;;|       FUNCOES PROCURA        |
;;;|______________________________|

;;; solucao : estado -> logico
;;; devolve "t" se o estado dado for solucao,
;;; e "nil" caso contrario
(defun solucao (state)
    (cond
        ((tabuleiro-topo-preenchido-p (estado-tabuleiro state)) nil)
        ((not (null (estado-pecas-por-colocar state))) nil)
        (t t)
    )
)

;;; accoes : estado -> lista
;;; devolve uma lista correspondente as accoes possiveis 
;;; a partir de um dado estado do tabuleiro e pecas
(defun accoes (state)
    (let ((lista-accoes (list)) (pecas (retorna-pecas (first (estado-pecas-por-colocar state)))))
        (if (tabuleiro-topo-preenchido-p (estado-tabuleiro state))
            (setf lista-accoes nil)
            (progn
                (loop
                    for peca from 0 to (1- (length pecas))
                do
                    (loop
                        for coluna from 0 to 17
                    do
                        (if (posicao-valida (estado-tabuleiro state) (nth peca pecas) (- (- 18 1) (1- (array-dimension (nth peca pecas) 0))) coluna) 
                            (push (cria-accao coluna (nth peca pecas)) lista-accoes)
                        )
                    )
                    
                )
            )
        )
        (remove nil (reverse lista-accoes))
    )
)

;;; retorna-pecas : peca -> lista
;;; retorna uma lista de pecas correspondente a todas as possiveis
;;; rotacoes da peca dada
(defun retorna-pecas (peca)
    (let ((lista-pecas '()))
        (cond
            ((eql peca 'i) (setf lista-pecas (copy-list (list peca-i0 peca-i1))))
            ((eql peca 'l) (setf lista-pecas (copy-list (list peca-l0 peca-l1 peca-l2 peca-l3))))
            ((eql peca 'j) (setf lista-pecas (copy-list (list peca-j0 peca-j1 peca-j2 peca-j3))))
            ((eql peca 'o) (setf lista-pecas (copy-list (list peca-o0))))
            ((eql peca 's) (setf lista-pecas (copy-list (list peca-s0 peca-s1))))
            ((eql peca 'z) (setf lista-pecas (copy-list (list peca-z0 peca-z1))))
            ((eql peca 't) (setf lista-pecas (copy-list (list peca-t0 peca-t1 peca-t2 peca-t3))))
        )
    )
)

;;; posicao-valida : tabuleiro x peca x inteiro x inteiro -> logico
;;; devolve "t" se a peca colocada na posicao (linha, coluna) 
;;; (dadas pelo 1o e 2o inteiros, respectivamente) e valida no tabuleiro dado, 
;;; e "nil" caso contrario
(defun posicao-valida (tabuleiro peca linha coluna)
    (cond
        ((or (< 18 (+ linha (array-dimension peca 0))) (< 10 (+ coluna (array-dimension peca 1)))) nil)
        ((zona-limpa tabuleiro (cria-accao coluna peca) linha) t)
        (t nil)
    )
)

;;; start-at : peca -> inteiro
;;; indica a coluna que tem o primeiro espaco preenchido pela peca
(defun start-at (peca)
    (loop 
        for i from 0 to (1- (array-dimension peca 0))
        do
            (if (aref peca i 0)
                (return i)
            )
    )
)

;;; zona-limpa : tabuleiro x accao x linha -> logico
;;; recebe um tabuleiro, uma accao e uma linha e devolve verdadeiro
;;; se a peca colocada nao sobrepuser a noutras ja colocadas no tabuleiro
;;; e falso caso contrario
(defun zona-limpa (tab action line)
    (let ((incremento t))
        (dotimes (linha (array-dimension (accao-peca action) 0)  )
            (dotimes (coluna (array-dimension (accao-peca action) 1 ))
                (if (and (aref (accao-peca action) linha coluna) (tabuleiro-preenchido-p tab  (+ linha line) (+ coluna (accao-coluna action))))
                    (setf incremento nil)                           
                )
            )
        )
        incremento
    )
)


;;; resultado : estado x accao -> estado
;;; devolve o estado resultante de aplicar a accao ao estado dado
(defun resultado (state action)
    (let ((cleared_lines 0) (novo_estado (copia-estado state)) (acima 0) (aux 0) (preenchido 0))
        
        ;Verifica se posicao onde vai ser colocada a peca nao esta preenchida por outras
        (setq preenchido 
            (block exit
                (loop
                    for i from (- (- 18 1) (1- (array-dimension (accao-peca action) 0))) downto 0
                    do
                    (when (not (zona-limpa (estado-tabuleiro novo_estado) action i))
                        (return-from exit (+ 1 i))
                    )
                )
                0
            )
        )
        
        ;Verifica se pode colocar na linha abaixo (se as posicoes abaixo nao estao preenchidas)
        (dotimes (coluna (array-dimension (accao-peca action) 1))
            (if (tabuleiro-preenchido-p (estado-tabuleiro novo_estado) preenchido coluna)
                (setf acima 1)
            )
            ;Caso em que e a linha 0
            (if (and (eq 0 preenchido) (> (start-at (accao-peca action)) 0))
                (setf aux -1)
            )
        )
        
        (dotimes (linha (array-dimension (accao-peca action) 0)  )   
            (dotimes (coluna (array-dimension (accao-peca action) 1 ))
                (if (eql t (aref (accao-peca action) linha coluna))
                    (tabuleiro-preenche! (estado-tabuleiro novo_estado) (+ linha preenchido) (+ (accao-coluna action) coluna)) 
                )
            )
        )
        (setf acima 0)
        (setf (estado-pecas-colocadas novo_estado) (cons (pop (estado-pecas-por-colocar novo_estado)) (estado-pecas-colocadas novo_estado)))
        (cond
            ((tabuleiro-topo-preenchido-p (estado-tabuleiro novo_estado)) novo_estado)
            (t  (loop
                    for i from (1- 18) downto 0
                do
                    (if (tabuleiro-linha-completa-p (estado-tabuleiro novo_estado) i)
                        (progn (tabuleiro-remove-linha! (estado-tabuleiro novo_estado) i)
                            (setf cleared_lines (1+ cleared_lines)))
                    )
                )
                (setf (estado-pontos novo_estado) (+ (get-score cleared_lines) (estado-pontos novo_estado)))
                novo_estado
            )
        )
    )
)

;;; get-score : inteiro -> inteiro
;;; devolve os pontos em funcao do numero de linhas limpas
(defun get-score (cleared_lines)
    (cond
        ((= cleared_lines 1) 100)
        ((= cleared_lines 2) 300)
        ((= cleared_lines 3) 500)
        ((= cleared_lines 4) 800)
        (t 0)
    )
)

;;; qualidade : estado -> inteiro
;;; devolve um inteiro correspondente ao valor negativo dos pontos ganhos 
;;; ate ao momento
(defun qualidade (state)
    (- (estado-pontos state))
)

;;; custo-oportunidade : estado -> inteiro
;;; devolve um inteiro correspondente ao custo de oportunidade de todas as
;;; accoes realizadas ate ao momento, assumindo que e sempre possivel 
;;; fazer o maximo de pontos por cada peca
(defun custo-oportunidade (state)
    (let ((cost 0))
        (loop
            for i from 0 to (1- (length (estado-pecas-colocadas state)))
        do
            (setf cost (+ cost (maximo-conseguido (nth i (estado-pecas-colocadas state)))))
        )
        (- cost (estado-pontos state))
    )
)

;;; maximo-conseguido : peca -> inteiro
;;; devolve o numero maxido de pontos possivel dada uma peca
(defun maximo-conseguido (peca)
    (cond
        ((eql peca 'i) 800)
        ((eql peca 'l) 500)
        ((eql peca 'j) 500)
        ((eql peca 'o) 300)
        ((eql peca 's) 300)
        ((eql peca 'z) 300)
        ((eql peca 't) 300)
        (t 0)
    )
)




;:: _______________________________
;;;|                               |
;;;| FUNCOES/ESTRUTURAS AUXILIARES |
;;;|_______________________________|

;;; Estrutura auxiliar 
(defstruct node estado parent parent-action f-score order)

;;; reconstruct-path: node -> lista
;;; dado um no, devolve uma lista de accoes correspondente ao
;;; caminho desde o estado inicial ate ao dado
(defun reconstruct-path (goal)
    (let (
        (total-path (list))
        (node goal))
        
        (loop do
            (push (node-parent-action node) total-path)
            (setf node (node-parent node))
        while (not (null node)))
        (remove nil total-path)
    )
)

;;; select : lista -> node
;;; devolve o proximo no a ser escolhido pela procura, conforme o 
;;; menor valor de f ou aquele que foi aberto ha mais tempo
(defun select (open)
    (let ((next (first open)))
        (dolist (node open)
            ;escolhe o node com menor f ou, em caso de empate, aquele que foi aberto ha menos tempo
            (if (or (< (node-f-score node) (node-f-score next)) (and (= (node-f-score node) (node-f-score next)) (> (node-order node) (node-order next))))
                (setf next node)
            )
        )
        next
    )
)

;;; calculate-aggregate-height : estado -> inteiro
;;; calcula a altura total do tabuleiro
(defun calculate-aggregate-height (estado)
    (let (
        (tabuleiro (estado-tabuleiro estado))
        (sum 0))
        (dotimes (i (array-dimension tabuleiro 1))
            (incf sum (tabuleiro-altura-coluna tabuleiro i))
        )
        sum
    )
)

;;; coluna-mais-alta : estado -> inteiro
;;; devolve um inteiro que corresponde a coluna mais alta do tabuleiro
(defun coluna-mais-alta (estado)
    (let (
        (tabuleiro (estado-tabuleiro estado))
        (coluna 0)
        )
        (dotimes (i (array-dimension tabuleiro 1))
            (if (> (tabuleiro-altura-coluna tabuleiro i) (tabuleiro-altura-coluna tabuleiro coluna))
                (setf coluna i))
        )
        coluna
    )
)

;;; calculate-bumpiness : estado -> inteiro
;;; calcula a "irregularidade" do tabuleiro (altos e baixos)
(defun calculate-bumpiness (estado)
    (let (
        (tabuleiro (estado-tabuleiro estado))
        (bumpiness 0)
        (coluna1 0)
        (coluna2 1))
        
        (dotimes (i (1- (array-dimension tabuleiro 1)))
            (setf bumpiness (+ bumpiness (abs (- (tabuleiro-altura-coluna tabuleiro (+ coluna1 i)) (tabuleiro-altura-coluna tabuleiro (+ coluna2 i))))))
        )
    bumpiness
    )
)

;;; calculate-bubbles : estado -> inteiro
;;; calcula o numero de "buracos" do tabuleiro
(defun calculate-bubbles (estado)
    (let ((tabuleiro (estado-tabuleiro estado)) (holes 0))
        (dotimes (coluna 9)
            (dotimes (linha (tabuleiro-altura-coluna tabuleiro coluna))
                (if (not (tabuleiro-preenchido-p tabuleiro linha coluna))
                    (incf holes)
                )
            )
        )
        holes
    )
)

;;; heuristica-best : estado -> inteiro
;;; computa a heuristica utilizada pela procura-best
(defun heuristica-best (estado)
    (let (
        (h1 (tabuleiro-altura-coluna (estado-tabuleiro estado) (coluna-mais-alta estado)))
        (h2 (calculate-bumpiness estado))
        (h3 (calculate-aggregate-height estado))
        (h4 (calculate-bubbles estado))
        (h5 (estado-pontos estado))
        )

    (+ (+ (* 30 h1) (* 5 h2) (* 10 h3) (* 25 h4)) (- (* h5 0.6)))
    )
)

;:: ______________________________
;;;|                              |
;;;|           PROCURAS           |
;;;|______________________________|

;;; procura-pp: problema -> lista accoes
(defun procura-pp (problema)
    (let* (
        (open (list))
        (start (problema-estado-inicial problema))
        (actions (list))
        (new-child nil)
        (current (make-node :estado start :parent nil :parent-action nil)))

        (push current open)
        
        (loop do
            (setf current (first open))
            (if (funcall (problema-solucao problema) (node-estado current))
                (return-from procura-pp (reconstruct-path current))
                (progn
                    (setf open (remove current open))
                    (setf actions (funcall (problema-accoes problema) (node-estado current)))
                    
                    (dolist (action actions)
                        (setf new-child (funcall (problema-resultado problema) (node-estado current) action))
                        (push (make-node :estado new-child :parent current 
                                         :parent-action action) open)
                    )
                )
            )
            
        while (not (null open)))
        nil
    )
)


;;; procura-A*: problema x heuristica -> lista accoes
(defun procura-A* (problema heuristica)
    (let* (
        (open (list))
        (start (problema-estado-inicial problema))
        (order 1)
        (actions (list))
        (new-child nil)
        (current (make-node :estado start :parent nil :parent-action nil :f-score (funcall heuristica start) :order order)))
        
        (incf order)
        (push current open)
        
        (loop do
            (setf current (select open))
            (if (funcall (problema-solucao problema) (node-estado current))
                (return-from procura-A* (reconstruct-path current))
                (progn
                    
                    (setf open (remove current open))
                    
                    (setf actions (funcall (problema-accoes problema) (node-estado current)))
                    
                    
                    (dolist (action actions)
                        (setf new-child (funcall (problema-resultado problema) (node-estado current) action))
                        (push (make-node :estado new-child :parent current 
                                         :parent-action action
                                         :f-score (+ (funcall (problema-custo-caminho problema) new-child) (funcall heuristica new-child)) 
                                         :order order) open)
                        (incf order)
                    )
                )
            )
            
        while (not (null open)))
        nil
    )
)

;;; procura-best: array x lista pecas -> lista accoes
(defun procura-best (array lista_pecas) 
    (let* (
        (board (array->tabuleiro array))
        (start (make-estado :pontos 0 
                            :tabuleiro board 
                            :pecas-colocadas ()
                            :pecas-por-colocar lista_pecas))
        (problem (make-problema
                            :estado-inicial start
                            :solucao #'solucao 
                            :accoes #'accoes 
                            :resultado #'resultado 
                            :custo-caminho #'custo-oportunidade))
        (heuristica #'heuristica-best)
        (open (list))
        (order 1)
        (actions (list))
        (new-child nil)
        (current (make-node :estado start :parent nil :parent-action nil :f-score (funcall heuristica start) :order order))
        (media-heuristica 0)
        (aux 0)
        (num_actions 0)
        )
    (incf order)
    (push current open)
    
    (loop do
            (setf current (select open))            ;selecciona o no entre os que estao abertos com menor valor de f
            ;(desenha-estado (node-estado current))
            (if (funcall (problema-solucao problem) (node-estado current))  ;se o no for solucao    
                (return-from procura-best (reconstruct-path current))       ;faz backtracking a partir do no e retorna o caminho
                (progn                                                      ;senao
                    (setf open (remove current open))                       ;remove o no da lista de abertos
                    (setf actions (funcall (problema-accoes problem) (node-estado current)))    ;vai buscar accoes possiveis a partir do no
                        
                    (setf aux 0)                                                                                    ;; Este bloco procura
                    (setf num_actions 0)                                                                            ;; a media das funcoes 
                    (dolist (action actions)                                                                        ;; heuristicas dos estados
                        (setf new-child (funcall (problema-resultado problem) (node-estado current) action))        ;; que vao ser criados
                        (setf aux (+ aux (funcall heuristica new-child)))                                           ;; para mais a frente
                        (incf num_actions)                                                                          ;; nao colocar na lista
                    )                                                                                               ;; os estados que tem uma 
                    (if (not (= 0 num_actions))
                        (setf media-heuristica (/ aux num_actions))                                                 ;; funcao heuristica superior
                        (setf media-heuristica 100)                                                                 ;; a media
                    )
                    
                    ;gera filhos
                    (dolist (action actions)                
                        ;cria um estado filho
                        (setf new-child (funcall (problema-resultado problem) (node-estado current) action))    
                        (if (< (funcall heuristica new-child) media-heuristica)
                            ;cria um novo no com estado, pais, etc
                            (progn (push (make-node :estado new-child :parent current      
                                             :parent-action action
                                             :f-score (+ (funcall (problema-custo-caminho problem) new-child) (funcall heuristica new-child)) 
                                             :order order) open)
                                             
                            ;incrementa a ordem, isto e usado para saber qual e que foi 
                            ;aberto primeiro
                            (incf order))                                           
                        )
                    )
                            
                        
                )
            )
        while (not (null open)))    ;enquanto houver nos abertos
        nil
    )
)

(load "utils.fas")