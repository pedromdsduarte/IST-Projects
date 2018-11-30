package domospec.home;


import java.util.Map;
import java.util.TreeMap;

import domospec.AbstractSpecEntity;

public class Floor extends AbstractSpecEntity {

	private String name;
	private int heightOrder;
	private House house;
	private Map<Integer, Division> divisions;	
	
	public Floor(int id, String name, int heightOrder, House house) {
		super(id);
		this.name = name;
		this.heightOrder = heightOrder;
		this.house = house;
		divisions = new TreeMap<Integer, Division>();
	}
	
	public Floor(int id, String name, int heightOrder) {
		super(id);
		this.name = name;
		this.heightOrder = heightOrder;
		divisions = new TreeMap<Integer, Division>();
	}
	
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getHeightOrder() {
		return heightOrder;
	}
	public void setHeightOrder(int heightOrder) {
		this.heightOrder = heightOrder;
	}

	
	public void addDivision(Division division) {
		divisions.put(division.getId(), division);
	}
	
	public Map<Integer, Division> getDivisions() {
		return divisions;
	}
	
	public Division getDivision(int id) {
		return divisions.get(id);
	}
	
}
