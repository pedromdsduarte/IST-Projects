package pt.tecnico.myDrive.exception;

public class CreateDirWithContentException extends MyDriveException {

	private String fileName;

	public CreateDirWithContentException(String s) {
		this.fileName = s;
	}

	public CreateDirWithContentException() {
		this.fileName = null;
	}

	@Override
	public String getMessage() {
	  if (fileName != null)
		  return "Directory " + this.fileName + " cannot be created with a content!";
	  else
		  return "Directories cannot be created with a content!";
	}
}
