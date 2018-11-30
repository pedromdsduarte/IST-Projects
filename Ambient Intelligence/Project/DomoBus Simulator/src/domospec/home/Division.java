package domospec.home;

import java.util.Map;
import java.util.TreeMap;

import domospec.AbstractSpecEntity;
import domospec.devices.Device;

public class Division extends AbstractSpecEntity {

	private String name;
	private int accessLevel;
	private Floor floor;
	private Map<Integer, Device> devices;
	
	public Division(int id, String name, int accessLevel, Floor floor) {
		super(id);
		this.name = name;
		this.accessLevel = accessLevel;
		this.floor = floor;
		this.devices = new TreeMap<Integer, Device>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAccessLevel() {
		return accessLevel;
	}
	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}
	public Floor getFloor() {
		return floor;
	}
	public void setFloor(Floor floor) {
		this.floor = floor;
	}

	public Map<Integer, Device> getDevices() {
		return devices;
	}

	public void setDevices(Map<Integer, Device> devices) {
		this.devices = devices;
	}
	
	
	public void addDevice(Device device) {
		devices.put(device.getId(), device);
	}
	
	
}
