package pt.tecnico.myDrive.exception;

public class ImportDocumentFromXMLException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Some problem occured while importing data from a XML document.";
	}
}
