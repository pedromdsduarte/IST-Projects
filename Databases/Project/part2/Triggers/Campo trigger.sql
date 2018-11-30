# Instituto Superior Técnico
# Autores:
# Gonçalo Fialho- 79112
# João Pedro Almeida-78451
# Pedro Santos- 78328

# Descrição- o trigger verifica se não existe mais nenhuma ocorrência em que o idseq é igual ao id_seq numa das 
# restanstes tabelas, ou seja, pretende-se verificar se o valor de id_seq acrescentado a tabela 
# onde estamos não ocorre em nenhumas das restantes quatro tabelass

delimiter |
create trigger Campo before insert on campo
for each row
BEGIN
	If  (Select  count(*)
    from tipo_registo tp, pagina p, registo r, valor v
    where tp.idseq= NEW.idseq
    and p.idseq= NEW idseq
    and r.idseq=NEW idseq
    and v.idseq=NEW idseq
    ) != 0
    then call error;
	end if;
END;
delimiter ;

