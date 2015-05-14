package com.philemonworks.typewise.test.visual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.UIBuilder;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.html.PageWriter;
import com.philemonworks.util.Color;

public class TestTableInHTML extends ApplicationModel {
	
	public TestTableInHTML(ApplicationModel myParent) {
		super(myParent);
	}
	
	public static void main(String[] args) throws Throwable {
		Logger.getLogger(TestTableInHTML.class).info("main");
		PrintStream hw = new PrintStream(new FileOutputStream(new File("c:/temp/TestTableInHTML.html")));
		PageWriter pw = new PageWriter();
		UIBuilder ui = new UIBuilder();
		ui.addScreen("TestTableInHTML", new TestTableInHTML(null), 60, 80);
		ui.setPosition(4, 4);
		
		TableList table = ui.addTableList("table1", 20, 30);
		table.setBackground(Color.darkGray);
		int maxc = 10;
		for (int j = 0; j < maxc; j++) {
			TableColumn tc1 = new TableColumn();
			tc1.setWidth(8);
			tc1.getHeading().setText("Column " + j);
			if (j % 2 == 0)
				tc1.setBackground(Color.white);
			table.add(tc1);
		}
		List items = new ArrayList();
		for (int i = 0; i < 4; i++) {
			TableListItem item = new TableListItem((String.valueOf(i)));
			for (int j = 0; j < maxc; j++) {
				item.add("cell entry." + j + "." + i);
			}
			items.add(item);
		}
		table.setItems(items);
		
		
		TableList table2 = ui.addTableList("table2", 20, 30);
		table2.setBackground(Color.blue);
		maxc = 10;
		for (int j = 0; j < maxc; j++) {
			TableColumn tc2 = new TableColumn();
			tc2.setWidth(8);
			tc2.getHeading().setText("Column " + j);
			if (j % 2 == 0)
				tc2.setBackground(Color.white);
			table2.add(tc2);
		}
		items = new ArrayList();
		for (int i = 0; i < 4; i++) {
			TableListItem item = new TableListItem((String.valueOf(i)));
			for (int j = 0; j < maxc; j++) {
				item.add("cell entry." + j + "." + i);
			}
			items.add(item);
		}
		table2.setItems(items);
		
		
		pw.writeOn(ui.getScreen(), null, hw, "post");
	}
}
