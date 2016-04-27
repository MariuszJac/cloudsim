package cs.entities;

import iplatform.batch.ModelRunner;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import cs.ModelInterface;
import cs.config.ModelConfiguration;

/*** This provider has following functionality built in:
 * Queue of tasks
 * Slot-based execution (only single task can be executed at the same time -> no hardware heterogeneity)
 * Request response contains allocation time estimate based on the already queued tasks (aka Apollo)
 *
 */

public class BasicProvider implements ProviderInt {

	private Logger logger = Logger.getLogger(BasicProvider.class);
	private ModelInterface modelInterface = null;
	private ModelConfiguration modelConfiguration = null;
	private int id;
	private ArrayList<Task> listTasks = new ArrayList<Task>();
	private Task currentlyExecutedTask = null;
	private boolean isTaskExecutionComplete = false;
	
	public BasicProvider(ModelConfiguration modelConfiguration, int id) {
		super();
		this.modelConfiguration = modelConfiguration;
		this.id = id;
	}

	public BasicProvider(ModelInterface modelInterface, ModelConfiguration modelConfiguration, int id) {
		super();
		this.modelConfiguration = modelConfiguration;
		this.modelInterface = modelInterface;
		this.id = id;
	}
	
	/***
	 * returns time it will take to execute a task (time is in milliseconds)
	 * called before actual task allocation on this provider
	 * calculates the time it will take to execute the task given the number of queued tasks and their allocation times
	 * if returns -1 then provider rejects execution 
	 */
	public int requestTaskExecutionTime(Task task, int currentSimulationTick) {
		//calculate wait time (if there are other queued tasks)
		int allocationTimeWait = 0;
		for(int i=0; i<listTasks.size();i++) {
			allocationTimeWait = allocationTimeWait + listTasks.get(i).getTaskPlacementTimeMilliSeconds() + 
					listTasks.get(i).getTaskExecutionTimeMilliSeconds();
		}
			
		//factor in task execution time
		int executionFinishTimeWait = allocationTimeWait + task.getTaskPlacementTimeMilliSeconds() + task.getTaskExecutionTimeMilliSeconds(); 

		logger.debug(getLogEntry(currentSimulationTick)+" calculating task execution time, total tasks queued: "+listTasks.size()+" TET: "+executionFinishTimeWait);
		
		return executionFinishTimeWait;
	}
 
	public void requestTaskExecution(Task task, int currentSimulationTick) {
		//FIFO based queue
		listTasks.add(task);
		logger.debug(getLogEntry(currentSimulationTick)+" added to queue, size is: "+listTasks.size());
	}
	
	/***
	 * called by simulation clock every tick interval
	 * tickTimeDuration (in milliseconds) represents the real time duration of each tick
	 * perform task execution for the duration of the tick (continue execution, finish execution and start new task execution) 
	 */
	public void execute(int tickTimeDuration, int currentSimulationTick) {
		//if task execution has completed update its status and notify the scheduler 
		if(isTaskExecutionComplete) {
			listTasks.remove(0); //delete from queue
			currentlyExecutedTask.setExecuted(true, tickTimeDuration, currentSimulationTick);
			currentlyExecutedTask.setTaskExecutionEndTickNumber(currentSimulationTick);
			
			//flag that there is no task set for execution
			logger.debug(getLogEntry(currentSimulationTick)+" completed task: "+currentlyExecutedTask.getId()+" execution...");
			currentlyExecutedTask = null;
			isTaskExecutionComplete = false;
		}

		if(currentlyExecutedTask == null) { //if no task currently executed-get first task from the list and initiate execution
			if(listTasks.size()>0) {
				currentlyExecutedTask = listTasks.get(0);
				logger.debug(getLogEntry(currentSimulationTick)+" starting execution of task: "+currentlyExecutedTask.getId());
				//calculate number of ticks that need to pass for the task processing to be finished
				int numberOfTicksToFinishTask = (currentlyExecutedTask.getTaskExecutionTimeMilliSeconds()+
						currentlyExecutedTask.getTaskPlacementTimeMilliSeconds())/tickTimeDuration;
				currentlyExecutedTask.setNumberOfTicksToFinishTask(numberOfTicksToFinishTask);
				currentlyExecutedTask.setTaskExecutionStartTickNumber(currentSimulationTick);
				if(currentlyExecutedTask.getJob().getJobExecutionStartTickNumber()==-1) { //set job execution start tick number
					currentlyExecutedTask.getJob().setJobExecutionStartTickNumber(currentSimulationTick);
				}
				isTaskExecutionComplete = currentlyExecutedTask.executeTask();
				logger.debug(getLogEntry(currentSimulationTick)+" starting execution of task: "+currentlyExecutedTask.getId()+" ticks to finish task: "+numberOfTicksToFinishTask);
			} else {
				logger.debug(getLogEntry(currentSimulationTick)+" no task to execute in this tick...");
			}
		} else {
			logger.debug(getLogEntry(currentSimulationTick)+" executing task: "+currentlyExecutedTask.getId()+" ticks to finish: "+currentlyExecutedTask.getNumberOfTicksToFinishTask()+" Queue: "+listTasks.size());
			isTaskExecutionComplete = currentlyExecutedTask.executeTask();
		}
	}
	
	private String getLogEntry(int currentSimulationTick) {
		return "Provider:"+getId()+" T:"+currentSimulationTick;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ArrayList<Task> getListTasks() {
		return listTasks;
	}

	public void setListTasks(ArrayList<Task> listTasks) {
		this.listTasks = listTasks;
	}

	public Task getCurrentlyExecutedTask() {
		return currentlyExecutedTask;
	}

	public void setCurrentlyExecutedTask(Task currentlyExecutedTask) {
		this.currentlyExecutedTask = currentlyExecutedTask;
	}

	
	
}
