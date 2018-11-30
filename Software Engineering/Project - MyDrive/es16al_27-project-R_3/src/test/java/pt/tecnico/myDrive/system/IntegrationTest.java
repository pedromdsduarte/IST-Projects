package pt.tecnico.myDrive.system;
import pt.tecnico.myDrive.service.AbstractServiceTest;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import java.util.*;

import org.jdom2.Document;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;


@RunWith(JMockit.class)
public class IntegrationTest extends AbstractServiceTest {
	
	MyDrive md;
	User root;
	Dir mainDir;
	Dir rHome;
	Dir rootHome;
	User test;
	Dir mainTestDir;
	Dir tHome;
	Dir testHome;	
	long token;
	LoginUserService login;
	CreateFileService file;
	ChangeDirectoryService curDir;
	TreeMap varsList;
	AddEnvVariableService envVar;
	ExecuteFileService exec;
	String[] appTest;
	ListDirectoryService list;
	DeleteFileService delete;
	ReadFileService read;
	WriteFileService write;
	
    protected void populate() { // populate mockup
		md = MyDrive.getInstance();
		root = new User("root", "***", "Super User", "rwxdr-x-");
		Dir mainDir = new Dir("/", 1, 2, root);
		Dir rHome = new Dir("home", 2, 2, root);
		Dir rootHome = new Dir("root", 3, 2, root);
		
		md.setRootDir(mainDir);
		md.addUser(root);
		
		mainDir.setOwner(root);
		mainDir.setParentDir(mainDir);
		
		rHome.setOwner(root);
		rHome.setParentDir(mainDir);
		
		rootHome.setOwner(root);
		rootHome.setParentDir(rHome);
		
		root.setHomeDir(rootHome);
		
		md.setLastID(3);
		
		User test = new User("test", "blablabla", "JUST A TEST", "rwxdr-x-");
		Dir testHome = new Dir("test", 5, 2, test);
		
		md.addUser(test);
		testHome.setOwner(test);
		testHome.setParentDir(rHome);
		
		test.setHomeDir(testHome);
		
		md.setLastID(5);
		
		appTest = new String[]{"Testing", "App"};
    }

    @Test
    public void success() throws Exception {
		
		login = new LoginUserService("root", "***");
		login.execute();
		token = login.getToken();
		
		file = new CreateFileService(token, "rootTestDir", "dir");
		file.execute();
		assertEquals((md.browseToRelativeDir(token, "/home/root/rootTestDir")).getDirSize(), 2);

		curDir = new ChangeDirectoryService(token, "rootTestDir");
		curDir.execute();
		assertEquals(curDir.result(), "/home/root/rootTestDir");
		
		envVar = new AddEnvVariableService(token, "$USER", "root");
		envVar.execute();
		envVar = new AddEnvVariableService(token, "$TEST", "root/rootTestDir");
		envVar.execute();
		
		varsList = envVar.getVarList();
		assertTrue(varsList.size()==2);
		assertTrue(varsList.containsKey("$USER"));
		assertTrue(varsList.containsValue("root/rootTestDir"));
		
		Dir d = md.browseToRelativeDir(token, "/home/root/rootTestDir"); // this directory is empty
		ListDirectoriesDto expected = new ListDirectoriesDto(); // populate DTO with . and ..
		expected.addFile(new FileDto("Dir", "", -1, ".", d.getDate(), 0, root));
		expected.addFile(new FileDto("Dir", "", -1, "..", d.getDate(), 0, root));

		list = new ListDirectoryService(token);
		list.execute();
		ListDirectoriesDto result = list.result();
		assertEquals(expected.toString(), result.toString());
									
		
		login = new LoginUserService("test", "blablabla");
		login.execute();
		token = login.getToken();
		
		Dir dir = md.browseToRelativeDir(token, "/home/test");
		
		file = new CreateFileService(token, "testApp", "app", "pt.tecnico.myDrive.Test.echo");
		file.execute();
		assertEquals(dir.getDirSize(), 3);
		
		
		file = new CreateFileService(token, "testFile", "plainfile", "testing file service");
		file.execute();
		assertEquals(dir.getDirSize(), 4);
		
		System.out.println(">>>>>"+ dir.getDirSize());
		
		exec = new ExecuteFileService(token, "/home/test/testApp", appTest);
		exec.execute();
		
		read = new ReadFileService(token, "testFile");
		read.execute();
		assertEquals(read.result(), "testing file service");
		
		write = new WriteFileService(token, "testFile", "new text");
		write.execute();
		read = new ReadFileService(token, "testFile");
		read.execute();
		assertEquals(read.result(), "new text");
		
		delete = new DeleteFileService(token, "testFile");
		delete.execute();
		assertEquals((md.browseToRelativeDir(token, "/home/test")).getDirSize(), 3);
		
    }
}
