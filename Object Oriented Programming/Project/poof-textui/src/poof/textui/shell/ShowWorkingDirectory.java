/** @version $Id: ShowWorkingDirectory.java,v 1.3 2014/11/25 17:59:38 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
/**
 * §2.2.7.
 */
public class ShowWorkingDirectory extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ShowWorkingDirectory(Manager manager) {
		super(MenuEntry.PWD, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
		IO.println(_receiver.showCurrentPath());
	}

}
