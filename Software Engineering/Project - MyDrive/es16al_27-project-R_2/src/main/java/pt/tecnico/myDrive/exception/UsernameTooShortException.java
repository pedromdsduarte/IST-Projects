package pt.tecnico.myDrive.exception;

public class UsernameTooShortException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Username too short. Minimum is 3 characters.";
	}
}
