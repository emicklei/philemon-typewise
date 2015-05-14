package com.philemonworks.typewise.server;

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

/**
 * ServerWidgetFactory is an implementor of WidgetFactory
 * that creates widgets that only live server-side.
 *
 * @author E.M.Micklei
 */
public class ServerWidgetFactory implements WidgetFactory {

	public Button createButton() {
		return new Button("", 1, 1, 1, 1);
	}

	public CheckBox createCheckBox() {
		return new CheckBox("", 1, 1, 1, 1);
	}

	public Choice createChoice() {
		return new Choice("", 1, 1, 1, 1);
	}

	public GroupBox createGroupBox() {
		return new GroupBox("",1,1,1,1);
	}

	public Image createImage() {
		return new Image("",1,1,1,1);
	}

	public Label createLabel() {
		return new Label("", 1, 1, 1, 1);
	}

	public List createList() {
		return new List("", 1, 1, 1, 1);
	}

	public Menu createMenu() {
		return new Menu("");
	}

	public MenuBar createMenuBar() {
		return new MenuBar("", 1, 1, 1, 1);
	}

	public Panel createPanel() {
		return new Panel("",1,1,1,1);
	}

	public RadioButton createRadioButton() {
		return new RadioButton("", 1, 1, 1, 1);
	}

	public Screen createScreen() {
		return new Screen("unknown",null, 1, 1);
	}

	public TableList createTableList() {
		return new TableList("",1,1,1,1);
	}

	public TextArea createTextArea() {
		return new TextArea("", 1, 1, 1, 1);
	}

	public TextField createTextField() {
		return new TextField("", 1, 1, 1, 1);
	}

	public Timer createTimer() {
		return new Timer("",1,1,1,1);
	}

	public MenuList createMenuList() {
		return new MenuList("",1,1,1,1);
	}

	public Dialog createDialog() {
		return new Dialog();
	}

	public Console createConsole() {
		return new Console("",1,1,1,1);
	}
}
