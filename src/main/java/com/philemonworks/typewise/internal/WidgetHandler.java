/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 7-feb-2004: created by emicklei
 * 4-4-2004: last modified
 * 
 * */
package com.philemonworks.typewise.internal;

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
/**
 * @author emicklei
 *
 * Purpose:
 *
 */
public interface WidgetHandler {
	public void handle(Button aButton);	
	public void handle(CheckBox aCheckBox);
	public void handle(Choice aDropDownList);
	public void handle(GroupBox aGroupBox);
	public void handle(Image anImage);
	public void handle(Label aLabel);
	public void handle(List aList);
	public void handle(MenuBar aMenuBar);
	public void handle(MenuList aMenuList);
	public void handle(Menu aMenu);	
	public void handle(Panel aPanel);
	public void handle(RadioButton aRadioButton);
	public void handle(Screen aScreen);
	public void handle(TableColumn aTableColumn);
	public void handle(TableList aTableList);
	public void handle(TextArea aTextArea);	
	public void handle(TextField aTextField);
	public void handle(CompositeWidget aCompositeWidget);
	// Custom widgets
	public void handle(Timer aCounter);
	public void handle(Console aConsole);
}
