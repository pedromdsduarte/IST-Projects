/** @version $Id: ChangeOwner.java,v 1.4 2014/12/01 13:28:56 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryUnknownException;
import poof.textui.AccessDeniedException;
import poof.textui.UserUnknownException;
/**
 * §2.2.11.
 */
public class ChangeOwner extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ChangeOwner(Manager manager) {
		super(MenuEntry.CHOWN, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			String name = IO.readString(Message.nameRequest());
			String username = IO.readString(Message.usernameRequest());
			
			if(_receiver.entryExists(name) == false)
				throw new EntryUnknownException(name);
			if(_receiver.userExists(username) == false)
				throw new UserUnknownException(name);
				
			if(_receiver.getCurrentUsername() == "root" ||  _receiver.getCurrentDirectoryOwner() == _receiver.getCurrentUsername())
				_receiver.changeOwner(name, username);
			else
				throw new AccessDeniedException(_receiver.getCurrentUsername());
				
				
				
		}catch (EntryUnknownException e){throw e;}
		catch(AccessDeniedException e){throw e;}
	}

}
