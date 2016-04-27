package cs.entities.stats;

public class JobStatsEvent {
	private int tickNumber;
	private int tickTimeDuration;

	private int completedShortJobsCounter;
	private double ticksWaitingInQueueTimeForShortJob; //before job starts to be scheduled
	private double ticksSchedulingTimeForShortJob; //time it takes to schedule the job 
	private double ticksStartExecutionTimeForShortJob; //time it takes to start execution time (queue + scheduling time) 
	private double ticksProcessingTimeForShortJob; //time it takes to finish job execution 

	private int completedLongJobsCounter;
	private double ticksWaitingInQueueTimeForLongJob; //before job starts to be scheduled
	private double ticksSchedulingTimeForLongJob; //time it takes to schedule the job 
	private double ticksStartExecutionTimeForLongJob; //time it takes to start execution time (queue + scheduling time) 
	private double ticksProcessingTimeForLongJob; //time it takes to finish job execution 

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
	public double getTicksWaitingInQueueTimeForShortJob() {
		return ticksWaitingInQueueTimeForShortJob;
	}
	public void setTicksWaitingInQueueTimeForShortJob(
			double ticksWaitingInQueueTimeForShortJob) {
		this.ticksWaitingInQueueTimeForShortJob = ticksWaitingInQueueTimeForShortJob;
	}
	public double getTicksSchedulingTimeForShortJob() {
		return ticksSchedulingTimeForShortJob;
	}
	public void setTicksSchedulingTimeForShortJob(
			double ticksSchedulingTimeForShortJob) {
		this.ticksSchedulingTimeForShortJob = ticksSchedulingTimeForShortJob;
	}
	public double getTicksStartExecutionTimeForShortJob() {
		return ticksStartExecutionTimeForShortJob;
	}
	public void setTicksStartExecutionTimeForShortJob(
			double ticksStartExecutionTimeForShortJob) {
		this.ticksStartExecutionTimeForShortJob = ticksStartExecutionTimeForShortJob;
	}
	public double getTicksProcessingTimeForShortJob() {
		return ticksProcessingTimeForShortJob;
	}
	public void setTicksProcessingTimeForShortJob(
			double ticksProcessingTimeForShortJob) {
		this.ticksProcessingTimeForShortJob = ticksProcessingTimeForShortJob;
	}
	public double getTicksWaitingInQueueTimeForLongJob() {
		return ticksWaitingInQueueTimeForLongJob;
	}
	public void setTicksWaitingInQueueTimeForLongJob(
			double ticksWaitingInQueueTimeForLongJob) {
		this.ticksWaitingInQueueTimeForLongJob = ticksWaitingInQueueTimeForLongJob;
	}
	public double getTicksSchedulingTimeForLongJob() {
		return ticksSchedulingTimeForLongJob;
	}
	public void setTicksSchedulingTimeForLongJob(
			double ticksSchedulingTimeForLongJob) {
		this.ticksSchedulingTimeForLongJob = ticksSchedulingTimeForLongJob;
	}
	public double getTicksStartExecutionTimeForLongJob() {
		return ticksStartExecutionTimeForLongJob;
	}
	public void setTicksStartExecutionTimeForLongJob(
			double ticksStartExecutionTimeForLongJob) {
		this.ticksStartExecutionTimeForLongJob = ticksStartExecutionTimeForLongJob;
	}
	public double getTicksProcessingTimeForLongJob() {
		return ticksProcessingTimeForLongJob;
	}
	public void setTicksProcessingTimeForLongJob(
			double ticksProcessingTimeForLongJob) {
		this.ticksProcessingTimeForLongJob = ticksProcessingTimeForLongJob;
	}
	public int getCompletedShortJobsCounter() {
		return completedShortJobsCounter;
	}
	public void setCompletedShortJobsCounter(int completedShortJobsCounter) {
		this.completedShortJobsCounter = completedShortJobsCounter;
	}
	public int getCompletedLongJobsCounter() {
		return completedLongJobsCounter;
	}
	public void setCompletedLongJobsCounter(int completedLongJobsCounter) {
		this.completedLongJobsCounter = completedLongJobsCounter;
	}
	
}
