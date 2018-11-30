package domospec.devices;

import domospec.AbstractSpecEntity;

public class DeviceClass extends AbstractSpecEntity {

	private String name;

	public DeviceClass(int id, String name) {
		super(id);
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
