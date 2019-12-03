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

public class Results {
	
	private JFrame frame;
	private JTable table;
	private DefaultTableModel dtm;
	private JPanel right;
	
	public Results(String[] headerrow) {
		
		frame = new JFrame("Analysis Results");
		frame.setLayout(new BorderLayout());
		table = new JTable();
		dtm = new DefaultTableModel(null, headerrow);
		right = new JPanel();
		
	}
	
	//adicionar linha à tabela de resultados
	public void addRow(String[] args) {
		dtm.addRow(args);
	}
	
	//data[1-4
	//data[0] numero métodos
	//data[1] info iPlasma
	//data[2] info PMD
	//data[3] info User Long method
	//data[4] info Envy
	//ex: data[0][0] dci, data[0][1] dii, data[0][2] adii, data[0][3] adci
	public void addResults(int[][] data, String[] tiposDefeitos, String[] tiposInfo) {
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
	
	//Adicionar tabela com a informação dos defeitos à frame
	private void addContent() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		table.setDefaultRenderer(Object.class, centerRenderer);
		table.setModel(dtm);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		JScrollPane tableSP = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(tableSP, BorderLayout.WEST);
		
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridLayout(2,1));
		bottom.setBorder(new EmptyBorder(0,5,0,0));
		
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
	
	//Pôr a informação na frame
	public void displayResults() {
		addContent();
		frame.setPreferredSize(new Dimension(700, 500));
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
}