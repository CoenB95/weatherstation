package weatherstation;

import java.util.ArrayList;

public class Menu {
	private ArrayList<String> menu;
	
	public Menu(String[] values)
	{
		menu = new ArrayList<>();
		for(String value: values){
			menu.add(value);
		}
	}
	
	/**
	 * adds an option to the menu
	 * @param option
	 */
	public void addOption(String option)
	{
		menu.add(option);
	}
}