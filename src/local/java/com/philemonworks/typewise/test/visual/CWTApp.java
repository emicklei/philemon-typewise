package com.philemonworks.typewise.test.visual;

import java.util.ArrayList;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.util.Color;

/**
 * @author E.M.Micklei
 *
 */
public class CWTApp extends ApplicationModel {
	private UIBuilder ui;
	private List colors;
	private Button updateColor;
	private RadioButton radio1;
	private RadioButton radio2;
	private TextField singleInput;
	private CheckBox checkbox;	
	private Choice picklist;
	private Image image;
	private TableList table;
	
	public static void main(String[] args){
		new CWTApp(null).mainScreen().preview();
	}
	
	public CWTApp(ApplicationModel myParent) {
		super(myParent);
	}
	
	public Screen mainScreen(){
		ui = new UIBuilder();
		Screen top = ui.addScreen("main",this,20,40);
		top.setBackground(Color.blue);
		this.colors();
		this.updateColor();
		this.radio1();
		this.radio2();
		this.checkbox();
		this.singleInput();
		this.picklist();
		this.image();
		this.table();
		ui = null; // clean up
		return top;
	}
	public List colors(){
		if (colors == null){
			colors = ui.addList("colors",10,20);
			colors.setBackground(Color.black);
			colors.setForeground(Color.white);
			colors.setBorderColor(Color.yellow);
			ArrayList items = new ArrayList();
			items.add("Red");
			items.add("Green");
			items.add("Blue");
			colors.setItems(items);
		}
		return colors;
	}
	public Button updateColor(){
		if (updateColor == null){
			ui.setPosition(1,21);
			updateColor = ui.addButton("update","Update");
			updateColor.setBackground(Color.yellow);
			updateColor.setBackground(Color.black);
		}
		return updateColor;
	}
	public RadioButton radio1(){
		if (radio1 == null){
			ui.setPosition(3,21);
			radio1 = ui.addRadioButton("radio1","Pick 1",14);
			radio1.setBackground(Color.green);
			radio1.setForeground(Color.black);
			radio1.setGroupName("radios");
		}
		return radio1;
	}	
	public RadioButton radio2(){
		if (radio2 == null){
			ui.setPosition(4,21);
			radio2 = ui.addRadioButton("radio2","Pick 2",14);
			radio2.setGroupName("radios");			
		}
		return radio2;
	}	
	public CheckBox checkbox(){
		if (checkbox == null){
			ui.setPosition(6,21);
			checkbox = ui.addCheckBox("checkbox","Check me", 14);	
		}
		return checkbox;
	}		
	public TextField singleInput(){
		if (singleInput == null){
			ui.setPosition(11,21);
			singleInput = ui.addTextField("singleinput",20);
			//singleInput.setBackground(Color.white);
			//singleInput.setBackground(Color.red);
			singleInput.setString("twenty  characters !");
		}
		return singleInput;		
	}
	public Choice picklist(){
		if (picklist == null){
			ui.setPosition(12,1);
			picklist = ui.addChoice("picklist",12,18);
			ArrayList options = new ArrayList();
			options.add("Monday");
			options.add("Tuesday");
			options.add("Wednesday");
			picklist.setItems(options);
		}
		return picklist;		
	}	
	public Image image(){
		if (image == null){
			ui.setPosition(13,1);
			image = ui.addImage("img",4,9);
			image.setUrl("http://www.typewise.org/images/icon_80.gif");
		}
		return image;		
	}		
	public TableList table(){
		if (table == null){
			ui.setPosition(13,21);
			table = ui.addTableList("table",7,20);	
			table.setBackground(Color.yellow);		
			
			TableColumn col1 = new TableColumn();
			col1.setWidth(5);
			col1.getHeading().setString("Name");
			TableColumn col2 = new TableColumn();
			col2.setWidth(15);
			col2.getHeading().setString("Profession");			
							
			table.addColumn(col1);
			table.addColumn(col2);			
			
			ArrayList rows = new ArrayList();
			rows.add(new TableListItem("Pieter","Agent"));
			table.setItems(rows);	
		}
		return table;
	}
}
