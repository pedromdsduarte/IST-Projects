<html>
    <body>
<?php
	include("session.php");
	
	
    $userid = $login_session;
    $registry_type_name = $_POST['registry_type_name'];
	echo $page_name;
    try
    {
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , $userid
					FROM sequencia S";

      
        $db->query($sql);
		//Vai buscar id sequencia

		//Coloca na tabela pagina
		$sql = "INSERT INTO tipo_registo (userid, typecnt, nome, ativo, idseq)
				SELECT $userid , max(TR.typecnt) + 1 , '$registry_type_name' , 1 ,(SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S)
				FROM tipo_registo TR;";
		$db->query($sql);

        $db = null;
		header('Location: index.php');
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
