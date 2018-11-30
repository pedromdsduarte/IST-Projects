package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

import org.jdom2.Element;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
        super();
    }
	public PlainFile(String name,User user, int id, String fmask, String text, DateTime date){
		super();
		initMDF(name, id, fmask);
		setUser(user);
		setText(text);
		setDate(date);
	}
 
	@Override
	public Element xmlExport() {
		Element file = super.xmlExport();
		Element content = new Element("content");
		content.addContent(getText());
		file.addContent(content);
		
        return file;
  
	}
	
}
