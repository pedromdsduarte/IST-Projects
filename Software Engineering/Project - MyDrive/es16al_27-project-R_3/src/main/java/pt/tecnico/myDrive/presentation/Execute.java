package pt.tecnico.myDrive.presentation;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;

import pt.tecnico.myDrive.service.ExecuteFileService;

import pt.ist.fenixframework.Atomic;

public class Execute extends MdCommand {
	
	public Execute(Shell sh) {
		super((MdShell)sh, "do","Executa o ficheira no caminho (path) com os argumentos (args)" );
		}
	
	@Atomic
	@Override
	public
	void execute(String[] args) {
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		Long token = getMdShell().getCurrentToken();
		
		if (token == null) {
			throw new RuntimeException("You must be logged in.");
		}
		
		if(args.length == 0 ){
			throw new RuntimeException("USAGE: "+name()+" path"+" args(optional)");
		}
		
		int length = args.length - 1;
		String[] execArgs = new String[length];
		
		for(int i=0; i<length; i++)
			execArgs[i] = args[i+1];
		
		
		ExecuteFileService efs = new ExecuteFileService(token, args[0], execArgs);
		efs.execute();
		
	}
	
	
}
