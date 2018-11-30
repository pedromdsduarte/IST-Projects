package pt.tecnico.myDrive.exception;

public class DuplicateDirectoryException extends MyDriveException {

	@Override
	public String getMessage() {
		return "There is already a directory with that name in the given path.";
	}
}
