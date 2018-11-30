package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.LinkContentCannotBeChangedException;


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
	
	@Override
	public void setText(String text) {
		
		if(getText() != null)
			throw new LinkContentCannotBeChangedException();
		
		super.setText(text);
	}
	
	public MyDriveFile resolveLink() throws DirectoryNotFoundException {
		MyDrive md = MyDrive.getInstance();
		String path; 
		Dir dir;
		MyDriveFile file = this;
		
		while (file instanceof Link) {
			path = ((Link) file).getText();
			
			String[] tokens = path.split("/");
			String filename = tokens[tokens.length-1];
			path = path.substring(0, path.length()-filename.length());
	
			if (path.startsWith("/"))
				dir = md.browseToDir(path);
			else {
				if (path.startsWith("./"))
					path = path.substring(2, path.length());
				path = md.getPath(this) + "/" + path;	//HACK: gets absolute path
				dir = md.browseToDir(path);
			}
				
			file = md.getFileByName(dir.getFileSet(), filename);
		}
		
		return file;
	}
	
	public boolean isCyclic() {
		//TODO
		return false;
	}
	
}
