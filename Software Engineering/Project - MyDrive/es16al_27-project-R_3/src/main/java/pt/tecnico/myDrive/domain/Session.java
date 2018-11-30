package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

import pt.tecnico.myDrive.exception.CannotFindEnvVarException;
import pt.tecnico.myDrive.exception.InvalidTokenException;

public class Session extends Session_Base {

	// two hours in milisseconds
    private static final long TWOHOURS = 2 * 60 * 60 * 1000;
    private static final long TENMINUTES = 10 * 60 * 1000;
    
    public Session() {
        super();
        setTime(new DateTime());
    }

    public boolean isValid() {
        // user nobody always have a valid session
        if (getUser().getUsername().equals("nobody")) return true;
    	boolean res = false;
        
        DateTime temp = new DateTime();
    	long difference = temp.getMillis() - getTime().getMillis();
        // root user has a 10 minutes limit in his session
    	if (!getUser().getUsername().equals("root"))
            res = difference < TWOHOURS;
        else
            res = difference < TENMINUTES;

    	return res;
    }


    public void refreshSession() {
    	setTime(new DateTime());
    }
    
    @Override
    public void addEnvVar(EnvVar newV) {
		for(EnvVar v : getEnvVarSet())
			if(v.getName().equals(newV.getName())) {
				v.setValue(newV.getValue());
				return;
			}
		super.addEnvVar(newV);
		
	} //todo

    public EnvVar getEnvVar(String name) throws CannotFindEnvVarException {
    	
    	for(EnvVar v : getEnvVarSet())
			if(v.getName().equals(name))
				return v;
    	
    	throw new CannotFindEnvVarException(name);
    }

    @Override
    public void setToken(long token) {
        // TODO protect from unlawful manipulation (is it done?)
        if (!getSessionManager().tokenAlreadyExist(token))
            super.setToken(token);
    }

    @Override
    public void setUser(User user) {
        // TODO protect from unlawful manipulation (is it done?)
        if (getUser() == null)
            super.setUser(user);
    } 

    @Override
    public void setCurrentDir(Dir currentDir) {
        // TODO protect from unlawful manipulation (is it necessary?)
        super.setCurrentDir(currentDir);
    }
    
    
    public boolean hasEnvVar(String name) {
    	for (EnvVar var : getEnvVarSet())
    		if (var.getName().equals(name))
    			return true;
    	return false;
    }
    
    public EnvVar findEnvVar(String name) {
    	for (EnvVar var: getEnvVarSet())
    		if (var.getName().equals(name))
    			return var;
    	return null;
    }
}
