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
		mbar = mval / 1000 * 3386.38866667;
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
		celsius = (mval/10 - 32)/ 1.8;
		return celsius;
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

	/**
	* Regenmeter
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De hoeveelheid regen in mm
	*/
	public double regenmeter(short mval)
	{
		double rain;
		rain = mval *0.2;
		return rain;
	}

	
	/**
	* uvIndex
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/
	public static double uvIndex(double mval)
	{
		double uv;
		uv = mval/10;
		return uv;
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
		
		System.out.println("input");
		
		Scanner reader = new Scanner(System.in);
		input = reader.nextInt();
		
		//output = luchtdruk(input);
		
		//System.out.println(output);
		reader.close();
	}
}