<html>
    <body>
<?php
	include("session.php");
	
    $userid = $login_session;
	$id_registo = $_REQUEST['register_count'];
    $id_campo = $_REQUEST['id_campo'];
	echo $page_counter;
    try
    {
       
        $sql = "	UPDATE campo
					SET ativo = 0
					WHERE campocnt = $id_campo AND userid = $userid";

      
        $db->query($sql);

      
        $db = null;
		header('Location: show_register.php?register_count=' . $id_registo);
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
