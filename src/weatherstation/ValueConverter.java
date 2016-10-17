package weatherstation;

/**
 * Converts values received from the VP2Pro weather station
 * database to readable values.
 * 
 * @author CoenB95, Dokugan 
 * @version (a version number or a date)
 */
public class ValueConverter {
	
	public static final short SNOW = 		0b0001_0000;
	public static final short SUN = 		0b0000_1000;
	public static final short PARTIAL_SUN =	0b0000_0100;
	public static final short CLOUD = 		0b0000_0010;
	public static final short RAIN = 		0b0000_0001;
	
	/**
	* Converts the air pressure.
	*
	* @param mval the value from the database.
	* @return the amount of air-pressure in mBar.
	*/
	public static double convertBarometer(double mval)
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
	public static double temperature(short mval)
	{
		double celsius;
		celsius = (mval/10.0 - 32)/ 1.8;
		return celsius;
	}

	/**
	 * Converts the wind speed.
	 *
	 * @param mval the value from the database.
	 * @return the wind speed in km/h. 
	*/
	public static double windSpeed(short mval)
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
	public static double rainAmount(short mval)
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
	public static double uvIndex(short mval)
	{
		double uv;
		uv = mval/10.0;
		return uv;
	}
	
	/**
	* Converts the battery voltage.
	*
	* @param mval the value from the database.
	* @return the battery voltage.
	*/
	public static double batteryVoltage(short mval)
	{
		double batt = mval * 0.005859375;
		return batt;
	}
	
	public static String sunrise(short mval)
	{
		return sunConversion(mval);
	}
	
	public static String sunset(short mval)
	{
		return sunConversion(mval);
	}
	
	private static String sunConversion(short mval) {
		return String.format("%02d:%02d", (short) Math.floor(mval/100), mval%100);
	}
	
	/**
	 * Calculates the dew point.
	 * To work correctly, the humidity has to be at least 50%.
	 * 
	 * @param mval1 the outside temperature value from the database.
	 * @param mval2 the outside humidity value from the database.
	 * @return
	 */
	public static double dewpoint(short mval1, short mval2)
	{
		double temp = temperature(mval1);
		short hum = mval2;
		double dewp = temp - ((100-hum)/5);
		return dewp;
	}
	
	/**
	 * Calculates the wind chill.
	 * 
	 * @param mval1 the outside temperature value from the database.
	 * @param mval2 the windspeed value from the database.
	 * @return
	 */
	public static double windchill(short mval1, short mval2)
	{
		double windspd = windSpeed(mval2);
		double temp = temperature(mval1);
		
		if (temp < 10 && temp > -47)
		{
			double windChill = 13.12 + (.6215 * temp)+(0.3965 *(temp-28.676) * (Math.pow(windspd, 0.16)));
			return windChill;
		}
		return 0;
	}
	
	/**
	 * Calculates the heat-index.
	 * @param mval1 the outside temperature value from the database.
	 * @param mval2 the humidity.
	 * @return
	 */
	public static double heatindex(short mval1, short mval2)
	{
		double temp = mval1/10.0;
		double heatindex = -42.379 + (2.04901523 * temp) + (10.14333127*mval2) +
				(-0.22475541 * temp * mval2) + (-0.006873783 * temp * temp) +
				( -0.05481717 * mval2*mval2) + ( 0.00122874* temp*temp * mval2 ) + 
				(0.00085282 * temp * mval2*mval2) +
				( -0.00000199* temp*temp * mval2*mval2);
        heatindex = (heatindex - 32) / 1.8;
        return heatindex;
	}
}