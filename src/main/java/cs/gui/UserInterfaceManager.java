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

package cs.gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.gui.app.MainFrame;
import cs.gui.configuration.SetupPanelManager;
import cs.gui.stats.PerformanceStats;

public class UserInterfaceManager {

	protected static Logger logger = Logger.getLogger(ModelInterface.class);
	private ModelInterface modelInterface = null;
	
	private MainFrame mainFrame = null;
	private SetupPanelManager panelConfigurationManager = null;
	private PerformanceStats panelPerformanceStats = null;
	
	public UserInterfaceManager(ModelInterface modelInterfaceRef)
	{
		this.modelInterface = modelInterfaceRef;
		
		if (modelInterface.getSimulationClock().getToolConfiguration().getOperationMode() == 1) {
			logger.info("running in batch mode, show GUI: "+modelInterface.getSimulationClock().getToolConfiguration().isShowGUI());

			//initialise in batch mode but with GUI
			if (modelInterface.getSimulationClock().getToolConfiguration().isShowGUI()) {
				initFrame(modelInterface);
				//set the simulation speed
				//SystemClock.getClock().setTickDelay((long)0);
			} else {

				//initialise in batch mode but without gui
				//new StartArchiveSystemModel(true);
			}
		} else {
			//if running in other than batch mode - always show gui
			initFrame(modelInterface);
			//if GUI is used and we are not running in batch mode - pause the simulation until a user hits 'start' button
			modelInterface.getSimulationClock().setSimulationPaused(true);
		}
	}
	
	private void initFrame(ModelInterface modelInterface)
	{
		mainFrame = new MainFrame(modelInterface, this);
		mainFrame.createJMainFrame();
		mainFrame.setVisible(true);
	}

	public JPanel initModelConfigurationPanel()
	{
		//init performance stats panel
		panelConfigurationManager = new SetupPanelManager(modelInterface);
		logger.info("initialised performance stats panel...");

		return panelConfigurationManager.createGraph();
	}

	public JPanel initPerformanceStatsPanel()
	{
		//init performance stats panel
		panelPerformanceStats = new PerformanceStats(modelInterface, modelInterface.getManagerStats());
		logger.info("initialised performance stats panel...");

		return panelPerformanceStats;
	}

	public PerformanceStats getPanelPerformanceStats() {
		return panelPerformanceStats;
	}

	public MainFrame getMainFrame() {
		return mainFrame;
	}
}
