package pt.tecnico.myDrive.exception;

public class UsernameNotFoundException extends MyDriveException {

	@Override
	public String getMessage() {
		return "The username provided was not found on our database.";
	}
}
