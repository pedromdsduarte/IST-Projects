package pt.tecnico.myDrive.service;

import pt.tecnico.myDrive.domain.MyDrive;
import pt.tecnico.myDrive.domain.SessionManager;

import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UsernameTooShortException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;


public class LoginUserService extends MyDriveService {

	private String username, password;
	private long token;

	public LoginUserService(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	protected void dispatch() {

		MyDrive md = MyDrive.getInstance();

		if (this.username == null ) throw new InvalidUsernameException();
    	if (this.username.length() < 4) throw new UsernameTooShortException();
    	if (!md.hasUser(username)) throw new UsernameNotFoundException();

		SessionManager sm = md.getSessionManager();
		this.token = sm.login(this.username, this.password);

	}


	public long getToken() {
		return this.token;
	}

	public String getUsername() {
		return this.username;
	}

		
}