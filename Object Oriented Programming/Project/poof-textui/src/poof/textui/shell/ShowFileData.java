/** @version $Id: ShowFileData.java,v 1.5 2014/12/01 10:01:36 ist179112 Exp $ */
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
/**
 * §2.2.9.
 */
public class ShowFileData extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public ShowFileData(Manager manager) {
		super(MenuEntry.CAT,manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		String name = IO.readString(Message.fileRequest());
		try{
			
			if(_receiver.entryExists(name) == false)
				throw new EntryUnknownException(name);
			else if(_receiver.entryIsFile(name) == false)
				throw new IsNotFileException(name);
			else if( (_receiver.getContent(name)) != "")		
				IO.println(_receiver.getContent(name));
		
		}catch(EntryUnknownException e){throw e;}
		catch(IsNotFileException e){throw e;}
	}
}
