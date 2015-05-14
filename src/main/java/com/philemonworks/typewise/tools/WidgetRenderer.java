/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, renaming or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 7-feb-2004: created by emicklei
 * 12-04-2004: last edited by emicklei
 * 15-04-2004: mvhulsentop added widget implementation for 
 * 		TableList, CheckBox, RadioButton, GroupBox, MenuBar, CompositeWidget
 * 16-04-2004: 
 * 		mvhulsentop added widget implementation for	Menu
 * 		mvhulsentop added comment for many methods.
 * 18-04-2004:
 * 		emm modified Image display
 * 27-04-2004:
 * 		emm fixed bug in DropDownList handle
 * 24-09-2004
 *     emm fixed bug in Choice ; now it can show DisplayItem
 * 
 * */
package com.philemonworks.typewise.tools;

import java.awt.Graphics;
import java.util.ArrayList;
import com.philemonworks.typewise.Bounds;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Menu;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.cwt.MenuList;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Console;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.internal.ColorLookup;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.util.Color;

/**
 * @author emicklei Purpose: Provide a static graphical representation of the java widget structure provided.
 */
public class WidgetRenderer implements WidgetHandler {
	private Graphics g;
	public int charwidth = 0;
	public int charheight = 0;
	public int screenoffsety = 0;
	public int screenoffsetx = 0;
	public int charoffsetx = 0;
	public int charoffsety = 0;

	public void initialize(int charWidth, int charHeight, int screenOffsetY, int screenOffsetX, int charOffsetX,
			int charOffsetY) {
		charwidth = charWidth;
		charheight = charHeight;
		screenoffsety = screenOffsetY;
		screenoffsetx = screenOffsetX;
		charoffsetx = charOffsetX;
		charoffsety = charOffsetY;
	}
	/**
	 * setGraphics(Graphics) sets graphical window to draw on
	 * 
	 * @param aGraphics
	 *        the target canvas.
	 */
	public void setGraphics(Graphics aGraphics) {
		g = aGraphics;
	}
	/**
	 * setupForeground(Widget) sets the foreground color according to the widget's foreground color.
	 * 
	 * @param aWidget
	 */
	private void setupForeground(Widget aWidget) {
		setupForeground(ColorLookup.foregroundFor(aWidget));
	}
	/**
	 * setupForeground(Color) Sets foreground color
	 * 
	 * @param color
	 *        provides the color to be set.
	 */
	private void setupForeground(Color color) {
		g.setColor(Previewer.toAWT(color));
	}
	/**
	 * displayBackrgound(Widget) displays the background for the specified widget.
	 * 
	 * @param widget
	 *        the widget to display the background
	 */
	protected void displayBackground(Widget widget) {
		Color background = ColorLookup.backgroundFor(widget);
		Bounds bounds = widget.getBounds();
		this.displayBackground(background, bounds.getLeftColumn(), bounds.getTopRow(), bounds.getColumns(), bounds
				.getRows());
		this.displayBorder(widget);
	}
	/**
	 * displayBackground(Color, int, int, int, int) sets the rectangle provided in the specified widget backgroundColor.
	 * It basically just fills the rectangle with the color.
	 * 
	 * @param backgroundColor
	 *        the color to set
	 * @param left
	 *        upper left column location
	 * @param top
	 *        upper top row location
	 * @param width
	 *        width of the recangle to be filled
	 * @param height
	 *        height of the recatngle to be filled
	 */
	protected void displayBackground(Color backgroundColor, int left, int top, int columns, int rows) {
		if (backgroundColor == null)
			return;
		g.setColor(Previewer.toAWT(backgroundColor));
		int pixelLeft = (left - 1) * charwidth + screenoffsetx;
		int pixelTop = (top - 1) * charheight + screenoffsety;
		int pixelWidth = columns * charwidth;
		int pixelHeight = rows * charheight;
		g.fillRect(pixelLeft, pixelTop, pixelWidth, pixelHeight);
	}
	/**
	 * displayString(String, int, int, int) displays the part of the specified that fits into availableColumns
	 * 
	 * @param item
	 *        the String to show
	 * @param row
	 *        row position of the text
	 * @param column
	 *        column position of the text
	 * @param availableColumns
	 *        how much space (columns) can be used
	 */
	protected void displayString(String item, int row, int column, int availableColumns) {
		if (item == null || item.length() == 0)
			return;
		int top = (row - 1) * charheight + screenoffsety + charoffsety;
		int left = (column - 1) * charwidth + screenoffsetx + charoffsetx;
		String part = item.substring(0, Math.min(item.length(), availableColumns));
		g.drawString(part, left, top + charheight);
	}
	private String stringForItem(Object item) {
		if (item instanceof String) {
			return (String) item;
		} else if (item instanceof ListItem) {
			return ((ListItem) item).getItem();
		}
		return item.toString();
	}
	/**
	 * Displays the part of the specified that fits into availableColumns
	 * 
	 * @param item
	 *        the DisplayItem or String to show
	 * @param row
	 *        row position of the text
	 * @param column
	 *        column position of the text
	 * @param availableColumns
	 *        how much space (columns) can be used
	 */
	protected void displayString(Object item, int row, int column, int availableColumns) {
		this.displayString(this.stringForItem(item), row, column, availableColumns);
	}
	/**
	 * drawBorder(Widget) draws a border around a widget
	 * 
	 * @param widget
	 *        the widget that has to be bordered.
	 */
	protected void drawBorder(Widget widget) {
		drawBorder(widget, 0);
	}
	/**
	 * drawBorder(Widget, float) draws a border around or in a widget
	 * 
	 * @param widget
	 *        the widget that has to be 'bordered'
	 * @param toCenter
	 *        a float value that specifies how much the border 'moves' to the center of the widget. A value of 1
	 *        corresponds with the size of a single terminal character.
	 */
	protected void drawBorder(Widget widget, float toCenter) {
		int left, top, width, height;
		left = Math.round((widget.getLeftColumn() - 1 + toCenter) * charwidth + screenoffsetx);
		top = Math.round((widget.getTopRow() - 1 + toCenter) * charheight + screenoffsety);
		width = Math.round((widget.getBounds().getColumns() - (toCenter * 2)) * charwidth);
		height = Math.round((widget.getBounds().getRows() - (toCenter * 2)) * charheight);
		g.drawRect(left, top, width, height);
	}
	/*
	 * Semi primitive terminal draw-operations.
	 */
	public void displayBorder(Widget widget) {
		Color borderColor = ColorLookup.borderColorFor(widget);
		if (borderColor != null) {
			g.setColor(Previewer.toAWT(borderColor));
			this.drawBorder(widget);
		}
	}
	/**
	 * @param toDisplay
	 *        The string that will be shown
	 * @param row
	 *        row position
	 * @param column
	 *        column position
	 * @param columns
	 *        length of the box
	 * @param alignment
	 *        alignment in the box
	 */
	public void displayAlignedString(String toDisplay, int row, int column, int columns, byte alignment) {
		/**
		 * pre: alignment must be a Label alignment.
		 */
		if (toDisplay.length() >= columns) {
			toDisplay = toDisplay.substring(0, columns);
		} else {
			switch (alignment) {
			case Label.LEFT:
				// Do nothing; left alignment is default
				break;
			case Label.CENTER: // Center text
				// Center text by setting columns and leftColumn
				column += Math.floor((columns - toDisplay.length()) / 2);
				columns = toDisplay.length();
				break;
			case Label.RIGHT:
				// Align right by setting columns and leftColumn
				column += (columns - toDisplay.length());
				columns = toDisplay.length();
				break;
			default:
				// This code should not be reached, if it is, an unknown align
				// mode
				// is set. Enough reason to throw an exception.
				throw new RuntimeException("Encountered unknown label alignment in widgetrenderer");
			}
		}
		this.displayString(toDisplay, row, column, columns);
	}
	/**
	 * drawCheckAt(int, int, bool) shows a check mark if state is true at row, column
	 * 
	 * @param row
	 *        row position of check
	 * @param column
	 *        column position of check
	 * @param state
	 *        boolean containing the state
	 * @param check
	 *        contains a String of characters to be printed overlapping each other at row,column. "\\/" creates an X.
	 */
	public void drawCheckAt(int row, int column, boolean state, String check) {
		// TODO
		if (true) {
			for (int i = 0; i < check.length(); i++) {
				this.displayString(check.substring(i, i + 1), row, column, 1);
			}
		}
	}
	//	
	// Handling Wigets
	//
	public void handle(Panel aPanel) {
		this.displayBackground(aPanel);
		for (int w = 0; w < aPanel.getWidgets().size(); w++) {
			Widget each = (Widget) aPanel.getWidgets().get(w);
			each.kindDo(this);
		}
	}
	public void handle(Screen aScreen) {
		this.displayBackground(aScreen);
		for (int w = 0; w < aScreen.getWidgets().size(); w++) {
			Widget each = (Widget) aScreen.getWidgets().get(w);
			each.kindDo(this);
		}
	}
	public void handle(Label aLabel) {
		this.displayBackground(aLabel);
		this.setupForeground(aLabel);
		this.displayAlignedString(aLabel.getString(), aLabel.getTopRow(), aLabel.getLeftColumn(), aLabel.getColumns(),
				aLabel.getAlignment());
	}
	public void handle(Button aButton) {
		this.handle((Label) aButton);
	}
	public void handle(List aList) {
		this.displayBackground(aList);
		this.setupForeground(aList);
		for (int i = 0; i < Math.min(aList.getItems().size(), aList.getRows()); i++) {
			Object item = aList.getItems().get(i);
			this.displayString(item, aList.getTopRow() + i, aList.getLeftColumn(), aList.getColumns());
		}
	}
	public void handle(Choice aList) {
		this.displayBackground(aList);
		this.setupForeground(aList);
		// this.displayBorder(aList);
		String item = "";
		if (!aList.getItems().isEmpty()) {
			item = this.stringForItem(aList.getItems().get(0));
		}
		StringBuffer buffer = new StringBuffer();
		for (int c = 0; c < (Math.min(item.length(), aList.getColumns() - 1)); c++) {
			buffer.append(item.charAt(c));
		}
		for (int c = item.length(); c < aList.getColumns() - 1; c++) {
			buffer.append(' ');
		}
		buffer.append('=');
		this.displayString(buffer.toString(), aList.getTopRow(), aList.getLeftColumn(), aList.getColumns());
	}
	public void handle(TextField aTextField) {
		this.displayBackground(aTextField);
		this.setupForeground(aTextField);
		this.displayString(aTextField.getString(), aTextField.getTopRow(), aTextField.getLeftColumn(), aTextField
				.getColumns());
	}
	public void handle(TextArea aTextArea) {
		this.displayBackground(aTextArea);
		this.setupForeground(aTextArea);
		ArrayList lines = aTextArea.computeTextLines();
		for (int i = 0; i < lines.size(); i++)
			this.displayString((String) lines.get(i), aTextArea.getTopRow() + i, aTextArea.getLeftColumn(), aTextArea
					.getColumns());
	}
	public void handleCheck(CheckBox aBox, String check) {
		this.displayBackground(aBox);
		this.setupForeground(aBox);
		int row = aBox.getTopRow();
		int column = aBox.getLeftColumn();
		int columns = aBox.getColumns();
		switch (aBox.getTickAlignment()) {
		case Label.LEFT:
			this.drawCheckAt(row, column, aBox.isSelected(), check);
			column++;
			columns--;
			break;
		case Label.RIGHT:
			this.drawCheckAt(row, column + columns - 1, aBox.isSelected(), check);
			columns--;
			break;
		case Label.CENTER:
			throw new RuntimeException("Center alignment not valid for tickalignment");
		// No break because of throw clause
		default:
			throw new RuntimeException("Unknown alignment encountered for tickalignment");
		}
		this.displayAlignedString(aBox.getString(), row, column, columns, aBox.getAlignment());
	}
	public void handle(CheckBox aBox) {
		handleCheck(aBox, "\\/");
	}
	public void handle(Image anImage) {
		this.displayBackground(anImage);
		Bounds bounds = anImage.getBounds();
		int left = (bounds.getLeftColumn() - 1) * charwidth + screenoffsetx;
		int top = (bounds.getTopRow() - 1) * charheight + screenoffsety;
		int width = bounds.getColumns() * charwidth;
		int height = bounds.getRows() * charheight;
		left += 2;
		top += 2;
		width -= 4;
		height -= 4;
		g.setColor(java.awt.Color.red);
		g.drawLine(left, top, left, top + height);
		g.drawLine(left + width, top, left + width, top + height);
		g.drawLine(left, top, left + width, top);
		g.drawLine(left, top + height, left + width, top + height);
		g.drawLine(left, top, left + width, top + height);
		g.drawLine(left, top + height, left + width, top);
	}
	public void handle(RadioButton aRadioButton) {
		handleCheck(aRadioButton, "o");
	}
	public void displayTableColumn(TableColumn column, int leftEdge) {
		// Heading
		column.getHeading().setBounds(new Bounds(column.getTableList().getTopRow(), leftEdge, 1, column.getWidth()));
		this.handle(column.getHeading());
		// Data
		WidgetAppearance colunmAppearance = column.getAppearanceOrNull();
		if (colunmAppearance == null)
			colunmAppearance = column.getTableList().getAppearanceOrNull();
		this.displayBackground(colunmAppearance.getBackground(), leftEdge, column.getTableList().getTopRow() + 1,
				column.getWidth(), column.getTableList().getRows() - 1);
	}
	public void handle(TableList aTableList) {
		this.displayBackground(aTableList);
		// This method has become very large and is candidate for refactoring.
		int[] widths = new int[aTableList.getTableColumns().size()];
		int totalWidth = 0;
		WidgetAppearance[] appearances = new WidgetAppearance[aTableList.getTableColumns().size()];
		int leftEdge = aTableList.getLeftColumn();
		// Show heading and calculate width
		for (int i = 0; i < aTableList.getTableColumns().size(); i++) {
			TableColumn currentColumn = (TableColumn) aTableList.getTableColumns().get(i);
			this.displayTableColumn(currentColumn, leftEdge);
			leftEdge += currentColumn.getWidth();
		}
		// Show items in list
		for (int rowIter = 0; rowIter < aTableList.getItems().size(); rowIter++) {
			int currentPositionOnRow = 0;
			Object untypedObjects = aTableList.getItems().get(rowIter);
			String[] items;
			if (untypedObjects instanceof String[]) {
				items = (String[]) untypedObjects;
			} else {
				TableListItem dtiItems = (TableListItem) untypedObjects;
				items = new String[dtiItems.size()];
				for (int i = 0; i < dtiItems.size(); i++) {
					items[i] = dtiItems.get(i);
				}
			}
			for (int colIter = 0; colIter < Math.min(items.length, widths.length); colIter++) {
				WidgetAppearance wap = appearances[colIter];
				if (wap != null)
					this.setupForeground(wap.getForeground());
				this.displayString(items[colIter], aTableList.getTopRow() + rowIter + 1, aTableList.getLeftColumn()
						+ currentPositionOnRow, widths[colIter]);
				currentPositionOnRow += widths[colIter];
			}
		}
	}
	public void handle(TableColumn aTableColumn) {
		/*
		 * a TableColumn has no knowledge about its position, so for presentation, it's only useful in some kind of
		 * table context, so that's where the 'real stuff' happens See handle(TableList)
		 */
	}
	public void handle(GroupBox aGroupBox) {
		// What to do here?
		this.displayBackground(aGroupBox);
		this.setupForeground(aGroupBox);
		handle((CompositeWidget) aGroupBox);
		this.drawBorder(aGroupBox, 0.5F);
	}
	public void handle(MenuList aMenuList) {
		// A menu is a list with actions for each element.
		// Bug: border should be sized '1' at least. A list does not understand
		// this.
		handle((List) aMenuList);
	}
	public void handle(Menu aMenu) {
		// Should be handle by menu container such as MenuBar
	}
	public void handle(MenuBar aMenuBar) {
		// No preview
	}
	public void handle(CompositeWidget widg) {
		this.displayBackground(widg);
		this.setupForeground(widg);
		java.util.List widgets = widg.getWidgets();
		for (int widgIter = 0; widgIter < widgets.size(); widgIter++) {
			((Widget) widgets.get(widgIter)).kindDo(this);
		}
	}
	public void handle(Timer counter) {
		this.displayBackground(counter);
		this.setupForeground(counter);
		displayString(new Integer(counter.getStart()).toString(), counter.getTopRow(), counter.getLeftColumn(), counter
				.getColumns());
	}
	/**
	 * @todo
	 */
	public void handle(Console aConsole) {		
	}
}