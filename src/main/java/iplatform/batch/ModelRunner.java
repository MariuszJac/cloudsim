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

package iplatform.batch;

import iplatform.config.ToolConfigurationManager;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.config.ModelConfigurationManager;
import iplatform.model.config.ModelConfigurationTemplate;
import iplatform.service.ServiceRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class ModelRunner
{
	static Logger logger = Logger.getLogger(ModelRunner.class);

	//this class is used to read batch configuration files
    private ToolConfigurationTemplate toolConfiguration = null;
	private String batchFolderLocation = "";
	private String resultsFolderLocation = "";
	private String bootstrapDataFolderLocation = "";

	private String batchTemplateConfigurationFile = "";
	private String batchTemplateConfigurationFilePath = "";

	private ArrayList<String> batchConfigurationFiles = null; 
	
	private static String operatingPlatform="windows";
    private static String classPath = "";
    private static String simplatformConfigurationFilePath = "";

    private static long heapSize = 1024;
    
    private boolean isRunningAsService = false;
    
    public static void main(String[] args) throws Exception
    {
        new ModelRunner(args);
    }

    public ModelRunner(String[] args) throws Exception
    {
    	if(args.length==3)
    	{
        	operatingPlatform = (String)args[0];
        	classPath = (String)args[1];
        	simplatformConfigurationFilePath = (String)args[2]; 
        	
        	logger.info("arguments passed: "+args.length+" operating platform: "+operatingPlatform+" class path: "+classPath+" simplatform configuration file (config.txt) path: "+simplatformConfigurationFilePath);

        	//read initial tool configuration from configuration.txt file
        	logger.info("reading simplatform configuration from path: "+simplatformConfigurationFilePath);
        	toolConfiguration = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);

        	//if running in 'service' mode set this as a flag
        	if(toolConfiguration.getOperationMode() == 3)
        	{
        		isRunningAsService = true;
        	}
        	
        	//getting the heapSize value from the file heapSize.txt
        	heapSize = toolConfiguration.getHeapSize();
        	//set batch folder location
        	batchFolderLocation = toolConfiguration.getBatchFolderLocation();

        	//if running in service mode - identify main root folder of the application (child folder of WEB-INF)
        	if(toolConfiguration.getOperationMode() == 3)
        	{
            	batchFolderLocation = getMainApplicationDirPath()+File.separator+toolConfiguration.getBatchFolderLocation();
            	logger.info(">>>> looking for model configuration template in following folder: "+batchFolderLocation);
        	}
        	
        	//set results folder location
        	resultsFolderLocation = toolConfiguration.getResultsFolderLocation();
        	
        	logger.info("batchFolderLocation: "+toolConfiguration.getBatchFolderLocation()+" results folder location: "+toolConfiguration.getResultsFolderLocation());
        	
        	batchConfigurationFiles = getBatchConfigurationFileNames();

        	logger.info("identified number of configuration files: "+batchConfigurationFiles.size());
        	
        	logger.info("starting the simulation...");
        	
        	//run model
        	run();
    	}
    	else
    	{
    		logger.info("Insiffucient number of paramters passed from batch file to the module...the program will exit...");
    	}
    }
    
    public void run() throws Exception
    {
        for(int i=0;i<batchConfigurationFiles.size();i++)
        {
        	runModel(batchConfigurationFiles.get(i));
        }

        //exit the tool once all configuration templates were processed
        logger.info("all simulations have completed, exiting simplatform...");
    }

    public void runModel(String batchFileName)
    {
 	   try
 	   {
 		   //set the file name
 		   batchTemplateConfigurationFile = batchFileName;

 		   //this should point to lib folder
	       String currentDir = getCurrentExecutionDir();
	       //if running in 'service' mode then override classpath as we don't pass this as an argument
		   if(isRunningAsService)
		   {
		   	  classPath = getLibraryClasspath();
			  batchTemplateConfigurationFilePath = getMainApplicationDirPath()+File.separator+toolConfiguration.getBatchFolderLocation()+File.separator+batchTemplateConfigurationFile;
	 		  bootstrapDataFolderLocation = getMainApplicationDirPath()+File.separator+toolConfiguration.getBootstrapDataFolderLocation();
		   }
		   else
		   {
			   //if running in normal mode - reuse this path
			   batchTemplateConfigurationFilePath = toolConfiguration.getBatchFolderLocation()+File.separator+batchTemplateConfigurationFile;
			   bootstrapDataFolderLocation = toolConfiguration.getBootstrapDataFolderLocation();
		   }

		   Object modelConfigurationTemplateObject = ModelConfigurationManager.getManager(simplatformConfigurationFilePath).getModelConfigurationFromFile(batchTemplateConfigurationFilePath);
		   ModelConfigurationTemplate modelConfigurationTemplate = (ModelConfigurationTemplate)modelConfigurationTemplateObject;	


			//File file = new File(bootstrapDataFolderLocation);
			//File[] folders = file.listFiles();
			//logger.info("files in bootstrap folder: "+folders.length);
			//for(int z=0;z<folders.length;z++)
			//{
				//String strDataFolderNameForSingleSimulationRun = folders[z].getName();
				//String strDataFolderPathForSingleSimulationRun = folders[z].getAbsolutePath();
				String strDataFolderNameForSingleSimulationRun = modelConfigurationTemplate.getTemplateName().replace(" ","");
				String strDataFolderPathForSingleSimulationRun = batchTemplateConfigurationFilePath;
				
				   for(int i=0;i<modelConfigurationTemplate.getNumberOfSimulationRepetitions();i++)
				   {   
					   logger.info("===========INDIVIDUAL SIMULATION STARTING======================");
					   logger.info("current execution dir: "+currentDir);
					   logger.info("bootstrap data dir: "+bootstrapDataFolderLocation);
					   logger.info("classpath used during simulation run: "+classPath);
					   logger.info("starting simulation using batch file located in: "+batchTemplateConfigurationFilePath);
					   logger.info("number of simulation repetitions: "+modelConfigurationTemplate.getNumberOfSimulationRepetitions());
					   logger.info("=>output data folder name: "+strDataFolderNameForSingleSimulationRun);
					   logger.info("=>output data folder path: "+strDataFolderPathForSingleSimulationRun);
					   	 logger.info("operating in batch mode, running model based on batch file: "+batchTemplateConfigurationFilePath+" iteration: "+i);
						   
					   	 Runtime rt = Runtime.getRuntime();
						 Process proc;
						 
						 String runCommand = "";
						 logger.info("running the model with maximum heap size allocation: "+heapSize);

						 runCommand = "java -Xmx"+heapSize+"m -cp "+classPath+" iplatform.init.StartSystemModel "+
								 batchTemplateConfigurationFilePath+" "+
								 simplatformConfigurationFilePath+" "+
								 batchFileName+" "+
								 strDataFolderNameForSingleSimulationRun+" "+
								 strDataFolderPathForSingleSimulationRun;

						 logger.info("running command: "+runCommand);
						 logger.info("===================================================");

						 proc = rt.exec(runCommand);
					       
						 StreamGobbler errorGobbler = new 
						 StreamGobbler(proc.getErrorStream(), "ERROR");            
						   
						   // any output?
						   StreamGobbler outputGobbler = new 
						   	StreamGobbler(proc.getInputStream(), "OUTPUT", null);
						       
						   // kick them off
						   errorGobbler.start();
						   outputGobbler.start();
						                           
						   // any error???
						   int exitVal = proc.waitFor();
						   errorGobbler.join();
						   outputGobbler.join();
						   
					       try
					       {
					    	   proc.getInputStream().close();
				           } 
					       catch ( IOException e ){}
				       
				           try
				           {
				        	   proc.getOutputStream().close();
				           } 
				           catch ( IOException e ){}

				           try 
				           {
				        	   proc.getErrorStream().close();
				           } 
				           catch ( IOException e ){ }
						       		   
						   logger.info("ExitValue: " + exitVal);
						 
						   if(exitVal != 0)
						   {
							   logger.error("There was an error running this model configuration, please verify the log file for reasons... exiting the simulation...");
							   throw new Exception();
						   }

						   Thread.sleep(5);
				   }
			//}
 	   }
 	   catch(Exception exc)
 	   {
 		   exc.printStackTrace();
 		   exc.toString();
 		   logger.error(exc.toString());
 	   }
    }
    
    private ArrayList<String> getBatchConfigurationFileNames()
    {
    	ArrayList<String> batchConfigurationFileNames = new ArrayList<String>();
    	
    	logger.info(">>> looking for configuration files in dir: "+batchFolderLocation);

    	File folder = new File(batchFolderLocation);
    	File[] listOfFiles = folder.listFiles();
 
    	for (int i = 0; i < listOfFiles.length; i++) 
    	{
    		if (listOfFiles[i].isFile()) 
    		{
    			String fileName = listOfFiles[i].getName();
    			logger.info("adding file: "+fileName+" to list of batch templates that will be executed by the model");
    			
    			batchConfigurationFileNames.add(fileName);
    		} 
    	}
    	
    	return batchConfigurationFileNames;
    }

    private String getLibraryClasspath()
    {
    	boolean isLinuxOS = isUnix();
    	logger.info(">> running on linux: "+isLinuxOS);
		String libClasspath = "";
    	ArrayList<String> batchConfigurationFileNames = new ArrayList<String>();
    	//get location of lib directory 
    	String libDir = getCurrentExecutionDir();
    	logger.info(">> looking for configuration files in dir: "+libDir);
    	
    	File folder = new File(libDir);
    	File[] listOfFiles = folder.listFiles();
 
    	if(!isLinuxOS)
    	{
        	for (int i = 0; i < listOfFiles.length; i++) 
        	{
        		if (listOfFiles[i].isFile()) 
        		{
        			String fileName = listOfFiles[i].getName();
        			libClasspath = libClasspath + libDir+File.separator+"\\"+fileName+";";
        			logger.info("adding library: "+fileName+" to the classpath used when initialising the model");
        		} 
        	}
    	}
    	else
    	{
        	for (int i = 0; i < listOfFiles.length; i++) 
        	{
        		if (listOfFiles[i].isFile()) 
        		{
        			String fileName = listOfFiles[i].getName();
        			libClasspath = libClasspath + libDir+File.separator+fileName+":";
        			logger.info("adding library: "+fileName+" to the classpath used when initialising the model");
        		} 
        	}
    	}
    	
    	return libClasspath;
    }
    
    public static boolean isUnix() {
    	 
		String os = System.getProperty("os.name").toLowerCase();
		// linux or unix
		return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    //returns current execution dir of ModelRunner class (we assume it is located in lib directory)
    public String getCurrentExecutionDir()
    {
    	String executionPath = "";
    	try
    	{
    		File file = new File(ModelRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    		logger.info(">> ModelRunner class execution path is: "+file.getParent());
    		executionPath = file.getParent();
    	}
    	catch(Exception exc)
    	{
    		logger.error(exc.toString());
    		exc.printStackTrace();
    	}
    	
		return executionPath;
    }
    
	//when running in service this is required to get the folder path under WEB-INF dir of the service
    public String getMainApplicationDirPath()
    {
    	String binDirPath = "";
    	try
    	{
    		File file = new File(ModelRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    		logger.info(">> ModelRunner class execution path is: "+file.getParent());
    		File upperPath = new File(file.getParent());
    		binDirPath = upperPath.getParent();
    		logger.info(">> bin dir path is: "+binDirPath);
    	}
    	catch(Exception exc)
    	{
    		logger.error(exc.toString());
    		exc.printStackTrace();
    	}
    	
		return binDirPath;
    }

 }
