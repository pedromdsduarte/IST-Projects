package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

public class App extends App_Base {
    
    public App() {
        super();
    }
	
	public App(String name,User user, int id, String fmask, String text, DateTime date){
		super();
		setName(name);
		setUser(user);		
		setId(id);
		setFmask(fmask);
		setText(text);
		setDate(date);
	}
    
}
