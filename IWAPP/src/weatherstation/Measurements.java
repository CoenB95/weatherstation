package weatherstation;

import java.text.DecimalFormat;
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
		fetchPeriod(d1, d2);
	}

	public void fetchPeriod(LocalDate d1, LocalDate d2) {
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
			if (m.getDateStamp().getDayOfYear() != date.getDayOfYear()) {
				date = m.getDateStamp();
				measurementsPerDay.add(new ArrayList<>(measurements_of_the_day));
				measurements_of_the_day.clear();
			}
			measurements_of_the_day.add(m);
		}
		measurementsPerDay.add(new ArrayList<>(measurements_of_the_day));
	}

	public double getDegreeDays() {
		double days = 0;
		for (double d:getAverage(Measurement.TEMPERATURE_OUTSIDE)) {
			if (d < 18) days += 18 - d;
		}
		return days;
	}

	public boolean hadHeatwave() {
		int count_25 = 0;
		int count_30 = 0;
		for (double d : getHighest(Measurement.TEMPERATURE_OUTSIDE)) {
			if (d >= 25) count_25++;
			else count_25 = 0;
			if (d >= 30) count_30++;

			if (count_25 >= 5 && count_30 >= 2) return true;
		}
		return false;
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
		//		double total = 0;
		List<Double> result = new ArrayList<>();
		if (measurements.isEmpty()) return result;
		LocalDateTime date = measurements.get(0).getDateStamp();
		//		double avg = 0;
		for (List<Measurement> ms:measurementsPerDay) {
			//		for (Measurement m:measurements) {
			//			if (m.getDateStamp().getDayOfYear() > date.getDayOfYear() ||
			//					m.getDateStamp().getDayOfYear() == 0) {
			//				date = m.getDateStamp();
			//				avg = avg / total;
			//				result.add(avg);
			//				avg = 0;
			//				total = 0;
			//			}
			double avg = 0;
			double total = 0;
			for (Measurement m:ms) {
				avg += m.getDouble(field);
				total++;
			}
			result.add(avg/total);
		}
		//		avg = avg / total;
		//		result.add(avg);
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
	 * Find the longest day-period wherein a certain field's value stays above
	 * the specified minimum.
	 * @return a Period containing the start and end-time.
	 */
	public Period getLongestPeriodWithMoreThan(double min, int field) {
		Period period = new Period(LocalDateTime.now(), LocalDateTime.now());
		if (measurementsPerDay.isEmpty()) return null;
		LocalDateTime start = null;
		List<Double> doubles = getHighest(field);
		for (int i = 0;i < doubles.size();i++) {
			if (start == null) {
				if (doubles.get(i) >= min) 
					start = measurementsPerDay.get(i).get(0).getDateStamp();
			} else {
				if (doubles.get(i) < min) {

					if (ChronoUnit.MINUTES.between(start, 
							measurementsPerDay.get(i).get(0).getDateStamp()) > 
					ChronoUnit.MINUTES.between(period.getStartDate(), 
							period.getEndDate())) {

						period = new Period(start, 
								measurementsPerDay.get(i).get(0).getDateStamp());
					}
					start = null;
				}
			}
		}
		if (start != null) {
			Measurement m = measurementsPerDay.get(measurementsPerDay.size()-1).get(0);
			if (ChronoUnit.MINUTES.between(start, m.getDateStamp()) > 
			ChronoUnit.MINUTES.between(period.getStartDate(), 
					period.getEndDate())) {

				period = new Period(start, m.getDateStamp());
			}
		}

		return period;
	}

	/**
	 * Find the longest minute-duration wherein a certain field's value rises.
	 * @return a Period containing the start and end-time.
	 */
	public Period getLongestDurationWithRising(int field) {
		Period period = new Period(LocalDateTime.now(), LocalDateTime.now());
		if (measurements.isEmpty()) return null;
		LocalDateTime start = null;
		double last = 0;
		for (Measurement m:measurements) {
			if (start == null) {
				last = m.getDouble(field);
				start = m.getDateStamp();
			} else {
				if (m.getDouble(field) < last) {
					
					if (ChronoUnit.MINUTES.between(start, m.getDateStamp()) > 
					ChronoUnit.MINUTES.between(period.getStartDate(), 
							period.getEndDate())) {
						
						period = new Period(start, m.getDateStamp());
					}
					start = null;
				} else {
					last = m.getDouble(field);
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
	
	/**
	 * Find the longest minute-duration wherein a certain field's value stays above
	 * the specified minimum.
	 * @return a Period containing the start and end-time.
	 */
	public Period getLongestDurationWithMoreThan(double min, int field) {
		Period period = new Period(LocalDateTime.now(), LocalDateTime.now());
		if (measurements.isEmpty()) return null;
		LocalDateTime start = null;
		for (Measurement m:measurements) {
			if (start == null) {
				if (m.getDouble(field) >= min) start = m.getDateStamp();
			} else {
				if (m.getDouble(field) < min) {

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

	/**
	 * Find the longest minute-duration wherein a certain field's value stays below
	 * the specified maximum.
	 * @return a Period containing the start and end-time.
	 */
	public Period getLongestDurationWithLessThan(double max, int field) {
		Period period = new Period(LocalDateTime.now(), LocalDateTime.now());
		if (measurements.isEmpty()) return null;
		LocalDateTime start = null;
		for (Measurement m:measurements) {
			if (start == null) {
				if (m.getDouble(field) <= max) start = m.getDateStamp();
			} else {
				if (m.getDouble(field) > max) {

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
	
	public List<Double> getModus(int field) {
		List<Double> result = new ArrayList<>();
		int maxCount = 0;
		double maxValue = 0;
		ArrayList<Double> arraylist = new ArrayList<>();
		for (List<Measurement> ms:measurementsPerDay) {
			if (ms.isEmpty()) break;
			double value = ms.get(0).getDouble(field);
			for (Measurement m:ms) {
				arraylist.add(value* 100 / 100.00);
				}
			for(int i = 0; i < arraylist.size(); ++i){
				int count = 0;
				for(int j = 0; j < arraylist.size(); ++j){
					if (arraylist.get(j) == arraylist.get(i)){
						++count;
					}
					if (count > maxCount){
						maxCount = count;
						maxValue = arraylist.get(i);
				
				}
				}
			}
			result.add(maxValue);
			maxValue = 0;
			maxCount = 0;
			arraylist.clear();
		}
		
		return result;
	}

	private Double round(double value, int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getHighestContinuousRainrate() {
		double highestRainfall = 0;
		double tempHighestRainfall = 0;
		if (measurements.isEmpty()) return 0;
		for (Measurement m:measurements) {
			double rainrate = m.getDouble(7);
			double rainfall = rainrate / 60;
			if (tempHighestRainfall > highestRainfall) {
				highestRainfall = tempHighestRainfall;
			}
			if (rainrate <= 0) {
				tempHighestRainfall = 0;
			} else {
				tempHighestRainfall = tempHighestRainfall + rainfall;
			}
		}

		return highestRainfall;
	}
}
