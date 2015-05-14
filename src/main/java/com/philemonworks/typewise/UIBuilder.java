/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * */
package com.philemonworks.typewise;

import java.util.StringTokenizer;
import java.util.Vector;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.MenuList;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.internal.Widget;

/**
 * UIBuilder is a helper class for creating a CWT Widget tree such as a Screen.
 * An UIBuilder will maintain a position on a grid in terms of rows and columns.
 * All non-get/add methods of the UIBuilder return the builder itself allowing for cascading-style coding.
 * 
 * @author emicklei
 */
public class UIBuilder {
	/**
	 * Left column to which the position will return after a cr().
	 */
	public int leftEdge = 1;
	/**
	 * Current insertion location. (row, column).
	 */
	public Location location = new Location(1, 1);
	/**
	 * Stack of CWT Composite Widgets; every Widget will be added to the top Widget.
	 */
	private Vector stack = new Vector();
	/**
	 * Array of column values representing fixed tab locations.
	 */
	public int[] tabSet = new int[] {};
	/**
	 * Top row to which the position will return after ??.
	 */
	public int topEdge = 1;

	/**
	 * Add a widget. If the stack is empty then make it the first element. If the stack is not empty, then add the
	 * widget to the top widget of the stack.
	 * 
	 * @param widget Widget
	 * @return UIBuilder
	 */
	public UIBuilder add(Widget widget) {
		if (stack.isEmpty()) {
			stack.add(widget);
		} else {
			CompositeWidget top = (CompositeWidget) stack.lastElement();
			top.add(widget);
		}
		return this;
	}
	/**
	 * Add a new Button to the current widget holder. The new position is set to the same row and next to the right
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param labelString :
	 *        String
	 * @return Button : the new widget
	 */
	public Button addButton(String widgetName, String labelString) {
		return this.addButton(widgetName, labelString, labelString.length());
	}
	/**
	 * Add a new Button to the current widget holder. The new position is set to the same row and next to the right
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param labelString :
	 *        String
	 * @param useSpaces :
	 *        boolean, if true then the label is enclosed by one space character
	 * @param helpString :
	 *        String, displayed in the status bar when the button has focus
	 * @param styleName :
	 *        String, the style as defined in the StyleSheet of the Screen
	 * @param functionConstant :
	 *        byte, the accelerator constant such as F2. Available constants are defined in CWT.
	 * @param operationName :
	 *        String, name of the public method to invoke when the Button is triggered.
	 * @param model :
	 *        ApplicationModel, the implementor of the method (operationName) that is invoked by the EVENT_CLICKED event
	 * @return Button : the new widget
	 */
	public Button addButton(String widgetName, String labelString, boolean useSpaces, String helpString, String styleName, byte functionConstant,
			String operationName, ApplicationModel model) {
		String stringForLabel = labelString;
		if (useSpaces)
			stringForLabel = " " + stringForLabel + " ";
		Button button = this.addButton(widgetName, stringForLabel);
		button.setStyle(styleName);
		button.setHelp(helpString);
		button.onSendTo(CWT.EVENT_CLICKED, operationName, model);
		this.getScreen().onSendTo(functionConstant, operationName, model);
		return button;
	}
	/**
	 * Add a new Button to the current widget holder. The new position is set to the same row and next to the right
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param width :
	 *        int
	 * @return Button : the new widget
	 */
	public Button addButton(String widgetName, String labelString, int width) {
		Button widget = new Button(widgetName, location.row, location.column, 1, width);
		widget.setString(labelString);
		this.space(width);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new CheckBox to the current widget holder. The new position is set to same row next to the right column of
	 * the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param labelString :
	 *        String
	 * @param width :
	 *        int
	 * @return CheckBox : the new widget
	 */
	public CheckBox addCheckBox(String widgetName, String labelString, int width) {
		CheckBox widget = new CheckBox(widgetName, location.row, location.column, 1, width);
		widget.setString(labelString);
		this.space(width);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new Choice to the current widget holder. The new position is set to the same row next to the right column
	 * of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return Choice : the new widget
	 */
	public Choice addChoice(String widgetName, int rows, int columns) {
		Choice widget = new Choice(widgetName, location.row, location.column, 1, columns);
		widget.setMaximumVisibleRows(rows);
		this.add(widget);
		this.setPosition(location.row, location.column + columns);
		return widget;
	}
	/**
	 * Add a new Timer to the current widget holder. The new position is set to the row below the widget and at the left
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return Timer : the new widget
	 */
	public Timer addCounter(String widgetName, int rows, int columns) {
		Timer widget = new Timer(widgetName, location.row, location.column, rows, columns);
		this.add(widget);
		this.setPosition(location.row + rows, location.column);
		return widget;
	}
	/**
	 * Add a new Image to the current widget holder. The new position is set to the row below the widget and to the left
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return Image : the new widget
	 */
	public Image addImage(String widgetName, int rows, int columns) {
		Image widget = new Image(widgetName, location.row, location.column, rows, columns);
		this.setPosition(location.row + rows, location.column);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new Label to the current widget holder. The new position is set to same row and next to the right column of
	 * the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @return Label : the new widget
	 */
	public Label addLabel(String widgetName, String labelString) {
		return this.addLabel(widgetName, labelString, labelString.length());
	}
	/**
	 * Add a new Label to the current widget holder. The new position is set to same row and next to the right column of
	 * the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param width :
	 *        int
	 * @return Label : the new widget
	 */
	public Label addLabel(String widgetName, String labelString, int width) {
		Label widget = new Label(widgetName, location.row, location.column, 1, width);
		widget.setString(labelString);
		this.space(width);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new List to the current widget holder. The new position is set to the row below the widget at the left
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return List : the new widget
	 */
	public List addList(String widgetName, int rows, int columns) {
		List widget = new List(widgetName, location.row, location.column, rows, columns);
		this.add(widget);
		this.setPosition(location.row + rows, location.column);
		return widget;
	}
	/**
	 * Add a new MenuList to the current widget holder. The new position is set to the next row at the same column.
	 * 
	 * @param widgetName :
	 *        String
	 * @param width :
	 *        int
	 * @return MenuList : the new widget
	 */
	public MenuList addMenuList(String widgetName, int width) {
		MenuList widget = new MenuList(widgetName, location.row, location.column, 1, width);
		this.add(widget);
		this.setPosition(location.row + 1, location.column);
		return widget;
	}
	/**
	 * Add a new RadioButton to the current widget holder. The new position is set to same row next to the right column
	 * of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param labelString :
	 *        String
	 * @param width :
	 *        int
	 * @return RadioButton : the new widget
	 */
	public RadioButton addRadioButton(String widgetName, String labelString, int width) {
		RadioButton widget = new RadioButton(widgetName, location.row, location.column, 1, width);
		widget.setString(labelString);
		this.space(width);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new Screen to the builder.
	 * 
	 * @param widgetName :
	 *        String name of the Screen
	 * @param newModel :
	 *        ApplicationModel that will receive messages
	 * @param rows :
	 *        int height of the screen
	 * @param columns :
	 *        int width of the screen
	 * @return Screen
	 */
	public Screen addScreen(String widgetName, ApplicationModel newModel, int rows, int columns) {
		Screen widget = new Screen(widgetName, newModel, rows, columns);
		stack.add(widget);
		return widget;
	}
	/**
	 * Add a new TableList to the current widget holder. The new position is set to the row below the widget at the left
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return TableList : the new widget
	 */
	public TableList addTableList(String widgetName, int rows, int columns) {
		TableList widget = new TableList(widgetName, location.row, location.column, rows, columns);
		this.add(widget);
		this.setPosition(location.row + rows, location.column);
		return widget;
	}
	/**
	 * Add a new TextArea to the current widget holder. The new position is set to the row below the widget and to the
	 * left column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param rows :
	 *        int
	 * @param columns :
	 *        int
	 * @return TextArea : the new widget
	 */
	public TextArea addTextArea(String widgetName, int rows, int columns) {
		TextArea widget = new TextArea(widgetName, location.row, location.column, rows, columns);
		this.setPosition(location.row + rows, location.column);
		this.add(widget);
		return widget;
	}
	/**
	 * Add a new TextField to the current widget holder. The new position is set to same row and next to the right
	 * column of the widget.
	 * 
	 * @param widgetName :
	 *        String
	 * @param width :
	 *        int
	 * @return Image : the new widget
	 */
	public TextField addTextField(String widgetName, int width) {
		TextField widget = new TextField(widgetName, location.row, location.column, 1, width);
		this.space(width);
		this.add(widget);
		return widget;
	}
	/**
	 * Align one Widget with respect to another fixed Widget.
	 * 
	 * @param sideMoving :
	 *        constant of the side that is moving
	 * @param movingWidgetName :
	 *        name of the widget that is relocated
	 * @param sideFixed :
	 *        constant the side of the widget that is fixed
	 * @param fixedWidgetName :
	 *        name of the widget that will keep a fixed location
	 * @param offset :
	 *        offset in columns or rows with respect of the sideFixed
	 */
	public void align(byte sideMoving, String movingWidgetName, byte sideFixed, String fixedWidgetName, int offset) {
		Widget movingWidget = this.getScreen().widgetNamed(movingWidgetName);
		Widget fixedWidget = this.getScreen().widgetNamed(fixedWidgetName);
		new UILayoutHelper().align(sideMoving, movingWidget, sideFixed, fixedWidget, offset);
	}
	/**
	 * Begin a new Panel which is a CompositeWidget so it will be the new top of stack.
	 * The position of the builder is set to 1,1. Upon end of the panel the builder's position is set
	 * to the next row below the panel. Edge information will be lost.
	 * 
	 * @param widgetName :
	 *        name of the new Panel
	 * @param rows :
	 *        number of rows
	 * @param columns :
	 *        number of columns
	 * @return Panel
	 */
	public Panel beginPanel(String widgetName, int rows, int columns) {
		Panel widget = new Panel(widgetName, location.row, location.column, rows, columns);
		stack.add(widget);
		this.reset();
		return widget;
	}
	/**
	 * Compute the number of rows required for displaying the complete text in a TextArea that has a number of columns.
	 * The text is tokenized using any of TAB,SPACE,CR character. number
	 * 
	 * @param text :
	 *        String
	 * @param columns :
	 *        int
	 * @return integer rows
	 */
	public int computeHowManyRowsWritten(String text, int columns) {
		StringTokenizer tokenizer = new StringTokenizer(text, " \t\r");
		int lineCount = 1;
		int lineSize = 0;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			while (token.startsWith("\n")) {
				lineCount++;
				lineSize = 0;
				token = token.substring(1, token.length());
			}
			int extent = token.length();
			lineSize = lineSize + extent;
			if (lineSize > columns) {
				lineSize = extent + 1; // add separator
				lineCount++;
			} else {
				 lineSize++;  // add separator
			}
		}
		return lineCount;
	}
	/**
	 * Move the position to the next row and the current left edge.
	 * 
	 * @param howMany
	 *        rows to skip
	 * @return UIBuilder
	 */
	public UIBuilder cr(int howMany) {
		location = new Location(location.row + howMany, leftEdge);
		return this;
	}
	/**
	 * End the current Panel. It will be removed from the stack and added to new top of stack.
	 * The position of the builder is set to the next row below the panel. Edge information is lost.
	 * 
	 * @return UIBuilder
	 */
	public UIBuilder endPanel() {
		// Add the last element, a Panel, to the next on the stack (if any)
		Panel top = (Panel) stack.lastElement();
		stack.removeElement(top);
		this.add(top);
		this.setPosition(top.getBottomRow()+1,top.getLeftColumn());
		return this;
	}
	/**
	 * @return the current column value.
	 */
	public int getColumn() {
		return location.column;
	}
	/**
	 * @return the current row value.
	 */
	public int getRow() {
		return location.row;
	}
	/**
	 * Return the Screen Widget which is assumed to be the first element on the stack.
	 * 
	 * @return Screen
	 */
	public Screen getScreen() {
		return (Screen) stack.firstElement();
	}
	/**
	 * Convenience method 
	 * @return the application model for which the Screen is build
	 */
	public ApplicationModel getModel(){
		return this.getScreen().getModel();
	}
	/**
	 * @return UIBuilder
	 */
	public UIBuilder reset() {
		this.setPosition(topEdge, leftEdge);
		return this;
	}
	/**
	 * Move the position on the current row to a new column value.
	 * 
	 * @param newCol
	 *        is the new column value within the bounds of the top widget.
	 * @return UIBuilder
	 */
	public UIBuilder setColumn(int newCol) {
		this.setPosition(location.row, newCol);
		return this;
	}
	/**
	 * Set the current left edge in columns. A CR will return to this position on the next row.
	 * 
	 * @param newEdge
	 *        is the new column
	 * @return UIBuilder
	 */
	public UIBuilder setLeftEdge(int newEdge) {
		leftEdge = newEdge;
		location = new Location(location.row, leftEdge);
		return this;
	}
	/**
	 * Set the position to a new row-column pair The position is validated against the current container widget's
	 * bounds.
	 * 
	 * @param row :
	 *        int value of the row within the bounds of the top Screen
	 * @param column :
	 *        int value of the column within the bounds of the top Screen
	 * @return UIBuilder
	 */
	public UIBuilder setPosition(int row, int column) {
		Location newPos = new Location(row, column);
		if (newPos.row < 1)
			ErrorUtils.error(this, "illegal location:" + newPos);
		if (newPos.column < 1)
			ErrorUtils.error(this, "illegal location:" + newPos);
		location = newPos;
		return this;
	}
	/**
	 * Set both the top and left edge for the builder.
	 * @param row int
	 * @param column int 
	 * @return UIBuilder
	 */
	public UIBuilder setEdges(int row,int column){
		this.setTopEdge(row);
		this.setLeftEdge(column);
		return this;
	}
	
	/**
	 * Move the position on the current column to a new row value.
	 * 
	 * @param newRow
	 *        is the new row value within the bounds of the top widget.
	 * @return UIBuilder
	 */
	public UIBuilder setRow(int newRow) {
		this.setPosition(newRow, location.column);
		return this;
	}
	/**
	 * Set the top margin row
	 * 
	 * @param newEdge
	 *        is the new row
	 * @return UIBuilder
	 */
	public UIBuilder setTopEdge(int newEdge) {
		topEdge = newEdge;
		this.setPosition(newEdge, location.column);
		return this;
	}
	/**
	 * Move the position to a column by using the offset.
	 * 
	 * @param delta :
	 *        number of columns
	 * @return UIBuilder
	 */
	public UIBuilder space(int delta) {
		location = new Location(location.row, location.column + delta);
		return this;
	}
	/**
	 * Move the position to next tab location as defined by the tabSet If no location can be found, move the position by
	 * 1 column to the right.
	 * 
	 * @return UIBuilder
	 */
	public UIBuilder tab() {
		int t = 0;
		int next = location.column + 1;
		while (t < tabSet.length) {
			if (tabSet[t] >= next) {
				next = tabSet[t];
				t = tabSet.length + 1;
			} else
				t++;
		}
		location = new Location(location.row, next);
		return this;
	}
	/**
	 * Returns the Widget on the top of the stack.
	 * 
	 * @return Widget
	 */
	public Widget top() {
		return (Widget) stack.lastElement();
	}
}