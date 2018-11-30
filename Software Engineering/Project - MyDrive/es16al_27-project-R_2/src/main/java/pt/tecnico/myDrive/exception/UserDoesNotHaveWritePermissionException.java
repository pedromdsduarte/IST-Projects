package pt.tecnico.myDrive.exception;

public class UserDoesNotHaveWritePermissionException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The user does not have permissions to WRITE in this file.";
	}
}
