package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.DuplicateExtensionException;
import pt.tecnico.myDrive.exception.ExportDocumentToXMLException;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UsernameTooShortException;
import pt.tecnico.myDrive.exception.UsernameAlreadyExistsException;
import pt.tecnico.myDrive.exception.PasswordTooShortException;

//import pt.tecnico.myDrive.exception.

public class User extends User_Base {
   
   private final Integer MAX_PASSWORD_LENGTH = 8;
   private final Integer MAX_USERNAME_LENGTH = 3;

    public User(String username, String password, String name, String umask) {
        super();
        setUsername(username);
        setPassword(password);
        setName(name);
        setUmask(umask);
    }

    
    public Element xmlExport() throws ExportDocumentToXMLException {
    	Element user;
    	
    	try {
	        user = new Element("user");
	        user.setAttribute("username", getUsername());
	
	        Element password = new Element("password");
	        Element name = new Element("name");
	        Element umask = new Element("umask");
	        
	        password.addContent(getPassword());
	        name.addContent(getName());
	        umask.addContent(getUmask());
	        
	        user.addContent(password);
	        user.addContent(name);
	        user.addContent(umask);
    	} catch (Exception e) {
    		throw new ExportDocumentToXMLException();
    	}
        
        return user;
    }
    
    public void remove() {
        if (!getUsername().equals("nobody")) {
            setMydrive(null);
            setHomeDir(null);
            for (Session s : getSessionSet())
                if (s.getUser().equals(this))
                    s.setUser(null);
            deleteDomainObject();
        }
	}
    
    public void addAssociation(String extension, App app) throws DuplicateExtensionException {
    		
    	if(hasExtension(extension))
    		throw new DuplicateExtensionException();
    	else
    		new Association(this, extension, app);
    }
    
    public boolean hasExtension(String extension) {
    	
    	for(Association assoc : getAssociationSet())
    		if(assoc.getExtension().equals(extension))
    			return true;
    	
    	return false;
    }
    
    public App getAppByExtension(String extension) {
    	if (extension == null)
    		return null;
    	
    	for(Association assoc : getAssociationSet())
    		if(assoc.getExtension().equals(extension))
    			return assoc.getApp();
    	
    	return null;
    }

    @Override
    public void setUsername(String username) {
        if (username.equals(null)) throw new InvalidUsernameException();
        if (username.length() < MAX_USERNAME_LENGTH) throw new UsernameTooShortException();
        if (MyDrive.usernameAlreadyExists(username)) throw new UsernameAlreadyExistsException();
        super.setUsername(username);
    }

    @Override
    public void setPassword(String password) {
        if (getUsername().equals("nobody")) {
            super.setPassword("");
            return;
        } else if (getUsername().equals("root")) {
             // password for root does not verify minimum characters
            super.setPassword(password);
            return;
        }
        //if (password.length() < MAX_PASSWORD_LENGTH) throw new PasswordTooShortException();
        super.setPassword(password);
    }
}
