/** @version $Id: CreateDirectory.java,v 1.5 2014/11/30 17:14:10 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryExistsException;
/**
 * §2.2.6.
 */
public class CreateDirectory extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public CreateDirectory(Manager manager) {
		super(MenuEntry.MKDIR, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		//FIXME: implement command
		try{
			String name = IO.readString(Message.directoryRequest());
			if( _receiver.entryExists(name) ){
				throw new EntryExistsException(name);
			}else{
				_receiver.createDirectory(name,_receiver.getCurrentUsername());
			}
		}catch (EntryExistsException e) {throw e;}
		
	}
}
