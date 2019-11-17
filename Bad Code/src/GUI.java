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

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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


public class GUI {
	private JFrame frame;
	private JComboBox logic_function_threshold;
	private JLabel logic_function_label;
	private JCheckBox long_button;
	private JCheckBox envy_button;
	private JButton start_button;
	private JTable table;
	
	
	public GUI() {
		frame = new JFrame("GUI");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
		frame.setSize(500, 300);
		//frame.setSize(1280, 720);
		frame.setLocationRelativeTo(null);
	}
	public void open() {
		frame.setVisible(true);
	}
	
	
	private void addFrameContent() {
		//////////////////envy and long buttons
		envy_button = new JCheckBox();
		JLabel feature_envy_label = new JLabel("Feature Envy");
		JTextField envy_threshold = new JTextField("");
		envy_threshold.setPreferredSize(new Dimension(100, 20));
		envy_threshold.setEditable(false);
		envy_button.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				envy_threshold.setEditable((envy_button).isSelected());
				check_logic_button();
			}
		});

		logic_function_label = new JLabel("Logic Function");
		logic_function_threshold = new JComboBox(new Object[] {"AND", "OR"});
		logic_function_label.setVisible(false);
		logic_function_threshold.setVisible(false);
		
		
		long_button = new JCheckBox();
		JLabel long_method_label = new JLabel("Long Method");
		JTextField long_threshold = new JTextField("");
		long_threshold.setPreferredSize(new Dimension(100, 20));
		long_threshold.setEditable(false);
		long_button.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				long_threshold.setEditable((long_button).isSelected());
				check_logic_button();
			}
		});
		///////////////////////////////////////////////////
		
		

		JButton search_file = new JButton("Search File");
		JTextField search_file_result = new JTextField("");
		search_file_result.setPreferredSize(new Dimension(100, 20));
		search_file_result.setEditable(false);
		
		
		
		search_file.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setDialogTitle("Select excel file to import");
				int result = fc.showOpenDialog(null);
				if( result == JFileChooser.APPROVE_OPTION) {
					String excelPath = fc.getSelectedFile().getAbsolutePath();
					search_file_result.setText(excelPath);
				}
			}
			
		});

		
		
		start_button = new JButton("Start");
		start_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String feature_envy_treshold = envy_threshold.getText();
				String long_method_treshold	=long_threshold.getText();
				String logic = (String) logic_function_threshold.getSelectedItem();
				System.out.println("feature_envy_treshold " + feature_envy_treshold);
				System.out.println("logic function "+ logic);
				System.out.println("long_method_treshold " + long_method_treshold);
				
			}
			
		});
		
		//ScrollPane com tabela
		table = new JTable();
		JScrollPane jsp = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		//botão import
		JButton import_excel = new JButton("Import Excel");
		import_excel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					if(search_file_result.getText() != "") {
						XSSFWorkbook workbook = importExcel(search_file_result.getText());
						if(workbook != null) {
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
						
			}
		});
		
		
		/////////////
		JPanel feature_envy = new JPanel();
		feature_envy.add(envy_button);
		feature_envy.add(feature_envy_label);
		feature_envy.add(envy_threshold);

		JPanel logic_function = new JPanel();
		logic_function.add(logic_function_label);
		logic_function.add(logic_function_threshold);
		
		
		JPanel long_method = new JPanel();
		long_method.add(long_button);
		long_method.add(long_method_label);
		long_method.add(long_threshold);

		JPanel left = new JPanel(new GridLayout(3,1));
		left.add(feature_envy);
		left.add(logic_function);
		left.add(long_method);
		
		JPanel right = new JPanel();
		right.add(start_button);

		JPanel search = new JPanel();
		search.add(search_file);
		search.add(search_file_result);
		search.add(import_excel);
		

		JPanel upper = new JPanel(new BorderLayout());
		upper.add(left, BorderLayout.WEST);
		upper.add(search, BorderLayout.SOUTH);
		upper.add(right, BorderLayout.EAST);


		JPanel excel = new JPanel(new BorderLayout());
		//excel_sheet
		//excel.add(excel_sheet);
		excel.add(jsp);

		JPanel panel = new JPanel(new GridLayout(2,1));
		panel.add(upper);
		panel.add(excel);

		frame.add(panel);
		//////////////
	}
	
	public void check_logic_button() {
		Boolean visible = long_button.isSelected() && envy_button.isSelected();
		logic_function_label.setVisible(visible);
		logic_function_threshold.setVisible(visible);
	}

	private XSSFWorkbook importExcel(String path) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado!");
		} catch (IOException e) {
			System.out.println("Erro na procura do ficheiro...");
		}
		return workbook;
	}
	
	public static void main(String[] args) {
		GUI window = new GUI();
		window.open();
	}
}
