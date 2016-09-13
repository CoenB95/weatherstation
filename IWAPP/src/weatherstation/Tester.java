package weatherstation;

/**
 * Test class
 * @author CoenB95
 *
 */
public class Tester {

	/**
	 * Runs a test that prints all methods of
	 * {@link ConvertValues}.
	 */
	public static void run() {
		
		StringBuilder builder = new StringBuilder();
		
		builder.append(String.format("Luchtdruk: %d", 
				ConvertValues.airPressure(1)));
	}
}
