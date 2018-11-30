<html>
    <body>
<?php
	include("session.php");
	
    $userid = $login_session;
    $type_register = $_REQUEST['type_register'];
	echo $page_counter;
    try
    {
       
        $sql = "	UPDATE tipo_registo
					SET ativo = 0
					WHERE typecnt = $type_register AND userid = $userid";

      
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
