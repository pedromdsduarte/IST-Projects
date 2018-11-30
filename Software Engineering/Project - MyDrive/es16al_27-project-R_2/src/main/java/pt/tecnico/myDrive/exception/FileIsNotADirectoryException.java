package pt.tecnico.myDrive.exception;

public class FileIsNotADirectoryException extends MyDriveException {

	private String fileName = null;


	public FileIsNotADirectoryException(String s) {
		this.fileName = s;
	}

	public FileIsNotADirectoryException() {

	}

	@Override
	public String getMessage() {
	  if (fileName != null)
	  	return "The file " + this.fileName + " is not a directory.";
	  return "Given file is not a directory.";
	}
}