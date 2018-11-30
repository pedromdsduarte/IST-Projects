<?
	$connection = mysql_connect("database url","username ","password");
	$db = mysql_select_db("use db",$connection);
	
	session_start();
	
	$user_check = $_SESSION['login_user'];
	
	$ses_sql = mysql_query("SELECT userid FROM utilizador where email='$user_check'", $connection);
	
	$row = mysql_fetch_assoc($ses_sql);
	$login_session = $row['userid'];
	if(!isset($login_session)){
		mysql_close($connection);
		header('Location: index.php');
	}
	
	try
    {
        $host = "bla.bla.bla.pt";
        $user ="username";
        $password = "password";
        $dbname = $user;
    
        $db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
        $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
        
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }

?>
