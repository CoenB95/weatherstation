package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import weatherstation.IOHandler.ButtonHandler;
import weatherstation.menu.fallback.Menu;
import weatherstation.menu.fallback.MenuItem;
import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {
	
	public static void main(String[] args) {
		
		BooleanProperty in_menu = new SimpleBooleanProperty(true);
		
		setupConnectionWithDisplay();
		
		//MenuHandler hand = new MenuHandler();
		//hand.io.getMatrixHandler().clearMatrix();
		//hand.io.getMatrixHandler().appendText("Laden...");
		
		IOHandler iohandler = new IOHandler();
		
		Menu menu = new Menu(iohandler, "Menu:",
				new MenuItem("Test 1").addAll(
						new MenuItem("Test 1.1").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText("You found me!");
							in_menu.set(false);
						}),
						new MenuItem("Test 1.2")),
				new MenuItem("Test 2"),
				new MenuItem("Test 3"),
				new MenuItem("Test 4").addAll(
						new MenuItem("Test 4.1"),
						new MenuItem("Test 4.2")),
				new MenuItem("Test 5"),
				new MenuItem("Test 6"));
		menu.draw();
		
		iohandler.setOnButtonListener(new ButtonHandler() {
			@Override
			public void onButtonClicked(int button) {
				if (in_menu.get()) {
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
				} else {
					if (button == BUTTON_SELECT) {
						in_menu.set(true);
						menu.draw();
					}
				}
			}
		});
		

		// Measurements: The Database Util.
		// Pass todays date as the period we want data from (as a test).
		Measurements measurements = 
				new Measurements(LocalDate.now().minusDays(365), LocalDate.now());
		
		// The previous instruction blocks the main thread until the data has
		// been collected. Now display some useful information ;)
		Period p = measurements.getLongestPeriodWithLessRainfallThan(0);
		if (p == null) {
//			hand.io.getMatrixHandler().clearMatrix();
//			hand.io.getMatrixHandler().appendText("Geen data.");
		} else {
//			hand.io.getMatrixHandler().clearMatrix();
//			hand.io.getMatrixHandler().appendText("Droogte:\n" + 
//					String.format("Van: %1$td-%1$tm %1$tH:%1$tM:%1$tS\n"
//							+ "Tot: %2$td-%2$tm %2$tH:%2$tM:%2$tS",
//							p.getStartDate(),
//							p.getEndDate()));
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