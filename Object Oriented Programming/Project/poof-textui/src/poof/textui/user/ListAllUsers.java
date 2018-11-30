/** @version $Id: ListAllUsers.java,v 1.3 2014/11/14 05:40:47 ist179112 Exp $ */
package poof.textui.user;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;
import poof.Manager;
// FIXME: import project-specific classes

/**
 * §2.3.2.
 */
public class ListAllUsers extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ListAllUsers(Manager manager) {
		super(MenuEntry.LIST_USERS, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException  {
		IO.println(_receiver.listUsers());
	}
}
