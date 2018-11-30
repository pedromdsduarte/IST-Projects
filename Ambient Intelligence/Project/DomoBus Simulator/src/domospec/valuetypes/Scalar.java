package domospec.valuetypes;

import domospec.conversion.Conversion;

public class Scalar extends ValueType {

	private int minValue;
	private int maxValue;
	private String units;
	private int step;
	private int numBits;
	
	private Conversion valueConversion;
	
	public Scalar(int id, String name, int minValue, int maxValue, String units, int step, int numBits) {
		super(id, name);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.units = units;
		this.step = step;
		this.numBits = numBits;
	}
	
	public Scalar(int id, String name, int minValue, int maxValue, 
					String units, int step, int numBits, Conversion valueConversion) {
		super(id, name);
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.units = units;
		this.step = step;
		this.numBits = numBits;
		this.setValueConversion(valueConversion);
	}
	
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getNumBits() {
		return numBits;
	}
	public void setNumBits(int numBits) {
		this.numBits = numBits;
	}

	public Conversion getValueConversion() {
		return valueConversion;
	}

	public void setValueConversion(Conversion valueConversion) {
		this.valueConversion = valueConversion;
	}
	
	
	
}
