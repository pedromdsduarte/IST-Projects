package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.ChangeDirectoryService;
import pt.ist.fenixframework.Atomic;

public class ChangeWorkingDirectory extends MdCommand{
	public ChangeWorkingDirectory(Shell sh) {super((MdShell)sh,"cwd","use 'cwd [path]' to change working directory");}

	@Atomic
	public void execute(String[] args){
		ChangeDirectoryService cwd;
		if(args.length > 1){
			throw new RuntimeException("USAGE: "+name()+" [path]");
		}else if(getMdShell().getCurrentToken() == null){
			throw new RuntimeException("You must be logged in.");
		}else if(args.length == 0){
			cwd = new ChangeDirectoryService(getMdShell().getCurrentToken(),"");
		}else{
			cwd = new ChangeDirectoryService(getMdShell().getCurrentToken(),args[0]);
		}
		cwd.execute();
		getMdShell().setCurrentPath(cwd.result());
		System.out.println(cwd.result());
	}
}
