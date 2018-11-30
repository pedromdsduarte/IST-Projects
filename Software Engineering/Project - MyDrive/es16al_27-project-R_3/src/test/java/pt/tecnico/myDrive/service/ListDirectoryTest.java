package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.service.dto.*;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.Dir;

import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;


public class ListDirectoryTest extends AbstractServiceTest {

	private MyDrive md;
	private User user1;
	private long token1;
	
	protected void populate() {
		
		md = MyDrive.getInstance();
		
		// copy from Main.java
		 		User root = new User("root", "***", "Super User", "rwxd--x-");
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
		 		
		// create users
		user1 = md.createUser("test1", "12345678", "Test1", "rwxd----");
		
		// login users
		token1 = md.login("test1", "12345678");
	}
	
	
		@Test
	public void successEmptyDir() {
		
		// list empty dir with valid token and permissions
		ListDirectoriesDto expected = new ListDirectoriesDto();
		ListDirectoriesDto result;
		
		md.login("root", "***");
		Dir d = md.browseToDir("/home/test1");
		
		//expected = listDir(d);
		
		ListDirectoryService service = new ListDirectoryService(token1);
		
		service.execute();
		
		result = service.result();

		// date of . and .. is same date as parent dir
		expected.addFile(new FileDto("Dir", "", -1, ".", d.getDate(), 0, user1));
		expected.addFile(new FileDto("Dir", "", -1, "..", d.getDate(), 0, user1));

		
		assertEquals("Empty directories have size 2!", 2, d.getDirSize());
		assertEquals("Empty dir content not right!", expected.toString(), result.toString());
	}
	
	
	@Test
	public void successNotEmptyDir() {
		
		// list not empty dir with valid token and permissions
		ListDirectoriesDto expected = new ListDirectoriesDto();
		ListDirectoriesDto result;
		
		md.login("root", "***");
		Dir d = md.browseToDir("/home/test1");
		
		PlainFile fd = md.createPlainFile("newFile", user1, d, "test file\n");
		expected.addFile(new FileDto("Dir", "", -1, ".", d.getDate(), 0, user1));
		expected.addFile(new FileDto("Dir", "", -1, "..", d.getDate(), 0, user1));
		expected.addFile(new FileDto(fd.getClass().getSimpleName(), fd.getFmask(), fd.getId(), fd.getName(), fd.getDate(), fd.getText().length(), fd.getOwner()));
		//expected = listDir(d);
		
		ListDirectoryService service = new ListDirectoryService(token1);
		
		service.execute();
		
		result = service.result();
		
		assertEquals("Directory with one file has size 3", 3, d.getDirSize());
		assertEquals("Not empty dir content not right!", expected.toString(), result.toString());
	}
	
	
	@Test(expected = InvalidTokenException.class)
	public void invalidToken() {
		
		/* try to list dir with invalid token */
		md.browseToDir("/home/test1");
		
		ListDirectoryService service = new ListDirectoryService(1234567890);
		
		service.execute();
	}
	
	
	@Test(expected = UserDoesNotHaveReadPermissionException.class)
	public void noPermission() {
		
		/* try to list dir without permission */
		
		md.changeCurrentDirectory(token1, "/home/root");
		
		ListDirectoryService service = new ListDirectoryService(token1);
		
		service.execute();
	}
	
	
	/* auxiliary functions */
	
	// list the directory content
	public String listDir(Dir d) {
		
		String expected = "Type\t\tPermissions\tSize\tOwner\tID\tDate\t\t\t\tName\t\n";
		
		// get ..
		expected += "Dir" + "\t\t" + d.getParentDir().getFmask() + "\t";
		expected += d.getParentDir().getDirSize() + "\t";
		expected += d.getParentDir().getOwner().getUsername() + "\t";
		expected += d.getParentDir().getId() + "\t";
		expected += d.getParentDir().getDate() + "\t" + ".." + "\n";
		
		// get .
		expected += "Dir" + "\t\t" + d.getFmask() + "\t";
		expected += d.getDirSize() + "\t";
		expected += d.getOwner().getUsername() + "\t";
		expected += d.getId() + "\t";
		expected += d.getDate() + "\t" + "." + "\n";
		
		// get content if not empty
		Set<MyDriveFile> content = d.getFileSet();
		
		for(MyDriveFile file: content)
		{
			expected += file.getClass().getSimpleName() + "\t\t";
			expected += file.getFmask() + "\t";
			
			if (file instanceof Dir) { expected += ((Dir)file).getDirSize() + "\t"; }
			else { expected += ((PlainFile)file).getText().length() + "\t"; }
			
			expected += file.getOwner().getUsername() + "\t";
			expected += file.getId()+ "\t";
			expected += file.getDate() + "\t";
			expected += file.getName() + "\n";
		}
		
		return expected;
	}
}
