package test;

import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * 
 * TestSuite for all classes in model package.
 * 
 * @author Team AFK
 * @version 1.0
 *
 */

public class TestSuiteModel extends TestSuite {
	private static TestSuiteModel suite() {
		TestSuiteModel testSuite = new TestSuiteModel();
		testSuite.addTestSuite(TestGoban.class);
		testSuite.addTestSuite(TestTerritory.class);
		testSuite.addTestSuite(TestTerritories.class);
		testSuite.addTestSuite(TestUser.class);
		return testSuite;
	}

	public static void main(String[] args) {
		TestRunner.run(suite());
	}
}
