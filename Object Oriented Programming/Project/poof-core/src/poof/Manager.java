package poof;

import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.Map;
import java.util.Collections;
import java.util.Set;


public class Manager{
	
	/**
	* Actual filesystem, null untill created
	*/
	private FileSystem _session = null;
	
	/**
	* Name of the actual filesystem, relevant for saving
	*/
	private String _name = "";
	
	public Manager(){}

	/**
	* Creates a new filesystem
	*/
	public void createFileSystem(){
		_session = new FileSystem();

	}	


	/**
	* Recovers a filesystem from a given savefile.
	* The name of the session is the name of the file.
	* Also logins the last user of the recovered session.
	* 
	* @param filename
	* 				name of the savefile
	*/
	public void openFileSystem(String filename) throws FileNotFoundException,ClassNotFoundException{
		try {
		ObjectInputStream inp = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
			_session = (FileSystem) inp.readObject();
			_session.setChanged(false);
			_name = filename;
			inp.close();
			login(getCurrentUser().getUsername());
		}
		catch(FileNotFoundException e) {throw e;}
		catch(ClassNotFoundException e) {throw e;}
		catch(IOException e) {}
	}


	/**
	* Saves the current session to a savefile with the given savename.
	* 
	* @param savename
	* 				name of the savefile
	*/
	public void saveFileSystem(String savename) throws FileNotFoundException{
		String _savename = savename;
		
		if(savename.equals("") && _name.equals(""))
			throw new FileNotFoundException();
		
		if(savename.equals(""))
			_savename = _name;
		
		try {
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(savename)));
			out.writeObject(_session);
			_session.setChanged(false);
			_name = _savename;
			out.close();
		}
		catch(IOException e) {}
	
	}

	
	/**
	* Login a user
	* 
	* @param username
	* 			username of the user to login
	*/
	public void login(String username){
		_session.setCurrentUser(_session.getUser(username));
		_session.setCurrentDirectory(_session.getCurrentUser().getHomedir());

	}
	
	/**
	* Processes an input file with information on a filesystem.
	* The filesystem is generated with the state and information of the file.
	* 
	* @param input
	* 			name of the input file to read
	*/
	public void processInputFile(String input) throws FileNotFoundException, IOException{
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(input)));
			String buffer = "";
			String[] parts;
			_session = new FileSystem();
			
			
			while((buffer=in.readLine()) != null) {
				parts = buffer.split("\\|");
				if (parts[0].equals("USER")) {
					String username = parts[1];
					String name = parts[2];
					String path = parts[3];
					String usrhome = path.split("/")[2];
					
					createUser(username,name);		
					
				}else if (parts[0].equals("DIRECTORY")) {
					String path = parts[1];
					String owner = parts[2];
					String permission = parts[3];
					String[] path_parts = path.split("/");
					_session.changeToBarDir();

					for(int i = 1; i < path_parts.length; i++) {
						if(_session.getCurrentDirectory().get(path_parts[i]) == null) {
							createDirectory(path_parts[i],owner);
							changePermission(path_parts[i],permission);
						}						
						changeDirectory(path_parts[i]);

					}
									
				}else if (parts[0].equals("FILE")) {
					String path = parts[1];
					String owner = parts[2];
					String permission = parts[3];
					String content = parts[4];
					
					String[] path_parts = path.split("/");
					_session.changeToBarDir();
					for(int i = 1; i < path_parts.length - 1 ; i++)
						changeDirectory(path_parts[i]);
					int penultimo = path_parts.length - 1;
					String filename = path_parts[penultimo];

					createFile(filename,owner,permission);
					writeFile(filename,content);
				}
			}
				
			_session.changeToBarDir();
			login("root");
			}
			catch(FileNotFoundException e) {}
			catch(IOException e) {}


	}

	/**
	* Creates an user
	* 
	* @param username
	* 			username of the new user
	* @param name
	* 			name of the new user
	*/
	public void createUser(String username, String name) {
		_session.createUser(username, name);
	}

	/**
	* Creates a directory
	* 
	* @param dirname
	* 			name of the new directory
	* @param owner
	* 			name of the owner of the new directory
	*/
	public void createDirectory(String dirname, String owner) {
		_session.createDirectory(dirname, owner);
	}

	/**
	* Changes the current directory
	* 
	* @param directory
	* 			name of the directory to change
	*/
	public void changeDirectory(String directory) {
		_session.changeDirectory(directory);
	}

	/**
	* Changes the permission of an entry
	* 
	* @param entry
	* 			name of the entry
	* @param permission
	* 			new permission 
	*/
	public void changePermission(String entry, String permission) {
		_session.changePermission(entry, permission);
	}


	/**
	* Creates a new empty file
	* 
	* @param name
	* 			name of the new file
	* @param owner
	* 			owner of the new file
	* @param permission
	* 			permission of the new file
	*/
	public void createFile(String name, String owner, String permission) {
		_session.createFile(name,owner,permission);
	}

	/**
	* Writes on a file
	* 
	* @param file
	* 			name of the file to be written
	* @param content
	* 			content to write
	*/
	public void writeFile(String file, String content) {
		_session.writeFile(file,content);
	}
	
	/**
	* Deletes an entry
	*
	* @param name
	* 			name of the entry to be deleted
	*/
	public void deleteEntry(String name){
		_session.deleteEntry(name);
	}
	
	/**
	* Changes the owner of an entry
	*
	* @param entry
	* 			name of the entry to be changed
	* @param username
	* 			username of the new owner
	*/
	public void changeOwner(String entry, String username){
		_session.changeOwner(entry, username);
	}
	
	
	/**
	* Predicate that verifies if there is an active filesystem
	*/
	public boolean isReady() {
		return _session != null;
	}
	
	/**
	* Gets the current session of the filesystem.
	* 
	* @return the current session.
	*/
	public FileSystem getSession() {
		return _session;
	}


	/**
	* Lists the users of the current filesystem
	* 
	* @return the representation of the users
	* @see FileSystem#listUser(User)
	*/
	public String listUsers() {
		String res = "";
		for(User user : _session.getUsers().values())
			res += _session.listUser(user);
		return res.substring(0,res.length()-1);
	}

	/**
	* Gets the current directory.
	*/
	public Directory getCurrentDirectory() {
		return _session.getCurrentDirectory();
	}

	/**
	* Return the path of the current directory
	*/
	public String showCurrentPath() {
		return getCurrentDirectory().getPath();
	}
	

	/**
	* Gets the content of a file
	* 
	* @see FileSystem#getContent(String name)
	*/
	public String getContent(String name){
		return _session.getContent(name);
	}
	
	
	/**
	* List a given entry
	* 
	* @see FileSystem#listEntry(String name)
	*/
	public String listEntry(String name) {
		return _session.listEntry(name);
	}
	
	/**
	* Lists all the entries of the current directory
	* 
	* @see FileSystem#listEntry(String name)
	*/
	public String listEntries() {
		String res = "";	
		for(String entry : _session.getCurrentDirectory().getEntries().keySet()) {
			res += _session.listEntry(entry) + "\n";
		}
		return res.substring(0,res.length()-1);
	}
	
	
	/**
	* Gets the current logged in user
	* 
	* @return the current user
	*/
	public User getCurrentUser() {
		return _session.getCurrentUser();
	}
	
	
	/**
	* Gets the username of the current user
	* 
	* @return the username of the current user
	* @see #getCurrentUser()
	*/
	public String getCurrentUsername(){
		return getCurrentUser().getUsername();	
	
	}
	
	
	/**
	* Gets the username of the owner of the current directory
	*/
	public String getCurrentDirectoryOwner(){
		return _session.getCurrentDirectory().getOwner().getUsername();
	}
	
	/**
	* Gets the username of an entry owner
	* 
	* @param name
	* 			name of the entry
	* @return
	* 			the username of the owner
	*/
	public String getOwner(String name){
		return _session.getCurrentDirectory().getEntries().get(name).getOwner().getUsername();
	}

	/**
	* Predicate that verifies if an user exists
	* 
	* @param name
	* 			name of the user to verify
	*/
	public boolean userExists(String name){
		return _session.getUsers().keySet().contains(name);
	}
	
	
	/**
	* Predicate that verifies if an entry exists within the current directory
	* 
	* @param name
	* 			name of the entry to be verified
	*/
	public boolean entryExists(String name){
		return _session.getCurrentDirectory().getEntries().keySet().contains(name);
	}
	
	/**
	* Predicate that verifies if an entry is a Directory
	* 
	* @param name
	* 			name of the entry to be verified
	*/
	public boolean entryIsDirectory(String name){
		return ( _session.getCurrentDirectory().get(name) instanceof Directory);	
	}
	
	
	/**
	* Gets the permission of the current directory
	* 
	* @return 
	* 			true if the current directory is public, false otherwise
	*/
	public boolean currentDirectoryPermission(){
		return _session.getCurrentDirectory().getPublicMode();
	}
	
	/**
	* Gets the permission of a file
	* 	
	* @param name
	* 			name of the file
	* @return 
	* 			true if the file is public, false otherwise
	*/
	public boolean getFilePermission(String name){
		return _session.getCurrentDirectory().get(name).getPublicMode();
	}
	
	/**
	* Predicate that verifies if an entry is a File
	* 
	* @param name
	* 			name of the entry to be verified
	*/
	public boolean entryIsFile(String name){
		return ( _session.getCurrentDirectory().get(name) instanceof File);
	}
	
	/**
	* Predicate that verifies if the current filesystem was changed
	*/
	public boolean fileSystemChanged(){
		return _session.hasBeenChanged();
	}
	
	/**
	 * Predicate that verifies if an Directory is a Home Directory of file
	 * 
	 * @param name
	 * 			name of the directory to be verified
	 */
	public boolean isHomedir(String name){
		for(String username : _session.getUsers().keySet()) 
			if(_session.getUsers().get(username).getHomedir().equals(_session.getCurrentDirectory().getEntries().get(name)))
				return true;
		return false;
	}



	
}
