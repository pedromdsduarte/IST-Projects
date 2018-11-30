package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.*;
import pt.tecnico.myDrive.exception.*;

public class ReadFileService extends MyDriveService {
	private String res;
	private long token;
	private String name;
	
	public ReadFileService(long _token, String _name) {
		token = _token;
		name = _name;
	}
	
	@Override
	protected void dispatch() throws MyDriveException {
		MyDrive md = getMyDrive();
		SessionManager sm = md.getSessionManager();
		Dir dir;
		User user;
		MyDriveFile file;
		
		if (!sm.isTokenValid(token)){
			log.warn("Token not valid!");
			throw new InvalidTokenException();
		}
		dir=sm.getCurrentDirByToken(token);
		if(dir == null)
			throw new DirectoryNotFoundException();
		user = sm.getUserByToken(token);
		if(user == null)
			throw new UsernameNotFoundException();
		try{
			file = getFile(dir,name);
			if (file instanceof Dir)
				throw new FileIsNotADirectoryException(name);
			if(!sm.hasReadPermission(token,file))
				throw new UserDoesNotHaveReadPermissionException();
		}catch(CannotFindFileException e){
			throw e;
		}
		
		res = ((PlainFile)file).getText();
		sm.refreshSession(token);
	}
	public final String result(){
		return res;
	}
}
