package weatherstation;

import java.time.LocalDateTime;
import weatherstation.sql.RawMeasurement;

public class Measurement {

	public static final int BAROMETER = 1;
	public static final int BATTERYVOLTAGE = 2;
	public static final int DEWPOINT = 3;
	public static final int HEATINDEX = 4;
	public static final int HUMIDITY_INSIDE = 5;
	public static final int HUMIDITY_OUTSIDE = 6;
	public static final int RAINRATE = 7;
	public static final int SOLARRADIATION = 8;
	public static final int SUNRISE = 9;
	public static final int SUNSET = 10;
	public static final int TEMPERATURE_INSIDE = 11;
	public static final int TEMPERATURE_OUTSIDE = 12;
	public static final int UVLEVEL = 13;
	public static final int WINDCHILL = 14;
	public static final int WINDDIRECTION = 15;
	public static final int WINDSPEED = 16;
	public static final int WINDSPEED_AVERAGE = 17;


	private RawMeasurement rawMeasurement;

	public Measurement(RawMeasurement rawMeasurement) {
		this.rawMeasurement = rawMeasurement;
	}

	public double getDouble(int field) {

		if(field == BAROMETER) {
			return getBarometer();
		}
		else if(field == BATTERYVOLTAGE) {
			return getBatteryVoltage();
		}
		else if(field == DEWPOINT) {
			return getDewPoint();
		}
		else if( field == HEATINDEX) {
			return getHeatIndex();
		}
		else if(field == HUMIDITY_INSIDE) {
			return getInsideHumidity();
		}
		else if(field == HUMIDITY_OUTSIDE) {
			return getOutsideHumidity();
		}
		else if(field == RAINRATE) {
			return getRainRate();
		}
		else if(field == SOLARRADIATION) {
			return getSolarRadiation();
		}
		else if(field == TEMPERATURE_INSIDE) {
			return getInsideTemperature();
		}
		else if(field == TEMPERATURE_OUTSIDE) {
			return getOutsideTemperature();
		}
		else if(field == UVLEVEL) {
			return getUVLevel();
		}
		else if(field == WINDCHILL) {
			return getWindChill();
		}
		else if(field == WINDDIRECTION) {
			return getWindDirection();
		}
		else if(field == WINDSPEED) {
			return getWindSpeed();
		}
		else if(field == WINDSPEED_AVERAGE) {
			return getAverageWindSpeed();
		}
		else {
			return -1000;
		}
	}
	public String getString(int field) {
		if(field == SUNRISE) {
			return getSunrise();
		}
		else if(field == SUNSET) {
			return getSunset();
		}
		else {
			return "Wrong value entered";
		}
	}


	public double getAverageWindSpeed() {
		return ValueConverter.windSpeed(rawMeasurement.getAvgWindSpeed());
	}

	public double getBarometer() {
		return ValueConverter.convertBarometer(rawMeasurement.getBarometer());
	}

	public double getBatteryVoltage() {
		return ValueConverter.batteryVoltage(rawMeasurement.getBattLevel());
	}

	public LocalDateTime getDateStamp() {
		return rawMeasurement.getDateStamp().toLocalDateTime();
	}

	public double getDewPoint() {
		return ValueConverter.dewpoint(rawMeasurement.getOutsideTemp(), 
				rawMeasurement.getOutsideHum());
	}

	public double getHeatIndex() {
		return ValueConverter.heatindex(rawMeasurement.getOutsideTemp(), 
				rawMeasurement.getOutsideHum());
	}

	public double getInsideHumidity() {
		return rawMeasurement.getInsideHum();
	}

	public double getInsideTemperature() {
		return ValueConverter.temperature(rawMeasurement.getInsideTemp());
	}

	public double getOutsideHumidity() {
		return rawMeasurement.getOutsideHum();
	}

	public double getOutsideTemperature() {
		return ValueConverter.temperature(rawMeasurement.getOutsideTemp());
	}

	public double getRainRate() {
		return ValueConverter.rainAmount(rawMeasurement.getRainRate());
	}

	public double getSolarRadiation() {
		return rawMeasurement.getSolarRad();
	}

	public String getSunrise() {
		return ValueConverter.sunrise(rawMeasurement.getSunrise());
	}

	public String getSunset() {
		return ValueConverter.sunset(rawMeasurement.getSunset());
	}

	public double getUVLevel() {
		return ValueConverter.uvIndex(rawMeasurement.getUVLevel());
	}

	public double getWindChill() {
		return ValueConverter.windchill(rawMeasurement.getOutsideTemp(),
				rawMeasurement.getWindSpeed());
	}

	public double getWindDirection() {
		return rawMeasurement.getWindDir();
	}

	public double getWindSpeed() {
		return ValueConverter.windSpeed(rawMeasurement.getWindSpeed());
	}

	@Override
	public String toString() {
		String s = "Measurement:"
				+ "\ndateStamp = \t" + getDateStamp()
				+ "\nbarometer = \t" + getBarometer()
				+ "\ninsideTemp = \t" + getInsideTemperature()
				+ "\ninsideHum = \t" + getInsideHumidity()
				+ "\noutsideTemp = \t" + getOutsideTemperature()
				+ "\nwindSpeed = \t" + getWindSpeed()
				+ "\navgWindSpeed = \t" + getAverageWindSpeed()
				+ "\nwindDir = \t" + getWindDirection()
				+ "\noutsideHum = \t" + getOutsideHumidity()
				+ "\nrainRate = \t" + getRainRate()
				+ "\nUVLevel = \t" + getUVLevel()
				+ "\nsolarRad = \t" + getSolarRadiation()
				+ "\nbattLevel = \t" + getBatteryVoltage()
				+ "\nsunrise = \t" + getSunrise()
				+ "\nsunset = \t" + getSunset();
		return s; 
	} 
}
