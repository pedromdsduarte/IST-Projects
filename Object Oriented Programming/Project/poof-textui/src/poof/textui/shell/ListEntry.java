/** @version $Id: ListEntry.java,v 1.5 2014/11/30 17:41:36 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryUnknownException;
/**
 * §2.2.2.
 */
public class ListEntry extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ListEntry(Manager manager) {
		super(MenuEntry.LS_ENTRY, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			String name = IO.readString(Message.nameRequest());
			if(_receiver.entryExists(name))
				IO.println(_receiver.listEntry(name));
			else
				throw new EntryUnknownException(name);
	
		}catch(EntryUnknownException e){throw e;}
	}

}
