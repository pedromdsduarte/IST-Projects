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


public class ReadFileTest extends AbstractServiceTest {

	private User user;
	private MyDrive md;
	private long token;	
	
	private static final String USERNAME = "supermario";
	private static final String PASSWORD = "123456789";
	private static final String NAME = "Mario";
	private static final String PERMISSIONS = "rwxdrwxdr";
	private static final String FILENAME = "testfile";
	private static final String DIRNAME = "dirfile";
	private static final String INVALIDNAME = "failedtest";
	private static final String CONTENT = "this is a test file.";
	
	private static final long INVALIDTOKEN = 123123123123L;


	protected void populate() {
        md = MyDrive.getInstance();
        
     // copy from Main.java
      		User root = new User("root", "***", "Super User", "rwxdr-x-");
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
        token = md.login(USERNAME,PASSWORD);
        md.createPlainFile(FILENAME, user, user.getHomeDir(), CONTENT);
		md.createDir(DIRNAME, 2, user, user.getHomeDir());
    }
	
	
	@Test
    public void successValidTokenExistingNameAndNotDirectory() {
		SessionManager sm = md.getSessionManager();
        ReadFileService service = new ReadFileService(token,FILENAME);
        service.execute();
		String res = service.result();
        
        assertEquals("Correctly read file /home/supermario/testfile", res, CONTENT);
    }
	

    @Test(expected = InvalidTokenException.class)
    public void readFileInvalidToken() {
        ReadFileService service = new ReadFileService(INVALIDTOKEN,FILENAME);	
        service.execute();
    }

    @Test(expected = CannotFindFileException.class)
    public void readNonExistingFile() {
        ReadFileService service = new ReadFileService(token,INVALIDNAME);
        service.execute();
    }
	
	@Test(expected = FileIsNotADirectoryException.class)
    public void readDirectoryNamedFile() {
        ReadFileService service = new ReadFileService(token,DIRNAME);
        service.execute();
    }

}
