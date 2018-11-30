package pt.tecnico.myDrive.exception;

public class InvalidUsernameException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Username provided is invalid.";
	}
}
