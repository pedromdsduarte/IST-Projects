package pt.tecnico.myDrive.exception;

public class InvalidTokenException extends MyDriveException {
	@Override
	public String getMessage() {
		return "Invalid token.";
	}
}

