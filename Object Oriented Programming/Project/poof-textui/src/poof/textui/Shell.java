
package poof.textui;

import static ist.po.ui.Dialog.IO;

import java.io.IOException;
import java.io.FileNotFoundException;

import poof.Manager;


/**
 * Class that starts the application's textual interface.
 */
public class Shell {
	/**
	 * @param args
	 */
	@SuppressWarnings("nls")
	public static void main(String[] args) {
		Manager manager = new Manager();
		
		String datafile = System.getProperty("import"); //$NON-NLS-1$
		try {
			if (datafile != null) {
				manager.processInputFile(datafile);
			}

		}
		catch(FileNotFoundException e) {}
		catch(IOException e){}	

		poof.textui.main.MenuBuilder.menuFor(manager);
		IO.closeDown();

			
		}
	

}


