/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import badcode.GUI;
import badcode.Main;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
class GUITest {

	Main m;
	GUI g;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
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
		m = new Main();
		g = new GUI(m);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
		m = null;
		g = null;
	}

	@Test
	void testGetters() {
		assertNotNull(g.getFrame());
		assertNotNull(g.getTable().getModel());
	}

	/**
	 * Test method for {@link badcode.GUI#GUI(badcode.Main)}.
	 */
	@Test
	void testGUI() {
		GUI test = new GUI(m);
		JButton button = test.getStartButton();
	}

	/**
	 * Test method for {@link badcode.GUI#open()}.
	 */
	@Test
	void testOpen() {
		assertFalse(g.getFrame().isVisible());

		g.open();
		assertTrue(g.getFrame().isVisible());
	}

	/**
	 * Test method for
	 * {@link badcode.GUI#drawTable(org.apache.poi.xssf.usermodel.XSSFWorkbook)}.
	 */
	@Test
	void testDrawTable() {
		XSSFWorkbook workbook = null;
		try {
			workbook = new XSSFWorkbook(new FileInputStream("resources/Long-Method.xlsx"));
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado!");
		} catch (IOException e) {
			System.out.println("Erro na procura do ficheiro...");
		}

		g.drawTable(workbook);
		assertEquals(g.getTable().getColumnCount(), 12);
		assertEquals(g.getTable().getRowCount(), 420);

		XSSFRow header = workbook.getSheetAt(0).getRow(0);
		String[] headerWorkbook = new String[header.getLastCellNum()];
		for (int i = 0; i < header.getLastCellNum(); i++)
			headerWorkbook[i] = header.getCell(i).getStringCellValue();

		for (int j = 0; j < headerWorkbook.length; j++)
			assertEquals(headerWorkbook[j], g.getTable().getColumnName(j));
		
		
	}

}
