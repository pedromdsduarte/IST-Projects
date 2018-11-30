package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.FileIsNotADirectoryException;

public class ChangeDirectoryTest extends AbstractServiceTest {
	private User user;
	private MyDrive md;
	private long token;	
	
	private static final String NULLNAME = null;
	private static final String USERNAME = "Test";
	private static final String PASSWORD = "123456789";
	private static final String NAME = "TestUser";
	private static final String PERMISSIONS = "rwxdrwxdr";
	private static final String FILENAME = "testfile";
	private static final String CONTENT = "test123";
	private static final String DIRNAME = "testDir";
	
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
        md.createDir(DIRNAME, 100, user, user.getHomeDir());
    }
    
    @Test
    public void successValidTokenAndExistingName() {
		SessionManager sm = md.getSessionManager();
		Dir dir = sm.getCurrentDirByToken(token);
        ChangeDirectoryService service = new ChangeDirectoryService(token, DIRNAME);
        service.execute();
        
        assertFalse("Current Dir still the same", sm.getCurrentDirByToken(token) == dir);
    }
	
	@Test(expected = InvalidTokenException.class)
    public void changeDirectoryInvalidToken() {
        ChangeDirectoryService service = new ChangeDirectoryService(INVALIDTOKEN,DIRNAME);	
        service.execute();
    }
    
    @Test(expected = DirectoryNotFoundException.class)
    public void changeNonexistingDirectory() {
        ChangeDirectoryService service = new ChangeDirectoryService(token,NULLNAME);
        service.execute();
    }
    
    @Test(expected = FileIsNotADirectoryException.class)
    public void changeToAFile() {
        ChangeDirectoryService service = new ChangeDirectoryService(token,FILENAME);
        service.execute();
    }
    
    @Test
    public void changingToDot() {
		SessionManager sm = md.getSessionManager();
		Dir dir = sm.getCurrentDirByToken(token);
        ChangeDirectoryService service = new ChangeDirectoryService(token, ".");
        service.execute();
        
        assertFalse("Current Dir not the same", sm.getCurrentDirByToken(token) != dir);
    }
    
    @Test
    public void changingToDotDot() {
		SessionManager sm = md.getSessionManager();
		Dir dir = sm.getCurrentDirByToken(token);
        ChangeDirectoryService service = new ChangeDirectoryService(token, "..");
        service.execute();

        assertFalse("Current Dir not the parentDir", sm.getCurrentDirByToken(token) != dir.getParentDir());
    }
}
