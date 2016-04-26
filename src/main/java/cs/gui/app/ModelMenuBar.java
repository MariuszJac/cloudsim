//########################################################################
//#
//# � University of Southampton IT Innovation Centre, 2011 
//# Copyright in this library belongs to the University of Southampton 
//# University Road, Highfield, Southampton, UK, SO17 1BJ 
//# This software may not be used, sold, licensed, transferred, copied 
//# or reproduced in whole or in part in any manner or form or in or 
//# on any media by any person other than in accordance with the terms 
//# of the Licence Agreement supplied with the software, or otherwise 
//# without the prior written consent of the copyright owners.
//#
//# This software is distributed WITHOUT ANY WARRANTY, without even the 
//# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, 
//# except where stated in the Licence Agreement supplied with the software.
//#
//#	Created By :			Mariusz Jacyno
//#	Created Date :			2011-08-05
//#	Created for Project :	ROBUST
//#
//########################################################################

package cs.gui.app;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import cs.gui.UserInterfaceManager;

public class ModelMenuBar extends JMenuBar {

	MainFrame wilma = null;
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	public ModelMenuBar(MainFrame wilma, UserInterfaceManager userInterfaceManagerRef) {
		this.wilma = wilma;
		GlobalTopMenuBar menuBar = new GlobalTopMenuBar(wilma, userInterfaceManagerRef);
		menuBar.constructGlobalMenuItems(menu, this);
		constructGlobalMenuItems(menu, this);
	}

	public void constructGlobalMenuItems(JMenu menu, JMenuBar menuBar) {
	}
}
