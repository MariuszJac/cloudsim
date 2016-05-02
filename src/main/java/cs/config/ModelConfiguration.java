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

package cs.config;

import iplatform.model.config.ModelConfigurationTemplate;

//this class should be used as a configuration template fetched to the model
//it should contain all the parameters used by the model
public class ModelConfiguration  extends ModelConfigurationTemplate implements java.io.Serializable
{
	public ModelConfiguration()
	{
		super();
		//make sure template name has not spaces!
		setTemplateName("DefaultModelConfigurationTemplate");
	}

	/** 
	 * 0-centralized (two threads, one handling short and one handling long jobs 
	 * 1-distributed (shared memory), 
	 * 2-decentralized (power of two), 
	 * 3-decentralized (local memory)
	 */
	public int simulationMode = 0;  
	public int numberOfConsumersForShortJobs;
	public int numberOfConsumersForLongJobs;
	public int numberOfProviders;
	public boolean enableGraphVisualisation = false;

	public int getNumberOfProviders() {
		return numberOfProviders;
	}
	public void setNumberOfProviders(int numberOfProviders) {
		this.numberOfProviders = numberOfProviders;
	}
	public boolean isEnableGraphVisualisation() {
		return enableGraphVisualisation;
	}
	public void setEnableGraphVisualisation(boolean enableGraphVisualisation) {
		this.enableGraphVisualisation = enableGraphVisualisation;
	}
	public int getNumberOfConsumersForShortJobs() {
		return numberOfConsumersForShortJobs;
	}
	public void setNumberOfConsumersForShortJobs(int numberOfConsumersForShortJobs) {
		this.numberOfConsumersForShortJobs = numberOfConsumersForShortJobs;
	}
	public int getNumberOfConsumersForLongJobs() {
		return numberOfConsumersForLongJobs;
	}
	public void setNumberOfConsumersForLongJobs(int numberOfConsumersForLongJobs) {
		this.numberOfConsumersForLongJobs = numberOfConsumersForLongJobs;
	}
	public int getSimulationMode() {
		return simulationMode;
	}
	public void setSimulationMode(int simulationMode) {
		this.simulationMode = simulationMode;
	}
	
}
