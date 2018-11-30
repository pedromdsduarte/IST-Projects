/** @version $Id: Login.java,v 1.12 2014/11/30 17:14:10 ist179112 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;
import poof.textui.UserUnknownException;
// FIXME: import project-specific classes
import poof.Manager;

/**
 * §2.1.2.
 */
public class Login extends Command<Manager> /* FIXME: select core type for receiver */ {

	/**
	 * @param receiver
	 */
	public Login(Manager manager) {
		super(MenuEntry.LOGIN, manager , new ValidityPredicate<Manager>(manager){
				public boolean isValid(){
					return _receiver.isReady();
				}});
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() throws DialogException, IOException {
		try{
			String user = IO.readString(Message.usernameRequest());
			if(_receiver.userExists(user) == false)
				throw new UserUnknownException(user);
			_receiver.login(user);
		
			
		} catch (UserUnknownException e){throw e;}
	}
}
