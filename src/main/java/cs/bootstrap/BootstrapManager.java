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

package cs.bootstrap;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;
import cs.entities.BasicConsumer;
import cs.entities.BasicProvider;
import cs.entities.ConsumerInt;
import cs.entities.GlobalRegistry;
import cs.entities.ProviderInt;

public class BootstrapManager {

	static Logger logger = Logger.getLogger(BootstrapManager.class);
	
	private ModelInterface modelInterface = null;

	//this is where model configuration is kept
	private ModelConfiguration configuration = null;
	
	public BootstrapManager(ModelInterface modelInterfaceRef) {
		logger.info("successfully initialised bootstrap manager...");
		this.modelInterface = modelInterfaceRef;
		this.configuration = modelInterface.getModelConfiguration();
	}
	
	public void createProviders() {
		List<ProviderInt> listProviders = new ArrayList<ProviderInt>();
		logger.info("creating providers: "+configuration.getNumberOfProviders());
		for(int i=0;i<configuration.getNumberOfProviders();i++) {
			ProviderInt provider = new BasicProvider(modelInterface, configuration,i);
			listProviders.add(provider);
			if(modelInterface.getModelConfiguration().getSimulationMode()==0) {
				GlobalRegistry.addProvider(provider); //add all providers to global registry
				System.exit(0);
			}
		}
		modelInterface.setListProviders(listProviders);
	}
	
	public void createConsumers() {
		//create consumers for short jobs
		int schedulerIdCounter = 0;
		List<ConsumerInt> listConsumersShortJobs = new ArrayList<ConsumerInt>();
		logger.info("creating consumers for short jobs: "+configuration.getNumberOfConsumersForShortJobs());
		for(int i=0;i<configuration.getNumberOfConsumersForShortJobs();i++) {
			schedulerIdCounter++;
			listConsumersShortJobs.add(new BasicConsumer(modelInterface, configuration,schedulerIdCounter, true));
		}
		modelInterface.setListConsumersShortJobs(listConsumersShortJobs);
		
		//create consumers for long jobs
		List<ConsumerInt> listConsumersLongJobs = new ArrayList<ConsumerInt>();
		logger.info("creating consumers for long jobs: "+configuration.getNumberOfConsumersForLongJobs());
		for(int i=0;i<configuration.getNumberOfConsumersForLongJobs();i++) {
			schedulerIdCounter++;
			listConsumersLongJobs.add(new BasicConsumer(modelInterface, configuration,schedulerIdCounter, false));
		}
		modelInterface.setListConsumersLongJobs(listConsumersLongJobs);
	}

}
