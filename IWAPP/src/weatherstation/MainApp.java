package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Scanner;

import weatherstation.IOHandler.ButtonHandler;
import weatherstation.IOHandler.MatrixHandler;
import weatherstation.menu.fallback.Menu;
import weatherstation.menu.fallback.MenuItem;
import weatherstation.sql.RawMeasurement;
import weatherstation.sql.WeatherStation;

public class MainApp {

	IOHandler iohandler;
	Measurements measurements;
	ButtonHandler menu_buttons;
	Menu menu;
	
	public static void main(String[] args) {
		new MainApp().mainLogic();
	}
	
	public void mainLogic() {

		setupConnectionWithDisplay();

		//MenuHandler hand = new MenuHandler();
		//hand.io.getMatrixHandler().clearMatrix();
		//hand.io.getMatrixHandler().appendText("Laden...");

		iohandler = new IOHandler();

		// Measurements: The Database Util.
		// Pass todays date as the period we want data from (as a test).
		measurements = new Measurements();

		MenuItem[] category_one_day_menu = {
				new MenuItem("Temperatuur").setAction(() -> {
					iohandler.getMatrixHandler().setText(
							"        Temp.\nMin               Max");
					iohandler.writeNumber(IOHandler.NUMBER_FIELD_1,
							measurements.getLatestMeasurement()
							.getOutsideTemperature(), false);
					iohandler.writeNumber(IOHandler.NUMBER_FIELD_2,
							measurements.getLowest(Measurement.TEMPERATURE_OUTSIDE)
							, false);
					iohandler.writeNumber(IOHandler.NUMBER_FIELD_3,
							measurements.getHighest(Measurement.TEMPERATURE_OUTSIDE)
							, false);
				}),
				new MenuItem("Zon").setAction(() -> { 
					iohandler.getMatrixHandler().setText("Opkomst:   " +
							measurements.getAllMeasurements().get(0)
							.getSunrise() + "\nOndergang: " +
							measurements.getAllMeasurements().get(0)
							.getSunset());
				}),
				new MenuItem("Windrichting").setAction(() -> {
					iohandler.getMatrixHandler().setText(
							measurements.getLatestMeasurement()
							.getWindDirection() + "*");
				})
		};

		/**2nd level menu: the category selection*/
		MenuItem[] category_menu = {				
				new MenuItem("Luchtdruk").addAll(newStatisticsMenu(
						Measurement.BAROMETER, "Hpa")),

				new MenuItem("Buitentemperatuur").addAll(
						new MenuItem("Hittegolf").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(
									measurements.hadHeatwave() ? "Ja" : "Nee");
						}),
						new MenuItem("Graaddagen").setAction(() -> {
							iohandler.getMatrixHandler().setText(String.format(
									"%.1f graaddagen", measurements.getDegreeDays()));
						}),
						new MenuItem("Langste stijging").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestDurationWithRising(Measurement.TEMPERATURE_OUTSIDE);
							iohandler.getMatrixHandler().appendText(p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
						new MenuItem("Langste zomerse periode").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestPeriodWithMoreThan(25, Measurement.TEMPERATURE_OUTSIDE);
							iohandler.getMatrixHandler().appendText(p.getStartDate() + "\ntot\n" + p.getEndDate());
						}))
				.addAll(newStatisticsMenu(Measurement.TEMPERATURE_OUTSIDE, 
						"*C")),
				new MenuItem("Binnentemperatuur").addAll(newStatisticsMenu(
						Measurement.TEMPERATURE_INSIDE, "*C")),

				new MenuItem("Vochtigheid Buiten").addAll(newStatisticsMenu(
						Measurement.HUMIDITY_OUTSIDE, "%")),

				new MenuItem("vochtigheid binnen").addAll(newStatisticsMenu(
						Measurement.HUMIDITY_INSIDE, "%")),

				new MenuItem("windsnelheid").addAll(newStatisticsMenu(
						Measurement.WINDSPEED, "km/h")),

				new MenuItem("Neerslag").addAll(
						new MenuItem("Langste droogte", 0, 10, (i) -> {
							//).setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestDurationWithLessThan(
									i, Measurement.RAINRATE);
							iohandler.getMatrixHandler().appendText(
									p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
						new MenuItem("Langste natte periode", 0, 10, (i) -> {
							//.setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestDurationWithMoreThan(
									i, Measurement.RAINRATE);
							iohandler.getMatrixHandler().appendText(
									p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
						new MenuItem("Meeste aaneengesloten").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							double d = measurements.getHighestContinuousRainrate();
							iohandler.getMatrixHandler().appendText(
									d + " mm");
						})),

				new MenuItem("UV-index").addAll(newStatisticsMenu(
						Measurement.UVLEVEL, "")),

				new MenuItem("Batterij Spanning").addAll(newStatisticsMenu(
						Measurement.BATTERYVOLTAGE, "V"))
		};

		/**1st menu: the period selection.*/
		menu = new Menu(iohandler, "Periode:",
				new MenuItem("Alle metingen").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusYears(8), 
							LocalDate.now());
				}).addAll(category_menu),
				new MenuItem("Afgelopen jaar").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusYears(1), 
							LocalDate.now());
				}).addAll(category_menu),
				new MenuItem("Afgelopen maand").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusMonths(1), 
							LocalDate.now());
				}).addAll(category_menu),
				new MenuItem("Vandaag").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now(), 
							LocalDate.now());
				}).addAll(category_menu),
				new MenuItem("Nu").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now(), 
							LocalDate.now());
				}).addAll(category_one_day_menu),
				new MenuItem("Dag keuze").setAction((button) -> {
					return customDateAction(button, false);
				}).addAll(category_one_day_menu),
				new MenuItem("Periode keuze").setAction((button) -> {
					return customDateAction(button, true);
				}).addAll(category_menu)
				);
		menu.draw();
				
		menu_buttons = new ButtonHandler() {
			@Override
			public void onButtonClicked(int button) {
				menu.handleButton(button);
			}
		};
		iohandler.addOnButtonListener(menu_buttons);
	}
	
	private int custom_index = -1;
	private TemporalAmount custom_temp_amount;
	private LocalDate custom_start;
	private LocalDate custom_end;
	private ButtonHandler custom_buttons;
	
	private boolean customDateAction(int button, boolean period) {
		if (custom_index < 0) {
			custom_start = LocalDate.now();
			custom_end = LocalDate.now();
			custom_index = 0;
			custom_temp_amount = java.time.Period.ofYears(1);
		}
		MatrixHandler m = iohandler.getMatrixHandler();
		switch (button) {
		case ButtonHandler.BUTTON_LEFT: 
			if (custom_index < 3)
				custom_start = custom_start.minus(custom_temp_amount);
			else custom_end = custom_end.minus(custom_temp_amount); break;
		case ButtonHandler.BUTTON_RIGHT:
			if (custom_index < 3)
				custom_start = custom_start.plus(custom_temp_amount);
			else custom_end = custom_end.plus(custom_temp_amount); break;
		case ButtonHandler.BUTTON_SELECT:
			custom_index++;
			if (custom_index % 3 == 0) custom_temp_amount = java.time.Period.ofYears(1);
			if (custom_index % 3 == 1) custom_temp_amount = java.time.Period.ofMonths(1);
			if (custom_index % 3 == 2) custom_temp_amount = java.time.Period.ofDays(1);
			break;
		}
		if (custom_index < 3) custom_end = LocalDate.from(custom_start);
		m.setText(custom_index < 3 ? "Start: " + custom_start :
			"Eind:  " + custom_end);
		if (custom_index % 3 == 0) m.drawUnderline(0, 7, 10);
		if (custom_index % 3 == 1) m.drawUnderline(0, 12, 13);
		if (custom_index % 3 == 2) m.drawUnderline(0, 15, 16);
		
		if (custom_index >= (period ? 6 : 3)) {
			custom_index = -1;
			iohandler.getMatrixHandler().setText("laden...");
			System.out.println("Van " + custom_start + " tot " + custom_end);
			measurements.fetchPeriod(custom_start, custom_end);
			return true;
		} else return false;
	}

	/**3rd level menu creator. Makes the statistics menu (double-only).*/
	public MenuItem[] newStatisticsMenu(int field, String text) {
		return new MenuItem[] {
			new MenuItem("Hoogste").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(
						measurements.getHighest(field) + " " + text);
			}),
			new MenuItem("Laagste").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(
						measurements.getLowest(field) + " " + text);
			}),
			new MenuItem("Modus").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(printList(
						measurements.getModus(field)) + " " + text);
			}),
			new MenuItem("Mediaan").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(printList(
						measurements.getMedian(field)) + " " + text);
			}),
			new MenuItem("Gemiddelde").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(
						measurements.getAverage(field) + " " + text);
			}),
			new MenuItem("Standaard Deviatie").setAction(() -> {
				iohandler.getMatrixHandler().clearMatrix();
				iohandler.getMatrixHandler().appendText(
						measurements.getStandardDeviation(field) + " " + text);
			})
		};
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

	public static String printList(List<Double> list) {
		String s = "";
		for (Double d : list) {
			s += String.format("%.1f ", d);
		}
		return s;
	}
}