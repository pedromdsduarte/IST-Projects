package domospec;

import domospec.valuetypes.ValueType;

public class Property extends AbstractSpecEntity {

	private String name;
	private String accessMode;	//RW, RO, WO
	private ValueType valueType;	//SCALAR, ENUM or ARRAY
	
	private Image image;
	
	private String opmode;

	public Property(int id, String name, String accessMode, ValueType valueType) {
		super(id);
		this.name = name;
		this.accessMode = accessMode;
		this.valueType = valueType;
	}
	
	public Property(int id, String name, String accessMode, ValueType valueType, Image image) {
		super(id);
		this.name = name;
		this.accessMode = accessMode;
		this.valueType = valueType;
		this.image = image;
	}
	

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(String accessMode) {
		this.accessMode = accessMode;
	}

	public ValueType getValueType() {
		return valueType;
	}

	public void setValueType(ValueType valueType) {
		this.valueType = valueType;
	}

	public String getOpmode() {
		return opmode;
	}

	public void setOpmode(String opmode) {
		this.opmode = opmode;
	}
	
}
