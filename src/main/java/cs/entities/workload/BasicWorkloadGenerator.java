package cs.entities.workload;

import java.util.ArrayList;

import cs.entities.Job;
import cs.entities.Task;

public class BasicWorkloadGenerator implements WorkloadGeneratorInt {
	public int jobCounter = 0;
	public int taskCounter = 0;
	@Override
	public ArrayList<Job> generateWorkloadForShortJobs(int tickTimeDuration, int currentSimulationTick) {
		ArrayList<Job> listJobsWorkloadForShortJobs = new ArrayList<Job>();
		if(Math.random()>0.95){
			Job job = new Job(jobCounter++, true);
			job.addTask(new Task(taskCounter++, 200, 100, 100));
			job.addTask(new Task(taskCounter++, 200, 100, 100));
			listJobsWorkloadForShortJobs.add(job);
		}
		
		return listJobsWorkloadForShortJobs;
	}

	@Override
	public ArrayList<Job> generateWorkloadForLongJobs(int tickTimeDuration, int currentSimulationTick) {
		ArrayList<Job> listJobsWorkloadForLongJobs = new ArrayList<Job>();

		if(Math.random()>0.97){
			Job job = new Job(jobCounter++, false);
			job.addTask(new Task(taskCounter++, 2000, 100, 100));
			job.addTask(new Task(taskCounter++, 2000, 100, 100));
			listJobsWorkloadForLongJobs.add(job);
		}

		return listJobsWorkloadForLongJobs;
	}
}
