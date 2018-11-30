package domospec.devices;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import domospec.AbstractSpecEntity;
import domospec.Property;
import domospec.home.Division;
import domospec.valuetypes.Scalar;
import domospec.valuetypes.ValueType;

public class Device extends AbstractSpecEntity {
	
	private int address;
	private String name;
	private DeviceType deviceType;
	private Division division;
	
	private Map<String, TreeMap<Integer, Integer>> values;
	private Map<Integer, String> arrays;
	private Map<Integer, Boolean> invalid;

	public Device(int id, String name, int address, DeviceType deviceType) {
		super(id);
		this.name = name;
		this.address = address;
		this.deviceType = deviceType;
		initValues();
	}

	
	public Device(int id, String name, int address, DeviceType deviceType, Division division) {
		super(id);
		this.name = name;
		this.address = address;
		this.deviceType = deviceType;
		this.division = division;
		initValues();
	}
	
	private void initValues() {
		invalid = new TreeMap<Integer, Boolean>();
		values = new TreeMap<String, TreeMap<Integer, Integer>>();
		Set<String> types = new HashSet<String>();
		for (Property prop : deviceType.getProperties().values()) {
			types.add(prop.getValueType().getClass().getSimpleName());
		}

		for (String type : types) {
			TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
			values.put(type, map);
		}
		
		arrays = new TreeMap<Integer, String>();
		
	
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceType getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}
	
	public Division getDivision() {
		return division;
	}


	public void setDivision(Division division) {
		this.division = division;
	}
	
	public Property getProperty(int id) {
		return deviceType.getProperty(id);
	}
	
	public void setValue(int property, String type, int value) {
		TreeMap<Integer, Integer> value_map = values.get(type);
		value_map.put(property, value);
	}
	
	public int getValue(int property, String type) {
		TreeMap<Integer, Integer> value_map = values.get(type);
		int val = 0;
		if (value_map.get(property) == null) {
			if (type.equals("Scalar")) {
				Property prop = getProperty(property);
								
				ValueType valType = prop.getValueType();
				Scalar scalar = (Scalar)valType;
				
				val = scalar.getMinValue();
			}
			value_map.put(property, val);
		}
		return value_map.get(property);
	}
	
	public Map<Integer, Property> getProperties() {
		return deviceType.getProperties();
				
	}
	
	public void setValue(int property, String value) {
		arrays.put(property, value);
	}
	
	public String getValue(int property) {
		String val = arrays.get(property);
		if (val == null)
			return "";
		return val;
	}
	
	public boolean isInvalid(int property) {
		if (!invalid.containsKey(property))
			return false;
		return invalid.get(property);
	}
	
	public void setInvalid(int property, boolean value) {
		
		invalid.put(property, value);
		
		
	}
}
