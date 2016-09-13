package weatherstation;

import java.util.Scanner;

/**
 * Converts values received from the VP2Pro weather station
 * database to readable values.
 * 
 * @author CoenB95, Dokugan 
 * @version (a version number or a date)
 */
public class ConvertValues
{
	/**
	* Converts the air pressure.
	*
	* @param mval the value from the database.
	* @return the amount of air-pressure in mBar.
	*/
	public static double airPressure(double mval)
	{
		double mbar;
		mbar = mval / 100;
		return mbar;
	}
	
	/**
	* Converts the temperature.
	*
	* @param mval the value from the database.
	* @return the temperature in degrees Celsius.
	*/
	public static double temperature(double mval)
	{
		double celsius;
		celsius = (mval - 32) * 5/9;
		return celsius;
	}
	

	/**
	 * Converts the relative humidity
	 *
	 * @param mval the value from the database.
	 * @return the relative humidity in percents.
	 */
	public static double humidity(double mval)
	{
		return 1;
	}

	/**
	 * Converts the wind speed.
	 *
	 * @param mval the value from the database.
	 * @return the wind speed in km/h. 
	*/
	public static double windSpeed(double mval)
	{
		double kph;
		kph = mval * 1.609344;
		return kph;
	}
	
	/**
	* Converts the wind direction.
	*
	* @param mval the value from the database.
	* @return the wind direction in degrees.
	*/	
	public static double windDirection(double mval)
	{
		return 1;
	}
	
	/**
	* Converts the amount of rain.
	*
	* @param mval the value from the database.
	* @return the amount of rain in mm.
	*/	
	public static double rainAmount(double mval)
	{
		return 1;
	}
	
	/**
	* Converts the uv index.
	*
	* @param mval the value from the database.
	* @return the uv index.
	*/
	public static double uvIndex(double mval)
	{
		return 1;
	}
	
	/**
	* Converts the battery voltage.
	*
	* @param mval the value from the database.
	* @return the battery voltage.
	*/
	public static double batteryVoltage(double mval)
	{
		return 1;
	}
	
	public static void main(String[] args)
	{
		double input;
		double output;
		
		Scanner reader = new Scanner(System.in);
		input = reader.nextInt();
		
		output = airPressure(input);
		
		System.out.println(output);
		reader.close();
	}
}