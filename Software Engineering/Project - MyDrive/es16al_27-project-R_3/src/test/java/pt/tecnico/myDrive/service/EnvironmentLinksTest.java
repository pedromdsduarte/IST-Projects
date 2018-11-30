package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Deencapsulation;
import mockit.integration.junit4.JMockit;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.EnvVar;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotFindEnvVarException;
import pt.tecnico.myDrive.exception.MyDriveException;


@RunWith(JMockit.class)
public class EnvironmentLinksTest extends AbstractServiceTest {

	private static final String USER1 = "user1";
	
	private long token;
	private MyDrive md;
	private User u1;
	private Dir u1Home;
	private EnvVar testVar;
	private String pathWithEnvVar;
	
	private ChangeDirectoryService service;
	
	@Override
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

		// create and log in test user
		u1 = md.createUser(USER1, "password", "User de teste 1", "rwxd----");
		token = md.login(USER1, "password");
		
		// create and add an environment variable to the current session
		testVar = new EnvVar("DIR", "testDir");
		md.getSessionManager().getSessionByToken(token).addEnvVar(testVar);
		
		// get user home
		u1Home = u1.getHomeDir();
		
		// create new dir
		md.createDir("testDir", 2, u1, u1Home);
		
		// path with environment variable
		pathWithEnvVar = md.getPath(u1Home) + "/" + "$DIR";
	}

	
	@Test
    public void success() {
		
		new MockUp<ChangeDirectoryService>() {
			 
			@Mock
			void dispatch(Invocation inv) throws MyDriveException {
				
				if(inv.getInvocationCount() == 1) {
					ChangeDirectoryService thisService = inv.getInvokedInstance();
					String originalPath = Deencapsulation.getField(thisService, "path");
					
					//check if any environment variables in path
					String newPath = resolvePath(originalPath);

					ChangeDirectoryService cds = new ChangeDirectoryService(token, newPath);
					cds.execute();
					//System.out.println("inv 1 result = " + cds.result());
					Deencapsulation.setField(thisService, "res", cds.result());
					//System.out.println("orig inv result = " + thisService.result());
				}
				// invoke service with new path (without environment variables)
				else
				inv.proceed();
				
			}
		};
			
			service = new ChangeDirectoryService(token, pathWithEnvVar);
			service.execute();
			
			String newPath = service.result();
			String expectedPath = md.getPath(u1Home) + "/" + "testDir";
			assertEquals("Got a wrong path replacing the environment variable!", expectedPath, newPath);
	}
	
	
	@Test(expected=CannotFindEnvVarException.class)
    public void testWithNonExistingEnvVar() {
		
		// create path with non-existing environment variable
		String badVar = "BADVAR";
		String badPath = md.getPath(u1Home) + "/" + "$" + badVar;
		
		new MockUp<ChangeDirectoryService>() {
			 
			@Mock
			void dispatch() throws MyDriveException {
				  
				throw new CannotFindEnvVarException(badVar);
			}
		};
			
			service = new ChangeDirectoryService(token, badPath);
			service.execute();
	}
	
	public String resolvePath(String path) {
		
		String res = "";
		String[] tokens = path.split("/");
		String tok;
		EnvVar var;
		String value;
		
		List<String> tokenList = Arrays.asList(tokens);
		Iterator<String> it = tokenList.iterator();
		
		if(path.substring(0, 1).equals("/")) { // absolute path
			res = "/";
			it.next(); //skip empty space
		}
		
		// look for environment variables
		while(it.hasNext()) {
			var = null;
			value = null;
			
			tok = it.next();
			System.out.println(">>>>> tok = " + tok);
			if(tok.substring(0, 1).equals("$")) {
				String name = tok.substring(1); // TODO - verify that this is not null
				
				// get value of environment variable
				var = md.getSessionManager().getSessionByToken(token).getEnvVar(name);
				value = var.getValue();
				System.out.println(">>>>> value = " + value);
			}
			
			// build new path
			if(value == null) // was not environment variable
				res += tok;
			else
				res += value;

			if(it.hasNext()) // last token?
				res += "/";
			System.out.println(">>>>> res = " + res);
		}
		
		return res;
	}
}
