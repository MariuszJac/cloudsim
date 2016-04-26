package cs.entities;

public class Task {
	//request data
	private int id;
	private int taskExecutionTimeMilliSeconds; //how long task needs to execute (in milliseconds)
	private int taskSchedulingTimeMilliSeconds; //how long it takes to allocate the task on the computational node (e.g, transfer data)
	private int taskPlacementTimeMilliSeconds; //how long it takes to place the task on the computational node (e.g, transfer data)
	private Job job;
	
	//execution stats
	private int numberOfTicksToFinishTask = -1;
	private int taskQueuePlacementTickNumber;
	private int taskExecutionStartTickNumber;
	private int taskExecutionEndTickNumber;
	private boolean executed = false;

	public Task(int id, int taskExecutionTimeMilliSeconds, int taskSchedulingTimeMilliSeconds, 
			int taskPlacementTimeMilliSeconds) {
		super();
		this.id = id;
		this.taskExecutionTimeMilliSeconds = taskExecutionTimeMilliSeconds;
		this.taskSchedulingTimeMilliSeconds = taskSchedulingTimeMilliSeconds;
		this.taskPlacementTimeMilliSeconds = taskPlacementTimeMilliSeconds;
	}

	/***
	 * returns true if task has finished execution
	 * false if it still executes
	 */
	public boolean executeTask() {
		numberOfTicksToFinishTask--;
		if(numberOfTicksToFinishTask == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getTaskExecutionEndTickNumber() {
		return taskExecutionEndTickNumber;
	}
	public void setTaskExecutionEndTickNumber(int taskExecutionEndTickNumber) {
		this.taskExecutionEndTickNumber = taskExecutionEndTickNumber;
	}
	public boolean isExecuted() {
		return executed;
	}
	public void setExecuted(boolean executed, int tickTimeDuration, int currentSimulationTick) {
		this.executed = executed;
		job.getConsumer().notifyTaskExecution(this, tickTimeDuration, currentSimulationTick);
	}
	public int getTaskQueuePlacementTickNumber() {
		return taskQueuePlacementTickNumber;
	}
	public void setTaskQueuePlacementTickNumber(int taskQueuePlacementTickNumber) {
		this.taskQueuePlacementTickNumber = taskQueuePlacementTickNumber;
	}
	public int getTaskExecutionStartTickNumber() {
		return taskExecutionStartTickNumber;
	}
	public void setTaskExecutionStartTickNumber(int taskExecutionStartTickNumber) {
		this.taskExecutionStartTickNumber = taskExecutionStartTickNumber;
	}
	public int getNumberOfTicksToFinishTask() {
		return numberOfTicksToFinishTask;
	}
	public void setNumberOfTicksToFinishTask(int numberOfTicksToFinishTask) {
		this.numberOfTicksToFinishTask = numberOfTicksToFinishTask;
	}

	public int getTaskExecutionTimeMilliSeconds() {
		return taskExecutionTimeMilliSeconds;
	}

	public void setTaskExecutionTimeMilliSeconds(int taskExecutionTimeMilliSeconds) {
		this.taskExecutionTimeMilliSeconds = taskExecutionTimeMilliSeconds;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Job getJob() {
		return job;
	}

	public void setJob(Job job) {
		this.job = job;
	}

	public int getTaskPlacementTimeMilliSeconds() {
		return taskPlacementTimeMilliSeconds;
	}

	public void setTaskPlacementTimeMilliSeconds(int taskPlacementTimeMilliSeconds) {
		this.taskPlacementTimeMilliSeconds = taskPlacementTimeMilliSeconds;
	}

	public int getTaskSchedulingTimeMilliSeconds() {
		return taskSchedulingTimeMilliSeconds;
	}

	public void setTaskSchedulingTimeMilliSeconds(int taskSchedulingTimeMilliSeconds) {
		this.taskSchedulingTimeMilliSeconds = taskSchedulingTimeMilliSeconds;
	}

	
}
