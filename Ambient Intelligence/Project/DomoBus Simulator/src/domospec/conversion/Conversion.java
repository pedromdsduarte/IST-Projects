package domospec.conversion;

import domospec.valuetypes.ValueType;

public abstract class Conversion extends ValueType {

	private String userToSystem;
	private String systemToUser;
	private int decimalPlaces;
	
	public Conversion(int id, String name, String userToSystem, String systemToUser, int decimalPlaces) {
		super(id, name);
		this.userToSystem = userToSystem;
		this.systemToUser = systemToUser;
		this.decimalPlaces = decimalPlaces;
	}

	public int getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	public String getUserToSystem() {
		return userToSystem;
	}

	public void setUserToSystem(String userToSystem) {
		this.userToSystem = userToSystem;
	}

	public String getSystemToUser() {
		return systemToUser;
	}

	public void setSystemToUser(String systemToUser) {
		this.systemToUser = systemToUser;
	}
	
}
