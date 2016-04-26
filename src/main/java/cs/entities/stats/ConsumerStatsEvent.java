package cs.entities.stats;

public class ConsumerStatsEvent {
	private int tickNumber;
	private int tickTimeDuration;
	//for short jobs
	private int numberOfQueuedShortJobs;
	private int numberOfCompletedShortJobs;
	//for short jobs
	private int numberOfQueuedLongJobs;
	private int numberOfCompletedLongJobs;
	
	public int getTickNumber() {
		return tickNumber;
	}
	public void setTickNumber(int tickNumber) {
		this.tickNumber = tickNumber;
	}
	
	public int getTickTimeDuration() {
		return tickTimeDuration;
	}
	public void setTickTimeDuration(int tickTimeDuration) {
		this.tickTimeDuration = tickTimeDuration;
	}
	public int getNumberOfQueuedShortJobs() {
		return numberOfQueuedShortJobs;
	}
	public void setNumberOfQueuedShortJobs(int numberOfQueuedShortJobs) {
		this.numberOfQueuedShortJobs = numberOfQueuedShortJobs;
	}
	public int getNumberOfCompletedShortJobs() {
		return numberOfCompletedShortJobs;
	}
	public void setNumberOfCompletedShortJobs(int numberOfCompletedShortJobs) {
		this.numberOfCompletedShortJobs = numberOfCompletedShortJobs;
	}
	public int getNumberOfQueuedLongJobs() {
		return numberOfQueuedLongJobs;
	}
	public void setNumberOfQueuedLongJobs(int numberOfQueuedLongJobs) {
		this.numberOfQueuedLongJobs = numberOfQueuedLongJobs;
	}
	public int getNumberOfCompletedLongJobs() {
		return numberOfCompletedLongJobs;
	}
	public void setNumberOfCompletedLongJobs(int numberOfCompletedLongJobs) {
		this.numberOfCompletedLongJobs = numberOfCompletedLongJobs;
	}
}
