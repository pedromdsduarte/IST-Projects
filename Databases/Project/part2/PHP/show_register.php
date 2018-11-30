<?
	include("session.php");
?>
<!DOCTYPE html>
<?
	$registercount = $_REQUEST['register_count'];
	$sql = "SELECT nome FROM tipo_registo WHERE typecnt=$registercount;";
	$result = $db->query($sql);
	foreach($result as $row){
		$type_name = $row['nome'];
	}
	
?>
<html>
<head>
<title>Tipo de Registo <? echo $type_name ?></title>
</head>
<body>
<div id="profile" style="position: relative;    left: 31%;">
<b id="type_name"> Página : <i><? echo $type_name ?> </i></b>
<b id="back">&emsp;&emsp;<a href="index.php">Voltar</a></b>
<p></p>
</div>

<table border="1" cellspacing="2" style="position: absolute;    left: 10%;">
	<tr><td colspan="2" align="center">Campos (<a id="add_field_box" href="#" onclick='if(document.getElementById("add_field").style.display=="none"){document.getElementById("add_field").style.display="";document.getElementById("add_field_box").textContent = "Cancelar";}else{document.getElementById("add_field").style.display="none";document.getElementById("add_field_box").textContent = "Adicionar"}'>Adicionar</a>)</td></tr>
	<?
	$sql = "SELECT nome, campocnt FROM
		campo WHERE typecnt = $registercount AND ativo = 1;";

	$result = $db->query($sql);
	foreach($result as $row)
        {
			echo("<tr>
				<td>{$row['nome']}</td>
				<td> <a href='remove_campo.php?id_campo={$row['campocnt']}&register_count=$registercount' > Remover </a></td>
				</tr>\n");
        }
	?>
	<tr id="add_field" style="display: none;"><td colspan="2"><form action="inserir_campo.php" method="post"> <input type="text" placeholder="Nome do Campo"  name="nome_campo"/> <input type="text" name="type_id" id="type_id" style="display : none;" value="<? echo $registercount;?>"/><input type="submit" value="Adicionar" /> </form></td></tr>

</table>
</body>
</html>