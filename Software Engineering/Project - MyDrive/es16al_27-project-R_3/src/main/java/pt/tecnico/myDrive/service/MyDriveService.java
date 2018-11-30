package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.MyDriveException;


import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jvstm.Atomic;

public abstract class MyDriveService {
	
    protected static final Logger log = LogManager.getRootLogger();


	@Atomic
	public void execute() throws MyDriveException {
		dispatch();
	}
	
	 static MyDrive getMyDrive() {
		 return MyDrive.getInstance();
	 }

	 
	 static MyDriveFile getFile(Dir dir, String filename) throws CannotFindFileException {
		 MyDriveFile file = null;
		 Set<MyDriveFile> files;
		 try {
			 files = dir.getFileSet();
			 file = getMyDrive().getFileByName(files,filename);
			 return file;
		 } catch (Exception e) {
			 throw new CannotFindFileException(filename);
		 }
	 }


	protected abstract void dispatch() throws MyDriveException;

}