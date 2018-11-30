package pt.tecnico.myDrive.exception;

public class FileIsADirectoryException extends MyDriveException {

	private String fileName = null;


	public FileIsADirectoryException(String s) {
		this.fileName = s;
	}

	public FileIsADirectoryException() {

	}

	@Override
	public String getMessage() {
	  if (fileName != null)
	  	return "The file " + this.fileName + " is a directory.";
	  return "Given file is a directory.";
	}
	
}

