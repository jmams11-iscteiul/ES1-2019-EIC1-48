/**
 * 
 */
package tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import badcode.ExcelMethod;
import badcode.Main;
import badcode.Rule;

/**
 * @author Ricardo, João M., João R., Miguel.
 *
 */
class RuleTest {

	static Rule rule;

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
		rule = new Rule("LAA", ">", 2);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link badcode.Rule#Rule(java.lang.String, java.lang.String, double)}.
	 */
	@Test
	final void testRule() {
		Rule rule1 = new Rule("ATFD", ">", 20);
		assertNotNull(rule1);

	}

	/**
	 * Test method for {@link badcode.Rule#check(badcode.ExcelMethod)}.
	 */
	@Test
	final void testCheck() {
		Main aux = new Main();
		aux.loadExcel("./resources/Long-Method.xlsx");
		ArrayList<ExcelMethod> auxList = aux.getList();

		assertFalse(rule.check(auxList.get(0)));
		rule = new Rule("ATFD", ">=", 20.0);
		assertFalse(rule.check(auxList.get(0)));
		rule = new Rule("LAA", ">", 20.0);
		assertFalse(rule.check(auxList.get(0)));
		rule = new Rule("LOC", "<", 20.0);
		assertTrue(rule.check(auxList.get(0)));
		rule = new Rule("CYCLO", "<=", 20.0);
		assertTrue(rule.check(auxList.get(0)));
		rule = new Rule("ATFD", "=", 20.0);
		assertFalse(rule.check(auxList.get(0)));

	}

	/**
	 * Test method for
	 * {@link badcode.Rule#validateArguments(java.lang.String, java.lang.String, java.lang.Double)}.
	 */
	@Test
	final void testValidateArguments() {
		NullPointerException thrown = assertThrows(NullPointerException.class,
				() -> Rule.validateArguments("cc", ">", 20.0));
		assertTrue(thrown.getMessage().contains("Metrica não existente"));

		NullPointerException thrown2 = assertThrows(NullPointerException.class,
				() -> Rule.validateArguments("LOC", "aaaa", 20.0));
		assertTrue(thrown2.getMessage().contains("Comparador não suportado"));
		Rule.validateArguments("ATFD", ">", 20.0);
		Rule.validateArguments("LOC", "<", 10.0);
		Rule.validateArguments("CYCLO", ">=", 0.0);
		Rule.validateArguments("LAA", "<=", 21.1);
		Rule.validateArguments("LAA", "=", 21.2);
	}

}
