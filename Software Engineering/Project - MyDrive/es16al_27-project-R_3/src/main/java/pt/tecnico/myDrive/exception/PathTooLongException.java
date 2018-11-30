package pt.tecnico.myDrive.exception;

public class PathTooLongException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Path is too long. Maximum is 1024 characters.";
	}
}
