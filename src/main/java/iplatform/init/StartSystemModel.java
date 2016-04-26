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
//#	Created By :	 Mariusz Jacyno
//#	Created Date :	 2011-08-05
//#	Created for Project :	PrestoPRIME
//# Modifications for projects: ROBUST
//# Contact IT Innovation for details of individual project contributions
//#
//########################################################################

package iplatform.init;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationManager;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.SimulationModelSchema;
import iplatform.model.config.ModelConfigurationManager;
import iplatform.model.config.ModelConfigurationTemplate;
import iplatform.model.interfaces.IPlatformSetup;

import java.io.File;

import org.apache.log4j.Logger;

//this class is used to start a single instance of a system model
//in here batch (headless) mode can be used or GUI elements can be initialised that let the user interact with the model
public class StartSystemModel {

	static Logger logger = Logger.getLogger(ModelRunner.class);
	static String batchConfigurationTemplateFileName = ""; 
	static String batchConfigurationTemplateFilePath = ""; 
	static String toolConfigurationTemplateFilePath = ""; 
	static String strDataFolderNameForSingleSimulationRun = "";
	static String strDataFolderPathForSingleSimulationRun = "";
	
	SimulationClock clock = null;
	IPlatformSetup model = null;

 	public static void main(String[] args) 
	{
       	batchConfigurationTemplateFilePath = args[0];
       	toolConfigurationTemplateFilePath = args[1];
       	batchConfigurationTemplateFileName = args[2];
    	strDataFolderNameForSingleSimulationRun = args[3];
    	strDataFolderPathForSingleSimulationRun = args[4];
       	
    	logger.info("running in batch mode using batch configuration template path: "+batchConfigurationTemplateFilePath+" file name: "+batchConfigurationTemplateFileName);
      	logger.info("running in batch mode using tool configuration template: "+toolConfigurationTemplateFilePath);
      	logger.info("running in batch mode using data folder name for single simulation run: "+strDataFolderNameForSingleSimulationRun);
     	logger.info("running in batch mode using data folder path for single simulation run: "+strDataFolderPathForSingleSimulationRun);
     	 
    	//set batch mode configuration file
    	ToolConfigurationTemplate toolConfiguration = ToolConfigurationManager.getManager().readInitialToolConfiguration(toolConfigurationTemplateFilePath);
    	//initialise in batch mode in headless mode (without GUI)
        new StartSystemModel(true);
 	}
 	
 	public StartSystemModel(boolean runInHeadlessMode)
 	{
    	runModel(runInHeadlessMode);
 	}

 	private void initModel()
 	{
		  try
		  {
			  logger.info("initialising model...");
			  //load model configuration
			  ToolConfigurationTemplate toolConfiguration = ToolConfigurationManager.getManager().readInitialToolConfiguration(toolConfigurationTemplateFilePath);	
			  //String filePathToModelConfiguration = toolConfiguration.getBatchFolderLocation()+File.separator+batchConfigurationTemplateFileName;
					  
			  Object modelConfigurationTemplateObject = ModelConfigurationManager.getManager(toolConfigurationTemplateFilePath).getModelConfigurationFromFile(batchConfigurationTemplateFilePath);
			  ModelConfigurationTemplate modelConfiguration = (ModelConfigurationTemplate)modelConfigurationTemplateObject;	
			  
			  clock = new SimulationClock(toolConfiguration, modelConfiguration);
			  //set batch configuraiton file name as we need it during data export to correct folders
			  clock.batchConfigurationFilePath = batchConfigurationTemplateFilePath;
			  clock.batchConfigurationFileName = batchConfigurationTemplateFileName;
			  clock.simplatformConfigurationFilePath = toolConfigurationTemplateFilePath;
			  clock.strDataFolderNameForSingleSimulationRun = strDataFolderNameForSingleSimulationRun;
			  clock.strDataFolderPathForSingleSimulationRun = strDataFolderPathForSingleSimulationRun;
			  
			  //set number of simulation days (for how long the simulation will run)
			  clock.setSimulationHours(modelConfiguration.getNumberOfSimulationHours());
			  
			  //load model 
			  logger.info("instantiating model with a given class name: " + toolConfiguration.getModelClassToInstantiate());
			  String theType = toolConfiguration.getModelClassToInstantiate();
			  Class<? extends SimulationModelSchema> theClass = Class.forName(theType).asSubclass(SimulationModelSchema.class);
			  SimulationModelSchema simulationModelObject = theClass.newInstance();
			  SimulationModelSchema obj = theClass.cast(simulationModelObject);
			  model = (IPlatformSetup) obj;
			
			  //setup this run based on model configuration
			  model.setModelConfiguration(modelConfigurationTemplateObject);
			  model.setSimulationClock(clock);
			  model.registerEventWatchers(clock);
		  }
		  catch(Exception exc)
		  {
			  exc.printStackTrace();
			  logger.error(exc.toString());
			  //System.exit(0);
		  }
 	}
	  
 	//this method is used to run the model in batch (headless) mode
 	public void runModel(boolean runInHeadlessMode)
 	{
		//start the simulation
		logger.info("Starting Simulation...");
		//1. initialise the model code 
    	initModel();

		//2. set the simulation speed
		clock.setTickDelay(0);

		//3. run the clock
		clock.initialiseSimulationClock(runInHeadlessMode);
		logger.info("initialised model in batch mode...");
	  }
}
