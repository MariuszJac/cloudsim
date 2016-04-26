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

public class TestBasicConsumer {

	private ModelConfiguration modelConfig = null;
	private ProviderInt provider = null;
	private ConsumerInt consumer = null;
	private Job job = null;
	private Task task = null;
	private Task task2 = null;

	private Job job2 = null;
	private Task task21 = null;
	private Task task22 = null;

	private void initModel() {
		modelConfig = new ModelConfiguration();
		modelConfig.setTickTimeInMilliseconds(100);
		provider = new BasicProvider(modelConfig, 0);
		consumer = new BasicConsumer(0);
		
		job = new Job(0, consumer);
		task = new Task(0, 200, 50, 100);
		job.addTask(task);
		task2 = new Task(1, 2000, 150, 100);
		job.addTask(task2);

		job2 = new Job(1, consumer);
		task21 = new Task(0, 200, 100, 100);
		job2.addTask(task21);
		task22 = new Task(1, 200, 100, 100);
		job2.addTask(task22);
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
	public void testConsumer() {
		initModel();
		assertEquals(consumer.getListJobs().size(), 0);
		//add job 0
		consumer.queueJob(100, 0, job);
		assertEquals(consumer.getListJobs().size(), 1);
		
		//add job 2
		consumer.queueJob(100, 0, job2);
		assertEquals(consumer.getListJobs().size(), 2);

		//trigger job scheduling
		consumer.scheduleJob(100, 1);
		consumer.scheduleJob(100, 2);
		consumer.scheduleJob(100, 3);
		consumer.scheduleJob(100, 4);
		consumer.scheduleJob(100, 5);
		consumer.scheduleJob(100, 6);
		assertEquals(consumer.getListJobs().size(), 0);
		
		assertEquals(job.getTicksItTakesToSchedule(), 2);
		assertEquals(job.getTicksItTakesToStartExecution(), 3);
		
		assertEquals(job2.getTicksItTakesToSchedule(), 2);
		assertEquals(job2.getTicksItTakesToStartExecution(), 5);
		//assertEquals(job.get.size(), 0);
	}

}
