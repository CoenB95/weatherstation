package weatherstation;

public class MenuHandler {
	IOHandler io;
	private Menu period, value, property; 
	private String[] periodArray = {"meest recente meting", "laatste maand", "laatste week", "alle metingen", "afsluiten"};
	private String[] valueArray = {"temperatuur buiten", "temperatuur binnen", "luchtdruk", "luchtvochtigheid", "vorige", "afsluiten"};
	private String[] propertyArray = {"hoogste waarde", "laagste waarde", "gemiddelde", "modus", "mediaan", "standaardafwijking", "vorige", "afsluiten"};
	private String[] chosen = new String[3];
	int chosenIndex = 0;
	
	public MenuHandler() 
	{
		io = new IOHandler();
		makeMenu(period, periodArray);
		makeMenu(value, valueArray);
		makeMenu(property, propertyArray);
	}
	
	public void addChosen(String value)
	{
		chosen[chosenIndex] = value;
		if(chosenIndex == 3) {
			chosenIndex = 0;
		} else chosenIndex++;
	}
	
	public void makeMenu(Menu name, String[] values) 
	{
		Menu menu = new Menu(values);
		name = menu;
	}
}