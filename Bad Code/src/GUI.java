import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GUI {
	private JFrame frame;
	// private JComboBox logic_function_threshold;
	// private JLabel logic_function_label;
	// private JCheckBox long_button;
	// private JCheckBox envy_button;
	private JButton startButton;
	private JTable table;
	
	private Main main;

	public GUI(Main main) {
		this.main = main;
		
		frame = new JFrame("GUI");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
		frame.setSize(800, 450);
		frame.setLocationRelativeTo(null);
	}

	public void open() {
		frame.setVisible(true);
	}

	private void addFrameContent() {
		//////////////////

		// logic_function_label = new JLabel("Logic Function");
		// logic_function_threshold = new JComboBox(new Object[] {"AND", "OR"});
		// logic_function_label.setVisible(false);
		// logic_function_threshold.setVisible(false);

		///////////////////////////////////////////////////

		// Left Panel - painel com as opcoes para escolher
		JPanel featureEnvyPanel = createPanel("Feature Envy", "ATFD", "LAA");

		JPanel longMethodPanel = createPanel("Long Method", "LOC", "CYCLO");

		JPanel leftPanel = new JPanel(new GridLayout(3, 1));
		leftPanel.add(featureEnvyPanel);
		leftPanel.add(longMethodPanel);
		// Fim Left Panel

		// RightPanel - painel com botao start
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int locThreshold = -1;
				int cycloThreshold = -1;
				int atfdThreshold = -1;
				double laaThreshold = -1;
				try {
					JTextField aux = (JTextField) featureEnvyPanel.getComponent(1);
					atfdThreshold = Integer.parseInt(aux.getText());
					aux = (JTextField) featureEnvyPanel.getComponent(2);
					laaThreshold = Double.parseDouble(aux.getText());

					aux = (JTextField) longMethodPanel.getComponent(1);
					locThreshold = Integer.parseInt(aux.getText());
					aux = (JTextField) longMethodPanel.getComponent(2);
					cycloThreshold = Integer.parseInt(aux.getText());

					main.analyzeTable(locThreshold, cycloThreshold, atfdThreshold, laaThreshold);
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Introduza Numeros Inteiros");
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

	private JPanel createPanel(String labelText, String textfield1, String textfield2) {
		JPanel toReturn = new JPanel();

		JLabel label = new JLabel(labelText);
		JTextField text1 = new JTextField(textfield1);
		JTextField text2 = new JTextField(textfield2);
		text1.setPreferredSize(new Dimension(100, 20));
		text2.setPreferredSize(new Dimension(100, 20));

		toReturn.add(label);
		toReturn.add(text1);
		toReturn.add(text2);

		return toReturn;
	}

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

		// bot�o import
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
	
	public void drawTable(XSSFWorkbook workbook) {
		XSSFSheet excelSheet = workbook.getSheetAt(0);
		String[] headerrow= {"methodID", "package", "class", "method", "loc", 
				"cyclo", "atfd", "laa", "is_long_method", "iplasma", "pmd", "is_feature_envy"};
		DefaultTableModel dtm = new DefaultTableModel(null, headerrow);
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

}
