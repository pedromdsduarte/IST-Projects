# Instituto Superior Técnico
# Autores:
# Gonçalo Fialho - 79112
# João Pedro Almeida - 78451
# Pedro Santos - 78328

# Descricao - os seguintes triggers inserem novos dados na tabela de dimensoes d_utilizador e d_tempo 
# sempre que estas sofrem alteracoes

DELIMITER //

CREATE TRIGGER DW_DIM_Utilizador_Trigger_Insert AFTER INSERT ON utilizador 
for each row
BEGIN
	INSERT INTO d_utilizador (user_id, email, nome, pais, categoria)
		VALUES (NEW.userid, NEW.email, NEW.nome, NEW.pais, NEW.categoria);
END//

CREATE TRIGGER DW_DIM_Utilizador_Trigger_Update AFTER UPDATE ON utilizador 
for each row
BEGIN
	INSERT INTO d_utilizador (user_id, email, nome, pais, categoria)
		VALUES (NEW.userid, NEW.email, NEW.nome, NEW.pais, NEW.categoria);
END//


CREATE TRIGGER DW_DIM_Tempo_Trigger AFTER INSERT ON login 
for each row
BEGIN
	INSERT INTO d_tempo (dia, mes, ano)
		VALUES (
			EXTRACT(DAY FROM NEW.moment),
            EXTRACT(MONTH FROM NEW.moment),
            EXTRACT(YEAR FROM NEW.moment)
        );
	/*INSERT INTO login_facts (user_id, login_success, tempo_id)
		SELECT MAX(DU.dim_user_id), NEW.sucesso, MAX(DT.dim_tempo_id)
        FROM d_utilizador DU, utilizador U, d_tempo DT
		WHERE DU.user_id = NEW.userid;	*/
END//

DELIMITER ;