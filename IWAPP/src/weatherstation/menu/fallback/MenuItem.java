package weatherstation.menu.fallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MenuItem {
	
	private String title;
	private List<MenuItem> items;
	private MenuItem parent;
	private boolean backAllowed = true;
	private Runnable action;
	
	public MenuItem() {
		this("");
	}
	
	public MenuItem(String title) {
		this.title = title;
		this.items = new ArrayList<>();
	}
	
	public MenuItem addAll(MenuItem... items) {
		return addAll(Arrays.asList(items));
	}
	
	public MenuItem addAll(Collection<? extends MenuItem> items) {
		for (MenuItem item:items) item.parent = this;
		this.items.clear();
		this.items.addAll(items);
		return this;
	}
	
	public MenuItem getItem(int i) {
		if (backAllowed && i == items.size()) return parent;
		else if (i <= items.size()) return items.get(i);
		else return null;
	}
	
	public String getTitle() {
		return title;
	}
	
	public boolean hasAction() {
		return action != null;
	}
	
	public boolean hasIndex(int i) {
		return i >= 0 && (backAllowed ? i <= items.size() : i < items.size());
	}
	
	public boolean isBackIndex(int i) {
		return backAllowed && i == items.size();
	}
	
	public void runAction() {
		action.run();
	}
	
	public MenuItem setAction(Runnable value) {
		action = value;
		return this;
	}
	
	protected void setBackAllowed(boolean value) {
		backAllowed = value;
	}
	
	public void setTitle(String value) {
		title = value;
	}
}