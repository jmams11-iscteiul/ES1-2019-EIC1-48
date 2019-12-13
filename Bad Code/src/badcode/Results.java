package badcode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
public class Results {
	
	/**
	 * table - table to display results 
	 */
	private JFrame frame;
	private JTable table;
	private DefaultTableModel dtm;
	private JPanel text;
	private JTabbedPane tab;
	private int [][] matrix;
	private JFreeChart chart;
	
	/**
	 * @param headerrow - columns to create in the table to display results
	 * {"MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"}
	 * Initiates variables 
	 */
	public Results(String[] headerrow) {
		
		frame = new JFrame("Analysis Results");
		frame.setLayout(new BorderLayout());
		table = new JTable();
		dtm = new DefaultTableModel(null, headerrow) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		tab = new JTabbedPane();
		text = new JPanel();
		
	}
	
	/**
	 * Add line to table results
	 * 
	 * @param args - information about a a method or function
	 * {"MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"}
	 * create and add a row with information about a method to table
	 */
	public void addRow(String[] args) {
		dtm.addRow(args);
	}
	
	/**
	 * data[0] number methods
	 * data[1] info iPlasma
	 * data[2] info PMD
	 * data[3] info User Long method
	 * data[4] info Envy
	 * ex: data[0][0] dci, data[0][1] dii, data[0][2] adii, data[0][3] adci
	 * 
	 * @param data array with information about methods, id and different software analyzers results
	 * @param tiposDefeitos - {"DCI","DII","ADII","ADCI"}
	 * 
	 * @param tiposInfo- iPlmas, PMD and others defects created by user
	 * {"iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"};
	 */
	public void addResults(int[][] data, String[] tiposDefeitos, String[] tiposInfo) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		matrix = data;
		text.setLayout(new GridLayout(13,1));
		text.setBorder(new EmptyBorder(10,0,10,0));
		
		JLabel nMetodos = new JLabel("> Número de Métodos: " + data[0][0]);
		nMetodos.setHorizontalAlignment(JLabel.CENTER);
		text.add(nMetodos);
		
		int k = 0;
		for(int i = 0; i < tiposInfo.length; i++) {
			JLabel tipo = new JLabel("- " + tiposInfo[i] + ": ");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));
					
			//caso não seja necessário o UserLongMethod passar essa vetor de dados na matriz
			if(tiposInfo.length == 3 && i == 2) 
				if(tiposInfo[2].equals("UserFeatureEnvy"))
					k = 1;
			
			for(int j = 0; j < tiposDefeitos.length; j++) {
				JLabel defeito = new JLabel(" > " + tiposDefeitos[j] + ": " + data[i + 1 + k][j]);
				defeito.setHorizontalAlignment(JLabel.CENTER);
				dataset.addValue(data[i + 1 + k][j], tiposInfo[i], tiposDefeitos[j]);
				panel.add(defeito);
			}

			float uip = (((float)data[i + 1 + k][1] + (float)data[i + 1 + k][2])/(float)data[0][0])*100;
			String uipS = String.format ("%.2f", uip);
			JLabel uncorrectlyIdentifiedFaultPercentage = new JLabel("=> Uncorretly Identified Fault Percentage: " + uipS + "%");
			uncorrectlyIdentifiedFaultPercentage.setHorizontalAlignment(JLabel.CENTER);
			
			tipo.setHorizontalAlignment(JLabel.CENTER);
			text.add(tipo);
			panel.setOpaque(true);
			panel.setBackground(Color.WHITE);
			text.add(panel);
			uncorrectlyIdentifiedFaultPercentage.setOpaque(true);
			uncorrectlyIdentifiedFaultPercentage.setBackground(Color.WHITE);
			text.add(uncorrectlyIdentifiedFaultPercentage);
		}
		
		tab.addTab("Text", text);
		
		chart = createChart(dataset);
		ChartPanel cp = new ChartPanel(chart);
		tab.addTab("Chart", cp);
	}
	
	
	/**
	 * 
	 * @param cds Dataset with the analyzer and user rules results
	 * @return Bar Chart with the info from the dataset
	 */
	private JFreeChart createChart(CategoryDataset cds) {
		JFreeChart fc = ChartFactory.createBarChart("Faults", "Fault Types", "Value", cds, PlotOrientation.VERTICAL, true, true, false);
		
		//Customize chart
		fc.setBackgroundPaint(Color.white);
		CategoryPlot cplot = (CategoryPlot)fc.getPlot();
	    ((BarRenderer)cplot.getRenderer()).setBarPainter(new StandardBarPainter());

	    BarRenderer r = (BarRenderer)fc.getCategoryPlot().getRenderer();
	    r.setSeriesPaint(0, Color.blue);
		
		return fc;
	}
	
	/**
	 *  add content to frame:
	 *  table with information about defects in different methods
	 *  panel with statistics about defects analysis with iPlasma, PMD and this software
	 *  panel with subtitle
	 *  
	 */
	private void addContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(Object.class, centerRenderer);
		table.setModel(dtm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane tableSP = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(tableSP, BorderLayout.WEST);
		
		//Jpanel com a legenda
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(2,1));
		bottom.setBorder(new EmptyBorder(0,5,0,0));
		
		//legenda
		JLabel legenda = new JLabel("Subtitle:");
		bottom.add(legenda);
		
		JPanel aux = new JPanel();
		aux.setLayout(new GridLayout(2,2));
		aux.setBorder(new EmptyBorder(0,5,10,10));
		
		JLabel dci = new JLabel("DCI - Defeitos Corretamente Identificados");
		JLabel dii = new JLabel("DII - Defeitos Incorretamente Identificados");
		JLabel adci = new JLabel("ADCI - Ausência de Defeitos Corretamente Identificados");
		JLabel adii = new JLabel("ADII - Ausência de Defeitos Incorretamente Identificados");
		
		aux.add(dci);
		aux.add(dii);
		aux.add(adci);
		aux.add(adii);
		
		bottom.add(aux);
		
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add(tableSP, BorderLayout.CENTER);
		p.add(bottom, BorderLayout.SOUTH);
		tab.add("Table", p);
		
		frame.add(tab);
		//frame.add(bottom, BorderLayout.SOUTH);
		
	}
	
	
	
	/**
	 * set information in frame
	 * display frame
	 */
	public void displayResults() {
		addContent();
		frame.setPreferredSize(new Dimension(700, 500));
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * @return table
	 */
	public JTable getTable() {
		return table;
	}
	
	/**
	 * @return defaultTableModel
	 */
	public DefaultTableModel getDTM() {
		return dtm;
	}
	
	/**
	 * @return right panel
	 */
	public JPanel getTextPanel() {
		return text;
	}
	
	/**
	 * @return matrix with data
	 */
	public int[][] getMatrix() {
		return matrix;
	}

	/**
	 * @return frame
	 */
	public JFrame getFrame() {
		return frame;
	}
}