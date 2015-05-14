package com.philemonworks.typewise.test.visual;

import java.io.IOException;
import junit.framework.TestCase;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidgetFactory;
import com.philemonworks.util.Color;

public class SwtCornerViewTest  extends TestCase implements MessageConstants {
	public void testNothing(){}
	public static void main(String[] args){
		if (!System.getProperty("os.arch").equals("x86")) return;
		Screen cwtScreen = new Screen("main",null,20,30);
		cwtScreen.setTitle("testCorners");
		cwtScreen.setBackground(Color.gray);
		Label lab = new Label("label",1,1,1,1);
		lab.setText("X");
		lab.setBackground(Color.blue);
		cwtScreen.add(lab);

		Label lab2 = new Label("lab2",1,30,1,1);
		lab2.setText("X");
		lab2.setBackground(Color.blue);
		cwtScreen.add(lab2);
		
		Label lab3 = new Label("lab3",20,1,1,1);
		lab3.setText("X");
		lab3.setBackground(Color.blue);
		cwtScreen.add(lab3);
		
		Label lab4 = new Label("lab4",20,30,1,1);
		lab4.setText("X");
		lab4.setBackground(Color.blue);
		cwtScreen.add(lab4);

		Label lab5 = new Label("lab5",1,2,1,1);
		lab5.setText("X");
		lab5.setBackground(Color.green);
		cwtScreen.add(lab5);		

		Label lab6 = new Label("lab6",2,2,1,1);
		lab6.setText("X");
		lab6.setBackground(Color.black);
		cwtScreen.add(lab6);		
				
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
