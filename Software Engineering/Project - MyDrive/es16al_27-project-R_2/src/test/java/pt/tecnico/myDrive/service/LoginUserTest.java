package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
//import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.InvalidUserPasswordException;
import pt.tecnico.myDrive.exception.UsernameTooShortException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;

import org.junit.Test;

public class LoginUserTest extends AbstractServiceTest {
	private User user;
	private MyDrive md;
	
	private static final String USERNAME 	= "dummy_username";
	private static final String PASSWORD 	= "dummy_password";
	private static final String NAME	 	= "dummy_name";
	private static final String PERMISSIONS = "rwxdrwxdr";
	private static final String UNKNOWNUSERNAME = "unknown_username";
	private static final String INVALIDPASSWORD = "invalid_password";
	private static final String INVALIDUSERNAME = null;
	private static final String USERNAMETOOSHORT1 = "X";
	private static final String USERNAMETOOSHORT2 = "XX";
	private static final String USERNAMETOOSHORT3 = "XXX";
	
	
	
	protected void populate(){
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
		 		
		//SessionManager sm = md.getSessionManager();
		user = md.createUser(USERNAME, PASSWORD, NAME, PERMISSIONS);
		
	}
	
	@Test
	public void sucessLogin(){
		LoginUserService service = new LoginUserService(USERNAME,PASSWORD);
		service.execute();
		// falta aqui um assert!!!
	}

	@Test(expected = UsernameNotFoundException.class)
	public void loginUnknownUser(){
		LoginUserService service = new LoginUserService(UNKNOWNUSERNAME,PASSWORD);
		service.execute();
	}
	
	@Test(expected = InvalidUserPasswordException.class)
	public void loginInvalidPassword(){
		LoginUserService service = new LoginUserService(USERNAME,INVALIDPASSWORD);
		service.execute();
	}
	
	@Test(expected = InvalidUsernameException.class)
	public void loginInvalidUsername(){
		LoginUserService service = new LoginUserService(INVALIDUSERNAME,PASSWORD);
		service.execute();
	}
	
	@Test(expected = UsernameTooShortException.class)
	public void loginUsernameTooShort(){
		LoginUserService service = new LoginUserService(USERNAMETOOSHORT1,PASSWORD);
		service.execute();
		service = new LoginUserService(USERNAMETOOSHORT2,PASSWORD);
		service.execute();
		service = new LoginUserService(USERNAMETOOSHORT3,PASSWORD);
		service.execute();
		
	
	}

		
}
