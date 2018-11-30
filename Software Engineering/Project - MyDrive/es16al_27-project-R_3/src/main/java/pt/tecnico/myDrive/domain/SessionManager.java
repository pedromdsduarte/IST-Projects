package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidTokenException;
import pt.tecnico.myDrive.exception.UserDoesNotHaveReadPermissionException;
import pt.tecnico.myDrive.exception.InvalidUsernameException;
import pt.tecnico.myDrive.exception.UsernameTooShortException;
import pt.tecnico.myDrive.exception.UsernameNotFoundException;
import pt.tecnico.myDrive.exception.InvalidUserPasswordException;
import pt.tecnico.myDrive.exception.PasswordTooShortException;

import java.math.BigInteger;
import java.util.Random;


public class SessionManager extends SessionManager_Base {
    
    public SessionManager() {
        super();
    }

    public long login(String username, String password) {
        if (username == null) throw new InvalidUsernameException();
        if (username.length() < 3) throw new UsernameTooShortException();
        
        MyDrive md = MyDrive.getInstance();
        if (!md.hasUser(username)) throw new UsernameNotFoundException();

    	User u = md.getUserByUsername(username);
    	long res = -1;
		
		if(u.getPassword().length()<8 && !(u.getUsername().equals("root")) && !(u.getUsername().equals("nobody"))){
			throw new PasswordTooShortException();
		}
		
    	if (u.getPassword().equals(password)) {
    		// password valida

    		res = new BigInteger(64, new Random()).longValue();

    		// unique token
    		while (tokenAlreadyExist(res)) {
    			res = new BigInteger(64, new Random()).longValue();
    		}

    		if (res < 0) res *= -1;
    		Session s = new Session();

    		// SessionManagerHasSessions relation
    		addSession(s);

    		s.setToken(res);

    		// SessionsHaveUsers relation
    		s.setUser(u);

    		// SessionsHaveCurrentDirectory relation
    		// it starts at "/home/USERNAME"
    		s.setCurrentDir(u.getHomeDir());

    		removeInvalidSessions();

    	} else {
    		throw new InvalidUserPasswordException();

    	}

    	return res;

    }


    // Auxiliary functions
    private void removeInvalidSessions() {
    	for (Session s : getSessionSet())
    		if (!s.isValid())
    			removeSession(s);
    }
    protected boolean tokenAlreadyExist(long token) {
    	for (Session s : getSessionSet())
    		if (s.getToken() == token)
    			return true;
    	return false;
    }
    // end Auxiliary functions


    public boolean isTokenValid(long token) {
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid())
                return true;
        return false;
    }


    // SessionsHaveCurrentDirectory relation
    public Dir getCurrentDirByToken(long token) {
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid())
                return s.getCurrentDir();
        throw new InvalidTokenException();
    }
    
    // Get a Session given a token
    public Session getSessionByToken(long token) {
		for (Session s : getSessionSet())
			if(s.getToken() == token && s.isValid())
				return s;
		throw new InvalidTokenException();
	}
	

	public void setCurrentDirByToken(long token, Dir newDir) {
        boolean res = false;
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid()) {
                s.setCurrentDir(newDir);
                res = true;
                break;
            }
        if (!res) throw new InvalidTokenException();
    }

    // SessionsHaveUsers relation
    public User getUserByToken(long token) {
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid())
                return s.getUser();
        throw new InvalidTokenException();
    }


    public void refreshSession(long token) {
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid())
                s.refreshSession();
    }


    public boolean hasReadPermission(long token, MyDriveFile mdf) {
        if (!isTokenValid(token)) throw new InvalidTokenException();
        return mdf.hasReadPermission(getUserByToken(token));
    }

    public boolean hasWritePermission(long token, MyDriveFile mdf) {
        if (!isTokenValid(token)) throw new InvalidTokenException();
        return mdf.hasWritePermission(getUserByToken(token));
    }

    public boolean hasExecutePermission(long token, MyDriveFile mdf) {
        if (!isTokenValid(token)) throw new InvalidTokenException();
        return mdf.hasExecutePermission(getUserByToken(token));
    }

    public boolean hasDeletePermission(long token, MyDriveFile mdf) {
        if (!isTokenValid(token)) throw new InvalidTokenException();
        return mdf.hasDeletePermission(getUserByToken(token));
    }

	public long getTokenByUsername(String username){
		MyDrive md = MyDrive.getInstance();
		for (User u : md.getUserSet()){
            if (u.getUsername().equals(username)){
				for (Session s : u.getSessionSet())
					if ((s.getUser().getUsername()).equals(username) && s.isValid())
						return s.getToken();
				throw new InvalidTokenException();
			}
		}
        throw new UsernameNotFoundException();
	}

}
