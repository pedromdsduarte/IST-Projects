<?php
	include('session.php');
?>
<!DOCTYPE html>
<html>
<head>
<title>Bloco de Notas</title>
</head>
<body>
<div id="profile" style="position: relative;    left: 31%;">
<b id="welcome">Bem Vindo : <i><?php echo $login_session; ?></i></b>
<b id="logout">&emsp;&emsp;<a href="logout.php">Terminar Sessão</a></b>
<p></p>
</div>

<table border="1" cellspacing="2" style="position: absolute;    left: 10%;">
	<tr><td colspan="2" align="center">Páginas (<a id="add_page_box" href="#" onclick='if(document.getElementById("add_page").style.display=="none"){document.getElementById("add_page").style.display="";document.getElementById("add_page_box").textContent = "Cancelar";}else{document.getElementById("add_page").style.display="none";document.getElementById("add_page_box").textContent = "Adicionar"}'>Adicionar</a>)</td></tr>
	<?
	$sql = "SELECT nome, pagecounter FROM pagina WHERE userid=$login_session AND ativa=1;";

	$result = $db->query($sql);
	foreach($result as $row)
        {
			echo("<tr>
				<td><a href=\"show_page.php?page_counter={$row['pagecounter']}\">{$row['nome']}</a></td>
				<td><a href=\"remove_page.php?page_counter={$row['pagecounter']}\"> Remover </a></td>
			</tr>\n");
		}
	?>
	<tr id="add_page" style="display: none;"><td colspan="2"><form action="inserir_pagina.php" method="post"> <input id="page_name" type="text" placeholder="Nome da Página"  name="page_name"/> <input type="submit" value="Adicionar" /> </form></td></tr>
</table>
<table border="1" cellspacing="2" style="    position: absolute;    left: 50%;">
	<tr><td colspan="2"  align="center">Tipos de Registo (<a id="add_type_box" href="#" onclick='if(document.getElementById("add_type").style.display=="none"){document.getElementById("add_type").style.display="";document.getElementById("add_type_box").textContent = "Cancelar";}else{document.getElementById("add_type").style.display="none";document.getElementById("add_type_box").textContent = "Adicionar"}'>Adicionar</a>)</td></tr>
	<?
	$sql = "SELECT nome, typecnt FROM tipo_registo WHERE userid=$login_session AND ativo=1;";

	$result = $db->query($sql);
	foreach($result as $row)
        {
			echo("<tr>
				<td><a href=\"show_register.php?register_count={$row['typecnt']}\">{$row['nome']}</td>
				<td><a href=\"remove_tipo_registo.php?type_register={$row['typecnt']}\"> Remover </a></td>
			</tr>\n");
        }
	?>
	<tr id="add_type" style="display: none;"><td colspan="2"><form action="inserir_tiporegisto.php" method="post"> <input type="text" placeholder="Nome do Tipo de Registo"  name="registry_type_name"/> <input type="submit" value="Adicionar" /> </form></td></tr>

</table>
</body>
</html>