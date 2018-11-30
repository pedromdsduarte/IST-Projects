<? 
	include('signin.php');
	
	if(isset($_SESSION['login_user'])){
		header("location: main_page.php");
	}
?>
<html>
<head>
<title>Bloco de Notas - Login</title>
</head>
<body>
<div id="main">
<h1>Iniciar sessão no bloco de notas!</h1>
<div id="login">
<h2>Login</h2>
<form action="" method="post">
<label>Email :</label>
<input id="name" name="username" placeholder="username" type="text">
<label>Password :</label>
<input id="password" name="password" placeholder="**********" type="password">
<input name="submit" type="submit" value=" Login ">
<span><?php echo $error; ?></span>
</form>
</div>
</div>
</body>
</html>