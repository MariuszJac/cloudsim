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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

import cs.gui.UserInterfaceManager;

public class GlobalTopMenuBar extends JMenuBar {

	MainFrame mainFrame = null;
	UserInterfaceManager modelUserInterfaceManager = null;
	
	JMenu menu, submenu;
	JMenuItem menuItem;
	JRadioButtonMenuItem rbMenuItem;
	JCheckBoxMenuItem cbMenuItem;

	public GlobalTopMenuBar(MainFrame mainFrameRef, UserInterfaceManager modelUserInterfaceManagerRef) {
		this.mainFrame = mainFrameRef;
		this.modelUserInterfaceManager = modelUserInterfaceManagerRef;
		
		constructGlobalMenuItems(menu, this);
	}

	public void constructGlobalMenuItems(JMenu menu, JMenuBar menuBar) {
		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");

		menuBar.add(menu);
		menuItem = new JMenuItem("Exit",
				KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_4, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menu.add(menuItem);

		menuItem.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					System.exit(0);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.exit(0);
			}
		});
		menuBar.add(menu);
	}

	public void showSolarisModelScreen() {
		JPanel mainPanel = mainFrame.getMainPanel();
		mainPanel.removeAll();

		//add default menu panel
		//mainPanel.add("Center", new MainModelPanel(wilma));

		//add bottom solaris panel
		mainPanel.add("South", new MainModelControlPanel(mainFrame, mainFrame.getModelInterface(),modelUserInterfaceManager));

		//substitute menu bar withe the solaris one
		loadSolarisMenuBar();

		mainFrame.update();
		System.out.println("Displayed Solaris panel...");
	}

	public void loadSolarisMenuBar() {
		ModelMenuBar topMenuBar = new ModelMenuBar(mainFrame, modelUserInterfaceManager);
		mainFrame.topMenuBar = topMenuBar;
		mainFrame.setJMenuBar(topMenuBar);
		mainFrame.validate();                // Make sure layout is ok
	}
}
