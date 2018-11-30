SELECT distinct P.userid as userid
FROM pagina P, reg_pag RP
WHERE P.userid = RP.userid AND P.ativa = 1 AND RP.ativa = 1
GROUP BY P.userid
HAVING (count(distinct RP.idregpag)/count(distinct P.pagecounter)) >=
		all (select (count(distinct RP1.idregpag) / count(distinct P1.pagecounter))
			FROM reg_pag RP1, pagina P1
            WHERE RP1.userid = P1.userid AND RP1.ativa = 1 AND P1.ativa = 1
            GROUP BY RP1.userid);
            
            
            
            