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
	public double luchtdruk(short mval)
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
	public double temperatuur(short mval)
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
	public double luchtVochtigheid(short mval)
	{
		return 1;
	}

	/**
	 * Windsnelheid
	 *
	 * @param mval	Meetwaarde van het vp2pro weerstation
	 * @return De windsnelheid in km/h
	*/
	public double windSnelheid(short mval)
	{
		return 1;
	}
	
	/**
	* Windrichting
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/	
	public double windRichting(short mval)
	{
		return 1;
	}
	
	/**
	* Regenmeter
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De hoeveelheid regen in mm
	*/	
	public double regenmeter(short mval)
	{
		return 1;
	}
	
	/**
	* uvIndex
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De windrichting in graden
	*/
	public double uvIndex(short mval)
	{
		return 1;
	}
	
	/**
	* batterySpanning
	*
	* @param mval	Meetwaarde van het vp2pro weerstation
	* @return De battery spanning in Volt
	*/
	public double batterySpanning(short mval)
	{
		return 1;
	}
	
	public void main(String[] args)
	{
		
	}
}