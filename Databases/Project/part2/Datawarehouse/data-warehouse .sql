# Instituto Superior Técnico
# Autores:
# Gonçalo Fialho - 79112
# João Pedro Almeida - 78451
# Pedro Santos - 78328

# Descricao - tabela de factos de login e as suas respectivas dimensoes, d_utilizador e d_tempo

CREATE TABLE IF NOT EXISTS d_utilizador (
	dim_user_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    email VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL,
    pais VARCHAR(45) NOT NULL,
    categoria VARCHAR(45) NOT NULL,
    PRIMARY KEY (dim_user_id)
);

CREATE TABLE IF NOT EXISTS d_tempo (
	dim_tempo_id INT NOT NULL AUTO_INCREMENT,
    dia INT NOT NULL,
    mes INT NOT NULL,
    ano INT NOT NULL,
    PRIMARY KEY (dim_tempo_id)
);

CREATE TABLE IF NOT EXISTS login_facts (
	lf_id INT NOT NULL AUTO_INCREMENT,
    user_id INT NOT NULL,
    tempo_id INT NOT NULL, 
    login_success TINYINT(1) NOT NULL,
    PRIMARY KEY (lf_id),
    FOREIGN KEY (user_id) REFERENCES d_utilizador (dim_user_id),
    FOREIGN KEY (tempo_id) REFERENCES d_tempo (dim_tempo_id)
);