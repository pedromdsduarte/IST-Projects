<html>
    <body>
    <h3>Tipo Registo</h3>
<?php
    try
    {
        $host = "db.ist.utl.pt";
        $user ="ist179112";
        $password = "base_dados2015";
        $dbname = $user;
    
        $db = new PDO("mysql:host=$host;dbname=$dbname", $user, $password);
        $db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
        $sql = "SELECT * FROM tipo_registo;";
    
        $result = $db->query($sql);
    
        echo("<table border=\"1\" cellspacing=\"2\">\n");
		echo("<tr>\n
				<td>userid</td>\n
				<td>typecnt</td>\n
				<td>nome</td>\n
				<td>ativo</td>\n
				<td>idseq</td>\n
				<td>ptypecnt</td>\n
				</tr>\n");
        foreach($result as $row)
        {
            echo("<tr>\n");
            echo("<td>{$row['userid']}</td>\n");
            echo("<td>{$row['typecnt']}</td>\n");
            echo("<td>{$row['nome']}</td>\n");
			echo("<td>{$row['ativo']}</td>\n");
            echo("<td>{$row['idseq']}</td>\n");
            echo("<td>{$row['ptypecnt']}</td>\n");
            echo("</tr>\n");
        }
        echo("</table>\n");
    
        $db = null;
    }
    catch (PDOException $e)
    {
        echo("<p>ERROR: {$e->getMessage()}</p>");
    }
?>
    </body>
</html>
        
