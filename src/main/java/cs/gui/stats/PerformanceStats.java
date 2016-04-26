//########################################################################
//#
//# � University of Southampton IT Innovation Centre, 2011 
//# Copyright in this library belongs to the University of Southampton 
//# University Road, Highfield, Southampton, UK, SO17 1BJ 
//# This software may not be used, sold, licensed, transferred, copied 
//# or reproduced in whole or in part in any manner or form or in or 
//# on any media by any person other than in accordance with the terms 
//# of the Licence Agreement supplied with the software, or otherwise 
//# without the prior written consent of the copyright owners.
//#
//# This software is distributed WITHOUT ANY WARRANTY, without even the 
//# implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, 
//# except where stated in the Licence Agreement supplied with the software.
//#
//#	Created By :			Mariusz Jacyno
//#	Created Date :			2011-08-05
//#	Created for Project :	ROBUST
//#
//########################################################################

package cs.gui.stats;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.UnitType;

import cs.ModelInterface;

public class PerformanceStats extends JPanel {

	static Logger logger = Logger.getLogger(PerformanceStats.class);
	ModelInterface modelInterface = null;
	
	private JFreeChart jfreechart1 = null;
	private JFreeChart jfreechart2 = null;
	private JFreeChart jfreechart3 = null;
	private JFreeChart jfreechart4 = null;
	private JFreeChart jfreechart5 = null;

	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private TimeSeriesCollection datasets[];
	public final int SUBPLOT_COUNT = 5;
	public JPanel jpanel = null;
	public ChartPanel chartpanel1 = null;
	public ChartPanel chartpanel2 = null;
	public ChartPanel chartpanel3 = null;
	public ChartPanel chartpanel4 = null;
	public ChartPanel chartpanel5 = null;
	
	public CombinedDomainXYPlot combineddomainxyplot1 = null;
	public CombinedDomainXYPlot combineddomainxyplot2 = null;
	public CombinedDomainXYPlot combineddomainxyplot3 = null;
	public CombinedDomainXYPlot combineddomainxyplot4 = null;
	public CombinedDomainXYPlot combineddomainxyplot5 = null;

	public XYPlot xyplot1 = null;
	public XYPlot xyplot2 = null;
	public XYPlot xyplot3 = null;
	public XYPlot xyplot4 = null;
	public XYPlot xyplot5 = null;

	//fast renderer (updated JFreechart code for this and recompiled the library)
	public StandardXYItemRenderer xyItemRender1 = null;
	//original renderer
	//public StandardXYItemRenderer xyItemRender1 = null;
	//fast renderer (updated JFreechart code for this and recompiled the library)
	public StandardXYItemRenderer xyItemRender2 = null;
	//original renderer
	//public StandardXYItemRenderer xyItemRender2 = null;
	public StandardXYItemRenderer xyItemRender3 = null;
	public StandardXYItemRenderer xyItemRender4 = null;
	public StandardXYItemRenderer xyItemRender5 = null;

	public int numberOfAgentsInTheLastSample = 0;
	public boolean initialState = true;
	public float lineThickness = 2.0f;
	private double lastValue[];
	//range management parameters
	private long scope1 = 0;
	private long scope2 = 0;
	private long scope3 = 0;
	private long scope4 = 0;
	private long scope5 = 0;
	
	private DateRange initialDateRange1 = null;
	private DateRange initialDateRange2 = null;
	private DateRange initialDateRange3 = null;
	private DateRange initialDateRange4 = null;
	private DateRange initialDateRange5 = null;

	//1st panel values
	NumberAxis numberaxis1 = null;
	DateAxis dateAxis1 = null;
	//2nd panel values
	NumberAxis numberaxis2 = null;
	ValueAxis valueaxis2 = null;
	DateAxis dateAxis2 = null;

	ValueAxis valueAxis1 = null, valueAxis2 = null, valueAxis3 = null, valueAxis4 = null, valueAxis5 = null;

	NumberAxis numberAxis3 = null;
	DateAxis dateAxis3 = null;

	NumberAxis numberAxis4 = null;
	DateAxis dateAxis4 = null;

	NumberAxis numberAxis5 = null;
	DateAxis dateAxis5 = null;

	private DateRange initialDateRange = null;

	ChartPanel chart1 = null;
	ChartPanel chart2 = null;
	public JTabbedPane tabbedPane;

	private StatsManager statsManager = null;

	public PerformanceStats(ModelInterface modelInterfaceRef, StatsManager statsManagerRer) {
		super(new BorderLayout());

		this.modelInterface = modelInterfaceRef;
		this.statsManager = statsManagerRer;
		datasets = new TimeSeriesCollection[SUBPLOT_COUNT];

		// set the initial date according to the one specified in SystemClock object
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR_OF_DAY, modelInterface.getSimulationClock().getStartTime().get(Calendar.HOUR_OF_DAY));
		calendar.set(GregorianCalendar.DAY_OF_MONTH, modelInterface.getSimulationClock().getStartTime().get(Calendar.DAY_OF_MONTH));
		calendar.set(GregorianCalendar.MONTH, modelInterface.getSimulationClock().getStartTime().get(Calendar.MONTH));
		calendar.set(GregorianCalendar.YEAR, modelInterface.getSimulationClock().getStartTime().get(Calendar.YEAR));

		//setup the initial date range
	    GregorianCalendar calendarForInitialDateRange = new GregorianCalendar();
	    calendarForInitialDateRange.set(GregorianCalendar.HOUR_OF_DAY, modelInterface.getSimulationClock().getStartTime().get(Calendar.HOUR_OF_DAY));
	    calendarForInitialDateRange.set(GregorianCalendar.DAY_OF_MONTH, modelInterface.getSimulationClock().getStartTime().get(Calendar.DAY_OF_MONTH));
	    calendarForInitialDateRange.set(GregorianCalendar.MONTH, modelInterface.getSimulationClock().getStartTime().get(Calendar.MONTH));
	    calendarForInitialDateRange.set(GregorianCalendar.YEAR, modelInterface.getSimulationClock().getStartTime().get(Calendar.YEAR));

	    initialDateRange = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    initialDateRange1 = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    initialDateRange2 = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    initialDateRange3 = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    initialDateRange4 = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    initialDateRange5 = new DateRange(calendarForInitialDateRange.getTime(),calendar.getTime());
	    
		JPanel tabbedPanel = createChartTab();
		add(tabbedPanel, "North");
	}

	private JPanel createChartTab() {

		JPanel mainPanel = new JPanel();
		tabbedPane = new JTabbedPane();

		tabbedPane.addTab("Community Throughput", createChart("Community Throughput", 0));
		tabbedPane.addTab("Thread Resolution Time", createChart("Thread Resolution Time", 1));
		tabbedPane.addTab("Mean Thread Answer Time", createChart("Mean Thread Answer Time", 4));
		tabbedPane.addTab("Number of Thread Replies", createChart("Number of Thread Replies", 2));
		tabbedPane.addTab("Demand-Supply Change", createChart("Demand-Supply Change", 3));

		mainPanel.add(tabbedPane);

		return mainPanel;
	}

	private ChartPanel createChart(String axisLabel, int type) {
		int width = (screenSize.width - (screenSize.width / 35));
		int height = (int) (screenSize.height - (screenSize.height / (double) 4));

		//int width = (screenSize.width - (screenSize.width / 35)) / 2;
		//int height = (int) (screenSize.height - (screenSize.height / (double) 4)) / 2;

		lastValue = new double[SUBPLOT_COUNT];
		//================= visualise standard chart ==========================
		CombinedDomainXYPlot combineddomainxyplot = new CombinedDomainXYPlot(new DateAxis("Time"));

		lastValue[0] = 100D;
		TimeSeries timeseries = null;
		XYPlot xyplot = null;

		//add new timeseries to modularity row
		datasets[type] = new TimeSeriesCollection();

		NumberAxis numberaxis = new NumberAxis(axisLabel);
		numberaxis.setAutoRangeIncludesZero(true);

		//fast
		StandardXYItemRenderer xyItemRender = new StandardXYItemRenderer();
		//original
		//xyItemRender = new StandardXYItemRenderer();

		xyItemRender.setSeriesStroke(0, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(1, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(2, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(3, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));

		//TODO colours need to be defined statically before the renderer is created!
		xyplot = new XYPlot(datasets[type], null, numberaxis, xyItemRender);
		
		switch (type) {
			case 0: {
				xyItemRender.setSeriesPaint(0, Color.BLACK);
				timeseries = new TimeSeries("Number of existing threads", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
				timeseries = new TimeSeries("Number of answered threads", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
			case 1: {
				xyItemRender.setSeriesPaint(0, Color.YELLOW);
				timeseries = new TimeSeries("Mean thread answer time (per day)", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
			case 2: {
				xyItemRender.setSeriesPaint(0, Color.GRAY);
				timeseries = new TimeSeries("Number of thread replies", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
			case 3: {
				xyItemRender.setSeriesPaint(0, Color.GREEN);
				timeseries = new TimeSeries("New thread arrival activity change (%)", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
				xyItemRender.setSeriesPaint(1, Color.BLUE);
				timeseries = new TimeSeries("Thread reply activity change (%)", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
			case 4: {
				xyItemRender.setSeriesPaint(0, Color.GREEN);
				timeseries = new TimeSeries("Mean thread aswer time (per month)", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
		}

		NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		xyplot.setBackgroundPaint(Color.lightGray);
		xyplot.setDomainGridlinePaint(Color.white);
		xyplot.setRangeGridlinePaint(Color.white);
		combineddomainxyplot.add(xyplot);

		ChartPanel chart = null;

		switch (type) {
			case 0: {
				jfreechart1 = new JFreeChart("", combineddomainxyplot);
				LegendTitle legendtitle = (LegendTitle) jfreechart1.getSubtitle(0);

				//set legend fonts
				jfreechart1.getLegend(0).setItemFont(new Font("Italic", Font.PLAIN, 11));

				legendtitle.setPosition(RectangleEdge.BOTTOM);
				legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));
				jfreechart1.setBorderPaint(Color.black);
				jfreechart1.setBorderVisible(true);
				jfreechart1.setBackgroundPaint(Color.white);
				combineddomainxyplot.setBackgroundPaint(Color.lightGray);
				combineddomainxyplot.setDomainGridlinePaint(Color.white);
				combineddomainxyplot.setRangeGridlinePaint(Color.white);
				combineddomainxyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

				valueAxis1 = combineddomainxyplot.getDomainAxis();
				dateAxis1 = (DateAxis) valueAxis1;

				if (scope1 == 0) {
					valueAxis1.setAutoRange(true);
				} else {
					valueAxis1.setFixedAutoRange(scope1);
				}

				chart = new ChartPanel(jfreechart1);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				chart.add(buildPlotDisplayManagementPanel(valueAxis1, dateAxis1, ddlScope1));
				combineddomainxyplot.setInsets(new RectangleInsets(40, 25, 0, 10));
				chart.setPreferredSize(new Dimension(width, height));

				jfreechart1.setAntiAlias(false);
			}
			break;
			case 1: {

				jfreechart2 = new JFreeChart("", combineddomainxyplot);
				LegendTitle legendtitle = (LegendTitle) jfreechart2.getSubtitle(0);

				//set legend fonts
				jfreechart2.getLegend(0).setItemFont(new Font("Italic", Font.PLAIN, 11));

				legendtitle.setPosition(RectangleEdge.BOTTOM);
				legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));
				jfreechart2.setBorderPaint(Color.black);
				jfreechart2.setBorderVisible(true);
				jfreechart2.setBackgroundPaint(Color.white);
				combineddomainxyplot.setBackgroundPaint(Color.lightGray);
				combineddomainxyplot.setDomainGridlinePaint(Color.white);
				combineddomainxyplot.setRangeGridlinePaint(Color.white);
				combineddomainxyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

				valueAxis2 = combineddomainxyplot.getDomainAxis();
				dateAxis2 = (DateAxis) valueAxis2;

				if (scope2 == 0) {
					valueAxis2.setAutoRange(true);
				} else {
					valueAxis2.setFixedAutoRange(scope2);
				}

				chart = new ChartPanel(jfreechart2);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				chart.add(buildPlotDisplayManagementPanel(valueAxis2, dateAxis2, ddlScope2));
				combineddomainxyplot.setInsets(new RectangleInsets(40, 25, 0, 10));
				chart.setPreferredSize(new Dimension(width, height));

				jfreechart2.setAntiAlias(false);
			}
			break;
			case 2: {

				jfreechart3 = new JFreeChart("", combineddomainxyplot);
				LegendTitle legendtitle = (LegendTitle) jfreechart3.getSubtitle(0);

				//set legend fonts
				jfreechart3.getLegend(0).setItemFont(new Font("Italic", Font.PLAIN, 11));

				legendtitle.setPosition(RectangleEdge.BOTTOM);
				legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));
				jfreechart3.setBorderPaint(Color.black);
				jfreechart3.setBorderVisible(true);
				jfreechart3.setBackgroundPaint(Color.white);
				combineddomainxyplot.setBackgroundPaint(Color.lightGray);
				combineddomainxyplot.setDomainGridlinePaint(Color.white);
				combineddomainxyplot.setRangeGridlinePaint(Color.white);
				combineddomainxyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

				valueAxis3 = combineddomainxyplot.getDomainAxis();
				dateAxis3 = (DateAxis) valueAxis3;

				if (scope3 == 0) {
					valueAxis3.setAutoRange(true);
				} else {
					valueAxis3.setFixedAutoRange(scope3);
				}

				chart = new ChartPanel(jfreechart3);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				chart.add(buildPlotDisplayManagementPanel(valueAxis3, dateAxis3, ddlScope3));
				combineddomainxyplot.setInsets(new RectangleInsets(40, 25, 0, 10));
				chart.setPreferredSize(new Dimension(width, height));

				jfreechart3.setAntiAlias(false);
			}
			break;
			case 3: {
				jfreechart4 = new JFreeChart("", combineddomainxyplot);
				LegendTitle legendtitle = (LegendTitle) jfreechart4.getSubtitle(0);

				//set legend fonts
				jfreechart4.getLegend(0).setItemFont(new Font("Italic", Font.PLAIN, 11));

				legendtitle.setPosition(RectangleEdge.BOTTOM);
				legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));
				jfreechart4.setBorderPaint(Color.black);
				jfreechart4.setBorderVisible(true);
				jfreechart4.setBackgroundPaint(Color.white);
				combineddomainxyplot.setBackgroundPaint(Color.lightGray);
				combineddomainxyplot.setDomainGridlinePaint(Color.white);
				combineddomainxyplot.setRangeGridlinePaint(Color.white);
				combineddomainxyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

				valueAxis4 = combineddomainxyplot.getDomainAxis();
				dateAxis4 = (DateAxis) valueAxis4;

				if (scope4 == 0) {
					valueAxis4.setAutoRange(true);
				} else {
					valueAxis4.setFixedAutoRange(scope4);
				}

				chart = new ChartPanel(jfreechart4);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				chart.add(buildPlotDisplayManagementPanel(valueAxis4, dateAxis4, ddlScope4));
				combineddomainxyplot.setInsets(new RectangleInsets(40, 25, 0, 10));
				chart.setPreferredSize(new Dimension(width, height));

				jfreechart4.setAntiAlias(false);
			}
			break;
			case 4: {
				jfreechart5 = new JFreeChart("", combineddomainxyplot);
				LegendTitle legendtitle = (LegendTitle) jfreechart5.getSubtitle(0);

				//set legend fonts
				jfreechart5.getLegend(0).setItemFont(new Font("Italic", Font.PLAIN, 11));

				legendtitle.setPosition(RectangleEdge.BOTTOM);
				legendtitle.setMargin(new RectangleInsets(UnitType.ABSOLUTE, 0.0D, 4D, 0.0D, 4D));
				jfreechart5.setBorderPaint(Color.black);
				jfreechart5.setBorderVisible(true);
				jfreechart5.setBackgroundPaint(Color.white);
				combineddomainxyplot.setBackgroundPaint(Color.lightGray);
				combineddomainxyplot.setDomainGridlinePaint(Color.white);
				combineddomainxyplot.setRangeGridlinePaint(Color.white);
				combineddomainxyplot.setAxisOffset(new RectangleInsets(4D, 4D, 4D, 4D));

				valueAxis5 = combineddomainxyplot.getDomainAxis();
				dateAxis5 = (DateAxis) valueAxis5;

				if (scope5 == 0) {
					valueAxis5.setAutoRange(true);
				} else {
					valueAxis5.setFixedAutoRange(scope5);
				}

				chart = new ChartPanel(jfreechart5);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				chart.add(buildPlotDisplayManagementPanel(valueAxis5, dateAxis5, ddlScope5));
				combineddomainxyplot.setInsets(new RectangleInsets(40, 25, 0, 10));
				chart.setPreferredSize(new Dimension(width, height));

				jfreechart5.setAntiAlias(false);
			}
			break;
		}

		return chart;

	}
	
	//scope of displayed data management
	JComboBox ddlScope1 = new JComboBox();

	//scope of displayed data management
	JComboBox ddlScope2 = new JComboBox();

	//scope of displayed data management
	JComboBox ddlScope3 = new JComboBox();

	//scope of displayed data management
	JComboBox ddlScope4 = new JComboBox();

	//scope of displayed data management
	JComboBox ddlScope5 = new JComboBox();

	private JComboBox fillScopeDDL(JComboBox ddlScope) {
		//ddlScope.addItem("All");
		ddlScope.addItem("One Day");
		ddlScope.addItem("Ten Days");
		ddlScope.addItem("One Month");
		ddlScope.addItem("Two Months");
		ddlScope.addItem("Four Months");
		ddlScope.addItem("Six Months");
		ddlScope.addItem("Ten Months");
		ddlScope.addItem("One Year");

		return ddlScope;
	}

	public void updatePlot() {
//		logger.info("!!!!!!!!!!!!!!!!!!!!: "+
//				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY)+" "+
//				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH)+" "+
//				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1+" "+
//				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR));
		
		datasets[0].getSeries(0).add(new Hour(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR)), statsManager.getTotalThreadsNew());
		datasets[0].getSeries(1).add(new Hour(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR)), statsManager.getTotalThreadsResolved());
		datasets[1].getSeries(0).add(new Hour(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR)), statsManager.getMeanThreadResolutionTime());
		datasets[2].getSeries(0).add(new Hour(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR)), statsManager.getAvereageThreadRepliesNumber());

		datasets[3].getSeries(0).add(new Hour(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR)), statsManager.getNewThreadArrivalRateChangePercentage());

		//update the upper bound on the range if we selected the scope to display all the data
		updateCurrentMaximumRange(modelInterface.getSimulationClock().getCurrentTime().get(Calendar.HOUR_OF_DAY), 
				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.DAY_OF_MONTH), 
				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.MONTH)+1, 
				modelInterface.getSimulationClock().getCurrentTime().get(Calendar.YEAR));
	}

	private void updateCurrentMaximumRange(int hour, int day, int month, int year) {
		// set the initial date to 01/01/2010
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
		calendar.set(GregorianCalendar.MONTH, month - 1);
		calendar.set(GregorianCalendar.YEAR, year);
		
		Date currentDate = calendar.getTime();
		if (scope1 == 0) {
			dateAxis1.setMaximumDate(currentDate);
		}
		if (scope2 == 0) {
			dateAxis2.setMaximumDate(currentDate);
		}
		if (scope3 == 0) {
			dateAxis3.setMaximumDate(currentDate);
		}
		if (scope4 == 0) {
			dateAxis4.setMaximumDate(currentDate);
		}
		if (scope5 == 0) {
			dateAxis5.setMaximumDate(currentDate);
		}
	}

	public void turnOffChartUpdateNotifications(boolean turnOff) {
		jfreechart1.setNotify(!turnOff);
		jfreechart2.setNotify(!turnOff);
		jfreechart3.setNotify(!turnOff);
		jfreechart4.setNotify(!turnOff);
		jfreechart5.setNotify(!turnOff);
	}
	
	private JPanel createBottomPanel(JPanel bottomChartPanel, JPanel generalStatsPanel) {
		JPanel bottomPanel = new JPanel();

		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(generalStatsPanel, "North");
		bottomPanel.add(bottomChartPanel, "South");

		return bottomPanel;
	}

	public JPanel buildPlotDisplayManagementPanel(ValueAxis valueAxis, DateAxis dateAxis, JComboBox ddlScope) {
		JPanel panel = new JPanel(new FlowLayout());

		JLabel lblDisplayScope = new JLabel("Time Range:");

		ddlScope = fillScopeDDL(ddlScope);
		lblDisplayScope.setFont(new Font("Italic", 1, 10));
		ddlScope.setFont(new Font("Italic", 1, 10));

		panel.add(lblDisplayScope);
		panel.add(ddlScope);

		ddlScope.addActionListener(new ComboBoxListener(valueAxis, dateAxis));

		return panel;
	}

	class ComboBoxListener implements ActionListener {

		private ValueAxis valueAxis = null;
		private DateAxis dateAxis = null;

		public ComboBoxListener(ValueAxis valueAxis, DateAxis dateAxis) {
			this.valueAxis = valueAxis;
			this.dateAxis = dateAxis;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			int selectedIndex = ((JComboBox) e.getSource()).getSelectedIndex();
			String strSelectedItemValue = (String) ((JComboBox) e.getSource()).getSelectedItem();

			logger.info("changing chart display resolution -> selected item: " + selectedIndex + " value: " + strSelectedItemValue);

			if (e.getSource().equals(ddlScope1)) {
				setScope(1, strSelectedItemValue, valueAxis1, dateAxis1);
			} else if (e.getSource().equals(ddlScope2)){
				setScope(2, strSelectedItemValue, valueAxis2, dateAxis2);
			} else if (e.getSource().equals(ddlScope3)){
				setScope(3, strSelectedItemValue, valueAxis3, dateAxis3);
			} else if (e.getSource().equals(ddlScope4)){
				setScope(4, strSelectedItemValue, valueAxis4, dateAxis4);
			} else if (e.getSource().equals(ddlScope5)){
				setScope(5, strSelectedItemValue, valueAxis5, dateAxis5);
			}
		}
	}


	private void setScope(int type, String strSelectedItemValue, ValueAxis valueAxis, DateAxis dateAxis) {
		long scope = 0;

		if (strSelectedItemValue.equals("All")) {
			scope = 0;
		} else if (strSelectedItemValue.equals("One Day")) {
			scope = 86500;
		} else if (strSelectedItemValue.equals("Ten Days")) {
			scope = 865000;
		} else if (strSelectedItemValue.equals("One Month")) {
			scope = 2600000;
		} else if (strSelectedItemValue.equals("Two Months")) {
			scope = 2 * 2600000;
		} else if (strSelectedItemValue.equals("Four Months")) {
			scope = 4 * 2600000;
		} else if (strSelectedItemValue.equals("Six Months")) {
			scope = 6 * 2600000;
		} else if (strSelectedItemValue.equals("Ten Months")) {
			scope = 10 * 2600000;
		} else if (strSelectedItemValue.equals("One Year")) {
			scope = 12 * 2600000;
		}

		//set the values based on scope
		if (scope == 0) {
			valueAxis.setAutoRange(true);
			dateAxis.setMinimumDate(initialDateRange.getLowerDate());
		} else {
			valueAxis.setAutoRange(true);
			valueAxis.setFixedAutoRange(scope);
		}

		switch (type) {
			case 1: {
				scope1 = scope;
			}
			break;
			case 2: {
				scope2 = scope;
			}
			break;
			case 3: {
				scope3 = scope;
			}
			break;
			case 4: {
				scope4 = scope;
			}
			break;
			case 5: {
				scope5 = scope;
			}
			break;
		}
	}
}
