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

package cs.dataexport;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationManager;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.config.ModelConfigurationManager;
import iplatform.service.SimulationOutput;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cs.ModelInterface;
import cs.service.SimulationResults;

public class ExportManager 
{
	static Logger logger = Logger.getLogger(ExportManager.class);

	private ModelInterface modelInterface = null;
	private ToolConfigurationTemplate toolConfiguration = null;
	private String resultsDirectory = "results";

	boolean isRunningAsService = false;

	public ExportManager(ModelInterface modelInterfaceRef)
	{
		this.modelInterface = modelInterfaceRef;

		logger.info("export manager initialised...");
    	toolConfiguration = modelInterface.getSimulationClock().getToolConfiguration();
    	resultsDirectory = toolConfiguration.getResultsFolderLocation();    	
	}
	
    public void exportData()
    {
    	try
    	{
    		//check if we need to create results dir
    		createResultsDir();
    		
    		//create directory where export files are moved after the simulation run
    		createDataExporDirForSimulationRun();
    	}
    	catch(Exception exc)
    	{
    		exc.printStackTrace();
    		System.exit(0);
    	}
    }
    
    public void createResultsDir()
    {
    	
    	if(toolConfiguration.getOperationMode() == 3)
    	{
    		isRunningAsService = true;
    	}
    	
    	if(!isRunningAsService)
    	{
        	logger.info("verifying if results folder exists...");
        	
        	File file = new File(resultsDirectory);
        	if(!file.exists())
        	{
            	logger.info("creating dir: "+resultsDirectory);
            	new File(resultsDirectory).mkdir();
        	}
    	}
    	else
    	{
        	logger.info("verifying if results folder exists...");
        	
        	File file = new File(getMainApplicationDirPath()+File.separator+resultsDirectory);
        	if(!file.exists())
        	{
            	logger.info("creating dir: "+resultsDirectory);
            	new File(getMainApplicationDirPath()+File.separator+resultsDirectory).mkdir();
        	}
    	}
    }
    
    private void createDataExporDirForSimulationRun() throws Exception
    {
    	createResultsDir();
    	
    	String date = getCurrentDate();
    	
       	String exportPath = "";
    	if(toolConfiguration.getOperationMode() == 0 || toolConfiguration.getOperationMode() == 1)
    	{
    		exportPath = resultsDirectory+File.separator+modelInterface.getSimulationClock().strDataFolderNameForSingleSimulationRun+File.separator+date;
        	logger.info("creating export directory: "+exportPath);
        	new File(exportPath).mkdirs();
    	}
    	else if(toolConfiguration.getOperationMode() == 3)
    	{
    		if(getNumberOfCompletedSimulations() == 0)
    		{
        		exportPath = resultsDirectory+File.separator+modelInterface.getModelConfiguration().getJobRefId();
    		}
    		else
    		{
    			exportPath = getMainApplicationDirPath()+File.separator+resultsDirectory+File.separator+getCurrentDate()+"-"+modelInterface.getModelConfiguration().getJobRefId();
    		}
    		
        	logger.info("creating export directory for batch mode: "+exportPath);
        	new File(exportPath).mkdirs();
    	}

    	logger.info("export path is: "+exportPath);
    	
    	if(toolConfiguration.getOperationMode() == 3)
    	{
        	//copy log files to the dir
        	logger.info("copying file: outputModelLogs.log to following location (in bin directory: " +exportPath+File.separator+"outputModelLogs.log");
        	FileCopy.copy(getBinDirPath()+File.separator+"outputModelLogs.log", exportPath+File.separator+"outputModelLogs.log");

        	logger.info("copying file: outputSystemEvents.log to following location (in bin directory: " +exportPath+File.separator+"outputSystemEvents.log");
        	FileCopy.copy(getBinDirPath()+File.separator+"outputSystemEvents.log", exportPath+File.separator+"outputSystemEvents.log");
        	
        	logger.info("copying file: outputServiceModelPerformance.log to following location (in bin directory: " +exportPath+File.separator+"outputSystemPerformance.log");
        	FileCopy.copy(getBinDirPath()+File.separator+"outputServiceModelPerformance.log", exportPath+File.separator+"outputSystemPerformance.log");

        	//remove service performance log file
        	removeFile(getBinDirPath()+File.separator+"outputServiceModelPerformance.log");
        	
        	logger.info("copying file: outputUserEvents.log to following location (in bin directory: " +exportPath+File.separator+"outputUserEvents.log");
        	FileCopy.copy(getBinDirPath()+File.separator+"outputUserEvents.log", exportPath+File.separator+"outputUserEvents.log");
    	}
    	else if(toolConfiguration.getOperationMode() != 3)
    	{
        	//copy log files to the dir
        	logger.info("copying file: outputServiceModelPerformance.log to following location (in bin directory: " +exportPath+File.separator+"outputServiceModelPerformance.log");
        	FileCopy.copy("outputServiceModelPerformance.log", exportPath+File.separator+"outputServiceModelPerformance.log");

        	//remove service performance log file
        	removeFile(getBinDirPath()+File.separator+"outputServiceModelPerformance.log");

        	logger.info("copying file: outputModelLogs.log to following location (in bin directory: " +exportPath+File.separator+"outputModelLogs.log");
        	FileCopy.copy("outputModelLogs.log", exportPath+File.separator+"outputModelLogs.log");
        	
        	logger.info("copying file: outputSystemEvents.log to following location (in bin directory: " +exportPath+File.separator+"outputSystemEvents.log");
        	FileCopy.copy("outputSystemEvents.log", exportPath+File.separator+"outputSystemEvents.log");
        	
        	logger.info("copying file: outputSystemPerformance.log to following location (in bin directory: " +exportPath+File.separator+"outputSystemPerformance.log");
        	FileCopy.copy("outputSystemPerformance.log", exportPath+File.separator+"outputSystemPerformance.log");

        	logger.info("copying file: outputUserEvents.log to following location (in bin directory: " +exportPath+File.separator+"outputUserEvents.log");
        	FileCopy.copy("outputUserEvents.log", exportPath+File.separator+"outputUserEvents.log");
    	}

    	if(toolConfiguration.getOperationMode() == 1)
    	{
        	logger.info("copying file: "+toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName()+" to following location (in bin directory: " +exportPath+File.separator+getBatchConfigurationFileName());
        	FileCopy.copy(toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName(), exportPath+File.separator+getBatchConfigurationFileName());
        	
        	//remove model configuration from the batch folder as it was executed (only if required number of simulations was reached)
        	int numberOfFinishedSimulations = getNumberOfCompletedSimulations();
        	logger.info(">>> number of finished simulations: "+numberOfFinishedSimulations);
        	
        	/*
        	 * Used when all simulations finish and we want to do some statistical analysis across all experiments
        	if(modelInterface.getModelConfiguration().numberOfSimulationRepetitions == numberOfFinishedSimulations)
        	{
            	//perform results calculation at this stage
            	TRTStatsAnalysis trtStatsAnalysis = new TRTStatsAnalysis(File.separator+resultsDirectory, true);
            	double calculatedRiskProbability = trtStatsAnalysis.getCalculatedRiskProbability();
            	
             	//process generated results and create results object that will be returned from the service
            	//generate json represenation of simulation output file
            	SimulationResults simulationResults = new SimulationResults();
            	simulationResults.setJobRefId(modelInterface.getModelConfiguration().jobRefId);
            	simulationResults.setProbability(calculatedRiskProbability);
 
            	//set start and end simulation time
            	//calculate end day (assume we have 1 week prediction horizon!
            	GregorianCalendar startDate = new GregorianCalendar(modelInterface.getModelConfiguration().startYear, modelInterface.getModelConfiguration().startMonth, modelInterface.getModelConfiguration().startDay);
            	GregorianCalendar endDate = new GregorianCalendar(modelInterface.getModelConfiguration().startYear, modelInterface.getModelConfiguration().startMonth, modelInterface.getModelConfiguration().startDay);
            	endDate.add(Calendar.DAY_OF_MONTH, 7);
            	simulationResults.setStart(modelInterface.getModelConfiguration().startYear+"/"+modelInterface.getModelConfiguration().startMonth+"/"+modelInterface.getModelConfiguration().startDay);
            	//this is how it should be but we override this value!
            	//simulationResults.setEnd(modelInterface.getModelConfiguration().startYear+"-"+modelInterface.getModelConfiguration().startMonth+"-"+modelInterface.getModelConfiguration().startDay);
            	simulationResults.setEnd(endDate.get(Calendar.YEAR)+"/"+endDate.get(Calendar.MONTH)+"/"+endDate.get(Calendar.DAY_OF_MONTH));
            	
            	//TODO - use TRT class to process results
            	logger.info(">>>> generating results in folder: "+resultsDirectory);
            	new File(resultsDirectory+File.separator+"simulationStats").mkdir();
            	generateSimulationOutputFile(resultsDirectory+File.separator+"simulationStats"+File.separator+toolConfiguration.getSimulationResultsFileName(), simulationResults);
            	
        		logger.info("--> all simulations completed for job: "+modelInterface.getModelConfiguration().jobRefId+" removing configuration template from batch folder...");
            	//removeFile(toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName());
        	}
        	*/
    	}
    	
       	if(toolConfiguration.getOperationMode() == 3)
    	{
        	logger.info("copying file: "+getMainApplicationDirPath()+File.separator+toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName()+" to following location (in bin directory: " +exportPath+File.separator+getBatchConfigurationFileName());
        	FileCopy.copy(getMainApplicationDirPath()+File.separator+toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName(), exportPath+File.separator+getBatchConfigurationFileName());
        	
        	//remove model configuration from the batch folder as it was executed (only if required number of simulations was reached)
        	int numberOfFinishedSimulations = getNumberOfCompletedSimulations();
        	logger.info(">>> number of finished simulations: "+numberOfFinishedSimulations);
        	
        	/*
        	 * Used when all simulations finish and we want to do some statistical analysis across all experiments
        	if(modelInterface.getModelConfiguration().numberOfSimulationRepetitions == numberOfFinishedSimulations)
        	{
            	//perform results calculation at this stage
            	TRTStatsAnalysis trtStatsAnalysis = new TRTStatsAnalysis(getMainApplicationDirPath()+File.separator+resultsDirectory, true);
            	double calculatedRiskProbability = trtStatsAnalysis.getCalculatedRiskProbability();
            	
             	//process generated results and create results object that will be returned from the service
            	//generate json represenation of simulation output file
            	SimulationResults simulationResults = new SimulationResults();
            	simulationResults.setJobRefId(modelInterface.getModelConfiguration().jobRefId);
            	simulationResults.setProbability(calculatedRiskProbability);
 
            	//set start and end simulation time
            	//calculate end day (assume we have 1 week prediction horizon!
            	GregorianCalendar startDate = new GregorianCalendar(modelInterface.getModelConfiguration().startYear, modelInterface.getModelConfiguration().startMonth, modelInterface.getModelConfiguration().startDay);
            	GregorianCalendar endDate = new GregorianCalendar(modelInterface.getModelConfiguration().startYear, modelInterface.getModelConfiguration().startMonth, modelInterface.getModelConfiguration().startDay);
            	endDate.add(Calendar.DAY_OF_MONTH, 7);
            	simulationResults.setStart(modelInterface.getModelConfiguration().startYear+"/"+modelInterface.getModelConfiguration().startMonth+"/"+modelInterface.getModelConfiguration().startDay);
            	//this is how it should be but we override this value!
            	//simulationResults.setEnd(modelInterface.getModelConfiguration().startYear+"-"+modelInterface.getModelConfiguration().startMonth+"-"+modelInterface.getModelConfiguration().startDay);
            	simulationResults.setEnd(endDate.get(Calendar.YEAR)+"/"+endDate.get(Calendar.MONTH)+"/"+endDate.get(Calendar.DAY_OF_MONTH));
            	
            	//TODO - use TRT class to process results
            	logger.info(">>>> generating results in folder: "+getMainApplicationDirPath()+File.separator+resultsDirectory);
            	new File(getMainApplicationDirPath()+File.separator+resultsDirectory+File.separator+"simulationStats").mkdir();
            	generateSimulationOutputFile(getMainApplicationDirPath()+File.separator+resultsDirectory+File.separator+"simulationStats"+File.separator+toolConfiguration.getSimulationResultsFileName(), simulationResults);
            	
        		logger.info("--> all simulations completed for job: "+modelInterface.getModelConfiguration().jobRefId+" removing configuration template from batch folder...");
            	removeFile(getMainApplicationDirPath()+File.separator+toolConfiguration.getBatchFolderLocation()+File.separator+getBatchConfigurationFileName());
        	}
        	*/
    	}
 
    }
    
    //once data are exported - process them
    
    private String getBatchConfigurationFileName()
    {
    	String fileName = modelInterface.getSimulationClock().batchConfigurationFileName;
    	return fileName;
    }
    private String getCurrentDate()
    {
    	Date dateNow = new Date();
    	SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    	StringBuilder dateString = new StringBuilder(dateformat.format(dateNow));

        return dateString.toString();
    }
    
    private void removeFile(String filePath)
    {
    	  String fileName = filePath;
    	    // A File object to represent the filename
    	    File f = new File(fileName);

    	    try
    	    {
        	    // Make sure the file or directory exists and isn't write protected
        	    if (!f.exists())
        	      throw new IllegalArgumentException(
        	          "Delete: no such file or directory: " + fileName);

        	    if (!f.canWrite())
        	      throw new IllegalArgumentException("Delete: write protected: "
        	          + fileName);

        	    // If it is a directory, make sure it is empty
        	    if (f.isDirectory()) {
        	      String[] files = f.list();
        	      if (files.length > 0)
        	        throw new IllegalArgumentException(
        	            "Delete: directory not empty: " + fileName);
        	    }

        	    // Attempt to delete it
        	    boolean success = f.delete();

        	    if (!success)
        	      throw new IllegalArgumentException("Delete: deletion failed");
    	    }
    	    catch(Exception exc)
    	    {
    	    	logger.error(exc.toString());
    	    }
    }

	public void generateSimulationOutputFile(String fileName, Object object)
	{
		//store it into a file
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		//Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

		String jsonOutput = gson.toJson(object);
		//logger.info("json configuration: "+jsonOutput);
		
		//String currentDir = new File(".").getAbsolutePath();
		System.out.println("attempting to generate simulation results file with name: "+fileName+ " at location (working dir): "+getBinDirPath()+" content: "+jsonOutput);
		
		try
		{
			writeJsonStream(jsonOutput, fileName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void writeJsonStream(String output, String fileName) throws IOException 
	{
		String teamName = "";
		
		try
		{
			BufferedWriter bw = getWriter(fileName);
			String lineToWrite = "";
			bw.write( output+"\n");
			bw.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	  public BufferedWriter getWriter(String fileLocation)
	  {
		  BufferedWriter bw = null;
        try 
        {
            bw=new BufferedWriter(new FileWriter(fileLocation));
        } 
        catch (IOException e) 
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
        return bw;
	  }

	  public int getNumberOfCompletedSimulations()
	  {
		  File resultsDir = null;
		  String resultsDirPath = "";
		  if(isRunningAsService)
		  {
			  resultsDir = new File(resultsDirPath);
		  }
		  else
		  {
			  resultsDirPath = resultsDirectory;
		  }

		  resultsDir = new File(resultsDirPath);

		  logger.info("==> verifying number of completed simulations in directory: "+resultsDirPath+" is running as a service: "+isRunningAsService);

		  return resultsDir.list().length;
	  }
	  
		//when running in service this is required to get the real bin folder path under WEB-INF dir of the service
	    public String getBinDirPath()
	    {
	    	String binDirPath = "";
	    	try
	    	{
	    		File file = new File(ExportManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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
	    		File file = new File(ExportManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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
