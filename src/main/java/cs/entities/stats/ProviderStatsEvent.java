package cs.entities.stats;

public class ProviderStatsEvent {
	private int tickNumber;
	private int completedJobs;
	public int getTickNumber() {
		return tickNumber;
	}
	public void setTickNumber(int tickNumber) {
		this.tickNumber = tickNumber;
	}
	public int getCompletedJobs() {
		return completedJobs;
	}
	public void setCompletedJobs(int completedJobs) {
		this.completedJobs = completedJobs;
	}
	
	
}
