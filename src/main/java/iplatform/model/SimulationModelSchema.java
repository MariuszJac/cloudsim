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

package iplatform.model;

import iplatform.batch.ModelRunner;
import iplatform.config.ToolConfigurationTemplate;
import iplatform.dataexport.interfaces.IDataExport;
import iplatform.init.SimulationClock;
import iplatform.model.config.ModelConfigurationTemplate;

import org.apache.log4j.Logger;

public class SimulationModelSchema{
	protected static Logger logger = Logger.getLogger(ModelRunner.class);
	
	protected String configurationFilePath = ""; 

	protected SimulationClock simulationClock = null;
	//this class is used to export simulation output
	protected IDataExport dataExport = null;

	//this holds the configuration specific to the whole platform
	protected ToolConfigurationTemplate toolConfigurationTemplate = null;

	public SimulationModelSchema()
	{
	}

}
