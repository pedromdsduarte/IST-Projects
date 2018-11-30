/** @version $Id: CreateFile.java,v 1.7 2014/12/01 13:28:56 ist179112 Exp $ */
package poof.textui.shell;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.EntryExistsException;
import poof.textui.AccessDeniedException;
/**
 * §2.2.5.
 */
public class CreateFile extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public CreateFile(Manager manager) {
		super(MenuEntry.TOUCH, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			String name = IO.readString(Message.fileRequest());
			if( _receiver.getCurrentUsername() == "root" || _receiver.getCurrentDirectoryOwner() == _receiver.getCurrentUsername()
			|| _receiver.currentDirectoryPermission()){
			
				if( _receiver.entryExists(name))
					throw new EntryExistsException(name);
				else
					_receiver.createFile(name, _receiver.getCurrentUsername(), "false");
			}else{
				throw new AccessDeniedException(_receiver.getCurrentUsername());
			}
			}catch(AccessDeniedException e){throw e;}
			catch(EntryExistsException e){throw e;}
			
	}

}
