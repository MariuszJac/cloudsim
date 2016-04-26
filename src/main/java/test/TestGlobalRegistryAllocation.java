package test;

import static org.junit.Assert.*;

import org.junit.Test;

import cs.config.ModelConfiguration;
import cs.entities.BasicConsumer;
import cs.entities.BasicProvider;
import cs.entities.ConsumerInt;
import cs.entities.GlobalRegistry;
import cs.entities.Job;
import cs.entities.ProviderInt;
import cs.entities.Task;

public class TestGlobalRegistryAllocation {

	private ModelConfiguration modelConfig = null;
	private GlobalRegistry globalRegistry = new GlobalRegistry();
	private ProviderInt provider1 = null;
	private ProviderInt provider2 = null;
	private ConsumerInt consumer = null;
	private ConsumerInt consumer2 = null;

	private Job job = null;
	private Task task = null;
	private Task task2 = null;

	private Job job2 = null;
	private Task task21 = null;
	private Task task22 = null;

	//@Test
	public void test1Consumer1ProviderModelOperation() {
		modelConfig = new ModelConfiguration();
		modelConfig.setTickTimeInMilliseconds(100);
		
		provider1 = new BasicProvider(modelConfig, 1);
		provider2 = new BasicProvider(modelConfig, 2);
		globalRegistry.addProvider(provider1);
		//globalRegistry.addProvider(provider2);
		
		consumer = new BasicConsumer(0);
		
		job = new Job(0, consumer);
		task = new Task(0, 200, 100, 100);
		job.addTask(task);
		task2 = new Task(1, 100, 100, 100);
		job.addTask(task2);

		job2 = new Job(1, consumer);
		task21 = new Task(2, 200, 100, 100);
		job2.addTask(task21);
		task22 = new Task(3, 200, 100, 100);
		job2.addTask(task22);

		
		assertEquals(consumer.getListJobs().size(), 0);
		//add job 0
		consumer.queueJob(100, 1, job);
		assertEquals(consumer.getListJobs().size(), 1);
		
		//add job 2
		consumer.queueJob(100, 1, job2);
		assertEquals(consumer.getListJobs().size(), 2);

		//run simulation clock
		int numberOfSimulationTicks = 15;
		for(int i=1;i<numberOfSimulationTicks;i++) {
			consumer.scheduleJob(100, i);
			provider1.execute(100, i);
			//provider2.execute(100, i);
		}
	}

	//@Test
	public void test1Consumer2ProvidersModelOperation() {
		modelConfig = new ModelConfiguration();
		modelConfig.setTickTimeInMilliseconds(100);
		
		provider1 = new BasicProvider(modelConfig, 1);
		provider2 = new BasicProvider(modelConfig, 2);
		globalRegistry.addProvider(provider1);
		globalRegistry.addProvider(provider2);
		
		consumer = new BasicConsumer(0);
		
		job = new Job(0, consumer);
		task = new Task(0, 100, 100, 100);
		job.addTask(task);
		task2 = new Task(1, 100, 100, 100);
		job.addTask(task2);

		job2 = new Job(1, consumer);
		task21 = new Task(2, 100, 100, 100);
		job2.addTask(task21);
		task22 = new Task(3, 100, 100, 100);
		job2.addTask(task22);

		
		assertEquals(consumer.getListJobs().size(), 0);
		//add job 0
		consumer.queueJob(100, 1, job);
		assertEquals(consumer.getListJobs().size(), 1);
		
		//add job 2
		consumer.queueJob(100, 1, job2);
		assertEquals(consumer.getListJobs().size(), 2);

		//run simulation clock
		int numberOfSimulationTicks = 15;
		for(int i=1;i<numberOfSimulationTicks;i++) {
			consumer.scheduleJob(100, i);
			provider1.execute(100, i);
			provider2.execute(100, i);
		}
	}

	@Test
	public void test2Consumers2ProvidersModelOperation() {
		modelConfig = new ModelConfiguration();
		modelConfig.setTickTimeInMilliseconds(100);
		
		provider1 = new BasicProvider(modelConfig, 1);
		provider2 = new BasicProvider(modelConfig, 2);
		globalRegistry.addProvider(provider1);
		globalRegistry.addProvider(provider2);
		
		consumer = new BasicConsumer(0);
		consumer2 = new BasicConsumer(1);
		
		job = new Job(0, consumer);
		task = new Task(0, 100, 100, 100);
		job.addTask(task);
		task2 = new Task(1, 100, 100, 100);
		job.addTask(task2);

		job2 = new Job(1, consumer2);
		task21 = new Task(2, 100, 100, 100);
		job2.addTask(task21);
		task22 = new Task(3, 100, 100, 100);
		job2.addTask(task22);

		
		assertEquals(consumer.getListJobs().size(), 0);
		//add job 0 to consumer 
		consumer.queueJob(100, 1, job);
		assertEquals(consumer.getListJobs().size(), 1);
		
		//add job 2 to consumer 2
		consumer2.queueJob(100, 1, job2);
		assertEquals(consumer2.getListJobs().size(), 1);

		//run simulation clock
		int numberOfSimulationTicks = 15;
		for(int i=1;i<numberOfSimulationTicks;i++) {
			consumer.scheduleJob(100, i);
			consumer2.scheduleJob(100, i);
			provider1.execute(100, i);
			provider2.execute(100, i);
		}
	}

}
