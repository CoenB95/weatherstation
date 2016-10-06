package weatherstation;

import java.time.LocalDate;
import java.util.List;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class Measurements {

	private WeatherStation station;
	private List<Measurement> measurements;
	
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
		for (RawMeasurement rm : station.getAllMeasurementsBetween(d1, d2)) {
			measurements.add(new Measurement(rm));
		}
	}
}
