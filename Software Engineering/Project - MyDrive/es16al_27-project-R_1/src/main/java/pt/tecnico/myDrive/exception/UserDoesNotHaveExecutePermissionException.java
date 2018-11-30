package pt.tecnico.myDrive.exception;

public class UserDoesNotHaveExecutePermissionException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The user does not have permissions to EXECUTE this file.";
	}
}
