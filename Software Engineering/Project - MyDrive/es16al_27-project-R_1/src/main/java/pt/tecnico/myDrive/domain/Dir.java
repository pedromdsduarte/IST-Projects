package pt.tecnico.myDrive.domain;
import org.joda.time.DateTime;

public class Dir extends Dir_Base {
    
    public Dir(String name, int id, String fmask, int size) {
        super();
        initMDF(name, id,fmask);
        setSize(size);
    }
	
	public Dir(String name, int id, String fmask, int size, DateTime date){
		super();
		initMDF(name, id, fmask);
		setSize(size);
		setDate(date);
	}
    
    
    @Override
    public void remove() {
		for(MyDriveFile mdf : getFileSet())
			if(!mdf.getName().equals("/"))
				mdf.remove();
		
		setUser2(null);
		setUser(null);
		setMydrive(null);
		setMydrive2(null);
		setDir(null);
		deleteDomainObject();
		
		
	}
}
