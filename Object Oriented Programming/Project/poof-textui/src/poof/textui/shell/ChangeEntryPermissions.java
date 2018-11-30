/** @version $Id: ChangeEntryPermissions.java,v 1.4 2014/12/01 13:18:54 ist179112 Exp $ */
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
 * §2.2.10.
 */
public class ChangeEntryPermissions extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ChangeEntryPermissions(Manager manager) {
		super(MenuEntry.CHMOD, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		//FIXME: implement command
		try{
			String entry = IO.readString(Message.nameRequest());
			String anwser = IO.readString(Message.publicAccess());
			String permission = "";
			if(_receiver.entryExists(entry) == false)
				throw new EntryUnknownException(entry);
			if (anwser.equals("s")) 
				permission = "public";
			else
				permission = "private";
			
			_receiver.changePermission(entry,permission);
		}catch (EntryUnknownException e){throw e;}
	}

}
