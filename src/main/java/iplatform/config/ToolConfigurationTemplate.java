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

package iplatform.config;

public class ToolConfigurationTemplate {
	//0 - normal mode, 1 - batch mode, 2 - replay mode
	private int operationMode = 0;
	private boolean showGUI = true;
	private int heapSize = 1024;
	private String batchFolderLocation = "batch";
	private String resultsFolderLocation = "results";
	private String bootstrapDataFolderLocation = "bootstrap";
	private String modelClassToInstantiate = "";
	private String modelConfigurationClassToInstantiate = "";
	private String simulationResultsClassToInstantiate = "";
	private String simulationResultsFileName = "SimulationResults.txt";
	
	public int getOperationMode() {
		return operationMode;
	}
	
	public void setOperationMode(int operationMode) {
		this.operationMode = operationMode;
	}
	
	public int getHeapSize() {
		return heapSize;
	}
	
	public void setHeapSize(int heapSize) {
		this.heapSize = heapSize;
	}
	
	public String getBatchFolderLocation() {
		return batchFolderLocation;
	}
	
	public void setBatchFolderLocation(String batchFolderLocation) {
		this.batchFolderLocation = batchFolderLocation;
	}

	public String getResultsFolderLocation() {
		return resultsFolderLocation;
	}

	public void setResultsFolderLocation(String resultsFolderLocation) {
		this.resultsFolderLocation = resultsFolderLocation;
	}

	public boolean isShowGUI() {
		return showGUI;
	}

	public void setShowGUI(boolean showGUI) {
		this.showGUI = showGUI;
	}

	public String getModelClassToInstantiate() {
		return modelClassToInstantiate;
	}

	public void setModelClassToInstantiate(String modelClassToInstantiate) {
		this.modelClassToInstantiate = modelClassToInstantiate;
	}

	public String getModelConfigurationClassToInstantiate() {
		return modelConfigurationClassToInstantiate;
	}

	public void setModelConfigurationClassToInstantiate(
			String modelConfigurationClassToInstantiate) {
		this.modelConfigurationClassToInstantiate = modelConfigurationClassToInstantiate;
	}

	public String getSimulationResultsFileName() {
		return simulationResultsFileName;
	}

	public void setSimulationResultsFileName(String simulationResultsFileName) {
		this.simulationResultsFileName = simulationResultsFileName;
	}

	public String getSimulationResultsClassToInstantiate() {
		return simulationResultsClassToInstantiate;
	}

	public void setSimulationResultsClassToInstantiate(
			String simulationResultsClassToInstantiate) {
		this.simulationResultsClassToInstantiate = simulationResultsClassToInstantiate;
	}

	public String getBootstrapDataFolderLocation() {
		return bootstrapDataFolderLocation;
	}

	public void setBootstrapDataFolderLocation(String bootstrapDataFolderLocation) {
		this.bootstrapDataFolderLocation = bootstrapDataFolderLocation;
	}
	
	
}
