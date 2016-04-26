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

package iplatform.model.config;

import java.util.ArrayList;
import java.util.HashMap;

//this class should be used as a configuration template fetched to the model
//it should contain all the parameters used by the model
public class ModelConfigurationTemplate implements java.io.Serializable
{
	protected String templateName = "Default Model Configuration Template";

	protected int tickTimeInMilliseconds = 100;

	protected int numberOfSimulationRepetitions = 1;

	protected boolean useCurrentTimeAsStartDate = false;
	//start simulation date
	protected int numberOfSimulationHours = 0;
	//this is where we put the job reference if the model is runing in service mode
	protected String jobRefId = "";

	public ModelConfigurationTemplate()
	{
		
	}
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public int getNumberOfSimulationRepetitions() {
		return numberOfSimulationRepetitions;
	}
	public void setNumberOfSimulationRepetitions(int numberOfSimulationRepetitions) {
		this.numberOfSimulationRepetitions = numberOfSimulationRepetitions;
	}
	public boolean isUseCurrentTimeAsStartDate() {
		return useCurrentTimeAsStartDate;
	}
	public void setUseCurrentTimeAsStartDate(boolean useCurrentTimeAsStartDate) {
		this.useCurrentTimeAsStartDate = useCurrentTimeAsStartDate;
	}
	public int getNumberOfSimulationHours() {
		return numberOfSimulationHours;
	}

	public void setNumberOfSimulationHours(int numberOfSimulationHours) {
		this.numberOfSimulationHours = numberOfSimulationHours;
	}

	public String getJobRefId() {
		return jobRefId;
	}
	public void setJobRefId(String jobRefId) {
		this.jobRefId = jobRefId;
	}

	public int getTickTimeInMilliseconds() {
		return tickTimeInMilliseconds;
	}

	public void setTickTimeInMilliseconds(int tickTimeInMilliseconds) {
		this.tickTimeInMilliseconds = tickTimeInMilliseconds;
	}
	
}
