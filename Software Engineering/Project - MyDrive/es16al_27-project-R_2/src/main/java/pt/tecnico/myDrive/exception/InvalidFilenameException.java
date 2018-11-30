package pt.tecnico.myDrive.exception;

public class InvalidFilenameException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The filename contains invalid characters.";
	}
}
