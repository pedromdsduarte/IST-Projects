package poof;

import java.io.Serializable;

public class User implements Serializable {
	
	/**
	* Holds the username of the user
	*/
	private String _username = "";
	
	/**
	* Holds the name of the user
	*/
	private String _name = "";
	
	/**
	* Holds the home directory of the user
	*/
	private Directory _homedir = null;

	public User(String username, String name) {
		_username = username;
		_name = name;
	}

	/**
	* Gets username of the user
	*/
	public String getUsername() {
		return _username;
	}
	
	/**
	* Gets name of the user
	*/
	public String getName() {
		return _name;
	}
	
	/**
	* Gets the home directory of the user
	*/
	public Directory getHomedir() {
		return _homedir;
	}

	/**
	* Sets the home directory of the user to a new one.
	* 
	* @param homedir
	* 			new home directory to be assigned to the user.
	*/
	public void setHomedir(Directory homedir) {
		_homedir = homedir;
	}
	
	/**
	* Verifies if the user is the same as other one
	* 
	* @param o
	* 			other user to compare to
	* 
	* @return
	* 			true if the user equals the other, false otherwise.
	*/
	@Override
	public boolean equals(Object o) {
		if(o instanceof User) {
			User u = (User)o;
			return _username.equals(u.getUsername())
					&& _name.equals(u.getName()) 
					&& _homedir == u.getHomedir();
		}
		return false;
	}
}
