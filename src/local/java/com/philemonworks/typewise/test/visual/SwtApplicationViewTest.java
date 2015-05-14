package com.philemonworks.typewise.test.visual;

import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Menu;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.cwt.MenuItem;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidgetFactory;
import com.philemonworks.util.Color;

public class SwtApplicationViewTest extends TestCase implements MessageConstants {
	public void testNothing(){}
	public static void main(String[] args){
		if (!System.getProperty("os.arch").equals("x86")) return;
		Screen cwtScreen = new Screen("main",new CWTApp(null),40,30);
		cwtScreen.setTitle("testButton");
				
		Button cwtButton = new Button("button",2,2,1,14);
		cwtButton.setBackground(Color.gray);
		cwtButton.setForeground(Color.yellow);
		cwtButton.setText("Hello World");
		cwtButton.onSendToWith(CWT.EVENT_CLICKED,"setTitle",cwtScreen,"Clicked!");
		cwtScreen.add(cwtButton);
		
		TextField field = new TextField("textfield",3,2,1,14);
		field.setBackground(Color.white);
		field.setForeground(Color.blue);
		field.setText("Help");
		field.onSendToWith(CWT.EVENT_CLICKED,"setTitle",cwtScreen,"Entered!");
		cwtScreen.add(field);
		
		CheckBox box = new CheckBox("check",4,2,1,14);
		box.setAlignment(CWT.RIGHT);
		box.setText("Click me");
		box.setTickAlignment(CWT.LEFT);
		box.onSendToWith(CWT.EVENT_CLICKED,"setTitle",cwtScreen,"Checked!");
		cwtScreen.add(box);
		
		Choice drop = new Choice("choice",5,2,1,14);
		drop.addItem("Hello");
		drop.addItem("World");
		drop.setBackground(Color.yellow);
		drop.setForeground(Color.black);		
		drop.onSendTo(CWT.EVENT_SELECTEDITEM,"setTitle",cwtScreen);
		cwtScreen.add(drop);
		
		Image img = new Image("image",6,2,10,18);
		img.setUrl("http://www.philemonworks.com/images/logo_small.jpg");
		img.onSendToWith(CWT.EVENT_CLICKED,"setTitle",cwtScreen,"Seen!");
		cwtScreen.add(img);
		
		Label lab = new Label("label",17,2,1,14);
		lab.setText("GSM:");
		cwtScreen.add(lab);
		
		List lst = new List("list",18,2,4,14);
		lst.addItem("Good");
		lst.addItem("Morning");
		lst.addItem("Amersfoort");
		lst.addItem("The Netherlands");
		lst.onSendTo(CWT.EVENT_SELECTEDITEM,"setTitle",cwtScreen);
		cwtScreen.add(lst);
		
		TextArea area = new TextArea("textarea",22,2,4,14);
		area.setBackground(Color.white);
		area.setForeground(Color.blue);
		area.setText("Help\nme\nto\nget\nfamous");
		cwtScreen.add(area);		
		
		TableList table = new TableList("tablelist",27,2,6,24);
		table.setBackground(Color.lightGray);
		TableColumn tc1 = new TableColumn();
		tc1.getHeading().setText("Left");
		tc1.setWidth(4);
		table.addColumn(tc1);
		TableColumn tc2 = new TableColumn();
		tc2.getHeading().setText("Right");
		tc2.setWidth(10);
		table.addColumn(tc2);
		area.setBackground(Color.gray);
		java.util.List its = new ArrayList();
		its.add(new TableListItem("0","Zero","Nul"));
		its.add(new TableListItem("1","One","Een"));
		its.add(new TableListItem("2","Two","Twee"));
		its.add(new TableListItem("3","Three","Drie"));
		table.setItems(its);
		cwtScreen.add(table);
		
		RadioButton ba = new RadioButton("radioA",34,2,1,14);
		ba.setText("Pick A");
		ba.setItem("radioA");
		ba.setGroupName("group");
		ba.onSendTo(CWT.EVENT_SELECTEDITEM,"setTitle",cwtScreen);
		cwtScreen.add(ba);
		RadioButton bb = new RadioButton("radioB",35,2,1,14);
		bb.setItem("radioB");
		bb.setText("Pick B");
		bb.setGroupName("group");
		bb.onSendTo(CWT.EVENT_SELECTEDITEM,"setTitle",cwtScreen);
		cwtScreen.add(bb);		
		
		Timer tim = new Timer("timer",36,2,1,14);
		tim.setDelayInSeconds(1);
		tim.setInterval(0,10);
		tim.onSendTo(CWT.EVENT_COUNTER_END,"setTitle",cwtScreen);
		cwtScreen.add(tim);
		
		MenuBar bar = new MenuBar("bar",1,1,1,1);
		Menu fileMenu = new Menu("File");
		fileMenu.add(new MenuItem("New",MessageConstants.CLOSE));
		fileMenu.add(new MenuItem("Open...",MessageConstants.CLOSE));
		fileMenu.add(new MenuItem("Save",MessageConstants.CLOSE));
		bar.addKeyMenu("File",'f',fileMenu);
		cwtScreen.setMenuBarOrNull(bar);
		
		// make show message
		IndexedMessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_SHOW);
        msg.addArgument(cwtScreen);
		MessageSequence seq = msg.asSequence();
		cwtScreen.addStateMessagesTo(seq);
		cwtScreen.addEventConnectionsTo(seq);
		System.out.println(seq);
		// serialize using server widget factory
		BinaryWidgetAccessor writer;
		byte[] bits = null;
		try {
			writer = new BinaryWidgetAccessor();
			writer.write(seq);
			bits = writer.toByteArray();
		} catch (BinaryObjectReadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BinaryWidgetAccessor reader;
		Message req = null;
		try {
			reader = new BinaryWidgetAccessor(bits);
			reader.setFactory(new SwtWidgetFactory());
			req = (Message)reader.readNext();
		} catch (BinaryObjectReadException e) {
			e.printStackTrace();
		} 
		SwtApplicationView view = new SwtApplicationView();
		view.getMessageDispatcher().dispatch(req);		
		view.open();
		SwtUtils.flushColors();
	}
}
