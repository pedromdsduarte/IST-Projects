<html>
    <body>
<?php
	include("session.php");
	$userid = $login_session;
    $nome_campo = $_REQUEST['nome_campo'];
	$nome_valor = $_REQUEST['nome_valor'];
	$id_type = $_REQUEST['id_type'];
	$id_register = $_REQUEST['id_register'];
	$page_counter = $_REQUEST['page_counter'];
	try{
		//TRANSACOES - START TRANSACTION
		$db->query("start transaction;");
		
		
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , $userid
					FROM sequencia S";

      
        $db->query($sql);
		
		//Vai buscar id campo
		$sql = "	SELECT C.campocnt as id_campo
					FROM campo C 
					WHERE userid = $userid AND C.nome = '$nome_campo';";

		$result = $db->query($sql);
		foreach($result as $row){
			$id_campo = $row['id_campo'];
		}		
		echo("$userid , $id_type , $id_register , $id_campo , '$nome_valor' ");
		//Coloca na tabela pagina
		$sql = "INSERT INTO valor (userid, typeid, regid, campoid, valor, idseq, ativo)
				VALUES ($userid , $id_type , $id_register , $id_campo , '$nome_valor' , (SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S) , 1)";
		$db->query($sql);

		//TRANSACOES - COMMIT
		$db->query("commit;");
        
        $db = null;
		header("Location: show_page.php?page_counter=$page_counter");
    }
    catch (PDOException $e)
    {
		//TRANSACOES - COMMIT
		$db->query("rollback;");
        
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
