/** @version $Id: Open.java,v 1.13 2014/11/29 15:37:56 ist179112 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import java.io.FileNotFoundException;
import poof.Manager;
/**
 * Open existing file.
 */
public class Open extends Command<Manager> /* FIXME: select core type for receiver */ {

	/**
	 * @param receiver
	 */
	public Open(Manager manager) {
		super(MenuEntry.OPEN, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		String filename = IO.readString(Message.openFile());
		try{		
			_receiver.openFileSystem(filename);
		}catch(FileNotFoundException e){IO.println(Message.fileNotFound(filename));}
		catch(ClassNotFoundException e){}
	}


}
