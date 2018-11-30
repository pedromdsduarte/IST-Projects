package pt.tecnico.myDrive.exception;

public class UsernameAlreadyExistsException extends MyDriveException {

	private String username = null;

	public UsernameAlreadyExistsException(String s) {
		this.username = s;
	}
	public UsernameAlreadyExistsException() {
		
	}

	@Override
	public String getMessage() {
		if (username != null && !username.equals("")) return "There is already someone registered the following username: " + this.username + ".";
		return "There is already someone registered with that username.";
	}
}
