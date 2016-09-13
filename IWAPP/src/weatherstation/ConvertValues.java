package weatherstation;

import java.util.Scanner;

/**
 * Write a description of class ConvertValues here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class ConvertValues
{
	/**
	* Luchtdruk
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De luchtdruk in mbar
	*/
	public static double luchtdruk(double mval)
	{
		double mbar;
		mbar = mval / 100;
		return mbar;
	}
	
	/**
	* Temperatuur
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De temperatuur in graden Celcius
	*/
	public static double temperatuur(double mval)
	{
		double celsius;
		celsius = (mval - 32) * 5/9;
		return celsius;
	}
	

	/**
	 * Relatieve Luchtvochtigheid
	 *
	 * @param mval	Meetwaarde van het vp2pro weerstation
	 * @return De relatieve luchtvochtigheid in procenten
	 */
	public static double luchtVochtigheid(double mval)
	{
		return 1;
	}

	/**
	 * Windsnelheid
	 *
	 * @param mval	Meetwaarde van het vp2pro weerstation
	 * @return De windsnelheid in km/h
	*/
	public static double windSnelheid(double mval)
	{
		double kph;
		kph = mval * 1.609344;
		return kph;
	}
	
	/**
	* Windrichting
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/	
	public static double windRichting(double mval)
	{
		return 1;
	}
	
	/**
	* Regenmeter
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De hoeveelheid regen in mm
	*/	
	public static double regenmeter(double mval)
	{
		return 1;
	}
	
	/**
	* uvIndex
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/
	public static double uvIndex(double mval)
	{
		return 1;
	}
	
	/**
	* batterySpanning
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De battery spanning in Volt
	*/
	public static double batterySpanning(double mval)
	{
		return 1;
	}
	
	public static void main(String[] args)
	{
		double input;
		double output;
		
		Scanner reader = new Scanner(System.in);
		input = reader.nextInt();
		
		output = luchtdruk(input);
		
		System.out.println(output);
		reader.close();
	}
}