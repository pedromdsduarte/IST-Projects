package pt.tecnico.myDrive.presentation;

import java.util.Map;
import java.util.TreeMap;

import pt.ist.fenixframework.Atomic;

import pt.tecnico.myDrive.exception.CannotFindEnvVarException;
import pt.tecnico.myDrive.service.AddEnvVariableService;


public class Environment extends MdCommand {
	
	public Environment(Shell sh) { 
		
		super((MdShell)sh, "env" ,"usage:\n"+ "\tenv - print all environment variables" + 
				"\tenv name - print the value of name environment variable" + 
				"\tenv name value - creates or sets new value for name environment variable");
	}

	@Atomic
	@Override
	public
	void execute(String[] args) {
		
		long token = getMdShell().getCurrentToken();
		
		switch(args.length) {
		
		case 2:
			AddEnvVariableService service = new AddEnvVariableService(token, args[0], args[1]);
			service.execute();
			
			// update envVars map
			getMdShell().setEnvVars(service.getVarList());
			break;
			
		case 1:
			String value = getMdShell().getEnvVars().get(args[0]);
			
			if(value == null)
				throw new CannotFindEnvVarException(args[0]);
			
			System.out.println(args[0] + " = " + value);
			break;
			
		case 0:
			printEnvVars();
			break;
			
		default:
			throw new RuntimeException("USAGE: "+ name() +" name");
		}
	}

	private void printEnvVars() {
		
		Map<String,String> envVars = getMdShell().getEnvVars();
		
		if(envVars.size() == 0)
			System.out.println("No environment variables defined!");
		else {
			System.out.println("Current environment variables:");
			
			for(Map.Entry<String,String> entry : envVars.entrySet()) {
				  String key = entry.getKey();
				  String value = entry.getValue();
	
				  System.out.println(key + " = " + value);
			}
		}
	}
}
