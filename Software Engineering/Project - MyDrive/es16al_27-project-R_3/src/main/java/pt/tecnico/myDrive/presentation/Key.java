package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.EnvVar;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import java.util.*;
import pt.tecnico.myDrive.service.ChangeDirectoryService;

public class Key extends MdCommand{
	public Key(Shell sh) { super((MdShell)sh,"token","troca o utilizador da corrente sessão");}
	
	@Atomic
	@Override
	public void execute(String[] args) {
		
		MyDrive md = MyDrive.getInstance();
		SessionManager sm = md.getSessionManager();
		Long token = getMdShell().getCurrentToken();
		TreeMap<String, String> envVar = new TreeMap();
		ChangeDirectoryService cwd;
		
		if (token == null) {
			throw new RuntimeException("You must be logged in.");
		}

		if (args.length == 0) {
			System.out.println("token: " + token);
			System.out.println("user: " + sm.getUserByToken(token).getUsername());
		    System.out.println("use 'token <username>' to change the user of the current session");
		    
		} else if (args.length == 1){
			
			String username = args[0];
			try{
				token = sm.getTokenByUsername(username);
				getMdShell().setToken(token);
				System.out.println("token: " + token);
				cwd = new ChangeDirectoryService(getMdShell().getCurrentToken(),".");
				envVar.clear();
				for (EnvVar ev: sm.getSessionByToken(token).getEnvVarSet()){
					envVar.put(ev.getName(), ev.getValue());
				}
				getMdShell().setEnvVars(envVar);
			}catch(InvalidTokenException e){
				throw e;
			}catch(UsernameNotFoundException e){
				throw e;
			}
		    cwd.execute();
			getMdShell().setCurrentPath(cwd.result());
			getMdShell().setUsername(username);
		} else {
			throw new RuntimeException("USAGE: "+name()+" name");
		}
	}
}
