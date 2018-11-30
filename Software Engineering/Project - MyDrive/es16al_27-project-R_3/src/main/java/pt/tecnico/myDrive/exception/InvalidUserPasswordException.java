package pt.tecnico.myDrive.exception;

public class InvalidUserPasswordException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Password mismatch.";
	}

}
