package pt.tecnico.myDrive.exception;

public class UserDoesNotHaveDeletePermissionException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The user does not have permissions to DELETE this file.";
	}
}
