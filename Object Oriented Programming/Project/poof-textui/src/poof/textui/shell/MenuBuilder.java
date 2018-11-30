/** @version $Id: MenuBuilder.java,v 1.2 2014/11/14 00:01:51 ist179112 Exp $ */
package poof.textui.shell;

import ist.po.ui.Command;
import ist.po.ui.Menu;

// FIXME: import project-specific classes
import poof.Manager;
/**
 * Menu builder for shell operations.
 */
public class MenuBuilder {

	/**
	 * @param receiver
	 */
	public static void menuFor(Manager manager) {
		Menu menu = new Menu(MenuEntry.TITLE, new Command<?>[] {
				new ListAllEntries(manager),
				new ListEntry(manager),
				new RemoveEntry(manager),
				new ChangeWorkingDirectory(manager),
				new CreateFile(manager),
				new CreateDirectory(manager),
				new ShowWorkingDirectory(manager),
				new AppendDataToFile(manager),
				new ShowFileData(manager),
				new ChangeEntryPermissions(manager),
				new ChangeOwner(manager),
				});
		menu.open();
	}

}
