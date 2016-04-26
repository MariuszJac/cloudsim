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
	
	private ArrayList<Job> listCompletedJobs = new ArrayList<Job>();
	private ArrayList<Job> listQueuedJobs = new ArrayList<Job>();
	
	public void reportCompletedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listCompletedJobs.add(job);
	}

	public void reportQueuedJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		listQueuedJobs.add(job);
	}

	private void clearConsumerData(int tickTimeDuration, int currentSimulationTick) {
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
		statsEvent.setNumberOfQueuedJobs(listQueuedJobs.size());
		statsEvent.setNumberOfCompletedJobs(listCompletedJobs.size());
		
		listConsumerStatsEvent.add(statsEvent);
		//clear the list
		clearConsumerData(tickTimeDuration, currentSimulationTick);
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

	
}
