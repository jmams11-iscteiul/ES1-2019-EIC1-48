package badcode;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelMethod {
	
	private int methodID;
	private String packageName;
	private String className;
	private String methodName;
	private int loc;
	private int cyclo;
	private int atfd;
	private float laa;
	private boolean isLongMethod;
	private Boolean iPlasma;
	private Boolean pmd;
	private boolean isFeatureEnvy;
	
	public ExcelMethod(XSSFRow excelRow){
		methodID = Integer.parseInt(excelRow.getCell(0).getRawValue());
		packageName = excelRow.getCell(1).toString();
		className = excelRow.getCell(2).toString();
		methodName = excelRow.getCell(3).toString();
		loc = Integer.parseInt(excelRow.getCell(4).getRawValue());
		cyclo = Integer.parseInt(excelRow.getCell(5).getRawValue());
		atfd = Integer.parseInt(excelRow.getCell(6).getRawValue());
		laa = Float.parseFloat(excelRow.getCell(7).toString());
		isLongMethod = excelRow.getCell(8).getBooleanCellValue();
		iPlasma = excelRow.getCell(9).getBooleanCellValue();
		pmd = excelRow.getCell(10).getBooleanCellValue();
		isFeatureEnvy = excelRow.getCell(11).getBooleanCellValue();
	}
	
	public int getMethodID() {
		return methodID;
	}



	public String getPackageName() {
		return packageName;
	}



	public String getClassName() {
		return className;
	}



	public String getMethodName() {
		return methodName;
	}



	public int getLoc() {
		return loc;
	}



	public int getCyclo() {
		return cyclo;
	}



	public int getAtfd() {
		return atfd;
	}



	public float getLaa() {
		return laa;
	}



	public boolean isLongMethod() {
		return isLongMethod;
	}



	public Boolean getiPlasma() {
		return iPlasma;
	}



	public Boolean getPmd() {
		return pmd;
	}



	public boolean isFeatureEnvy() {
		return isFeatureEnvy;
	}



	@Override
	public String toString() {
		String toReturn = "[ \n";
		toReturn += "MethodID: " + this.methodID + "\n"; 
		toReturn += "Package: " + this.packageName + "\n";
		toReturn += "Class: " + this.className + "\n";
		toReturn += "MethodName: " + this.methodName + "\n";
		toReturn += "LOC: " + this.loc + "\n";
		toReturn += "CYCLO: " + this.cyclo + "\n";
		toReturn += "ATFD: " + this.atfd + "\n";
		toReturn += "LAA: " + this.laa + "\n";
		toReturn += "is_long_method: " + this.isLongMethod + "\n";
		toReturn += "iPlasma: " + this.iPlasma + "\n";
		toReturn += "PMD: " + this.pmd + "\n";
		toReturn += "is_feature_envy: " + this.isFeatureEnvy + "\n";
		toReturn += "]";
		return toReturn;
	}

}
