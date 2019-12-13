package badcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

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
	 * list - array with "ExcelMethods" information about functions of a existing project
	 * @see GUI, ExcelMethod
	 */
	private GUI gui;
	private ArrayList<ExcelMethod> excelMethodsList;

	/**
	 * @author Ricardo, João M., João R., Miguel.
	 * 
	 * Enum with all the fault types
	 *
	 */
	private enum FaultType {
		DCI, DII, ADCI, ADII;
	}

	/**
	 * Initiate attributes
	 * Open a window with JTextFields to enter thresholds and import an ExcelFile. 
	 */
	public Main() {
		this.gui = new GUI(this);
		this.excelMethodsList = new ArrayList<>();
		open();
	}

	/**
	 * Open a window with JTextFields to enter thresholds and import an ExcelFile. 
	 * @see GUI.open() 
	 */
	private void open() {
		gui.open();
	}

	/**
	 * If path is correct and the ExcelFile exists, load a XSSFSheet with the information of excel
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
	 * Iterates excel file with information about functions
	 * For each function see if is a Long Method or a Envy Feature based on user thresholds
	 * Compares  results between this software analyzer with other softwares analyzers (iPlasma and PMD)
	 * 
	 * @param locThreshold - LOC (logMethod threshold)
	 * @param cycloThreshold - CYCLO (logMethod threshold)
	 * @param lmLogic - logical function between longMethod thresholds (and/or)
	 * @param aftdThreshold - ATFD (envyFeature threshold)
	 * @param laaThreshold - LAA (envyFeature threshold)
	 * @param feLogic - logical function between envyFeature thresholds (and/or)
	 * 
	 * @return results
	 */
	public Results analyzeTable(int locThreshold, int cycloThreshold, int aftdThreshold, double laaThreshold, boolean featureEnvySelected, boolean longMethodSelected) {
		if (excelMethodsList.size() != 0) {
			boolean isLongMethod = false;
			boolean isFeatureEnvy = false;

			int nMethods = excelMethodsList.size();
			String indULM = "-";
			String indUFE = "-";

			ArrayList<FaultType> iPlasmasFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> PMDFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userLMFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userFEFaults = new ArrayList<FaultType>();

			// criar janela de resultados

			String[] header = { "MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy" };
			Results resultado = new Results(header);

			for (int row = 0; row < excelMethodsList.size(); row++) {
				ExcelMethod currentMethod = excelMethodsList.get(row);
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
					indULM = aux.toString();
					userLMFaults.add(aux);
				}
				// comparação regras user envy
				if (featureEnvySelected) {
					isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
					aux = getFaultType(isFeatureEnvyExcelValue, isFeatureEnvy);
					indUFE = aux.toString();
					userFEFaults.add(aux);
				}
				String[] newRow = { String.valueOf(currentMethod.getMethodID()), indPlasma, indPMD, indULM, indUFE };
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

//			resultado.displayResults();
			return resultado;
		} else {
			return null;
		}
	}

	/**
	 * @param lm - indicate if Long Method is selected in user's interface (if (lm != "") long method is selected)
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

	/**
	 * @param args - this main doesn't receive any arguments
	 */
	public static void main(String[] args) {
		new Main();
	}

}
