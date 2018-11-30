package pt.tecnico.myDrive.exception;

public class InvalidAppException extends MyDriveException {
	@Override
	public String getMessage() {
		return "This is not a well formed java class name or it was not found.";
	}
}
