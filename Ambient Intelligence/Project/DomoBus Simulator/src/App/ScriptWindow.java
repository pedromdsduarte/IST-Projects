package App;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Group;



public class ScriptWindow extends Dialog {

	protected Object result;
	protected Shell shlScript;
	
	private List scriptCommands;
	private Button runButton;
	private Button pauseButton;
	private Button continueButton;
	
	private int exec_point = 0;
	private boolean paused = false;
	private boolean pause_clicked = false;
	
	private static final String PAUSE = "p";
	private static final String SET = "s";
	private static final String WAIT = "w";
	
	private DomoBusBE dmb;
	private Display display;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public ScriptWindow(Shell parent, int style) {
		super(parent, style);
		setText("Script Controller");
	}
	
	public void setDmb(DomoBusBE dmb) {
		this.dmb = dmb;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shlScript.open();
		shlScript.layout();
		display = getParent().getDisplay();
		while (!shlScript.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlScript = new Shell(getParent(), SWT.CLOSE | SWT.TITLE);
		shlScript.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shlScript.setImage(SWTResourceManager.getImage(AboutWindow.class, "/resources/domobus.png"));
		shlScript.setSize(331, 420);
		shlScript.setText("Script Controller");
		
		Menu menu = new Menu(shlScript, SWT.BAR);
		shlScript.setMenuBar(menu);
		
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		//"Open file" menu
		MenuItem menuLoadScript = new MenuItem(menu_1, SWT.NONE);
		menuLoadScript.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shlScript, SWT.OPEN);
		        fd.setText("Open");
		        fd.setFilterPath(".");
		        String[] filterExt = { "*.txt"};
		        fd.setFilterExtensions(filterExt);
		        String selected = fd.open();
		        loadScript(selected);
			}
		        
		});
		menuLoadScript.setText("Load script...");
		
		scriptCommands = new List(shlScript, SWT.BORDER | SWT.V_SCROLL);
		scriptCommands.setBounds(10, 10, 306, 267);
		
		Group grpScriptControls = new Group(shlScript, SWT.NONE);
		grpScriptControls.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		grpScriptControls.setText("Script control");
		grpScriptControls.setBounds(10, 283, 306, 73);
		
		runButton = new Button(grpScriptControls, SWT.NONE);
		runButton.setBounds(10, 34, 90, 30);
		runButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				exec_point = 0;
				paused = false;
				pauseButton.setEnabled(true);
				continueButton.setEnabled(true);
				runScript();	//run script from the beginning
			}
		});
		runButton.setText("Run");
		
		pauseButton = new Button(grpScriptControls, SWT.NONE);
		pauseButton.setBounds(110, 34, 90, 30);
		pauseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pause_clicked = true;
				pauseScript();
			}
		});
		pauseButton.setText("Pause");
		pauseButton.setEnabled(false);
		
		continueButton = new Button(grpScriptControls, SWT.NONE);
		continueButton.setBounds(206, 34, 90, 30);
		continueButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				continueScript();
			}
		});
		continueButton.setText("Continue");
		continueButton.setEnabled(false);
		
	}
	
	
	

	protected void handleError(Exception exc, Shell shell, String errmsg) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
        
		//exc.printStackTrace();
		
        messageBox.setText("Error");
        messageBox.setMessage(errmsg);
        int buttonID = messageBox.open();
        switch(buttonID) {
          case SWT.OK:
        	  //
        }
	}
	
	
	private String removeComments(String line) {
		int	offset = line.indexOf("!");

		if (offset != -1) {
		    line = line.substring(0, offset);
		}
		return line;
	}
	
	///////////////////////////////////////////////////////////////////////////
      
	

	private void loadScript(String path) {

		if (path == null)
			return;
		
		File file = new File(path);
		
		scriptCommands.removeAll();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		       if (line.startsWith("!") || line.isEmpty())
		    	   continue;
		       scriptCommands.add(removeComments(line).trim());
		    }
		} catch (Exception e) {
			e.printStackTrace();
			handleError(e, shlScript, "Could not load script file");
		}
		
	}
	
	///////////////////////////////////////////////////////////////////////////

	
	private void runScript() {

		//System.out.println(" -- Exec point is " + exec_point);
		
		while (exec_point < scriptCommands.getItemCount()) {
			String command = scriptCommands.getItem(exec_point);
			
			System.out.println("Running command " + exec_point + ": " + command);
			
			scriptCommands.setSelection(exec_point);
			runCommand(command);
			exec_point++;
			if (paused) {
				break;
			}
		}
		
		if (exec_point == scriptCommands.getItemCount()) {
			System.out.println("Terminated execution");
			pauseButton.setEnabled(false);
			continueButton.setEnabled(false);
		}
		

	}
	
	
	
	private void continueScript() {
		paused = false;
		runScript();		
	}

	private void pauseScript() {
		paused = true;		
	}
	
	///////////////////////////////////////////////////////////////////////////

	
	private void runCommand(String command) {
		
		if (command.startsWith(PAUSE))
			doPause(command);
		
		else if (command.startsWith(SET))
			doSet(command);		
		
		else if (command.startsWith(WAIT)) 
			doWait(command);
		
	}
		
	///////////////////////////////////////////////////////////////////////////

	private void doWait(String command) {
		int waiting_time = Integer.parseInt(command.split(" ")[1]);
		/*try {
			pauseButton.setEnabled(false);
			Thread.sleep(waiting_time * 1000);
			pauseButton.setEnabled(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		*/
		
		doPause(command);		
		Runnable timer = new Runnable() {
			public void run() {
				if (!pause_clicked)
					continueScript();
				pause_clicked = false;
			}
		};
		display.timerExec(waiting_time * 1000, timer);
	}
	
	private void doPause(String command) {
		pauseScript();
	}
	
	private void doSet(String command) {
		try {
			dmb.handleSet(command.substring(2));
			dmb.updateWorkspace();
		} 
		catch (NullPointerException e) {
			handleError(e, shlScript, e.getMessage() + " (line " + exec_point + ")");
			doPause(command);
			e.printStackTrace();
			return;
		}
				
	}

	
}
