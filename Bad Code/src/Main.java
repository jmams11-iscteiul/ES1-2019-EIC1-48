import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Main {

	private GUI gui;
	private ArrayList<ExcelMethod> list;

	Main() {
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
		}
		return workbook;
	}

	public void analyzeTable(int locThreshold, int cycloThreshold, String lmLogic, int aftdThreshold, double laaThreshold, String feLogic) {
		if (list.size() != 0) {
			boolean isLongMethod = false;
			boolean isFeatureEnvy = false;

			int dciPlasma = 0, diiPlasma = 0;
			int adiiPlasma = 0, adciPlasma = 0;

			int dciPMD = 0, diiPMD = 0;
			int adiiPMD = 0, adciPMD = 0;

			int dciUser = 0, diiUser = 0;
			int adciUser = 0, adiiUser = 0;

			int dciEnvy = 0, diiEnvy = 0;
			int adciEnvy = 0, adiiEnvy = 0;

			for (int row = 0; row < list.size(); row++) {
				ExcelMethod currentMethod = list.get(row);
				int locFunction = currentMethod.getLoc();
				int cycloFunction = currentMethod.getCyclo();
				if(lmLogic == "and")
					isLongMethod = (locFunction > locThreshold && cycloFunction > cycloThreshold);
				else if(lmLogic=="or")
					isLongMethod = (locFunction > locThreshold || cycloFunction > cycloThreshold);
				// o resultado para cada função está no isLongMethod
				// table.setValueAt(isLongMethod, row, 8);

				// ler booleans da tabela
				boolean isLongMethodExcelValue = currentMethod.isLongMethod();
				boolean iPlasmaExcelValue = currentMethod.getiPlasma();
				boolean pmdExcelValue = currentMethod.getPmd();

				// comparação iplasma
				if (isLongMethodExcelValue && iPlasmaExcelValue) {
					dciPlasma++;
				} else if (!isLongMethodExcelValue && iPlasmaExcelValue) {
					diiPlasma++;
				} else if (isLongMethodExcelValue && !iPlasmaExcelValue) {
					adiiPlasma++;
				} else if(!isLongMethodExcelValue && !iPlasmaExcelValue){
					adciPlasma++;
				}

				// comparação pmd
				if (isLongMethodExcelValue && pmdExcelValue) {
					dciPMD++;
				} else if (!isLongMethodExcelValue && pmdExcelValue) {
					diiPMD++;
				} else if (isLongMethodExcelValue && !pmdExcelValue) {
					adiiPMD++;
				} else if(!isLongMethodExcelValue && !pmdExcelValue){
					adciPMD++;
				}

				// comparação regras user long
				if (isLongMethodExcelValue && isLongMethod) {
					dciUser++;
				} else if (!isLongMethodExcelValue && isLongMethod) {
					diiUser++;
				} else if (isLongMethodExcelValue && !isLongMethod) {
					adiiUser++;
				} else if(!isLongMethodExcelValue && !isLongMethod){
					adciUser++;
				}

				int atfdExcelValue = currentMethod.getAtfd();
				double laaExcelValue = currentMethod.getLaa();
				if(feLogic == "and")
				isFeatureEnvy = (atfdExcelValue > aftdThreshold && laaExcelValue < laaThreshold);
				if(feLogic == "or")
					isFeatureEnvy = (atfdExcelValue > aftdThreshold || laaExcelValue < laaThreshold);
				// o resultado para cada função está no isEnvyFeature
				// table.setValueAt(isFeatureEnvy, row, 11);

				boolean isFeatureEnvyExcelValue = currentMethod.isFeatureEnvy();

				// comparação regras user envy
				if (isFeatureEnvyExcelValue && isFeatureEnvy) {
					dciEnvy++;
				} else if (!isFeatureEnvyExcelValue && isFeatureEnvy) {
					diiEnvy++;
				} else if (isFeatureEnvyExcelValue && !isFeatureEnvy) {
					adiiEnvy++;
				} else if(!isFeatureEnvyExcelValue && !isFeatureEnvy){
					adciEnvy++;
				}
			}

			// resultado experimental
			System.out.println("DCI iPLASMA:   " + dciPlasma + ";  DII iPLASMA:   " + diiPlasma);
			System.out.println("ADCI iPLASMA:   " + adciPlasma + ";  ADII iPLASMA:   " + adiiPlasma);
			System.out.println("DCI PMD:       " + dciPMD + ";  DII PMD:       " + diiPMD);
			System.out.println("ADCI PMD:       " + adciPMD + ";  ADII PMD:       " + adiiPMD);
			System.out.println("DCI USER LONG: " + dciUser + ";  DII USER LONG: " + diiUser);
			System.out.println("ADCI USER LONG: " + adciUser + ";  ADII USER LONG: " + adiiUser);
			System.out.println("DCI USER ENVY: " + dciEnvy + ";  DII USER ENVY: " + diiEnvy);
			System.out.println("ADCI USER ENVY: " + adciEnvy + ";  ADII USER ENVY: " + adiiEnvy);

			//			String funLog = (String) logic_function_threshold.getSelectedItem();
			//			boolean result;
			//			// RESULT NAO É USADO PARA NADA
			//			if (funLog == "AND")
			//				result = (isLongMethod && isEnvyFeature);
			//			if (funLog == "OR")
			//				result = (isLongMethod || isEnvyFeature);
		} else {
			JOptionPane.showMessageDialog(null, "Importe um Ficheiro Excel");
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
