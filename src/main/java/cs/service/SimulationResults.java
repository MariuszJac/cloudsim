package cs.service;

import iplatform.service.SimulationOutput;

public class SimulationResults extends SimulationOutput {
	private String jobRefId = "";
	private double probability = 0;
	private String start = "";
	private String end = "";
	public String getJobRefId() {
		return jobRefId;
	}
	public void setJobRefId(String jobRefId) {
		this.jobRefId = jobRefId;
	}
	public double getProbability() {
		return probability;
	}
	public void setProbability(double probability) {
		this.probability = probability;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}

	
}
