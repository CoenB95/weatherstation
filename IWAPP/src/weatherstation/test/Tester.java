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
		//TODO: define a test for air pressure
		//assertEquals(?, ConvertValues.airPressure(?), ?);
		assertEquals(26.39, ConvertValues.temperature((short) 795), 0.01);
		assertEquals(51.5, ConvertValues.windSpeed((short) 32), 0.01);
		assertEquals(51.2, ConvertValues.rainAmount((short) 256), 0.01);
		assertEquals(6.6, ConvertValues.uvIndex((short) 66), 0.01);
		//Should this expected value be more hardcoded?
		assertEquals(100*300.0/512.0/100.0, ConvertValues.batteryVoltage((short) 100), 0.01);
		assertEquals("06:50", ConvertValues.sunrise((short) 650));
		assertEquals("06:50", ConvertValues.sunset((short) 650));
		
		//TODO: define a test for dew point
		//assertEquals(0, ConvertValues.dewpoint((short) 0, (short) 0), 0.01);
		
		//TODO: define a test for wind chill
		//assertEquals(0, ConvertValues.windchill((short) 0, (short) 0), 0.01);
		
		//TODO: define a test for heat index
		//assertEquals(0, ConvertValues.heatindex((short) 0, (short) 0), 0.01);
	}

}
