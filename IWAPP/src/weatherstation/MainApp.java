package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	public static void main(String[] args) {
		
		setupConnectionWithDisplay();
		
		MenuHandler hand = new MenuHandler();
		hand.io.getMatrixHandler().clearMatrix();
		hand.io.getMatrixHandler().appendText("Laden...");
		

		// Measurements: The Database Util.
		// Pass todays date as the period we want data from (as a test).
		Measurements measurements = 
				new Measurements(LocalDate.now().minusYears(2), LocalDate.now()
						.minusYears(1));
		
		// The previous instruction blocks the main thread until the data has
		// been collected. Now display some useful information ;)
		Period p = measurements.getLongestPeriodWithMoreThan(25, Measurement.TEMPERATURE_OUTSIDE);
		if (p == null) {
			hand.io.getMatrixHandler().clearMatrix();
			hand.io.getMatrixHandler().appendText("Geen data.");
		} else {
			hand.io.getMatrixHandler().clearMatrix();
			hand.io.getMatrixHandler().appendText("Hittegolf: " + 
					String.format("%.1f\nVan: %2$td-%2$tm-%2$ty %2$tH:%2$tM\n"
							+ "Tot: %3$td-%3$tm-%3$ty %3$tH:%3$tM",
							measurements.getDegreeDays(),
							p.getStartDate(),
							p.getEndDate()));
		}
	}
	
	/**Starts the wsDisplay.jar and monitors it. When that application
	 * closes, we exit too.*/
	public static void setupConnectionWithDisplay() {
		File location = new File("resources/GUIBoard/wsDisplay.jar");
		try {
			Process p = Runtime.getRuntime().exec("javaw -jar " + 
					location.getAbsolutePath());
			new Thread(() -> {
				try {
					p.waitFor();
				} catch (InterruptedException e) {}
				Runtime.getRuntime().exit(0);
			}).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Measurement testDatabase() {
		WeatherStation station = new WeatherStation();
		RawMeasurement raw = station.getMostRecentMeasurement();
		if (raw.getStationId() != null) {
			Measurement real = new Measurement(raw);
			System.out.println(raw.toString());
			System.out.println(real.toString());
			return real;
		} else return null;
	}
}