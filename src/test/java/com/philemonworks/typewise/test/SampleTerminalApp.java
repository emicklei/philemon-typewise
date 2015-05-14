/*
 * Licensed Material - Property of PhilemonWorks B.V.
 *
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted.
 * See http://www.philemonworks.com for information.
 *
 * VERSION HISTORY
 * Created on 10-jan-2004 by emicklei
 * Last edited: 4-4-2004 by emicklei
 * */
package com.philemonworks.typewise.test;

import java.util.ArrayList;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageFactory;

/**
 * @author emicklei Purpose:
 */
public class SampleTerminalApp extends ApplicationModel implements CWT {
	
	public SampleTerminalApp(ApplicationModel myParent) {
		super(myParent);
	}
	public Screen test_ButtonClicked_TerminalClose() {
		UIBuilder ui = new UIBuilder();
		ui.addScreen("main", this, 20, 40).setTitle("close test");
		ui.addButton("simpleButton", "Close", 5).onSend(EVENT_CLICKED, MessageFactory.CLOSE);
		return ui.getScreen();
	}
	public Screen test_ButtonClicked_TerminalOpenHelp() {
		UIBuilder ui = new UIBuilder();
		ui.addScreen("main", this, 20, 40).setTitle("help test");
		ui.addButton("simpleButton", "Help", 5).onSend(EVENT_CLICKED, MessageFactory.OPENHELP);
		return ui.getScreen();
	}
	public Screen test_ListSelectedItem_ApplicationLog() {
		UIBuilder ui = new UIBuilder();
		ui.addScreen("main", this, 20, 40).setTitle("list test");
		List list = ui.addList("simpleList", 10, 10);
		ArrayList items = new ArrayList();
		items.add("Hello");
		items.add("World");
		items.add(new ListItem("1", "DisplayItem"));
		list.setItems(items);
		list.onSendTo(EVENT_SELECTEDITEM, "log", this);
		return ui.getScreen();
	}
	public Screen test_ListSelectedItem_TextFieldSetString() {
		UIBuilder ui = new UIBuilder();
		ui.addScreen("main", this, 20, 40).setTitle("list test");
		List list = ui.addList("simpleList", 10, 10);
		ArrayList items = new ArrayList();
		items.add("Hello");
		items.add("World");
		list.setItems(items);
		ui.cr(2);
		TextField field = ui.addTextField("which", 10);
		list.onSendTo(EVENT_SELECTEDITEM, WidgetAccessorConstants.SET_STRING, field);
		return ui.getScreen();
	}
	public Object screenWithArgument(Integer argument) {
		return new Screen("top", this, 1, 1);
	}
	public Message log(String aString) {
		System.out.println("String:" + aString);
		return MessageFactory.OK;
	}
	public Message log(ListItem aDisplayItem) {
		System.out.println("DisplayItem:" + aDisplayItem.getItem());
		return MessageFactory.OK;
	}
}
