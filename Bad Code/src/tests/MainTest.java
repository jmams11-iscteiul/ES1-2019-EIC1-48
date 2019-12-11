package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import badcode.ExcelMethod;
import badcode.Main;

class MainTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
		fail("Not yet implemented");
	}

	@Test
	void testMain1() {
		fail("Not yet implemented");
	}

}
