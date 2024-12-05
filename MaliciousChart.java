package com;
import java.awt.Font;
import javax.swing.JPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.awt.event.WindowEvent;
public class MaliciousChart extends ApplicationFrame {
	static String title;
public MaliciousChart(String t) {
	super(t);
	title = t;
	setContentPane(createDemoPanel());
}
public void windowClosing(WindowEvent we){
	this.setVisible(false);
}
private static PieDataset createDataset() {
	DefaultPieDataset dataset = new DefaultPieDataset();
	dataset.setValue("Total URLS",ReadTweets.urls.size());
	dataset.setValue("Malicious URLS",Main.malicious_count);
    return dataset;        
}

private static JFreeChart createChart(PieDataset dataset) {
	JFreeChart chart = ChartFactory.createPieChart(title,dataset,true,true,false);
	PiePlot plot = (PiePlot) chart.getPlot();
    plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
    plot.setNoDataMessage("No data available");
    plot.setCircular(false);
    plot.setLabelGap(0.02);
    return chart;
}
    
  
public static JPanel createDemoPanel() {
	JFreeChart chart = createChart(createDataset());
	return new ChartPanel(chart);
}
}
