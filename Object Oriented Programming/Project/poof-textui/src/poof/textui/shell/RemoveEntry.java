/** @version $Id: RemoveEntry.java,v 1.8 2014/12/01 15:37:19 ist179112 Exp $ */
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
import poof.textui.IllegalRemovalException;
/**
 * §2.2.3.
 */
public class RemoveEntry extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public RemoveEntry(Manager manager) {
		super(MenuEntry.RM, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		//FIXME: implement command
		try{
			String name = IO.readString(Message.nameRequest());
			if(_receiver.entryExists(name) == false)
				throw new EntryUnknownException(name);
			
			if ( _receiver.currentDirectoryPermission() &&  _receiver.getFilePermission(name)
			|| (_receiver.getCurrentUsername() == "root")
			|| ( _receiver.getCurrentDirectoryOwner() == _receiver.getCurrentUsername() && (_receiver.getFilePermission(name) || _receiver.getOwner(name) == _receiver.getCurrentUsername()))
			){
				if(_receiver.isHomedir(name))
					throw new AccessDeniedException(_receiver.getCurrentUsername());
				if(name.equals(".") || name.equals(".."))
					throw new IllegalRemovalException();
				
				_receiver.deleteEntry(name);
			}else{
				throw new AccessDeniedException(_receiver.getCurrentUsername());
			}		
		}catch(EntryUnknownException e){throw e;}
		catch(AccessDeniedException e){throw e;}
		catch(IllegalRemovalException e){throw e;}
	}
}
