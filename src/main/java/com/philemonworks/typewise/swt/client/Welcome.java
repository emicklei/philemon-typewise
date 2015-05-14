package com.philemonworks.typewise.swt.client;

import java.util.HashMap;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.util.Color;

/**
 * Welcome is the default starup screen when no URL was passed to the ApplicationView.
 *
 * @author E.M.Micklei
 */
public class Welcome extends ApplicationModel {
	private static final long serialVersionUID = 1L;
	private String defaultURL = "http://{server}/{context}/{model}/{method}";
	private String errorMessage = "                                                                    ";
	public Welcome(ApplicationModel myParent) {
		super(myParent);
	}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Welcome(null).test("main");
	}
    /**
     * This methods is sent when the normal server application startup fails.
     * @param argumentMap
     */
    public void error(HashMap argumentMap) {
        view.show(this.mainScreen());
    }
	public Screen mainScreen() {
		UIBuilder ui = new UIBuilder();
		Screen shell = ui.addScreen("main", this, 30, 80);
		shell.setBackground(Color.white);
		shell.setTitle("TypeWise - Internet Application Browser");
		ui.setPosition(14, 10);
		this.addAddressTo(ui);
		ui.setPosition(16,10);
		this.addErrorMessageTo(ui);		
		ui.setPosition(2, 4);
		this.addLogoTo(ui);
		ui.setPosition(22, 48);
		this.addGuideTo(ui);
		return shell;
	}
	public void afterShow() {
		view.getTextField("address").setFocus();
	}
	/**
	 * @param ui
	 */
	public void addAddressTo(UIBuilder ui) {
		TextField address = ui.addTextField("address", 60);
		address.setText(defaultURL);
		address.setForeground(Color.black);
		address.onSendTo(CWT.EVENT_CLICKED, "gotoURL", this);
		ui.space(1);
		Button go = ui.addButton("go", " Go ");
		go.setForeground(Color.white);
		go.setBackground(Color.gray);
		go.onSendTo(CWT.EVENT_CLICKED, "gotoURL", this);
	}
	/**
	 * Displays any error message passed from the requestor
	 * @param ui
	 */
	public void addErrorMessageTo(UIBuilder ui){
		if (errorMessage.length() == 0) return;
		Label msg = ui.addLabel("error",errorMessage);
		msg.setForeground(Color.red);
	}
	/**
	 * @param ui
	 */
	public void addLogoTo(UIBuilder ui) {
		Image img = ui.addImage("logo", 20, 30);
		img.setScaleToFit(false);
		img.setUrl(SwtUtils.urlForLocalResource("icon_final.gif"));
		img.onSendTo(CWT.EVENT_CLICKED, "gotoTypeWise", this);
	}
	/**
	 * @param ui
	 */
	public void addGuideTo(UIBuilder ui) {
		TextArea area = ui.addTextArea("guide", 7, 30);
		area.setForeground(Color.gray);
		area.setText("Control 1 (+) = larger font\nControl 2 (-) = smaller font\nControl 3     = choose font");
		area.setEnabled(false);
	}
	/**
	 * 
	 */
	public void gotoURL() {
		view.gotoURL(view.getTextField("address").getText());
	}
	/**
	 * 
	 */
	public void gotoTypeWise(){
		view.openURL("http://www.typewise.org");
	}
	/**
	 * @return Returns the defaultURL.
	 */
	public String getDefaultURL() {
		return defaultURL;
	}
	/**
	 * @param defaultURL The defaultURL to set.
	 */
	public void setDefaultURL(String defaultURL) {
		this.defaultURL = defaultURL;
	}
	/**
	 * @return Returns the errorMessage.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
