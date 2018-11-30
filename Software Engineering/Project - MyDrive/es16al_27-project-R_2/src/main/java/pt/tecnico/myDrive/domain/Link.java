package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }
    
	public Link(String name, User owner, int id, String text){
		super();
		initMDF(name, id, owner);		
		setText(text);
	}
	public Link(String name, int id, User owner, String text, DateTime date, String fmask){
		super();
		initMDF(name, id, owner, date, fmask);
		setText(text);
	}
}
