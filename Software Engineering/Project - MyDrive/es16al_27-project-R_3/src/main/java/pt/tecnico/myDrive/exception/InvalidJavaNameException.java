package pt.tecnico.myDrive.exception;

public class InvalidJavaNameException extends MyDriveException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String getMessage() {
		return "Invalid Java name!";
	}
}

