package pt.tecnico.myDrive.exception;

public abstract class MyDriveException extends RuntimeException {

	public MyDriveException() {
	}

	public MyDriveException(String msg) {
		super(msg);
	}
}