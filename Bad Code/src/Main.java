import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

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
	 * @see GUI, ExcelMethod
	 * list - array with "ExcelMethods" information about functions of a existing project
	 */
	
	private GUI gui;
	private ArrayList<ExcelMethod> list;

	/**
	 * Initiate attributes
	 * Open a window with JTextFields to enter thresholds and import an ExcelFile. 
	 */
	Main() {
		this.gui = new GUI(this);
		this.list = new ArrayList<>();
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
	 * @param path - path to an Excel File
	 * @see importExcel(String path)
	 * If path is correct and the ExcelFile exists, load a XSSFSheet with the information of excel
	 */
	public void loadExcel(String path) {
		XSSFWorkbook workbook = importExcel(path);
		if (workbook != null) {
			XSSFSheet excelSheet = workbook.getSheetAt(0);

			for (int row = 1; row < excelSheet.getLastRowNum() + 1; row++) {
				XSSFRow excelRow = excelSheet.getRow(row);
				ExcelMethod aux = new ExcelMethod(excelRow);
				list.add(aux);
			}
			gui.drawTable(workbook);
		} else {

		}
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
			JOptionPane.showMessageDialog(null, "Introduza um ficheiro do tipo XML!");
		}
		return workbook;
	}

	/**
	 * @param locThreshold - LOC (logMethod threshold)
	 * @param cycloThreshold - CYCLO (logMethod threshold)
	 * @param lmLogic - logical function between longMethod thresholds (and/or)
	 * @param aftdThreshold - ATFD (envyFeature threshold)
	 * @param laaThreshold - LAA (envyFeature threshold)
	 * @param feLogic - logical function between envyFeature thresholds (and/or)
	 * 
	 * Iterates excel file with information about functions
	 * For each function see if is a Long Method or a Envy Feature based on user thresholds
	 * Compares  results between this software analyzer with other softwares analyzers (iPlasma and PMD)
	 * 
	 */
	public void analyzeTable(int locThreshold, int cycloThreshold, String lmLogic, int aftdThreshold, double laaThreshold, String feLogic) {
		if (list.size() != 0) {
			boolean isLongMethod = false;
			boolean isFeatureEnvy = false;
			
			int nMethods = 0;

			int dciPlasma = 0, diiPlasma = 0;
			int adiiPlasma = 0, adciPlasma = 0;
			String indPlasma = "";
			
			int dciPMD = 0, diiPMD = 0;
			int adiiPMD = 0, adciPMD = 0;
			String indPMD = "";

			int dciUser = 0, diiUser = 0;
			int adciUser = 0, adiiUser = 0;
			String indULM = "-";

			int dciEnvy = 0, diiEnvy = 0;
			int adciEnvy = 0, adiiEnvy = 0;
			String indUFE = "-";			
			
			//criar janela de resultados
			
			String[] header = {"MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"};
			Results resultado = new Results(header);
	
			for (int row = 0; row < list.size(); row++) {
				ExcelMethod currentMethod = list.get(row);
				nMethods++;
				int locFunction = currentMethod.getLoc();
				int cycloFunction = currentMethod.getCyclo();
				if(lmLogic == "and")
					isLongMethod = (locFunction > locThreshold && cycloFunction > cycloThreshold);
				else if(lmLogic=="or")
					isLongMethod = (locFunction > locThreshold || cycloFunction > cycloThreshold);
				// o resultado para cada função está no isLongMethod

				// ler booleans da tabela
				boolean isLongMethodExcelValue = currentMethod.isLongMethod();
				boolean iPlasmaExcelValue = currentMethod.getiPlasma();
				boolean pmdExcelValue = currentMethod.getPmd();

				// comparação iplasma
				if (isLongMethodExcelValue && iPlasmaExcelValue) {
					dciPlasma++;
					indPlasma = "DCI";
				} else if (!isLongMethodExcelValue && iPlasmaExcelValue) {
					diiPlasma++;
					indPlasma = "DII";
				} else if (isLongMethodExcelValue && !iPlasmaExcelValue) {
					adiiPlasma++;
					indPlasma = "ADII";
				} else if(!isLongMethodExcelValue && !iPlasmaExcelValue){
					adciPlasma++;
					indPlasma = "ADCI";
				}

				// comparação pmd
				if (isLongMethodExcelValue && pmdExcelValue) {
					dciPMD++;
					indPMD = "DCI";
				} else if (!isLongMethodExcelValue && pmdExcelValue) {
					diiPMD++;
					indPMD= "DII";
				} else if (isLongMethodExcelValue && !pmdExcelValue) {
					adiiPMD++;
					indPMD= "ADII";
				} else if(!isLongMethodExcelValue && !pmdExcelValue){
					adciPMD++;
					indPMD = "ADCI";
				}

				
				// comparação regras user long
				if(!lmLogic.equals("")) {
					if (isLongMethodExcelValue && isLongMethod) {
						dciUser++;
						indULM = "DCI";
					} else if (!isLongMethodExcelValue && isLongMethod) {
						diiUser++;
						indULM = "DII";
					} else if (isLongMethodExcelValue && !isLongMethod) {
						adiiUser++;
						indULM = "ADII";
					} else if(!isLongMethodExcelValue && !isLongMethod){
						adciUser++;
						indULM = "ADCI";
					}
				}
				int atfdExcelValue = currentMethod.getAtfd();
				double laaExcelValue = currentMethod.getLaa();
				if(feLogic == "and")
				isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
				if(feLogic == "or")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold || laaExcelValue < laaThreshold);
				// o resultado para cada função está no isEnvyFeature

				boolean isFeatureEnvyExcelValue = currentMethod.isFeatureEnvy();

				// comparação regras user envy
				if(!feLogic.equals("")) {
					if (isFeatureEnvyExcelValue && isFeatureEnvy) {
						dciEnvy++;
						indUFE = "DCI";
					} else if (!isFeatureEnvyExcelValue && isFeatureEnvy) {
						diiEnvy++;
						indUFE = "DII";
					} else if (isFeatureEnvyExcelValue && !isFeatureEnvy) {
						adiiEnvy++;
						indUFE = "ADII";
					} else if(!isFeatureEnvyExcelValue && !isFeatureEnvy){
						adciEnvy++;
						indUFE = "ADCI";
					}
				}
				String[] newRow = {String.valueOf(currentMethod.getMethodID()), indPlasma, indPMD, indULM, indUFE};
				resultado.addRow(newRow);
			}

			// resultado experimental
			/*
			System.out.println("NUMBER OF METHODS:    " + nMethods);
			System.out.println("DCI iPLASMA:   " + dciPlasma + ";  DII iPLASMA:   " + diiPlasma);
			System.out.println("ADCI iPLASMA:   " + adciPlasma + ";  ADII iPLASMA:   " + adiiPlasma);
			System.out.println("DCI PMD:       " + dciPMD + ";  DII PMD:       " + diiPMD);
			System.out.println("ADCI PMD:       " + adciPMD + ";  ADII PMD:       " + adiiPMD);
			System.out.println("DCI USER LONG: " + dciUser + ";  DII USER LONG: " + diiUser);
			System.out.println("ADCI USER LONG: " + adciUser + ";  ADII USER LONG: " + adiiUser);
			System.out.println("DCI USER ENVY: " + dciEnvy + ";  DII USER ENVY: " + diiEnvy);
			System.out.println("ADCI USER ENVY: " + adciEnvy + ";  ADII USER ENVY: " + adiiEnvy);
			 */
			
			//array com todos os valores necessários para apresentar ao user
			int[][] data = {{nMethods},
					{dciPlasma, diiPlasma, adiiPlasma, adciPlasma},
					{dciPMD, diiPMD, adiiPMD, adciPMD},
					{dciUser, diiUser, adiiUser, adciUser},
					{dciEnvy, diiEnvy, adiiEnvy, adciEnvy}};
			String[] tiposDefeitos = {"DCI","DII","ADII","ADCI"};
			String[] tiposInfo = tiposInfoPedido(lmLogic, feLogic);
			
			resultado.addResults(data, tiposDefeitos, tiposInfo);
			
			resultado.displayResults();
		} else {
			JOptionPane.showMessageDialog(null, "Importe um Ficheiro Excel");
		}
	}
	
	
	
	/**
	 * @param lm - indicate if Long Method is selected in user's interface (if (lm != "") long method is selected)
	 * @param fe - indicate if Envy Feature is selected in user's interface 
	 * @return a array of string with iPlasma, PMD and thresholds selected by user 
	 */
	private String[] tiposInfoPedido(String lm, String fe) {
		if(lm.equals("") && fe.equals("")) {
			String[] ini = {"iPlasma", "PMD"};
			return ini;
		} else if(lm.equals("") && !fe.equals("")) {
			String[] ini = {"iPlasma", "PMD", "UserFeatureEnvy"};
			return ini;
		} else if(!lm.equals("") && fe.equals("")) {
			String[] ini = {"iPlasma", "PMD", "UserLongMethod"};
			return ini;
		} else {
			String[] ini = {"iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"};
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
