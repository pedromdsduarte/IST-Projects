package domospec.valuetypes;

import domospec.AbstractSpecEntity;

public abstract class ValueType extends AbstractSpecEntity {
	
	private String name;
	private int value = 0;
	
	public ValueType(int id, String name) {
		super(id);
		this.name = name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		
		if (!ValueType.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
		ValueType other = (ValueType)obj;
		return this.getName().equals(other.getName()) && 
				this.getClass().getSimpleName().equals(other.getClass().getSimpleName());
	}
	
}
