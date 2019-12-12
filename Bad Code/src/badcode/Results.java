package badcode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
	private JPanel right;
	private int [][] matrix;
	
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
		right = new JPanel();
		
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
		matrix=data;
		right.setLayout(new GridLayout(9,1));
		right.setBorder(new EmptyBorder(10,0,10,50));
		
		JLabel nMetodos = new JLabel("> Número de Métodos: " + data[0][0]);
		right.add(nMetodos);
		
		for(int i = 0; i < tiposInfo.length; i++) {
			JLabel tipo = new JLabel("- " + tiposInfo[i] + ": ");
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(2,2));
			
			//caso não seja necessário o UserLongMethod passar essa vetor de dados na matriz
			if(tiposInfo.length == 3 && i == 2) 
				if(tiposInfo[2].equals("UserFeatureEnvy"))
					i++;
			
			for(int j = 0; j < tiposDefeitos.length; j++) {
				JLabel defeito = new JLabel(" > " + tiposDefeitos[j] + ": " + data[i + 1][j]);
				panel.add(defeito);
			}
			right.add(tipo);
			right.add(panel);
		}
		frame.add(right, BorderLayout.EAST);
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
		
		frame.add(bottom, BorderLayout.SOUTH);
		
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
	public JPanel getRight() {
		return right;
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