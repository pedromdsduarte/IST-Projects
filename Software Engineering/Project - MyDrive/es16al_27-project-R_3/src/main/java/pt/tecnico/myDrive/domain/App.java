package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

public class App extends App_Base {
    
    public App() {
        super();
    }
	
	public App(String name, User owner, int id, String text){
		super();
		initMDF(name, id, owner);		
		setText(text);
	}
	public App(String name, int id, User owner, String text, DateTime date, String fmask){
		super();
		initMDF(name, id, owner, date, fmask);
		setText(text);
	}
    
	
	public boolean hasExtension(String extension) {
    	
    	for(Association assoc : getAssociationSet())
    		if(assoc.getExtension().equals(extension))
    			return true;
    	
    	return false;
    }
}
