/** @version $Id: MenuBuilder.java,v 1.2 2014/11/12 22:34:08 ist179112 Exp $ */
package poof.textui.main;

import ist.po.ui.Command;
import ist.po.ui.Menu;

// FIXME: import project-specific classes
import poof.Manager;
/**
 * Menu builder.
 */
public abstract class MenuBuilder {

	/**
	 * @param receiver
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new New(manager),
				new Open(manager),
				new Save(manager),
				new Login(manager),
				new MenuOpenShell(manager),
				new MenuOpenUserManagement(manager)
		});
		menu.open();
	}

}
