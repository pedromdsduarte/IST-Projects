package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.FileIsNotADirectoryException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveWritePermissionException;

public class WriteFileTest extends AbstractServiceTest {
	private User user;
	private MyDrive md;
	private long token;
	private long token_root;
	
	private static final String USERNAME = "teste";
	private static final String PASSWORD= "teste";
	private static final String NAME = "UtilizadorTeste";
	private static final String PERMISSIONS = "rwxd----";
	private static final String FILENAME = "filename";
	private static final String DIRNAME = "dirname";
	private static final String FAKEFILENAME = "fakefilename"; 
	private static final String CONTENT = "this is a test\nHello World!";
	
	private static final long INVALIDTOKEN = 123456789012L;
	
	
	protected void populate(){
		md = MyDrive.getInstance();
		
		// copy from Main.java
		 		User root = new User("root", "***", "Super User", "rwxd--x-");
		 		Dir mainDir = new Dir("/", 1, 2, root);
		 		Dir home = new Dir("home", 2, 2, root);
		 		Dir rootHome = new Dir("root", 3, 2, root);
		 		
		 		md.setRootDir(mainDir);
		 		md.addUser(root);
		 		
		 		mainDir.setOwner(root);
		 		mainDir.setParentDir(mainDir);
		 		
		 		home.setOwner(root);
		 		home.setParentDir(mainDir);
		 		
		 		rootHome.setOwner(root);
		 		rootHome.setParentDir(home);
		 		
		 		root.setHomeDir(rootHome);
		 		
		 		md.setLastID(3);
		 		// end copy from Main.java
		 		
		user = md.createUser(USERNAME, PASSWORD, NAME, PERMISSIONS);
		token = md.login(USERNAME, PASSWORD);
		token_root = md.login("root", "***");
		md.createPlainFile(FILENAME, user, user.getHomeDir(), CONTENT);
		md.createPlainFile(FILENAME, root, root.getHomeDir(), CONTENT);
		md.createDir(DIRNAME,2,user,user.getHomeDir());
	}


	@Test
	public void sucess() {
		WriteFileService service = new WriteFileService(token, FILENAME, CONTENT);
		service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void writeFileInvalidToken(){
		WriteFileService service = new WriteFileService(INVALIDTOKEN, FILENAME, CONTENT);
		service.execute();
	}
	
	@Test(expected = CannotFindFileException.class)
	public void writeFileDoesntExists(){
		WriteFileService service = new WriteFileService(token, FAKEFILENAME, CONTENT);
		service.execute();
	}
	
	@Test(expected = FileIsNotADirectoryException.class)
	public void directoryIsNotAFile(){
		WriteFileService service = new WriteFileService(token, DIRNAME, CONTENT);
		service.execute();
	}
	
	@Test(expected = UserDoesNotHaveWritePermissionException.class)
	public void userDoesNotHavePermissionToWriteFile(){
		md.changeCurrentDirectory(token, "/home/root");
		WriteFileService service = new WriteFileService(token, FILENAME, CONTENT);
		service.execute();
	}

	
	@Test
	public void rightWrite(){
		WriteFileService service = new WriteFileService(token, FILENAME, CONTENT);
		service.execute();
		//check content of test
		assertEquals("Correctly write file", CONTENT , md.getFileContent("/home/teste/"+FILENAME));
	
	}
	
}
