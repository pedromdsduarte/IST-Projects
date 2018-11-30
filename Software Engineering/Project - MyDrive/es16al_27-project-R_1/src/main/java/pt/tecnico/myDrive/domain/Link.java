package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

public class Link extends Link_Base {
    
    public Link() {
        super();
    }
    
	public Link(String name,User user, int id, String fmask, String text, DateTime date){
		super();
		setName(name);
		setUser(user);
		setId(id);
		setFmask(fmask);
		setText(text);
		setDate(date);
	}
}
