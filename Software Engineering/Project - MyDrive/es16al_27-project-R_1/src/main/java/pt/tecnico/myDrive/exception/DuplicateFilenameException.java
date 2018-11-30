package pt.tecnico.myDrive.exception;

public class DuplicateFilenameException extends MyDriveException {

	@Override
	public String getMessage() {
		return "There is already a file with the same name in this directory.";
	}
}
