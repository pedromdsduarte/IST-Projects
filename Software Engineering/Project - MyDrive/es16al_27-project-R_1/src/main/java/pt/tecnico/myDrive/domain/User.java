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

    public User(String username) {
        this(username, username, username, "rwxd----");
        //super();
        //setUsername(name);
        //System.out.println("My Name is: " + getUsername());
    }


	/*
    public void xmlImport(Element userElement) throws ImportDocumentFromXMLException{
		
		try{
			setName(new String (userElement.getAttribute("username").getValue().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new ImportDocumentFromXMLException();
		}
		
		for (Element )
		
    }*/
    
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
		setDir(null);
		deleteDomainObject();
	}
    
}
