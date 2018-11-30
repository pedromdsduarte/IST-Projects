/** @version $Id: ChangeWorkingDirectory.java,v 1.4 2014/12/01 10:01:36 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryUnknownException;
import poof.textui.IsNotDirectoryException;

/**
 * §2.2.4.
 */
public class ChangeWorkingDirectory extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ChangeWorkingDirectory(Manager manager) {
		super(MenuEntry.CD, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		//FIXME: implement command
		try{
			String name = IO.readString(Message.directoryRequest());
			if(_receiver.entryExists(name) == false)
				throw new EntryUnknownException(name);
			if(_receiver.entryIsDirectory(name) == false)
				throw new IsNotDirectoryException(name);
			_receiver.changeDirectory(name);
		}catch(EntryUnknownException e){throw e;}
		catch(IsNotDirectoryException e){throw e;}
			
	}

}
