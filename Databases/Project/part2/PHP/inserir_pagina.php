<html>
    <body>
<?php
	include("session.php");
	
	
    $userid = $login_session;
    $page_name = $_POST['page_name'];
	echo $page_name;
    try
    {
		//Coloca na tabela sequencia
        $sql = "	INSERT INTO sequencia (contador_sequencia, userid)
					SELECT max(S.contador_sequencia) + 1 , $userid
					FROM sequencia S";

      
        $db->query($sql);

		//Coloca na tabela pagina
		$sql = "	INSERT INTO pagina (userid, pagecounter, nome, idseq, ativa)
					SELECT $userid , max(P.pagecounter) + 1 , '$page_name' , (SELECT max(S.contador_sequencia) as contador_sequencia FROM sequencia S) , 1
					FROM pagina P;";
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
