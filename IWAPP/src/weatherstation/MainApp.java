package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import weatherstation.IOHandler.ButtonHandler;

//import java.util.Scanner;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	public static void main(String[] args)
	{
		File GUIBoard = new File("resources/GUIBoard/wsDisplay.jar");
		String absolutePath = GUIBoard.getAbsolutePath();
		System.out.println(absolutePath);
		
		absolutePath = absolutePath.replaceAll("\\u005c", "/");
		System.out.println(absolutePath);
		
		try {
			Runtime.getRuntime().exec("javaw -jar " + absolutePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		MenuHandler hand = new MenuHandler();

		String[] x = {"test1", "test2", "test3"};

		hand.displayMenu(x);
		
		// IOHandler: The GUI.
		IOHandler handler = new IOHandler();
		handler.setOnButtonListener((button) -> {
			switch (button) {
			case ButtonHandler.BUTTON_LEFT:
				System.out.println("Links!");
				break;

			default:
				break;
			}
		});
		handler.getMatrixHandler().clearMatrix();
		handler.getMatrixHandler().appendText("Een ogenblik geduld...");
		
		// Measurements: The Database Util.
		// Pass todays date as the period we want data from (as a test).
		Measurements measurements = new Measurements(LocalDate.now().minusDays(1), LocalDate.now());
		
		// The previous instruction blockes the main thread until the data has
		// been collected. Now display some useful information ;)
		handler.getMatrixHandler().clearMatrix();
		handler.getMatrixHandler().appendText(
				String.format("Tot nu toe:\nMin: %.1f|Max: %.1f", 
						measurements.getLowest(Measurement.TEMPERATURE_OUTSIDE).get(0),
						measurements.getHighest(Measurement.TEMPERATURE_OUTSIDE).get(0)));
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










//double input;
//double output;
//
//System.out.println("input");
//
//Scanner reader = new Scanner(System.in);
//input = reader.nextInt();
//IOHandler handler = new IOHandler();
//handler.writeNumber((short) 0x10, 246, true);
//handler.clearNumbers(0x10, 5);
//handler.getMatrixHandler().clearMatrix();
//handler.getMatrixHandler().appendText("Laden...");
//
//output = ValueConverter.convertBarometer(input);
//
//System.out.println(output);
//reader.close();
//Measurement m = testDatabase();
//handler.getMatrixHandler().clearMatrix();
//if (m != null) {
//	handler.getMatrixHandler().appendText(
//			String.format("Het is buiten: %.1f\nTijd: %tR",
//					m.getOutsideTemperature(),
//					m.getDateStamp()));
//} else {
//	handler.getMatrixHandler().appendText("Geen verbinding.");
//}