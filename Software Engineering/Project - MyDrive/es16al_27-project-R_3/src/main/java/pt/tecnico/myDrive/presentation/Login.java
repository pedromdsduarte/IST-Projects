package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.LoginUserService;
import pt.ist.fenixframework.Atomic;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;

public class Login extends MdCommand{
	public Login(Shell sh) { super((MdShell)sh,"login","use 'login <username> [password]' to login");}
	@Atomic
	public void execute(String[] args){
		if(args[0].equals("nobody") && args.length == 1){
			// login as nobody
			LoginUserService login = new LoginUserService(args[0],"");
			login.execute();
			getMdShell().setUsername(args[0]);
			getMdShell().setCurrentPath("/home/"+args[0]);
			setShellToken(login.getToken());
			return;
		} else if(!args[0].equals("nobody") && args.length != 2 ){
			// bad login
			throw new RuntimeException("USAGE: "+name()+" name");
		} else {
			// login as normal user

			// remove session for nobody
			if (getMdShell().getUsername().equals("nobody")) {
				MyDrive md = MyDrive.getInstance();
				SessionManager sm = md.getSessionManager();
				sm.removeSession(sm.getSessionByToken(getMdShell().getCurrentToken()));
			}

			LoginUserService login = new LoginUserService(args[0],args[1]);
			login.execute();
			getMdShell().setUsername(args[0]);
			getMdShell().setCurrentPath("/home/"+args[0]);
			setShellToken(login.getToken());
		}
	}
}
