package weatherstation;

import java.time.LocalDateTime;
import weatherstation.sql.RawMeasurement;

public class Measurement {

	private RawMeasurement rawMeasurement;
	
	public Measurement(RawMeasurement rawMeasurement) {
		this.rawMeasurement = rawMeasurement;
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
