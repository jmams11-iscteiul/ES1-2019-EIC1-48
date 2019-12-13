package tests;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

import badcode.Rule;

@RunWith(JUnitPlatform.class)
@SelectClasses({ExcelMethodTest.class, GUITest.class, MainTest.class, ResultsTest.class, RuleTest.class})
public class AllTests {

}
