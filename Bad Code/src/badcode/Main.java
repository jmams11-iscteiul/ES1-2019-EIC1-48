package badcode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.swing.JOptionPane;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

	private GUI gui;
	private ArrayList<ExcelMethod> excelMethodsList;

	private enum FaultType {
		DCI, DII, ADCI, ADII;
	}

	public Main() {
		this.gui = new GUI(this);
		this.excelMethodsList = new ArrayList<>();
		open();
	}

	private void open() {
		gui.open();
	}

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
		} else {
			throw new NullPointerException("Workboout null!");
		}
	}

	public ArrayList<ExcelMethod> getList() {
		return this.excelMethodsList;
	}

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

	public Results analyzeTable(int locThreshold, int cycloThreshold, String lmLogic, int aftdThreshold, double laaThreshold, String feLogic) {
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

				// o resultado para cada função está no isLongMethod
				if (lmLogic == "AND")
					isLongMethod = (locFunction > locThreshold && cycloFunction > cycloThreshold);
				else if (lmLogic == "OR")
					isLongMethod = (locFunction > locThreshold || cycloFunction > cycloThreshold);

				// o resultado para cada função está no isEnvyFeature
				if (feLogic == "AND")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
				if (feLogic == "OR")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold || laaExcelValue < laaThreshold);

				// comparação iplasma
				FaultType aux = getFaultType(isLongMethodExcelValue, iPlasmaExcelValue);
				String indPlasma = aux.toString();
				iPlasmasFaults.add(aux);
				// comparação pmd
				aux = getFaultType(isLongMethodExcelValue, pmdExcelValue);
				String indPMD = aux.toString();
				PMDFaults.add(aux);
				// comparação regras user long
				if (!lmLogic.equals("")) {
					aux = getFaultType(isLongMethodExcelValue, isLongMethod);
					indULM = aux.toString();
					userLMFaults.add(aux);
				}
				// comparação regras user envy
				if (!feLogic.equals("")) {
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
			String[] tiposInfo = tiposInfoPedido(lmLogic, feLogic);

			resultado.addResults(data, tiposDefeitos, tiposInfo);

//			resultado.displayResults();
			return resultado;
		} else {
			JOptionPane.showMessageDialog(null, "Importe um Ficheiro Excel");
			return null;
		}
	}

	// Tipos de informação dependendo das boxes assinaladas
	private String[] tiposInfoPedido(String lm, String fe) {
		if (lm.equals("") && fe.equals("")) {
			String[] ini = { "iPlasma", "PMD" };
			return ini;
		} else if (lm.equals("") && !fe.equals("")) {
			String[] ini = { "iPlasma", "PMD", "UserFeatureEnvy" };
			return ini;
		} else if (!lm.equals("") && fe.equals("")) {
			String[] ini = { "iPlasma", "PMD", "UserLongMethod" };
			return ini;
		} else {
			String[] ini = { "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy" };
			return ini;
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
