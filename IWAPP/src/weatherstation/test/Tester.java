package weatherstation.test;

import static org.junit.Assert.*;

import org.junit.Test;

import weatherstation.ConvertValues;

/**
 * The JUnit test-class for this application.
 * Tip: the javadoc location needs to be changed to 
 * {@link http://junit.sourceforge.net/javadoc/} in
 * order to work.
 * @author CoenB95
 *
 */
public class Tester {

	@Test
	public void test() {
		//assertEquals(1000, ConvertValues.airPressure(32000), 0.001);
		assertEquals(26.39, ConvertValues.temperature((short) 795), 0.01);
		assertEquals(51.5, ConvertValues.windSpeed((short) 32), 0.01);
		assertEquals(1000, ConvertValues.airPressure(32000), 0.01);
		assertEquals(1000, ConvertValues.airPressure(32000), 0.01);
	}

}
