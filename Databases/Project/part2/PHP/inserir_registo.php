<html>
    <body>
<?php
	include("session.php");
	
	
    $userid = $login_session;
    $registry_name = $_REQUEST['registry_name'];
	$registry_type_name = $_REQUEST['registry_type_name'];
	$page_counter = $_REQUEST['pagecounter'];
   try
    {
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , $userid
					FROM sequencia S";

      
        $db->query($sql);
		
		//Procura ID do nome de registo seleccionado
		$sql = "	SELECT typecnt 
					FROM tipo_registo
					WHERE nome = '$registry_type_name' AND userid = $userid;";
		
		$result = $db->query($sql);
		foreach($result as $row)
		{
			$registry_type_id = $row['typecnt'];
		}
		
		
		//Coloca na tabela registo
		$sql = "	INSERT INTO registo (userid, typecounter, regcounter, nome, ativo, idseq)
					SELECT $userid , $registry_type_id , max(R.regcounter) + 1 , '$registry_name', 1, (SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S)
					FROM registo R;";
		$db->query($sql);
		
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , 1
					FROM sequencia S";

      
        $db->query($sql);
		
		//Coloca na tabela Reg_Pag
		$sql = "INSERT INTO reg_pag (idregpag, userid, pageid, typeid, regid, idseq, ativa)
				SELECT max(RP.idregpag) + 1, $userid, $page_counter, $registry_type_id, (SELECT max(R.regcounter) FROM registo R) , (SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S), 1
				FROM reg_pag RP;";
				
		$db->query($sql);

        $db = null;
		header("Location: show_page.php?page_counter=$page_counter");
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
