package pt.tecnico.myDrive.domain;

import org.joda.time.DateTime;

public class Session extends Session_Base {

	// two hours in milisseconds
    private static final long TWOHOURS = 2 * 60 * 60 * 1000;
    
    public Session() {
        super();
        setTime(new DateTime());
    }

    public boolean isValid() {
    	DateTime temp = new DateTime();

    	long difference = temp.getMillis() - getTime().getMillis();
    	boolean res = difference < TWOHOURS;

    	return res;
    }


    public void refreshSession() {
    	setTime(new DateTime());
    }

    public void changeCurrentDir(Dir newDir) {
        setCurrentDir(newDir);
    }
    
}
