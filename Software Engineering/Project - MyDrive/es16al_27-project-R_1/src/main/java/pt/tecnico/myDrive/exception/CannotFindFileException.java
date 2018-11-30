package pt.tecnico.myDrive.exception;

public class CannotFindFileException extends MyDriveException {

	//
	private String filename = null;

	public CannotFindFileException(String s) {
		this.filename = s;
		// podemos adicionar tambem a directoria para dar mais informacao ao utilizador
	}

	@Override
	public String getMessage() {
		if (filename != null) return "There is no file named " + filename + "in the given directory";
		return "There is no file with that name in the given directory.";
	}
}
