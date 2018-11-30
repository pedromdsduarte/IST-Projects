package pt.tecnico.myDrive.presentation;
import pt.tecnico.myDrive.service.WriteFileService;
import pt.ist.fenixframework.Atomic;

public class Write extends MdCommand {
	public Write(Shell sh) {
		super((MdShell)sh, "update", "change the content of the file indicated by <path>");
	}

	@Atomic
	@Override
	public void execute(String[] args) {
		String path = "", content = "", filename = "";
		if (args.length < 1)
			throw new RuntimeException("USAGE: "+name()+" <path> [<text>]");
		else if (args.length == 1) {
			// verify if args[0] is really a path
			path = args[0];
			filename = path.substring(path.lastIndexOf('/')+1, path.length());
		} else if (args.length == 2) {
			// verify if args[0] is really a path
			path = args[0];
			filename = path.substring(path.lastIndexOf('/')+1, path.length());
			content = args[1];
		} else if (args.length > 2) {
			path = args[0];
			filename = path.substring(path.lastIndexOf('/')+1, path.length());
			for (int i=1; i<args.length; i++)
				content += args[i] + " ";
			content = content.substring(0, content.length()-1);
		}

		new WriteFileService(getMdShell().getCurrentToken(), filename, content).execute();
	}
}
