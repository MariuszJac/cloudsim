package cs.entities;

import java.util.ArrayList;

public interface ProviderInt {
	public int getId();
	public int requestTaskExecutionTime(Task task, int currentSimulationTick);
	public void requestTaskExecution(Task task, int currentSimulationTick);
	public void execute(int tickTimeDuration, int currentSimulationTick);
	public ArrayList<Task> getListTasks();
	public Task getCurrentlyExecutedTask();

}
