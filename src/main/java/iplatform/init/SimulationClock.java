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
//#	Created for Project :	PrestoPRIME
//# Created for project: PrestoPRIME
//# Modifications for projects: ROBUST
//# Contact IT Innovation for details of individual project contributions
//########################################################################
package iplatform.init;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.model.config.ModelConfigurationTemplate;
import iplatform.model.interfaces.SimulationClockEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

public class SimulationClock extends Thread {

	static Logger logger = Logger.getLogger(ModelRunner.class);
	
	//this has to be passed from the main class that runs the all the simulations
	private Object memoryObject = null;
	
	public String batchConfigurationFilePath = "";
	public String batchConfigurationFileName = "";
	public String simplatformConfigurationFilePath = "";
	public String strDataFolderNameForSingleSimulationRun = "";
	public String strDataFolderPathForSingleSimulationRun = "";

	//TODO hash table (check/generate id's on a simplatform side)
	private ArrayList<SimulationClockEvent> registeredTickEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registered100msEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredMinuteEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredHourlyEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredDailyEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredMonthlyEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredYearlyEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredStartSimulationEvents = new ArrayList<SimulationClockEvent>();
	private ArrayList<SimulationClockEvent> registeredEndSimulationEvents = new ArrayList<SimulationClockEvent>();
	
	//this template will be used during model simulation
	private ModelConfigurationTemplate modelConfiguration = null;
	//this is where initial tool configuration parameters are stored
	private ToolConfigurationTemplate toolConfiguration = null;
	// ISO8601 without seconds
	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
	private GregorianCalendar currentDate, startDate, endDate = null;
	//public static SimulationClock simulationClock = null;
	//duration of month in days
	//private int daysInMonth = 30; 
	//set stats update time intervals for all charts (in ms)
	private long statsUpdateTimeIntervals = 500;
	//this is the time (in days) within which the accuracy of the model is considered as ok
	public int acceptableModelAccuracy = 1;
	public int currentSimulationTick = 0;

	private boolean simulationPaused = false;
	private int simulationHours = 0;
	
	//how long to sleep between ticks
	private int tickDelay = 0;
	
	// Set tick length directly in milliseconds
	public int realTickTimeDuration = 100;
	
	
	public SimulationClock(ToolConfigurationTemplate tConf, ModelConfigurationTemplate mConf) {
		//read initial tool configuration from configuration.txt file
		toolConfiguration = tConf;
		modelConfiguration = mConf;
		
		realTickTimeDuration = mConf.getTickTimeInMilliseconds();
	}
	
	public void initialiseSimulationClock(boolean runInHeadlessMode)
	{
		setDaemon(!runInHeadlessMode);
		start();
 	}
	
	private boolean stopBatchSimulation() {
		boolean stopSimulation = false;
		//System.out.println(currentDate.getTime().toGMTString()+" +++++++++++++++++++++++++++ "+endDate.getTime().toGMTString());

		if (currentDate.compareTo(endDate) >= 0) // true when we match or have passed endDate
		{
			stopSimulation = true;
		}
		return stopSimulation;
	}

	private void sleepTime(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			logger.error("Clock was interrupted whilst sleeping", e);
		}
	}

	@Override
	public void run() {
		//fire simulation start event
		fireStartSimulationEvent();

		while (true) {
			if (!isSimulationPaused()) {
				//each iteration models single tick (1 tick imitates 10 minutes of real time)
				iterateModelTick();
			} else {
				//if simulation is paused - sleep
				sleepTime(500);
				logger.debug("simulation paused, sleeping...");
			}
		}
	}

	private void iterateModelTick() {
		try {
			GregorianCalendar tickStart = (GregorianCalendar) currentDate.clone();
			currentSimulationTick++;
			currentDate.add(GregorianCalendar.MILLISECOND, realTickTimeDuration);
			//fire tick event
			fireTickEvents(currentSimulationTick, realTickTimeDuration);

			if (currentDate.get(GregorianCalendar.MINUTE) != tickStart.get(GregorianCalendar.MINUTE)) {
				fireMinuteEvents(currentSimulationTick, realTickTimeDuration);
			}

			if (currentDate.get(GregorianCalendar.HOUR_OF_DAY) != tickStart.get(GregorianCalendar.HOUR_OF_DAY)) {
				fireHourlyEvents(currentSimulationTick, realTickTimeDuration);
			}

			if (currentDate.get(GregorianCalendar.DAY_OF_MONTH) != tickStart.get(GregorianCalendar.DAY_OF_MONTH)) {
				fireDailyEvents(currentSimulationTick, realTickTimeDuration);
			}

			if (stopBatchSimulation()) {
				logger.info("stopping batch mode run as simulation end time condition was reached...");
				fireEndSimulationEvent();
			}

			//sleep(tickDelay);
		} catch (Exception exc) {
			logger.error("Clock generated an error whilst iterating a model tick", exc);
		}
	}

	//set events within the model that will be fired on the simulation end
	private void fireStartSimulationEvent() {
		//logger.debug("firing start simulation events...");
		for (SimulationClockEvent event : registeredStartSimulationEvents) {
			event.triggerEvent();
		}
	}

	private void fireEndSimulationEvent() {
		//logger.debug("firing end simulation events...");
		for (SimulationClockEvent event : registeredEndSimulationEvents) {
			event.triggerEvent();
		}
	}

	private void fireTickEvents(int tickNumber, int tickTimeDurationInMilliseconds) {
		//logger.debug("firing tick events...");
		for (SimulationClockEvent event : registeredTickEvents) {
			event.triggerEvent(tickNumber, tickTimeDurationInMilliseconds);
		}
	}

	private void fireMinuteEvents(int tickNumber, int tickTimeDurationInMilliseconds) {
		for (SimulationClockEvent event : registeredMinuteEvents) {
			event.triggerEvent(tickNumber, tickTimeDurationInMilliseconds);
		}
	}

	private void fireHourlyEvents(int tickNumber, int tickTimeDurationInMilliseconds) {
		//logger.debug("firing hourly events...");
		for (SimulationClockEvent event : registeredHourlyEvents) {
			event.triggerEvent(tickNumber, tickTimeDurationInMilliseconds);
		}
	}

	private void fireDailyEvents(int tickNumber, int tickTimeDurationInMilliseconds) {
		//logger.debug("firing daily events...");
		for (SimulationClockEvent event : registeredDailyEvents) {
			event.triggerEvent(tickNumber, tickTimeDurationInMilliseconds);
		}
	}

	public int getCurrentTick() {
		return currentSimulationTick;
	}

	public long getStatsUpdateTimeIntervals() {
		return statsUpdateTimeIntervals;
	}

	public long getTickDelay() {
		return tickDelay;
	}

	public boolean isSimulationPaused() {
		return simulationPaused;
	}

	public void setSimulationPaused(boolean paused) {
		simulationPaused = paused;
	}

	public double getRealTickTimeLengthInSeconds() {
		return realTickTimeDuration;
	}

	public ToolConfigurationTemplate getToolConfiguration() {
		return toolConfiguration;
	}

	public void setToolConfiguration(ToolConfigurationTemplate tConf) {
		toolConfiguration = tConf;
	}

	public void setTickDelay(int d) {
		tickDelay = d;
	}

	//events registration
	public void registerTickClockEvent(SimulationClockEvent eventWatcher) {
		registeredTickEvents.add(eventWatcher);
	}

	public void registerStartSimulationClockEvent(SimulationClockEvent eventWatcher) {
		registeredStartSimulationEvents.add(eventWatcher);
	}

	public void registerEndSimulationClockEvent(SimulationClockEvent eventWatcher) {
		registeredEndSimulationEvents.add(eventWatcher);
	}

	public void registerMinuteClockEvent(SimulationClockEvent eventWatcher) {
		registeredMinuteEvents.add(eventWatcher);
	}

	public void registerTenMinuteClockEvent(SimulationClockEvent eventWatcher) {
		registeredTickEvents.add(eventWatcher);
	}

	public void registerHourlyClockEvent(SimulationClockEvent eventWatcher) {
		registeredHourlyEvents.add(eventWatcher);
	}

	public void registerDailyClockEvent(SimulationClockEvent eventWatcher) {
		registeredDailyEvents.add(eventWatcher);
	}

	public void registerMonthlyClockEvent(SimulationClockEvent eventWatcher) {
		registeredMonthlyEvents.add(eventWatcher);
	}

	public void registerYearlyClockEvent(SimulationClockEvent eventWatcher) {
		registeredYearlyEvents.add(eventWatcher);
	}
	
	public GregorianCalendar getCurrentTime() {
		return currentDate;
	}
	
	public GregorianCalendar getStartTime() {
		return startDate;
	}

	public GregorianCalendar getEndTime() {
		return endDate;
	}
	
	public SimpleDateFormat getDateFormat(){
		return format;
	}
	
	//once new simulation instance is run - the memory object ref is passed to its simulation clock instance
	public void initMemoryObject(Object memoryObjectRef)
	{
		this.memoryObject = memoryObjectRef;
	}

	public int getSimulationHours() {
		return simulationHours;
	}

	public void setSimulationHours(int simulationHours) {
		this.simulationHours = simulationHours;
		startDate = new GregorianCalendar(2012, 1, 1);
		endDate = new GregorianCalendar(2012, 1, 1);
		endDate.add(Calendar.HOUR_OF_DAY, simulationHours);
		currentDate = startDate;
	}
	
	
}

