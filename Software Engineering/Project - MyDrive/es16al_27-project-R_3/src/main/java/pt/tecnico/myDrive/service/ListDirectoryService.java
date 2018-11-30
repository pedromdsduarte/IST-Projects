package pt.tecnico.myDrive.service;
import pt.tecnico.myDrive.service.dto.ListDirectoriesDto;
import pt.tecnico.myDrive.service.dto.FileDto;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.PathTooLongException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;

public class ListDirectoryService extends MyDriveService {
	private long token;
	private ListDirectoriesDto content;
	
	public ListDirectoryService(long token) {
		this.token = token;
	}

	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		SessionManager sm = md.getSessionManager();
		Dir curDir;
		String path;
		User user;
		
		if (!sm.isTokenValid(token))
			throw new InvalidTokenException();
		
		else
			sm.refreshSession(token);
		
		curDir = sm.getCurrentDirByToken(token);
		
		if (curDir == null)
			throw new DirectoryNotFoundException();
		
		user = sm.getUserByToken(token);
		
		if (user == null) 
			throw new UsernameNotFoundException();
		
		if (!(sm.hasReadPermission(token, curDir)))
			throw new UserDoesNotHaveReadPermissionException();
		
		
		try {
			path = md.getPath(curDir);
			if (path.length() <= 1024){
				this.content = md.listDirectories(path);
			}
			else
				throw new PathTooLongException();
		} catch (PathTooLongException e) {
			throw e;
		}

	}
		//TODO: check write permissions
				
		public ListDirectoriesDto result(){
			return content;
	}

}
