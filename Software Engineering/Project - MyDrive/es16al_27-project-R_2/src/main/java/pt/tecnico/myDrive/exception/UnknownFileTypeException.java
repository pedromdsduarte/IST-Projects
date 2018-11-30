package pt.tecnico.myDrive.exception;

public class UnknownFileTypeException extends MyDriveException {

	private String fileName;

	public UnknownFileTypeException(String s) {
		this.fileName = s;
	}

	public UnknownFileTypeException() {
		this.fileName = null;
	}

	@Override
	public String getMessage() {
	  if (fileName != null)
		  return "Argument " + this.fileName + " is an unsupported file type!";
	  else
		  return "The provived argument is an unsupported file type!";
	}
}
