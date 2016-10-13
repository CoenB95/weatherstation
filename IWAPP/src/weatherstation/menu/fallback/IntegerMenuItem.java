package weatherstation.menu.fallback;

import java.util.ArrayList;
import java.util.List;

public class IntegerMenuItem extends MenuItem {
	
	private int min;
	private int max;
	
	public IntegerMenuItem(String title, int min, int max, Selection s) {
		super(title);
		List<MenuItem> temp = new ArrayList<>();
		for (int i = min;i < max;i++) {
			final int ip = i;
			temp.add(new MenuItem(String.valueOf(i)).setAction(() -> {
				if (s != null) s.selected(ip);
			}));
		}
		addAll(temp);
	}
	
	@FunctionalInterface
	public interface Selection {
		void selected(int number);
	}
}
