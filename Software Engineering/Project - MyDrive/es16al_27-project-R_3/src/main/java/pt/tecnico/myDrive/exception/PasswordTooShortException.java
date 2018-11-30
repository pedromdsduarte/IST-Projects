package pt.tecnico.myDrive.exception;

public class PasswordTooShortException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Password too short. Minimum is 8 characters.";
	}
}
