package cs.entities.stats;

public class JobStatsEvent {
	private int tickNumber;
	private int tickTimeDuration;

	private int ticksWaitingInQueueTime; //before job starts to be scheduled
	private int ticksSchedulingTime; //time it takes to schedule the job 
	private int ticksStartExecutionTime; //time it takes to start execution time (queue + scheduling time) 
	private int ticksProcessingTime; //time it takes to finish job execution 

	public int getTickNumber() {
		return tickNumber;
	}
	public void setTickNumber(int tickNumber) {
		this.tickNumber = tickNumber;
	}
	public int getTicksWaitingInQueueTime() {
		return ticksWaitingInQueueTime;
	}
	public void setTicksWaitingInQueueTime(int ticksWaitingInQueueTime) {
		this.ticksWaitingInQueueTime = ticksWaitingInQueueTime;
	}
	public int getTicksSchedulingTime() {
		return ticksSchedulingTime;
	}
	public void setTicksSchedulingTime(int ticksSchedulingTime) {
		this.ticksSchedulingTime = ticksSchedulingTime;
	}
	public int getTicksStartExecutionTime() {
		return ticksStartExecutionTime;
	}
	public void setTicksStartExecutionTime(int ticksStartExecutionTime) {
		this.ticksStartExecutionTime = ticksStartExecutionTime;
	}
	public int getTicksProcessingTime() {
		return ticksProcessingTime;
	}
	public void setTicksProcessingTime(int ticksProcessingTime) {
		this.ticksProcessingTime = ticksProcessingTime;
	}
	public int getTickTimeDuration() {
		return tickTimeDuration;
	}
	public void setTickTimeDuration(int tickTimeDuration) {
		this.tickTimeDuration = tickTimeDuration;
	}
	
}
