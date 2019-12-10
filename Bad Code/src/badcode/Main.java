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
	private ArrayList<ExcelMethod> list;

	private enum FaultType {
		DCI, DII, ADCI, ADII;
	}

	public Main() {
		this.gui = new GUI(this);
		this.list = new ArrayList<>();
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
				list.add(aux);
			}
			gui.drawTable(workbook);
		} else {

		}
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

	public void analyzeTable(int locThreshold, int cycloThreshold, String lmLogic, int aftdThreshold,
			double laaThreshold, String feLogic) {
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

			ArrayList<FaultType> iPlasmasFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> PMDFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userLMFaults = new ArrayList<FaultType>();
			ArrayList<FaultType> userFEFaults = new ArrayList<FaultType>();

			// criar janela de resultados

			String[] header = { "MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy" };
			Results resultado = new Results(header);

			for (int row = 0; row < list.size(); row++) {
				ExcelMethod currentMethod = list.get(row);
				nMethods++;
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
				if (lmLogic == "and")
					isLongMethod = (locFunction > locThreshold && cycloFunction > cycloThreshold);
				else if (lmLogic == "or")
					isLongMethod = (locFunction > locThreshold || cycloFunction > cycloThreshold);

				// o resultado para cada função está no isEnvyFeature
				if (feLogic == "and")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
				if (feLogic == "or")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold || laaExcelValue < laaThreshold);

				// comparação iplasma
				FaultType aux = getFaultType(isLongMethodExcelValue, iPlasmaExcelValue);
				indPlasma = aux.toString();
				iPlasmasFaults.add(aux);
				// comparação pmd
				aux = getFaultType(isLongMethodExcelValue, pmdExcelValue);
				indPMD = aux.toString();
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
					indUFE =  aux.toString();
					userFEFaults.add(aux);
				}
				String[] newRow = { String.valueOf(currentMethod.getMethodID()), indPlasma,
						indPMD, indULM, indUFE };
				resultado.addRow(newRow);
			}

			dciPlasma = Collections.frequency(iPlasmasFaults, FaultType.DCI);
			diiPlasma = Collections.frequency(iPlasmasFaults, FaultType.DII);
			adiiPlasma = Collections.frequency(iPlasmasFaults, FaultType.ADII);
			adciPlasma = Collections.frequency(iPlasmasFaults, FaultType.ADCI);

			dciPMD = Collections.frequency(PMDFaults, FaultType.DCI);
			diiPMD = Collections.frequency(PMDFaults, FaultType.DII);
			adiiPMD = Collections.frequency(PMDFaults, FaultType.ADII);
			adciPMD = Collections.frequency(PMDFaults, FaultType.ADCI);

			dciUser = Collections.frequency(userLMFaults, FaultType.DCI);
			diiUser = Collections.frequency(userLMFaults, FaultType.DII);
			adiiUser = Collections.frequency(userLMFaults, FaultType.ADII);
			adciUser = Collections.frequency(userLMFaults, FaultType.ADCI);

			dciEnvy = Collections.frequency(userFEFaults, FaultType.DCI);
			diiEnvy = Collections.frequency(userFEFaults, FaultType.DII);
			adiiEnvy = Collections.frequency(userFEFaults, FaultType.ADII);
			adciEnvy = Collections.frequency(userFEFaults, FaultType.ADCI);

			// array com todos os valores necessários para apresentar ao user
			int[][] data = { { nMethods }, { dciPlasma, diiPlasma, adiiPlasma, adciPlasma },
					{ dciPMD, diiPMD, adiiPMD, adciPMD }, { dciUser, diiUser, adiiUser, adciUser },
					{ dciEnvy, diiEnvy, adiiEnvy, adciEnvy } };
			String[] tiposDefeitos = { "DCI", "DII", "ADII", "ADCI" };
			String[] tiposInfo = tiposInfoPedido(lmLogic, feLogic);

			resultado.addResults(data, tiposDefeitos, tiposInfo);

			resultado.displayResults();
		} else {
			JOptionPane.showMessageDialog(null, "Importe um Ficheiro Excel");
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
