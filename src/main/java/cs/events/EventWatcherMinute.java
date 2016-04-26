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

import java.util.Hashtable;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;
import cs.dataexport.DataExport;
import eu.robust.simulation.entities.User;

public class EventWatcherMinute implements SimulationClockEvent{

	static Logger logger = Logger.getLogger(ModelRunner.class);

	private ModelInterface modelInterface = null;
	private int id = 0;

	//this is class used to configure the model
	private ModelConfiguration modelConfiguration = null;
	
	public EventWatcherMinute(ModelInterface modelInterfaceRef, int idRef)
	{
		this.id = idRef;
		
		this.modelInterface = modelInterfaceRef;
		this.modelConfiguration = modelInterface.getModelConfiguration();
	}
	
	@Override
	public void triggerEvent(int tickNumber, int tickTimeDurationInMilliseconds) {
		logger.info("---> minute event triggered by simplatform clock: currentTickNumber: "+tickNumber+" tickTimeDuration: "+tickTimeDurationInMilliseconds);
		modelInterface.getPerformanceMonitor().generateConsumerMonitEvent(tickTimeDurationInMilliseconds, tickNumber);
	}

	@Override
	public void triggerEvent() {
	}

	@Override
	public int getEventId() {
		// TODO Auto-generated method stub
		return id;
	}
}
