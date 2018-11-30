package pt.tecnico.myDrive.exception;

public class DuplicateExtensionException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public String getMessage() {
		return "There is already an association with the provided extension for this user!";
	}
}
