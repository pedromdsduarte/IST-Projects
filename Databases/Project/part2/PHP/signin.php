<?
	session_start();
	$error='';
	if(isset($_POST['submit'])){
		if(empty($_POST['username']) || empty($_POST['password'])){
			$error = "Username or Password Inválido!";
		}else{
		$username = $_POST['username'];
		$password = $_POST['password'];
		
		$connection = mysql_connect("db.ist.utl.pt","ist179112","base_dados2015");
		$username = stripslashes($username);
		$password = stripslashes($password);
		$username = mysql_real_escape_string($username);
		$password = mysql_real_escape_string($password);
		
		$db = mysql_select_db("ist179112", $connection);
		$query = mysql_query("SELECT * FROM utilizador WHERE password='$password' AND email='$username'", $connection);
		$rows = mysql_num_rows($query);
		if($rows == 1){
			$_SESSION['login_user'] = $username;
			header("location: main_page.php");
		}else{
			$error = "Username or Password is invalid";
		}
		mysql_close($connection);
		}
	}
?>