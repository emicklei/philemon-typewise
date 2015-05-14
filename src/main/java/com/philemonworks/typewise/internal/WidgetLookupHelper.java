/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004-2006. All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author E.M.Micklei
 */
package com.philemonworks.typewise.internal;

import java.io.Serializable;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.server.ApplicationView;

/**
 *
 */
public class WidgetLookupHelper implements WidgetLookup, Serializable {
	private static final long serialVersionUID = 4416185756309821929L;
	ApplicationView client;

	public WidgetLookupHelper(ApplicationView newClient) {
		client = newClient;
	}
	public WidgetLookupHelper(ApplicationModel aModel) {
		client = aModel.view;
	}
	public WidgetLookupHelper(Screen aScreen) {
		client = aScreen.getModel().view;
	}
	private Widget widgetNamed(String aString) {
		Widget foundOrNull = client.getCurrentScreen().widgetNamed(aString);
		if (foundOrNull == null)
			Logger.getLogger(this.getClass()).warn(
					"no such widget found:[" + aString + "] in screen:" + client.getCurrentScreen() + " of model:" + client.getCurrentScreen().getModel());
		return foundOrNull;
	}
	// Convenient access to widgets
	public CheckBox getCheckBox(String widgetName) {
		return (CheckBox) this.widgetNamed(widgetName);
	}
	public RadioButton getRadioButton(String widgetName) {
		return (RadioButton) this.widgetNamed(widgetName);
	}
	public TableList getTableList(String widgetName) {
		return (TableList) this.widgetNamed(widgetName);
	}
	public TextField getTextField(String widgetName) {
		return (TextField) this.widgetNamed(widgetName);
	}
	public TextArea getTextArea(String widgetName) {
		return (TextArea) this.widgetNamed(widgetName);
	}
	public Button getButton(String widgetName) {
		return (Button) this.widgetNamed(widgetName);
	}
	public Choice getChoice(String widgetName) {
		return (Choice) this.widgetNamed(widgetName);
	}
	public GroupBox getGroupBox(String widgetName) {
		return (GroupBox) this.widgetNamed(widgetName);
	}
	public Image getImage(String widgetName) {
		return (Image) this.widgetNamed(widgetName);
	}
	public Label getLabel(String widgetName) {
		return (Label) this.widgetNamed(widgetName);
	}
	public List getList(String widgetName) {
		return (List) this.widgetNamed(widgetName);
	}
	public Panel getPanel(String widgetName) {
		return (Panel) this.widgetNamed(widgetName);
	}
	public Timer getTimer(String widgetName) {
		return (Timer) this.widgetNamed(widgetName);
	}
}
