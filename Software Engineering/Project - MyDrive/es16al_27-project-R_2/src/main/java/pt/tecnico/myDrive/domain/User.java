package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import pt.tecnico.myDrive.exception.ExportDocumentToXMLException;

import java.io.UnsupportedEncodingException;

//import pt.tecnico.myDrive.exception.ImportDocumentException;

public class User extends User_Base {
   
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
		setMydrive(null);
		setHomeDir(null);
		for (Session s : getSessionSet())
			if (s.getUser().equals(this))
				s.setUser(null);
		deleteDomainObject();
	}
    
}
