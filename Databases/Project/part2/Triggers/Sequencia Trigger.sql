# Instituto Superior Técnico
# Autores:
# Gonçalo Fialho- 79112
# João Pedro Almeida-78451
# Pedro Santos- 78328

# Descrição- o trigger verifica se existe uma e uma só ocorrência em que o idseq é igual ao contador_sequencia, ou seja, pretende-se
# verificar se o valor de contador_sequencia ocorre uma e uma só vez nas outras tabelas

delimiter |
create trigger Sequencia before insert on sequencia
for each row
BEGIN
	If  (Select  count(*)
    from tipo_registo tp, pagina p, registo r, campo c, valor v
    where tp.idseq= NEW.contador_sequencia
    and p.idseq= NEW.contador_sequencia
    and r.idseq=NEW.contador_sequencia
    and c.idseq=New.contador_sequencia
    and v.idseq=NEW.contador_sequencia
    ) != 1 
    then call error;
	end if;
END;
delimiter ;


