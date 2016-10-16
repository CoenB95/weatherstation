package weatherstation;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import weatherstation.IOHandler.ButtonHandler;
import weatherstation.menu.fallback.IntegerMenuItem;
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
				
				new MenuItem("Luchtdruk").addAll(
						new MenuItem("Hoogste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getHighest(Measurement.BAROMETER)) + " Hpa");
						}),
						new MenuItem("Laagste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getLowest(Measurement.BAROMETER)) + " Hpa");
						}),
						new MenuItem("Modus").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getModus(Measurement.BAROMETER)) + " Hpa");
						}),
						new MenuItem("Mediaan").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getMedian(Measurement.BAROMETER)) + " Hpa");
						}),
						new MenuItem("Gemiddelde").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getAverage(Measurement.BAROMETER)) + " Hpa");
					})),
				
					new MenuItem("Buitentemperatuur").addAll(
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
						new MenuItem("Langste stijging").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestDurationWithRising(Measurement.TEMPERATURE_OUTSIDE);
							iohandler.getMatrixHandler().appendText(p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
						new MenuItem("Langste zomerse periode").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							Period p = measurements.getLongestPeriodWithMoreThan(25, Measurement.TEMPERATURE_OUTSIDE);
							iohandler.getMatrixHandler().appendText(p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
							new MenuItem("Hoogste").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getHighest(Measurement.TEMPERATURE_OUTSIDE)) + " graden");
							}),
							new MenuItem("Laagste").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getLowest(Measurement.TEMPERATURE_OUTSIDE)) + " graden");
							}),
							new MenuItem("Modus").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getModus(Measurement.TEMPERATURE_OUTSIDE)) + " graden");
							}),
							new MenuItem("Mediaan").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getMedian(Measurement.TEMPERATURE_OUTSIDE)) + " graden");
							}),
							new MenuItem("Gemiddelde").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getAverage(Measurement.TEMPERATURE_OUTSIDE)) + " graden");
						})),
				new MenuItem("Binnentemperatuur").addAll(
							new MenuItem("Hoogste").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getHighest(Measurement.TEMPERATURE_INSIDE)) + " graden");
							}),
							new MenuItem("Laagste").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getLowest(Measurement.TEMPERATURE_INSIDE)) + " graden");
							}),
							new MenuItem("Modus").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getModus(Measurement.TEMPERATURE_INSIDE)) + " graden");
							}),
							new MenuItem("Mediaan").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getMedian(Measurement.TEMPERATURE_INSIDE)) + " graden");
							}),
							new MenuItem("Gemiddelde").setAction(() -> {
								iohandler.getMatrixHandler().clearMatrix();
								iohandler.getMatrixHandler().appendText(printList(
										measurements.getAverage(Measurement.TEMPERATURE_INSIDE)) + " graden");
						})),
				
				new MenuItem("vochtigheid buiten").addAll(
						new MenuItem("Hoogste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getHighest(Measurement.HUMIDITY_OUTSIDE)) + " %");
						}),
						new MenuItem("Laagste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getLowest(Measurement.HUMIDITY_OUTSIDE)) + " %");
						}),
						new MenuItem("Modus").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getModus(Measurement.HUMIDITY_OUTSIDE)) + " %");
						}),
						new MenuItem("Mediaan").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getMedian(Measurement.HUMIDITY_OUTSIDE)) + " %");
						}),
						new MenuItem("Gemiddelde").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getAverage(Measurement.HUMIDITY_OUTSIDE)) + " %");
					})),
				
				new MenuItem("vochtigheid binnen").addAll(
						new MenuItem("Hoogste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getHighest(Measurement.HUMIDITY_INSIDE)) + " %");
						}),
						new MenuItem("Laagste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getLowest(Measurement.HUMIDITY_INSIDE)) + " %");
						}),
						new MenuItem("Modus").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getModus(Measurement.HUMIDITY_INSIDE)) + " %");
						}),
						new MenuItem("Mediaan").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getMedian(Measurement.HUMIDITY_INSIDE)) + " %");
						}),
						new MenuItem("Gemiddelde").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getAverage(Measurement.HUMIDITY_INSIDE)) + " %");
					})),
				
				new MenuItem("windsnelheid").addAll(
						new MenuItem("Hoogste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getHighest(Measurement.WINDSPEED)) + " km/h");
						}),
						new MenuItem("Laagste").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getLowest(Measurement.WINDSPEED)) + " km/h");
						}),
						new MenuItem("Modus").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getModus(Measurement.WINDSPEED)) + " km/h");
						}),
						new MenuItem("Mediaan").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getMedian(Measurement.WINDSPEED)) + " km/h");
						}),
						new MenuItem("Gemiddelde").setAction(() -> {
							iohandler.getMatrixHandler().clearMatrix();
							iohandler.getMatrixHandler().appendText(printList(
									measurements.getAverage(Measurement.WINDSPEED)) + " km/h");
					})),
				
				new MenuItem("Neerslag").addAll(
					new IntegerMenuItem("Langste droogte", 0, 10, (i) -> {
					//).setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						Period p = measurements.getLongestDurationWithLessThan(
							i, Measurement.RAINRATE);
						iohandler.getMatrixHandler().appendText(
							p.getStartDate() + "\ntot\n" + p.getEndDate());
						}),
					new IntegerMenuItem("Langste natte periode", 0, 10, (i) -> {
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
						})
				),
			
			new MenuItem("UV-index").addAll(
					new MenuItem("Hoogste").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						iohandler.getMatrixHandler().appendText(printList(
								measurements.getHighest(Measurement.UVLEVEL)) + " ");
					}),
					new MenuItem("Laagste").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						iohandler.getMatrixHandler().appendText(printList(
								measurements.getLowest(Measurement.UVLEVEL)) + " ");
					}),
					new MenuItem("Modus").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						iohandler.getMatrixHandler().appendText(printList(
								measurements.getModus(Measurement.UVLEVEL)) + " ");
					}),
					new MenuItem("Mediaan").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						iohandler.getMatrixHandler().appendText(printList(
								measurements.getMedian(Measurement.UVLEVEL)) + " ");
					}),
					new MenuItem("Gemiddelde").setAction(() -> {
						iohandler.getMatrixHandler().clearMatrix();
						iohandler.getMatrixHandler().appendText(printList(
								measurements.getAverage(Measurement.UVLEVEL)) + " ");
				})),
		
		new MenuItem("BatterijSpannig").addAll(
				new MenuItem("Hoogste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getHighest(Measurement.BATTERYVOLTAGE)) + " V");
				}),
				new MenuItem("Laagste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getLowest(Measurement.BATTERYVOLTAGE)) + " V");
				}),
				new MenuItem("Modus").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getModus(Measurement.BATTERYVOLTAGE)) + " V");
				}),
				new MenuItem("Mediaan").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getMedian(Measurement.BATTERYVOLTAGE)) + " V");
				}),
				new MenuItem("Gemiddelde").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getAverage(Measurement.BATTERYVOLTAGE)) + " V");
			})),
		new MenuItem("SunRise").addAll(
				new MenuItem("Hoogste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getHighest(Measurement.SUNRISE)) + " ");
				}),
				new MenuItem("Laagste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getLowest(Measurement.SUNRISE)) + " ");
				}),
				new MenuItem("Modus").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getModus(Measurement.SUNRISE)) + " ");
				}),
				new MenuItem("Mediaan").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getMedian(Measurement.SUNRISE)) + " ");
				}),
				new MenuItem("Gemiddelde").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getAverage(Measurement.SUNRISE)) + " ");
			})),
		
		new MenuItem("SunSet").addAll(
				new MenuItem("Hoogste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getHighest(Measurement.SUNSET)) + " ");
				}),
				new MenuItem("Laagste").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getLowest(Measurement.SUNSET)) + " ");
				}),
				new MenuItem("Modus").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getModus(Measurement.SUNSET)) + " ");
				}),
				new MenuItem("Mediaan").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getMedian(Measurement.SUNSET)) + " ");
				}),
				new MenuItem("Gemiddelde").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText(printList(
							measurements.getAverage(Measurement.SUNSET)) + " ");
			}))};

		//1st menu
		Menu menu = new Menu(iohandler, "Periode:",
				new MenuItem("Alle metingen").setAction(() -> {
					iohandler.getMatrixHandler().clearMatrix();
					iohandler.getMatrixHandler().appendText("laden...");
					measurements.fetchPeriod(LocalDate.now().minusYears(9), 
							LocalDate.now());
				}).addAll(inner),
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
	
	public static String printList(List<Double> list) {
		String s = "";
		for (Double d : list) {
			s += String.format("%.1f ", d);
		}
		return s;
	}
}