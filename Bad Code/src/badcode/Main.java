package badcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
public class Main {
	/**
	 * list - array with "ExcelMethods" information about functions of a existing
	 * project
	 * 
	 * @see GUI, ExcelMethod
	 */
	private GUI gui;
	private ArrayList<ExcelMethod> excelMethodsList;

	/**
	 * @author Ricardo, João M., João R., Miguel.
	 * 
	 *         Enum with all the fault types
	 *
	 */
	private enum FaultType {
		DCI, DII, ADCI, ADII;
	}

	/**
	 * Initiate attributes Open a window with JTextFields to enter thresholds and
	 * import an ExcelFile.
	 */
	public Main() {
		this.gui = new GUI(this);
		this.excelMethodsList = new ArrayList<>();
		open();
	}

	/**
	 * Open a window with JTextFields to enter thresholds and import an ExcelFile.
	 * 
	 * @see GUI.open()
	 */
	private void open() {
		gui.open();
	}

	/**
	 * If path is correct and the ExcelFile exists, load a XSSFSheet with the
	 * information of excel
	 * 
	 * @param path - path to an Excel File
	 * @see Main#importExcel(String)
	 */
	public void loadExcel(String path) {
		XSSFWorkbook workbook = importExcel(path);
		if (workbook != null) {
			XSSFSheet excelSheet = workbook.getSheetAt(0);

			for (int row = 1; row < excelSheet.getLastRowNum() + 1; row++) {
				XSSFRow excelRow = excelSheet.getRow(row);
				ExcelMethod aux = new ExcelMethod(excelRow);
				excelMethodsList.add(aux);
			}
			gui.drawTable(workbook);
		}
	}

	/**
	 * @return list with all the ExcelMethods loaded
	 */
	public ArrayList<ExcelMethod> getList() {
		return this.excelMethodsList;
	}

	/**
	 * @return GUI
	 */
	public GUI getGUI() {
		return gui;
	}

	/**
	 * @param path - path to an existing excel file
	 * @return XSSFWorkbook to read the information of Excel
	 */
	private XSSFWorkbook importExcel(String path) {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream(path));
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado!");
		} catch (IOException e) {
			System.out.println("Erro na procura do ficheiro...");
		} catch (NotOfficeXmlFileException e) {
			JOptionPane.showMessageDialog(null, "Introduza um ficheiro Excel!");
		}
		return workbook;
	}

	/**
	 * Return the fault type based on inputs
	 * 
	 * @param a
	 * @param b
	 * @return fault type
	 */
	private FaultType getFaultType(boolean a, boolean b) {
		FaultType temp = null;
		if (a && b) {
			temp = FaultType.DCI;
		} else if (!a && b) {
			temp = FaultType.DII;
		} else if (a && !b) {
			temp = FaultType.ADII;
		} else if (!a && !b) {
			temp = FaultType.ADCI;
		}
		return temp;
	}

	/**
	 * Iterates excel file with information about functions For each function see if
	 * is a Long Method or a Envy Feature based on user thresholds Compares results
	 * between this software analyzer with other softwares analyzers (iPlasma and
	 * PMD)
	 * 
	 * @param locThreshold   - LOC (logMethod threshold)
	 * @param cycloThreshold - CYCLO (logMethod threshold)
	 * @param lmLogic        - logical function between longMethod thresholds
	 *                       (and/or)
	 * @param aftdThreshold  - ATFD (envyFeature threshold)
	 * @param laaThreshold   - LAA (envyFeature threshold)
	 * @param feLogic        - logical function between envyFeature thresholds
	 *                       (and/or)
	 * 
	 * @return results
	 */
	public Results analyzeTable(int locThreshold, int cycloThreshold, int aftdThreshold, double laaThreshold,
			boolean featureEnvySelected, boolean longMethodSelected, String userExpression) {
		if (excelMethodsList.size() != 0) {
			boolean isLongMethod = false;
			boolean isFeatureEnvy = false;

			int nMethods = excelMethodsList.size();
			String faultULM = "-";
			String faultUFE = "-";
			String faultUR = "-";
			boolean userExpressionValid = validateUserExpression(userExpression);

			ArrayList<FaultType> iPlasmasFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> PMDFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userLMFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userFEFaults = new ArrayList<FaultType>();

			// criar janela de resultados

			String[] header = { "MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy", "UserRule" };
			Results resultado = new Results(header);

			Rule[] rules = null;
			LinkedList<String> logicOperators = null;
			if (!userExpression.equals("")) {
				if (!userExpressionValid)
					throw new NullPointerException("Regra não Suportada!");

				String[] ruleArray = userExpression.split("AND|OR");
				rules = new Rule[ruleArray.length];
				for (int i = 0; i < ruleArray.length; i++) {
					String[] textRule = ruleArray[i].trim().split(" ");
					Rule r = new Rule(textRule[0].trim(), textRule[1].trim(), Double.parseDouble(textRule[2].trim()));
					rules[i] = r;
				}

				logicOperators = new LinkedList<String>();
				String[] allWords = userExpression.split(" ");
				for (int i = 0; i < allWords.length; i++) {
					if (allWords[i].trim().equals("AND") || allWords[i].trim().equals("OR"))
						logicOperators.add(allWords[i].trim());
				}
			}

			for (int row = 0; row < excelMethodsList.size(); row++) {
				ExcelMethod currentMethod = excelMethodsList.get(row);
				if (!userExpression.equals(""))
					faultUR = userMetricValue(rules, logicOperators, currentMethod).toString().toUpperCase();
				int locFunction = currentMethod.getLoc();
				int cycloFunction = currentMethod.getCyclo();
				int atfdExcelValue = currentMethod.getAtfd();
				double laaExcelValue = currentMethod.getLaa();
				// ler booleans da tabela
				boolean isFeatureEnvyExcelValue = currentMethod.isFeatureEnvy();
				boolean isLongMethodExcelValue = currentMethod.isLongMethod();
				boolean iPlasmaExcelValue = currentMethod.getiPlasma();
				boolean pmdExcelValue = currentMethod.getPmd();

				// comparação iplasma
				FaultType aux = getFaultType(isLongMethodExcelValue, iPlasmaExcelValue);
				String indPlasma = aux.toString();
				iPlasmasFaults.add(aux);
				// comparação pmd
				aux = getFaultType(isLongMethodExcelValue, pmdExcelValue);
				String indPMD = aux.toString();
				PMDFaults.add(aux);
				// comparação regras user long
				if (longMethodSelected) {
					isLongMethod = (locFunction > locThreshold && cycloFunction > cycloThreshold);
					aux = getFaultType(isLongMethodExcelValue, isLongMethod);
					faultULM = aux.toString();
					userLMFaults.add(aux);
				}
				// comparação regras user envy
				if (featureEnvySelected) {
					isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
					aux = getFaultType(isFeatureEnvyExcelValue, isFeatureEnvy);
					faultUFE = aux.toString();
					userFEFaults.add(aux);
				}
				String[] newRow = { String.valueOf(currentMethod.getMethodID()), indPlasma, indPMD, faultULM, faultUFE,
						faultUR };
				resultado.addRow(newRow);
			}

			int dciPlasma = Collections.frequency(iPlasmasFaults, FaultType.DCI);
			int diiPlasma = Collections.frequency(iPlasmasFaults, FaultType.DII);
			int adiiPlasma = Collections.frequency(iPlasmasFaults, FaultType.ADII);
			int adciPlasma = Collections.frequency(iPlasmasFaults, FaultType.ADCI);

			int dciPMD = Collections.frequency(PMDFaults, FaultType.DCI);
			int diiPMD = Collections.frequency(PMDFaults, FaultType.DII);
			int adiiPMD = Collections.frequency(PMDFaults, FaultType.ADII);
			int adciPMD = Collections.frequency(PMDFaults, FaultType.ADCI);

			int dciUser = Collections.frequency(userLMFaults, FaultType.DCI);
			int diiUser = Collections.frequency(userLMFaults, FaultType.DII);
			int adiiUser = Collections.frequency(userLMFaults, FaultType.ADII);
			int adciUser = Collections.frequency(userLMFaults, FaultType.ADCI);

			int dciEnvy = Collections.frequency(userFEFaults, FaultType.DCI);
			int diiEnvy = Collections.frequency(userFEFaults, FaultType.DII);
			int adiiEnvy = Collections.frequency(userFEFaults, FaultType.ADII);
			int adciEnvy = Collections.frequency(userFEFaults, FaultType.ADCI);

			// array com todos os valores necessários para apresentar ao user
			int[][] data = { { nMethods }, { dciPlasma, diiPlasma, adiiPlasma, adciPlasma },
					{ dciPMD, diiPMD, adiiPMD, adciPMD }, { dciUser, diiUser, adiiUser, adciUser },
					{ dciEnvy, diiEnvy, adiiEnvy, adciEnvy } };
			String[] tiposDefeitos = { "DCI", "DII", "ADII", "ADCI" };
			String[] tiposInfo = tiposInfoPedido(longMethodSelected, featureEnvySelected);

			resultado.addResults(data, tiposDefeitos, tiposInfo);

			return resultado;
		} else {
			return null;
		}
	}

	/**
	 * @param lm - indicate if Long Method is selected in user's interface (if (lm
	 *           != "") long method is selected)
	 * @param fe - indicate if Envy Feature is selected in user's interface
	 * @return a array of string with iPlasma, PMD and thresholds selected by user
	 */
	private String[] tiposInfoPedido(boolean lm, boolean fe) {
		if (!lm && !fe) {
			String[] ini = { "iPlasma", "PMD" };
			return ini;
		} else if (!lm && fe) {
			String[] ini = { "iPlasma", "PMD", "UserFeatureEnvy" };
			return ini;
		} else if (lm && !fe) {
			String[] ini = { "iPlasma", "PMD", "UserLongMethod" };
			return ini;
		} else {
			String[] ini = { "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy" };
			return ini;
		}
	}

	private Boolean userMetricValue(Rule[] rules, LinkedList<String> ops, ExcelMethod em) {
		boolean b = rules[0].check(em);
		for (int i = 1; i < rules.length - 1; i++) {
			boolean aux = rules[i].check(em);
			if (ops.get(i - 1).equals("AND"))
				b = b && aux;
			if (ops.get(i - 1).equals("OR"))
				b = b || aux;
		}
		return b;
	}

	private Boolean validateUserExpression(String rule) {
		String[] splitted = rule.split("AND|OR");
		if(splitted.length == 0)
			return false;
		for (int i = 0; i < splitted.length; i++) {
			String[] textRule = splitted[i].trim().split(" ");
			if (textRule.length != 3) {
				return false;
			} else {
				try {
					Rule.validateArguments(textRule[0].trim(), textRule[1].trim(),
							Double.parseDouble(textRule[2].trim()));
					int ao = 0;
					String[] allWords = rule.split(" ");
					for (int j = 0; j < allWords.length; j++)
						if (allWords[j].trim().equals("AND") || allWords[j].trim().equals("OR"))
							ao++;
					if (ao != splitted.length - 1) {
						return false;
					}
				} catch (NullPointerException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					return false;
				}
			}
		}

		return true;

	}

	/**
	 * @param args - this main doesn't receive any arguments
	 */
	public static void main(String[] args) {
		new Main();
	}

}
