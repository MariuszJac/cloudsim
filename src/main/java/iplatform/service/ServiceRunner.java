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

package iplatform.service;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationManager;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.config.ModelConfigurationTemplate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ServiceRunner implements Runnable {

	static Logger logger = Logger.getLogger(ModelRunner.class);

	//this class is used to read batch configuration files
	String simplatformConfigurationFilePath = ""; 
	String modelConfigurationFilePath = ""; 
			
    private ToolConfigurationTemplate toolConfiguration = null;
	
	//we obtain it from the information about there the lib folder is located in the service package
	private static String classPath = "";

    private static long heapSize = 1024;

	public ServiceRunner()
	{
		logger.debug("ServiceRunner class initialised...");
	}
	
	public String createSimulationJob(ToolConfigurationTemplate simplatformConfigurationTemplate, ModelConfigurationTemplate modelConfigurationTemplate)
	{
		//first remove any previous results from the results folder
		performResultsFolderCleanup(simplatformConfigurationTemplate);
		
		simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
		modelConfigurationFilePath = getBinDirPath()+File.separator+"batch"+File.separator + modelConfigurationTemplate.getTemplateName()+".txt";
		
		logger.info("reading files from locations: "+simplatformConfigurationTemplate.getModelClassToInstantiate());
		
		//generate job ref id and store it in one of the configurations
		String jobRefId = getCurrentDate()+"("+modelConfigurationTemplate.getTemplateName()+")";
		modelConfigurationTemplate.setJobRefId(jobRefId);
		
		//create batch folder if it does not exist
		File batchFolder = new File(getMainApplicationDirPath()+File.separator+simplatformConfigurationTemplate.getBatchFolderLocation());
		if(!batchFolder.exists())
		{
			logger.info("created batch folder...");
			batchFolder.mkdir();
		}
		
    	//read initial tool configuration from configuration.txt file
		logger.info("creating simplatform configuration file in the following location: "+simplatformConfigurationFilePath);
    	ToolConfigurationManager.getManager().generateToolConfigurationFile(simplatformConfigurationFilePath,simplatformConfigurationTemplate);
    	ToolConfigurationManager.getManager().generateModelConfigurationFile(modelConfigurationFilePath,modelConfigurationTemplate);
    	
    	//return job ref id
    	return jobRefId;
	}
	
	public boolean runSimulation()
	{
		boolean isSimulationStarted = false;
		simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
		String[] args = new String[]{"","",simplatformConfigurationFilePath};

		new ModelInstanceThread(args).start();
//		try
//		{
//			new ModelRunner(args);
//			isSimulationStarted = true;
//		}
//		catch(Exception exc)
//		{
//			exc.toString();
//			logger.error(exc.toString());
//			logger.info("simulation start failed...");
//			isSimulationStarted = false;
//		}
		
		return isSimulationStarted;
	}
	
	public boolean isJobCompleted(String jobRefId)
	{
		boolean jobCompleted = false;

		try
		{
			simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
	    	ToolConfigurationTemplate toolConfigurationTemplate = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);

			String resultsFolderPath = getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getResultsFolderLocation();
			File resultsFolder = new File(resultsFolderPath);
			logger.info("verifying if results for job with refId= "+jobRefId+" have been generated under following results folder location: "+resultsFolder.getAbsolutePath());
			
			String[] filesInFolder = resultsFolder.list();
			for(int i=0;i<filesInFolder.length;i++)
			{
				if(filesInFolder[i].equals("simulationStats"))
				{
					logger.info("identified job as completed...");
					jobCompleted = true;
				}
			}

		}
		catch(Exception exc)
		{
			logger.error(exc.toString());
		}
		
		return jobCompleted;
	}

	public boolean isJobStarted(String jobRefId)
	{
		boolean jobStarted = false;

		try
		{
			simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
	    	ToolConfigurationTemplate toolConfigurationTemplate = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);

			String resultsFolderPath = getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getResultsFolderLocation();
			File resultsFolder = new File(resultsFolderPath);
			logger.info("verifying if job with refId= "+jobRefId+" has started and there are results under following results folder location: "+resultsFolder.getAbsolutePath());
			
			String[] filesInFolder = resultsFolder.list();
			if(filesInFolder.length > 0)
			{
				jobStarted = true;
			}
		}
		catch(Exception exc)
		{
			logger.error(exc.toString());
		}
		
		return jobStarted;
	}

	public boolean isJobExists(String jobRefId)
	{
		boolean jobExists = false;

		try
		{
			simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
	    	ToolConfigurationTemplate toolConfigurationTemplate = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);

			String resultsFolderPath = getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getBatchFolderLocation();
			File batchFolder = new File(resultsFolderPath);
			logger.info("verifying if job with refId= "+jobRefId+" has been created in batch folder location: "+batchFolder.getAbsolutePath());
			
			String[] filesInFolder = batchFolder.list();
			if(filesInFolder.length >0)
			{
				jobExists = true;
			}
			else
			{
				jobExists = false;
			}
		}
		catch(Exception exc)
		{
			logger.error(exc.toString());
		}
		
		return jobExists;
	}

	public Object getSimulationResults(String jobRefId)
	{
		boolean jobCompleted = false;
		Object simulationOutput = null;
		
		try
		{
			simplatformConfigurationFilePath = getBinDirPath()+File.separator+"configuration(serviceMode).txt";
	    	ToolConfigurationTemplate toolConfigurationTemplate = ToolConfigurationManager.getManager().readInitialToolConfiguration(simplatformConfigurationFilePath);

			File resultsFolder = new File(getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getResultsFolderLocation()+File.separator+jobRefId);
			logger.debug("retrieving results of job with refId= "+jobRefId+" from the following results folder location: "+resultsFolder.getAbsolutePath());
			if(resultsFolder.exists() && resultsFolder.isDirectory())
			{
				String filePath  = getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getResultsFolderLocation()+File.separator+"simulationStats"+File.separator+toolConfigurationTemplate.getSimulationResultsFileName();
				logger.debug("directory exists - pulling results data from results file: "+filePath);
				simulationOutput = getSimulationOutputObject(filePath, toolConfigurationTemplate);
			}
		}
		catch(Exception exc)
		{
			logger.error(exc.toString());
		}
		
		//for now just fake that we extracted some probability
		return simulationOutput;
	}

	private String getCurrentDate()
    {
    	Date dateNow = new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	StringBuilder dateString = new StringBuilder(dateformat.format(dateNow));

        return dateString.toString();
    }

	public Object getSimulationOutputObject(String file, ToolConfigurationTemplate toolConfigurationTemplate)
	{
		Object simulationOutputObjectRecreated = null;
		Class<? extends SimulationOutput> theClass = null;
		
		try
		{
			String readJsonFileContent = readJsonFile(file);
			//logger.info(readJsonFileContent);
			//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
			Gson gson = new GsonBuilder().setPrettyPrinting().create();

			String theType = toolConfigurationTemplate.getSimulationResultsClassToInstantiate();
			theClass = Class.forName(theType).asSubclass(SimulationOutput.class);
			simulationOutputObjectRecreated = gson.fromJson(readJsonFileContent, theClass);
			//Class<? extends ModelConfigurationTemplate> modelTemplateRecreated = gson.fromJson(readJsonFileContent, theClass);
			
			SimulationOutput modelConfigurationTemplateRecreated = (SimulationOutput)simulationOutputObjectRecreated; 
			logger.info("simulation results class recreated from text configuration file under location: "+file);
		}
		catch(Exception exc)
		{
			logger.error("error: ",exc);
		}
		
		return simulationOutputObjectRecreated;
	}

	//capture in each time snapshot which services are offered by which providers
	public String readJsonFile(String fileName)
	{
		String fileLine="";
		String content = "";
		try{
			BufferedReader bw = getReader(fileName);
			while ((fileLine = bw.readLine()) != null)
			{
				content = content + fileLine;
			}

			logger.info(">>>>>>>> read configuration file: "+content);
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace(); 
		}
		
		return content;
	}

	public BufferedReader getReader(String fileLocation)
	{
		BufferedReader br = null;
        try 
        {
        	FileInputStream fis = new FileInputStream(new File(fileLocation));
        	InputStreamReader inputstreamreader = new InputStreamReader(fis);
        	br = new BufferedReader(inputstreamreader);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return br;
    }
	
	public void performResultsFolderCleanup(ToolConfigurationTemplate toolConfigurationTemplate)
	{
		File resultsFolder = new File(getMainApplicationDirPath()+File.separator+toolConfigurationTemplate.getResultsFolderLocation());
		File[] files = resultsFolder.listFiles();
		
		if(files != null && files.length >0)
		{
			for(int i=0;i<files.length;i++)
			{
				deleteFileSubfolders(files[i]);
				files[i].delete();
			}
		}
	}

	private void deleteFileSubfolders(File file)
	{
		File[] files = file.listFiles();
		if(files != null)
		{
			for(int i=0;i<files.length;i++)
			{
				files[i].delete();
			}
		}
	}
	
	//when running in service this is required to get the real bin folder path under WEB-INF dir of the service
    public String getBinDirPath()
    {
    	String binDirPath = "";
    	try
    	{
    		File file = new File(ServiceRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    		logger.info(">> ModelRunner class execution path is: "+file.getParent());
    		File upperPath = new File(file.getParent());
    		binDirPath = upperPath.getParent();
    		binDirPath = binDirPath + File.separator+"bin";
    		logger.info(">> bin dir path is: "+binDirPath);
    	}
    	catch(Exception exc)
    	{
    		logger.error(exc.toString());
    		exc.printStackTrace();
    	}
    	
		return binDirPath;
    }
    
	//when running in service this is required to get the folder path under WEB-INF dir of the service
    public String getMainApplicationDirPath()
    {
    	String binDirPath = "";
    	try
    	{
    		File file = new File(ServiceRunner.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
