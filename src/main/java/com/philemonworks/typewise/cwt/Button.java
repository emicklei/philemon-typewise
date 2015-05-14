/**
 * Licensed Material - Property of PhilemonWorks B.V.
 *
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted.
 * See http://www.philemonworks.com for information.
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * The Button widget is generally used by the user to request an action performed on the application. A Button widget
 * can trigger the EVENT_CLICKED event and performs all Message objects installed for this event. Accellerators can be
 * used to provide shortcuts to speed up user interaction (see below).
 */
public class Button extends Label implements BinaryAccessible, CWT {
	private static final long serialVersionUID = 4049642274000548153L;
	{
		// Set (different) default alignment for Button.
		alignment = CENTER;
	}
	/**
	 * A character from the label for activation using the [Alt] key. The button will show an indication of the
	 * accellerator character.
	 */
	private char accelerator = '\n'; // means no accelerator
	/**
	 * Help text to display in the status bar when this button has focus.
	 */
	private String help = null;
	/**
	 * If true then the button will respond to pressing [Enter] anywhere on the Terminal. When multiple buttons are set
	 * to default, only the first button found in the widgettree will be activated.
	 */
	private boolean isDefault = false;

	/**
	 * Constructor for this Widget class. Checks whether the name is unique and whether the bounds are within that of
	 * the container (if any).
	 * 
	 * @param newName :
	 *        unique name of the Widget
	 * @param topRow :
	 *        1-based integer to specify the row coordinate of the origin
	 * @param leftColumn :
	 *        1-based integer to specify the column coordinate of the origin
	 * @param howManyRows :
	 *        integer number of rows occupied by the widget
	 * @param howManyColumns :
	 *        integer number of columns occupied by the widget
	 */
	public Button(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	/**
	 * @return the accelerator character
	 */
	public char getAccelerator() {
		return accelerator;
	}
	/**
	 * @return the help text which will be displayed on the client when the Button has focus.
	 */
	public String getHelp() {
		return help;
	}
	/**
	 * @return whether this Button is the default one.
	 */
	public boolean isDefault() {
		return isDefault;
	}
	/**
	 * Set whether the Button is the default one
	 * 
	 * @param makeDefault :
	 *        true | false
	 */
	public void isDefault(boolean makeDefault) {
		isDefault = makeDefault;
	}
	/**
	 * Dispatch the request to the WidgetHandler.
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((Button) this);
	}
	/**
	 * Set the accellerator character.
	 * 
	 * @param c
	 */
	public void setAccelerator(char c) {
		accelerator = c;
	}
	/**
	 * Set the button as the default button. Pressing the [Enter] key will activate the default Button.
	 * 
	 * @param makeDefault
	 */
	public void setDefault(boolean makeDefault) {
		isDefault = makeDefault;
	}
	/**
	 * @param help
	 *        The help to set.
	 */
	public void setHelp(String help) {
		this.help = help;
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Button) this);
	}
}