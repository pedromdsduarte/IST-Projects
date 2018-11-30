package domospec.devices;


import java.util.Map;
import java.util.TreeMap;


import domospec.AbstractSpecEntity;
import domospec.Image;
import domospec.Property;

public class DeviceType extends AbstractSpecEntity {

	
	private String name;
	private DeviceClass deviceClass;
	private String description;
	private Map<Integer, Property> properties;
	
	private Image defaultImg;
	//private Map<Integer, Image> images;
	private Map<Integer, TreeMap<Integer, Image>> images;
	
	public DeviceType(int id, String name, String description) {
		super(id);
		this.name = name;
		this.description = description;
		properties = new TreeMap<Integer, Property>();
		initImages();
		
	}

	private void initImages() {
		images = new TreeMap<Integer, TreeMap<Integer, Image>>();
	}
	
	public Map<Integer, Property> getProperties() {
		return properties;
	}

	public void setProperties(Map<Integer, Property> properties) {
		this.properties = properties;
	}

	public DeviceType(int id, String name, DeviceClass deviceClass, String description) {
		super(id);
		this.name = name;
		this.deviceClass = deviceClass;
		this.description = description;
		properties = new TreeMap<Integer, Property>();
		initImages();
	}
	
	public Image getDefaultImg() {
		return defaultImg;
	}

	public void setDefaultImg(Image defaultImg) {
		this.defaultImg = defaultImg;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public void setDeviceClass(DeviceClass deviceClass) {
		this.deviceClass = deviceClass;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Property getProperty(int id) {
			
		return properties.get(id);
	}
	
	public void addProperty(Property property) {
		properties.put(property.getId(), property);
	}
	
	public void addImage(int property, Image image) {
		
		TreeMap<Integer, Image> prop_imgs = null;
		if (images.containsKey(property)) {
			prop_imgs = images.get(property);
		} else {
			prop_imgs = new TreeMap<Integer, Image>();
			images.put(property, prop_imgs);
		}
		prop_imgs.put(image.getId(), image);
		
	}
	
	public Image getImage(int property, int value) {
		
		TreeMap<Integer, Image> prop_imgs = images.get(property);
		
		if (prop_imgs == null)
			return null;
		
		for (Image image : prop_imgs.values()) {
			String range = image.getValueRange();
			String[] tokens = range.split("-");
			int min = Integer.parseInt(tokens[0]);
			int max = Integer.parseInt(tokens[1]); 
			
			System.out.println("Trying image " + image.getPath());
			System.out.println("\tValue: " + value + " - (min,max) = (" + min + ","+max+")");
			
			if (value >= min && value <= max) {
				return image;
			}
			
		}
		
		return null;
	}
	

	public Image getImage(int value) {
		/*
				
		for (Image image : images.values()) {
			String range = image.getValueRange();
			int min = Integer.parseInt(range.substring(0, 1));
			int max = Integer.parseInt(range.substring(2, 3)); 
						
			if (value >= min && value <= max) {
				return image;
			}
			
		}*/
		
		//System.out.println("## Value is " + value);
		
		for (TreeMap<Integer, Image> prop_imgs : images.values()) {
			for (Image image : prop_imgs.values()) {
				String range = image.getValueRange();
				String[] tokens = range.split("-");
				int min = Integer.parseInt(tokens[0]);
				int max = Integer.parseInt(tokens[1]); 
				
				//System.out.println("Trying range " + min + "," + max);
				
				if (value >= min && value <= max) {
					return image;
				}
				
			}
		}
		return null;
	}
	
}
