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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.gui.UserInterfaceManager;

public class MainFrame extends JFrame implements WindowListener {

	static Logger logger = Logger.getLogger(MainFrame.class);
	public MainModelControlPanel controlPanel = null;
	
	public ModelInterface modelInterface = null;
	public UserInterfaceManager modelUserInterfaceManager = null;
	
	//public MainModelPanel solarisDefaultMenuPanel = null;
	public JPanel mainPanel = null;
	public JMenuBar topMenuBar = null;

	public JPanel getMainPanel() {
		return mainPanel;
	}

	public void hideSingleStrategyPanel() {
		mainPanel.removeAll();
		update();
	}

	public MainFrame(ModelInterface modelInterfaceRef, UserInterfaceManager modelUserInterfaceManagerRef) {
		this.modelInterface = modelInterfaceRef;
		this.modelUserInterfaceManager = modelUserInterfaceManagerRef;
		
		addWindowListener(this);
	}

	public void loadMainPanel() {
		//loads top menu bar
		topMenuBar = new GlobalTopMenuBar(this, modelUserInterfaceManager);
		mainPanel.setLayout(new BorderLayout());

		//substitute menu bar withe the solaris one
		ModelMenuBar topMenuBar = new ModelMenuBar(this, modelUserInterfaceManager);
		this.topMenuBar = topMenuBar;
		this.setJMenuBar(topMenuBar);

		//solarisDefaultMenuPanel = new MainModelPanel(this);
		//add default menu panel
		//mainPanel.add(solarisDefaultMenuPanel, BorderLayout.CENTER);

		//add bottom solaris panel
		controlPanel = new MainModelControlPanel(this,modelInterface, modelUserInterfaceManager);
		mainPanel.add(controlPanel, BorderLayout.SOUTH);
	}

	public void init() {
		try {
			mainPanel = new JPanel();
			mainPanel.setLayout(new BorderLayout());
			add(mainPanel);

			//load default start page 
			loadMainPanel();
		} catch (Exception exc) {
			System.out.println("STH went wrong...");
			exc.printStackTrace();
		}
	}

	public MainModelControlPanel getControlPanel() {
		return controlPanel;
	}

	public void createJMainFrame() {
		init();
		setTitle("Interactive Community Simulation Tool v 1.0-beta-1 (IT Innovation Centre) ");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height - screenSize.height / 20);

		validate();                // Make sure layout is ok
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		show();
	}

	public void windowClosing(WindowEvent e) {
	}

	public void update() {
		getRootPane().updateUI();
		validate();
	}

	public void setSize(int width, int height) {
		logger.info("resizing: width=" + width + ", height=" + height);
		super.setSize(width, height);
		getRootPane().updateUI();
		validate();
	}

	public void windowClosed(WindowEvent e) {
		//This will only be seen on standard output.
	}

	public void windowOpened(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowGainedFocus(WindowEvent e) {
	}

	public void windowLostFocus(WindowEvent e) {
	}

	public void windowStateChanged(WindowEvent e) {
	}

	public ModelInterface getModelInterface() {
		return modelInterface;
	}
	
	
	
}
