/** @version $Id: MenuOpenUserManagement.java,v 1.3 2014/11/14 01:23:26 ist179112 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
/**
 * Open user management menu.
 */
public class MenuOpenUserManagement extends Command<Manager> /* FIXME: select core type for receiver */ {

	/**
	 * @param receiver
	 */
	public MenuOpenUserManagement(Manager manager) {
		super(MenuEntry.MENU_USER_MGT, manager, new ValidityPredicate<Manager>(manager){
				public boolean isValid(){
					return _receiver.isReady();
				}});
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
		poof.textui.user.MenuBuilder.menuFor(_receiver);
	}

}
