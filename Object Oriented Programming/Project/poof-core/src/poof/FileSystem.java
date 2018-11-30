package poof;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.io.Serializable;

public class FileSystem implements Serializable{

	private boolean _changed = true;

	/**
	* Holds all the users of the filesystem
	* 
	*/
	private Map<String, User> _users = new TreeMap<String, User>();
	
	/**
	* The "/" directory 
	*/
	private Directory _bar;
	
	/**
	* The currently logged in user
	*/
	private User _current_user;
	
	/**
	* The current work directory
	*/
	private Directory _current_directory;
	
	
	/**
	* Creates the root user, the "/", "home" and the "root" directories
	*/
	public FileSystem(){

		
		User root = new User("root","Super User");
		_users.put(root.getUsername(),root);
		
				_bar = new Directory("/",root);
		_bar.putSelf();
		_bar.setParent(_bar);

		Directory home = new Directory("home",root);
		home.setParent(_bar);
		home.putSelf();
		_bar.put(home);
		
		root.setHomedir(home);
		
		Directory rootdir = new Directory("root",root);
		rootdir.setParent(home);
		rootdir.putSelf();
		home.put(rootdir);



		root.setHomedir(rootdir);
		_current_user = root;
		_current_directory = _current_user.getHomedir();
		

	}

	
	/**
	* Lists an entry according to it's structure.
	* 
	* 
	* @param entry
	* 			entry to list
	* @return 
	*		the representation of the entry
	*/

	public String listEntry(String entryname) {
		String permission = "-";
		String type = "d";
		String name = entryname;
		Entry entry = getCurrentDirectory().get(entryname);
		if(entry instanceof File) 
			type = "-";
		if(entry.getPublicMode()) 
			permission = "w";
		return type + " " + permission + " " + entry.getOwner().getUsername() + 
			" " + entry.getSize() + " " + entryname;
	}

	/**
	* Changes to another directory
	* 
	* 
	* @param directory
	* 			directory to change to
	*/
	
	public void changeDirectory(String directory){
		Directory dir = getDirectory(directory);
		setCurrentDirectory(dir);
	}	
	
	/**
	* Creates a new directory 
	* 
	* 
	* @param name
	* 			name of the directory to create
	*
	* @param owner
	*			name of the owner of the directory
	* 
	*/

	public void createDirectory(String name, String owner){
		Directory newdirectory = new Directory(name,_users.get(owner));
		newdirectory.setParent(getCurrentDirectory());
		newdirectory.putSelf();
		getCurrentDirectory().put(newdirectory);
	}
	

	
	
	/**
	* Changes the permission of an entry
	* 
	* 
	* @param entry
	* 			entry to change permission
	*
	* @param permission
	*			new permission
	*/
	
	public void changePermission(String entry, String permission){
		_current_directory.get(entry).setPublicMode(permission.equals("public"));
       }
       
       /**
       * Changes the owner of an entry
       *
       * @param entry
       * 			entry to change owner
       * @param username
       * 			new owner
       * 
       */
	
	public void changeOwner(String entry, String username){
		_current_directory.get(entry).setOwner(_users.get(username));
	}
	
	
	/**
	* Creates a new user
	* 
	* 
	* @param username
	* 			new user username
	*
	* @param name
	*			new user name
	*/
	
	public void createUser(String username, String name){
		Directory previous = getCurrentDirectory();
		changeToBarDir();
		changeDirectory("home");
		User newuser = new User(username,name);
		_users.put(username, newuser);
		createDirectory(username,username);
		newuser.setHomedir(getDirectory(username));
		goToDirectory(previous);
	}
	
	
	/**
	* Finds the location of a directory
	* 
	* @param dir
	* 		directory to find
	*/
	public void goToDirectory(Directory dir) {
		changeToBarDir();
		String path = dir.getPath();
		String[] path_parts = path.split("/");
		for(int i = 1; i < path_parts.length; i++) {
			if(dir.equals(path_parts[i]))
				break;
			changeDirectory(path_parts[i]);

		}
	}
	
	
	/**
	* Lists an user according to it's structure.
	* 
	* 
	* @param user
	* 			user to list
	* @return 
	*		the representation of the user
	*/
	public String listUser(User user){
		return user.getUsername() + ":" + user.getName() + ":" + user.getHomedir().getPath() + "\n";
	}

		
	/**
	 * Return the content of  a file
	 * 
	 * @param name
	 * 			name of the file 
	 * @return 
	 * 			the content of file
	*/
	public String getContent(String name){
		return _current_directory.getFile(name).getContent();
	}
	 
		
	/**
	* @return 
	*		the user logged in.
	*/
	public User getCurrentUser() {
		return _current_user;
	}

	/**
	 * Finds an user
	 * 
	 * @param user
	 *		the user to find
	 * @return 
	 *		the found (or not) user.
	*/
	public User getUser(String user){
		return _users.get(user);
	}
	
	/**
	 * Gets the Map of the users 
	 * 
	 * @return 
	 *		the map that contains all of the users
	 */
	public Map<String, User> getUsers() {
		return _users;
	}
	
	/**
	* @return 
	*		the List that contains all of the directories (not a set)
	*/

	
	/**
	* Changed the current user
	* 
	* @param user
	*		the new current user
	*/
	public void setCurrentUser(User user) {
		_current_user = user;
	}

	/**
	* Changed the current directory
	* 
	* @param directory
	*		the new current directory
	*/
	public void setCurrentDirectory(Directory directory) {
		_current_directory = directory;
	}
		
	/**
	* Gets the directory (contained in the current directory)
	* @param name
	*		the directory to get
	* @return 
	*		the found directory
	*/
	public Directory getDirectory(String name) {
		return (Directory) _current_directory.get(name);
	}

	/**
	* Gets the current dirctory 
	* @return 
	*		the current directory
	*/
	public Directory getCurrentDirectory(){
		return _current_directory;
	}

	/**
	* Changes the current directory to "/"
	*/
	public void changeToBarDir() {
		_current_directory = _bar;
	}

	/**
	* Gets the bar directory
	* 
	* @return the "/" directory
	*/
	public Directory getBar() {
		return _bar;
	}

	/**
	* Creates a new (empty) file
	*
	* @param name
	*		name of the file
	* @param owner
	*		owner of the file
	* @param permission
	*		permission of the file
	*
	*/
	public void createFile(String name, String owner, String permission) {

		File file = new File(name,getUser(owner),permission.equals("public"),"");
		_current_directory.put(file);
	}
	
	
	/**
	* Writes to a file
	*
	* @param file
	*		file to be written
	* @param content
	*		content to write
	*
	*/
	public void writeFile(String file, String content) {
		File tochange =(File)_current_directory.get(file);
		tochange.addContent(content);
	}
	
	
	/**
	* Return true if File has been Changed 
	* 
	*/
	public boolean hasBeenChanged(){
		return _changed;
	}
	
	
	/**
	* Change the 'dirty bit' of the filesystem
	*
	* @param conditon
	*		new condition of the filesystem
	*/
	public void setChanged(boolean condition){
		_changed = condition;
	}
	
	
	/**
	* Deletes an entry
	* 
	* @param name
	* 		name of the entry to be deleted
	* @see Directory#deleteEntry(String name)
	*/
	public void deleteEntry(String name){
		_current_directory.deleteEntry(name);
	}
	
	
}

