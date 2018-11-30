package poof;

import java.io.Serializable;


public abstract class Entry implements Serializable {
	/**
	 * Holds the name of the entry
	*/
	private String _name;
	
	/**
	 * Holds the Owner of the entry
	*/
	private User _owner;
	
	/**
	* Holds the mode of the entry.
	* True if all users can change the entry, false otherwise.
	*/
	private boolean _public_mode;



	public Entry(String name,User owner, boolean publicMode){
		_name = name;
		_owner = owner;
		_public_mode = publicMode;
	}
	
	/**
	 * Sets the name of the entry
	 * @param name
	 * 			New name of the entry
	 */
	 
	public void setName(String name){
		_name = name;
	}
	
	/**
	 * Sets a new owner of the entry
	 * @param owner
	 * 			New user of the entry
	 */
	public void setOwner(User owner){
		_owner = owner;
	}
	
	/**
	 * Get the Name of the entry
	 * @return 
	 * 		return the name of the entry
	 */
	public String getName(){
		return _name;
	}
	
	/** 
	 * Get the Owner of the entry
	 * @return 
	 * 		return the Owner of the entry
	 */
	public User getOwner(){
		return _owner;
	}
	
	/**
	 * Predicate that verifies if the entry is Writtable
	 * @return
	 * 		True if entry is writtable, otherwise returns false*/
	public boolean getPublicMode(){
		return _public_mode;
	}
	
	/**
	 * Sets a new PublicMode of the entry
	 * @param publicMode
	 * 			True to writtable, otherwise false
	 */
	public void setPublicMode(boolean publicMode){
		_public_mode = publicMode;
	}
	
	/**
	 * Get the size of the entry
	 * @return 
	 * 		Size of the entry
	 */

	public int getSize() {
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Entry) {
			Entry e = (Entry)o;
			return _name.equals(e.getName()) && 
					_owner.equals(e.getOwner()) && 
					_public_mode == e.getPublicMode();
		}
		return false;
	}



}
