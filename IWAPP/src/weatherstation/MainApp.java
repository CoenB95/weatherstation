package weatherstation;

import java.util.Scanner;

import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	public static void main(String[] args)
	{
		double input;
		double output;
		
		System.out.println("input");
		
		Scanner reader = new Scanner(System.in);
		input = reader.nextInt();
		
		output = ValueConverter.convertBarometer(input);
		
		System.out.println(output);
		reader.close();
		testDatabase();
	}
	
	public static void testDatabase() {
		WeatherStation station = new WeatherStation();
		RawMeasurement raw = station.getMostRecentMeasurement();
		Measurement real = new Measurement(raw);
		System.out.println(raw.toString());
		System.out.println(real.toString());
	}
}
