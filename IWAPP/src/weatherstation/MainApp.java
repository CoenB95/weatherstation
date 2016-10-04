package weatherstation;

//import java.util.Scanner;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	public static void main(String[] args)
	{
//		double input;
//		double output;
//		
//		System.out.println("input");
//		
//		Scanner reader = new Scanner(System.in);
//		input = reader.nextInt();
//		IOHandler handler = new IOHandler();
//		handler.writeNumber((short) 0x10, 246, true);
//		handler.clearNumbers(0x10, 5);
//		handler.getMatrixHandler().clearMatrix();
//		handler.getMatrixHandler().appendText("Laden...");
//		
//		output = ValueConverter.convertBarometer(input);
//		
//		System.out.println(output);
//		reader.close();
//		Measurement m = testDatabase();
//		handler.getMatrixHandler().clearMatrix();
//		if (m != null) {
//			handler.getMatrixHandler().appendText(
//					String.format("Het is buiten: %.1f\nTijd: %tR",
//							m.getOutsideTemperature(),
//							m.getDateStamp()));
//		} else {
//			handler.getMatrixHandler().appendText("Geen verbinding.");
//		}
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
