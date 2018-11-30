package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveDeletePermissionException;


public class DeleteFileTest extends AbstractServiceTest {

	private User user;
	private User userNoPermission;
	private MyDrive md;
	private long token;	
	private long tokenNoPermission;
	
	private static final String NULLNAME = null;
	private static final String USERNAME = "Test";
	private static final String PASSWORD = "123456789";
	private static final String NAME = "TestUser";
	private static final String PERMISSIONS = "rwxd----";
	private static final String FILENAME = "testfile";
	private static final String CONTENT = "test123";
	
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
        
        userNoPermission = md.createUser("Bad_user", "666-666-666", "ImBad", "r-x-r-x-");
        tokenNoPermission = md.login("Bad_user","666-666-666");
 
        md.createPlainFile(FILENAME, user, user.getHomeDir(), CONTENT);
        md.createPlainFile("DONOTTOUCH", user, user.getHomeDir(), CONTENT);
    }
	
	
	@Test
    public void successValidTokenAndExistingName() {
		SessionManager sm = md.getSessionManager();
		Dir dir = sm.getCurrentDirByToken(token);
        DeleteFileService service = new DeleteFileService(token,FILENAME);
        service.execute();

        // check file was removed
        
        assertFalse("file was not deleted", md.hasFile(dir,FILENAME));
    }
	

    @Test(expected = InvalidTokenException.class)
    public void deleteFileInvalidToken() {
        DeleteFileService service = new DeleteFileService(INVALIDTOKEN,FILENAME);	
        service.execute();
    }
    
    
    @Test(expected = CannotFindFileException.class)
    public void deleteNonexistingFile() {
        DeleteFileService service = new DeleteFileService(token,NULLNAME);
        service.execute();
    }

    @Test(expected = UserDoesNotHaveDeletePermissionException.class)
    public void deleteFileWithoutPermission() {
    	md.changeCurrentDirectory(tokenNoPermission, "/home/Test");
    	
    	DeleteFileService service = new DeleteFileService(tokenNoPermission, "DONOTTOUCH");
    	service.execute();
    }
    
    

}
