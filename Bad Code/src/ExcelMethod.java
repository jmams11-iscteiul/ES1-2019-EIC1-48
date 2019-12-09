import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
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
	
	/**
	 * Create java class based on the excel information for easier accessibility 
	 * 
	 * @param Excel file row
	 */
	ExcelMethod(XSSFRow excelRow){
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
	
	/**
	 * @return methodID
	 */
	public int getMethodID() {
		return methodID;
	}

	/**
	 * @return method package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * @return class of the method
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return method name
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * @return number of lines of code in this method
	 */
	public int getLoc() {
		return loc;
	}

	/**
	 * @return cyclic complexity of this method
	 */
	public int getCyclo() {
		return cyclo;
	}

	/**
	 * @return number of accesses to other classes
	 */
	public int getAtfd() {
		return atfd;
	}

	/**
	 * @return number of accesses to attributes of the same class
	 */
	public float getLaa() {
		return laa;
	}

	/**
	 * @return if LongMethod is true or false in the excel file
	 */
	public boolean isLongMethod() {
		return isLongMethod;
	}

	/**
	 * @return value of the iPlasma evaluation
	 */
	public Boolean getiPlasma() {
		return iPlasma;
	}

	/**
	 * @return value of the PDM evaluation
	 */
	public Boolean getPmd() {
		return pmd;
	}

	/**
	 * @return if FeatureEnvy is true or false in the excel file
	 */
	public boolean isFeatureEnvy() {
		return isFeatureEnvy;
	}



	/* (non-Javadoc)
	 * 
	 * Prints all the atributes of this class in the console
	 * 
	 * @see java.lang.Object#toString()
	 */
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
