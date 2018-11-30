/** @version $Id: Save.java,v 1.5 2014/11/29 15:37:56 ist179112 Exp $ */
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
 * Save to file under current name (if unnamed, query for name).
 */
public class Save extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public Save(Manager manager) {
		super(MenuEntry.SAVE, manager , new ValidityPredicate<Manager>(manager){
				public boolean isValid(){
					return _receiver.isReady();
				}
			});
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			_receiver.saveFileSystem("");
		}catch(FileNotFoundException e){
			String file = IO.readString(Message.newSaveAs());
			_receiver.saveFileSystem(file);
		}
	}
}
