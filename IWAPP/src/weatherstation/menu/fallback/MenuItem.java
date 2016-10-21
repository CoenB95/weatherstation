package weatherstation.menu.fallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import weatherstation.IOHandler.ButtonHandler;

public class MenuItem {
	
	private String title;
	private List<MenuItem> items;
	private MenuItem parent;
	private boolean backAllowed = true;
	private InteractiveAction action;
	
	public MenuItem() {
		this("");
	}
	
	public MenuItem(String title) {
		this.title = title;
		this.items = new ArrayList<>();
	}
	
	public MenuItem(String title, int min, int max, Selection s) {
		this(title);
		List<MenuItem> temp = new ArrayList<>();
		for (int i = min;i < max;i++) {
			final int ip = i;
			temp.add(new MenuItem(String.valueOf(i)).setAction(() -> {
				if (s != null) s.selected(ip);
			}));
		}
		addAll(temp);
	}
	
	public MenuItem addAll(MenuItem... items) {
		return addAll(Arrays.asList(items));
	}
	
	public MenuItem addAll(Collection<? extends MenuItem> items) {
		for (MenuItem item:items) item.parent = this;
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
	
	public boolean isEmpty() {
		return items.size() == 0;
	}
	
	public boolean runAction(int button) {
		return action.run(button);
	}
	
	public MenuItem setAction(InteractiveAction value) {
		action = value;
		return this;
	}
	
	public MenuItem setAction(Runnable value) {
		return setAction((button) -> {
			value.run();
			return true;
		});
	}
	
	protected void setBackAllowed(boolean value) {
		backAllowed = value;
	}
	
	public void setTitle(String value) {
		title = value;
	}
	
	@FunctionalInterface
	public interface InteractiveAction {
		/**
		 * @param button the pressed button's id.
		 * @return true if the action is done and the menu may return,
		 * false otherwise.
		 */
		public abstract boolean run(int button);
	}
	
	@FunctionalInterface
	public interface Selection {
		void selected(int number);
	}
}