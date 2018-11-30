package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.domain.PlainFile;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.CannotFindFileException;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveWritePermissionException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.FileIsNotADirectoryException;

public class WriteFileService extends MyDriveService {
	
	private long token;
	private String filename;
	private String content;

	public WriteFileService(long token, String filename, String content) {
		this.token = token;
		this.filename = filename;
		this.content = content;
	}

	
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		SessionManager sm = md.getSessionManager();
		Dir dir;
		MyDriveFile file;
		User user;
		
		/* Assert zone */
		if (!sm.isTokenValid(token))
			throw new InvalidTokenException();
		dir = sm.getCurrentDirByToken(token);
		if (dir == null)
			throw new DirectoryNotFoundException();
		user = sm.getUserByToken(token);
		if (user == null) 
			throw new UsernameNotFoundException();
		try {
			file = getFile(dir,filename);
			if (file instanceof Dir)
				throw new FileIsNotADirectoryException(filename);
		} catch (CannotFindFileException e) {
			throw e;
		}

		if (!(sm.hasWritePermission(token, file)))
			throw new UserDoesNotHaveWritePermissionException();
		
		((PlainFile)file).setText(content);
		sm.refreshSession(token);


	}

}
