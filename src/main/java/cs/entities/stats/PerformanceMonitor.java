package cs.entities.stats;

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
	
	private ArrayList<Job> listCompletedJobs = new ArrayList<Job>();
	private ArrayList<Job> listQueuedJobs = new ArrayList<Job>();
	
	public void reportCompletedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listCompletedJobs.add(job);
	}

	public void reportQueuedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listQueuedJobs.add(job);
	}

	private void clearIntervalData(int tickTimeDuration, int currentSimulationTick) {
		listQueuedJobs.clear();
		listCompletedJobs.clear();
	}
	
	/**
	 * Generated every minute based on the list of completed jobs (calculates various stats based on it for given time interval of analysis)
	 */
	public void generateConsumerMonitEvent(int tickTimeDuration, int currentSimulationTick) {
		logger.info("Generating MonitEvent, currentTickNumber: "+currentSimulationTick+" tickTimeDuration: "+tickTimeDuration);
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

		//============================== jobs stats ==============================
		JobStatsEvent jobStatsEvent = new JobStatsEvent();
		jobStatsEvent.setTickNumber(currentSimulationTick);
		jobStatsEvent.setTickTimeDuration(tickTimeDuration);

		//--------------------- calculate jobs stats per job type

		int ticksItTakesToStartScheduling = 0;
		int ticksItTakesToSchedule = 0;
		int ticksItTakesToStartExecution = 0;
		int ticksToExecuteJob = 0;
		for(int i=0;i<listCompletedJobs.size();i++) {
			Job completedJob = listCompletedJobs.get(i);
			ticksItTakesToStartScheduling = ticksItTakesToStartScheduling + completedJob.getTicksItTakesToStartScheduling();
			ticksItTakesToSchedule = ticksItTakesToSchedule + completedJob.getTicksItTakesToSchedule();
			ticksItTakesToStartExecution = ticksItTakesToStartExecution + completedJob.getTicksItTakesToStartExecution();
			ticksToExecuteJob = ticksToExecuteJob + completedJob.getTotalTicksToExecuteJob();
		}	
		jobStatsEvent.setTicksWaitingInQueueTime(ticksItTakesToStartScheduling / listCompletedJobs.size());
		jobStatsEvent.setTicksSchedulingTime(ticksItTakesToSchedule / listCompletedJobs.size());
		jobStatsEvent.setTicksStartExecutionTime(ticksItTakesToStartExecution / listCompletedJobs.size());
		jobStatsEvent.setTicksProcessingTime(ticksToExecuteJob / listCompletedJobs.size());

		listJobStatsEvent.add(jobStatsEvent);
		
		//clear the list
		clearIntervalData(tickTimeDuration, currentSimulationTick);
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

	
}
