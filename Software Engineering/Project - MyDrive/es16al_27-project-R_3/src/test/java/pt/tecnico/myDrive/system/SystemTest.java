package pt.tecnico.myDrive.system;

import org.junit.Test;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.presentation.*;
import pt.tecnico.myDrive.service.AbstractServiceTest;

public class SystemTest extends AbstractServiceTest {
	private MdShell sh;
	private User user, nobody;
	private MyDrive md;
	
	private static final String ROOT_USERNAME	= "root";
	private static final String ROOT_PASSWORD 	= "***";
	private static final String USERNAME 		= "dummy_username";
	private static final String PASSWORD 		= "dummy_password";
	private static final String NAME	 		= "dummy_name";
	private static final String PERMISSIONS 	= "rwxdrwxdr";
	private static final String NOBODY	 		= "nobody";
	private static final String DOT				= ".";
	private static final String TWODOTS			= "..";
	private static final String ENV_NAME		= "environment_name";
	private static final String ENV_VALUE		= "environment_value";
	private static final String PLAIN_PATH		= "/home/dummy_username/plainfile";
	private static final String PLAIN_NAME		= "plainfile";
	private static final String PLAIN_TEXT		= "Hello World!";
	private static final String APP_PATH		= "appfile";
	private static final String APP_TEXT		= "hello.bat\nhello.bat";

	private static final String APP_PATH1		= "/home/dummy_username/echo";
	private static final String APP_NAME1		= "echo";
	private static final String APP_TEXT1		= "pt.tecnico.myDrive.Test.echo";
	private static final String ARGS1			= "Hello";
	private static final String ARGS2			= "World";
	protected void populate(){
		
		md = MyDrive.getInstance();
		
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
 		
		user = md.createUser(USERNAME, PASSWORD, NAME, PERMISSIONS);
		nobody = md.createUser(NOBODY, "", "Guest", "rxwdr-x-");
		md.createPlainFile(PLAIN_NAME, user, user.getHomeDir(), PLAIN_TEXT);
		md.createPlainFile(APP_PATH, user, user.getHomeDir(), APP_TEXT);
		md.createApp(APP_NAME1, user, user.getHomeDir(), APP_TEXT1);
		sh = new MdShell();
	}
	
	@Test
	public void sucess(){
		//Login
		new Login(sh).execute(new String[] { NOBODY });
		new Login(sh).execute(new String[] { ROOT_USERNAME , ROOT_PASSWORD } );
		new Login(sh).execute(new String[] { USERNAME , PASSWORD } );
		
		//List
		new List(sh).execute(new String[] {});
		new List(sh).execute(new String[] { TWODOTS });
		
		//ChangeWorkingDirectory
		new ChangeWorkingDirectory(sh).execute(new String[] { DOT });
		new ChangeWorkingDirectory(sh).execute(new String[] { TWODOTS });
		new ChangeWorkingDirectory(sh).execute(new String[] {  });
		
		//Environment
		new Environment(sh).execute(new String[] { ENV_NAME , ENV_VALUE });
		new Environment(sh).execute(new String[] { ENV_NAME });
		new Environment(sh).execute(new String[] {});
		
		//Execute
		//new Execute(sh).execute(new String[] { APP_PATH } );
		new Execute(sh).execute(new String[] { APP_PATH1 , ARGS1 , ARGS2} );	
		
		//Key
		new Key(sh).execute(new String[] { ROOT_USERNAME });
		new Key(sh).execute(new String[] { USERNAME });
		new Key(sh).execute(new String[] { });
		
		//Write
		new Write(sh).execute(new String[] { PLAIN_PATH , PLAIN_TEXT });
	}
}
