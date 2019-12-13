package badcode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
public class GUI {
	private JFrame frame;
	private JButton startButton;
	private JTable table;
	
	private Main main;

	/**
	 * Creates a window and adds all the necessary components
	 * 
	 * @param main class that processes the information
	 * @see GUI#addFrameContent()
	 */
	public GUI(Main main) {
		this.main = main;
		
		//JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("GUI");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
		frame.setSize(500, 400);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}

	/**
	 * Opens the user interface
	 */
	public void open() {
		frame.setVisible(true);
	}

	/**
	 * Adds all the necessary components do the JFrame
	 */
	private void addFrameContent() {
		// Left Panel - painel com as opcoes para escolher
		JPanel featureEnvyPanel = createPanel("Feature Envy", "ATFD", "LAA");

		JPanel longMethodPanel = createPanel("Long Method", "LOC", "CYCLO");
		
		JPanel userFunc = new JPanel();
		JCheckBox box = new JCheckBox();
		JLabel label = new JLabel("UserExpression");
		JTextField text = new JTextField("ex: LAA > 20 AND ATFD > 10");
		text.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if(text.getText().contentEquals("ex: LAA > 20 AND ATFD > 10"))
					text.setText("");
			}
			
			public void focusLost(FocusEvent e) {
				if(text.getText().isEmpty())
					text.setText("ex: LAA > 20 AND ATFD > 10");
			}
		});
		
		text.setPreferredSize(new Dimension(200, 20));
		text.setEditable(false);
		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)  {
					text.setEditable(true);
				}
				else {
					text.setEditable(false);
				}
				
			}
			
		});
		userFunc.add(box);
		userFunc.add(label);
		userFunc.add(text);
		
		
		
		

		JPanel leftPanel = new JPanel(new GridLayout(3, 1));
		leftPanel.add(longMethodPanel);
		leftPanel.add(featureEnvyPanel);
		leftPanel.add(userFunc);
		// Fim Left Panel

		// RightPanel - painel com botao start
		startButton = new JButton("Start");
		startButton.setSize(100, 100);
		startButton.setPreferredSize(new Dimension(100, leftPanel.getPreferredSize().height - leftPanel.getPreferredSize().height*3/10));
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int locThreshold = -1;
				int cycloThreshold = -1;
				int atfdThreshold = -1;
				double laaThreshold = -1;
				String userExpression = "";
				try {
					boolean isFeatureEnvySelected = ((JCheckBox) featureEnvyPanel.getComponent(0)).isSelected();
					boolean isLongMethodSelected = ((JCheckBox) longMethodPanel.getComponent(0)).isSelected();
					boolean isUserExpressionSelected = ((JCheckBox) userFunc.getComponent(0)).isSelected();
					if(isFeatureEnvySelected) {
						JTextField aux = (JTextField) featureEnvyPanel.getComponent(2);
						atfdThreshold = Integer.parseInt(aux.getText());
						aux = (JTextField) featureEnvyPanel.getComponent(3);
						laaThreshold = Double.parseDouble(aux.getText());
					}
					if(isLongMethodSelected) {
						JTextField aux = (JTextField) longMethodPanel.getComponent(2);
						locThreshold = Integer.parseInt(aux.getText());
						aux = (JTextField) longMethodPanel.getComponent(3);
						cycloThreshold = Integer.parseInt(aux.getText());
					}
					if(isUserExpressionSelected) {
						JTextField aux = (JTextField) userFunc.getComponent(2);
						userExpression = aux.getText();
					}
					Results res = main.analyzeTable(locThreshold, cycloThreshold, atfdThreshold, laaThreshold, isFeatureEnvySelected, isLongMethodSelected, userExpression);
					res.displayResults();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Introduza Numeros Válidos para cada Métrica");
				} catch (NullPointerException e) {
					if(e.getMessage() != null)
						JOptionPane.showMessageDialog(null, e.getMessage());
					else
						JOptionPane.showMessageDialog(null, "Importe um ficheiro Excel!");
				}
			}
		});

		JPanel rightPanel = new JPanel();
		rightPanel.add(startButton);
		// Fim RightPanel

		JPanel searchFilePanel = createImportFilePanel();

		JPanel upperPanel = new JPanel(new BorderLayout());
		upperPanel.add(leftPanel, BorderLayout.WEST);
		upperPanel.add(searchFilePanel, BorderLayout.SOUTH);
		upperPanel.add(rightPanel, BorderLayout.EAST);


		// ScrollPane com tabela
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane excelScrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		
		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(excelScrollPane);
		

		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(upperPanel);
		panel.add(bottomPanel);

		frame.add(panel);
	}

	/**
	 * Creates a panel with a specific layout (one label and two textfields) 
	 * based on the strings in the arguments
	 * 
	 * @param label for this panel
	 * @param text for the first textfield
	 * @param text for the second textfield
	 * @return the whole panel to be added to the frame
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private JPanel createPanel(String labelText, String textfield1, String textfield2) {
		JPanel toReturn = new JPanel();

		JCheckBox box = new JCheckBox();
		JLabel label = new JLabel(labelText);
		JTextField text1 = new JTextField(textfield1);
		text1.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if(text1.getText().contentEquals(textfield1))
					text1.setText("");
			}
			
			public void focusLost(FocusEvent e) {
				if(text1.getText().isEmpty())
					text1.setText(textfield1);
			}
		});
		JTextField text2 = new JTextField(textfield2);
		text2.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				if(text2.getText().contentEquals(textfield2))
					text2.setText("");
			}
			
			public void focusLost(FocusEvent e) {
				if(text2.getText().isEmpty())
					text2.setText(textfield2);
			}
		});
		text1.setPreferredSize(new Dimension(100, 20));
		text2.setPreferredSize(new Dimension(100, 20));

		
		text1.setEditable(false);
		text2.setEditable(false);
		box.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)  {
					text1.setEditable(true);
					text2.setEditable(true);
				}
				else {
					text1.setEditable(false);
					text2.setEditable(false);
				}
				
			}
			
		});
		
		toReturn.add(box);
		toReturn.add(label);
		toReturn.add(text1);
		toReturn.add(text2);

		return toReturn;
	}

	/**
	 * Creates the panel with all the buttons related to importing 
	 * and viewing the excel file
	 * 
	 * @return panel with all the buttons to be added to the frame
	 */
	private JPanel createImportFilePanel() {
		JPanel toReturn = new JPanel();

		JTextField searchFileResultLabel = new JTextField("");
		searchFileResultLabel.setPreferredSize(new Dimension(100, 20));
		searchFileResultLabel.setEditable(false);

		JButton searchFileButton = new JButton("Search File");
		searchFileButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Select excel file to import");
				int result = fc.showOpenDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					String excelPath = fc.getSelectedFile().getAbsolutePath();
					searchFileResultLabel.setText(excelPath);
				}
			}
		});

		// botão import
		JButton importExcelButton = new JButton("Import Excel");
		importExcelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (searchFileResultLabel.getText() != "") {
					main.loadExcel(searchFileResultLabel.getText());
				}
			}
		});

		toReturn.add(searchFileButton);
		toReturn.add(searchFileResultLabel);
		toReturn.add(importExcelButton);

		return toReturn;
	}
	
	/**
	 * Displays the excel file passed in the argument in the user interface
	 * 
	 * @param workbook excel file
	 */
	@SuppressWarnings("serial")
	public void drawTable(XSSFWorkbook workbook) {
		XSSFSheet excelSheet = workbook.getSheetAt(0);
		XSSFRow header = excelSheet.getRow(0);
		String[] headerrow = new String[header.getLastCellNum()];
		for(int i = 0; i < header.getLastCellNum(); i++) 
			headerrow[i] = header.getCell(i).getStringCellValue();
		DefaultTableModel dtm = new DefaultTableModel(null, headerrow) {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		for(int row = 1; row < excelSheet.getLastRowNum() + 1; row++) {
			XSSFRow excelRow = excelSheet.getRow(row);

			XSSFCell methodID = excelRow.getCell(0);
			XSSFCell pacote = excelRow.getCell(1);
			XSSFCell classe = excelRow.getCell(2);
			XSSFCell method = excelRow.getCell(3);
			XSSFCell loc = excelRow.getCell(4);
			XSSFCell cyclo = excelRow.getCell(5);
			XSSFCell atfd = excelRow.getCell(6);
			XSSFCell laa = excelRow.getCell(7);
			XSSFCell is_long_method = excelRow.getCell(8);
			XSSFCell iplasma = excelRow.getCell(9);
			XSSFCell pmd = excelRow.getCell(10);
			XSSFCell is_feature_envy = excelRow.getCell(11);

			dtm.addRow(new Object[] { methodID, pacote, classe, method, loc, cyclo, atfd, laa,
					is_long_method, iplasma, pmd, is_feature_envy});

		}
		table.setModel(dtm);
	}
	
	
	/**
	 * @return GUI table
	 */
	public JTable getTable() {
		return table;
	}
	
	/**
	 * @return GUI frame
	 */
	public JFrame getFrame() {
		return frame;
	}
	
	/**
	 * @return GUI startButton
	 */
	public JButton getStartButton() {
		return startButton;
	}

}
