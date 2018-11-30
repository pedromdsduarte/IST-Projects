<html>
    <body>
<?php
	include("session.php");
	
    $userid = $login_session;
    $page_counter = $_REQUEST['page_counter'];
	echo $page_counter;
    try
    {
       
        $sql = "	UPDATE pagina
					SET ativa = 0
					WHERE pagecounter = $page_counter AND userid = $userid";

      
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
