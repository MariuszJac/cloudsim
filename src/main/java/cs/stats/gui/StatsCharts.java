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

package cs.stats.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
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

public class StatsCharts extends JPanel {

	static Logger logger = Logger.getLogger(StatsCharts.class);
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

	private double tresholdTRT = 0;
	
	public StatsCharts(double tresholdTRT) {
		super(new BorderLayout());

		this.tresholdTRT = tresholdTRT;
		
		datasets = new TimeSeriesCollection[SUBPLOT_COUNT];

		// set the initial date according to the one specified in SystemClock object
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR, 8);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
		calendar.set(GregorianCalendar.MONTH, 2);
		calendar.set(GregorianCalendar.YEAR, 2010);

		//setup the initial date range
	    GregorianCalendar calendarForInitialDateRange = new GregorianCalendar();
	    calendarForInitialDateRange.set(GregorianCalendar.HOUR, 8);
	    calendarForInitialDateRange.set(GregorianCalendar.DAY_OF_MONTH, 1);
	    calendarForInitialDateRange.set(GregorianCalendar.MONTH, 2);
	    calendarForInitialDateRange.set(GregorianCalendar.YEAR, 2010);

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
		
		tabbedPane.addTab("Probability of thread resolution time exceeding "+tresholdTRT+" days", createChart("Probability of thread resolution time exceeding "+tresholdTRT+" days", 0));
		tabbedPane.addTab("Probability of thread resolution time exceeding "+tresholdTRT+" days", createChart("Probability of thread resolution time exceeding "+tresholdTRT+" days", 1));
		//tabbedPane.addTab("Mean thread resolution time", createChart("Mean thread resolution time", 4));
		//tabbedPane.addTab("Average Replies Number", createChart("Average Replies Number", 2));
		//tabbedPane.addTab("Demand-Supply Change", createChart("Demand-Supply Change", 3));

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
		xyItemRender.setSeriesStroke(4, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(5, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(6, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(7, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(8, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));
		xyItemRender.setSeriesStroke(9, new BasicStroke(lineThickness, BasicStroke.CAP_ROUND,
				BasicStroke.JOIN_ROUND, 0, null, 0));

		//TODO colours need to be defined statically before the renderer is created!
		xyplot = new XYPlot(datasets[type], null, numberaxis, xyItemRender);

		switch (type) {
			case 0: {
				xyItemRender.setSeriesPaint(0, Color.BLACK);
				timeseries = new TimeSeries("Reply rate 1.0", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(1, Color.BLUE);
				timeseries = new TimeSeries("Reply rate 0.9", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(2, Color.ORANGE);
				timeseries = new TimeSeries("Reply rate 0.8", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(3, Color.GREEN);
				timeseries = new TimeSeries("Reply rate 0.7", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(4, Color.MAGENTA);
				timeseries = new TimeSeries("Reply rate 0.6", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(5, Color.LIGHT_GRAY);
				timeseries = new TimeSeries("Reply rate 0.5", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(6, Color.YELLOW);
				timeseries = new TimeSeries("Reply rate 0.4", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

//				xyItemRender.setSeriesPaint(7, Color.PINK);
//				timeseries = new TimeSeries("Reply rate 0.3", org.jfree.data.time.Hour.class);
//				datasets[type].addSeries(timeseries);
//
//				xyItemRender.setSeriesPaint(8, Color.RED);
//				timeseries = new TimeSeries("Reply rate 0.2", org.jfree.data.time.Hour.class);
//				datasets[type].addSeries(timeseries);
//
//				xyItemRender.setSeriesPaint(9, Color.CYAN);
//				timeseries = new TimeSeries("Reply rate 0.1", org.jfree.data.time.Hour.class);
//				datasets[type].addSeries(timeseries);
			}
			break;
			case 1: {
				xyItemRender.setSeriesPaint(0, Color.BLACK);
				timeseries = new TimeSeries("0% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(1, Color.BLUE);
				timeseries = new TimeSeries("1% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(2, Color.ORANGE);
				timeseries = new TimeSeries("2% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(3, Color.GREEN);
				timeseries = new TimeSeries("3% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(4, Color.MAGENTA);
				timeseries = new TimeSeries("3.5% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(5, Color.LIGHT_GRAY);
				timeseries = new TimeSeries("4% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(6, Color.YELLOW);
				timeseries = new TimeSeries("4.5% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(7, Color.PINK);
				timeseries = new TimeSeries("5% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(8, Color.RED);
				timeseries = new TimeSeries("5.5% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);

				xyItemRender.setSeriesPaint(9, Color.CYAN);
				timeseries = new TimeSeries("6% of community users removed", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
			case 2: {
				xyItemRender.setSeriesPaint(0, Color.GRAY);
				timeseries = new TimeSeries("Average number of thread replies (per hour)", org.jfree.data.time.Hour.class);
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
				timeseries = new TimeSeries("Mean thread aswer time", org.jfree.data.time.Hour.class);
				datasets[type].addSeries(timeseries);
			}
			break;
		}

		NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
		//rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setTickUnit(new NumberTickUnit(0.1));

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
				dateAxis1.setTickUnit(new DateTickUnit(1, 1));
				
				if (scope1 == 0) {
					valueAxis1.setAutoRange(true);
				} else {
					valueAxis1.setFixedAutoRange(scope1);
				}

				chart = new ChartPanel(jfreechart1);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				//chart.add(buildPlotDisplayManagementPanel(valueAxis1, dateAxis1, ddlScope1));
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
				dateAxis2.setTickUnit(new DateTickUnit(1, 1));

				if (scope2 == 0) {
					valueAxis2.setAutoRange(true);
				} else {
					valueAxis2.setFixedAutoRange(scope2);
				}

				chart = new ChartPanel(jfreechart2);
				chart.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
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

	private void updateCurrentMaximumRange(int hour, int day, int month, int year) {
		// set the initial date to 01/01/2010
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(GregorianCalendar.HOUR_OF_DAY, hour);
		calendar.set(GregorianCalendar.DAY_OF_MONTH, day);
		calendar.set(GregorianCalendar.MONTH, month);
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

	public void updatePlot(int month, int year, int seriesIndex, int dataSetIndex, double value) {

//		if(dataSetIndex == 0 && seriesIndex == 5)
//		{
//			logger.info("updated plot with values: "+value+" month: "+month);
//			datasets[dataSetIndex].getSeries(seriesIndex).add(new Hour(1, 1, month, year), 1);
//		}
//		else
		{
			datasets[dataSetIndex].getSeries(seriesIndex).add(new Hour(1, 1, month, year), value);
		}
		
//		//update the upper bound on the range if we selected the scope to display all the data
		//updateCurrentMaximumRange(8, 1, 4, 2010);
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
