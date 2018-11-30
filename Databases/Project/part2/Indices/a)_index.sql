create index idpag USING BTREE on pagina (ativa);
create index idrp USING BTREE on reg_pag (ativa,userid);
create index idreg USING BTREE on registo (ativo,userid);


explain SELECT DISTINCT (COUNT(DISTINCT r_p.idregpag) / COUNT(DISTINCT p.pagecounter))
FROM
    utilizador ut, reg_pag r_p, pagina p, registo r
WHERE 	r_p.ativa =true AND p.ativa= true AND r.ativo=true
        AND p.userid= 472
        AND r.userid= 472
        AND ut.userid= 472
        AND p.pagecounter=26569
        AND r_p.regid = r.regcounter
;