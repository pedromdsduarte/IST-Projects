/** @version $Id: CreateUser.java,v 1.6 2014/11/30 17:41:36 ist179112 Exp $ */
package poof.textui.user;


import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.UserExistsException;
import poof.textui.AccessDeniedException;
/**
 * §2.3.1.
 */
public class CreateUser extends Command<Manager> /* FIXME: select core type for receiver */ {
	/**
	 * @param receiver
	 */
	public CreateUser(Manager manager) {
		super(MenuEntry.CREATE_USER, manager);
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			String username = IO.readString(Message.usernameRequest());
			String name = IO.readString(Message.nameRequest());
			if(_receiver.getCurrentUsername() != "root")
				throw new AccessDeniedException(_receiver.getCurrentUsername());
			if(_receiver.userExists(username))
				throw new UserExistsException(username);
	
			_receiver.createUser(username,name);
		}catch (AccessDeniedException e){throw e;}
		catch (UserExistsException e){throw e;}
	}
}
