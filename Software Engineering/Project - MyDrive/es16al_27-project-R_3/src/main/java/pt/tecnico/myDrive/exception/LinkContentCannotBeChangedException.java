package pt.tecnico.myDrive.exception;

public class LinkContentCannotBeChangedException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMessage() {
		return "Link content cannot be changed after creation!";
	}
}
