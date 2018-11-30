package pt.tecnico.myDrive.exception;

public class CannotFindEnvVarException extends MyDriveException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name = null;

	public CannotFindEnvVarException(String s) {
		
		this.name = s;
	}

	@Override
	public String getMessage() {
		
		if (name != null)
			return "There is no environment variable named " + name;
		else
			return "There is no environment variable with the provided name";
	}
}
