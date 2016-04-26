package cs.entities.stats;

public class ConsumerStatsEvent {
	private int tickNumber;
	private int tickTimeDuration;
	private int numberOfQueuedJobs;
	private int numberOfCompletedJobs;
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
	public int getNumberOfCompletedJobs() {
		return numberOfCompletedJobs;
	}
	public void setNumberOfCompletedJobs(int numberOfCompletedJobs) {
		this.numberOfCompletedJobs = numberOfCompletedJobs;
	}
	public int getNumberOfQueuedJobs() {
		return numberOfQueuedJobs;
	}
	public void setNumberOfQueuedJobs(int numberOfQueuedJobs) {
		this.numberOfQueuedJobs = numberOfQueuedJobs;
	}
	
}
