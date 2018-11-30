package pt.tecnico.myDrive.exception;

public class DirectoryNotFoundException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Given directory was not found.";
	}
}
