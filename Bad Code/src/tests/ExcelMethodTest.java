/**
 * 
 */
package tests;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import badcode.ExcelMethod;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
class ExcelMethodTest {

	private static ExcelMethod excelMethod;
	private static int methodID;
	private static String packageName;
	private static String className;
	private static String methodName;
	private static int loc;
	private static int cyclo;
	private static int atfd;
	private static float laa;
	private static boolean isLongMethod;
	private static Boolean iPlasma;
	private static Boolean pmd;
	private static boolean isFeatureEnvy;
	private static XSSFWorkbook workbook;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		workbook = new XSSFWorkbook(new FileInputStream("resources/Long-Method.xlsx"));

		XSSFSheet excelSheet = workbook.getSheetAt(0);
		XSSFRow excelRow = excelSheet.getRow(1);

		excelMethod = new ExcelMethod(excelRow);

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
	 * @throws java.lang.Exception
	 */
	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testExcelMethod() throws FileNotFoundException, IOException {
	}

	/**
	 * Test method for {@link ExcelMethod#getMethodID()}.
	 */
	@Test
	final void testGetMethodID() {
		assertEquals(methodID, excelMethod.getMethodID());
	}

	/**
	 * Test method for {@link ExcelMethod#getPackageName()}.
	 */
	@Test
	final void testGetPackageName() {
		assertEquals(packageName, excelMethod.getPackageName());
	}

	/**
	 * Test method for {@link ExcelMethod#getClassName()}.
	 */
	@Test
	final void testGetClassName() {
		assertEquals(className, excelMethod.getClassName());
	}

	/**
	 * Test method for {@link ExcelMethod#getMethodName()}.
	 */
	@Test
	final void testGetMethodName() {
		assertEquals(methodName, excelMethod.getMethodName());
	}

	/**
	 * Test method for {@link ExcelMethod#getLoc()}.
	 */
	@Test
	final void testGetLoc() {
		assertEquals(loc, excelMethod.getLoc());
	}

	/**
	 * Test method for {@link ExcelMethod#getCyclo()}.
	 */
	@Test
	final void testGetCyclo() {
		assertEquals(cyclo, excelMethod.getCyclo());
	}

	/**
	 * Test method for {@link ExcelMethod#getAtfd()}.
	 */
	@Test
	final void testGetAtfd() {
		assertEquals(atfd, excelMethod.getAtfd());
	}

	/**
	 * Test method for {@link ExcelMethod#getLaa()}.
	 */
	@Test
	final void testGetLaa() {
		assertEquals(laa, excelMethod.getLaa(), 0.0f);
	}

	/**
	 * Test method for {@link ExcelMethod#isLongMethod()}.
	 */
	@Test
	final void testIsLongMethod() {
		assertEquals(isLongMethod, excelMethod.isLongMethod());
	}

	/**
	 * Test method for {@link ExcelMethod#getiPlasma()}.
	 */
	@Test
	final void testGetiPlasma() {
		assertEquals(iPlasma, excelMethod.getiPlasma());
	}

	/**
	 * Test method for {@link ExcelMethod#getPmd()}.
	 */
	@Test
	final void testGetPmd() {
		assertEquals(pmd, excelMethod.getPmd());
	}

	/**
	 * Test method for {@link ExcelMethod#isFeatureEnvy()}.
	 */
	@Test
	final void testIsFeatureEnvy() {
		assertEquals(isFeatureEnvy, excelMethod.isFeatureEnvy());
	}

	/**
	 * Test method for {@link ExcelMethod#toString()}.
	 */
	@Test
	final void testToString() {
		String test = "[ \n" + "MethodID: " + methodID + "\nPackage: " + packageName + "\nClass: " + className + "\nMethodName: " + methodName + "\nLOC: " + loc + "\nCYCLO: "
				+ cyclo + "\nATFD: " + atfd + "\nLAA: " + laa + "\nis_long_method: " + isLongMethod + "\niPlasma: " + iPlasma
				+ "\nPMD: " + pmd + "\nis_feature_envy: " + isFeatureEnvy + "\n]";
		assertEquals(excelMethod.toString(), test);
	}

}
