package cs.entities.stats;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import cs.entities.BasicConsumer;
import cs.entities.Job;

public class PerformanceMonitor {
	private Logger logger = Logger.getLogger(PerformanceMonitor.class);

	//holds consumer stats events
	private ArrayList<ConsumerStatsEvent> listConsumerStatsEvent = new ArrayList<ConsumerStatsEvent>();
	//holds provider stats events
	private ArrayList<ProviderStatsEvent> listProviderStatsEvent = new ArrayList<ProviderStatsEvent>();
	//holds jobs stats events
	private ArrayList<JobStatsEvent> listJobStatsEvent = new ArrayList<JobStatsEvent>();
	
	private ArrayList<Job> listCompletedJobs = new ArrayList<Job>(); //this list is cleared every minute
	private ArrayList<Job> listQueuedJobs = new ArrayList<Job>(); //this list is cleared every minute
	private ArrayList<Job> listAllCompletedJobs = new ArrayList<Job>();//this list contains all completed jobs from the beginning of the simulation
	
	public void reportCompletedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listCompletedJobs.add(job);
		listAllCompletedJobs.add(job);
	}

	public void reportQueuedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listQueuedJobs.add(job);
	}

	/**
	 * Generated every minute based on the list of completed jobs (calculates various stats based on it for given time interval of analysis)
	 */
	public void generateConsumerMonitEvent(int tickTimeDuration, int currentSimulationTick) {
		logger.info("Generating ConsumerStats MonitEvent, currentTickNumber: "+currentSimulationTick+" tickTimeDuration: "+tickTimeDuration);
		//create ConsumerMonitEvent and fill it based on based on jobs stored in the list
		ConsumerStatsEvent statsEvent = new ConsumerStatsEvent();
		statsEvent.setTickNumber(currentSimulationTick);
		statsEvent.setTickTimeDuration(tickTimeDuration);
		
		//============================== consumer stats ==============================
		//--------------------- calculate number of queued jobs per job type
		int numberOfQueuedShortJobs = 0;
		int numberOfQueuedLongJobs = 0;
		for(int i=0;i<listQueuedJobs.size();i++) {
			if(listQueuedJobs.get(i).isShortJob()) {
				numberOfQueuedShortJobs++;
			} else {
				numberOfQueuedLongJobs++;
			}
		}	
		statsEvent.setNumberOfQueuedShortJobs(numberOfQueuedShortJobs);
		statsEvent.setNumberOfQueuedLongJobs(numberOfQueuedLongJobs);

		//---------------------- calculate number of completed jobs per job type
		int numberOfCompletedShortJobs = 0;
		int numberOfCompletedLongJobs = 0;
		for(int i=0;i<listCompletedJobs.size();i++) {
			if(listCompletedJobs.get(i).isShortJob()) {
				numberOfCompletedShortJobs++;
			} else {
				numberOfCompletedLongJobs++;
			}
		}	
		statsEvent.setNumberOfCompletedShortJobs(numberOfCompletedShortJobs);
		statsEvent.setNumberOfCompletedLongJobs(numberOfCompletedLongJobs);
		
		listConsumerStatsEvent.add(statsEvent);

		//clear the list
		clearTemporaryJobsData(tickTimeDuration, currentSimulationTick);
	}

	/**
	 * Generated at the very end of the simulation process as we collect all jobs first and then process their stats)
	 */
	public void generateJobsMonitEvent(int currentSimulationTick, int tickTimeDuration) {
		logger.info("Generating JobStats MonitEvent, currentTickNumber: "+currentSimulationTick+" tickTimeDuration: "+tickTimeDuration);

		//============================== jobs stats ==============================
		JobStatsEvent jobStatsEvent = new JobStatsEvent();
		jobStatsEvent.setTickNumber(currentSimulationTick);
		jobStatsEvent.setTickTimeDuration(tickTimeDuration);

		//--------------------- calculate jobs stats per job type
		int ticksItTakesToStartSchedulingForShortJob = 0;
		int ticksItTakesToScheduleForShortJob = 0;
		int ticksItTakesToStartExecutionForShortJob = 0;
		int ticksToExecuteJobForShortJob = 0;

		int ticksItTakesToStartSchedulingForLongJob = 0;
		int ticksItTakesToScheduleForLongJob = 0;
		int ticksItTakesToStartExecutionForLongJob = 0;
		int ticksToExecuteJobForLongJob = 0;

		int completedShortJobsCounter = 0;
		int completedLongJobsCounter = 0;
		
		for(int i=0;i<listAllCompletedJobs.size();i++) {
			Job completedJob = listAllCompletedJobs.get(i);
			if(completedJob.isShortJob()) {
				ticksItTakesToStartSchedulingForShortJob = ticksItTakesToStartSchedulingForShortJob + completedJob.getTicksItTakesToStartScheduling();
				ticksItTakesToScheduleForShortJob = ticksItTakesToScheduleForShortJob + completedJob.getTicksItTakesToSchedule();
				ticksItTakesToStartExecutionForShortJob = ticksItTakesToStartExecutionForShortJob + completedJob.getTicksItTakesToStartExecution();
				ticksToExecuteJobForShortJob = ticksToExecuteJobForShortJob + completedJob.getTotalTicksToExecuteJob();
				completedShortJobsCounter++;
			} else {
				ticksItTakesToStartSchedulingForLongJob = ticksItTakesToStartSchedulingForLongJob + completedJob.getTicksItTakesToStartScheduling();
				ticksItTakesToScheduleForLongJob = ticksItTakesToScheduleForLongJob + completedJob.getTicksItTakesToSchedule();
				ticksItTakesToStartExecutionForLongJob = ticksItTakesToStartExecutionForLongJob + completedJob.getTicksItTakesToStartExecution();
				ticksToExecuteJobForLongJob = ticksToExecuteJobForLongJob + completedJob.getTotalTicksToExecuteJob();
				completedLongJobsCounter++;
			}
		}
		//for short jobs
		jobStatsEvent.setTicksWaitingInQueueTimeForShortJob(round((double)ticksItTakesToStartSchedulingForShortJob / (double)completedShortJobsCounter));
		jobStatsEvent.setTicksSchedulingTimeForShortJob(round((double)ticksItTakesToScheduleForShortJob / (double)completedShortJobsCounter));
		jobStatsEvent.setTicksStartExecutionTimeForShortJob(round((double)ticksItTakesToStartExecutionForShortJob / (double)completedShortJobsCounter));
		jobStatsEvent.setTicksProcessingTimeForShortJob(round((double)ticksToExecuteJobForShortJob / (double)completedShortJobsCounter));
		jobStatsEvent.setCompletedShortJobsCounter(completedShortJobsCounter);
		//for long jobs
		jobStatsEvent.setTicksWaitingInQueueTimeForLongJob(round((double)ticksItTakesToStartSchedulingForLongJob / (double)completedLongJobsCounter));
		jobStatsEvent.setTicksSchedulingTimeForLongJob(round((double)ticksItTakesToScheduleForLongJob / (double)completedLongJobsCounter));
		jobStatsEvent.setTicksStartExecutionTimeForLongJob(round((double)ticksItTakesToStartExecutionForLongJob / (double)completedLongJobsCounter));
		jobStatsEvent.setTicksProcessingTimeForLongJob(round((double)ticksToExecuteJobForLongJob / (double)completedLongJobsCounter));
		jobStatsEvent.setCompletedLongJobsCounter(completedLongJobsCounter);

		listJobStatsEvent.add(jobStatsEvent);
	}

	private void clearTemporaryJobsData(int tickTimeDuration, int currentSimulationTick) {
		listQueuedJobs.clear();
		listCompletedJobs.clear();
	}

	public double round(double value) {
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(2, RoundingMode.HALF_DOWN);
		return bd.doubleValue();
	}
	
	public ArrayList<ConsumerStatsEvent> getListConsumerStatsEvent() {
		return listConsumerStatsEvent;
	}

	public void setListConsumerStatsEvent(
			ArrayList<ConsumerStatsEvent> listConsumerStatsEvent) {
		this.listConsumerStatsEvent = listConsumerStatsEvent;
	}

	public ArrayList<ProviderStatsEvent> getListProviderStatsEvent() {
		return listProviderStatsEvent;
	}

	public void setListProviderStatsEvent(
			ArrayList<ProviderStatsEvent> listProviderStatsEvent) {
		this.listProviderStatsEvent = listProviderStatsEvent;
	}

	public ArrayList<JobStatsEvent> getListJobStatsEvent() {
		return listJobStatsEvent;
	}

	public void setListJobStatsEvent(ArrayList<JobStatsEvent> listJobStatsEvent) {
		this.listJobStatsEvent = listJobStatsEvent;
	}

	public ArrayList<Job> getListAllCompletedJobs() {
		return listAllCompletedJobs;
	}

	public void setListAllCompletedJobs(ArrayList<Job> listAllCompletedJobs) {
		this.listAllCompletedJobs = listAllCompletedJobs;
	}

	
	
}

