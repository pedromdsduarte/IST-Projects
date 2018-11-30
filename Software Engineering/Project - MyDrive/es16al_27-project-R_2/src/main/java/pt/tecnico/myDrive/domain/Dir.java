package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;
import java.util.*;

public class Dir extends Dir_Base {
    
    public Dir(String name, int id, int size, User owner) {
        super();
        initMDF(name, id, owner);
        setSize(size);
    }
    public Dir(String name, int id, int size, User owner, DateTime date, String fmask){
    	super();
    	initMDF(name, id, owner, date, fmask);
    	setSize(size);
    }
    
    
    @Override
    public void remove() {
		for(MyDriveFile mdf : getFileSet())
			if(!mdf.getName().equals("/"))
				mdf.remove();
		
		setUser(null);
		setMydrive(null);
		setParentDir(null);
		setOwner(null);
		for (Session s : getSessionSet())
			if (s.getCurrentDir().equals(this))
				s.setCurrentDir(null);
		deleteDomainObject();
	}
	
	public int getDirSize(){
		return getFileSet().size() + 2;
	}
}
