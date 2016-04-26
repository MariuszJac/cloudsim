package cs.entities;

import java.util.ArrayList;

public interface ConsumerInt {

	public void scheduleJob(int tickTimeDuration, int currentSimulationTick);
	public void notifyTaskExecution(Task task, int tickTimeDuration, int currentSimulationTick);
	public void queueJob(int tickTimeDuration, int currentSimulationTick, Job job);
	public ArrayList<Job> getListJobs();
	
}
