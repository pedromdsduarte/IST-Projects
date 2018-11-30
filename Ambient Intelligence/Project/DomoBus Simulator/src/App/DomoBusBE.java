package App;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import DComm.DCommAPI;
import domospec.AbstractSpecEntity;
import domospec.Image;
import domospec.Property;
import domospec.devices.Device;
import domospec.devices.DeviceClass;
import domospec.devices.DeviceType;

import domospec.home.Division;
import domospec.home.Floor;
import domospec.home.House;
import domospec.valuetypes.Array;
import domospec.valuetypes.Enumerated;
import domospec.valuetypes.Scalar;
import domospec.valuetypes.ValueType;


public class DomoBusBE {
	
	private DomoBusSystem domosystem;
	
	private House house;
	private Map<Integer, Division> divisions;
	private Map<Integer, Floor> floors;
	private Map<Integer, Device> devices;
	private Map<Integer, DeviceType> deviceTypes;
	private Map<Integer, DeviceClass> deviceClasses;
	
	private Map<Integer, Array> arrays;
	private Map<Integer, Enumerated> enums;
	private Map<Integer, Scalar> scalars;
	
	private Map <Device, Map<Property, Integer>> devicesToChange;
	
	private static final String ID = "ID";
	private static final String NAME = "Name";
	private static final String VERSION = "Version";
	private static final String DATE = "Date";
	private static final String ADDRESS = "Address";
	private static final String PHONE = "Phone";
	private static final String HEIGHT_ORDER = "HeightOrder";
	private static final String REF_FLOOR = "RefFloor";
	private static final String ACCESS_LEVEL = "AccessLevel";
	
	private static final String HOUSE_TAG = "House";
	private static final String FLOOR_TAG = "Floor";
	private static final String DIVISION_TAG = "Division";
	private static final String DOMOBUS_SYSTEM_TAG = "DomoBusSystem";
	private static final String FLOOR_LIST_TAG = "FloorList";
	private static final String DIVISION_LIST_TAG = "DivisionList";
	private static final String DEVICE_LIST_TAG = "DeviceList";
	private static final String DEVICE_TAG = "Device";
	private static final String DEVICE_TYPE_LIST_TAG = "DeviceTypeList";
	private static final String DEVICE_TYPE_TAG = "DeviceType";
	private static final String PROPERTY_LIST_TAG = "PropertyList";
	private static final String DESCRIPTION = "Description";
	private static final String PROPERTY_TAG = "Property";
	private static final String ACCESS_MODE = "AccessMode";
	private static final String VALUE_TYPE = "ValueType";
	
	private static final String REF_DEVICE_TYPE = "RefDeviceType";
	private static final String REF_DIVISION = "RefDivision";
	private static final String REF_VALUE_TYPE = "RefValueType";
	
	private static final String ENUM_TYPE = "ENUM";
	private static final String SCALAR_TYPE = "SCALAR";
	private static final String ARRAY_TYPE = "ARRAY";
	private static final String SCALAR_VALUE_TYPE_TAG = "ScalarValueType";
	private static final String ENUM_VALUE_TYPE_TAG = "EnumValueType";
	private static final String ARRAY_VALUE_TYPE_TAG = "ArrayValueType";
	private static final String SCALAR_VALUE_TYPE_LIST_TAG = "ScalarValueTypeList";
	private static final String ENUM_VALUE_TYPE_LIST_TAG = "EnumValueTypeList";
	private static final String ARRAY_VALUE_TYPE_LIST_TAG = "ArrayValueTypeList";
	private static final String DEVICE_CLASS_LIST_TAG = "DeviceClassList";
	private static final String DEVICE_CLASS_TAG = "DeviceClass";
	private static final String REF_DEVICE_CLASS = "RefDeviceClass";
	private static final String IMAGE_PATH = "ImagePath";
	private static final String IMAGE_DEFAULT = "ImageDefault";
	private static final String IMAGE_LIST_TAG = "ImageList";
	private static final String REF_PROPERTY = "RefProperty";
	private static final String VALUE_RANGE = "ValueRange";
	private static final String OPMODE = "OpMode";
	
	
	private static final String NUM_BITS = "NumBits";
	private static final String UNITS = "Units";
	private static final String MIN_VALUE = "MinValue";
	private static final String MAX_VALUE = "MaxValue";
	private static final String STEP = "Step";
	
	private static final String VALUE = "Value";
	
	private static final String MAX_LEN = "MaxLen";
	
	private App app;
	
	private DCommAPI api;
	
	//////////////////////////////////////////////////////////////////////////////
	
	public final int DS_PROPERTY_TYPE_MASK 	= 	0xC0;  	/* 1100 0000 */
	public final int DS_VALUE_INVALID_MASK 	= 	0x20;  	/* 0010 0000 */
	public final int DS_PROPERTY_ID_MASK 		= 	0x1F; 	/* 0001 1111 */
	public final int DS_PROPERTY_TID_MASK 	= 	0xDF;  	/* 1101 1111 */ /* TID = TYPE + ID */

	public final int DS_VALUE_TYPE_8_BIT 		= 	0x00;  	/* 00xx xxxx */ /* 0 */
	public final int DS_VALUE_TYPE_16_BIT 	= 	0x40; 	/* 01xx xxxx */ /* 64 */
	public final int DS_VALUE_TYPE_32_BIT 	= 	0x80; 	/* 10xx xxxx */ /* 128 */
	public final int DS_VALUE_TYPE_ARRAY 		=	0xC0;  	/* 11xx xxxx */ /* 192 */

	public final int DS_VALUE_INVALID 		=	0x20;  	/* xx1x xxxx */ /* 32 */
	public final int DS_VALUE_VALID 			=	0x00;  	/* xx0x xxxx */

	//////////////////////////////////////////////////////////////////////////////

	
	public DomoBusBE(App app) {
		this.app = app;
		//
	}
	
	/**
	 * Initializes the data structures for the DomoBus 
	 * elements (devices and house partitions), as well as the 
	 * data structure that saves tentative changes that would be sent 
	 * to the DomoBus Supervisor.
	 * Also initializes the DCommAPI.
	 *
	 * @see         DCommAPI
	 */
	
	public void init() {
		//houses = new ArrayList<House>();
		//houses = new TreeMap<Integer, House>();
		divisions = new TreeMap<Integer, Division>();
		floors = new TreeMap<Integer, Floor>();
		devices = new TreeMap<Integer, Device>();
		deviceTypes = new TreeMap<Integer, DeviceType>();
		deviceClasses = new TreeMap<Integer, DeviceClass>();

		arrays = new TreeMap<Integer, Array>();
		enums = new TreeMap<Integer, Enumerated>();
		scalars = new TreeMap<Integer, Scalar>();
		
		devicesToChange = new HashMap<Device, Map<Property, Integer>>();
		
		initDCommAPI();

	}
	
	
	
	/**
	 * Loads a DomoBus system specification from a XML file, and
	 * calls the method xmlImport to build the system recursively.
	 *
	 * @param  filename  an absolute path giving the location of the file
	 */
	public void loadSpec(String filename) throws Exception {
		System.out.println("[DEBUG] Loading specification file: " + filename);
		
		if (filename == null)
			return;
		File file = new File(filename);
		SAXBuilder builder = new SAXBuilder();
		

		try {
			Document document = (Document)builder.build(file);
			xmlImport(document.getRootElement());
			
		} catch (JDOMException | IOException e) {
			e.printStackTrace();
			throw e;
		}
		
	}
	
	/**
	 * Recursively parses the XML file and creates the respective
	 * DomoBus elements, starting on the root (which corresponds to the
	 * DomoBus system specification metadata)
	 *
	 * @param  root  the root element of the XML file loaded
	 */
	public void xmlImport(Element root) throws Exception {
		
		try {
			//Creates DomoBusSytem
			createEntity(root);
			
			for (Element element : root.getChildren()) {
				createEntity(element);
			}
			
			System.out.println("[DEBUG] System specification successfully imported");
		} catch (Exception e) {
			System.out.println("[ERROR] Could not parse XML specification!");
			throw e;
		}
		
	}
	
	
	/**
	 * Parses the DomoBus system metadata to display in the GUI
	 *
	 * @return An array of strings with the system information
	 */
	public String[] getSystemInfo() throws Exception {
		String[] info = new String[5];
		info[0] = Integer.toString(domosystem.getId());
		info[1] = domosystem.getName();
		info[2] = domosystem.getVersion();
		info[3] = domosystem.getDate();
		
		return info;
	}
	
	
	
	/**
	 * Recursively creates the DomoBus system elements.
	 * 
	 * Lists (e.g. DivisionList) are created by recursively calling this method, and adding 
	 * the created entity to its respective data structure. If creating a list, the returning 
	 * value is null.
	 * 
	 * Singular entities (e.g. Division) are created by parsing an XML tag and it's attributes,
	 * and then returning themselves in case a list is trying to build a collection of them.
	 * 
	 *
	 * @param  entity  an JDom entity corresponding to a tag in the
	 * XML system specification.
	 * 
	 * @return      an abstract DomoBus entity 
	 * @see         Image
	 */
	public AbstractSpecEntity createEntity(Element entity) throws Exception {
		
		AbstractSpecEntity createdEntity = null;
		
		if (entity.getAttributes().size() != 0) {
		
			int id = Integer.parseInt(getAttribute(entity,ID));
			
			
			if (entity.getName().equals(DOMOBUS_SYSTEM_TAG)) {	
				
				String name = getAttribute(entity, NAME);
				String version = getAttribute(entity, VERSION);
				String date = getAttribute(entity, DATE);
				this.domosystem = new DomoBusSystem(id, name, version, date);
				createdEntity = domosystem;
			}
			
			else if (entity.getName().equals(HOUSE_TAG)) {
				String name = getAttribute(entity, NAME);
				String address = getAttribute(entity, ADDRESS);
				String phone = getAttribute(entity, PHONE);
				
				if (phone != null)
					house = new House(id, name, address, phone);
				else
					house = new House(id, name, address);
				
				
				Element floorList = entity.getChild(FLOOR_LIST_TAG);
				for (Element floor : floorList.getChildren()) {
					Floor fl = (Floor)createEntity(floor);
					addToCollection(fl, floors);
					house.addFloor(fl);
				}
				
				Element divList = entity.getChild(DIVISION_LIST_TAG);
				for (Element div : divList.getChildren()) {
					Division dv = (Division)createEntity(div);
					addToCollection(dv, divisions);
					Floor floor = dv.getFloor();
					floor.addDivision(dv);
					house.addDivision(dv);
				}
				
				createdEntity = house;			
			}
			
			else if (entity.getName().equals(FLOOR_TAG)) {
				String name = getAttribute(entity, NAME);
				int heightOrder = Integer.parseInt(getAttribute(entity, HEIGHT_ORDER));
				Floor floor = new Floor(id, name, heightOrder);
				createdEntity = floor;
			}
			
			else if (entity.getName().equals(DIVISION_TAG)) {
				String name = getAttribute(entity, NAME);
				int refFloor = Integer.parseInt(getAttribute(entity, REF_FLOOR));
				int accessLevel = Integer.parseInt(getAttribute(entity, ACCESS_LEVEL));
				Floor floor = findFloor(refFloor);
				Division division = new Division(id, name, accessLevel, floor);
				createdEntity = division;
			}
			
			else if (entity.getName().equals(DEVICE_TYPE_TAG)) {
				String name = getAttribute(entity, NAME);
				String description = getAttribute(entity, DESCRIPTION);
				String refClassStr = getAttribute(entity, REF_DEVICE_CLASS);
				
				DeviceType devType;
				if (refClassStr != null) {
					int refClass = Integer.parseInt(refClassStr);
					DeviceClass devClass = (DeviceClass) findInCollection(refClass, deviceClasses);
					if (devClass == null) {
						createdEntity = null;
						return createdEntity;
					}
					devType = new DeviceType(id, name, devClass, description);
				}
				else
					devType = new DeviceType(id, name, description);
				
				String defaultImgPath = getAttribute(entity, IMAGE_DEFAULT);
				if (defaultImgPath != null) {
					Image image = new Image(0, defaultImgPath);
					devType.setDefaultImg(image);
				}
				
				
				Element propertyList = entity.getChild(PROPERTY_LIST_TAG);
				for (Element property : propertyList.getChildren()) {
					Property prop = (Property)createEntity(property);
					devType.addProperty(prop);
				}
				
				Element imgList = entity.getChild(IMAGE_LIST_TAG);
				if (imgList != null)
					for (Element imgElement : imgList.getChildren()) {
						int img_id = Integer.parseInt(getAttribute(imgElement, ID));
						int refProperty = Integer.parseInt(getAttribute(imgElement, REF_PROPERTY));
						String valueRange = getAttribute(imgElement, VALUE_RANGE);
						String imagePath = getAttribute(imgElement, IMAGE_PATH);					
						Property property = devType.getProperty(refProperty);
						
						Image image = new Image(img_id, property, valueRange, imagePath);
						devType.addImage(property.getId(), image);
					}
					createdEntity = devType;
			}
			
			else if (entity.getName().equals(PROPERTY_TAG)) {
				String name = getAttribute(entity, NAME);
				String accessMode = getAttribute(entity, ACCESS_MODE);
				String valueType = getAttribute(entity, VALUE_TYPE);
				int refValueType = Integer.parseInt(getAttribute(entity, REF_VALUE_TYPE));
				ValueType valType = findValueType(valueType, refValueType);
				
				String imagePath = getAttribute(entity, IMAGE_PATH);	
				
				Property property;
				if (imagePath != null) {
					Image image = new Image(0, imagePath);
					property = new Property(id, name, accessMode, valType, image);
				}
				else
					property = new Property(id, name, accessMode, valType);
				
				String opmode = getAttribute(entity, OPMODE);
				if (opmode != null) {
					property.setOpmode(opmode);
				}
				
				
				createdEntity = property;
			}
			
			else if (entity.getName().equals(SCALAR_VALUE_TYPE_TAG)) {
				String name = getAttribute(entity, NAME);
				int numBits = Integer.parseInt(getAttribute(entity, NUM_BITS));
				String units = getAttribute(entity, UNITS);
				int minValue = Integer.parseInt(getAttribute(entity, MIN_VALUE));
				int maxValue = Integer.parseInt(getAttribute(entity, MAX_VALUE));
				int step = Integer.parseInt(getAttribute(entity, STEP));
				
				Scalar scalar = new Scalar(id, name, minValue, maxValue, units, step, numBits);
				
				createdEntity = scalar;
			}
			
			else if (entity.getName().equals(ENUM_VALUE_TYPE_TAG)) {
				String name = getAttribute(entity, NAME);
				Enumerated enumerated = new Enumerated(id, name);
				
				for (Element val : entity.getChildren()) {
					String designation = getAttribute(val, NAME);
					int value = Integer.parseInt(getAttribute(val, VALUE));
					enumerated.addValue(designation, value);
				}
				
				createdEntity = enumerated;
			}
			
			else if (entity.getName().equals(ARRAY_VALUE_TYPE_TAG)) {
				String name = getAttribute(entity, NAME);
				int maxLen = Integer.parseInt(getAttribute(entity, MAX_LEN));
				
				Array array = new Array(id, name, maxLen);
				createdEntity = array;
			}
			
			else if (entity.getName().equals(DEVICE_TAG)) {
				
				int refDevType = Integer.parseInt(getAttribute(entity, REF_DEVICE_TYPE));
				String name = getAttribute(entity, NAME);
				int address = Integer.parseInt(getAttribute(entity, ADDRESS));
				int refDivision = Integer.parseInt(getAttribute(entity, REF_DIVISION));
				
				DeviceType devType = (DeviceType) findInCollection(refDevType, deviceTypes);
				Division division = (Division) findInCollection(refDivision, divisions);
				
				if (devType == null || division == null) {
					createdEntity = null;
					return createdEntity;
				}
				
				Device device = new Device(id, name, address, devType, division);
				division.addDevice(device);
				createdEntity = device;
			}
			
			else if (entity.getName().equals(DEVICE_CLASS_TAG)) {
				String name = getAttribute(entity, NAME);
				DeviceClass devClass = new DeviceClass(id, name);
				createdEntity = devClass;
			}
			
			
			///////////////////////////////////////
			// ADD NEW ENTITIES BEFORE THIS LINE
			///////////////////////////////////////
			
			else {
				//System.out.println("[DEBUG] No handler for " + entity.getName());
				return null;
			}
			
			//System.out.println("[DEBUG] Created " + entity.getName());
			return createdEntity;
		
			
		} else {
			
			/////////////////////////////////////////////	
			// FOR LISTS OR ENTITIES WITH NO ATTRIBUTES
			/////////////////////////////////////////////
			if (entity.getName().equals(DEVICE_TYPE_LIST_TAG)) {
				
				for (Element devtype : entity.getChildren()) {
					DeviceType deviceType = (DeviceType) createEntity(devtype);
					addToCollection(deviceType, deviceTypes);
				}
					
				createdEntity = null;
			}
			
			else if (entity.getName().equals(DEVICE_LIST_TAG)) {
								
				for (Element device : entity.getChildren()) {
					Device dev = (Device) createEntity(device);
					addToCollection(dev, devices);
				}

				createdEntity = null;
			}
			
			
			
			else if (entity.getName().equals(SCALAR_VALUE_TYPE_LIST_TAG)) {
				for (Element scalarEntity : entity.getChildren()) {
					Scalar scalar = (Scalar)createEntity(scalarEntity);
					addToCollection(scalar, scalars);
				}
					
			}
			
			else if (entity.getName().equals(ENUM_VALUE_TYPE_LIST_TAG)) {
				for (Element enumEntity : entity.getChildren()) {
					Enumerated enumerated = (Enumerated)createEntity(enumEntity);
					addToCollection(enumerated, enums);
				}
			}
			
			else if (entity.getName().equals(ARRAY_VALUE_TYPE_LIST_TAG)) {
				for (Element arrayEntity : entity.getChildren()) {
					Array array = (Array)createEntity(arrayEntity);
					addToCollection(array, arrays);
				}
			}
			
			else if (entity.getName().equals(DEVICE_CLASS_LIST_TAG)) {
				for (Element devClassEntity : entity.getChildren()) {
					DeviceClass devclass = (DeviceClass)createEntity(devClassEntity);
					addToCollection(devclass, deviceClasses);
				}
			}
			
			
			//System.out.println("[DEBUG] No handler for " + entity.getName());
		}
		
		return null;
		
	}
	
	
	
	/**
	 * General method to find some DomoBus element in some collection
	 * 
	 *
	 * @param	id	the ID of the DomoBus element that is to be found
	 * @param	collection	a collection of a DomoBus elements, parametrized by 
	 * 						a subclass of an abstract DomoBus entity
	 * 
	 * @return      the DomoBus element corresponding to the id, or null if non existent
	 */
	private <T extends AbstractSpecEntity> AbstractSpecEntity findInCollection(int id, Map<Integer, T> collection) {
		return collection.get(id);
	}

	/**
	 * General method to add some DomoBus element to some collection
	 * 
	 *
	 * @param	entity	the entity that is to be added to the collection
	 * @param	collection	a collection of a DomoBus elements, parametrized by 
	 * 						a subclass of an abstract DomoBus entity
	 */
	private <T extends AbstractSpecEntity> void addToCollection(T entity, Map <Integer, T> collection) {
		collection.put(entity.getId(), entity);
	}

	
	/**
	 * Returns the ValueType corresponding to a string description and
	 * an id of the value type.
	 * 
	 *
	 * @param	valueType	a string corresponding to the name of the ValueType
	 * @param	refValueType	an integer corresponding to the id of the respective ValueType
	 * 
	 * @return      the correspondent ValueType
	 */
	private ValueType findValueType(String valueType, int refValueType) {
		
		Map<Integer, ?> map; 

		if (valueType.equals(ENUM_TYPE)) {
			map = enums;
		}
		
		else if (valueType.equals(SCALAR_TYPE)) {
			map = scalars;
		}
		
		else if (valueType.equals(ARRAY_TYPE)) {
			map = arrays;
		} 
		
		else {
			return null;
		}
		
		return (ValueType)map.get(refValueType);
		
	}

	
	/**
	 * Finds a floor within all the possible houses. Useful if a system specification
	 * has more than one house.
	 *
	 * @param	id	the ID of the floor that is to be found
	 * 
	 * @return      the Floor with the corresponding id
	 */
	public Floor findFloor(int id) {
		Floor floor = findFloorOfHouse(house, id);
		if (floor != null) {
			return floor;
		}
		return null;
		
	}
	
	
	/**
	 * Returns a floor with a specific id within a specific house
	 * 
	 *
	 * @param	house	the house to find the floor
	 * @param	floorId	the floor to be found
	 * @return      the Floor within the house with the id given
	 */
	public Floor findFloorOfHouse(House house, int floorId) {
		return house.getFloor(floorId);
	}
	
	
	/**
	 * Returns a division of a specific floor 
	 *
	 * @param	floor	the floor to find the division of
	 * @param	divId	the id corresponding to the division to be found
	 * @return      the Division corresponding to the divId of the floor
	 */
	public Division findDivisionOfFloor(Floor floor, int divId) {
		return floor.getDivision(divId);
	}

	
	/**
	 * Given a division, returns all the devices in that division 
	 *
	 * @param	division	the division to return all the devices
	 * @return      a collection of devices from the given division
	 */
	public Map<Integer, Device> getDevicesOfDivision(Division division) {
		return division.getDevices();
	}
	
	
	/**
	 * Given a floor, returns all the divisions in that floor 
	 *
	 * @param	floor	the floor to return all the divisions
	 * @return      a collection of divisions from the given floor
	 */
	public Map<Integer, Division> getDivisionsOfFloor(Floor floor) {
		return floor.getDivisions();
	}
	
	/**
	 * Given a division and a device name, returns the device of that division
	 * with that name. 
	 * This is needed because the GUI only works with names, and for this to work
	 * each device must have a unique name.
	 *
	 * @param	device	the name of the device to be returned
	 * @param	division	the division of the device
	 * @return      the device with the corresponding name 
	 */
	public Device findDeviceOfDivision(String device, Division division) {
		for (Device dev : getDevicesOfDivision(division).values())
			if (dev.getName().equals(device))
				return dev;
		return null;
	}
	
	
	
	public Map<Integer, Device> getDevicesOfFloor(int id, Floor floor) {
		Division div = findDivisionOfFloor(floor, id);
		return div.getDevices();
	}
		
	private String getAttribute(Element element, String attribute) {
		if (element.getAttribute(attribute) != null)
			return element.getAttribute(attribute).getValue();
		else
			return null;
	}

	public void changeValue(Device device, Property property, int newvalue) {
		
		
		Map<Property, Integer> values = devicesToChange.get(device);
		if (values == null) {
			values = new HashMap<Property, Integer>();
			devicesToChange.put(device, values);
		}
			
		values.put(property, newvalue);
		
		
	}
	
	
	public void commitChanges(Device device, int id, ValueType valueType) {
		
		Map<Property, Integer> toRemove = new HashMap<Property, Integer>();

		String type = device.getProperty(id).getValueType().getClass().getSimpleName();
		HashMap<Device, Map<Property, Integer>> changes = new HashMap<Device, Map<Property, Integer>>();
		
		Map<Property, Integer> values = devicesToChange.get(device);
		
		for (Property prop : values.keySet()) {
			if (prop.getId() == id && prop.getValueType().equals(valueType)) {
				type = prop.getValueType().getClass().getSimpleName();
				device.setValue(prop.getId(), type, values.get(prop));
				
				sendNotify(device, prop, values.get(prop), null);
				
				if (prop.getOpmode().equals("ACTION")) {
					device.setValue(prop.getId(), type, 0);
				}
				
				int newvalue = values.get(prop);
							
				System.out.println("Changed " + device.getName() + ": " + prop.getName() + " to " + newvalue);
				
				toRemove.put(prop, newvalue);
			}
		}
		
		Map<Property, Integer> propChanges = new HashMap<Property, Integer>();

		for (Property prop : toRemove.keySet()) {
			propChanges.put(prop, toRemove.get(prop));
			values.remove(prop);
		}
		
		changes.put(device, propChanges);

		
		app.setChangedDevices(changes);

		
	}
	

	
	public int readValue(Device device, Property property) {
		String type = property.getValueType().getClass().getSimpleName();
		return device.getValue(property.getId(), type);
	}

	
	public House getHouse() {
		return house;
	}

	public void changeValue(Device device, Property property, String value) {
		device.setValue(property.getId(), value);
		
		System.out.println("Changed " + device.getName() + ": " + property.getName() + " to " + value);

		//3rd argument does not matter, only for enum or scalar
		sendNotify(device, property, 0, value);
		
		if (property.getOpmode().equals("ACTION")) {
			device.setValue(property.getId(), "");
		}
	}
	
	public String readArray(Device device, Property property) {
		return device.getValue(property.getId());
	}

	public void saveState(String path) throws IOException {
		
		Element components = new Element("Components");
        Document doc;
        	
        doc = new Document(components);
        
        Element stateList = new Element("DeviceStateList");

        for (Device device : devices.values()) {
        	for (Property property : device.getProperties().values()) {
        		Element state = new Element("DeviceState");
        		
        		String str_invalid = "";
        		
        		if (isInvalid(device, property.getId()))
        			str_invalid = "True";
        		else
        			str_invalid = "False";
        		
        		Attribute invalid = new Attribute("InvalidValue", str_invalid);
        		Attribute refDevice = new Attribute("RefDevice", Integer.toString(device.getId()));
        		Attribute refProperty = new Attribute("RefProperty", Integer.toString(property.getId()));
        		String type = property.getValueType().getClass().getSimpleName();
        		Attribute value;
        		if (type.equals("Array")) {
        			value = new Attribute("Value",readArray(device, device.getProperty(property.getId())));
        		}
        		else {
        			value = new Attribute("Value", Integer.toString(
        				readValue(device, device.getProperty(property.getId()))));
        		}
        		
        		state.setAttribute(invalid);
        		state.setAttribute(refDevice);
        		state.setAttribute(refProperty);
        		state.setAttribute(value);
        		
        		stateList.addContent(state);
        	}
        }
        
        Element workspaceList = new Element("WorkspaceList");
        
        for (Device device : app.getWorkspace().values()) {
        	Element dev = new Element("Device");
        	
        	Attribute id = new Attribute(ID,Integer.toString(device.getId()));
        	
        	dev.setAttribute(id);
        	workspaceList.addContent(dev);
        }
            
        
        components.addContent(stateList);
        components.addContent(workspaceList);
        
        XMLOutputter xmlOutput = new XMLOutputter(Format.getPrettyFormat());
		xmlOutput.output(doc, new FileWriter(path));

	}

	public void loadState(String path) throws JDOMException, IOException {
				
		File file = new File(path);
		SAXBuilder builder = new SAXBuilder();
		

		
		Document document = (Document)builder.build(file);
		Element root = document.getRootElement();
		Element stateList = root.getChild("DeviceStateList");
		Element workspace = root.getChild("WorkspaceList");
		
		for (Element state : stateList.getChildren()) {
			int deviceID = Integer.parseInt(getAttribute(state, "RefDevice"));
			int propertyID = Integer.parseInt(getAttribute(state, "RefProperty"));
			
			String str_invalid = getAttribute(state, "InvalidValue");
			
			boolean invalid = true;
			if (str_invalid.equals("False")) {
				invalid = false;
			}
			
			
			
			String str_value = getAttribute(state, "Value");
			
			Device device = devices.get(deviceID);
			
			setInvalid(device, propertyID, invalid);
			
			Property property = device.getProperty(propertyID);
			String type = property.getValueType().getClass().getSimpleName();
			
			if (type.equals("Array")) {
				changeValue(device, property, str_value);
				app.updateImage(device, property.getId());
			} else {
				changeValue(device, property, Integer.parseInt(str_value));
				app.loadValue(device, property);
			}
			
			
			
		}
		
		app.getWorkspace().clear();
		for (Element device : workspace.getChildren()) {
			
			int deviceID = Integer.parseInt(getAttribute(device, ID));
			
			Device dev = devices.get(deviceID);
			app.getWorkspace().put(deviceID, dev);
			app.updateWorkspace();
		}
		
		
	}
	
	public void setInvalid(Device device, int property, boolean value) {
		
		device.setInvalid(property, value);
		
		app.invalidDevice(device, value);
		
	}

	public boolean isInvalid(Device device, int property) {
		return device.isInvalid(property);
	}
	
	public boolean hasInvalidProperty(Device device) {
		for (Property property : device.getProperties().values()) {
			int prop = property.getId();
			
			if (device.isInvalid(prop))
				return true;
		}
		return false;
	}
		
	///////////////////////////////////////////////////////////////////////
	
	
	private void initDCommAPI() {
		
		System.out.println("[DEBUG] Initializing DComm module");
		
		api = DCommAPI.init("c1.txt");
		api.setDmb(this);		
	}
	
	
	 
	
	/*
	 * Auxiliar functions
	 * */
		
	 public String byteToHexString(byte bytes[]) {
		    StringBuffer retString = new StringBuffer();
		    for (int i = 0; i < bytes.length; ++i) {
		      retString.append(Integer.toHexString(0x0100 + (bytes[i] & 0x00FF))
		                       .substring(1));
		    }
		    return retString.toString();
	}
	 
	private byte[] intToByteArray(int value) {
		return new byte[] {
	            (byte)(value),
	            (byte)(value >>> 8),
	            (byte)(value >>> 16),
	            (byte)(value >>> 24)};
	}
	 
	public void closeDComm() {
		//if (api != null)
			//api.closeDComm();
	}

	
	public Device getDeviceFromAddress(int addr) {
		for (Device dev : devices.values())
			if (dev.getAddress() == addr)
				return dev;
		return null;
	}
	
	public Property getProperty(int device_id, int property_id, String type) {
		Device device = devices.get(device_id);
		
		for (Property property : device.getProperties().values()) {
			String ptype = property.getValueType().getClass().getSimpleName();
			if (ptype.equals(type) && property.getId() == property_id)
				return property;
		}
		return null;
	}
	
	public int handleSet(String params) {
		
		String[] tokens = params.split(" ");
		

		
		int device_addr = Integer.parseInt(tokens[0]);
		int prop_desc = 0;
		
		if (tokens[1].startsWith("0x")) {
			prop_desc = Integer.parseInt(tokens[1].substring(2), 16);
		} else {
			prop_desc = Integer.parseInt(tokens[1]);
		}
		
		Device device = getDeviceFromAddress(device_addr);
		if (device == null) {
			String msg = "Device with address " + device_addr + " not found";
			throw new NullPointerException(msg);
		}
		
		//////////////////////////

		int property_type = prop_desc & DS_PROPERTY_TYPE_MASK;
		int property_id = prop_desc & DS_PROPERTY_ID_MASK;
		int value_invalid = prop_desc & DS_VALUE_INVALID_MASK;
		String type = "";

		
		if (value_invalid != 0) {
			
			setInvalid(device, property_id, true);
			return 0;
			
		}
		
		if (property_type == DS_VALUE_TYPE_ARRAY) {
			type = "Array";
			
			Property property = getProperty(device.getId(), property_id, type);
			if (property == null) {
				String msg = "Property with id " + property_id + " of type " + type + " of device " + device.getAddress() + " not found";
				throw new NullPointerException(msg);		
			}
			
			String value = params.split("\"")[1];
			
			System.out.println("\n===================");
			System.out.println("SETTING:");
			System.out.println("\tDevice addr: " + device_addr);
			System.out.println("\tProperty: " + property_id);
			System.out.println("\tValue: " + value);
			System.out.println("\tType: " + property.getValueType().getClass().getSimpleName());
			System.out.println("\t(" + type + ")");
			System.out.println("===================\n");
			
			changeValue(device, property, value);
		}
		
		
		else {
			if (property_type == DS_VALUE_TYPE_8_BIT) {
				type = "Enumerated";
			}
			
			else {
				type = "Scalar";
			}
			
			Property property = getProperty(device.getId(), property_id, type);
			if (property == null) {
				String msg = "Property with id " + property_id + " of type " + type + " of device " + device.getAddress() + " not found";
				throw new NullPointerException(msg);		
			}
			
			int value = Integer.parseInt(tokens[2].trim());
			
			System.out.println("\n===================");
			System.out.println("SETTING:");
			System.out.println("\tDevice addr: " + device_addr);
			System.out.println("\tProperty: " + property_id);
			System.out.println("\tValue: " + value);
			System.out.println("\tType: " + property.getValueType().getClass().getSimpleName());
			System.out.println("\t(" + type + ")");
			System.out.println("===================\n");
			
			changeValue(device, property, value);
			commitChanges(device, property_id, property.getValueType());
		}
		return 0;
	}

	public int handleGet(int device_addr, int prop_desc, byte[] value_a) {
		
		Device device = getDeviceFromAddress(device_addr); 
		
		if (device == null)
			return -1;
		
		int property_type = prop_desc & DS_PROPERTY_TYPE_MASK;
		int property_id = prop_desc & DS_PROPERTY_ID_MASK;
		
		if (isInvalid(device, property_id)) {
			value_a[0] = 1;
			return 0;
		} else {
			value_a[0] = 0;
		}
		
		try {
		
			
			if (property_type == DS_VALUE_TYPE_ARRAY) {
				
				String value = device.getValue(property_id);
				byte[] str_array = value.getBytes();
				value_a[1] = (byte)value.length();
				System.arraycopy(str_array, 0, value_a, 2, value.length());
				
			}
			
			
			else {
				int value = 0;
				if (property_type == DS_VALUE_TYPE_8_BIT) {
					String type = "Enumerated";
					value = device.getValue(property_id, type);
					//value_a[1] = (byte) (value);
				}
				
				else {
					String type = "Scalar";
					value = device.getValue(property_id, type);
					//byte[] actual_value = intToByteArray(value);
					//value_a = joinArrays(value_a, actual_value);
					//System.out.println("Answering with " + byteToHexString(value_a));
				}

				value_a[1] = (byte) (value & 0x00FF);
				value_a[2] = (byte) ((value & 0x00FF00) >> 8);
				value_a[3] = (byte) ((value & 0x00FF0000) >> 16);
				value_a[4] = (byte) ((value & 0x00FF000000) >> 24);
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 0;
	}
	
	
	
	
	private void sendNotify(Device device, Property property, int value, String value_array) {

		
		/*
		 * If property is enumerated or scalar, only the third argument (int value) matters
		 * If property is an array, only the last arguments (String value_array) matters
		 */
		int dev_addr = device.getAddress();
		int prop_id = property.getId();

		int prop_desc = 0;
		byte[] value_a = new byte[4];	

		int validity = DS_VALUE_VALID;
		if (isInvalid(device, prop_id))
			validity = DS_VALUE_INVALID;

		int val_type_bit = 0;

		if (property.getValueType() instanceof Enumerated) {

			value_a[0] = (byte) (value);
			val_type_bit = DS_VALUE_TYPE_8_BIT;

		}

		else if (property.getValueType() instanceof Scalar) {
			System.out.println("Sending a scalar");

			int num_bits = ((Scalar)property.getValueType()).getNumBits();
			if (num_bits == 16)
				val_type_bit = DS_VALUE_TYPE_16_BIT;
			else
				val_type_bit = DS_VALUE_TYPE_8_BIT;

			value_a = intToByteArray(value);

		} 


		else {
			System.out.println("Sending an array");

			val_type_bit = DS_VALUE_TYPE_ARRAY;
			int length = value_array.length();
			byte[] array = value_array.getBytes();

			value_a = new byte[length + 1];
			value_a[0] = (byte)length;
			System.arraycopy(array, 0, value_a, 1, length);

			System.out.println(Arrays.toString(value_a) + " : length = " + value_a.length);			


		}

		prop_desc = prop_id | validity | val_type_bit;
		api.sendNotify(dev_addr, prop_desc, value_a);

	}

	public void updateWorkspace() {
		app.updateWorkspace();
		
	}



}
