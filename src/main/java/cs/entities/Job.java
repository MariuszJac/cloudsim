package cs.entities;

import java.util.ArrayList;

public class Job {
	private int id;
	private ConsumerInt consumer; 
	private ArrayList<Task> listTasks = new ArrayList<Task>();
	
	//execution stats
	private int numberOfTicksToFinishJobScheduling=-1;
	private int jobQueuePlacementTickNumber;
	private int jobSchedulingStartTickNumber;
	private int jobSchedulingEndTickNumber;
	private int jobExecutionStartTickNumber;
	private int jobExecutionEndTickNumber;

	private boolean shortJob = false;
	private boolean scheduled = false;
	private boolean executed = false;

	public Job(int id, boolean shortJob) {
		super();
		this.id = id;
		this.shortJob = shortJob;
	}

	public Job(int id, ConsumerInt consumer) {
		super();
		this.id = id;
		this.consumer = consumer;
	}

	public Job(int id) {
		super();
		this.id = id;
	}

	/***
	 * returns true if task has finished execution
	 * false if it still executes
	 */
	public boolean scheduleJob() {
		numberOfTicksToFinishJobScheduling--;

		if(numberOfTicksToFinishJobScheduling == 0) {
			return true;
		} else {
			return false;
		}
	}

	public int getTicksItTakesToStartScheduling() {
		return jobSchedulingStartTickNumber - jobQueuePlacementTickNumber;
	}

	public int getTicksItTakesToSchedule() {
		return jobSchedulingEndTickNumber - jobSchedulingStartTickNumber;
	}

	/***
	 * ticks it takes to start job for execution (between queue placement - end of scheduling)
	 */
	public int getTicksItTakesToStartExecution() {
		return jobSchedulingEndTickNumber - jobQueuePlacementTickNumber;
	}

	/***
	 * ticks it takes to process the whole job (between queue placement - end of execution of all jobs tasks)
	 */
	public int getTicksItTakesToProcess() {
		return jobExecutionEndTickNumber - jobQueuePlacementTickNumber;
	}

	/***
	 * ticks it takes to execute (between start and end of execution time)
	 */
	public int getTicksItTakesToExecute() {
		return jobExecutionEndTickNumber - jobExecutionStartTickNumber + 1;
	}

	public int getTotalTicksToExecuteJob() {
		int ticksNumber = 0;
		for(int i=0;i< listTasks.size();i++) {
			Task task = listTasks.get(i);
			int numberOfTicksToFinishTask = (task.getTaskExecutionTimeMilliSeconds()+
					task.getTaskPlacementTimeMilliSeconds())/100;
			ticksNumber = ticksNumber + numberOfTicksToFinishTask;
		}
		return ticksNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void addTask(Task task) {
		task.setJob(this);
		listTasks.add(task);
	}

	public ArrayList<Task> getListTasks() {
		return listTasks;
	}

	public void setListTasks(ArrayList<Task> listTasks) {
		this.listTasks = listTasks;
	}

	public ConsumerInt getConsumer() {
		return consumer;
	}

	public void setConsumer(ConsumerInt consumer) {
		this.consumer = consumer;
	}

	public int getJobQueuePlacementTickNumber() {
		return jobQueuePlacementTickNumber;
	}

	public void setJobQueuePlacementTickNumber(int jobQueuePlacementTickNumber) {
		this.jobQueuePlacementTickNumber = jobQueuePlacementTickNumber;
	}

	public int getJobExecutionStartTickNumber() {
		return jobExecutionStartTickNumber;
	}

	public void setJobExecutionStartTickNumber(int jobExecutionStartTickNumber) {
		this.jobExecutionStartTickNumber = jobExecutionStartTickNumber;
	}

	public int getJobExecutionEndTickNumber() {
		return jobExecutionEndTickNumber;
	}

	public void setJobExecutionEndTickNumber(int jobExecutionEndTickNumber) {
		this.jobExecutionEndTickNumber = jobExecutionEndTickNumber;
	}

	public boolean isExecuted() {
		return executed;
	}

	public void setExecuted(boolean executed) {
		this.executed = executed;
	}

	public int getJobSchedulingStartTickNumber() {
		return jobSchedulingStartTickNumber;
	}

	public void setJobSchedulingStartTickNumber(int jobSchedulingStartTickNumber) {
		this.jobSchedulingStartTickNumber = jobSchedulingStartTickNumber;
	}

	public int getJobSchedulingEndTickNumber() {
		return jobSchedulingEndTickNumber;
	}

	public void setJobSchedulingEndTickNumber(int jobSchedulingEndTickNumber) {
		this.jobSchedulingEndTickNumber = jobSchedulingEndTickNumber;
	}

	public int getNumberOfTicksToFinishJobScheduling() {
		return numberOfTicksToFinishJobScheduling;
	}

	public void setNumberOfTicksToFinishJobScheduling(
			int numberOfTicksToFinishJobScheduling) {
		this.numberOfTicksToFinishJobScheduling = numberOfTicksToFinishJobScheduling;
	}

	public boolean isScheduled() {
		return scheduled;
	}

	public void setScheduled(boolean scheduled) {
		this.scheduled = scheduled;
	}

	public boolean isShortJob() {
		return shortJob;
	}

	public void setShortJob(boolean shortJob) {
		this.shortJob = shortJob;
	}
	
}
