/**
 * 
 */
package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import badcode.Results;


/**
 * @author 
 *
 */
class ResultsTest {
	
	static Results r;
	static Results r1;
	static Results r2;
	static Results r3;
	static String[] header = {"MethodID", "iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"};
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		r = new Results(header);
		r1 = new Results(header);
		r2 = new Results(header);
		r3 = new Results(header);
	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterAll
//	void tearDownAfterClass() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@BeforeEach
//	void setUp() throws Exception {
//	}
//
//	/**
//	 * @throws java.lang.Exception
//	 */
//	@AfterEach
//	void tearDown() throws Exception {
//	}
	
	@Test
	void testGetters() {
		Results rg = new Results(header);
		JTable aux = rg.getTable();
		assertEquals(rg.getDTM().getRowCount(), aux.getModel().getRowCount());
		String[] a = {"1",".2","a", "a","s"};
		rg.addRow(a);
		assertEquals(r.getDTM().getRowCount(), aux.getModel().getRowCount());
		assertTrue(rg.getRight() instanceof JPanel);
	}

	@Test
	void test() {
		assertNotNull(r);
		
		String[] aux = {"1", "DCII", "DCII", "DCII", "DCII"};
		r.addRow(aux);
		r1.addRow(aux);
		r2.addRow(aux);
		r3.addRow(aux);
		assertEquals(r.getDTM().getRowCount(), 1);
		
		int[][] dt = {{200},
				{50, 50, 50, 50},
				{40, 60, 50, 50},
				{100, 0, 0, 100},
				{70, 120, 10, 0}};
		String[] td = {"DCI","DII","ADII","ADCI"};
		String[] s = {"iPlasma", "PMD"};
		String[] s1 = {"iPlasma", "PMD", "UserFeatureEnvy"};
		String[] s2 = {"iPlasma", "PMD", "UserLongMethod"};
		String[] s3 = {"iPlasma", "PMD", "UserLongMethod", "UserFeatureEnvy"};
				
		r.addResults(dt, td, s);
		assertEquals(((JLabel)r.getRight().getComponent(0)).getText(),"> Número de Métodos: " + dt[0][0]);
		
		assertEquals(((JLabel)r.getRight().getComponent(1)).getText(),"- " + s[0] + ": ");
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(2)).getComponent(0)).getText()," > " + td[0] + ": " + dt[1][0]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(2)).getComponent(1)).getText()," > " + td[1] + ": " + dt[1][1]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(2)).getComponent(2)).getText()," > " + td[2] + ": " + dt[1][2]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(2)).getComponent(3)).getText()," > " + td[3] + ": " + dt[1][3]);
		
		assertEquals(((JLabel)r.getRight().getComponent(3)).getText(),"- " + s[1] + ": ");
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(4)).getComponent(0)).getText()," > " + td[0] + ": " + dt[2][0]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(4)).getComponent(1)).getText()," > " + td[1] + ": " + dt[2][1]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(4)).getComponent(2)).getText()," > " + td[2] + ": " + dt[2][2]);
		assertEquals(((JLabel)((JPanel)r.getRight().getComponent(4)).getComponent(3)).getText()," > " + td[3] + ": " + dt[2][3]);
	
	
		
	
	
	
	
	
	
	
	
	
	
	}
	
}
