import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

public class GUI {
	private JFrame frame;

	public GUI() {
		frame = new JFrame("GUI");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		addFrameContent();
		frame.pack();
		frame.setSize(1280, 720);
		frame.setLocationRelativeTo(null);
	}
	public void open() {
		frame.setVisible(true);
	}
	private void addFrameContent() {
		JCheckBox envy_button = new JCheckBox();
		JLabel feature_envy_label = new JLabel("Feature Envy");
		JTextField feature_envy_result = new JTextField("");
		feature_envy_result.setPreferredSize(new Dimension(100, 20));
		feature_envy_result.setEditable(false);

		JLabel logic_function_label = new JLabel("Logic Function");
		JComboBox logic_function_result = new JComboBox(new Object[] {"AND", "OR"});

		JCheckBox long_button = new JCheckBox();
		JLabel long_method_label = new JLabel("Long Method");
		JTextField long_method_result = new JTextField("");
		long_method_result.setPreferredSize(new Dimension(100, 20));
		long_method_result.setEditable(false);

		JButton search_file = new JButton("Search File");
		JTextField search_file_result = new JTextField("");
		JButton import_excel = new JButton("Import Excel");
		search_file_result.setPreferredSize(new Dimension(100, 20));
		search_file_result.setEditable(false);

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

		JPanel search = new JPanel();
		search.add(search_file);
		search.add(search_file_result);
		search.add(import_excel);

		JPanel upper = new JPanel(new BorderLayout());
		upper.add(left, BorderLayout.WEST);
		upper.add(search, BorderLayout.SOUTH);


		JPanel excel = new JPanel(new BorderLayout());
		//excel_sheet
		//excel.add(excel_sheet);

		JPanel panel = new JPanel(new GridLayout(2,1));
		panel.add(upper);
		panel.add(excel);

		frame.add(panel);
	}
	public static void main(String[] args) {
		GUI window = new GUI();
		window.open();
	}
}
