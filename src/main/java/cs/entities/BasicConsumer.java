package cs.entities;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;

public class BasicConsumer implements ConsumerInt {
	private Logger logger = Logger.getLogger(BasicConsumer.class);
	private ModelInterface modelInterface = null;
	private ModelConfiguration modelConfiguration = null;
	
	private int id;

	private ArrayList<Job> listJobs = new ArrayList<Job>();
	private Job currentlyScheduledJob = null;

	private int numberOfCompletedJobs = 0;
	private int currentSimulationTickValue;
	boolean isJobSchedulingComplete = false;

	boolean isShortJobScheduler = false;
	
	public BasicConsumer(int id) {
		super();
		this.id = id;
	}

	public BasicConsumer(ModelConfiguration modelConfiguration, int id) {
		super();
		this.modelConfiguration = modelConfiguration;
		this.id = id;
	}

	public BasicConsumer(ModelInterface modelInterface, ModelConfiguration modelConfiguration, int id, boolean isShortJobScheduler) {
		super();
		this.modelInterface = modelInterface;
		this.id = id;
		this.isShortJobScheduler = isShortJobScheduler;
	}
	
	public void queueJob(int tickTimeDuration, int currentSimulationTick, Job job) {
		job.setJobQueuePlacementTickNumber(currentSimulationTick);
		job.setConsumer(this);
		listJobs.add(job);
		logger.debug(getLogEntry(currentSimulationTick)+" adding job to queue: "+job.getId()+" Tasks: "+job.getListTasks().size());
		//report to PerformanceMonitor
		if(modelInterface != null) {
			modelInterface.getPerformanceMonitor().reportQueuedJob(job, tickTimeDuration, currentSimulationTick);
		}
	}
	
	/***
	 * called by simulation clock every tick interval
	 * tickTimeDuration (in milliseconds) represents the real time duration of each tick
	 * perform job scheduling using following steps:
	 * 0) pick job from the queue
	 * 1) calculate the time it takes to schedule all tasks
	 * 2) allocate tasks to providers
	 * 3) pick new job to allocate or wait if queue is empty 
	 */
	public void scheduleJob(int tickTimeDuration, int currentSimulationTick) {

		//if task execution has completed update its status and notify the scheduler 
		if(currentlyScheduledJob != null && isJobSchedulingComplete) {
			listJobs.remove(0); //delete from queue
			currentlyScheduledJob.setScheduled(true);
			currentlyScheduledJob.setJobSchedulingEndTickNumber(currentSimulationTick);
			
			//flag that there is no task set for execution
			logger.debug(getLogEntry(currentSimulationTick)+" completed scheduling of job: "+currentlyScheduledJob.getId());
			//allocate all job tasks on processing nodes
			allocateJob(currentlyScheduledJob, tickTimeDuration, currentSimulationTick);
			currentlyScheduledJob = null;
		}

		if(currentlyScheduledJob == null) { //if no task currently executed-get first task from the list and initiate execution
			if(listJobs.size()>0) {
				currentlyScheduledJob = listJobs.get(0);
				logger.debug(getLogEntry(currentSimulationTick)+" starting scheduling of job: "+currentlyScheduledJob.getId());
				//calculate the number of ticks that need to pass for the job scheduling to be finished
				int numberOfTicksToFinishJobScheduling = getNumberOfTicksToFinishJobScheduling(currentlyScheduledJob, tickTimeDuration, currentSimulationTick);
				currentlyScheduledJob.setNumberOfTicksToFinishJobScheduling(numberOfTicksToFinishJobScheduling);
				currentlyScheduledJob.setJobSchedulingStartTickNumber(currentSimulationTick);
				logger.debug(getLogEntry(currentSimulationTick)+" scheduling of job: "+currentlyScheduledJob.getId()+" ticks to finish job scheduling: "+numberOfTicksToFinishJobScheduling);
				isJobSchedulingComplete = currentlyScheduledJob.scheduleJob();
			} else {
				logger.debug(getLogEntry(currentSimulationTick)+" no jobs to schedule in this tick...");
			}
		} else {
			logger.debug(getLogEntry(currentSimulationTick)+" scheduling of job: "+currentlyScheduledJob.getId()+" ticks to finish job scheduling: "+currentlyScheduledJob.getNumberOfTicksToFinishJobScheduling());
			isJobSchedulingComplete = currentlyScheduledJob.scheduleJob();
		}
	}

	private int getNumberOfTicksToFinishJobScheduling(Job job, int tickTimeDuration, int currentSimulationTick) {
		int numberOfTicksToFinishJobScheduling = 0;
		int millisecondsToFinishJobScheduling = 0;
		ArrayList<Task> listTasks = job.getListTasks();
		for(int i=0;i<listTasks.size();i++) {
			Task task = listTasks.get(i);
			millisecondsToFinishJobScheduling = millisecondsToFinishJobScheduling + task.getTaskSchedulingTimeMilliSeconds();
		}
		numberOfTicksToFinishJobScheduling = millisecondsToFinishJobScheduling/tickTimeDuration;
		logger.debug(getLogEntry(currentSimulationTick)+" ticks number: "+numberOfTicksToFinishJobScheduling+" ("+millisecondsToFinishJobScheduling+" ms) until scheduling is completed for job with id: "+job.getId()+" (tasks: "+listTasks.size()+")");
		
		return numberOfTicksToFinishJobScheduling;
	}
	
	/**
	 * this method performs the actual job allocation using specific algorithm for it
	 */
	private boolean allocateJob(Job job, int tickTimeDuration, int currentSimulationTick) {
		boolean isSuccess = false;
		if(GlobalRegistry.getListProviders().size() == 0){
			return false;
		}

		/**
		 * Apply simplest random scheduling across all providers using GlobalRegistry
		 */
		logger.debug(getLogEntry(currentSimulationTick)+" placing job: "+job.getId()+" tasks on provider nodes...");
		for(int i=0;i<job.getListTasks().size();i++) {
			Task taskToPlace = job.getListTasks().get(i);
			int randomIndex = (int)(Math.random()*GlobalRegistry.getListProviders().size());
			ProviderInt selectedProvider = GlobalRegistry.getListProviders().get(randomIndex);
			int taskExecutionTime = selectedProvider.requestTaskExecutionTime(taskToPlace, currentSimulationTick);
			logger.debug(getLogEntry(currentSimulationTick)+" placing task: "+taskToPlace.getId()+" (job: "+job.getId()+" on provider: "+selectedProvider.getId()+" TET: "+taskExecutionTime);
			selectedProvider.requestTaskExecution(taskToPlace, currentSimulationTick);
		}
		
		return isSuccess;
	}
	
	public void notifyTaskExecution(Task task, int tickTimeDuration, int currentSimulationTick) {
		logger.debug(getLogEntry(currentSimulationTick)+" received notification from task: "+task.getId());
		if(isJobExecuted(task)) {
			//set the current simulation tick as the end of job execution time
			task.getJob().setJobExecutionEndTickNumber(currentSimulationTick);
			logger.debug(getLogEntry(currentSimulationTick)+" COMPLETED JOB ");
			logger.debug(getLogEntry(currentSimulationTick)+" COMPLETED JOB stats time to start scheduling (wait in queue time): "+task.getJob().getTicksItTakesToStartScheduling());
			logger.debug(getLogEntry(currentSimulationTick)+" COMPLETED JOB stats time to start scheduling (scheduling time): "+task.getJob().getTicksItTakesToSchedule());
			logger.debug(getLogEntry(currentSimulationTick)+" COMPLETED JOB stats time to start execution (queueing + scheduling time): "+task.getJob().getTicksItTakesToStartExecution());
			//logger.debug("Consumer: "+id+" COMPLETED JOB stats execution time: "+task.getJob().getTicksItTakesToExecute());
			logger.debug(getLogEntry(currentSimulationTick)+" COMPLETED JOB stats processing time: "+task.getJob().getTicksItTakesToProcess()+" total ticks to execute job: "+task.getJob().getTotalTicksToExecuteJob());
			numberOfCompletedJobs++;
			
			//report to PerformanceMonitor
			if(modelInterface != null) {
				modelInterface.getPerformanceMonitor().reportCompletedJob(task.getJob(), tickTimeDuration, currentSimulationTick);
			}
		}
		logger.debug(getLogEntry(currentSimulationTick)+" number or completed jobs: "+numberOfCompletedJobs);
	}
	
	private boolean isJobExecuted(Task task) {
		Job job = task.getJob();
		ArrayList<Task> listTasks = job.getListTasks();
		for(int i=0;i<listTasks.size();i++) {
			if(!listTasks.get(i).isExecuted()) {
				return false;
			}
		}
		return true;
	}
	
	private String getLogEntry(int currentSimulationTick) {
		return "Scheduler:"+getId()+" Short job: "+isShortJobScheduler+" T:"+currentSimulationTick;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumberOfCompletedJobs() {
		return numberOfCompletedJobs;
	}

	public void setNumberOfCompletedJobs(int numberOfCompletedJobs) {
		this.numberOfCompletedJobs = numberOfCompletedJobs;
	}

	public ArrayList<Job> getListJobs() {
		return listJobs;
	}

	public void setListJobs(ArrayList<Job> listJobs) {
		this.listJobs = listJobs;
	}
	
}
