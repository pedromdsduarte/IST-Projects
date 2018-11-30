package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

import org.jdom2.Element;

public class PlainFile extends PlainFile_Base {
    
    public PlainFile() {
        super();
    }
	public PlainFile(String name, int id, User owner, String text){
		super();
		initMDF(name, id, owner);
		setText(text);
	}
	
	public PlainFile(String name, int id, User owner, String text, DateTime date, String fmask){
		super();
		initMDF(name, id, owner, date, fmask);
		setText(text);
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
