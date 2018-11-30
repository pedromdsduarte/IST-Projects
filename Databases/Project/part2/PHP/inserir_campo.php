<html>
    <body>
<?php
	include("session.php");
	
	
    $userid = $login_session;
	$type_id = $_POST['type_id'];
    $nome_campo = $_POST['nome_campo'];
	echo $page_name;
    try
    {
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , $userid
					FROM sequencia S";

      
        $db->query($sql);

		//Coloca na tabela pagina
		$sql = "INSERT INTO campo (userid, typecnt, campocnt, idseq, ativo , nome)
				SELECT $userid , '$type_id' , max(C.campocnt) + 1 ,(SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S), 1, '$nome_campo' 
				FROM campo C;";
		$db->query($sql);

        $db = null;
		header("Location: show_register.php?register_count=$type_id");
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
