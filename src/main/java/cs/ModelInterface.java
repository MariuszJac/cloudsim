package cs;

import iplatform.init.SimulationClock;
import iplatform.model.SimulationModelSchema;
import iplatform.model.interfaces.IPlatformSetup;

import java.util.Hashtable;
import java.util.List;

import cs.bootstrap.BootstrapManager;
import cs.config.ModelConfiguration;
import cs.dataexport.DataExport;
import cs.dataexport.ExportManager;
import cs.entities.BasicProvider;
import cs.entities.ConsumerInt;
import cs.entities.ProviderInt;
import cs.entities.stats.PerformanceMonitor;
import cs.entities.workload.BasicWorkloadGenerator;
import cs.entities.workload.WorkloadGeneratorInt;
import cs.events.EventWatcherDaily;
import cs.events.EventWatcherEndSimulation;
import cs.events.EventWatcherHourly;
import cs.events.EventWatcherMinute;
import cs.events.EventWatcherMonthly;
import cs.events.EventWatcherStartSimulation;
import cs.events.EventWatcherTick;
import cs.gui.UserInterfaceManager;
import cs.gui.stats.StatsManager;
import eu.robust.simulation.entities.CIManager;
import eu.robust.simulation.entities.UserManager;

public class ModelInterface extends SimulationModelSchema implements IPlatformSetup{

	//this is class used to configure the model
	private ModelConfiguration modelConfiguration = null;
	private DataExport dataExport = null;
	//simulation management objects
	private BootstrapManager managerBootstrap = null;
	private List<ConsumerInt> listConsumersShortJobs = null;
	private List<ConsumerInt> listConsumersLongJobs = null;
	private List<ProviderInt> listProviders = null;
	private PerformanceMonitor performanceMonitor = new PerformanceMonitor();
	private WorkloadGeneratorInt workloadGenerator = new BasicWorkloadGenerator();
	private UserInterfaceManager managerUserInterface = null;
	private StatsManager managerStats = new StatsManager();
	
	public ModelInterface()
	{
		super();
	}

	@Override
	public void setSimulationClock(SimulationClock arg0) {
		// TODO Auto-generated method stub
		simulationClock = arg0;
		logger.info("successfully set simulation clock...");
	}

	@Override
	public void setModelConfiguration(Object arg0) {
		modelConfiguration = (ModelConfiguration)arg0;
		logger.info("succcessfully loaded model configuration: "+modelConfiguration.getTemplateName());
	}

	@Override
	public void registerEventWatchers(SimulationClock arg0)
	{
		arg0.registerStartSimulationClockEvent(new EventWatcherStartSimulation(this,0));
		arg0.registerEndSimulationClockEvent(new EventWatcherEndSimulation(this,1));
		arg0.registerTickClockEvent(new EventWatcherTick(this,2));
		arg0.registerMinuteClockEvent(new EventWatcherMinute(this,3));
		//arg0.registerHourlyClockEvent(new EventWatcherHourly(this,3));
		//arg0.registerDailyClockEvent(new EventWatcherDaily(this,4));
		logger.info("successfully loaded simulation events...");
	}

	public void initialiseManagers()
	{
		if (isGUIEnabled()) {
			//start user interface
			managerUserInterface = new UserInterfaceManager(this);
		}

		//set the reference to data export object that will correctly format the exported output
		managerStats.setModelInterface(this);
		dataExport = new DataExport(this);
		managerBootstrap = new BootstrapManager(this);
	}
	
	public boolean isGUIEnabled()
	{
		if (getSimulationClock().getToolConfiguration().isShowGUI()) {
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isGraphVisualisationEnabled()
	{
		if (getSimulationClock().getToolConfiguration().isShowGUI() && getModelConfiguration().enableGraphVisualisation) {
			return true;
		}
		else
		{
			return false;
		}
	}

	public SimulationClock getSimulationClock() {
		return simulationClock;
	}

	public ModelConfiguration getModelConfiguration() {
		return modelConfiguration;
	}

	public DataExport getDataExport() {
		return dataExport;
	}

	public BootstrapManager getManagerBootstrap() {
		return managerBootstrap;
	}

	public UserInterfaceManager getManagerUserInterface() {
		return managerUserInterface;
	}

	public StatsManager getManagerStats() {
		return managerStats;
	}

	public void setModelConfiguration(ModelConfiguration modelConfiguration) {
		this.modelConfiguration = modelConfiguration;
	}

	public void setDataExport(DataExport dataExport) {
		this.dataExport = dataExport;
	}

	public void setManagerBootstrap(BootstrapManager managerBootstrap) {
		this.managerBootstrap = managerBootstrap;
	}

	public void setManagerUserInterface(UserInterfaceManager managerUserInterface) {
		this.managerUserInterface = managerUserInterface;
	}

	public void setManagerStats(StatsManager managerStats) {
		this.managerStats = managerStats;
	}
	
	public List<ConsumerInt> getListConsumersShortJobs() {
		return listConsumersShortJobs;
	}

	public void setListConsumersShortJobs(List<ConsumerInt> listConsumersShortJobs) {
		this.listConsumersShortJobs = listConsumersShortJobs;
	}

	public List<ConsumerInt> getListConsumersLongJobs() {
		return listConsumersLongJobs;
	}

	public void setListConsumersLongJobs(List<ConsumerInt> listConsumersLongJobs) {
		this.listConsumersLongJobs = listConsumersLongJobs;
	}

	public List<ProviderInt> getListProviders() {
		return listProviders;
	}

	public void setListProviders(List<ProviderInt> listProviders) {
		this.listProviders = listProviders;
	}

	public PerformanceMonitor getPerformanceMonitor() {
		return performanceMonitor;
	}

	public void setPerformanceMonitor(PerformanceMonitor performanceMonitor) {
		this.performanceMonitor = performanceMonitor;
	}

	public WorkloadGeneratorInt getWorkloadGenerator() {
		return workloadGenerator;
	}

	public void setWorkloadGenerator(WorkloadGeneratorInt workloadGenerator) {
		this.workloadGenerator = workloadGenerator;
	}

	
}
