package weatherstation;

import java.time.LocalDate;

import weatherstation.sql.WeatherStation;

public class Measurements {

	private WeatherStation station;
	
	public Measurements() {
		station = new WeatherStation();
	}
	
	public Measurements(LocalDate d) {
		this(d, d);
	}
	
	public Measurements(LocalDate d1, LocalDate d2) {
		station = new WeatherStation();
		setPeriod(d1, d2);
	}
	
	public void setPeriod(LocalDate d1, LocalDate d2) {
		station.getAllMeasurementsBetween(d1, d2);
	}
}
