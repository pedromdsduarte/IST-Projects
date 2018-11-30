package domospec.valuetypes;

import java.util.HashMap;
import java.util.Map;

public class Enumerated extends ValueType {
	

	
	private Map<String, Integer> enums;
	

	public Enumerated(int id, String name) {
		super(id, name);
		enums = new HashMap<String, Integer>();
	}
	
	public void addValue(String designation, int value) {
		enums.put(designation, value);
	}
	
	public int getValue(String designation) {
		return enums.get(designation);
	}
	
	public Map<String, Integer> getEnums() {
		return enums;
	}


	
}
