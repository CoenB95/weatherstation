package weatherstation;

import java.io.File;
import java.io.IOException;

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
		
		IOHandler handler = null;
		try {
			handler = new IOHandler();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//handler.getMatrixHandler().clearPixel(0, 0);
		//handler.getMatrixHandler().MatrixInvert((short)0, (short)0, (short)0b0, (short)0b0);
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