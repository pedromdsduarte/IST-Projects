package pt.tecnico.myDrive.exception;

public class UserDoesNotHaveReadPermissionException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The user does not have permissions to READ this file.";
	}
}
