package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import com.mysql.fabric.xmlrpc.base.Array;

import weatherstation.IOHandler.ButtonHandler;
import weatherstation.menu.fallback.Menu;
import weatherstation.menu.fallback.MenuItem;
import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	public static void main(String[] args) {

		setupConnectionWithDisplay();

		//MenuHandler hand = new MenuHandler();
		//hand.io.getMatrixHandler().clearMatrix();
		//hand.io.getMatrixHandler().appendText("Laden...");

		IOHandler iohandler = new IOHandler();

		// Measurements: The Database Util.
		// Pass todays date as the period we want data from (as a test).
		Measurements measurements = new Measurements();

		//2nd menu
		MenuItem[] inner = 
			{
				new MenuItem("Temperatuur").addAll(
						new MenuItem("Hittegolf").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(
									measurements.hadHeatwave() ? "Ja" : "Nee");
						}),
						new MenuItem("Graaddagen").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(
									measurements.getDegreeDays() + " graaddagen");
						}),
						new MenuItem("graphtest").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							List<Double> d = measurements.getHighest(Measurement.TEMPERATURE_OUTSIDE);
							iohandler.drawGraph(d);
							//iohandler.getMatrixHandler().appendText(
/*									measurements.getDegreeDays() + " graaddagen");
 * */
						}),
						new MenuItem("Langste stijging").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestDurationWithRising(Measurement.TEMPERATURE_OUTSIDE);
							iohandler.getMatrixHandler().appendText(p.getStartDate() + "\ntot\n" + p.getEndDate());
						})),
				
						

				new MenuItem("Neerslag").addAll(
					new MenuItem("Langste droogte").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						Period p = measurements.getLongestDurationWithLessThan(
							0, Measurement.RAINRATE);
						iohandler.getMatrixHandler().appendText(
							p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
					new MenuItem("Langste natte periode").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						Period p = measurements.getLongestDurationWithMoreThan(
							0, Measurement.RAINRATE);
						iohandler.getMatrixHandler().appendText(
							p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
					new MenuItem("Meeste aaneengesloten").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						double d = measurements.getHighestContinuousRainrate();
						iohandler.getMatrixHandler().appendText(
							d + " mm");
						})
				)};

		//1st menu
		Menu menu = new Menu(iohandler, "Periode:",
				new MenuItem("Afgelopen jaar").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusYears(1), 
							LocalDate.now());
				}).addAll(inner),
				new MenuItem("Afgelopen maand").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusMonths(1), 
							LocalDate.now());
				}).addAll(inner),
				new MenuItem("Vandaag").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now(), 
							LocalDate.now());
				}).addAll(inner));
		menu.draw();

		iohandler.setOnButtonListener(new ButtonHandler() {
			@Override
			public void onButtonClicked(int button) {
				switch (button) {
				case BUTTON_LEFT:
					menu.focusPrevious();
					break;
				case BUTTON_RIGHT:
					menu.focusNext();
					break;
				case BUTTON_SELECT:
					menu.select();
				}
			}
		});
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