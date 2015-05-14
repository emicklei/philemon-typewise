package com.philemonworks.typewise.swt.client;

import com.philemonworks.typewise.Dialog;
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
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Console;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.server.WidgetFactory;
import com.philemonworks.typewise.swt.SwtButton;
import com.philemonworks.typewise.swt.SwtCheckBox;
import com.philemonworks.typewise.swt.SwtChoice;
import com.philemonworks.typewise.swt.SwtComposite;
import com.philemonworks.typewise.swt.SwtDialog;
import com.philemonworks.typewise.swt.SwtGroupBox;
import com.philemonworks.typewise.swt.SwtImageView;
import com.philemonworks.typewise.swt.SwtLabel;
import com.philemonworks.typewise.swt.SwtList;
import com.philemonworks.typewise.swt.SwtMenu;
import com.philemonworks.typewise.swt.SwtMenuBar;
import com.philemonworks.typewise.swt.SwtMenuList;
import com.philemonworks.typewise.swt.SwtRadioButton;
import com.philemonworks.typewise.swt.SwtScreen;
import com.philemonworks.typewise.swt.SwtTableList;
import com.philemonworks.typewise.swt.SwtTextArea;
import com.philemonworks.typewise.swt.SwtTextField;
import com.philemonworks.typewise.swt.custom.SwtConsole;
import com.philemonworks.typewise.swt.custom.SwtTimer;

/**
 * SwtWidgetFactory is a WidgetFactory that creates specilizations
 * of CWT widgets that use SWT technology.
 *
 * @author E.M.Micklei
 */
public class SwtWidgetFactory implements WidgetFactory {

	public Button createButton() {
		return new SwtButton("",1,1,1,1);
	}

	public CheckBox createCheckBox() {
		return new SwtCheckBox("",1,1,1,1);
	}

	public Choice createChoice() {
		return new SwtChoice("",1,1,1,1);
	}

	public GroupBox createGroupBox() {
		return new SwtGroupBox("",1,1,1,1);
	}

	public Image createImage() {
		// return new SwtImage("",1,1,1,1);
		return new SwtImageView("",1,1,1,1);
	}

	public Label createLabel() {
		return new SwtLabel("",1,1,1,1);
	}

	public List createList() {
		return new SwtList("",1,1,1,1);
	}

	public Menu createMenu() {
		return new SwtMenu("");
	}

	public MenuList createMenuList() {
		return new SwtMenuList("",1,1,1,1);
	}

	public MenuBar createMenuBar() {
		return new SwtMenuBar("",1,1,1,1);
	}

	public Panel createPanel() {
		return new SwtComposite("",1,1,1,1);
	}

	public RadioButton createRadioButton() {
		return new SwtRadioButton("",1,1,1,1);
	}

	public Screen createScreen() {
		return new SwtScreen("",null,1,1);
	}

	public TableList createTableList() {
		return new SwtTableList("",1,1,1,1);
	}

	public TextArea createTextArea() {
		return new SwtTextArea("",1,1,1,1);
	}

	public TextField createTextField() {
		return new SwtTextField("",1,1,1,1);
	}

	public Timer createTimer() {
		return new SwtTimer("",1,1,1,1);
	}

	public Dialog createDialog() {
		return new SwtDialog();
	}

	public Console createConsole() {
		return new SwtConsole("",1,1,1,1);
	}
}
