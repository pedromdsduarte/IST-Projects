package pt.tecnico.myDrive.service;

import static org.junit.Assert.*;

import org.junit.Test;


import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.App;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.Link;


import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.DuplicateDirectoryException;
import pt.tecnico.myDrive.exception.DuplicateFilenameException;
import pt.tecnico.myDrive.exception.InvalidFilenameException;
import pt.tecnico.myDrive.exception.FileIsNotADirectoryException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.UnknownFileTypeException;
import pt.tecnico.myDrive.exception.CreateLinkWithoutContentException;
import pt.tecnico.myDrive.exception.CreateDirWithContentException;
import pt.tecnico.myDrive.exception.PathTooLongException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveWritePermissionException;
import pt.tecnico.myDrive.exception.LinkCyclicityFoundException;
import pt.tecnico.myDrive.exception.LinkContentCannotBeChangedException;
// import pt.tecnico.myDrive.exception.

public class CreateFileTest extends AbstractServiceTest {

	
	private static final String NULLNAME = null;
	private static final String EMPTYNAME = "";
	private static final String NAME = "name";
	private static final String CONTENT = "test";
	private static final long INVALIDTOKEN = 123123123123L;
	private static final String PLAINFILE = "plainfile";
	private static final String DIR = "dir";
	private static final String APP = "app";
	private static final String LINK = "link";
	private static final String HUGEPATH = "hugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehugehuge";

	private static final String LINKEDFILENAME = "point.me";
	private static final String LINKEDFILEABSOLUTEPATH = "/home/teste1/"+LINKEDFILENAME;
	private static final String LINKEDFILERELATIVEPATH = "./"+LINKEDFILENAME;


	private static final String LINKEDPLAINFILE = "linkedplainfile";
	private static final String LINKEDDIR = "linkeddir";
	private static final String LINKEDAPP = "linkedapp";
	private static final String LINKEDLINK = "linkedlink";

	private MyDrive md;
	private User u1;
	private long token;
	/*A aplicac¸ao˜ e um ficheiro de texto onde o conte ´ udo, uma cadeia de carateres constitu ´ ´ıda por
um ou mais identificadores Java separados pelo carater ’.’, representa o nome completo de
um metodo de uma classe Java ( ´ package.class.method). As operac¸oes de leitura ou escrita ˜
sobre uma aplicac¸ao permitem obter ou alterar, respetivamente, o seu conte ˜ udo. A operac¸ ´ ao˜
de execuc¸ao corresponde ˜ a execuc¸ ` ao do m ˜ etodo da classe. Se o nome do m ´ etodo for omi- ´
tido (package.class) sera invocado o m ´ etodo ´ main. Para simplificar, consideraremos apenas
metodos est ´ aticos onde os argumentos sejam vetores de cadeias de carateres ( ´ String[]).
/home/teste1/*/
	protected void populate() {

		this.md = MyDrive.getInstance();

		// copy from Main.java
		User root = new User("root", "***", "Super User", "rwxdr-x-");
		this.u1 = root;
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

		this.u1 = md.createUser("teste1", "1etset123", "Teste1", "rwxd----");
		md.createPlainFile(LINKEDFILENAME, this.u1, md.browseToDir("/home/teste1/"), LINKEDFILENAME+LINKEDFILENAME);
		this.token = md.login("teste1", "1etset123");
	}


	/********************
	*** SUCCESS CASES ***
	********************/
	@Test
	public void success() {
		CreateFileService cfs1 = new CreateFileService(this.token, DIR, DIR);
		CreateFileService cfs2 = new CreateFileService(this.token, PLAINFILE, PLAINFILE, CONTENT);
		CreateFileService cfs3 = new CreateFileService(this.token, APP, APP);
		// the following link points to the recently created plainfile, named DIR
		CreateFileService cfs4 = new CreateFileService(this.token, LINK, LINK, "/home/teste1/"+DIR);
		// the followin link uses a relative path, and points to the same file
		CreateFileService cfs5 = new CreateFileService(this.token, NAME, LINK, "./"+DIR);
		try {
    		cfs1.execute();
    		cfs2.execute();
    		cfs3.execute();
    		cfs4.execute();
    		cfs5.execute();
    	} catch (MyDriveException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	//the first assert is to ensure that the user "teste1" has a file named LINKEDFILENAME.
    	assertTrue("linked plainfile to test links not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDFILENAME));
    	assertTrue("directory not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), DIR));
    	assertTrue("plainfile not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), PLAINFILE));
    	assertTrue("app not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), APP));
    	assertTrue("link with absolute path not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINK));
    	assertTrue("link with relative not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}

	@Test
	public void createPlainFileWithNullContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, PLAINFILE, NULLNAME);
		cfs1.execute();
		assertTrue("plain file with null content not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}
	@Test
	public void createAppWithContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, APP, CONTENT);
		cfs1.execute();
		assertTrue("app with random content not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}
	@Test
	public void createAppWithNullContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, APP, NULLNAME);
		cfs1.execute();
		assertTrue("app with NULL content not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}

	@Test
	public void createLinkWithAbsPath() {
		CreateFileService cfs = new CreateFileService(this.token, NAME, LINK, LINKEDFILEABSOLUTEPATH);
		cfs.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}

	@Test
	public void createLinkWithRelPath() {
		CreateFileService cfs = new CreateFileService(this.token, NAME, LINK, LINKEDFILERELATIVEPATH);
		cfs.execute();
		assertTrue("Link with relative path not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
	}

	/* Tests to check if link follows to the specified file */
	@Test
	public void checkLinkFollowingPlainFileWithAbsPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDPLAINFILE, PLAINFILE, CONTENT);
		cfs1.execute();
		assertTrue("Plain file to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDPLAINFILE));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "/home/teste1/"+LINKEDPLAINFILE);
		cfs2.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("No link following plainfiles using absolute path.", "/home/teste1/"+LINKEDPLAINFILE, pf.getText());
	}

	@Test
	public void checkLinkFollowingPlainFileWithRelPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDPLAINFILE, PLAINFILE, CONTENT);
		cfs1.execute();
		assertTrue("Plain file to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDPLAINFILE));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "./"+LINKEDPLAINFILE);
		cfs2.execute();
		assertTrue("Link with relative path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("No link following plainfiles using relative path.", "./"+LINKEDPLAINFILE, pf.getText());
	}

	@Test
	public void checkLinkFollowingDirWithAbsPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDDIR, DIR);
		cfs1.execute();
		assertTrue("Dir to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDDIR));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "/home/teste1/"+LINKEDDIR);
		cfs2.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("Link content (path to other file) not correctly set.", "/home/teste1/"+LINKEDDIR, pf.getText());

		// TODO verify if link points to directory
	}

	@Test
	public void checkLinkFollowingDirWithRelPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDDIR, DIR);
		cfs1.execute();
		assertTrue("Dir to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDDIR));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "./"+LINKEDDIR);
		cfs2.execute();
		assertTrue("Link with relative path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		// TODO verify if link points to directory
	}

	@Test
	public void checkLinkFollowingAppWithAbsPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDAPP, APP, CONTENT);
		cfs1.execute();
		assertTrue("App to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDAPP));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "/home/teste1/"+LINKEDAPP);
		cfs2.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("No link following apps using absolute path.", "/home/teste1/"+LINKEDAPP, pf.getText());
	}

	@Test
	public void checkLinkFollowingAppWithRelPath() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDAPP, APP, CONTENT);
		cfs1.execute();
		assertTrue("App to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDAPP));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "./"+LINKEDAPP);
		cfs2.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("No link following apps using relative path.", "./"+LINKEDAPP, pf.getText());
	}
	


	/********************
	** UNSUCCESS CASES **
	********************/

	@Test(expected = LinkContentCannotBeChangedException.class)
	public void changeLinkContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, LINKEDAPP, APP, CONTENT);
		cfs1.execute();
		assertTrue("App to be linked not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINKEDAPP));

		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, "./"+LINKEDAPP);
		cfs2.execute();
		assertTrue("Link with absolute path not created", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));

		Link pf = (Link) md.getFileByName(md.getSessionManager().getCurrentDirByToken(this.token).getFileSet(), NAME);

		assertEquals("Link content is not correctly set.", "./"+LINKEDAPP, pf.getText());
		pf.setText("badpath"); // should throw exception

	}

	// Tests regarding cyclicity
//	@Test(expected = LinkCyclicityFoundException.class)
//	public void ciclicityToSameFile() {
//		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, "/home/teste1/"+NAME);
//		cfs1.execute();
//		assertFalse("Link with absolute path to itself created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//	}

//	@Test(expected = LinkCyclicityFoundException.class)
//	public void ciclicityUsingAbsolutePath() {
//		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, "/home/teste1/"+LINK);
//		cfs1.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		
//		CreateFileService cfs2 = new CreateFileService(this.token, LINK, LINK, "/home/teste1/"+NAME);
//		cfs2.execute();
//		// the service cannot create the second link, because it creates a loop (cfs1 -> cfs2 -> cfs1 ->...)
//	}

//	@Test(expected = LinkCyclicityFoundException.class)
//	public void ciclicityUsingRelativePath() {
//		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, "./"+LINK);
//		cfs1.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		
//		CreateFileService cfs2 = new CreateFileService(this.token, LINK, LINK, "./"+NAME);
//		cfs2.execute();
//		// the service cannot create the second link, because it creates a loop (cfs1 -> cfs2 -> cfs1 ->...)
//	}

//	@Test(expected = LinkCyclicityFoundException.class)
//	public void ciclicityWithThreeFiles() {
//		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, "./"+LINK);
//		cfs1.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		
//		CreateFileService cfs2 = new CreateFileService(this.token, LINK, LINK, "./"+PLAINFILE);
//		cfs2.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINK));
//		
//		CreateFileService cfs3 = new CreateFileService(this.token, PLAINFILE, LINK, "./"+NAME);
//		cfs3.execute();
//		// the service cannot create the third link, because it creates a loop (cfs1 -> cfs2 -> cfs3 -> cfs1...)
//	}

//	@Test(expected = LinkCyclicityFoundException.class)
//	public void ciclicityWithFourFiles() {
//		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, "./"+LINK);
//		cfs1.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		
//		CreateFileService cfs2 = new CreateFileService(this.token, LINK, LINK, "./"+PLAINFILE);
//		cfs2.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINK));
//		
//		CreateFileService cfs3 = new CreateFileService(this.token, PLAINFILE, LINK, "./"+DIR);
//		cfs3.execute();
//		assertTrue("A link pointing to a link that is not yet created MUST be created in order to test this.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), PLAINFILE));
//		
//		CreateFileService cfs4 = new CreateFileService(this.token, DIR, LINK, "./"+NAME);
//		cfs4.execute();
//		// the service cannot create the fourth link, because it creates a loop (cfs1 -> cfs2 -> cfs3 -> cfs4 -> cfs1...)
//	}

	/* Creating a file and getting permission denied */
	@Test(expected = UserDoesNotHaveWritePermissionException.class)
	public void noWritePermission() {
		assertFalse("No file named NAME in rootDir", md.hasFile(md.getRootDir(), NAME));

		// set the currentDir of the Session to be the rootDir of MyDrive. Only root has permissions there
		this.md.getSessionManager().setCurrentDirByToken(this.token, md.getRootDir());

		CreateFileService cfs = new CreateFileService(this.token, NAME, PLAINFILE, CONTENT);
		cfs.execute();

		//assertFalse("No file again...", md.hasFile(md.getRootDir(), NAME));

	}

	// in the next tests, a file/dir/link/app is created with a name 1500 characters long. This way, it creates a file with a path
	// longer that the maximum length allowed (parentDir path length + filename length) has to be less than 1024
	@Test(expected = PathTooLongException.class)
	public void dirPathTooLong() {
		CreateFileService cfs = new CreateFileService(this.token, HUGEPATH, DIR);
		cfs.execute();
	}
	@Test(expected = PathTooLongException.class)
	public void appPathTooLong() {
		CreateFileService cfs = new CreateFileService(this.token, HUGEPATH, APP);
		cfs.execute();
	}
	@Test(expected = PathTooLongException.class)
	public void plainFilePathTooLong() {
		CreateFileService cfs = new CreateFileService(this.token, HUGEPATH, PLAINFILE, CONTENT);
		cfs.execute();
	}
	@Test(expected = PathTooLongException.class)
	public void linkPathTooLong() {
		CreateFileService cfs = new CreateFileService(this.token, HUGEPATH, LINK, LINKEDFILEABSOLUTEPATH);
		cfs.execute();
	}
	@Test(expected = PathTooLongException.class)
	public void linkPathTooLongInContent() {
		CreateFileService cfs = new CreateFileService(this.token, LINK, LINK, HUGEPATH);
		cfs.execute();
	}

	// DirectoryNotFoundException in Links (passed in content)
//	@Test(expected = DirectoryNotFoundException.class)
//	public void linkWithNonExistingDirectoryRelativePath() {
//		assertFalse("This directory is not supposed to exist.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		CreateFileService cfs = new CreateFileService(this.token, LINK, LINK, "./"+NAME);
//		cfs.execute();
//	}
//	@Test(expected = DirectoryNotFoundException.class)
//	public void linkWithNonExistingDirectoryAbsolutePath() {
//		assertFalse("This directory is not supposed to exist.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), NAME));
//		CreateFileService cfs = new CreateFileService(this.token, LINK, LINK, "/home/teste1/"+NAME);
//		cfs.execute();
//	}

	// InvalidTokenException
	@Test(expected = InvalidTokenException.class)
	public void invalidTokenDir() {
		CreateFileService cfs = new CreateFileService(INVALIDTOKEN, NAME, DIR);
		cfs.execute();
	}

	@Test(expected = InvalidTokenException.class)
	public void invalidTokenApp() {
		CreateFileService cfs = new CreateFileService(INVALIDTOKEN, NAME, APP);
		cfs.execute();
	}

	@Test(expected = InvalidTokenException.class)
	public void invalidTokenPlainFile() {
		CreateFileService cfs = new CreateFileService(INVALIDTOKEN, NAME, PLAINFILE, CONTENT);
		cfs.execute();
	}

	@Test(expected = InvalidTokenException.class)
	public void invalidTokenLink() {
		CreateFileService cfs = new CreateFileService(INVALIDTOKEN, NAME, LINK, LINKEDFILEABSOLUTEPATH);
		cfs.execute();
	}

	// InvalidFilenameException for directories
	@Test(expected = InvalidFilenameException.class)
	public void invalidFilenameDir() {
		//md.createDir(NULLNAME, 2, this.u1, this.root);
		CreateFileService cfs = new CreateFileService(this.token, NULLNAME, DIR);
		cfs.execute();
	}

	@Test(expected = InvalidFilenameException.class)
	public void emptyFilenameDir() {
		//md.createDir(EMPTYNAME, 2, this.u1, this.root);
		CreateFileService cfs = new CreateFileService(this.token, EMPTYNAME, DIR);
		cfs.execute();
	}

	// InvalidFilenameException for apps
	@Test(expected = InvalidFilenameException.class)
	public void invalidFilenameApp() {
		//md.createApp(NULLNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, NULLNAME, APP);
		cfs.execute();
	}
	
	@Test(expected = InvalidFilenameException.class)
	public void emptyFilenameApp() {
		//md.createApp(EMPTYNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, EMPTYNAME, APP);
		cfs.execute();
	}

	// InvalidFilenameException for plainfile
	@Test(expected = InvalidFilenameException.class)
	public void invalidFilenamePlainFile() {
		//md.createPlainFile(NULLNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, NULLNAME, PLAINFILE, CONTENT);
		cfs.execute();

	}
	
	@Test(expected = InvalidFilenameException.class)
	public void emptyFilenamePlainFile() {
		//md.createPlainFile(EMPTYNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, EMPTYNAME, PLAINFILE, CONTENT);
		cfs.execute();
	}


	
	// InvalidFilenameException for link
	@Test(expected = InvalidFilenameException.class)
	public void invalidFilenameLink() {
		//md.createLink(NULLNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, NULLNAME, LINK, LINKEDFILEABSOLUTEPATH);
		cfs.execute();
	}
	@Test(expected = InvalidFilenameException.class)
	public void emptyFilenameLink() {
		//md.createLink(EMPTYNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, EMPTYNAME, LINK, LINKEDFILEABSOLUTEPATH);
		cfs.execute();
	}

	// DuplicateDirectoryException
	@Test(expected = DuplicateDirectoryException.class)
	public void duplicateDirectory() {
		//md.createDir(NAME, 2, this.u1, this.root);
		//md.createDir(NAME, 2, this.u1, this.root);
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, DIR);
		CreateFileService cfs2 = new CreateFileService(this.token, NAME, DIR);	
		cfs1.execute();
		cfs2.execute();
	}

	@Test(expected = DuplicateFilenameException.class)
	public void duplicateFilenameApp() {
		//md.createApp(NAME, this.u1, this.root, CONTENT);
		//md.createApp(NAME, this.u1, this.root, CONTENT);
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, APP);
		CreateFileService cfs2 = new CreateFileService(this.token, NAME, APP);	
		cfs1.execute();
		cfs2.execute();
	}

	@Test(expected = DuplicateFilenameException.class)
	public void duplicateFilenamePlainFile() {
		//md.createPlainFile(NAME, this.u1, this.root, CONTENT);
		//md.createPlainFile(NAME, this.u1, this.root, CONTENT);
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, PLAINFILE, CONTENT);
		CreateFileService cfs2 = new CreateFileService(this.token, NAME, PLAINFILE, CONTENT);	
		cfs1.execute();
		cfs2.execute();

	}
	@Test(expected = DuplicateFilenameException.class)
	public void duplicateFilenameLink() {
		//md.createLink(NAME, this.u1, this.root, CONTENT);
		//md.createLink(NAME, this.u1, this.root, CONTENT);
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, LINKEDFILEABSOLUTEPATH);
		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, LINKEDFILEABSOLUTEPATH);	
		cfs1.execute();
		cfs2.execute();
	}

	
	// UnknownFileTypeException
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileType() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, NAME);
		cfs1.execute();
	}
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileTypeNull() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, NULLNAME);
		cfs1.execute();
	}
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileTypeEmpty() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, EMPTYNAME);
		cfs1.execute();
	}
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileType2() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, NAME, CONTENT);
		cfs1.execute();
	}
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileTypeNull2() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, NULLNAME, CONTENT);
		cfs1.execute();
	}
	@Test(expected = UnknownFileTypeException.class)
	public void unknownFileTypeEmpty2() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, EMPTYNAME, CONTENT);
		cfs1.execute();
	}

	// CreateLinkWithoutContentException
	@Test(expected = CreateLinkWithoutContentException.class)
	public void createLinkWithoutContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK);
		cfs1.execute();
	}
	@Test(expected = CreateLinkWithoutContentException.class)
	public void createLinkWithNullContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, NULLNAME);
		cfs1.execute();
	}

	// CreateDirWithContentException
	@Test(expected = CreateDirWithContentException.class)
	public void createDirWithContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, DIR, NAME);
		cfs1.execute();
	}
	@Test(expected = CreateDirWithContentException.class)
	public void createDirWithEmptyContent() {
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, DIR, EMPTYNAME);
		cfs1.execute();
	}





}