/** @version $Id: AppendDataToFile.java,v 1.5 2014/12/01 13:28:56 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryUnknownException;
import poof.textui.IsNotFileException;
import poof.textui.AccessDeniedException;

/**
 * §2.2.8.
 */
public class AppendDataToFile extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public AppendDataToFile(Manager manager) {
		super(MenuEntry.APPEND, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{	
		String name = IO.readString(Message.fileRequest());
		String text = IO.readString(Message.textRequest());
		if(_receiver.entryExists(name) == false)
			throw new EntryUnknownException(name);
		if(_receiver.entryIsFile(name) == false)
			throw new IsNotFileException(name);
		if(_receiver.getCurrentUsername() == "root" ||  _receiver.getCurrentDirectoryOwner() == _receiver.getCurrentUsername()
			|| _receiver.getFilePermission(name))
			_receiver.writeFile(name,text);
		else
			throw new AccessDeniedException(_receiver.getCurrentUsername());
		}catch(EntryUnknownException e){throw e;}
		catch(IsNotFileException e){throw e;}
		catch(AccessDeniedException e){throw e;}
	}
}
