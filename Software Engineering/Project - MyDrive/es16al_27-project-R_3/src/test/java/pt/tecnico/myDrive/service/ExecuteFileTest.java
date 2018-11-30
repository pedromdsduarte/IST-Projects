package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;
import org.junit.Test;

import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;

import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.FileIsADirectoryException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.CannotFindFileException;

public class ExecuteFileTest extends AbstractServiceTest {
	private MyDrive md;
	private User user;
	private long token;
	private App app;
	private PlainFile pf;

	private static final long INVALIDTOKEN = 123123123123L;
	
	@Override
	protected void populate() {

		this.md = MyDrive.getInstance();

		// copy from Main.java
		User root = new User("root", "***", "Super User", "rwxdr-x-");
		this.user = root;
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
		
		this.user = md.createUser("User", "password", "User", "rwxd----");
		this.token = md.login("User", "password");
		this.app = md.createApp("app",user,user.getHomeDir(), "test app");
		this.pf = md.createPlainFile("plainfile", user, user.getHomeDir(), "test plainfile");

	}
	
		/********************
	*** SUCCESS CASES ***
	********************/
	
	@Test
	public void successValidToken() {
		SessionManager sm = md.getSessionManager();
		Session session = sm.getSessionByToken(token);
		String[] test = {""};
		ExecuteFileService efs = new ExecuteFileService(token, md.getPath(app), test);
	}
	
	/********************
	** UNSUCCESS CASES **
	********************/
	
	@Test(expected = FileIsADirectoryException.class)
	public void currentDirExec() {	
		String[] test = {""};
        ExecuteFileService efs = new ExecuteFileService(token, "/home", test);
        efs.execute();
	}
	
	@Test(expected = CannotFindFileException.class)
	public void dirNotExist() {	
		String[] test = {""};
        ExecuteFileService efs = new ExecuteFileService(token, "/home/cenas", test);
        efs.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void invalidTokenExec() {	
		String[] test = {""};
        ExecuteFileService efs = new ExecuteFileService(INVALIDTOKEN, "app", test);
        efs.execute();
	}
	
	
	
	
}
