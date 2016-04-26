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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.gui.UserInterfaceManager;

public class MainModelControlPanel extends JPanel {

	static Logger logger = Logger.getLogger(ModelInterface.class);
	private ModelInterface modelInterface = null;
	private UserInterfaceManager userInterfaceManager = null;
	
	JButton btnStart = new JButton("Start");
	JButton btnExitSimulation = new JButton("Exit Simulation");
	JButton btnPause = new JButton("Pause");
	JRadioButton simulationSpeed1 = new JRadioButton("1");
	JRadioButton simulationSpeed2 = new JRadioButton("2");
	JRadioButton simulationSpeed3 = new JRadioButton("3");
	JRadioButton simulationSpeed4 = new JRadioButton("4");
	JRadioButton simulationSpeed5 = new JRadioButton("5");
	JRadioButton simulationSpeed6 = new JRadioButton("6");
	JRadioButton simulationSpeed7 = new JRadioButton("7");
	JRadioButton simulationSpeed8 = new JRadioButton("8");
	JRadioButton simulationSpeed9 = new JRadioButton("9");
	ButtonGroup simulationSpeedRadioButtonGroup = new ButtonGroup();
	private double[] simulationSpeedTimings = new double[]{225, 125, 60, 30, 20, 10, 3, 1, 0};
	private int currentSimulationSpeedLevel = 6;

	public MainFrame wilma = null;
	public boolean isSetupRunning = false;
	boolean isDemoRunning = false;
	//initialise stats window object
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	//simulation speed toggle bar
	//model execution time - initial value is obtained from SystemClock object
	//tickDelay
	public static int simulationTickTimeIntervals = 100;
	JLabel lblModelExecutionSpeedLabel = new JLabel("Simulation Delay: ");
	JSlider modelExecutionSpeedToggle = new JSlider(0, 100, 10);
	int totalSystemResources = 1000;
	//use this to define proportion of resources between different services
	int resourcesScale = 100;

	public MainModelControlPanel(MainFrame localWilmaMain, ModelInterface modelInterfaceRef, UserInterfaceManager userInterfaceManagerRef) {
		wilma = localWilmaMain;
		this.modelInterface = modelInterfaceRef;
		this.userInterfaceManager = userInterfaceManagerRef;

		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void setSimulationSpeed(int speedLevel) {
		double simulationTickSpeed = simulationSpeedTimings[speedLevel - 1];
		modelInterface.getSimulationClock().setTickDelay((int)simulationTickSpeed);
		logger.info("simulation speed set to: " + speedLevel);

		if (speedLevel == 1) {
			simulationSpeed1.setSelected(true);
		} else if (speedLevel == 2) {
			simulationSpeed2.setSelected(true);
		} else if (speedLevel == 3) {
			simulationSpeed3.setSelected(true);
		} else if (speedLevel == 4) {
			simulationSpeed4.setSelected(true);
		} else if (speedLevel == 5) {
			simulationSpeed5.setSelected(true);
		} else if (speedLevel == 6) {
			simulationSpeed6.setSelected(true);
		} else if (speedLevel == 7) {
			simulationSpeed7.setSelected(true);
		} else if (speedLevel == 8) {
			simulationSpeed8.setSelected(true);
		} else if (speedLevel == 9) {
			simulationSpeed9.setSelected(true);
		}
	}

	public void createUpdateStatusDialog(String message) {
		JOptionPane.showMessageDialog(this, message);
	}

	void jbInit() throws Exception {
		simulationTickTimeIntervals = 100;
		lblModelExecutionSpeedLabel.setText("Simulation Delay (" + simulationTickTimeIntervals + "ms): ");
		modelExecutionSpeedToggle.setValue(simulationTickTimeIntervals);
		modelExecutionSpeedToggle.setPaintLabels(true);
		modelExecutionSpeedToggle.setMajorTickSpacing(10);
		modelExecutionSpeedToggle.setPaintTrack(true);
		modelExecutionSpeedToggle.setMinorTickSpacing(1);
		modelExecutionSpeedToggle.setPaintTicks(true);

		this.setLayout(new BorderLayout());
		JPanel subPanel1 = new JPanel();

		//set simulation speed
		simulationSpeedRadioButtonGroup.add(simulationSpeed1);
		simulationSpeedRadioButtonGroup.add(simulationSpeed2);
		simulationSpeedRadioButtonGroup.add(simulationSpeed3);
		simulationSpeedRadioButtonGroup.add(simulationSpeed4);
		simulationSpeedRadioButtonGroup.add(simulationSpeed5);
		simulationSpeedRadioButtonGroup.add(simulationSpeed6);
		simulationSpeedRadioButtonGroup.add(simulationSpeed7);
		simulationSpeedRadioButtonGroup.add(simulationSpeed8);
		simulationSpeedRadioButtonGroup.add(simulationSpeed9);

		subPanel1.setBorder(BorderFactory.createLoweredBevelBorder());
		//subPanel1.add(btnStart);
		subPanel1.add(new JLabel("Simulation speed:"));
		subPanel1.add(simulationSpeed1);
		subPanel1.add(simulationSpeed2);
		subPanel1.add(simulationSpeed3);
		subPanel1.add(simulationSpeed4);
		subPanel1.add(simulationSpeed5);
		subPanel1.add(simulationSpeed6);
		subPanel1.add(simulationSpeed7);
		subPanel1.add(simulationSpeed8);
		subPanel1.add(simulationSpeed9);
		//add pause button
		subPanel1.add(btnPause);
		//add exit button
		subPanel1.add(btnExitSimulation);

		btnPause.setEnabled(false);
		btnExitSimulation.setEnabled(false);

		//set initial simulation speed
		setSimulationSpeed(currentSimulationSpeedLevel);

		this.add(subPanel1, BorderLayout.SOUTH);
		this.setForeground(Color.BLACK);

		btnStart.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					//init simulation
					runModel();
				} catch (Exception exc) {
					exc.printStackTrace();
					logger.error(exc.toString());
				}
			}
		});

		btnExitSimulation.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					//export model simulation logs
					//stop the simulation and go back to the main panel
					System.exit(0);
				} catch (Exception exc) {
					exc.printStackTrace();
					logger.error(exc.toString());
				}
			}
		});

		btnPause.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (modelInterface.getSimulationClock().isSimulationPaused()) {
					btnPause.setText("Pause");
					modelInterface.getSimulationClock().setSimulationPaused(false);
				} else if (!modelInterface.getSimulationClock().isSimulationPaused()) {
					btnPause.setText("Unpause");
					modelInterface.getSimulationClock().setSimulationPaused(true);
				}
			}
		});

		simulationSpeed1.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed1.isSelected()) {
					currentSimulationSpeedLevel = 1;
					setSimulationSpeed(1);
				}
			}
		});

		simulationSpeed2.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed2.isSelected()) {
					currentSimulationSpeedLevel = 2;
					setSimulationSpeed(2);
				}
			}
		});

		simulationSpeed3.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed3.isSelected()) {
					currentSimulationSpeedLevel = 3;
					setSimulationSpeed(3);
				}
			}
		});

		simulationSpeed4.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed4.isSelected()) {
					currentSimulationSpeedLevel = 4;
					setSimulationSpeed(4);
				}
			}
		});

		simulationSpeed5.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed5.isSelected()) {
					currentSimulationSpeedLevel = 5;
					setSimulationSpeed(5);
				}
			}
		});

		simulationSpeed6.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed6.isSelected()) {
					currentSimulationSpeedLevel = 6;
					setSimulationSpeed(6);
				}
			}
		});

		simulationSpeed7.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed7.isSelected()) {
					currentSimulationSpeedLevel = 7;
					setSimulationSpeed(7);
				}
			}
		});

		simulationSpeed8.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed8.isSelected()) {
					currentSimulationSpeedLevel = 8;
					setSimulationSpeed(8);
				}
			}
		});

		simulationSpeed9.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (simulationSpeed9.isSelected()) {
					currentSimulationSpeedLevel = 9;
					setSimulationSpeed(9);
				}
			}
		});


		modelExecutionSpeedToggle.addMouseListener(new MouseListener() {

			public void mouseClicked(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mouseReleased(MouseEvent arg0) {
				int currentSimulationTimeIntervals = (int) modelExecutionSpeedToggle.getValue() * 10;
				//set this interval to params object
				if (currentSimulationTimeIntervals == 0) {
					currentSimulationTimeIntervals = 1;
				}
				modelInterface.getSimulationClock().setTickDelay(currentSimulationTimeIntervals);
				//System.out.println("New model execution time intervals speed set: "+DCommsModel.getModel(wilma,thisPanel).params.currentModelSimulationTimeInterval);
				lblModelExecutionSpeedLabel.setText("Simulation Delay (" + currentSimulationTimeIntervals + "ms): ");
			}

			public void mousePressed(MouseEvent e) {
			}
		});

		//create main panel displaying tabs
		showSimulationSolarisPanel();

		logger.info("running model in batch mode...");
		
		//run the model automatically if in batch + GUI mode combination
		if (modelInterface.getSimulationClock().getToolConfiguration().getOperationMode() == 1 && modelInterface.getSimulationClock().getToolConfiguration().isShowGUI()) {
			runModel();
		} 
	}

	public void runModel() {
		btnStart.setEnabled(false);
		isDemoRunning = true;
		btnPause.setEnabled(true);
		btnExitSimulation.setEnabled(true);

		//start solaris model
		logger.info("Starting Simulation...");

		//disable top menu
		//wilma.topMenuBar.setVisible(false);
		//wilma.topMenuBar.updateUI();

		//initialise system clock
		modelInterface.getSimulationClock().setSimulationPaused(false);

		enableControlPanelUI(true);
	}

	public void showSimulationSolarisPanel() {
		JPanel mainPanel = wilma.getMainPanel();
		mainPanel.add("Center", loadStatsPanel());
		wilma.validate();
	}

	public JPanel loadStatsPanel() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JTabbedPane tab = new JTabbedPane();
		tab.setPreferredSize(new Dimension(screenSize.width, screenSize.height - screenSize.height / 20));

		//add tabbed pane
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(tab, BorderLayout.CENTER);

		tab.add("Startup Configuration", userInterfaceManager.initModelConfigurationPanel());

		tab.add("Community Performance Stats", userInterfaceManager.initPerformanceStatsPanel());

		return panel;
	}

	public void enableControlPanelUI(boolean enable) {
		modelExecutionSpeedToggle.setEnabled(enable);
	}

	public void hideMouseHelp() {
//		    mouseHelpPanel.setVisible(false);
	}

	public void showMouseHelp() {
//		    mouseHelpPanel.setVisible(true);
	}

	public void setMessage() {
//		    messageLabel.setText(defaultMessage);
	}

	public void setMessage(String msg) {
//		    messageLabel.setText(msg);
	}

	public void add(JPanel panel) {
//		    dialogPanel.add(panel, BorderLayout.CENTER);
		doLayout();
	}

	public void add(JButton button) {
//		    dialogPanel.add(button, BorderLayout.CENTER);
		doLayout();
	}
}
