package App;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;


import domospec.Image;
import domospec.Property;
import domospec.devices.Device;
import domospec.home.Division;
import domospec.home.Floor;
import domospec.home.House;
import domospec.valuetypes.Array;
import domospec.valuetypes.Enumerated;
import domospec.valuetypes.Scalar;
import domospec.valuetypes.ValueType;

import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;


public class App {

	protected Shell shlDomobusSimulator;
	private DomoBusBE dmb;
	
	private MenuItem menuSave;
	private MenuItem menuLoad;
	private MenuItem openScriptController;
	
	private Label nameTxt;
	private Label versionTxt;
	private Label dateTxt;
	private Label lblHelp;
	
	private List floorsList;
	private List divisionsList;
	private List devicesList;
	private List previewList;
	
	private House selectedHouse;
	private Floor selectedFloor;
	private Division selectedDivision;
	private Device selectedDevice;
	private Device selectedDeviceList;
	private Button selectedButton;
	
	private Display display;
	
	Group grpDevices;
	Group grpProperties;
	
	private Map<Button, Device> devButtons;
	private Map<Button, Label> devLabels;
	
	private Map<Integer, Device> workspace;
	
	private Map<Group, Property> propGroups;
	private Map<Device, Image> images;
	
	private Map <Device, Map<Property, Integer>> changedDevices;
	
	
	private static final int DEVICE_STARTING_X = 40;
	private static final int DEVICE_STARTING_Y = 37;
	private static final int INTERVAL_BETWEEN_DEVICES_X = 50;
	private static final int INTERVAL_BETWEEN_DEVICES_Y = 50;
	private static final int DEVICE_WIDTH = 64;
	private static final int DEVICE_HEIGHT = 64;
	
	private static final int DEVICE_LABEL_INTERVAL = 5;
	private static final int DEVICE_LABEL_WIDTH = 100;
	private static final int DEVICE_LABEL_HEIGHT= 20;
	//private static final int LABEL_THRESHOLD_WRAP = 13; 	//13 characters
	
	private static final int PROPERTY_GROUP_STARTING_X = 10;
	private static final int PROPERTY_GROUP_STARTING_Y = 30;
	private static final int PROPERTY_GROUP_WIDTH = 345;
	private static final int PROPERTY_GROUP_HEIGHT = 70;
	private static final int PROPERTY_GROUP_INTERVAL = 10;
	
	private static final String CLASS_ENUM = "Enumerated";
	private static final String CLASS_SCALAR = "Scalar";
	private static final String CLASS_ARRAY = "Array";
	private MenuItem scriptMenu;
	private ScriptWindow scriptWindow;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
			
		try {

			App window = new App();
			window.init();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/////////////////////////////////////////////////////////////////////
	
	
	private void init() {
		dmb = new DomoBusBE(this);
		devButtons = new HashMap<Button, Device>();
		devLabels = new HashMap<Button, Label>();
		propGroups = new HashMap<Group, Property>();
		workspace = new HashMap<Integer, Device>();
		images = new HashMap<Device, Image>();
	}
	
	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shlDomobusSimulator.open();
		shlDomobusSimulator.layout();
		while (!shlDomobusSimulator.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlDomobusSimulator = new Shell(SWT.CLOSE | SWT.MIN | SWT.TITLE); //prevent resize
		shlDomobusSimulator.addShellListener(new ShellAdapter() {
			@Override
			public void shellClosed(ShellEvent e) {
				handleClose(shlDomobusSimulator, e);
			}
		});
		shlDomobusSimulator.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shlDomobusSimulator.setImage(SWTResourceManager.getImage(App.class, "/resources/domobus.png"));
		shlDomobusSimulator.setSize(1024, 768);
		shlDomobusSimulator.setText("DomoBus Simulator");
		shlDomobusSimulator.setLayout(null);

		
		Label lblFloor = new Label(shlDomobusSimulator, SWT.NONE);
		lblFloor.setBounds(20, 479, 70, 20);
		lblFloor.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblFloor.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblFloor.setAlignment(SWT.RIGHT);
		lblFloor.setText("Floor");
		
		Label lblRoom = new Label(shlDomobusSimulator, SWT.NONE);
		lblRoom.setBounds(145, 479, 70, 20);
		lblRoom.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblRoom.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblRoom.setAlignment(SWT.RIGHT);
		lblRoom.setText("Room");
		
		Label lblDevice = new Label(shlDomobusSimulator, SWT.NONE);
		lblDevice.setBounds(307, 479, 70, 20);
		lblDevice.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDevice.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblDevice.setAlignment(SWT.CENTER);
		lblDevice.setText("Device");
		
		nameTxt = new Label(shlDomobusSimulator, SWT.NONE);
		nameTxt.setBounds(733, 626, 182, 20);
		nameTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		versionTxt = new Label(shlDomobusSimulator, SWT.NONE);
		versionTxt.setBounds(733, 652, 182, 20);
		versionTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		dateTxt = new Label(shlDomobusSimulator, SWT.NONE);
		dateTxt.setBounds(733, 678, 182, 20);
		dateTxt.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		
		
		
		floorsList = new List(shlDomobusSimulator, SWT.BORDER | SWT.V_SCROLL);
		floorsList.setBounds(10, 505, 120, 193);
		floorsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectedItems = floorsList.getSelectionIndices();
				
				if (selectedItems.length > 0) {
					int floorNumber = selectedItems[0];
					String floorName = floorsList.getItem(floorNumber);
					selectedFloor = selectedHouse.findFloorByName(floorName);
					populateList(divisionsList, dmb.getDivisionsOfFloor(selectedFloor));
				}
			}
		});
		
		divisionsList = new List(shlDomobusSimulator, SWT.BORDER);
		divisionsList.setBounds(136, 505, 120, 193);
		divisionsList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectedItems = divisionsList.getSelectionIndices();
				
				if (selectedItems.length > 0) {
					int divisionNumber = selectedItems[0];
					String divisionName = divisionsList.getItem(divisionNumber);
					selectedDivision = selectedHouse.findDivisionByName(divisionName);					
					populateDevices(selectedDivision);
					
				}
			}
		});
		
		devicesList = new List(shlDomobusSimulator, SWT.BORDER);
		devicesList.setBounds(262, 505, 162, 193);
		devicesList.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectedItems = devicesList.getSelectionIndices();
				
				if (selectedItems.length > 0) {
					int deviceNumber = selectedItems[0];
					String deviceName = devicesList.getItem(deviceNumber);
					selectedDevice = dmb.findDeviceOfDivision(deviceName, selectedDivision);
					selectedDeviceList = selectedDevice;
					populatePreview(selectedDevice);
					
				}
			}
		});
			
		Menu menu = new Menu(shlDomobusSimulator, SWT.BAR);
		shlDomobusSimulator.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		//"Open file" menu
		MenuItem mntmOpen = new MenuItem(menu_1, SWT.NONE);
		mntmOpen.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				FileDialog fd = new FileDialog(shlDomobusSimulator, SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.xml"};
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();

		        if (selected == null)
		        	return;
		        cleanAll();
		        try {
		        	dmb.init();
		        	
		        } catch (Exception dmb_e) {
		        	handleError(dmb_e, shlDomobusSimulator, dmb_e.getMessage());
		        }
		        
		        catch (java.lang.UnsatisfiedLinkError error) {
		        	handleError(new Exception(), shlDomobusSimulator, "Could not find DComm.dll");
		        	System.exit(-1);
		        }
		        try {
		        	dmb.loadSpec(selected);
		        	menuSave.setEnabled(true);
		        	menuLoad.setEnabled(true);
		        	openScriptController.setEnabled(true);
		        } catch (Exception exc) {
		        	String errmsg = "Could not load the XML specification file";
		        	handleError(exc, shlDomobusSimulator, errmsg);
		        	return;
		        }
		        updateSystemLabels();
		        
		        selectedHouse = dmb.getHouse();
		        Map<Integer, Floor> floors = selectedHouse.getFloors();
		        
				populateList(floorsList, floors);
		        
			}
		});
		mntmOpen.setText("Open DomoBus specification file...");
		
		
		menuSave = new MenuItem(menu_1, SWT.NONE);
		menuSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleSave(shlDomobusSimulator);
			}
		});
		menuSave.setText("Save state...");
		menuSave.setEnabled(false);
		
		menuLoad = new MenuItem(menu_1, SWT.NONE);
		menuLoad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleLoad(shlDomobusSimulator);
			}
		});
		menuLoad.setText("Load state...");
		menuLoad.setEnabled(false);
		
		
		scriptMenu = new MenuItem(menu, SWT.CASCADE);
		scriptMenu.setText("Script");
		
		Menu menu_2 = new Menu(scriptMenu);
		scriptMenu.setMenu(menu_2);
		
		openScriptController = new MenuItem(menu_2, SWT.NONE);
		openScriptController.setText("Open script controller");
		openScriptController.setEnabled(false);
		openScriptController.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				scriptWindow = new ScriptWindow(shlDomobusSimulator, 1);
				scriptWindow.setDmb(dmb);
				scriptWindow.open();
			}
		});
		
		
		//"About" window
		MenuItem mntmAbout = new MenuItem(menu, SWT.NONE);
		mntmAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				AboutWindow abtWindow = new AboutWindow(shlDomobusSimulator, 1);
				abtWindow.open();
			}
		});
		mntmAbout.setText("About");		
		
		Label lblName = new Label(shlDomobusSimulator, SWT.NONE);
		lblName.setBounds(657, 626, 70, 20);
		lblName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblName.setAlignment(SWT.RIGHT);
		lblName.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblName.setText("Name:");
		
		Label lblVersion = new Label(shlDomobusSimulator, SWT.NONE);
		lblVersion.setBounds(657, 652, 70, 20);
		lblVersion.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblVersion.setAlignment(SWT.RIGHT);
		lblVersion.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblVersion.setText("Version:");
		
		Label lblDate = new Label(shlDomobusSimulator, SWT.NONE);
		lblDate.setBounds(657, 678, 70, 20);
		lblDate.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblDate.setAlignment(SWT.RIGHT);
		lblDate.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD));
		lblDate.setText("Date:");
		
		grpDevices = new Group(shlDomobusSimulator, SWT.SHADOW_IN);
		grpDevices.setBounds(10, 10, 627, 463);
		grpDevices.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpDevices.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		grpDevices.setText("Workspace");
		
		lblHelp = new Label(grpDevices, SWT.NONE);
		lblHelp.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblHelp.setForeground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BORDER));
		lblHelp.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.ITALIC));
		lblHelp.setAlignment(SWT.CENTER);
		lblHelp.setBounds(90, 214, 451, 47);
		lblHelp.setText("To add a device to the workspace, right-click on the device and select \"Add to workspace\"");
		

					
		grpProperties = new Group(shlDomobusSimulator, SWT.SHADOW_IN);
		grpProperties.setBounds(643, 10, 365, 604);
		grpProperties.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpProperties.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		grpProperties.setText("Properties");
		
				
		previewList = new List(shlDomobusSimulator, SWT.BORDER);
		previewList.setBounds(430, 505, 207, 193);
		
		Label lblPreview = new Label(shlDomobusSimulator, SWT.NONE);
		lblPreview.setBounds(448, 479, 163, 20);
		lblPreview.setText("Preview");
		lblPreview.setFont(SWTResourceManager.getFont("Segoe UI", 11, SWT.NORMAL));
		lblPreview.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblPreview.setAlignment(SWT.CENTER);
				
		
	}
	

	protected void handleClose(Shell shell, ShellEvent event) {

		MessageBox save = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
		
		save.setText("Save");
		save.setMessage("Do you want to save the current state?");
		
		int buttonID = save.open();
		
        switch(buttonID) {
          case SWT.YES:
        	  if(!handleSave(shell)) {
        		  event.doit = false;
        		  return;
        	  } else {
        		  event.doit = true;
        	  }
        	  dmb.closeDComm();
        		  
          case SWT.NO:
        	  dmb.closeDComm();
        	  System.exit(1);
        }
		
	}


	private boolean handleSave(Shell shell) {
		FileDialog fd = new FileDialog(shell, SWT.SAVE);
		fd.setOverwrite(true);
		fd.setText("Save to...");
		fd.setFilterPath(".");
		String[] filterExt = { "*.xml"};
		fd.setFilterExtensions(filterExt);
		String path = fd.open();
		
		if (path == null) {
			return false;
		}
	  	  
		try {
			dmb.saveState(path);
			System.out.println("[DEBUG] Saved state to " + path);
			return true;
		} catch (Exception e) {
			handleError(e, shell, "Could not save state");
			return false;
		}
		
	}

	protected void handleLoad(Shell shell) {
		FileDialog fd = new FileDialog(shell, SWT.OPEN);
		fd.setText("Load from...");
		fd.setFilterPath(".");
		String[] filterExt = { "*.xml"};
		fd.setFilterExtensions(filterExt);
		String path = fd.open();
		
		if (path == null) {
			return;
		}
	  	  
		try {
			dmb.loadState(path);
			System.out.println("[DEBUG] Loaded state from " + path);
			return;
		} catch (Exception e) {
			handleError(e, shell, "Could not load state");
			return;
		}
		
	}

	protected void handleError(Exception exc, Shell shell, String errmsg) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
        
		exc.printStackTrace();
		
        messageBox.setText("Error");
        messageBox.setMessage(errmsg);
        int buttonID = messageBox.open();
        switch(buttonID) {
          case SWT.OK:
        	  //
        }
		
	}


	/////////////////////////////////////////////////////////////////////

	
		
	
	protected void populateProperties(Device device, Map<Group, Property> propGroups) {
		
		for (Group grp : propGroups.keySet())
			grp.dispose();
		
		Collection<Property> properties = device.getDeviceType().getProperties().values();
		
		if (selectedDeviceList == device)
			populatePreview(device);
		
		int x = PROPERTY_GROUP_STARTING_X;
		int y = PROPERTY_GROUP_STARTING_Y;
		int width = PROPERTY_GROUP_WIDTH;
		
		
		for (Property property : properties) {
					
			int height = PROPERTY_GROUP_HEIGHT;
			
			Group propGroup = new Group(grpProperties, SWT.SHADOW_IN);
			propGroup.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			propGroup.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
			propGroup.setText(property.getName());
			propGroup.setBounds(x, y, width, height);
			propGroups.put(propGroup, property);
			
			ValueType valType = property.getValueType();
			String type = valType.getClass().getSimpleName();
			
			//int value = dmb.readValue(selectedDevice, property);
			
			if (type.equals(CLASS_ENUM)) {
				
				Enumerated en = (Enumerated)valType;
				
				int button_x = x;
				int button_y = 30;
				int button_w = 90;
				int button_h = 20;				
				
				int buttonsPerColumn = 2;
				for (String designation : en.getEnums().keySet()) {
					
					button_w = designation.length() * 9 + 20;
					
					Button button = new Button(propGroup, SWT.RADIO);
					button.setBounds(button_x, button_y, button_w, button_h);
					button.setText(designation);
					button.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					
					
					button_y += button_h + 10;
					
					
					if (buttonsPerColumn <= 0) {
						buttonsPerColumn = 2;
						button_x += button_w + 100;
						button_y = 30;
						button.setBounds(button_x, button_y, button_w, button_h);
					}
					else {
						height += 15;
						propGroup.setBounds(x,y,width,height);
						buttonsPerColumn--;
					}					
					
					int value = dmb.readValue(selectedDevice, property);
					
					if (en.getValue(designation) == value) {
						button.setSelection(true);
					}
					
					
					button.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseDown(MouseEvent e) {
							

							
							button.setSelection(true);
							button.setEnabled(false);
							
							
							
							
							
							
							//int value = dmb.readValue(selectedDevice, property);
							
							//System.out.println("ENUM-Listener - value = " + value);
							
							int value = en.getValue(designation);			
							dmb.changeValue(selectedDevice, property, value);
							loadValue(device, property);

							
							value = dmb.readValue(selectedDevice, property);
							
							for (Control control : propGroup.getChildren()) {
								Button but = (Button)control;
								if (en.getEnums().keySet().contains(but.getText())) {
									
									but.setSelection(false);
									
									if (en.getValue(but.getText()) == value) {
										but.setSelection(true);
									}
									
									
								}
								
							}
							
							button.setEnabled(true);
							/*
							
							dmb.commitChanges(selectedDevice, property.getId(), property.getValueType());
							
							
							for (Button devButton : devButtons.keySet()) {
								if (devButtons.get(devButton).equals(device)) {
									changeImage(devButton, device, property);
									break;
								}
									
							}
							
														
							populatePreview(device);
							*/
							
						}
					});	
					
					
					
					
				}
			}
			
			else if (type.equals(CLASS_SCALAR)) {
				
				Scalar scalar = (Scalar)valType;
				
				
				int minValue = scalar.getMinValue();
				int maxValue = scalar.getMaxValue();
				int step = scalar.getStep();
				
				int spinner_x = 10;
				int spinner_y = 35;
				int spinner_w = 50;
				int spinner_h = 25;	
				
				int label_w = scalar.getName().length() * 10  + 20;
				int label_h = 20;				
								
				Spinner spinner = new Spinner(propGroup, SWT.BORDER);
				spinner.setMaximum(maxValue);
				spinner.setMinimum(minValue);
				spinner.setIncrement(step);
				spinner.setBounds(spinner_x, spinner_y, spinner_w, spinner_h);
				int val = dmb.readValue(device, property);
				spinner.setSelection(val);
				
				Label label = new Label(propGroup, SWT.NONE);
				label.setAlignment(SWT.LEFT);
				label.setBounds(spinner_x + spinner_w + 10, spinner_y, label_w, label_h);
				String unitsText = "";
				if (scalar.getUnits() != "")
					unitsText = " (" + scalar.getUnits() + ")";
				label.setText(scalar.getName() + unitsText);
				label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				
				
				
				spinner.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						if (spinner.getText() != "") {
							int newvalue = Integer.parseInt(spinner.getText());
							dmb.changeValue(selectedDevice, property, newvalue);	
						}
								
						/*
						for (Button devButton : devButtons.keySet()) {
							if (devButtons.get(devButton).equals(device)) {
								changeImage(devButton, device, property);
								break;
							}
								
						}
						*/
						
						
						
					}
				});
				
				
				Button ok = new Button(propGroup, SWT.NONE);
				ok.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						
						dmb.changeValue(device, property, spinner.getSelection());
						loadValue(device, property);
						spinner.setSelection(dmb.readValue(device, property));
							
						
						/*
						dmb.commitChanges(selectedDevice, property.getId(), property.getValueType());

						for (Button devButton : devButtons.keySet()) {
							Device device = devButtons.get(devButton);
							
							if (changedDevices.containsKey(device)) {
								for (Property property : changedDevices.get(device).keySet()) {
									changeImage(devButton, device, property);
								}
							}			
								
						}
						
						populatePreview(device);
						*/
					}
				});
				
				int ok_w = 50;
				int ok_h = 25;
				int ok_x = propGroup.getBounds().x + propGroup.getBounds().width - ok_w - 20;
				int ok_y = label.getBounds().y;
				ok.setBounds(ok_x, ok_y, ok_w, ok_h);
				ok.setText("Ok");
				
			}
			
			else if (type.equals(CLASS_ARRAY)) {
				
				Array array = (Array)valType;
				
				Text text = new Text(propGroup, SWT.BORDER);
				int text_x = 10;
				int text_y = 35;
				int text_w = 250;
				int text_h = 25;	
				text.setBounds(text_x, text_y, text_w, text_h);
				text.setTextLimit(array.getMaxLength());
				text.setText(dmb.readArray(device, property));
				
				Button ok = new Button(propGroup, SWT.NONE);
				ok.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDown(MouseEvent e) {
						String value = text.getText();
						dmb.changeValue(selectedDevice, property, value);
						
						text.setText(dmb.readArray(device, property));
						
						//populatePreview(device);
					}
				});
				
				int ok_w = 50;
				int ok_h = 25;
				int ok_x = propGroup.getBounds().x + propGroup.getBounds().width - ok_w - 20;
				int ok_y = text.getBounds().y;
				ok.setBounds(ok_x, ok_y, ok_w, ok_h);
				ok.setText("Ok");
				
			}		
			
		
			
			propGroup.setBounds(propGroup.getBounds().x, propGroup.getBounds().y, propGroup.getBounds().width, propGroup.getBounds().height + 25);
			
			int i_w = 150;
			int i_h = 25;
			int i_x = propGroup.getBounds().x;
			int i_y = propGroup.getBounds().height - 30;
			
			
			Button invalid = new Button(propGroup, SWT.CHECK);
			invalid.setBounds(i_x, i_y, i_w, i_h);
			invalid.setText("Invalid value");
			invalid.setSelection(dmb.isInvalid(device, property.getId()));
			invalid.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			//invalid.setEnabled(false);
			invalid.setToolTipText("Checked box means the value of this property cannot be considered valid.\nAn invalid device will be red in the workspace.");
			
			invalid.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					dmb.setInvalid(device, property.getId(), !invalid.getSelection());
					
				}
			});
			
			int prop_y_min = propGroup.getBounds().y + propGroup.getBounds().height;
			y = prop_y_min + PROPERTY_GROUP_INTERVAL;
		}
		
			
		
	}
	
	public void populatePreview(Device device) {

		previewList.removeAll();
		
		Map<Integer, Property> properties = device.getProperties();
		for (Property prop : properties.values()) {
			//System.out.println(prop.getName());
			String type = prop.getValueType().getClass().getSimpleName();
			if (type.equals(CLASS_ARRAY)) {
				String value = dmb.readArray(device, prop);
				previewList.add(prop.getName() + " : " + value);
			}
			else {
				int value = dmb.readValue(device, prop);
				previewList.add(prop.getName() + " : " + value);
			}
			
		}
	
	}
	
	private void populateDevices(Division division) {

		Map<Integer, Device> devices = dmb.getDevicesOfDivision(selectedDivision);
			
		populateList(devicesList, devices);
		
		Menu menu = new Menu(devicesList);
		devicesList.setMenu(menu);
			
		
		menu.addMenuListener(new MenuAdapter() {
	        public void menuShown(MenuEvent e) {
	        	
	            int selected = devicesList.getSelectionIndex();

	            if(selected < 0 || selected >= devicesList.getItemCount())
	                return;

	            MenuItem[] items = menu.getItems();
	            for (int i = 0; i < items.length; i++) {
	                items[i].dispose();
	            }
	            
	            MenuItem add = new MenuItem(menu, SWT.NONE);
	            add.setText("Add to workspace");
	            add.addSelectionListener(new SelectionAdapter() {
	    			@Override
	    			public void widgetSelected(SelectionEvent e) {
	    				String deviceName = devicesList.getItem(selected);
	    				
	    				for (Device device : devices.values())
	    					if (device.getName().equals(deviceName)) {
	    						workspace.put(device.getId(), device);
	    						updateWorkspace();
	    					}
	    				
	    			}
	    		});
	        }
	    });
		
		//populateButtons(devButtons, devices, devLabels);
	}
	
	public void updateWorkspace() {

		display.asyncExec(new Runnable() {

			@Override
			public void run() {
				if (!workspace.isEmpty())
					lblHelp.setVisible(false);
				else
					lblHelp.setVisible(true);
				
				populateButtons(devButtons, workspace);
				
				if (selectedDevice != null)
					populateProperties(selectedDevice, propGroups);
				
			}
			
		});
		
		/*if (!workspace.isEmpty())
			lblHelp.setVisible(false);
		else
			lblHelp.setVisible(true);
		
		populateButtons(devButtons, workspace);*/
		
	
	}


	private void populateButtons(Map<Button, Device> buttons, Map<Integer, Device> devices) {
		
		
		for (Button button : buttons.keySet()) {
			button.setVisible(false);
		}

		for (Label label : devLabels.values()) {
			label.setVisible(false);
		}
		
		
		buttons.clear();
		devLabels.clear();
		grpProperties.setText("Properties");
		
		if (devices.isEmpty())
			return;
		
		int x = DEVICE_STARTING_X;
		int y = DEVICE_STARTING_Y;
		int width = DEVICE_WIDTH;
		int height = DEVICE_HEIGHT;
		
		int interval = width + INTERVAL_BETWEEN_DEVICES_X;
		
		for (Device device : devices.values()) {
			Button button = new Button(grpDevices, SWT.TOGGLE);
						
			if (!grpDevices.getBounds().contains(x+width, y+height)) {
				x = DEVICE_STARTING_X;
				y += height + INTERVAL_BETWEEN_DEVICES_Y;
			}

			
			button.setBounds(x,y,width,height);
			button.setText("");
			
			button.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			/*Image img = device.getDeviceType().getDefaultImg();
			String imgPath = img.getPath();
			
			button.setImage(SWTResourceManager.getImage(imgPath));
			if (images.get(device) != null)
				button.setImage(SWTResourceManager.getImage(images.get(device).getPath()));
				
			*/
			
			for (Property property : device.getProperties().values()) {
				//System.out.println("\t" + property.getName());
				if (changeImage(button, device, property))
					break;
			}
			
			
			
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					
					if (selectedButton != null) {
						selectedButton.setSelection(false);
						selectedButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
					}
					
					selectedButton = button;
					
					if (dmb.hasInvalidProperty(device)) {
						selectedButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					} else {
						selectedButton.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					}
					
					
					selectedDevice = buttons.get(selectedButton);
					
					grpProperties.setText("Properties - " + device.getName());
					
					populateProperties(selectedDevice, propGroups);
					
				}
			});
			
			Menu menu = new Menu(button);
			button.setMenu(menu);				
			
			menu.addMenuListener(new MenuAdapter() {
		        public void menuShown(MenuEvent e) {
		        	
		        	MenuItem[] items = menu.getItems();
		        	if (items.length > 0)
		        		return;
		            
		            MenuItem remove = new MenuItem(menu, SWT.NONE);
		            remove.setText("Remove from workspace");
		            remove.addSelectionListener(new SelectionAdapter() {
		    			@Override
		    			public void widgetSelected(SelectionEvent e) {
		    				workspace.remove(device.getId());
		    				updateWorkspace();
		    				
		    			}
		    		});
		        }
		    });
			
			Label label = new Label(grpDevices, SWT.CENTER);
			int labelWidth = DEVICE_LABEL_WIDTH;
			int labelHeight = DEVICE_LABEL_HEIGHT;
					
			/*
			if (device.getName().length() > LABEL_THRESHOLD_WRAP)
				labelHeight += LABEL_HEIGHT;
			*/
			
			label.setBounds(x - 15, y + height + DEVICE_LABEL_INTERVAL, labelWidth, labelHeight);
			label.setText(device.getName()); 
			label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
			
			if (!dmb.hasInvalidProperty(device))
				label.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
			else
				label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
			

			buttons.put(button, device);

			devLabels.put(button, label);
			
			
			x += interval;
		}
	}
	

	
	private boolean changeImage(Button button, Device device, Property property) {
		
		String type = property.getValueType().getClass().getSimpleName();

		if (type.equals("Array")) {
			return false;
		}
		
			
		else {		
			
			
			
			int value = dmb.readValue(device, property);
			Image img = device.getDeviceType().getImage(value);
			
			
			if (img != null) {
				String imgPath = img.getPath();				
				if(dmb.hasInvalidProperty(device)) {
					
					imgPath = device.getDeviceType().getDefaultImg().getPath();
				}				
				
				button.setImage(SWTResourceManager.getImage(imgPath));
						
				images.put(device, img);
				return true;
	
				
			}
		}
		return false;
	}


	private void populateList(List list, Map<Integer, ?> map) {
		
		list.removeAll();
		
		if (map.isEmpty())
			return;
		
		Object[] elements = map.values().toArray();
		
		if (elements[0] instanceof House) {
			for (Object house : map.values()) {
				list.add(((House)house).getName());
			}
		}
		
		
		if (elements[0] instanceof Floor) {
			for (Object floor : map.values()) {
				list.add(((Floor)floor).getName());
			}
		}
		
		if (elements[0] instanceof Division) {
			for (Object div : map.values()) {
				list.add(((Division)div).getName());
			}
		}
		
		if (elements[0] instanceof Device) {
			for (Object dev : map.values()) {
				Device device = (Device)dev;
				String name = device.getName();
				list.add(name);
			}
		} 
				
	}
	
	
	private void updateSystemLabels() {
		String[] info;
		try {
			info = dmb.getSystemInfo();
			nameTxt.setText(info[1]);
			versionTxt.setText(info[2]);
			dateTxt.setText(info[3]);
		} catch (Exception e) {
		}
		
	}

	
	private void cleanAll() {
		floorsList.removeAll();
		divisionsList.removeAll();
		devicesList.removeAll();
		previewList.removeAll();
		workspace.clear();
		
		
		
		for (Button button : devButtons.keySet()) {
			button.setVisible(false);
		}
		
		for (Label label : devLabels.values()) {
			label.setVisible(false);
		}
		
		devButtons.clear();
		devLabels.clear();
		
		selectedHouse = null;
		selectedFloor = null;
		selectedDivision = null;
		selectedDevice = null;
		
		nameTxt.setText("");
		versionTxt.setText("");
		dateTxt.setText("");
		
	}
	
	public void setChangedDevices(Map<Device, Map<Property, Integer>> devices) {
		changedDevices = devices;
	}
	
	public Map<Integer, Device> getWorkspace() {
		return workspace;
	}
	
	public void loadValue(Device device, Property property) {
		dmb.commitChanges(device, property.getId(), property.getValueType());
		

		for (Button devButton : devButtons.keySet()) {
			Device dev = devButtons.get(devButton);
			
			if (changedDevices.containsKey(dev)) {
				
				for (Property prop : changedDevices.get(dev).keySet()) {
					if (changeImage(devButton, dev, prop))
						break;
				}
				
			}			
				
		}
		
		if (selectedDevice != null)
			populatePreview(selectedDevice);
	}
	
	public void invalidDevice(Device device, boolean invalid) {
		
		for (Button button : devButtons.keySet()) {
			
			if (device.equals(devButtons.get(button))) {
				if (invalid) {
					button.setBackground(SWTResourceManager.getColor(SWT.COLOR_RED));
					
					/*org.eclipse.swt.graphics.Image img = button.getImage();
					org.eclipse.swt.graphics.Image disable = new org.eclipse.swt.graphics.Image(display, img, SWT.IMAGE_DISABLE);
					button.setImage(disable);*/
					Label label = devLabels.get(button);
					label.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					
					changeImage(button, device, device.getProperty(1)); //property does not matter
				}
				else {
					if (dmb.hasInvalidProperty(device))
						return;
					button.setBackground(SWTResourceManager.getColor(SWT.COLOR_BLUE));
					Label label = devLabels.get(button);
					label.setForeground(SWTResourceManager.getColor(SWT.COLOR_BLACK));
					changeImage(button, device, device.getProperty(1));
				}
			}
			
			
		}
	}
	
	
	/*
	 * Updates the images and the text of the properties
	 * */
	public void updateImage(Device device, int property_id) {
				
		if (!workspace.containsKey(device.getId()))
			return;
		Button button = null;
		for (Button but : devButtons.keySet())
			if (devButtons.get(but).equals(device))
				button = but;
		

		changeImage(button, device, device.getProperty(property_id));
		
		
		if (selectedDevice != null)
			populateProperties(selectedDevice, propGroups);
		return;
		
	}
}
