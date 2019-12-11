package tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import badcode.ExcelMethod;
import badcode.Main;
import badcode.Results;

class MainTest {
	static Main aux;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		 aux = new Main();

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void testMain() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadExcel() {
		Main aux = new Main();
		ArrayList<ExcelMethod> auxList = aux.getList();
		assertEquals(0, auxList.size());
		aux.loadExcel("./resources/Long-Method.xlsx");
		auxList = aux.getList();
		assertEquals(420, auxList.size());

		NullPointerException thrown = assertThrows(NullPointerException.class,
				() -> aux.loadExcel("./resources/LongMethods.xlsx"));
		assertTrue(thrown.getMessage().contains("Workboout null!"));

		aux.loadExcel("./resources/testes.txt");
	}

	@Test
	void testAnalyzeTable() {
		assertNull(aux.analyzeTable(1, 1, "AND", 1, 1, "AND"));
		aux.loadExcel("./resources/Long-Method.xlsx");
		Results temp = aux.analyzeTable(1, 1, "AND", 1, 1, "AND");
		int [][] m = temp.getMatrix();
		int [][] tempMatrix= {{420},{140,0,0,280},{140,18,0,262},{140,117,0,163},{114,55,0,251}};
		assertArrayEquals(tempMatrix, m);
		assertNotEquals(tempMatrix, new int [0][0]);
		
		temp = aux.analyzeTable(1, 1, "AND", 1, 0.1, "AND");
		temp = aux.analyzeTable(13, 18, "", 15, 5, "OR");
		temp = aux.analyzeTable(13, 18, "OR", 15, 5, "");
		temp = aux.analyzeTable(13, 18, "", 15, 5, "");
		
		Results temp2 = aux.analyzeTable(13, 18, "OR", 15, 5, "OR");
		int [][] m2 = temp2.getMatrix();
		int [][] tempMatrix2= {{420},{140,0,0,280},{140,18,0,262},{140,90,0,190},{114,306,0,0}};
		assertArrayEquals(tempMatrix2, m2);
		
	}

	@Test
	void testMain1() {
		String[] args= {};
		Main.main(args);
	}

}
