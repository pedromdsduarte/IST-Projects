SELECT userid
FROM
(SELECT  L.userid,S.nome, count(L.userid) as Tentativas, SUM(L.Sucesso) as Sucessos
FROM 
	login L, utilizador S
Where L.userid = S.userid
GROUP BY L.userid
HAVING Tentativas-sucessos > sucessos) aq
;