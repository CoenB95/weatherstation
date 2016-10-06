package weatherstation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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

	
	/**
	 * Returns the max bv temperature of each day in the period.
	 * @param field
	 * @return
	 */
	public List<Double> getHighest(int field) {
		List<Double> result = new ArrayList<>();
		if (measurements.isEmpty()) return result;
		LocalDateTime date = measurements.get(0).getDateStamp();
		double max = 0;measurements.get(0).get(field);
		for (Measurement m:measurements) {
			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
					m.getDateStamp().getDayOfYear() == 0) {
				result.add(max);
			}
			if (m.get(field) > max) 
				max = m.get(field);
		}
		return result;
	}
}
