create index active_index using BTREE on pagina (ativa,pagecounter);
create index regid_index using BTREE on reg_pag (ativa, pageid);
create index regcounter_index using BTREE on registo (ativo, regcounter);

select R.nome
from registo R
where R.ativo = True and R.regcounter in (
	select RP.regid
   	from reg_pag RP
   	where RP.ativa = True and RP.pageid in (
		select P.pagecounter
		from pagina P
		where P.ativa = True
	)
);



