package pt.tecnico.myDrive.domain;
import pt.tecnico.myDrive.service.*;
import pt.tecnico.myDrive.service.dto.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

import pt.ist.fenixframework.FenixFramework;

//import pt.tecnico.myDrive.domain.User;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.tecnico.myDrive.exception.*;

public class MyDrive extends MyDrive_Base {
	
	static final long MAX_PATH_LENGTH = 1024;
	static final Logger log = LogManager.getRootLogger();
  
	public static MyDrive getInstance(){
		MyDrive md = FenixFramework.getDomainRoot().getMydrive();
		if(md != null)
			return md;

		log.trace("new MyDrive");
		return new MyDrive();
   }
    
    private MyDrive() {
    	setRoot(FenixFramework.getDomainRoot());
    	setSessionManager(new SessionManager());
    	log.trace("New Session Manager created.");
    }
    
	public User getUserByUsername(String username){
		for (User user : getUserSet()){
			if(user.getUsername().equals(username))
				return user;
		}
		return null;
	}
	
	public Integer generateID(){
		setLastID(getLastID()+1);
		return getLastID();
	}
	
	public MyDriveFile getFileById(java.util.Set<MyDriveFile> set, int id) throws CannotFindFileException{
		for (MyDriveFile file : set){
			if(file.getId().equals(id))
				return file;
		}
		throw new CannotFindFileException(null);
	}
	
	    
	public MyDriveFile getFileByName(java.util.Set<MyDriveFile> set, String filename) throws CannotFindFileException{
		for (MyDriveFile file : set){
			if(file.getName().equals(filename))
				return file;
		}
		throw new CannotFindFileException(filename);
	}
	    
	 public void xmlImport(Element element) throws ImportDocumentFromXMLException, UsernameNotFoundException{
			try{
				for (Element unode: element.getChildren("users")){
					for (Element node: unode.getChildren("user")){
						String username 			= node.getAttribute("username").getValue();
						String password 			= node.getChild("password").getText();
						String name 				= node.getChild("name").getText();
						String umask				= node.getChild("umask").getText();
						
						if(username.equals("root"))	
							continue;
						
						User u = new User(username, password, name, umask);
				        addUser(u);		
					}
				}
				for (Element fnode: element.getChildren("files")){
					for (Element node: fnode.getChildren("file")){
						String type					= node.getAttribute("type").getValue();
						String name					= node.getChild("name").getText();
						String owner				= node.getChild("owner").getText();
						String fmask				= node.getChild("fmask").getText();
						String date;

						String path					= node.getChild("path").getText();
						String content;
						//User username;
						DateTime dt;
						Dir parent;
						//MyDriveFile file;
						String parentPath;		
						
						//Check if date field is empty
						if(node.getChild("date")==null)
							dt = new DateTime();
						else{
							DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
							date = node.getChild("date").getText();
							dt = formatter.parseDateTime(date);
						}
						
						//Check if content field is empty
						if(node.getChild("content")==null)
							content = "";
						else
							content = node.getChild("content").getText();
						
						
						
						if(type.equalsIgnoreCase("Dir")){
							if((path.equals("/") && name.equals("/")) || path.equals("/") || path.equals("/home/root") || path.equals("/home"))										//if special case /
								continue;
							//Create Directory
							Dir newDir = new Dir(name, generateID(), 100, getUserByUsername(owner), dt, fmask);
							//Add new Directory to parent
							if(path.substring(0,path.lastIndexOf("/")).equals("")){ //if only bar, example /home , /teste , etc
								newDir.setParentDir(getRootDir());
							}else{	//get parent dir if other cases not verified
								parentPath = path.substring(0,path.lastIndexOf("/"));
								parent = browseToDir(parentPath);
								newDir.setParentDir(parent);
								System.out.println("Substring home: "+path.substring(0,6)+ "\tsubstring owner: "+path.substring(6));
								if(path.substring(0,6).equals("/home/") && path.substring(6).equals(owner))
									getUserByUsername(owner).setHomeDir(newDir);
							}
						
						}else if(type.equalsIgnoreCase("PlainFile")){	
							//Create PlainFile
							PlainFile pf = new PlainFile(name, generateID(), getUserByUsername(owner), content, dt, fmask);
							//Add new PlainFile to parent
					    	if(path.equals("/") || path.substring(0,path.lastIndexOf("/")).equals(""))
					    		pf.setParentDir(getRootDir());
					    	else{
					    		parentPath = path.substring(0,path.lastIndexOf("/"));
					    		parent = browseToDir(parentPath);
					    		pf.setParentDir(parent);
					    	}
					    	
						}else if(type.equalsIgnoreCase("Link")){
							//Create Link
							Link lk = new Link(name, generateID(), getUserByUsername(owner), content, dt, fmask);
							//Add new Link to parent
							if(path.equals("/") || path.substring(0,path.lastIndexOf("/")).equals(""))
					    		lk.setParentDir(getRootDir());
					    	else{
					    		parentPath = path.substring(0,path.lastIndexOf("/"));
					    		parent = browseToDir(parentPath);
					    		lk.setParentDir(parent);
					    	}
							
						}else if(type.equalsIgnoreCase("App")){
							//Create App
							App ap = new App(name, generateID(), getUserByUsername(owner), content, dt, fmask);
							//Add new App to parent
							if(path.equals("/") || path.substring(0,path.lastIndexOf("/")).equals(""))
					    		ap.setParentDir(getRootDir());
					    	else{
					    		parentPath = path.substring(0,path.lastIndexOf("/"));
					    		parent = browseToDir(parentPath);
					    		ap.setParentDir(parent);
					    	}
						}
		
					}
				}
			}catch(ImportDocumentFromXMLException e){
				throw new ImportDocumentFromXMLException();
			}
	    }
    
	
    public Document xmlExport() throws ExportDocumentToXMLException {
    	
        Element mydrive = new Element("mydrive");
        Document doc;
        
        try {
        	
	        doc= new Document(mydrive);
	        
	        Element users = new Element("users");
	        Element files = new Element("files");
	
	        
	        List<MyDriveFile> filesList = orderFilesDFS();
	        
	        for (User u: getUserSet())
	            users.addContent(u.xmlExport());
	        for (MyDriveFile f : filesList)
	        	files.addContent(f.xmlExport());
	        
	        
	        mydrive.addContent(users);
	        mydrive.addContent(files); 
        
        } catch (ExportDocumentToXMLException e) {
        	throw new ExportDocumentToXMLException();
        }
        
        return doc;
    }
    
    
    
    public List<MyDriveFile> orderFilesDFS() {
       	
    	List<MyDriveFile> result = new ArrayList<MyDriveFile>();    	
    	Stack<MyDriveFile> toVisit = new Stack<MyDriveFile>();
    	MyDriveFile node = getRootDir();
    	toVisit.push(node);
    	
    	while (!toVisit.isEmpty()) {
    		node = toVisit.pop();
    		if (node.getClass().getSimpleName().equals("Dir")) {
	    		for (MyDriveFile children : ((Dir)node).getFileSet())
	    			if (children != node)
	    				toVisit.push(children);
    		}
    		result.add(node);
    	}
    	   	
    	return result;
    }
	
	
	public boolean hasUser(String username) {
		return getUserByUsername(username) != null;
	}

	
    public void cleanup() {
		getRootDir().remove();
		
		for(User user : getUserSet())
			user.remove();

	}
	
	public Dir browseToDir(String path) throws FileIsNotADirectoryException, DirectoryNotFoundException{
		String delim = "/";
		String[] tokens = path.split(delim);
		Dir currentdir = (Dir)getRootDir();
		Set<MyDriveFile> currentset = currentdir.getFileSet();
		
		ArrayList<String> tokenlist = new ArrayList<String>(Arrays.asList(tokens));
		if ((tokenlist.get(0)).equals("")){
			tokenlist.remove(0);
		}
	
		for(String token : tokenlist){
			if (currentset.contains(getFileByName(currentset, token))){
				if (getFileByName(currentset, token) instanceof Dir){
					currentdir = (Dir)getFileByName(currentset, token);
					currentset = currentdir.getFileSet();
				}
			}
			else 
				throw new DirectoryNotFoundException();
			}
		return currentdir;
	}
	
	
	public Dir browseToRelativeDir(long sessiontoken, String path) throws FileIsNotADirectoryException, DirectoryNotFoundException{
		SessionManager sm = getSessionManager();
		String delim = "/";
		String[] tokens = path.split(delim);
		Dir currentdir = (Dir)getRootDir();
		Set<MyDriveFile> currentset = currentdir.getFileSet();
		
		if ((path.substring(0,1).equals("/")) && path.length()==1){
			return currentdir;
		}
		
		ArrayList<String> tokenlist = new ArrayList<String>(Arrays.asList(tokens));
		if ((tokenlist.get(0)).equals("")){
			tokenlist.remove(0);
		}
		
		if (!(path.substring(0,1).equals("/"))){
			currentdir = sm.getCurrentDirByToken(sessiontoken);
		}

		currentset = currentdir.getFileSet();
		
		for(String token : tokenlist){
			if (token.equals("..") | token.equals(".")){
				currentdir = getRelativeDir(token, currentdir);
			}
			
			else if (currentset.contains(getFileByName(currentset, token))){
				if (getFileByName(currentset, token) instanceof Dir){
					currentdir = (Dir)getFileByName(currentset, token);
				}
				else throw new FileIsNotADirectoryException(token);
			}
			else 
				throw new DirectoryNotFoundException();
			
			currentset = currentdir.getFileSet();
		}
		return currentdir;
	}	
	
	public ListDirectoriesDto listDirectories(String path){
		ListDirectoriesDto directories = new ListDirectoriesDto();
		Dir currentdir = browseToDir(path);
		java.util.Set<MyDriveFile> currentset = currentdir.getFileSet();
		int size = 0;
		/*String content="Type\t\tPermissions\tSize\tOwner\tID\tDate\t\t\t\tName\t\n";
		content = content + (getRelativeDir("..",currentdir).getClass()).getSimpleName() + "\t\t" + getRelativeDir("..",currentdir).getFmask() + "\t" + getRelativeDir("..",currentdir).getDirSize() + "\t" + (getRelativeDir("..",currentdir).getOwner()).getUsername() + "\t" + getRelativeDir("..",currentdir).getId() + "\t" + getRelativeDir("..",currentdir).getDate() + "\t" + ".." + "\n";
		content = content + (getRelativeDir(".",currentdir).getClass()).getSimpleName() + "\t\t" + getRelativeDir(".",currentdir).getFmask() + "\t" + getRelativeDir(".",currentdir).getDirSize() + "\t" + ((getRelativeDir(".",currentdir)).getOwner()).getUsername() + "\t" + getRelativeDir(".",currentdir).getId() + "\t" + getRelativeDir(".",currentdir).getDate() + "\t" + "." + "\n";
		for(MyDriveFile fd : currentset){
			if (fd instanceof Dir){
				size = ((Dir)fd).getDirSize();
			}
			else{
				size = ((PlainFile)fd).getText().length();
			}
			content = content + fd.getClass().getSimpleName() + "\t\t" + fd.getFmask() + "\t" + size + "\t" + (fd.getOwner()).getUsername() + "\t" + fd.getId() + "\t" + fd.getDate() + "\t" + fd.getName() + "\n";
		}*/

		directories.addFile(new FileDto("Dir", "", -1, ".", currentdir.getDate(), 0, currentdir.getOwner()));
		directories.addFile(new FileDto("Dir", "", -1, "..", currentdir.getDate(), 0, currentdir.getOwner()));

		for (MyDriveFile fd : currentset) {
			if (fd instanceof Dir) {
				size = ((Dir)fd).getDirSize();
			} else {
				size = ((PlainFile) fd).getText().length();
			}

			FileDto f = new FileDto(fd.getClass().getSimpleName(), fd.getFmask(), fd.getId(), fd.getName(), fd.getDate(), size, fd.getOwner());
			directories.addFile(f);
		}
		//return directories.toString();
		return directories;
	}
	
	public void showFileContent(String path){
		Dir currentdir = browseToDir(path);
		Set<MyDriveFile> currentset = currentdir.getFileSet();
		String delim = "/";
		String[] tokens = path.split(delim);
		String filename = tokens[tokens.length-1];

		if(currentset.contains(getFileByName(currentset, filename)) && (getFileByName(currentset, filename) instanceof PlainFile)){
			PlainFile file = (PlainFile)getFileByName(currentset, filename);
			System.out.println(file.getText());
		}
	}
	
	public void removeFile(String path){
		String delim = "/";
		String[] tokens = path.split(delim);
		String filename = tokens[tokens.length-1];
		
		ArrayList<String> tokenlist = new ArrayList<String>(Arrays.asList(tokens));
		if ((tokenlist.get(0)).equals("")){
			tokenlist.remove(0);
		}
		
		String newpath = "";
		for (int i=0; i < tokenlist.size()-1; i++){
			newpath = newpath + "/" + tokenlist.get(i);
		}
		
		Dir currentdir = browseToDir(newpath);
		Set<MyDriveFile> currentset = currentdir.getFileSet();
		if(currentset.contains(getFileByName(currentset, filename))){
			if(getFileByName(currentset, filename) instanceof PlainFile){
				getFileByName(currentset, filename).remove();
			}
			else if(getFileByName(currentset, filename) instanceof Dir){
				currentdir = (Dir)getFileByName(currentset, filename);
				currentset = currentdir.getFileSet();
				if (currentset.size()<=2){
					currentdir.remove();
				}
			}
		}
	}
	
	
	public String getPath(MyDriveFile file){
		String path;
		String filename = file.getName();
		if (!(filename.equals("/"))){
			path = "/" + file.getName();
			Dir currentdir = file.getParentDir();
			int fileid = currentdir.getId();
			
			while (fileid != getRootDir().getId()){
				path = "/" + currentdir.getName() + path;
				currentdir = currentdir.getParentDir();
				fileid = currentdir.getId();
			}
			return path;
		}
		else
			return filename;
	}
	
    
    public User createUser(String username, String password, String name, String umask) throws UsernameAlreadyExistsException, InvalidUsernameException {
        if (username == null || username.equals("") ) throw new InvalidUsernameException();
       	if (usernameAlreadyExists(username)) throw new UsernameAlreadyExistsException(username);

        User u = new User(username, password, name, umask);
        Dir parent = (Dir) getFileById(getRootDir().getFileSet(), 2);
		Dir newHome = createDir(username, 100, getRootUser(), parent);
		newHome.setOwner(u);
		newHome.setUser(u);
		addUser(u);
		
		return u;
        }


    public static boolean usernameAlreadyExists(String username) {
        MyDrive md = MyDrive.getInstance();
        for (User u : md.getUserSet()) {
            if (u.getUsername().equals(username)) return true;
        }
        return false;
    }

    
    public static boolean directoryAlreadyExists(String name, Dir directory) {
        for (MyDriveFile mdf : directory.getFileSet())
            if (mdf.getClass().getSimpleName().equals("Dir") && mdf.getName().equals(name))
                return true;
        return false;
    }
    
    public Dir createDir(String name, int size, User owner, Dir parent) throws InvalidFilenameException, DuplicateDirectoryException {
        
    	if (name == null || name.equals(""))
    		throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	
    	if (owner == null)
    		throw new UserNotFoundException();
    	
    	if(parent == null)
    		throw new DirectoryNotFoundException();
    	
//    	if(!parent.hasWritePermission(owner))
//    		throw new UserDoesNotHaveWritePermissionException();
    		
    	if(hasFile(parent, name))
    		throw new DuplicateDirectoryException();
    	
    	if(pathLengthExceeded(parent,name))
    		throw new PathTooLongException();
        
        setLastID(getLastID() + 1);
        
        Dir newDir = new Dir(name, getLastID(), size, owner);
		newDir.setParentDir(parent);
		
        return newDir;
    }
    
    public PlainFile createPlainFile(String name, User owner, Dir parent, String text) {
    	
    	if (name == null || name.equals(""))
    		throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	
    	if (owner == null)
    		throw new UserNotFoundException();
    	
    	if(parent == null)
    		throw new DirectoryNotFoundException();
    	
    	if(!parent.hasWritePermission(owner))
    		throw new UserDoesNotHaveWritePermissionException();
    	
    	if(hasFile(parent, name))
    		throw new DuplicateFilenameException();
    	
    	if(pathLengthExceeded(parent,name))
    		throw new PathTooLongException();
		
    	// create PlainFile
		 setLastID(getLastID() + 1);
		
    	PlainFile pf = new PlainFile(name, getLastID() + 1, owner, text);
    	pf.setParentDir(parent);
    	
    	return pf;
    }
    
    public App createApp(String name, User owner, Dir parent, String text) {
    	
    	if (name == null || name.equals(""))
    		throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	
    	if (owner == null)
    		throw new UserNotFoundException();
    	
    	if(parent == null)
    		throw new DirectoryNotFoundException();
    	
    	// check if content is a Java name
    	if(!text.equals(""))
    		if(!isValidJavaName(text))
    			throw new InvalidJavaNameException();
    	
//    	if(!parent.hasWritePermission(owner))
//    		throw new UserDoesNotHaveWritePermissionException();
    	
    	// check if it is duplicate
    	if(hasFile(parent, name))
    		throw new DuplicateFilenameException();
    	
    	if(pathLengthExceeded(parent,name))
    		throw new PathTooLongException();
    	
    	// create App
    	 setLastID(getLastID() + 1);

    	App app = new App(name, owner, getLastID(),text);
    	app.setParentDir(parent);
    	
    	return app;
    }
    
    public Link createLink(long token, String name, User owner, Dir parent, String text) throws LinkCyclicityFoundException {
    	
    	if (name == null || name.equals(""))
    		throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	
    	if (owner == null)
    		throw new UserNotFoundException();
    	
    	if(parent == null)
    		throw new DirectoryNotFoundException();
    	
//    	if(!parent.hasWritePermission(owner))
//    		throw new UserDoesNotHaveWritePermissionException();
    	
    	if(hasFile(parent, name))
    		throw new DuplicateFilenameException();
    	
    	if(pathLengthExceeded(parent,name))
    		throw new PathTooLongException();
    	
    	// check if text is a valid Linux path
    	if(!isValidPath(text))
    		throw new NotAPathException();
    	
    	// check if text is longer than expected
    	if(text.length() > MAX_PATH_LENGTH)
    		throw new PathTooLongException();
    	
    	// check for cycles
    	String pathForThisLink = getPath(parent) + "/" + name;
    	List<String> paths = new ArrayList<String>();
    	paths.add(pathForThisLink);
    	if(!checkLinkCyclicity(paths,text,token))
    		throw new LinkCyclicityFoundException();
    	 setLastID(getLastID() + 1);

    	Link link = new Link(name, owner, getLastID(), text);
    	link.setParentDir(parent);
    	return link;
    }

    public long login(String username, String password) throws InvalidUsernameException, UsernameNotFoundException {

    	LoginUserService lus = new LoginUserService(username, password);
    	try {
    		lus.execute();
    	} catch (MyDriveException e) {
    		System.out.println(e.getMessage());
    	}

    	return lus.getToken();
    }
    
    public boolean hasFile(String path, String filename) {
    	Dir dir = null;
		Set<MyDriveFile> currentset = null;
    	try {
    		dir = browseToDir(path);
    		currentset = dir.getFileSet();
    		return currentset.contains(getFileByName(currentset,filename));
    	}
    	catch (CannotFindFileException e) {
    		return false;
    	}
    }
    
    public boolean hasFile(Dir dir, String filename) {
		Set<MyDriveFile> currentset = null;
    	try {
    		currentset = dir.getFileSet();
    		return currentset.contains(getFileByName(currentset,filename));
    	}
    	catch (CannotFindFileException e) {
    		return false;
    	}
    }
    
    public String getFileContent(String path){
		Dir currentdir = browseToDir(path);
		Set<MyDriveFile> currentset = currentdir.getFileSet();
		String delim = "/";
		String[] tokens = path.split(delim);
		String filename = tokens[tokens.length-1];

		if(currentset.contains(getFileByName(currentset, filename)) && (getFileByName(currentset, filename) instanceof PlainFile)){
			PlainFile file = (PlainFile)getFileByName(currentset, filename);
			return file.getText();
		}
		return null;
	}
	
	public void changeCurrentDirectory(long token, String path){
		Dir newdir = browseToRelativeDir(token, path);
		SessionManager sm = getSessionManager();
		sm.setCurrentDirByToken(token, newdir);
	}
	
	public Dir getRelativeDir(String name, Dir dir){
		if (name.equals("..") && !(dir.equals(getRootDir()))){
			dir = dir.getParentDir();
		}
		
		else if (name.equals(".") && !(dir.equals(getRootDir()))){
			//
		}
		
		else if ((name.equals(".") | name.equals("..")) && (dir.equals(getRootDir()))){
			dir = getRootDir();
		}
		
		return dir;
	}
	
	// check if a string is a valid java full name
	public boolean isValidJavaName(String line) {
		
		// define pattern (very permissive pattern for file paths)
		String pattern = "([a-zA-Z_$][a-zA-Z0-9_$]*)(.([a-zA-Z_$][a-zA-Z0-9_$]*)?)*";

		// create Pattern object
		Pattern r = Pattern.compile(pattern);

		// create Matcher object
		Matcher m = r.matcher(line);
		
		if (m.matches())
			return true;
		else
			return false;
	}
	
	// check if a string is a valid linux path
	public boolean isValidPath(String line) {
		
		// define pattern (very permissive pattern for file paths)
		String pattern = "^(/?[^/ ]*)*/?$";

		// create Pattern object
		Pattern r = Pattern.compile(pattern);

		// create Matcher object
		Matcher m = r.matcher(line);
		
		if (m.matches())
			return true;
		else
			return false;
	}
	
	
	public boolean pathLengthExceeded(Dir parent, String filename) {
		
		if((getPath(parent).length() + filename.length()) > MAX_PATH_LENGTH)
			return true;
		else
			return false;
	}
	
	public User getRootUser() {
		
		User u;
		
		for (Iterator<User> it = getUserSet().iterator(); it.hasNext(); ) {
	        u = it.next();
	        if(u.getUsername().equals("root"))
	        	return u;
		}
		
		return null;
	}
	
	// returns true if no cycles
	public boolean checkLinkCyclicity(List<String> paths, String text, long token) throws CannotFindFileException, NotAPathException {
		
		// check if text links to a previous link in the chain
		if(paths.contains(text))
			return false;
		
		// check if text points to a link
		MyDriveFile file = getFileByPath(text, token);
		
		if(file instanceof Link) {
			paths.add(text);
			return checkLinkCyclicity(paths, ((Link) file).getText(), token);
		}
		
		return true;
	}
	
	public MyDriveFile getFileByPath(String orig, long token) throws CannotFindFileException, NotAPathException {
		
		if(!isValidPath(orig))
			throw new NotAPathException();
		
		String delim = "/";
		String[] tokens = orig.split(delim);
		ArrayList<String> tokenList = new ArrayList<String>(Arrays.asList(tokens));
		Dir currentDir;
		Set<MyDriveFile> currentSet;
		MyDriveFile file;
		MyDriveFile result = null;
		
		if (tokenList.get(0).equals("")) { // path is absolute
			tokenList.remove(0);
			currentDir = (Dir) getRootDir();
		}
		else { // path is relative
			currentDir = getSessionManager().getCurrentDirByToken(token); // TO DO
		}

		currentSet = currentDir.getFileSet();
		
		// build path
		Iterator<String> iterator = tokenList.iterator();
		String tok;
		
		while(iterator.hasNext()) {
			tok= iterator.next();
			
			if(tok.equals("..") | tok.equals(".")) {
				currentDir = getRelativeDir(tok, currentDir);
				currentSet = currentDir.getFileSet(); // TO DO: check if file set is empty
				result = currentDir;
			}
			else {
				file = getFileByName(currentSet, tok);
				// check if file is a directory; if not, check it isn't the last file in the path
				if(file instanceof Dir) {
					currentDir = (Dir) file;
					currentSet = currentDir.getFileSet();
					result = currentDir;
				}
				else
					if(iterator.hasNext()) // it's a plainfile but not the last file in path
						throw new FileIsNotADirectoryException();
					else
						result = file;
			}
		}
		
		return result;
	}
}

	
