/** @version $Id: New.java,v 1.14 2014/12/01 13:28:56 ist179112 Exp $ */
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
 * Open a new file.
 */
public class New extends Command<Manager>{
	/**
	 * @param receiver
	 */
	public New(Manager manager) {
		super(MenuEntry.NEW, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		if(_receiver.isReady() && _receiver.fileSystemChanged()){
			if(IO.readBoolean(Message.saveBeforeExit())){
				try{
					_receiver.saveFileSystem("");
				}catch(FileNotFoundException e){	
					String file= IO.readString(Message.newSaveAs());
					_receiver.saveFileSystem(file);
				}
			}
		}
		_receiver.createFileSystem();
	}

	
}
