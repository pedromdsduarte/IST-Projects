package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.service.ChangeDirectoryService;
import pt.tecnico.myDrive.service.ListDirectoryService;
import pt.ist.fenixframework.Atomic;

public class List extends MdCommand{
	
	public List(Shell sh) { 
		super((MdShell)sh,"ls","lista a informacao das entradas existentes numa directoria"
				+ "(caso omitida, a actual)");
	}
	@Atomic
	@Override 
	public void execute(String[] args) {

		Long token = getMdShell().getCurrentToken();
		
		if (token == null) {
			throw new RuntimeException("You must be logged in.");
		}
		
		if (args.length == 0) {
			
		    ListDirectoryService lds = new ListDirectoryService(token);	
		    lds.execute();
			System.out.println(lds.result());
		    System.out.println("use 'ls <directory>' to list the contents of a directory");
		    
		} else if (args.length == 1){
			
			String path = args[0];
			ChangeDirectoryService cd = new ChangeDirectoryService(token, ".");
			cd.execute();
			String actual = cd.result();
			new ChangeDirectoryService(token, path).execute();
			
			ListDirectoryService lds = new ListDirectoryService(token);	
		    lds.execute();
			System.out.println(lds.result());
			
			new ChangeDirectoryService(token, actual).execute();
		    
		} else {
			throw new RuntimeException("USAGE: "+name()+" name");
		}
	}

}
