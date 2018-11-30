package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;

import pt.ist.fenixframework.FenixFramework;

//import pt.tecnico.myDrive.domain.User;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import pt.tecnico.myDrive.exception.*;

public class MyDrive extends MyDrive_Base {
	
	
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
    }
    
	public User getUserByUsername(String username){
		for (User user : getUserSet()){
			if(user.getUsername().equals(username))
				return user;
		}
		return null;
	}
	
	public MyDriveFile getFileById(int id){
		for (MyDriveFile file : getFileSet()){
			if(file.getId().equals(id))
				return file;
		}
		return null;
	}
	
	    
    public void xmlImport(Element element) throws ImportDocumentFromXMLException, UsernameNotFoundException{
		try{
			for (Element unode: element.getChildren("users")){
				for (Element node: unode.getChildren("user")){
					String username 			= node.getAttribute("username").getValue();
					String password 			= node.getChild("password").getText();
					String name 				= node.getChild("name").getText();
					String umask				= node.getChild("umask").getText();
					
					User user = createUser(username, password, name, umask);
				}
			}
			for (Element fnode: element.getChildren("files")){
				for (Element node: fnode.getChildren("file")){
					int id						= Integer.parseInt(node.getAttribute("id").getValue());
					System.out.println("\n\n\tid: "+id+"\n\n");
					String type					= node.getAttribute("type").getValue();
					String name					= node.getChild("name").getText();
					String owner				= node.getChild("owner").getText();
					String fmask				= node.getChild("fmask").getText();
					String date					= node.getChild("date").getText();
					/*Cant Implement yet*/
					String path					= node.getChild("path").getText();
					String content				= "";
					User username;
					Dir dir 					= browseToDir(path);
				
					MyDriveFile file = getFileById(id);
										
					if(date.equals(""))
						date = "22/03/2014 10:14:04";
					
					
					DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
					DateTime dt = formatter.parseDateTime(date);
					
					if(file == null){
						if(node.getChild("content") != null)
							content = node.getChild("content").getText();
						
						username = getUserByUsername(owner);
						if(username == null)
							throw new UsernameNotFoundException();
						
						
						if(type.equalsIgnoreCase("PlainFile")){
							//PlainFile plainfile = new PlainFile(name, username, getFileCount()+1, fmask, content, dt);
							//addFile(plainfile);
							PlainFile newfile = createPlainFile(name,username,id,fmask,content,dt);
							addFile(newfile);
							
						}else if(type.equalsIgnoreCase("Link")){
							//Link link = new Link(name, username, getFileCount()+1, fmask, content, dt);
							//addFile(link);
							Link newlink = createLink(name,username,id,fmask,content,dt);
							addFile(newlink);
							newlink.setDir(dir);
						}else if(type.equalsIgnoreCase("App")){
							//App app = new App(name, username, getFileCount()+1, fmask, content, dt);
							//addFile(app);
							App newapp = createApp(name,username,id,fmask, content, dt);
							addFile(newapp);
							newapp.setDir(dir);
						//ADICIONAR FICHEIRO
						}else if(type.equalsIgnoreCase("Dir")){
							//Dir dir = new Dir(name, username, getFileCount()+1 ,fmask, content, dt);
							//addDir(dir);
							Dir newdir = createDir(name,username.getUsername(),id,fmask,2,dt);
							addFile(newdir);
							
						}else{
							/* Need to create InvalidTypeFileException */
							System.out.println("\n\t\t"+type + " is not a valid type of file at myDrive!\n");
							throw new ImportDocumentFromXMLException();
						}
					}else{
						/* Need to create InvalidIdFileException */
						System.out.println("\n\nFile id already exists!\n\n");
						throw new ImportDocumentFromXMLException();
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
	
	        
	        List<MyDriveFile> filesList = new ArrayList<MyDriveFile>(getFileSet());
	        filesList = orderFilesDFS(filesList);
	        
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
    
    
    
    public List<MyDriveFile> orderFilesDFS(List<MyDriveFile> filesList) {
       	
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
    
    public MyDriveFile getRootDir() {
		return getFileByName("/");
	}
	
	public MyDriveFile getFileByName(String nameFile) {
		for (MyDriveFile fd : getFileSet()) {
			if(fd.getName().equals(nameFile))
				return fd;
		}
		
		return null;
	}	
    
    public void cleanup() {
		getFileByName("/").remove();
		
		for(User user : getUserSet())
			user.remove();
			

	}
	
	public Dir browseToDir(String path){
		//adicionar if's para -> começar sem / e haver . e ..
		String delim = "/";
		String[] tokens = path.split(delim);
		Dir currentdir = (Dir)getRootDir();
		java.util.Set currentset = currentdir.getFileSet();
		
		ArrayList<String> tokenlist = new ArrayList<String>(Arrays.asList(tokens));
		if ((tokenlist.get(0)).equals("")){
			tokenlist.remove(0);
		}
				
		for(String token : tokenlist){
			if (currentset.contains(getFileByName(token)) && (getFileByName(token) instanceof Dir)){
					currentdir = (Dir)getFileByName(token);
					currentset = currentdir.getFileSet();
				}
		}
				//Else needed Maybe
		return currentdir;
	}
	
	public void listDirectories(String path){
		Dir currentDir = browseToDir(path);
		currentDir.getFileSet();
		System.out.println("--Printing directories");
		for(MyDriveFile fd : currentDir.getFileSet()){
			System.out.println(">" + fd.getName() + "\n");
		}
	}
	

	public void showFileContent(String path){
		Dir currentdir = browseToDir(path);
		java.util.Set currentset = currentdir.getFileSet();
		String delim = "/";
		String[] tokens = path.split(delim);
		String filename = tokens[tokens.length-1];
		
		System.out.println("--Showing File Content of:" + filename);
		
		if(currentset.contains(getFileByName(filename)) && (getFileByName(filename) instanceof PlainFile)){
			PlainFile file = (PlainFile)getFileByName(filename);
			System.out.println(file.getText());
		}
	}
	
	public void removeFile(String path){
		Dir currentdir = browseToDir(path);
		java.util.Set currentset = currentdir.getFileSet();
		String delim = "/";
		String[] tokens = path.split(delim);
		String filename = tokens[tokens.length-1];
		
		System.out.println("--Removing file" + filename);
		
		if(currentset.contains(getFileByName(filename))){
			if((getFileByName(filename) instanceof PlainFile) || ((getFileByName(filename) instanceof Dir) && currentset.size()<=2)){
				currentset.remove(getFileByName(filename));
			}
		}
	}
	
	
	public String getPath(MyDriveFile file){
		String path;
		String filename = file.getName();
		if (!(filename.equals("/"))){
			path = "/" + file.getName();
			System.out.println(path + "1");
			Dir currentdir = file.getDir();
			int fileid = currentdir.getId();
			
			while (fileid != getRootDir().getId()){
				path = "/" + currentdir.getName() + path;
				System.out.println(currentdir.getName() + "2");
				currentdir = currentdir.getDir();
				System.out.println(currentdir.getDir() + "3");
				fileid = currentdir.getId();
			}
		}
		else
			path = filename;
		return path;
	}
	
	
    public User createUser(String username) throws UsernameAlreadyExistsException, InvalidUsernameException {
        User u = createUser(username, username, username, "rwxd----");
        return u;
    }
    
    
    public static User createUser(String username, String password, String name, String umask) throws UsernameAlreadyExistsException, InvalidUsernameException {
        if (username == null || username.equals("") ) throw new InvalidUsernameException();
        if (usernameAlreadyExists(username)) throw new UsernameAlreadyExistsException(username);

        User u = new User(username, password, name, umask);
        Dir rootDir, homeDir = null;
        MyDrive md = MyDrive.getInstance();
        md.addUser(u);

        // se o user a registar for root, cria a pasta "/" e a pasta "/home"
        if (username.equals("root")) {
            rootDir = createDir("/", "root", md.getFileCount() + 1, umask, 3);
            homeDir = createDir("home", "root", md.getFileCount() + 2, umask, 2);
            
            md.addFile(rootDir);
            rootDir.setDir(rootDir);
            rootDir.setUser(u);

            md.addFile(homeDir);
            homeDir.setDir(rootDir);
            homeDir.setUser(u);
        }

        // aqui vai ser criada a pasta de cada utilizador, na forma "/home/USERNAME"
        Dir userDir = createDir(username.toLowerCase(), username, md.getFileCount()+1, umask, 2);
        md.addFile(userDir);
        if (homeDir == null)
            homeDir = md.browseToDir("home");
        userDir.setDir(homeDir);
        userDir.setUser(u);

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
    
    public static Dir createDir(String name, String username, int id, String fmask, int size) throws InvalidFilenameException, DuplicateDirectoryException {
        MyDrive md = MyDrive.getInstance();
        Dir curr = md.getCurrentDir();

        if (name == null || name.equals("")) throw new InvalidFilenameException(); // invalid directory name? empty directory name?
        //if (directoryAlreadyExists(name, curr)) throw new DuplicateDirectoryException();

        return new Dir(name, id, fmask, size);
    }
    public static Dir createDir(String name, String user, int id, String fmask, int size, DateTime date) throws InvalidFilenameException, DuplicateDirectoryException {
        
    	
    	MyDrive md = MyDrive.getInstance();
        Dir curr = md.getCurrentDir();

        if (name == null || name.equals("")) throw new InvalidFilenameException(); // invalid directory name? empty directory name?
        //if (directoryAlreadyExists(name, curr)) throw new DuplicateDirectoryException();

        return new Dir(name, id, fmask, size, date);
    }
    
    // creates a plain file, and sets its owner
    public static PlainFile createPlainFile(String name, User owner, int id, String fmask, String text, DateTime date) {
    	if (name == null || name.equals("")) throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	if (owner == null) throw new UsernameNotFoundException();

    	PlainFile pf = new PlainFile(name, owner, id, fmask, text, date);
    	pf.setUser(owner);
    	return pf;
    }
    
    public static App createApp(String name, User owner, int id, String fmask, String text, DateTime date) {
    	if (name == null || name.equals("")) throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	if (owner == null) throw new UsernameNotFoundException();

    	App app = new App(name, owner, id, fmask, text, date);
    	app.setUser(owner);
    	return app;
    }
    
    public static Link createLink(String name, User owner, int id, String fmask, String text, DateTime date) {
    	if (name == null || name.equals("")) throw new InvalidFilenameException(); // invalid directory name? empty directory name?
    	if (owner == null) throw new UsernameNotFoundException();

    	Link link = new Link(name, owner, id, fmask, text, date);
    	link.setUser(owner);
    	return link;
    }
    
}
	
