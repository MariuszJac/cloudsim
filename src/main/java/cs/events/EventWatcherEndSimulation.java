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

package cs.events;

import iplatform.batch.ModelRunner;
import iplatform.model.interfaces.SimulationClockEvent;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;
import cs.dataexport.DataExport;
import cs.dataexport.ExportManager;
import cs.entities.stats.ConsumerStatsEvent;

public class EventWatcherEndSimulation implements SimulationClockEvent{

	static Logger logger = Logger.getLogger(ModelRunner.class);

	private int id = 0;
	//this is class used to configure the model
	private ModelInterface modelInterface = null;
	private ModelConfiguration modelConfiguration = null;
	
	public EventWatcherEndSimulation(ModelInterface modelInterfaceRef, int idRef)
	{
		this.id = idRef;
		
		this.modelInterface = modelInterfaceRef;
		this.modelConfiguration = modelInterface.getModelConfiguration();
	}
	
	@Override
	public void triggerEvent() {
		logger.info("---> end simulation event triggered by simplatform clock...");
		
		exportPerformanceStats();
		
		exportSimulationLogs();
		
		//exit the simulation as we received end simulation call
		System.exit(0);
	}

	private void exportPerformanceStats(){
		ArrayList<ConsumerStatsEvent> listConsumerStatsEvent = modelInterface.getPerformanceMonitor().getListConsumerStatsEvent();
		logger.info("============================== consumer stats ====================================");
		for(int i=0;i<listConsumerStatsEvent.size();i++) {
			ConsumerStatsEvent event = listConsumerStatsEvent.get(i);
			logger.info("tick: "+event.getTickNumber()+ 
					" S["+event.getNumberOfQueuedShortJobs()+
					":"+event.getNumberOfCompletedShortJobs()+"]"+
					" L["+event.getNumberOfQueuedLongJobs()+
					":"+event.getNumberOfCompletedLongJobs()+"]"
					);
		}
	}
	
	//finalise any necessary actions to export the simulation logs
	private void exportSimulationLogs()
	{
    	//set that we are in 'exit mode' and we want to stop the batch restart process
    	try 
    	{
    		//export model simulation logs
    		ExportManager exportManager = new ExportManager(modelInterface);
    		exportManager.exportData();	
		} 
    	catch (Exception e1) 
    	{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public int getEventId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void triggerEvent(int tickNumber, int tickTimeDurationInMilliseconds) {
		// TODO Auto-generated method stub
		
	}

}
