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
		mbar = mval / 1000 * 3386.38866667;
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
		celsius = (mval/10 - 32)/ 1.8;
		return celsius;
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
	* Converts the amount of rain.
	* 
	* @param mval the value from the database.
	* @return the amount of rain in mm.
	*/	
	public static double rainAmount(double mval)
	{
		double rain;
		rain = mval *0.2;
		return rain;
	}

	
	/**
	* Converts the uv index.
	*
	* @param mval the value from the database.
	* @return the uv index.
	*/
	public static double uvIndex(double mval)
	{
		double uv;
		uv = mval/10;
		return uv;
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
		
		System.out.println("input");
		
		Scanner reader = new Scanner(System.in);
		input = reader.nextInt();
		
		output = airPressure(input);
		
		System.out.println(output);
		reader.close();
	}
}