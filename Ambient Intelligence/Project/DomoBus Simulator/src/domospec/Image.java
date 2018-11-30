package domospec;

public class Image extends AbstractSpecEntity {

	private Property property;
	private String valueRange;
	private String path;
	
	public Image(int id, String path) {
		super(id);
		this.path = path;
	}
	
	public Image(int id, Property property, String valueRange, String path) {
		super(id);
		this.valueRange = valueRange;
		this.path = path;
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}

	public void setProperty(Property property) {
		this.property = property;
	}

	public String getValueRange() {
		return valueRange;
	}

	public void setValueRange(String valueRange) {
		this.valueRange = valueRange;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	
	
	
}
