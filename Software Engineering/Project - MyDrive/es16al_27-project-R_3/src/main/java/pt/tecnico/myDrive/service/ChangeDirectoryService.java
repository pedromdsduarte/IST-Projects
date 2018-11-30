package pt.tecnico.myDrive.service;
import pt.tecnico.myDrive.domain.Dir;
import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;
import pt.tecnico.myDrive.domain.User;
import pt.tecnico.myDrive.domain.MyDriveFile;
import pt.tecnico.myDrive.exception.DirectoryNotFoundException;
import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.MyDriveException;
import pt.tecnico.myDrive.exception.FileIsNotADirectoryException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;
public class ChangeDirectoryService extends MyDriveService {
	private long token;
	private String path;
	private String res;
	
	public ChangeDirectoryService(long token, String path) {
		this.token = token;
		this.path = path;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		SessionManager sm = md.getSessionManager();
		Dir curDir;
		MyDriveFile newFile;
		User user;
		
		if (!sm.isTokenValid(token))
			throw new InvalidTokenException();
		
		else
			sm.refreshSession(token);
		
		curDir = sm.getCurrentDirByToken(token);
		if (curDir == null|| path == null)
			throw new DirectoryNotFoundException();
		
		if (!(sm.hasReadPermission(token, curDir)))
			throw new UserDoesNotHaveReadPermissionException();
		
		user = sm.getUserByToken(token);
		
		if (user == null) 
			throw new UsernameNotFoundException();
		
		
		try {
			if(path.equals(""))
				md.changeCurrentDirectory(token, md.getPath(user.getHomeDir()));
			else
				md.changeCurrentDirectory(token, path);
		} catch (FileIsNotADirectoryException e) {
			throw e; 
		}

		
		//TODO: check write permissions
		curDir = sm.getCurrentDirByToken(token);
		

		
		res = md.getPath(curDir);
		

	}
	
	public String result(){
		return res;
	}
}
