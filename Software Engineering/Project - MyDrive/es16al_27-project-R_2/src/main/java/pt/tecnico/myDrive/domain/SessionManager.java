package pt.tecnico.myDrive.domain;

import pt.tecnico.myDrive.exception.InvalidUserPasswordException;
import pt.tecnico.myDrive.exception.InvalidTokenException;

import java.math.BigInteger;
import java.util.Random;


public class SessionManager extends SessionManager_Base {
    
    public SessionManager() {
        super();
    }

    public long login(String username, String password) {

    	MyDrive md = MyDrive.getInstance();
    	User u = md.getUserByUsername(username);
    	long res = -1;

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
    private boolean tokenAlreadyExist(long token) {
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
	

	public void setCurrentDirByToken(long token, Dir newDir) {
        boolean res = false;
        for (Session s : getSessionSet())
            if (s.getToken() == token && s.isValid()) {
                s.changeCurrentDir(newDir);
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
        User userByToken = getUserByToken(token);

        User userByFile = mdf.getOwner();
        char[] fileMask = mdf.getFmask().toCharArray();

        if (userByToken.equals(userByFile)) {
            // dono do ficheiro
            if (fileMask[0] == 'r') 
                return true;
            else 
                return false;
        } else {
            // NAO e dono do ficheiro
            if (fileMask[4] == 'r')
                return true;
            else
                return false;
        } 
    }
    public boolean hasWritePermission(long token, MyDriveFile mdf) {
        User userByToken = getUserByToken(token);

        User userByFile = mdf.getOwner();
        char[] fileMask = mdf.getFmask().toCharArray();

        if (userByToken.equals(userByFile)) {
            // dono do ficheiro
            if (fileMask[1] == 'w') 
                return true;
            else 
                return false;
        } else {
            // NAO e dono do ficheiro
            if (fileMask[5] == 'w')
                return true;
            else
                return false;
        } 
    }
    public boolean hasExecutePermission(long token, MyDriveFile mdf) {
        User userByToken = getUserByToken(token);

        User userByFile = mdf.getOwner();
        char[] fileMask = mdf.getFmask().toCharArray();

        if (userByToken.equals(userByFile)) {
            // dono do ficheiro
            if (fileMask[2] == 'x') 
                return true;
            else 
                return false;
        } else {
            // NAO e dono do ficheiro
            if (fileMask[6] == 'x')
                return true;
            else
                return false;
        } 
    }
    public boolean hasDeletePermission(long token, MyDriveFile mdf) {
        User userByToken = getUserByToken(token);

        User userByFile = mdf.getOwner();
        char[] fileMask = mdf.getFmask().toCharArray();

        if (userByToken.equals(userByFile)) {
            // dono do ficheiro
            if (fileMask[3] == 'd') 
                return true;
            else 
                return false;
        } else {
            // NAO e dono do ficheiro
            if (fileMask[7] == 'd')
                return true;
            else
                return false;
        }
    }



}
