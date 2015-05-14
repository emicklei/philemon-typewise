package com.philemonworks.typewise.test.visual;

/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * */
import java.util.ArrayList;
import java.util.Date;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.CWT;
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
import com.philemonworks.typewise.cwt.MenuItem;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.util.Color;

/**
 * @author emicklei Purpose: This class demonstrates all widgets available in a single Terminal window
 */
public class AllWidgetsSample extends ApplicationModel {	
	public AllWidgetsSample(ApplicationModel myParent) {
		super(myParent);
	}
	private static final long serialVersionUID = 3546919190859822392L;

	
	
	public static void main(String[] args) {
		// new AllWidgetsSample().mainScreen().preview();
		new SwtApplicationView().start(new AllWidgetsSample(null), "main");
	}
	public Screen mainScreen() {
		Screen window = new Screen("top", this, 40, 60);
		window.setTitle("TypeWise - AllWidgetsSample");
		addLabelTo(window);
		addCheckboxTo(window);
		addButtonTo(window);
		addListTo(window);
		addDropDownListTo(window);
		addTableListWithColumns(window);
		addAlignLabelsTo(window);
		addMenuBarTo(window);
		addRadioGroupTo(window);
		addTextAreaTo(window);
		addImageTo(window);
		return window;
	}
	public void afterShow() {
		view.setStatus("Status test");
	}
	public void addLabelTo(Panel aPanel) {
		Label newLabel = new Label("label1", 26, 2, 1, 12);
		newLabel.setString("Read me:");
		newLabel.getAppearance().setBackground(Color.blue);
		newLabel.getAppearance().setForeground(Color.yellow);
		aPanel.add(newLabel);
	}
	public void addAlignLabelsTo(Panel aPanel) {
		Label newLabel = new Label("label2", 21, 2, 1, 12);
		newLabel.setAlignment(Label.CENTER);
		newLabel.setString("No align; too long because it's too long");
		newLabel.getAppearance().setBackground(Color.white);
		newLabel.getAppearance().setForeground(Color.black);
		aPanel.add(newLabel);
		Label newLabel2 = new Label("label3", 22, 2, 1, 12);
		newLabel2.setAlignment(Label.LEFT);
		newLabel2.setString("Left");
		newLabel2.getAppearance().setBackground(Color.lightGray);
		aPanel.add(newLabel2);
		Label newLabel3 = new Label("label4", 23, 2, 1, 12);
		newLabel3.setAlignment(Label.CENTER);
		newLabel3.setString("CenteR");
		newLabel3.getAppearance().setBackground(Color.gray);
		aPanel.add(newLabel3);
		Label newLabel4 = new Label("label5", 24, 2, 1, 12);
		newLabel4.setAlignment(Label.RIGHT);
		newLabel4.setString("righT");
		newLabel4.getAppearance().setBackground(Color.darkGray);
		aPanel.add(newLabel4);
	}
	public void addCheckboxTo(Panel aPanel) {
		CheckBox newBox = new CheckBox("box1", 27, 2, 1, 12);
		newBox.setString("Check me:");
		newBox.getAppearance().setBackground(Color.pink);
		newBox.getAppearance().setForeground(Color.black);
		newBox.setSelection(true);
		newBox.setTickAlignment(Label.RIGHT);
		newBox.setAlignment(Label.CENTER);
		newBox.onSendTo(EVENT_CLICKED, "checkBoxClicked", this);
		aPanel.add(newBox);
	}
	public void addButtonTo(Panel aPanel) {
		Button newButton = new Button("button1", 28, 2, 1, 12);
		newButton.setString("Press Me");
		newButton.getAppearance().setBackground(Color.gray);
		newButton.getAppearance().setForeground(Color.yellow);
		aPanel.add(newButton);
		newButton.onSendTo(EVENT_CLICKED, "widgetClicked", this);
	}
	public void addListTo(Panel aPanel) {
		List newList = new List("list1", 6, 2, 7, 12);
		ArrayList items = new ArrayList();
		items.add(new ListItem("1", "------------"));
		items.add(new ListItem("2", "Nederland"));
		items.add(new ListItem("3", "Duitsland"));
		items.add(new ListItem("4", "Belgie"));
		items.add(new ListItem("5", "Engeland"));
		items.add(new ListItem("6", "------------"));
		newList.setItems(items);
		newList.getAppearance().setBackground(Color.white);
		newList.getAppearance().setForeground(Color.black);
		aPanel.add(newList);
	}
	public void addDropDownListTo(Panel aPanel) {
		Choice newList = new Choice("dropdownlist", 19, 2, 1, 12);
		ArrayList items = new ArrayList();
		items.add(new ListItem("1", "Drop me"));
		items.add(new ListItem("2", "Drop or him"));
		newList.setItems(items);
		newList.getAppearance().setBackground(Color.yellow);
		newList.getAppearance().setForeground(Color.black);
		aPanel.add(newList);
	}
	public void addTableListWithColumns(Panel aPanel) {
		Label header1 = new Label("xxx", 1, 1, 1, 1);
		header1.getAppearance().setBackground(Color.green);
		header1.setString("Header1");
		TableColumn column1 = new TableColumn();
		column1.setHeading(header1);
		column1.setWidth(8);
		column1.getAppearance().setForeground(Color.yellow);
		column1.getAppearance().setBackground(Color.red);
		Label header2 = new Label("xxx", 1, 1, 1, 1);
		header2.setString("Header2");
		TableColumn column2 = new TableColumn();
		column2.setHeading(header2);
		column2.setWidth(8);
		TableList list = new TableList("testTableList", 2, 16, 15, 16);
		list.addColumn(column1);
		list.addColumn(column2);
		list.getAppearance().setBackground(Color.darkGray);
		list.getAppearance().setForeground(Color.green);
		ArrayList items = new ArrayList();
		items.add(new TableListItem("1", "Hello", "World"));
		items.add(new TableListItem("2", "Hello2", "World2"));
		items.add(new TableListItem("3", "Hello3", "World3"));
		items.add(new TableListItem("4", "Hello4", "World4"));		
		list.setItems(items);
		aPanel.add(list);
		list.setMultiSelect(true);
		list.onSendTo(CWT.EVENT_SELECTEDITEMS,"tableSelectionChanged",this);
	}
	public void tableSelectionChanged(Object what){
		System.out.println("tableSelection:"+what);
	}
	public void addMenuBarTo(Screen aPanel) {
		MenuBar menuBar = new MenuBar("MenuBar1", 1, 1, 1, aPanel.getColumns());
		Menu fileMenu = new Menu("fileMenu");
		fileMenu.add(new MenuItem("Open", null));
		fileMenu.add(new MenuItem("Save", null));
		fileMenu.add(new MenuItem("Close", null));
		menuBar.addKeyMenu(" File ", 'F', fileMenu);
		Menu editMenu = new Menu("editMenu");
		editMenu.add(new MenuItem("Copy", null));
		editMenu.add(new MenuItem("Paste", null));
		menuBar.addKeyMenu(" Edit ", 'E', editMenu);
		Menu helpMenu = new Menu("helpMenu");
		helpMenu.add(new MenuItem("About", null));
		helpMenu.add(new MenuItem("Help", null));
		menuBar.addKeyMenu(" Help ", 'H', helpMenu);
		menuBar.setSelectedItem(fileMenu);
		aPanel.setMenuBarOrNull(menuBar);
	}
	public void addRadioGroupTo(Panel aPanel) {
		GroupBox radioButtons = new GroupBox("GroupName1", 18, 16, 6, 16);
		radioButtons.setBackground(Color.blue);
		radioButtons.setForeground(Color.white);
		RadioButton rb = new RadioButton("Option1", 1, 1, 1, 14);
		rb.setGroupName("GroupName1");
		rb.setString("Option 1");
		rb.setItem("radio1");
		rb.onSendTo(EVENT_SELECTEDITEM, "radioClicked", this);
		radioButtons.add(rb);
		RadioButton rb2 = new RadioButton("Option2", 2, 1, 1, 14);
		rb2.setGroupName("GroupName1");
		rb2.setString("Option 2");
		rb2.setItem("radio2");
		rb2.setSelection(true);
		rb2.onSendTo(EVENT_SELECTEDITEM, "radioClicked", this);
		radioButtons.add(rb2);
		RadioButton rb3 = new RadioButton("Option3", 3, 1, 1, 14);
		rb3.setGroupName("GroupName1");
		rb3.setString("Option 3");
		rb3.setItem("radio3");
		rb3.setSelection(false);
		rb3.onSendTo(EVENT_SELECTEDITEM, "radioClicked", this);
		radioButtons.add(rb3);
		radioButtons.getAppearance().setBorderColor(Color.blue);
		aPanel.add(radioButtons);
	}
	public void addTextAreaTo(Panel aPanel) {
		TextArea area = new TextArea("TextArea", 14, 2, 4, 12);
		area.getAppearance().setBackground(Color.orange);
		area.getAppearance().setForeground(Color.green);
		area.setString("This\nis\na\ntest");
		aPanel.add(area);
	}
	public void addImageTo(Panel aPanel) {
		Image img = new Image("Philemon", 26, 16, 3, 16);
		img.setUrl("http://www.philemonworks.com/images/logo_small.jpg");
		aPanel.add(img);
	}
	public void widgetClicked() {
		System.out.println("Clicked:" + new Date());
		view.show(this.mainScreen());
	}
	public void checkBoxClicked(Boolean now) {
		Logger.getLogger(this.getClass()).info("Clicked:" + new Date() + " now:" + now);
	}
	public void checkBoxClicked() {
		Logger.getLogger(this.getClass()).info("Clicked:" + new Date());
	}
	public void radioClicked(Object which) {
		Logger.getLogger(this.getClass()).info("Radio clicked:" + which);
	}
}
