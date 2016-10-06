package weatherstation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class Measurements {

	private WeatherStation station;
	private List<Measurement> measurements;
	
	public Measurements() {
		station = new WeatherStation();
		measurements = new ArrayList<>();
	}
	
	public Measurements(LocalDate d) {
		this(d, d);
	}
	
	public Measurements(LocalDate d1, LocalDate d2) {
		station = new WeatherStation();
		measurements = new ArrayList<>();
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
		double max = measurements.get(0).getDouble(field);
		for (Measurement m:measurements) {
			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
					m.getDateStamp().getDayOfYear() == 0) {
				date = m.getDateStamp();
				result.add(max);
				max = m.getDouble(field);
			}
			if (m.getDouble(field) > max) 
				max = m.getDouble(field);
		}
		result.add(max);
		return result;
	}
}
