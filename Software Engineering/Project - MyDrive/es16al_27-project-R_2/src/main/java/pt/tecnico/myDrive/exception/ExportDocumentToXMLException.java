package pt.tecnico.myDrive.exception;

public class ExportDocumentToXMLException extends MyDriveException {

	@Override
	public String getMessage() {
		return "Some problem occured while exporting data to a XML document.";
	}
}
