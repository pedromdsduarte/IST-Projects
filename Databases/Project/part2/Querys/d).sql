SELECT DISTINCT U.userid, U.nome
FROM utilizador U, pagina P
WHERE U.userid = P.userid 
AND P.userid NOT IN (
	SELECT U.userid
	FROM utilizador U, pagina P
    WHERE U.userid = P.userid
    AND EXISTS ( 
		SELECT T.typecnt
		FROM tipo_registo T
		WHERE U.userid = T.userid
			AND NOT EXISTS (
			SELECT RP.typeid
			FROM reg_pag RP
			WHERE P.pagecounter = RP.pageid 
            AND T.typecnt = RP.typeid
		)
	)
);