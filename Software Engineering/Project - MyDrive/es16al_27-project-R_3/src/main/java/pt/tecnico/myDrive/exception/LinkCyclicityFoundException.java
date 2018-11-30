package pt.tecnico.myDrive.exception;

public class LinkCyclicityFoundException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Link cannot be created with that content because it creates a loop.";
	}
}
