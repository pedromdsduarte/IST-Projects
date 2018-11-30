use ist178451;
SELECT userid, nome
FROM registo r
where r.ativo=true
and exists(select *
			FROM tipo_registo tp
            where tp.ativo=true 
			and not exists(select *
			 FROM pagina p
             where r.userid = p.userid
             and not exists(select*
					from reg_pag rp
                    where rp.ativa=true
                    and p.ativa=true 
                    and rp.regid = r.regcounter
                    and p.pagecounter=rp.pageid
					and rp.userid = p.userid
                    and tp.typecnt=rp.typeid)
		
))
;

