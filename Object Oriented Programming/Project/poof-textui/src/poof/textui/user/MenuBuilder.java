/** @version $Id: MenuBuilder.java,v 1.2 2014/11/14 00:01:52 ist179112 Exp $ */
package poof.textui.user;

import ist.po.ui.Command;
import ist.po.ui.Menu;

// FIXME: import project-specific classes
import poof.Manager;
/**
 * Menu builder for search operations.
 */
public class MenuBuilder {

	/**
	 * @param receiver
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new CreateUser(manager),
				new ListAllUsers(manager),
				});
		menu.open();
	}

}
