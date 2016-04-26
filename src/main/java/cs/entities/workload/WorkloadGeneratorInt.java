package cs.entities.workload;

import java.util.ArrayList;

import cs.entities.Job;

public interface WorkloadGeneratorInt {
	public ArrayList<Job> generateWorkloadForShortJobs(int tickTimeDuration, int currentSimulationTick);
	public ArrayList<Job> generateWorkloadForLongJobs(int tickTimeDuration, int currentSimulationTick);

}
