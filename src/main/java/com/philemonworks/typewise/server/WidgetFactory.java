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
 * WidgetFactory is used by the BinaryWidgetAccessor to create widgets by processing a BWA formatted bytestream.
 * 
 * @author E.M.Micklei
 */
public interface WidgetFactory {
	/**
	 * @return Button
	 */
	Button createButton();
	/**
	 * @return CheckBox
	 */
	CheckBox createCheckBox();
	/**
	 * @return Choice
	 */
	Choice createChoice();
	/**
	 * @return GroupBox
	 */
	GroupBox createGroupBox();
	/**
	 * @return Image
	 */
	Image createImage();
	/**
	 * @return Label
	 */
	Label createLabel();
	/**
	 * @return List
	 */
	List createList();
	/**
	 * @return Menu
	 */
	Menu createMenu();
	/**
	 * @return MenuList
	 */
	MenuList createMenuList();	
	/**
	 * @return MenuBar
	 */
	MenuBar createMenuBar();
	/**
	 * @return Panel
	 */
	Panel createPanel();
	/**
	 * @return RadioButton
	 */
	RadioButton createRadioButton();
	/**
	 * @return Screen
	 */
	Screen createScreen();
	/**
	 * @return TableList
	 */
	TableList createTableList();
	/**
	 * @return TextArea
	 */
	TextArea createTextArea();
	/**
	 * @return TextField
	 */
	TextField createTextField();
	/**
	 * @return Timer
	 */
	Timer createTimer();
	/**
	 * @return Dialog
	 */
	Dialog createDialog();
	/**
	 * @return Console
	 */
	Console createConsole();
}
