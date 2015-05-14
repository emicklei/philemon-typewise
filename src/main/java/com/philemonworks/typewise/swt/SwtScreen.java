package com.philemonworks.typewise.swt;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.internal.ColorLookup;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtScreen is a special Screen for implementing an SWT client.
 * 
 * @author E.M.Micklei
 */
public class SwtScreen extends Screen implements KeyListener, SwtWidget {
	private static final long serialVersionUID = 3906652998632420656L;
	private SwtButton defaultButton;
	Shell swt;
	SwtApplicationView view;

	/**
	 * @param newName
	 * @param aModel
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtScreen(String newName, ApplicationModel aModel, int howManyRows, int howManyColumns) {
		super(newName, aModel, howManyRows, howManyColumns);
	}
	/**
	 * If a default button was specified then activate it
	 */
	void activateDefaultButton(KeyEvent arg0) {
		if (defaultButton == null)
			return;
		defaultButton.keyReleased(arg0);
	}
	private void addMenuBar() {
		MenuBar cwtBar = this.getMenuBarOrNull();
		if (cwtBar == null) {
			// Hide the existing SWT MenuBar
			view.toggleMenuBar(false);
			return;
		}
		// add menubar and menus
		Menu bar = view.getMenuBar();
		List labels = cwtBar.getLabels();
		List menus = cwtBar.getMenus();
		for (int i = 0; i < labels.size(); i++) {
			String label = (String) labels.get(i);
			com.philemonworks.typewise.cwt.Menu cwtMenu = (com.philemonworks.typewise.cwt.Menu) menus.get(i);
			MenuItem swtBarItem = new MenuItem(bar, SWT.CASCADE);
			swtBarItem.setText(label);
			Menu submenu = new Menu(view.getShell(), SWT.DROP_DOWN);
			List menuItems = cwtMenu.getItems();
			for (Iterator iter = menuItems.iterator(); iter.hasNext();) {
				com.philemonworks.typewise.cwt.MenuItem cwtMenuItem = (com.philemonworks.typewise.cwt.MenuItem) iter.next();
				MenuItem swtMenuItem = new MenuItem(submenu, SWT.CASCADE);
				swtMenuItem.setText(cwtMenuItem.getLabel());
			}
			swtBarItem.setMenu(submenu);
		}
	}
	/**
	 * Add the receiver to the shell of the
	 * 
	 * @param swtView
	 *        SwtApplicationView
	 */
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = swtView.getShell();
		this.initSize();
		swt.setBackground(SwtUtils.toSWT(ColorLookup.backgroundFor(this), swt.getDisplay()));
		swt.setForeground(SwtUtils.toSWT(ColorLookup.foregroundFor(this), swt.getDisplay()));
		SwtUtils.addAllToView(swtView, swt, this.getWidgets());
		this.addMenuBar();
		swt.addKeyListener(this);
	}
	/**
	 * @return Returns the defaultButton.
	 */
	public SwtButton getDefaultButton() {
		return defaultButton;
	}
	/**
	 * Pre: view not null
	 * 
	 * @param arg0
	 *        KeyEvent
	 */
	private void handleKeyControl(KeyEvent arg0) {
		if (arg0.character == '1' || arg0.character == '=' || arg0.character == '+') {
			view.incrementFontSize();
		}
		if (arg0.character == '2' || arg0.character == '-') {
			view.decrementFontSize();
		}
		if (arg0.character == '3') {
			view.selectFont();
		}
		Logger.getLogger(this.getClass()).debug(arg0.toString());
	}
	private void initSize() {
		int w = this.getColumns() * view.charwidth;
		int h = (this.getRows() + 1) * view.charheight; // +1 for status line
		swt.setSize(w + SwtApplicationView.screenMarginX, h + SwtApplicationView.screenMarginY);
	}
	public void keyPressed(KeyEvent arg0) {
		// detect rare conditions
		if (view == null)
			return;
		if ((arg0.stateMask & SWT.CTRL) != 0) {
			this.handleKeyControl(arg0);
			return;
		}
		// todo ALT+F1, SHIFT+F1 etc
		switch (arg0.keyCode) {
		case SWT.F1:
			view.handleEventFrom(EVENT_F1, this);
			break;
		case SWT.F2:
			view.handleEventFrom(EVENT_F2, this);
			break;
		case SWT.F3:
			view.handleEventFrom(EVENT_F3, this);
			break;
		case SWT.F4:
			view.handleEventFrom(EVENT_F4, this);
			break;
		case SWT.F5:
			view.handleEventFrom(EVENT_F5, this);
			break;
		case SWT.F6:
			view.handleEventFrom(EVENT_F6, this);
			break;
		case SWT.F7:
			view.handleEventFrom(EVENT_F7, this);
			break;
		case SWT.F8:
			view.handleEventFrom(EVENT_F8, this);
			break;
		case SWT.F9:
			view.handleEventFrom(EVENT_F9, this);
			break;
		case SWT.F10:
			view.handleEventFrom(EVENT_F10, this);
			break;
		case SWT.F11:
			view.handleEventFrom(EVENT_F11, this);
			break;
		case SWT.F12:
			view.handleEventFrom(EVENT_F12, this);
			break;
		}
	}
	public void keyReleased(KeyEvent arg0) {
	}
	public void removeFromView(SwtApplicationView swtView) {
		swt.removeKeyListener(this);
		SwtUtils.removeAllFromView(swtView, this.getWidgets());
		swt = null; // not dispose it because it may be reused
		view = null; // for the GC
	}
	/**
	 * @param defaultButton
	 *        The defaultButton to set.
	 */
	public void setDefaultButton(SwtButton defaultButton) {
		this.defaultButton = defaultButton;
	}
	public void setTitle(String newTitle) {
		super.setTitle(newTitle);
		if (swt != null)
			swt.setText(newTitle);
	}
	public void updateFont(Font newFont) {
		swt.setFont(newFont);
		this.initSize();
		SwtUtils.updateFontForAll(newFont, this.getWidgets());
	}
}
