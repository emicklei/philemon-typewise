package com.philemonworks.typewise.test.visual;

import java.io.FileOutputStream;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.html.CSSBasedRenderer;
import com.philemonworks.util.Color;
import com.philemonworks.writer.HTMLWriter;

public class TestCSSRenderer  {
	CSSBasedRenderer r;

	public static void main(String[] args){
		TestCSSRenderer r = new TestCSSRenderer();
		try {
			r.setUp();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		r.testSimple();
	}
	
	public void setUp() throws Exception {
		FileOutputStream fos = new FileOutputStream("c:/temp/css.html");
		HTMLWriter w = new HTMLWriter(fos);
		r = new CSSBasedRenderer(w);
		r.includeEmptyListItem = true;
	}
	public void testSimple() {
		UIBuilder ui = new UIBuilder();
		Screen s = ui.addScreen("test", null, 40, 80);
		//
		RadioButton r1 = ui.addRadioButton("radio1", "Click me", 10);
		Label l1 = ui.addLabel("label1", "Hello World");
		l1.setBackground(Color.yellow);
		TextArea a1 = ui.addTextArea("text1", 2, 20);
		a1.setText("The Quick brown lazy fox jumps over the lazy dog's back. The Quick brown lazy fox jumps over the lazy dog's back.");
		a1.setForeground(Color.red);
		ui.cr(1);
		RadioButton r2 = ui.addRadioButton("radio2", "Press me", 10);
		Label l2 = ui.addLabel("label2", "Born to live");
		ui.cr(1);
		List list1 = ui.addList("list1", 4, 20);
		list1.addItem("Should be ListItem");
		ui.cr(1);
		TextField field1 = ui.addTextField("field1",20);
		field1.setText("Help");
		ui.cr(1);
		Image im1 = ui.addImage("im1",10,10);
		im1.setUrl("http://www.typewise.org/images/icon_final.gif");
		ui.cr(1);
		Button b1 = ui.addButton("button1","Click me");
		Choice c1 = ui.addChoice("choice1",10,10);
		c1.addItem("Monday");
		c1.addItem("Tuesday");
		ui.cr(1);
		TableList tl = ui.addTableList("table1",4,20);
		TableColumn tc1 = new TableColumn();
		tc1.setWidth(4);
		tc1.getHeading().setText("4444");
		tl.addColumn(tc1);
		
		TableColumn tc2 = new TableColumn();
		tc2.setWidth(16);
		tc2.getHeading().setText("sixteen");
		tl.addColumn(tc2);
		
		TableListItem i1 = new TableListItem("1");
		i1.add("abc");
		i1.add("efghijklmnop");
		tl.addItem(i1);
		
		
		r.handle(ui.getScreen());
	}
}
