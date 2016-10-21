package weatherstation.menu.fallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import weatherstation.IOHandler;
import weatherstation.IOHandler.ButtonHandler;
import weatherstation.IOHandler.MatrixHandler;

public class Menu {

	private IOHandler io;
	private MenuItem currentMenu;
	private List<MenuItem> menuStack;
	private int index = -1;
	private boolean inAction = false;
	private boolean inLeaf = false;
	
	public Menu(IOHandler h, String title, MenuItem... baseItems) {
		this(h, title, Arrays.asList(baseItems));
	}
	
	public Menu(IOHandler h, String title, Collection<? extends MenuItem> baseItems) {
		io = h;
		menuStack = new ArrayList<>();
		currentMenu = new MenuItem(title).addAll(baseItems);
		currentMenu.setBackAllowed(false);
		index = currentMenu.hasIndex(0) ? 0 : -1;
	}
	
	public void draw() {
		MatrixHandler m = io.getMatrixHandler();
		io.clearAll();
		// Row 1
		if (index < 0) m.addLine("");
		else if (index > 0)
			m.addLine(" " + currentMenu.getItem(index-1).getTitle());
		else m.addLine(currentMenu.getTitle());
		
		// Row 2
		if (index < 0) m.addLine(currentMenu.getTitle());
		else if (currentMenu.hasIndex(index)) {
			if (currentMenu.isBackIndex(index))
				m.addLine(" Terug");
			else m.addLine(" " + currentMenu.getItem(index).getTitle());
		}
		//else if (index == items.size() && backAllowed) m.appendText(" < Terug");
		
		// Row 3
		if (currentMenu.hasIndex(index + 1)) {
			if (currentMenu.isBackIndex(index + 1))
				m.addLine(" Terug");
			else m.addLine(" " + currentMenu.getItem(index + 1).getTitle());
		}
		//if (index + 1 < items.size()) m.appendText(" " + items.get(index + 1).title);
		//else if (index + 1 == items.size() && backAllowed) m.appendText(" < Terug");
		
		// Arrow on row 2
		if (index >= 0 && currentMenu.isBackIndex(index)) {
			m.drawLine(5, 12, 2, 15);
			m.drawLine(2, 15, 5, 18);
		} else if (index >= 0) {
			m.drawLine(2, 12, 5, 15);
			m.drawLine(2, 18, 5, 15);
		}
	}
	
	public void focusNext() {
		if (currentMenu.hasIndex(index + 1) && !inAction) {
			index++;
			draw();
		} else System.out.println("No further");
	}
	
	public void focusPrevious() {
		if (currentMenu.hasIndex(index - 1) && !inAction) {
			index--;
			draw();
		} else System.out.println("No further");
	}
	
	public void handleButton(int button) {
		if (inAction) {
			inAction = !currentMenu.getItem(index).runAction(button);
			inLeaf = !inAction;
			if (!inAction) System.out.println("The action has finished listening.");
		}
		if (!inAction) {
			switch (button) {
			case ButtonHandler.BUTTON_LEFT:
				if (!inLeaf) focusPrevious();
				break;
			case ButtonHandler.BUTTON_RIGHT:
				if (!inLeaf) focusNext();
				break;
			case ButtonHandler.BUTTON_SELECT:
				select();
			}
		}
	}
	
	public void notifyActionDone() {
		inAction = false;
	}
	
	public void select() {
		if (!currentMenu.isBackIndex(index) && currentMenu.getItem(index).hasAction()) {
			if (inLeaf) {
				System.out.println("Return from leaf. Turn on controls.");
				inLeaf = false;
				draw();
			} else {
				inAction = !currentMenu.getItem(index).runAction(
						ButtonHandler.BUTTON_NONE);
				// In case this is a leaf, it will probably want to show
				// something. Return to the menu on next select click.
				inLeaf = currentMenu.getItem(index).isEmpty();
				if (inAction) System.out.println("Interactive action detected."
						+ " Pass button input to action. Shut off controls.");
				if (inLeaf) System.out.println("Leaf detected. Shut off controls.");
			}
		}
		if (!currentMenu.getItem(index).isEmpty() && !inAction && !inLeaf) {
			if (currentMenu.isBackIndex(index)) {
				currentMenu = menuStack.get(menuStack.size() - 1);
				menuStack.remove(menuStack.size() - 1);
				index = 0;
			} else {
				menuStack.add(currentMenu);
				currentMenu = currentMenu.getItem(index);
				index = 0;
			}
			draw();
		}
	}
}
