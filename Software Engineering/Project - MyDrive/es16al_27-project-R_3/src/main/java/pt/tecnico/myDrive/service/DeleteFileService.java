package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveDeletePermissionException;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;

public class DeleteFileService extends MyDriveService {
	private long token;
	private String fileName;
	
	public DeleteFileService(long tkn, String filename) {
		token = tkn;
		fileName = filename;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		SessionManager sm = md.getSessionManager();
		MyDriveFile mdf;
		Dir currentDir;
		User user;
		
		
		
		if(!sm.isTokenValid(token))
			throw new InvalidTokenException();
		
		currentDir = sm.getCurrentDirByToken(token);
		if (currentDir == null)
			throw new DirectoryNotFoundException();
		
		user = sm.getUserByToken(token);
		if (user == null) 
			throw new UsernameNotFoundException();
		
		
		/*if (!(sm.hasReadPermission(token, currentDir)))
			throw new UserDoesNotHaveReadPermissionException();*/
			
		try {
			mdf = getFile(currentDir,fileName);
			
			/*System.out.println("I am " + user.getUsername() + " on " + currentDir.getName() + " ->\t" + user.getUmask() + 
								" and I want to delete " + mdf.getName() + " with fmask " + mdf.getFmask());*/
			
			if (!(sm.hasDeletePermission(token, mdf)))
				throw new UserDoesNotHaveDeletePermissionException();
				
			mdf.remove(); //Binding Dinâmico?
		} catch (CannotFindFileException e) {
			throw e;
		}

	}

}
