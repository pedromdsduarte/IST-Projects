package pt.tecnico.myDrive.domain;

import org.jdom2.Element;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

import pt.tecnico.myDrive.exception.ExportDocumentToXMLException;

import org.joda.time.format.DateTimeFormat;

public class MyDriveFile extends MyDriveFile_Base 	{
    
    public MyDriveFile() {
        super();
    }

    public MyDriveFile(String name, int id, User owner) {
        super();
        initMDF(name, id, owner);
	}
	
    
    public Element xmlExport() throws ExportDocumentToXMLException {
    	
    	Element file;
    	
    	try {
    	
	        file = new Element("file");
	                
	        String type = getClass().getSimpleName();
	        
	        
	        file.setAttribute("id", getId().toString());
	        file.setAttribute("type",type);
	
	        Element owner = new Element("owner");
	        Element name = new Element("name");
	        Element fmask = new Element("fmask");
	        Element date = new Element("date");
	        Element path = new Element("path");
	        
	        DateTimeFormatter dateformat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
	        String datestr = getDate().toString(dateformat);
	        
	        owner.addContent(getOwner().getUsername());
	        name.addContent(getName());
	        fmask.addContent(getFmask());
	        date.addContent(datestr);
	        path.addContent(MyDrive.getInstance().getPath(this)); 
	        
	        file.addContent(owner);
	        file.addContent(name);
	        file.addContent(fmask);     
	        file.addContent(path);
	        file.addContent(date);
    	} catch (Exception e) {
    		throw new ExportDocumentToXMLException();
    	}
        
        return file;
    }
    
    protected void initMDF(String name, int id, User owner) {
		setName(name);
		setId(id);
		setFmask(owner.getUmask());
		setDate(new DateTime());
		setOwner(owner);
	}
    
    protected void initMDF(String name, int id, User owner, DateTime date, String fmask){
    	initMDF(name,id,owner);
    	setFmask(fmask);
    	setDate(date);
    }
	
	public void remove() {
		setOwner(null);
		setParentDir(null);
		deleteDomainObject();
	}

	public boolean hasReadPermission(User user) {
		if (user == null) return false;
		if (user.getUsername().equals("root")) return true;
		char[] fileMask = getFmask().toCharArray();
		
		MyDrive md = MyDrive.getInstance();
		if	(user.equals(md.getRoot())){
			return true;
		}
		else if(user.equals(md.getUserByUsername("nobody"))){
			return false;
		}
		
		if (user.equals(getOwner())) {
			// dono
			if (fileMask[0] == 'r') 
                return true;
            else 
                return false;
		} else {
			// nao dono
			if (fileMask[4] == 'r')
                return true;
            else
                return false;
		}

	}

	public boolean hasWritePermission(User user) {
		if (user == null) return false;
		if (user.getUsername().equals("root")) return true;
		char[] fileMask = getFmask().toCharArray();
		
		MyDrive md = MyDrive.getInstance();
		if	(user.equals(md.getRoot())){
			return true;
		}
		else if(user.equals(md.getUserByUsername("nobody"))){
			return false;
		}
		
		if (user.equals(getOwner())) {
			// dono
			if (fileMask[1] == 'w') 
                return true;
            else 
                return false;
		} else {
			// nao dono
			if (fileMask[5] == 'w')
                return true;
            else
                return false;
		}
		
	}

	public boolean hasExecutePermission(User user) {
		if (user == null) return false;
		if (user.getUsername().equals("root")) return true;
		char[] fileMask = getFmask().toCharArray();

		MyDrive md = MyDrive.getInstance();
		if	(user.equals(md.getRoot())){
			return true;
		}
		else if(user.equals(md.getUserByUsername("nobody"))){
			return false;
		}
		
		if (user.equals(getOwner())) {
			// dono
			if (fileMask[2] == 'x') 
                return true;
            else 
                return false;
		} else {
			// nao dono
			if (fileMask[6] == 'x')
                return true;
            else
                return false;
		}
		
	}

	public boolean hasDeletePermission(User user) {
		if (user == null) return false;
		if (user.getUsername().equals("root")) return true;
		char[] fileMask = getFmask().toCharArray();
		
		MyDrive md = MyDrive.getInstance();
		if	(user.equals(md.getRoot())){
			return true;
		}
		else if(user.equals(md.getUserByUsername("nobody"))){
			return false;
		}
		
		if (user.equals(getOwner())) {
			// dono
			if (fileMask[3] == 'd') 
                return true;
            else 
                return false;
		} else {
			// nao dono
			if (fileMask[7] == 'd')
                return true;
            else
                return false;
		}
		
	}


}
