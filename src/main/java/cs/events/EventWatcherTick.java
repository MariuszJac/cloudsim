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
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;
import cs.dataexport.DataExport;
import cs.entities.ConsumerInt;
import cs.entities.Job;
import cs.entities.ProviderInt;
import eu.robust.simulation.entities.User;

public class EventWatcherTick implements SimulationClockEvent{

	static Logger logger = Logger.getLogger(ModelRunner.class);

	private ModelInterface modelInterface = null;
	private int id = 0;

	//this is class used to configure the model
	private ModelConfiguration modelConfiguration = null;
	
	public EventWatcherTick(ModelInterface modelInterfaceRef, int idRef)
	{
		this.id = idRef;
		
		this.modelInterface = modelInterfaceRef;
		this.modelConfiguration = modelInterface.getModelConfiguration();
	}
	
	@Override
	public int getEventId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void triggerEvent(int tickNumber, int realTickTimeDuration) {
		logger.debug("---> tick event triggered by simplatform clock: currentTickNumber: "+tickNumber+
				" tickTimeDuration: "+realTickTimeDuration+
				" Consumers(SJ): "+modelConfiguration.getNumberOfConsumersForShortJobs()+
				" Consumers(LJ): "+modelConfiguration.getNumberOfConsumersForLongJobs()+
				" Providers: "+modelConfiguration.numberOfProviders);

		generateWorkload(realTickTimeDuration, tickNumber);
		activateConsumers(realTickTimeDuration, tickNumber);
		activateProviders(realTickTimeDuration, tickNumber);
		
		//update performance stats
		if(modelInterface.isGUIEnabled())
		{
			modelInterface.getManagerUserInterface().getPanelPerformanceStats().updatePlot();
		}
	}

	/**
	 * generate a list of new jobs each tick time
	 * randomly distribute these jobs randomly across schedulers
	 */
	private void generateWorkload(int tickTimeDuration, int currentSimulationTick) {
		//handle workload generation for short jobs
		List<ConsumerInt> listConsumersShortJobs = modelInterface.getListConsumersShortJobs();
		ArrayList<Job> listJobsWorkloadForShortJobs = modelInterface.getWorkloadGenerator().generateWorkloadForShortJobs(tickTimeDuration, currentSimulationTick);
		
		for(int i=0;i<listJobsWorkloadForShortJobs.size();i++) {
			int consumerIndex = (int)(Math.random()*listConsumersShortJobs.size());
			ConsumerInt consumer = listConsumersShortJobs.get(consumerIndex);
			consumer.queueJob(tickTimeDuration, currentSimulationTick, listJobsWorkloadForShortJobs.get(i));
		}
		
		//handle workload generation for long jobs
		List<ConsumerInt> listConsumersLongJobs = modelInterface.getListConsumersLongJobs();
		ArrayList<Job> listJobsWorkloadForLongJobs = modelInterface.getWorkloadGenerator().generateWorkloadForLongJobs(tickTimeDuration, currentSimulationTick);
		
		for(int i=0;i<listJobsWorkloadForLongJobs.size();i++) {
			int consumerIndex = (int)(Math.random()*listConsumersLongJobs.size());
			ConsumerInt consumer = listConsumersLongJobs.get(consumerIndex);
			consumer.queueJob(tickTimeDuration, currentSimulationTick, listJobsWorkloadForLongJobs.get(i));
		}
	}
	
	private void activateConsumers(int tickTimeDuration, int currentSimulationTick) {
		List<ConsumerInt> listConsumersShortJobs = modelInterface.getListConsumersShortJobs();
		for(int i=0;i<listConsumersShortJobs.size();i++) {
			ConsumerInt consumer = listConsumersShortJobs.get(i);
			consumer.scheduleJob(tickTimeDuration, currentSimulationTick);
		}
		List<ConsumerInt> listConsumersLongJobs = modelInterface.getListConsumersLongJobs();
		for(int i=0;i<listConsumersLongJobs.size();i++) {
			ConsumerInt consumer = listConsumersLongJobs.get(i);
			consumer.scheduleJob(tickTimeDuration, currentSimulationTick);
		}
	}

	private void activateProviders(int tickTimeDuration, int currentSimulationTick) {
		List<ProviderInt> listProviders = modelInterface.getListProviders();
		for(int i=0;i<listProviders.size();i++) {
			ProviderInt provider = listProviders.get(i);
			provider.execute(tickTimeDuration, currentSimulationTick);
		}
	}

	@Override
	public void triggerEvent() {
	}
}
