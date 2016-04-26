package test;

import static org.junit.Assert.*;

import org.junit.Test;

import cs.config.ModelConfiguration;
import cs.entities.BasicConsumer;
import cs.entities.BasicProvider;
import cs.entities.ConsumerInt;
import cs.entities.Job;
import cs.entities.ProviderInt;
import cs.entities.Task;

public class TestBasicProvider {

	private ModelConfiguration modelConfig = null;
	private ProviderInt provider = null;
	private ConsumerInt consumer = null;
	private Job job = null;
	private Task task = null;
	private Task task2 = null;
	
	private void initModel() {
		modelConfig = new ModelConfiguration();
		modelConfig.setTickTimeInMilliseconds(100);
		provider = new BasicProvider(modelConfig, 0);
		consumer = new BasicConsumer(0);
		
		job = new Job(0, consumer);
		task = new Task(0, 200, 5, 100);
		job.addTask(task);
		task2 = new Task(1, 2000, 5, 100);
		job.addTask(task2);

	}
	
	@Test
	public void testTask() {
		initModel();
		assertEquals(200, task.getTaskExecutionTimeMilliSeconds());
		assertEquals(0, task.getId());
		assertEquals(task.getJob(), job);
		assertEquals(task.getJob().getConsumer(), consumer);
	}

	@Test
	public void testProvider() {
		initModel();
		//add task 0
		assertEquals(provider.getListTasks().size(), 0);
		assertEquals(provider.requestTaskExecutionTime(task,0), 200);
		provider.requestTaskExecution(task,0);
		assertEquals(provider.getListTasks().size(), 1);
		//add task 1
		assertEquals(provider.requestTaskExecutionTime(task2,0), 2300);
		provider.requestTaskExecution(task2,0);
		//execute task 0
		provider.execute(100, 0);
		provider.execute(100, 1);
		provider.execute(100, 2);
		provider.execute(100, 3);
		assertEquals(provider.getListTasks().size(), 1);
		provider.execute(100, 2);
		assertEquals(provider.getCurrentlyExecutedTask(), task2);
		provider.execute(100, 2);
		provider.execute(100, 2);
		provider.execute(100, 2);
		assertEquals(provider.getCurrentlyExecutedTask().getNumberOfTicksToFinishTask(), 16);
	}

}
