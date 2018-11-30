package domospec.home;

import java.util.Map;
import java.util.TreeMap;

import domospec.AbstractSpecEntity;

public class House extends AbstractSpecEntity {
	
	private String name;
	private String address;
	private String phone;

	private Map<Integer, Floor> floors;
	private Map<Integer, Division> divisions;
	
	public House(int id, String name, String address, String phone) {
		super(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		floors = new TreeMap<Integer, Floor>();
		divisions = new TreeMap<Integer, Division>();
	}
	
	public void setDivisions(Map<Integer, Division> divisions) {
		this.divisions = divisions;
	}

	public void setFloors(Map<Integer, Floor> floors) {
		this.floors = floors;
	}

	public House(int id, String name, String address) {
		super(id);
		this.name = name;
		this.address = address;
		floors = new TreeMap<Integer, Floor>();
		divisions = new TreeMap<Integer, Division>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public void addFloor(Floor floor) {
		floors.put(floor.getId(), floor);
	}
	
	public void addDivision(Division div) {
		divisions.put(div.getId(), div);
	}
	
	public Map<Integer, Floor> getFloors() {
		return floors;
	}
	
	public Map<Integer, Division> getDivisions() {
		return divisions;
	}
	
	
	public String listFloors() {
		String res = "";
		
		for (Floor floor : floors.values()) {
			res += floor.getName() + ", ";
		}
		return res;
	}
	
	public String listDivisions() {
		String res = "";
		
		for (Division div : divisions.values()) {
			res += div.getName() + ", ";
		}
		return res;
	}
	
	public Floor getFloor(int id) {
		return floors.get(id);
	}
	
	public Floor findFloorByName(String name) {
		for (Floor floor : floors.values()) 
			if (floor.getName().equals(name))
				return floor;
		return null;
	}
	
	public Division findDivisionByName(String name) {
		for (Division div: divisions.values()) 
			if (div.getName().equals(name))
				return div;
		return null;
	}
	
}
