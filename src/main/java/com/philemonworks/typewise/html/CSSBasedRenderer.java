package com.philemonworks.typewise.html;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.Bounds;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.TableListItem;
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
import com.philemonworks.writer.HTMLWriter;
import com.philemonworks.writer.XMLWriter;

/**
 * CSSBasedRenderer is an HTML 4.01 renderer that uses Style for formatting the elements.
 * 
 * @author E.M.Micklei
 */
public class CSSBasedRenderer implements WidgetHandler {
	private static final double TEXTAREA_ROW_FACTOR = 0.7;
	/**
	 * Is needed for generating documentation pages from models.
	 */
	public static final String EMPTYLISTITEM = "<empty>";
	int dx = 10;
	int dy = 13;
	/**
	 * Is needed for the URL construction of the image parameter.
	 */
	public String imagesPath = "";
	/**
	 * Is needed for generating documentation pages from models.
	 */
	public boolean includeEmptyListItem = false;
	HTMLWriter writer;
	final static String TIMER_SIZE = "4";
	/**
	 * Default font
	 */
	public String fontFamily = "monospace";
	public String fontSize = null;
	private String philemonOnload = "<script type=\"text/javascript\">\nfunction philemonOnload() { ";
	private String philemonOnloadEnd = "}\n</script>";
	private String philemonOnkeydown = "<script type=\"text/javascript\">\nfunction philemonOnkeydown(e) {";
	private String philemonOnkeydownEnd = "}\n</script>";
	private String postAction = null;
	/**
	 * absolute offset horizontal in pixels
	 */ 
	public int screenOffsetX = 2;
	/**
	 * absolute offset vertical in pixels
	 */	
	public int screenOffsetY = 2;
	/**
	 * @param w 
	 */
	public CSSBasedRenderer(HTMLWriter w) {
		super();
		writer = w;
	}
	protected String asFixedLength(ListItem line, int length) {
		return this.asFixedLength(line.getItem(), length);
	}
	protected String asFixedLength(Object line, int length) {
		return this.asFixedLength(line.toString(), length);
	}
	public String asHTML(String content){
		// TODO 
		return content.replaceAll("&#10;","<br/>");
	}
	/**
	 * Answer a new String with a fixed length based on the input line. If the line is too long then chop it. If the
	 * line is too short then append non-breaking spaces to it. Length is not the String length!.
	 * 
	 * @param line
	 * @param length
	 * @return String
	 */
	protected String asFixedLength(String line, int length) {
		if (line.length() > length)
			return line.substring(0, length);
		StringBuffer buffer = new StringBuffer();
		buffer.append(XMLWriter.encoded(line));
		for (int i = line.length(); i < length; i++)
			buffer.append("&nbsp;");
		return buffer.toString();
	}
	/**
	 * @param item
	 *        is a String | DisplayItem
	 * @return the String value of an item
	 */
	private String asString(Object item) {
		if (item == null)
			return "";
		if (item instanceof String)
			return (String) item;
		if (item instanceof ListItem)
			return ((ListItem) item).getItem();
		throw new RuntimeException("cannot convert to String:" + item);
	}
	/**
	 * Export the HTML to show a dialog along with the current screen.
	 * 
	 * @param dialog : Dialog
	 * @param bounds : Bounds
	 */
	public void handle(Dialog dialog, Bounds bounds) {
		Style style = this.newStyle();
		// todo: move this to ????
		String rawStyle = "background:#FF0000;position:absolute;left:{0};top:10;width:{1};height:20;border:#FF0000 solid 1px;text-align:center;color:white;font-weight:bold;";		
		style.append(MessageFormat.format(rawStyle, new String[]{ 
				String.valueOf(screenOffsetX),
				String.valueOf(bounds.getColumns() * dx) 
				}));
		writer.tag("div", style.asMap(), false);
		writer.print(false, dialog.getText(), false);
		writer.end(); // div
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Button)
	 */
	public void handle(Button aButton) {
		Style style = this.newStyle();
		// calc position
		style.append(this.positionFor(aButton, -8, -8));
		style.background(aButton.getBackground());
		style.foreground(aButton.getForeground());
		style.width(dx * aButton.getColumns() + 8);
		style.height(dy * aButton.getRows() + 12);
		Map map = style.asMap();
		map.put("name", aButton.getName());
		map.put("value", aButton.getName());
		map.put("onclick", JavascriptBuilder.writeFormEventCall(aButton.getName(), ApplicationModel.EVENT_CLICKED));
		writer.button(map, aButton.getText());
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.CheckBox)
	 */
	public void handle(CheckBox aCheckBox) {
		Style style = this.newStyleFor(aCheckBox);
		Map map = style.asMap();
		map.put("type", "checkbox");
		map.put("name" , aCheckBox.getName());
		map.put("size", "" + aCheckBox.getColumns());
		if (aCheckBox.isSelected()) {
			map.put("checked value", aCheckBox.getName());
		} else {
			map.put("value", aCheckBox.getName());
		}
		writer.tag("input", map, true);
		// temp label
		Label boxLabel = new Label(aCheckBox.getName() + "_label", aCheckBox.getTopRow(), aCheckBox.getLeftColumn() + 2, aCheckBox.getRows(), aCheckBox
				.getColumns());
		boxLabel.setText(aCheckBox.getText());
		this.handle(boxLabel);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Choice)
	 */
	public void handle(Choice aChoice) {
		Style style = this.newStyleFor(aChoice);
		Map map = style.asMap();
		map.put("size", "1");
		if (aChoice.hasMessageForEvent(ApplicationModel.EVENT_SELECTEDITEM)) {
			map.put("onchange", "submit();");
		}
		map.put("name", aChoice.getName());
		writer.tag("select", map, false);
		java.util.List itemsToDisplay = aChoice.getItems();
		if (includeEmptyListItem && itemsToDisplay.isEmpty()) {
			itemsToDisplay = new ArrayList();
			itemsToDisplay.add(EMPTYLISTITEM);
		}
		int fixedLength = aChoice.getColumns();
		this.writeOptionItems(itemsToDisplay, aChoice.getSelectionIndex(), fixedLength);
		writer.end();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.internal.CompositeWidget)
	 */
	public void handle(CompositeWidget aCompositeWidget) {
		writer.pretty = false;
		for (int w = 0; w < aCompositeWidget.getWidgets().size(); w++) {
			Widget each = (Widget) aCompositeWidget.getWidgets().get(w);
			each.kindDo(this);
			writer.raw("\n");
		}
		writer.pretty = true;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.custom.Console)
	 */
	public void handle(Console aConsole) {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.GroupBox)
	 */
	public void handle(GroupBox aGroupBox) {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Image)
	 */
	public void handle(Image anImage) {
		String url = anImage.getUrl();
		Style style = this.newStyleFor(anImage);
		Map map = style.asMap();
		map.put("alt", anImage.getName());
		if (url.length() > 0) {
			int start = imagesPath.length();
			if (start > 0)
				start++; // loose the slash
			// compute width and height
			int width = anImage.getColumns() * dx;
			int height = anImage.getRows() * dy;
			if (anImage.isScaleToFit()) {
				map.put("width", String.valueOf(width));
				map.put("height", String.valueOf(height));
			}
			map.put("src", url.substring(start, url.length()));
		} else
			map.put("src", "");
		writer.tag("img", map, true);
	}
	public void handle(Label aLabel) {
		Style style = this.newStyleFor(aLabel);
		writer.tag("div", style.asMap(), false);
		writer.print(false, aLabel.getText(), false);
		writer.end();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.List)
	 */
	public void handle(List aList) {
		Style style = this.newStyle();
		// calc position
		style.append(this.positionFor(aList, 0, -2));
		style.background(aList.getBackground());
		style.foreground(aList.getForeground());
		style.width(dx * aList.getColumns() + 4); // TODO
		style.height(dy * aList.getRows() + 8); // TODO
		Map map = style.asMap();
		map.put("name", aList.getName());
		if (aList.hasMessageForEvent(ApplicationModel.EVENT_SELECTEDITEM)) {
			// map.put("onchange", "submit();");
			map.put("onchange", JavascriptBuilder.writeFormEventCall(aList.getName(), CWT.EVENT_SELECTEDITEM));
		}
		map.put("size", String.valueOf(aList.getRows()));
		writer.tag("select", map, false);
		java.util.List itemsToDisplay = aList.getItems();
		if (itemsToDisplay.isEmpty() && includeEmptyListItem) {
			itemsToDisplay = new ArrayList();
			itemsToDisplay.add(EMPTYLISTITEM);
		}
		int fixedLength = aList.getColumns();
		this.writeOptionItems(itemsToDisplay, aList.getSelectionIndex(), fixedLength);
		writer.end();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Menu)
	 */
	public void handle(Menu aMenu) {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.MenuBar)
	 */
	public void handle(MenuBar aMenuBar) {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.MenuList)
	 */
	public void handle(MenuList aMenuList) {
		// TODO Auto-generated method stub
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Panel)
	 */
	public void handle(Panel aPanel) {
		this.writeBackgroundFor(aPanel, 0, 0);
		this.handle((CompositeWidget) aPanel);
	}
	public void handle(RadioButton aRadioButton) {
		Style style = this.newStyleFor(aRadioButton);
		Map map = style.asMap();
		map.put("type", "radio");
		map.put("name", aRadioButton.getGroupName());
		map.put("size", "" + aRadioButton.getColumns());
		if (aRadioButton.isSelected()) {
			map.put("checked value", aRadioButton.getName());
		} else {
			map.put("value", aRadioButton.getName());
		}
		writer.tag("input", map, true);
		// temp label
		Label radioLabel = new Label(aRadioButton.getName() + "_label", aRadioButton.getTopRow(), aRadioButton.getLeftColumn() + 2, aRadioButton.getRows(),
				aRadioButton.getColumns());
		radioLabel.setText(aRadioButton.getText());
		this.handle(radioLabel);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Screen)
	 */
	public void handle(Screen aScreen) {
		String javascript = JavascriptBuilder.writeFormEventFunction();
		writer.raw(javascript);
		writer.raw(this.writeKeyEventHandler(aScreen));
		writer.raw("\n");
		this.writeBackgroundFor(aScreen, screenOffsetX, screenOffsetY);
		// open form
		Map map = new HashMap();
		map.put("method", "post");
		map.put("action", postAction);
		writer.tag("form", map, false);
		writer.div(null);
		// Write hidden variables + formEvent javascript
		// TODO: Is this the right place for javascript stuff?
		Map map2 = new HashMap();
		map2.put("type", "hidden");
		map2.put("name", "_submitWidget");
		writer.tag("input", map2, true);
		Map map3 = new HashMap();
		map3.put("type", "hidden");
		map3.put("name", "_widgetEvent");
		writer.tag("input", map3, true);
		this.handle((CompositeWidget) aScreen);
		writer.end("div");
		writer.end("form");
		// Now we can write the OnLoad and KeyDown because source has been collected
		writer.raw(philemonOnload + philemonOnloadEnd); // write javascript onload
		writer.raw(philemonOnkeydown + philemonOnkeydownEnd); // write javascript onkeydown
	}
	private void writeBackgroundFor(CompositeWidget container, int marginX, int marginY) {
		Map map;
		String bgcolor = ColorLookup.backgroundFor(container).toHTML();
		map = writer.newMap("style", "{background:" + bgcolor + ";position:absolute;left:" + (marginX + dx * (container.screenLocation().column - 1))
				+ "px;top:" + (marginY + dy * (container.screenLocation().row - 1)) + "px;width:" + (dx * (container.getColumns() + 1)) + "px;height:"
				+ (dy * (container.getRows() + 2)) + // TODO why 2 ?
				"px;border-style:solid;border-width:1px;border-color:gray;"); // TODO borderColor?
		writer.tag("div", map, true);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.TableColumn)
	 */
	public void handle(TableColumn aTableColumn) {
		// TODO Auto-generated method stub
	}
	public void handle(TableList aTableList) {
		new SimpleTableListRenderer(this).handle(aTableList);
		// new SortableTableListRenderer(this).handle(aTableList);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.TableList)
	 */
	public void handle2(TableList aTableList) {
		int left = aTableList.getLeftColumn();
		for (int c = 0; c < aTableList.getTableColumns().size(); c++) {
			TableColumn column = (TableColumn) aTableList.getTableColumns().get(c);
			Label header = new Label(column.getName(), 1, 1, 1, 1);
			// header.setParent(aTableList);
			header.setAppearance(column.getHeading().getAppearanceOrNull());
			header.setAlignment(column.getHeading().getAlignment());
			header.setText(column.getHeading().getText());
			Bounds box = new Bounds(aTableList.getTopRow(), left, 1, column.getWidth());
			header.setBounds(box);
			// this.handle(header);
			left += column.getWidth() - 1;
		}
		// temporary change toprow and rows
		aTableList.getBounds().setTopRow(aTableList.getTopRow() + 1);
		aTableList.getBounds().setRows(aTableList.getRows() - 1);
		Style style = this.newStyleFor(aTableList);
		aTableList.getBounds().setTopRow(aTableList.getTopRow() - 1);
		aTableList.getBounds().setRows(aTableList.getRows() + 1);
		Map map = style.asMap();
		map.put("leftpadding", "0");
		map.put("width", "100%");
		// Adjusting the style after printing the header for IE / Mozilla inconsequences
		// Mozilla needs the padding to be 0, otherwise it will pad the stuff in the options also
		// IE needs the width to be 100%, because it won't show a scrollbar if not necessary.
		map.put("name", aTableList.getName());
		if (aTableList.hasMessageForEvent(ApplicationModel.EVENT_SELECTEDITEM)) {
			map.put("onchange", "submit();");
		}
		map.put("size", String.valueOf(aTableList.getRows() - 2));
		writer.tag("select", map, false);
		java.util.List itemsToDisplay = aTableList.getItems();
		if (itemsToDisplay.isEmpty() && includeEmptyListItem) {
			itemsToDisplay = new ArrayList();
			TableListItem item = new TableListItem("no-id");
			for (int c = 0; c < aTableList.getTableColumns().size(); c++) {
				item.add(EMPTYLISTITEM);
			}
			itemsToDisplay.add(item);
		}
		for (int i = 0; i < itemsToDisplay.size(); i++) {
			TableListItem item = (TableListItem) itemsToDisplay.get(i);
			if (i == aTableList.getSelectionIndex()) {
				writer.tag("option", "selected value", String.valueOf(i));
			} else {
				writer.tag("option", "value", String.valueOf(i));
			}
			StringBuffer buffer = new StringBuffer();
			for (int c = 0; c < aTableList.getTableColumns().size(); c++) {
				TableColumn column = (TableColumn) aTableList.getTableColumns().get(c);
				buffer.append(this.asFixedLength(item.get(c), column.getWidth())); 
			}
			writer.raw(buffer.toString()); //
			writer.end(); // option
		}
		writer.end(); // select
	}
	public void handle(TextArea aTextArea) {
//		if (!aTextArea.isEditable()) {
//			this.handle((Label) aTextArea);
//			return;
//		}
		/*
		boolean oldpretty = writer.pretty;
		writer.pretty = false;
		Style style = this.newStyleFor(aTextArea);
		// style.borderStyle("none");
		// style.append("overflow: hidden");
		Map map = style.asMap();
		map.put("type", "text");
		map.put("readonly","readonly");
		map.put("name", aTextArea.getName());
		map.put("cols", String.valueOf(aTextArea.getColumns()+4)); // TODO why 4?
		map.put("rows", String.valueOf(aTextArea.getRows()));
		writer.tag("textarea", map, false);
		writer.print(false, aTextArea.getText(), false);
		writer.end(); // textarea
		writer.pretty = oldpretty;
		*/
		Style style = this.newStyleFor(aTextArea);
		Map map = style.asMap();
		writer.tag("div" , map , false);
		writer.print(false, aTextArea.getText(), false);
		writer.end(); // textarea
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.TextField)
	 */
	public void handle(TextField aTextField) {
		Style style = this.newStyleFor(aTextField);
		Map map = style.asMap();
		String type = TextField.TYPE_PASSWORD.equals(aTextField.getType()) ? "password" : "text";
		map.put("type", type);
		map.put("size", String.valueOf(aTextField.getColumns()));
		if (aTextField.getMaxLength() > 0)
			map.put("maxlength", String.valueOf(aTextField.getMaxLength()));
		map.put("name", aTextField.getName());
		map.put("value", aTextField.getText());
		writer.tag("input", map, true);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.custom.Timer)
	 */
	public void handle(Timer aCounter) {
		Style style = this.newStyleFor(aCounter);
		style.borderStyle("none");
		style.background(ColorLookup.backgroundFor(aCounter));
		Map map = style.asMap();
		String javascript = JavascriptBuilder.writeTimerFunction(aCounter);
		writer.raw(javascript);
		philemonOnload += " " + JavascriptBuilder.TIMER_FUNCTION_NAME + "(); ";
		map.put("type", "text");
		map.put("name", aCounter.getName());
		map.put("size", String.valueOf(aCounter.getColumns()));
		writer.tag("input", map, false);
		writer.raw(aCounter.getText());
		writer.end();
	}
	private Style newStyle() {
		return new Style(); // .font(fontFamily).fontSize(fontSize);
	}
	public Style newStyleFor(Widget w) {
		Style style = this.newStyle();
		style.append(this.positionFor(w));
		style.background(w.getBackground());
		style.foreground(w.getForeground());
		return style;
	}
	public String positionFor(Widget w) {
		return this.positionFor(w, 0, 0);
	}
	public String positionFor(Widget w, int offx, int offy) {
		return MessageFormat.format("position:absolute;left:{0}px;top:{1}px;", new Object[] { String.valueOf(w.getLeftColumn() * dx + offx),
				String.valueOf(w.getTopRow() * dy + offy) });
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.WidgetHandler#handle(com.philemonworks.typewise.cwt.Label)
	 */
	/**
	 * Writes html <option> tags of the provided list of strings. The selectedItem provided will be the highlighted item
	 * on the page.
	 * 
	 * @param itemsToDisplay
	 *        is a List of String | ListItem
	 * @param index
	 *        is the current selection index (between -1 and size-1)
	 * @param fixedLength
	 *        is the number of characters for the option.
	 */
	private void writeOptionItems(java.util.List itemsToDisplay, int index, int fixedLength) {
		for (int i = 0; i < itemsToDisplay.size(); i++) {
			String stringItem = this.asString(itemsToDisplay.get(i));
			if (index == i) {
				writer.tag("option", "selected value", String.valueOf(i));
			} else {
				writer.tag("option", "value", String.valueOf(i));
			}
			writer.raw(this.asFixedLength(stringItem, fixedLength));
			writer.end();
		}
	}
	/**
	 * Generates key press javascript. Tricky, how to capture F1 events (which might not be triggered by browser) Tricky
	 * 2, are event codes equal for all browsers?
	 * 
	 * @param aScreen
	 * @return String the javascript
	 */
	private String writeKeyEventHandler(Screen aScreen) {
		StringBuffer javascript = JavascriptBuilder.writeKeyHandlerFunction(aScreen);
		philemonOnkeydown += " checkKeyDown(e);";
		return javascript.toString();
	}
	/**
	 * @return Returns the postAction.
	 */
	public String getPostAction() {
		return postAction;
	}
	/**
	 * @param postAction
	 *        The postAction to set.
	 */
	public void setPostAction(String postAction) {
		this.postAction = postAction;
	}
}
