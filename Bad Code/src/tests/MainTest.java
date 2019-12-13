/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import badcode.ExcelMethod;
import badcode.Main;

/**
 * @author Ricardo, Joao M., Joao G., Miguel
 *
 */
class MainTest {

	static Main aux;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		aux = new Main();
	}

	/**
	 * Test method for {@link badcode.Main#Main()}.
	 */
	@Test
	final void testMain() {
		Main test = new Main();
		assertNotNull(test.getGUI());
	}

	/**
	 * Test method for {@link badcode.Main#loadExcel(java.lang.String)}.
	 */
	@Test
	final void testLoadExcel() {
		Main aux = new Main();
		ArrayList<ExcelMethod> auxList = aux.getList();
		assertEquals(0, auxList.size());
		aux.loadExcel("./resources/Long-Method.xlsx");
		auxList = aux.getList();
		assertEquals(420, auxList.size());
	}

	/**
	 * Test method for {@link badcode.Main#getList()}.
	 */
	@Test
	final void testGetList() {
		assertEquals(0, aux.getList().size());
	}
	
	/**
	 * Test method for {@link badcode.Main#getGUI()}.
	 */
	@Test
	final void testGetGUI() {
		assertNotNull(aux.getGUI());
	}

	/**
	 * Test method for {@link badcode.Main#analyzeTable(int, int, java.lang.String, int, double, java.lang.String)}.
	 */
	@Test
	final void testAnalyzeTable() {
//		assertNull(aux.analyzeTable(1, 1, 1, 1, true, true));
//		aux.loadExcel("./resources/Long-Method.xlsx");
//		Results temp = aux.analyzeTable(1, 1, 1, 1, true, true);
//		int [][] m = temp.getMatrix();
//		int [][] tempMatrix= {{420},{140,0,0,280},{140,18,0,262},{140,117,0,163},{114,55,0,251}};
//		assertArrayEquals(tempMatrix, m);
//		assertNotEquals(tempMatrix, new int [0][0]);
//		
//		temp = aux.analyzeTable(1, 1, 1, 0.1, true, true);
//		temp = aux.analyzeTable(13, 18, 15, 5, true, true);
//		temp = aux.analyzeTable(13, 18, 15, 5, true, true);
//		temp = aux.analyzeTable(13, 18, 15, 5, true, true);
//		
//		Results temp2 = aux.analyzeTable(13, 18, 15, 5, true, true);
//		int [][] m2 = temp2.getMatrix();
//		int [][] tempMatrix2= {{420},{140,0,0,280},{140,18,0,262},{117,2,23,278},{40,2,74,304}};
//		assertArrayEquals(tempMatrix2, m2);
	}

}
