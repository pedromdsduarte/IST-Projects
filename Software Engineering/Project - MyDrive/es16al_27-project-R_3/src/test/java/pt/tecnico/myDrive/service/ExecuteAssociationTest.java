package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.service.ExecuteFileService;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Link;

import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.InvalidAppException;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.FileIsADirectoryException;
import pt.tecnico.myDrive.exception.InvalidJavaNameException;
import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.LinkCyclicityFoundException;
import pt.tecnico.myDrive.exception.InvalidExtensionException;
import pt.tecnico.myDrive.exception.NotAPathException;
import pt.tecnico.myDrive.exception.InvalidTokenException;

import java.io.*;
import java.util.*;


@RunWith(JMockit.class)
public class ExecuteAssociationTest extends AbstractServiceTest {

	private ExecuteFileService service;
	private User user;
	private MyDrive md;
	private SessionManager sm;
	private App app;
	private PlainFile pf;
	private Link lnk;
	private Dir dir;
	private long token;
	private String[] args = {"Hello", "Test"};

	private static final String USERNAME = "test";
	private static final String USER_NAME = "Test";
	private static final String USERPASSWORD = "tsettest";
	private static final String USERPERMISSIONS = "rwxdr-x-";

	private static final String FILENAME1 = "plainfile.abc";
	private static final String FILENAME2 = "directory";
	private static final String FILENAME3 = "link";

	private static final String FILEPATH1 = "/home/"+USERNAME+"/"+FILENAME1;
	private static final String FILEPATH2 = "/home/"+USERNAME+"/"+FILENAME2;
	private static final String FILEPATH3 = "/home/"+USERNAME+"/"+FILENAME3;

	private static final String FILEEXTENSION = "abc";

	private static final String APPNAME = "executeABC.app";

	private static final String APPPATH = "/home/"+USERNAME+"/"+APPNAME;

	private static final long DUMMYTOKEN = 1L;



	@Override
	protected void populate() {
		
		md = MyDrive.getInstance();
		sm = md.getSessionManager();

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
		user = md.createUser(USERNAME, USERPASSWORD, USER_NAME, USERPERMISSIONS);
		token = md.login(USERNAME, USERPASSWORD);


		app = md.createApp(APPNAME, user, sm.getCurrentDirByToken(token), "pt.tecnico.myDrive.Test.echo");
		pf = md.createPlainFile(FILENAME1, user, sm.getCurrentDirByToken(token), APPPATH+" Hello World");
		lnk = md.createLink(token, FILENAME3, user, sm.getCurrentDirByToken(token), FILEPATH1); // links to plainFile
		dir = md.createDir(FILENAME2, 0, user, sm.getCurrentDirByToken(token));

		user.addAssociation(FILEEXTENSION, app);

	}


	@Test
    public void successPlainFile() {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {

				PlainFile temp = (PlainFile) md.getFileByName(sm.getCurrentDirByToken(token).getFileSet(), FILENAME1); // get the file

				assertEquals(FILEEXTENSION, temp.getExtension()); // correct file extension
				assertTrue(user.hasExtension(temp.getExtension())); // user has an association between extension and app
				assertEquals(app, user.getAppByExtension(temp.getExtension())); // lets check if app in association is correct
			}

		};

		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test
    public void successLink() {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {

				assertTrue(md.getFileByName(sm.getCurrentDirByToken(token).getFileSet(), FILENAME3) instanceof Link); // file is a link

				Link temp = (Link) md.getFileByName(sm.getCurrentDirByToken(token).getFileSet(), FILENAME3); // get the file

				assertFalse(temp.resolveLink() instanceof Dir); // after being resolved, cannot be a directory

				PlainFile resolved = (PlainFile) temp.resolveLink();
				assertTrue(!(resolved instanceof Link)); // after being resolved, cannot be a link
				assertTrue(resolved instanceof PlainFile); // we know that this link points to a plainfile name FILENAME1

				assertEquals(FILEEXTENSION, resolved.getExtension()); // extension for linked file 
				assertTrue(user.hasExtension(resolved.getExtension()));
				assertEquals(app, user.getAppByExtension(resolved.getExtension())); // app for linked file
			}

		};

		service = new ExecuteFileService(token, FILEPATH3, args);
		service.execute();
	}

	@Test(expected=InvalidAppException.class)
    public void InvalidAppAssociated() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new InvalidAppException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=InvalidJavaNameException.class)
    public void FileContentNotAJavaName() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new InvalidJavaNameException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=DirectoryNotFoundException.class)
    public void DirectoryInPathNotFound() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new DirectoryNotFoundException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=CannotFindFileException.class)
    public void FileInPathNotFound() throws MyDriveException { // this should be reviewed
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new CannotFindFileException(FILENAME1);
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=FileIsADirectoryException.class)
    public void DirectoriesCannotBeExecutedOnlySearched() throws MyDriveException { // this should be reviewed
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new FileIsADirectoryException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=LinkCyclicityFoundException.class)
    public void ExecutingWithLinkCiclicity() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new LinkCyclicityFoundException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=InvalidExtensionException.class)
    public void InvalidExtension() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new InvalidExtensionException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	@Test(expected=NotAPathException.class)
    public void PathIsNotAPath() throws MyDriveException {
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new NotAPathException();
			}
		};
		service = new ExecuteFileService(token, FILEPATH1, args);
		service.execute();
	}

	// Token test
	@Test(expected=InvalidTokenException.class)
    public void InvalidToken() throws MyDriveException { // this should be reviewed
		new MockUp<ExecuteFileService>() {
			@Mock
			void dispatch() {
				throw new InvalidTokenException();
			}
		};
		service = new ExecuteFileService(DUMMYTOKEN, FILEPATH1, args);
		service.execute();
	}

}