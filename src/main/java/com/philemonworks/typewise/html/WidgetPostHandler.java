/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 21-june-2005: created
 *
 */
package com.philemonworks.typewise.html;

import java.util.Map;
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
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetHandler;

/**
 * WidgetPostHandler can update the state of a Widget using the request parameter Map.
 * 
 * @author E.M.Micklei
 */
public class WidgetPostHandler implements WidgetHandler {
	Map parameters;

	public WidgetPostHandler(Map parameterMap) {
		super();
		parameters = parameterMap;
	}
	public void handle(Button aButton) {
		// TODO Auto-generated method stub
	}
	public void handle(CheckBox aCheckBox) {
		String contents = this.contentsOf(aCheckBox);
		if (contents == null) {
			aCheckBox.setSelection(false);
			return;
		}
		aCheckBox.setSelection(contents.equals(aCheckBox.getName()));
	}
	public void handle(Choice aDropDownList) {
		this.handleList((List) aDropDownList);
	}
	public void handle(GroupBox aGroupBox) {
		this.handle((CompositeWidget) aGroupBox);
	}
	public void handle(Image anImage) {
		// TODO Auto-generated method stub
	}
	public void handle(Label aLabel) {
		// TODO Auto-generated method stub
	}
	public void handle(List aList) {
		this.handleList(aList);
	}
	public void handle(MenuBar aMenuBar) {
		// TODO Auto-generated method stub
	}
	public void handle(MenuList aMenuList) {
		// TODO Auto-generated method stub
	}
	public void handle(Menu aMenu) {
		// TODO Auto-generated method stub
	}
	public void handle(Panel aPanel) {
		// TODO Auto-generated method stub
	}
	public void handle(RadioButton aRadioButton) {
		String selectedRadio = this.contentsForKey(aRadioButton.getGroupName());
		if (selectedRadio == null)
			return;
		if (selectedRadio.equals(aRadioButton.getName()))
			aRadioButton.setSelection(true);
		else
			aRadioButton.setSelection(false);
	}
	public void handle(Screen aScreen) {
		this.handle((CompositeWidget) aScreen);
	}
	public void handle(TableColumn aTableColumn) {
		// TODO Auto-generated method stub
	}
	public void handle(TableList aTableList) {
		this.handleList((List) aTableList);
	}
	public void handle(TextArea aTextArea) {
		String text = this.contentsOf(aTextArea);
		if (text != null)
			aTextArea.setText(text);
	}
	public void handle(TextField aTextField) {
		String text = this.contentsOf(aTextField);
		if (text != null)
			aTextField.setText(text);
	}
	public void handle(CompositeWidget aCompositeWidget) {
		java.util.List children = aCompositeWidget.getWidgets();
		for (int w = 0; w < children.size(); w++) {
			Widget child = (Widget) children.get(w);
			child.kindDo(this);
		}
	}
	public void handle(Timer aCounter) {
		String contents = this.contentsOf(aCounter);
		if (contents == null)
			return;
		int newStart = Integer.parseInt(contents);
		if (newStart < aCounter.getStop()) {
			aCounter.setInterval(newStart, aCounter.getStop());
		}
	}
	private String contentsOf(Widget aWidget) {
		return this.contentsForKey(aWidget.getName());
	}
	private String contentsForKey(String key) {
		String[] args = (String[]) parameters.get(key);
		if (args == null)
			return null;
		if (args.length == 0)
			return null;
		return args[0];
	}
	private void handleList(List listWidget) {
		String contents = this.contentsOf(listWidget);
		if (contents == null)
			return;
		// Contents represents selection index in list
		if (contents.length() == 0)
			return;
		int index = Integer.valueOf(contents).intValue();
		listWidget.basicSetSelectionIndex(index);
	}
	/**
	 * @todo
	 */
	public void handle(Console aConsole) {
	}
}
