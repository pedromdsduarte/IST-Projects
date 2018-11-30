/** @version $Id: MenuOpenShell.java,v 1.5 2014/11/14 00:52:16 ist179112 Exp $ */
package poof.textui.main;

import static ist.po.ui.Dialog.IO;
import ist.po.ui.Command;
import ist.po.ui.DialogException;
import ist.po.ui.ValidityPredicate;

import java.io.IOException;

// FIXME: import project-specific classes
import poof.Manager;
import poof.textui.shell.MenuBuilder;
/**
 * Open shell menu.
 */
public class MenuOpenShell extends Command<Manager>{

	/**
	 * @param receiver
	 */
	public MenuOpenShell(Manager manager) {
		super(MenuEntry.MENU_SHELL, manager, new ValidityPredicate<Manager>(manager){
				public boolean isValid(){
					return _receiver.isReady();
				}});
	}

	/** @see ist.po.ui.Command#execute() */
	@Override
	public final void execute() {
		poof.textui.shell.MenuBuilder.menuFor(_receiver);
	}

}
