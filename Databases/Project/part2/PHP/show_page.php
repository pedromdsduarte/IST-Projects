<?
	include("session.php");
?>
<!DOCTYPE html>
<?
	$pagecounter = $_REQUEST['page_counter'];
	$sql = "SELECT nome FROM pagina WHERE pagecounter=$pagecounter;";
	$result = $db->query($sql);
	$teste = $result->current_field;
	foreach($result as $row){
		$page_name = $row['nome'];
	}
	
?>
<html>
<head>
<title>Página <? echo $page_name ?></title>
</head>
<body>
<div id="profile" style="position: relative;    left: 31%;">
<b id="page_name"> Página : <i><? echo $page_name ?> </i></b>
<b id="back">&emsp;&emsp;<a href="index.php">Voltar</a></b>
<p></p>
</div>

<table border="1" cellpadding="10" cellspacing="2" style="position: absolute;    left: 10%;">
	<tr><td align="center">Tipo de Registo</td> <td align="center" colspan="3">Registo</td><td><a name="add_page_box" id="add_page_box" href="#" 
		onclick='
			if(document.getElementById("add_registry").style.display=="none"){
				document.getElementById("add_registry").style.display="";
				document.getElementById("add_page_box").textContent = "Cancelar";
			}else{
				document.getElementById("add_registry").style.display="none";
				document.getElementById("add_page_box").textContent = "Adicionar Registo"}'>Adicionar Registo</a></td></tr>
	<?
	try{
	$sql = "SELECT R.nome as nome_registo
			FROM registo R
			WHERE R.regcounter in (
				SELECT RP.regid
				FROM reg_pag RP
				WHERE RP.userid = $login_session AND RP.pageid = $pagecounter AND RP.ativa = 1);";

	$result = $db->query($sql);
	foreach($result as $row)
        {	
			$sql3 = "SELECT TP.nome as nome_tipo_registo
						FROM tipo_registo TP
						WHERE typecnt = (
						SELECT R.typecounter 
						FROM registo R
						WHERE R.userid = $login_session AND R.nome = '{$row['nome_registo']}');";
			$result3 = $db->query($sql3);
			foreach($result3 as $row3){
				$nome_tipo_registo = $row3['nome_tipo_registo'];
			}
			
			$sql5 = "SELECT R.regcounter as id_registo
					FROM registo R
					WHERE userid = $login_session AND nome = '{$row['nome_registo']}';";
			$result5 = $db->query($sql5);
			foreach($result5 as $row5){
				$id_registo = $row5['id_registo'];
			}
			
				
			$sql4 = "SELECT TP.typecnt as id_tipo_registo
					FROM tipo_registo TP
					WHERE userid = $login_session AND nome = '$nome_tipo_registo';";
			$result4 = $db->query($sql4);
			foreach($result4 as $row4){
				$id_tipo_registo = $row4['id_tipo_registo'];
			}
			
			echo("<tr>
				<td>$nome_tipo_registo</td>
				<td>{$row['nome_registo']}</td>
				<td>
				<a href='#'  name=\"add_field_box_$id_registo\" id=\"add_field_box_$id_registo\"
					onclick='
							if(document.getElementById(\"form_$id_registo\").style.display==\"none\"){
								document.getElementById(\"form_$id_registo\").style.display=\"\";
								document.getElementById(\"add_field_box_$id_registo\").textContent = \"Cancelar\";
							}else{
								document.getElementById(\"form_$id_registo\").style.display=\"none\";
								document.getElementById(\"add_field_box_$id_registo\").textContent = \"Adicionar Campo e valor\"}'
				
				>Adicionar Campo e valor</a>
				<form action=\"inserir_valor.php\" style=\"display: none\"id=\"form_$id_registo\" name=\"form_$id_registo\">
					Campo: 
					<select id=\"nome_campo\" name=\"nome_campo\">");
			$sql4 = "SELECT C.nome as nome_campo
					FROM campo C
					WHERE userid = '$login_session' AND typecnt = '$id_tipo_registo';";
			$result4 = $db->query($sql4);
			foreach($result4 as $row4){
				echo("<option>{$row4['nome_campo']}</option>");
			}
			
			echo("</select>
					Valor:
					<input name=\"nome_valor\" type='text' placeholder='Valor do Campo' />
					<input name=\"id_type\" type='text' value=\"$id_tipo_registo\" style=\"display : none\" />
					<input name=\"id_register\" type='text' value=\"$id_registo\" style=\"display : none\"  />
					<input name=\"page_counter\" type='text' value=\"$pagecounter\" style=\"display : none\"  />
					<input type='submit' value='Adicionar' /> 
				</form>
				
				<table><tbody>");
			
			$sql1 = "SELECT campo.nome as nome_campo , valor.valor as valor
					FROM campo, valor
					WHERE (typecnt = '$id_tipo_registo' AND campo.ativo = 1) and campo.campocnt = valor.campoid";
			$result1 = $db->query($sql1);
			foreach($result1 as $row1)
			{
				echo("<tr><td>{$row1['nome_campo']}: </td>
					<td>{$row1['valor']}</td></tr>");
			}
			echo("</tbody></table></td></tr>\n");
		}
	}
	catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
		
	?>
	<tr colspan="4" name="add_registry" id="add_registry" style="display: none;"><td colspan="2"><form action="inserir_registo.php" method="post"> <input id="registry_name" type="text" placeholder="Nome do Registo"  name="registry_name"/>
	Tipo de Registo: <select id="registry_type_name" name="registry_type_name"> 
		<?
			$sql = "SELECT nome FROM tipo_registo WHERE userid=$login_session AND ativo=1;";
			$result = $db->query($sql);
			foreach($result as $row)
			{
				echo("<option>{$row['nome']}</option>");
			}
		?>
	</select>
	<input name="pagecounter" id="pageconter" type="text" style="display : none;" value="<? echo $pagecounter;?>"/>
	<input type="submit" value="Adicionar" /> </form></td></tr>

</table>

</body>
</html>
