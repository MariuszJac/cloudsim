package iplatform.service;

import iplatform.batch.ModelRunner;

import org.apache.log4j.Logger;

public class ModelInstanceThread extends Thread {

	static Logger logger = Logger.getLogger(ModelRunner.class);
	boolean isSimulationStarted = false;
	String[] simulationInputParameters = null;
	
	public ModelInstanceThread(String[] args)
	{
		logger.info("starting model instance thread...");
		simulationInputParameters = args;
	}

	public boolean getSimulationStartStatus()
	{
		return isSimulationStarted;
	}
	
	public void run()
	{
		logger.info("running model instance inside thread...");
		try
		{
			new ModelRunner(simulationInputParameters);
			isSimulationStarted = true;
		}
		catch(Exception exc)
		{
			exc.toString();
			logger.error(exc.toString());
			logger.info("simulation start failed...");
			isSimulationStarted = false;
		}
	}
}
