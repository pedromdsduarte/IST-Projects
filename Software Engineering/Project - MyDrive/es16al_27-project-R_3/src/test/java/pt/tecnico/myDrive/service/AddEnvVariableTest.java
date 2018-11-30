package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;
import org.junit.Test;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.EnvVar;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.Session;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.InvalidEnvVarAttributes;
import pt.tecnico.myDrive.exception.InvalidTokenException;

public class AddEnvVariableTest extends AbstractServiceTest {

	private MyDrive md;
	private User user;
	private long token;
	
	private static final String NAME = "name";
	private static final String VALUE = "value";
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

	}
	
	/********************
	*** SUCCESS CASES ***
	********************/
	
	@Test
	public void successValidToken() {
		SessionManager sm = md.getSessionManager();
		Session session = sm.getSessionByToken(token);		
        AddEnvVariableService service = new AddEnvVariableService(token,NAME, VALUE);
        service.execute();
        EnvVar var = session.findEnvVar(NAME);

        
        assertTrue("var was created", session.hasEnvVar(NAME));
        assertTrue("var has the right value", var.getValue().equals(VALUE));
        assertTrue("var is in list", service.getVarList().containsKey(NAME));
	}
	
	@Test
	public void successSubstitution() {
		SessionManager sm = md.getSessionManager();
		Session session = sm.getSessionByToken(token);		
        AddEnvVariableService service = new AddEnvVariableService(token,NAME, VALUE);
        service.execute();
        String oldval = service.getVarList().get(NAME);
        
        assertTrue("var was created", session.hasEnvVar(NAME));
        assertTrue("old var is in list", service.getVarList().containsKey(NAME));
        
        service = new AddEnvVariableService(token,NAME,"other value");
        service.execute();
        String newval = service.getVarList().get(NAME);
        
        assertFalse("old var was deleted", !session.hasEnvVar(NAME));
        assertTrue("new var has the right value", session.findEnvVar(NAME).getValue().equals("other value"));
        
        assertTrue("new var is in list", service.getVarList().containsKey(NAME));
        assertFalse("new var in list has the right value", oldval.equals(newval));

	}

	
	/********************
	** UNSUCCESS CASES **
	********************/
	
	@Test(expected = InvalidEnvVarAttributes.class)
	public void emptyVarName() {	
        AddEnvVariableService service = new AddEnvVariableService(token,"", VALUE);
        service.execute();
	}
	
	@Test(expected = InvalidEnvVarAttributes.class)
	public void emptyVarValue() {
        AddEnvVariableService service = new AddEnvVariableService(token,NAME, "");
        service.execute();
	}
	
	@Test(expected = InvalidEnvVarAttributes.class)
	public void nullVarName() {
        AddEnvVariableService service = new AddEnvVariableService(token, null, VALUE);
        service.execute();
	}
	
	@Test(expected = InvalidEnvVarAttributes.class)
	public void nullVarValue() {
        AddEnvVariableService service = new AddEnvVariableService(token,NAME, null);
        service.execute();
	}
	
	@Test(expected = InvalidTokenException.class)
	public void deleteFileInvalidToken() {
		AddEnvVariableService service = new AddEnvVariableService(INVALIDTOKEN,NAME, VALUE);
        service.execute();
	}

}
