package weatherstation;

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
	public static double luchtdruk(short mval)
	{
		double pa;
		pa = mval / 1000 * 3386.38866667;
		return pa;
	}
	
	/**
	* Temperatuur
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De temperatuur in graden Celcius
	*/
	public static double temperatuur(short mval)
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
	public static double luchtVochtigheid(short mval)
	{
		return 1;
	}

	/**
	 * Windsnelheid
	 *
	 * @param mval	Meetwaarde van het vp2pro weerstation
	 * @return De windsnelheid in km/h
	*/
	public static double windSnelheid(short mval)
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
	public static double windRichting(short mval)
	{
		return 1;
	}
	
	/**
	* Regenmeter
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De hoeveelheid regen in mm
	*/	
	public static double regenmeter(short mval)
	{
		return 1;
	}
	
	/**
	* uvIndex
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/
	public static double uvIndex(short mval)
	{
		return 1;
	}
	
	/**
	* batterySpanning
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De battery spanning in Volt
	*/
	public static double batterySpanning(short mval)
	{
		return 1;
	}
	
	public static void main(String[] args)
	{
		System.out.println("test");
	}
}