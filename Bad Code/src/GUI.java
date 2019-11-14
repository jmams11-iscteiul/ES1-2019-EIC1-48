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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;


import java.awt.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class GUI {
	private JFrame frame;
	private JComboBox logic_function_result;
	private JLabel logic_function_label;
	private JCheckBox long_button;
	private JCheckBox envy_button;
	private JButton start_button;
	
	
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
		envy_button = new JCheckBox();
		JLabel feature_envy_label = new JLabel("Feature Envy");
		JTextField feature_envy_result = new JTextField("");
		feature_envy_result.setPreferredSize(new Dimension(100, 20));
		feature_envy_result.setEditable(false);
		envy_button.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				feature_envy_result.setEditable((envy_button).isSelected());
				check_logic_button();
			}
		});

		logic_function_label = new JLabel("Logic Function");
		logic_function_result = new JComboBox(new Object[] {"AND", "OR"});
		logic_function_label.setVisible(false);
		logic_function_result.setVisible(false);
		
		
		

		long_button = new JCheckBox();
		JLabel long_method_label = new JLabel("Long Method");
		JTextField long_method_result = new JTextField("");
		long_method_result.setPreferredSize(new Dimension(100, 20));
		long_method_result.setEditable(false);
		long_button.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				long_method_result.setEditable((long_button).isSelected());
				check_logic_button();
			}
		});
		
		
		

		JButton search_file = new JButton("Search File");
		JTextField search_file_result = new JTextField("");
		JButton import_excel = new JButton("Import Excel");
		search_file_result.setPreferredSize(new Dimension(100, 20));
		search_file_result.setEditable(false);

		
		
		start_button = new JButton("Start");
		start_button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String feature_envy_treshold = feature_envy_result.getText();
				String long_method_treshold	=long_method_result.getText();
				String logic = (String) logic_function_result.getSelectedItem();
				System.out.println("feature_envy_treshold " + feature_envy_treshold);
				System.out.println("logic function "+ logic);
				System.out.println("long_method_treshold " + long_method_treshold);
				
			}
			
		});
		
import_excel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		JPanel feature_envy = new JPanel();
		feature_envy.add(envy_button);
		feature_envy.add(feature_envy_label);
		feature_envy.add(feature_envy_result);

		JPanel logic_function = new JPanel();
		logic_function.add(logic_function_label);
		logic_function.add(logic_function_result);
		
		
		JPanel long_method = new JPanel();
		long_method.add(long_button);
		long_method.add(long_method_label);
		long_method.add(long_method_result);

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

		JPanel panel = new JPanel(new GridLayout(2,1));
		panel.add(upper);
		panel.add(excel);

		frame.add(panel);
	}
	
	public void check_logic_button() {
		Boolean visible = long_button.isSelected() && envy_button.isSelected();
		logic_function_label.setVisible(visible);
		logic_function_result.setVisible(visible);
	}

	private void importExcel() {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("./resources/Long-Method.xlsx"));
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado!");
		} catch (IOException e) {
			System.out.println("Erro na procura do ficheiro...");
		}
	}
	
	public static void main(String[] args) {
		GUI window = new GUI();
		window.open();
	}
}
