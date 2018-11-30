package domospec.valuetypes;

import domospec.conversion.Conversion;

public class Array extends ValueType {

	private int maxLength;
	private Conversion valueConversion;
	
	public Array(int id, String name, int maxLength) {
		super(id, name);
		this.maxLength = maxLength;
	}
	
	public Array(int id, String name, int maxLength, Conversion valueConversion) {
		super(id, name);
		this.maxLength = maxLength;
		this.setValueConversion(valueConversion);
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public Conversion getValueConversion() {
		return valueConversion;
	}

	public void setValueConversion(Conversion valueConversion) {
		this.valueConversion = valueConversion;
	}
	
	
	
}
