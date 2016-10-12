package weatherstation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class Measurements {

	private WeatherStation station;
	/**Contains all the measurements of the current period*/
	private List<Measurement> measurements;
	/**Contains all the measurements of the current period, split on a daily bases.*/
	private List<List<Measurement>> measurementsPerDay;

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
		splitMeasurementsPerDay();
	}

	/**
	 * Splits all measurements up into multiple lists, containing the
	 * measurements of a single date.
	 * @return lists containing measurements. The amount of lists depends
	 * on the total of days.
	 */
	private void splitMeasurementsPerDay() {
		measurementsPerDay = new ArrayList<>();
		if (measurements.isEmpty()) return;
		LocalDateTime date = measurements.get(0).getDateStamp();
		List<Measurement> measurements_of_the_day = new ArrayList<>();
		for (Measurement m:measurements) {
			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
					m.getDateStamp().getDayOfYear() == 0) {
				date = m.getDateStamp();
				measurementsPerDay.add(new ArrayList<>(measurements_of_the_day));
				measurements_of_the_day.clear();
			}
			measurements_of_the_day.add(m);
		}
		measurementsPerDay.add(new ArrayList<>(measurements_of_the_day));
	}

	/**
	 * Returns the max bv temperature of each day in the period.
	 * @param field
	 * @return
	 */
	public List<Double> getHighest(int field) {
		List<Double> result = new ArrayList<>();
		for (List<Measurement> ms:measurementsPerDay) {
			if (ms.isEmpty()) break;
			double max = ms.get(0).getDouble(field);
			for (Measurement m:ms) {
				if (m.getDouble(field) > max) max = m.getDouble(field);
			}
			result.add(max);
		}
		return result;
	}

	public List<Double> getLowest(int field) {
		List<Double> result = new ArrayList<>();
		if (measurements.isEmpty()) return result;
		LocalDateTime date = measurements.get(0).getDateStamp();
		double min = measurements.get(0).getDouble(field);
		for (Measurement m:measurements) {
			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
					m.getDateStamp().getDayOfYear() == 0) {
				date = m.getDateStamp();
				result.add(min);
				min = m.getDouble(field);
			}
			if (m.getDouble(field) < min) 
				min = m.getDouble(field);
		}
		result.add(min);
		return result;
	}

	/**
	 *  This method calculates the average of the values specified in the MainApp.
	 *  It requests a List from the MainApp with values depending on the field variable.
	 *  It then reads out the current date and walks through the specified dates, where it calculates the average
	 *  for each day. After that it calculates the total average using the averages given per day.
	 * @param field
	 * @return
	 */
	public List<Double> getAverage(int field) {
		double total = 0;
		List<Double> result = new ArrayList<>();
		if (measurements.isEmpty()) return result;
		LocalDateTime date = measurements.get(0).getDateStamp();
		double avg = 0;
		for (Measurement m:measurements) {
			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
					m.getDateStamp().getDayOfYear() == 0) {
				date = m.getDateStamp();
				avg = avg / total;
				result.add(avg);
				avg = 0;
				total = 0;
			}
			avg += m.getDouble(field);
			total++;
		}
		avg = avg / total;
		result.add(avg);
		return result;
	}
	
	public List<Double> getMedian(int field) {
		List<Double> result = new ArrayList<>();
		for (List<Measurement> ms:measurementsPerDay) {
			if (ms.isEmpty()) break;
			Double value;
			int middle = ms.size()/2;
			if (ms.size()%2 == 1) 
				value = ms.get(middle).getDouble(field);
			value = (ms.get(middle-1).getDouble(field) + ms.get(middle).getDouble(field)) / 2.0;
			if(value != null) 
				result.add(value);
		}
		return result;
	}

	/**
	 * Returns the longest period with a certain maximum rainrate.
	 * @return
	 */

	public Period getLongestPeriodWithLessRainfallThan(double maxRainfall) {
		Period period = new Period(LocalDateTime.now(), LocalDateTime.now());
		if (measurements.isEmpty()) return null;
		LocalDateTime start = null;
		for (Measurement m:measurements) {
			if (start == null) {
				if (m.getRainRate() <= maxRainfall) start = m.getDateStamp();
			} else {
				if (m.getRainRate() > maxRainfall) {
					
					if (ChronoUnit.MINUTES.between(start, m.getDateStamp()) > 
					ChronoUnit.MINUTES.between(period.getStartDate(), 
							period.getEndDate())) {
						
						period = new Period(start, m.getDateStamp());
					}
					start = null;
				}
			}
		}
		if (start != null) {
			Measurement m = measurements.get(measurements.size()-1);
			if (ChronoUnit.MINUTES.between(start, m.getDateStamp()) > 
			ChronoUnit.MINUTES.between(period.getStartDate(), 
					period.getEndDate())) {
				
				period = new Period(start, m.getDateStamp());
			}
		}
		
		return period;
	}

	public double getStandardDeviation(int field){
		double total = 0;
		double avg = 0;
		double vari = 0;
		double devtot = 0;
		int i = 0;

		for (Measurement m:measurements){
			total += m.getDouble(field);
			i++;
		}
		avg = total / i;

		for (Measurement n : measurements){
			devtot += Math.pow((n.getDouble(field) - avg), 2);
		}
		vari = devtot / i;

		return Math.sqrt(vari);
		//return Math.sqrt(vari);
	}
}
