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

    public MyDriveFile(String name, int id, String fmask) {
        super();
        initMDF(name, id, fmask);
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
	        
	        owner.addContent(getUser().getUsername());
	        name.addContent(getName());
	        fmask.addContent(getFmask());
	        date.addContent(datestr);
	        path.addContent(getPath(this));
	        
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
    
    protected void initMDF(String name, int id, String fmask) {
		setName(name);
		setId(id);
		setFmask(fmask);
		setDate(new DateTime());
	}
	
	public void remove() {
		setMydrive(null);
		setUser(null);
		setDir(null);
		deleteDomainObject();
	}
	
	public String getPath(MyDriveFile file) {
		MyDrive md = MyDrive.getInstance();
		return md.getPath(file);
	}
    
}
