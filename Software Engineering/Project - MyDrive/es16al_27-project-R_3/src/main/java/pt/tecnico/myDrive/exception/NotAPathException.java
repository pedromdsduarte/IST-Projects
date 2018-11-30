package pt.tecnico.myDrive.exception;

public class NotAPathException extends MyDriveException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String getMessage() {
		return "Not a valid path!";
	}
}

