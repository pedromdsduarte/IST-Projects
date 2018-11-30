package pt.tecnico.myDrive.presentation;

public abstract class MdCommand extends Command{
	private MdShell mdShell;
	public MdCommand(Shell sh, String n) { super(sh,n);}
	public MdCommand(MdShell sh, String n, String h){
		super(n,h);
		(mdShell = sh).add(this);
	}
	public MdCommand(Shell sh, String n, String h) { super(sh,n,h);}
	public MdShell getMdShell(){ return mdShell;}
	public void setShellToken(Long token) { mdShell.setToken(token);}
}
