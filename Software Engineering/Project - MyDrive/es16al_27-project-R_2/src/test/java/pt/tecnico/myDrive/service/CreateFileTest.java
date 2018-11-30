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

	private MyDrive md;
	private User u1;
	private long token;

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

		this.u1 = md.createUser("teste1", "1etset", "Teste1", "rwxd----");
		this.token = md.login("teste1", "1etset");
	}


	/********************
	*** SUCCESS CASES ***
	********************/
	@Test
	public void success() {
		CreateFileService cfs1 = new CreateFileService(this.token, DIR, DIR);
		CreateFileService cfs2 = new CreateFileService(this.token, PLAINFILE, PLAINFILE, CONTENT);
		CreateFileService cfs3 = new CreateFileService(this.token, APP, APP);
		CreateFileService cfs4 = new CreateFileService(this.token, LINK, LINK, CONTENT);
		try {
    		cfs1.execute();
    		cfs2.execute();
    		cfs3.execute();
    		cfs4.execute();
    	} catch (MyDriveException e) {
    		System.out.println(e.getMessage());
    	}
    	
    	assertTrue("directory not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), DIR));
    	assertTrue("plainfile not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), PLAINFILE));
    	assertTrue("app not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), APP));
    	assertTrue("link not created.", md.hasFile(md.getSessionManager().getCurrentDirByToken(this.token), LINK));
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



	/********************
	** UNSUCCESS CASES **
	********************/

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
		CreateFileService cfs = new CreateFileService(INVALIDTOKEN, NAME, LINK, CONTENT);
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
		CreateFileService cfs = new CreateFileService(this.token, NULLNAME, LINK, CONTENT);
		cfs.execute();
	}
	@Test(expected = InvalidFilenameException.class)
	public void emptyFilenameLink() {
		//md.createLink(EMPTYNAME, this.u1, this.root, CONTENT);
		CreateFileService cfs = new CreateFileService(this.token, EMPTYNAME, LINK, CONTENT);
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
		CreateFileService cfs1 = new CreateFileService(this.token, NAME, LINK, CONTENT);
		CreateFileService cfs2 = new CreateFileService(this.token, NAME, LINK, CONTENT);	
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