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
import iplatform.dataexport.interfaces.IDataExport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import cs.ModelInterface;

//this class is used to export model output into log files. 
//it is possible (using log4j configuration) to configure more than a single log file and output specific logs into separate files
//once the simulation finishes these logs are moved to results directory representing the single simulation run
public class DataExport {
	
	static Logger logger = Logger.getLogger(DataExport.class);

	private ModelInterface modelInterface = null;
	
	File serviceModelPerformanceLogFile = null;
	
	public DataExport(ModelInterface modelInterfaceRef)
	{
		this.modelInterface = modelInterfaceRef;
		logger.info("successfully initialised data export...");
		
    	//remove service performance log file
		try
		{
			removeFile();
		}
		catch(Exception exc)
		{
			logger.error("Error when removing model performance log: "+exc.toString());
			exc.printStackTrace();
		}
    	

		//this is callled at the beginnig of each simulation
		createPerformanceLogFile();
	}
	
	//checking if an integer needs a "0" in front or not
	private String correctFormat(int x)
	{
		if(x/10==0)
			return "0"+String.valueOf(x);
		else
			return String.valueOf(x);
	}
	
	private synchronized String getCurrentSimulationTime()
	{	
		String years = correctFormat(modelInterface.getSimulationClock().getCurrentTime().YEAR);
		String months = correctFormat(modelInterface.getSimulationClock().getCurrentTime().MONTH+1);
		String days = correctFormat(modelInterface.getSimulationClock().getCurrentTime().DAY_OF_MONTH);
		String hours = correctFormat(modelInterface.getSimulationClock().getCurrentTime().HOUR);
		String minutes = correctFormat(modelInterface.getSimulationClock().getCurrentTime().MINUTE);
			
		//the new time format according to ISO8601 - excluding the seconds
		//T stands for Time and Z denotes the UTC time format
		String time = "[Current simulation time: "+years+"-"+months+"-"+days+"T"+hours+":"+minutes+"Z]";
		
		return time;
	}

	public void createPerformanceLogFile()
	{
		String performanceLogFile = "";
		if(modelInterface.getSimulationClock().getToolConfiguration().getOperationMode() != 3)
		{
			performanceLogFile = getBinDirPath()+File.separator+"outputServiceModelPerformance.log";
			//performanceLogFile = "bin"+File.separator+"outputServiceModelPerformance.log";
		}
		else
		{
			//if running as a service - get it from the right dir
			performanceLogFile = getBinDirPath()+File.separator+"outputServiceModelPerformance.log";
		}
		
		logger.info("===> creating performance log file under following location: "+performanceLogFile);
		
		try
		{
			serviceModelPerformanceLogFile = new File(performanceLogFile);
			serviceModelPerformanceLogFile.createNewFile();
			System.out.println("created file: "+serviceModelPerformanceLogFile.getAbsolutePath());
		}
		catch(Exception exc)
		{
			exc.printStackTrace();
			logger.error("error: "+exc.toString());
		}
	}
	
	private void writeServiceLogLine(String line)
	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(serviceModelPerformanceLogFile, true));
			out.write(line);
			out.newLine();
			out.close();
		}
		catch (Exception e)
		{
			System.out.println("Exception ");		
		}
	}
	
	//============================== system-performance events ================================
	public synchronized void exportHourlyOutput(String statsLog)
	{
	    Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
	    appLogger.info(statsLog);
	    writeServiceLogLine(statsLog);
	    
	//store system performance output in file 
//	appLogger.info(getCurrentSimulationTime()+" [category: daily output" +
//			"] "+"[throughput: "+throughput+
//			"] [runningCost: "+runningCost+"]");
	}

	public synchronized void exportDailyOutput(String statsLog)
	{
	    Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
	    appLogger.info(statsLog);

//		//store system performance output in file 
//	    Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
//		appLogger.info(getCurrentSimulationTime()+" [category: daily output" +
//				"] "+"[throughput: "+throughput+
//				"] [runningCost: "+runningCost+"]");
	}

	public synchronized void exportMonthlyOutput(Hashtable table)
	{
	    Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
	    appLogger.info("test monthly...");

//		//store system performance output in file 
//		Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
//		appLogger.info(getCurrentSimulationTime()+" [category: monthly output" +
//				"] "+"[throughput: "+throughput+
//				"] [runningCost: "+runningCost+"]");
	}
	
	public synchronized void exportYearlyOutput(Hashtable table)
	{
	    Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
	    appLogger.info("test yearly...");

//		//store system performance output in file 
//		Logger appLogger = Logger.getLogger("SystemPerformanceOutput");
//		appLogger.info(getCurrentSimulationTime()+" [category: monthly output" +
//				"] "+"[throughput: "+throughput+
//				"] [runningCost: "+runningCost+"]");
	}
	
    private void removeFile()
    {
		String performanceLogFile = "";
		if(modelInterface.getSimulationClock().getToolConfiguration().getOperationMode() != 3)
		{
			performanceLogFile = getBinDirPath()+File.separator+"outputServiceModelPerformance.log";
			//performanceLogFile = "bin"+File.separator+"outputServiceModelPerformance.log";
		}
		else
		{
			//if running as a service - get it from the right dir
			performanceLogFile = getBinDirPath()+File.separator+"outputServiceModelPerformance.log";
		}
		
	    File f = new File(performanceLogFile);

	    try
	    {
    	    // Make sure the file or directory exists and isn't write protected
    	    if (!f.exists())
    	      throw new IllegalArgumentException(
    	          "Delete: no such file or directory: " + performanceLogFile);

    	    if (!f.canWrite())
    	      throw new IllegalArgumentException("Delete: write protected: "
    	          + performanceLogFile);

    	    // If it is a directory, make sure it is empty
    	    if (f.isDirectory()) {
    	      String[] files = f.list();
    	      if (files.length > 0)
    	        throw new IllegalArgumentException(
    	            "Delete: directory not empty: " + performanceLogFile);
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

	//when running in service this is required to get the real bin folder path under WEB-INF dir of the service
    public String getBinDirPath()
    {
    	String binDirPath = "";
    	try
    	{
    		File file = new File(DataExport.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
    		logger.info(">> ModelRunner class execution path is: "+file.getParent());
    		File upperPath = new File(file.getParent());
    		binDirPath = file.getParent();
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
    		File file = new File(DataExport.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
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
