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
				new Measurements(LocalDate.now().minusDays(365), LocalDate.now());
		
		// The previous instruction blocks the main thread until the data has
		// been collected. Now display some useful information ;)
		List<Period> p = measurements.getLongestPeriodWithoutRainfall();
		if (p.isEmpty()) {
			hand.io.getMatrixHandler().clearMatrix();
			hand.io.getMatrixHandler().appendText("Geen data.");
		} else {
			hand.io.getMatrixHandler().clearMatrix();
			hand.io.getMatrixHandler().appendText("Droogte:\n" + 
					String.format("Van: %1$td-%1$tm %1$tH:%1$tM:%1$tS\n"
							+ "Tot: %2$td-%2$tm %2$tH:%2$tM:%2$tS",
							p.get(0).getStartDate(),
							p.get(0).getEndDate()));
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