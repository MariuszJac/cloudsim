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

package cs.gui.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.gui.UserInterfaceManager;
import cs.gui.app.MainModelControlPanel;
import cs.gui.stats.StatsManager;

public class SetupPanelManager {

	static Logger logger = Logger.getLogger(SetupPanelManager.class);
	ModelInterface modelInterface = null;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private JTextField tbStartHistoryDataTime = new JTextField(6);

	private JComboBox cbForums = null;
	
	private JButton btnBootstrapModel = new JButton("Bootstrap model");
	private JButton btnStartSimulation = new JButton("Start simulation");
	
	public SetupPanelManager(ModelInterface modelInterfaceRef){
		this.modelInterface = modelInterfaceRef;
	}
	
	public JPanel createGraph() {
		Border storageConfigBorder = BorderFactory.createEtchedBorder(Color.GRAY, Color.GRAY);

		//set some default values
		tbStartHistoryDataTime.setText(modelInterface.getModelConfiguration().getNumberOfSimulationHours()+"");
		btnStartSimulation.setEnabled(false);
		cbForums = generateForumsList();
		
		int width = (screenSize.width - (screenSize.width / 35));
		int height = (int) (screenSize.height - (screenSize.height / (double) 4));

        JPanel mainFrame = new JPanel(new BorderLayout());
        mainFrame.setBorder(BorderFactory.createTitledBorder(storageConfigBorder, "Model configuration"));
        JPanel mainPanel1 =  new JPanel(new BorderLayout());
        
        //configure history data start - end time
        JPanel panel1 =  new JPanel(new BorderLayout());
        JPanel panelFlow1 = new JPanel(new FlowLayout());
        panelFlow1.add(tbStartHistoryDataTime);
        panel1.add(panelFlow1, BorderLayout.WEST);
        panel1.setBorder(BorderFactory.createTitledBorder(storageConfigBorder, "Number of days forecasted by simulation model"));


        //configure forums to simulate
        JPanel panel3 =  new JPanel(new BorderLayout());
        JPanel panelFlow3 = new JPanel(new FlowLayout());
        panelFlow3.add(new JLabel("Select forums to simulate: "));
        panelFlow3.add(cbForums);
        panel3.add(panelFlow3, BorderLayout.WEST);
        panel3.setBorder(BorderFactory.createTitledBorder(storageConfigBorder, "Forums to simulate"));

        mainPanel1.add(panel1, BorderLayout.NORTH);
        mainPanel1.add(panel3, BorderLayout.SOUTH);

        JPanel mainPanel2 =  new JPanel(new BorderLayout());
        
        //configure history data start - end time
        JPanel panel4 =  new JPanel(new BorderLayout());
        JPanel panelFlow4 = new JPanel(new FlowLayout());
        panelFlow4.add(btnBootstrapModel);
        panelFlow4.add(btnStartSimulation);
        panel4.add(panelFlow4, BorderLayout.WEST);
        panel4.setBorder(BorderFactory.createTitledBorder(storageConfigBorder, "Model initialisation"));

        mainPanel2.add(panel4, BorderLayout.NORTH);

        mainFrame.add(mainPanel1, BorderLayout.NORTH);
        mainFrame.add(mainPanel2, BorderLayout.CENTER);

		btnBootstrapModel.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					//update the user provided simulation time 
					updateSimulationStartTime();

					//update the user provided history data analysis time 
					updateHistoryDataAnalysisWindow();
					
					// create all initial users
					modelInterface.getManagerBootstrap().createProviders();
					btnStartSimulation.setEnabled(true);
					btnBootstrapModel.setEnabled(false);
					
				} catch (Exception exc) {
					exc.printStackTrace();
					logger.error(exc.toString());
				}
			}
		});

		btnStartSimulation.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					//init simulation
					MainModelControlPanel mainControlPanel = modelInterface.getManagerUserInterface().getMainFrame().getControlPanel();
					mainControlPanel.runModel();
					
					btnStartSimulation.setEnabled(false);
				} catch (Exception exc) {
					exc.printStackTrace();
					logger.error(exc.toString());
				}
			}
		});
		
        return mainFrame;
	}

	//this updates the current model configuration according to user input in the GUI
	private void updateSimulationStartTime()
	{
		try
		{
			modelInterface.getSimulationClock().setSimulationHours(modelInterface.getModelConfiguration().getNumberOfSimulationHours());
			logger.info("set number of simulation days to: "+modelInterface.getModelConfiguration().getNumberOfSimulationHours());
		}
		catch(Exception exc){
			logger.error(exc.toString());
			exc.printStackTrace();
		}
	}

	//this updates the current model configuration according to user input in the GUI
	private void updateHistoryDataAnalysisWindow()
	{
	}

	private JComboBox generateForumsList() {
		JComboBox cb = new JComboBox();
		cb.addItem("Service Oriented Architecture");
		
		return cb;
	}

}
