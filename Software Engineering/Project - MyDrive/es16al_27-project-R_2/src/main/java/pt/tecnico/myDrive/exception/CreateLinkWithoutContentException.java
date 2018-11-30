package pt.tecnico.myDrive.exception;

public class CreateLinkWithoutContentException extends MyDriveException {

	private String fileName;

	public CreateLinkWithoutContentException(String s) {
		this.fileName = s;
	}

	public CreateLinkWithoutContentException() {
		this.fileName = null;
	}

	@Override
	public String getMessage() {
	  if (fileName != null)
		  return "File " + this.fileName + " of type Link must be created with a content!";
	  else
		  return "Files of type Link must be created with a content!";
	}
}
