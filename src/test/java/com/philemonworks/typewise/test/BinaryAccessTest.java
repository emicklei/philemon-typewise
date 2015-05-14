/*
 * Created on 1-feb-2004
 *
 * (c)2001-2004, PhilemonWorks. All rights reserved.
 * 
 * 9-2-2004: mvhulsentop: added some readwrite tests
 * 10-2-2004: mvhulsentop: added tests for tablecolumn and tablelist 
 * 10-2-2004: mvhulsentop: menu tests
 * 11-2-2004: mvhulsentop: image tests.
 * 11-2-2004: mvhulsentop: displayitem test.
 * 11-2-2004: mvhulsentop: displaytableitem test.
 * 13-2-2004: mvhulsentop: excluded tests that depend on cat. 2 state. 
 * 									see @ http://ntserver:8000/Terminal/57
 * 07-04-2004: emicklei: included excluded tests after fix

 * */
package com.philemonworks.typewise.test;

import java.io.IOException;
import java.util.ArrayList;
import junit.framework.TestCase;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Menu;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.cwt.MenuItem;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageFactory;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 *
 * Purpose:
 *
 */
public class BinaryAccessTest  extends TestCase {
	
	boolean log = true;  // this controls whether bytes are written to the console

	public BinaryAccessTest(String arg0) {
		super(arg0);
	}
	private void outputByteArrayForMethodNamed(byte[] toShow, String methodName) {
		if (!log) return;
		System.out.print(methodName+": ");
		System.out.print("\t");
		for(int place=0; place < toShow.length; place++) {
			System.out.print((new Byte(toShow[place])).toString()+" ");
		}
		System.out.println("");
		
		System.out.print(methodName+": ");
		System.out.print("\t");
		for(int place=0; place < toShow.length; place++) {
			if ((toShow[place] > 32) & (toShow[place] < 128)) {
				System.out.print((char) toShow[place]);
			} else {
				System.out.print("_");
			}
			int spaces = (new Byte(toShow[place])).toString().length();
			for (int tmp = 0; tmp < spaces; tmp++) {
				System.out.print(" ");
			}
		}
		System.out.println("");

	}
	
	public void testMessageSeq() throws BinaryObjectReadException , IOException {
		
		MessageSequence seq = new MessageSequence();
		seq.add(new MessageSend("me", "doSomething"));
		seq.add(new MessageSend("you", "doSomethingElse"));
		seq.add(new IndexedMessageSend("him",(byte)1));		
	
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(seq);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testMessageSeq");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		MessageSequence copy = (MessageSequence)reader.readNext();
		assertEquals(seq.getMessages().size(),copy.getMessages().size());
		assertEquals(((MessageSend) seq.getMessages().get(1)).getOperation(),((MessageSend)copy.getMessages().get(1)).getOperation());		
	}
	public void testTextField() throws BinaryObjectReadException , IOException {
		
		TextField widget = new TextField("test",1,2,3,4);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TextField copy = (TextField)reader.readNext();
		assertEquals("test",copy.getName());
		assertEquals(3,copy.getRows());
		assertEquals(4,copy.getColumns());		
	}
	public void testTableColumn() throws BinaryObjectReadException , IOException {
		
		TableColumn column = new TableColumn();
		Label header = new Label("xxx",1,1,1,1);
		header.setString("Hello");
		column.setHeading(header);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(column);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testTableColumn");
		TableColumn copy = (TableColumn)reader.readNext();
	}
	
	public void testTableList() throws BinaryObjectReadException, IOException {
		Label header1 = new Label("xxx",1,1,1,1);
		TableColumn column1 = new TableColumn();
		column1.setHeading(header1);
		Label header2 = new Label("xxx",1,1,1,1);
		TableColumn column2 = new TableColumn();
		column2.setHeading(header2);

		TableList list = new TableList("testTableList", 1, 1, 25, 25);
		list.addColumn(column1);
		list.addColumn(column2);
		list.getItems().add(new String[]{"Hello", "World"});
		list.getItems().add(new String[]{"Hello2", "World2"});
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(list);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testTableList");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TableList copy = (TableList)reader.readNext();
	}

	public void testTextArea() throws BinaryObjectReadException , IOException {
		
		TextField widget = new TextField("test",1,2,3,4);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TextField copy = (TextField)reader.readNext();
		assertEquals("test",copy.getName());
		assertEquals(3,copy.getRows());
		assertEquals(4,copy.getColumns());		
	}
	public void testImage() throws BinaryObjectReadException , IOException {
		
		Image widget = new Image("test",1,2,3,4);
		widget.setUrl("http://localhost/ikke.jpg");
		widget.setScaleToFit(false);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Image copy = (Image)reader.readNext();
		assertEquals("test",copy.getName());	
	}
	public void testScreen() throws BinaryObjectReadException , IOException {
		
		Screen widget = new Screen("top",null,3,4);
		widget.setTitle("MyScreen");
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testScreen");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Screen copy = (Screen)reader.readNext();
		assertEquals(widget.getAppearance().getBackground(), copy.getAppearance().getBackground());
	}	
	public void testScreen2() throws BinaryObjectReadException , IOException {
		
		Screen widget = new Screen("top",null,16,18);
		widget.setTitle("MyScreen");
		TextField inputWidget = new TextField("inputTest", 1, 4, 5, 7);
		inputWidget.setString("Hello world");
		widget.add(inputWidget);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testScreen2");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Screen copy = (Screen)reader.readNext();
		assertEquals(inputWidget.getClass(), ((Widget) copy.getWidgets().get(0)).getClass());
	}	
	public void testScreen3() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Screen widget = new Screen("top",null,22,22);
		widget.setTitle("MyScreen");
		TextField inputWidget = new TextField("inputTest", 1, 4, 5, 7);
		Label labelWidget = new Label("Hallo wereld", 2, 2, 6, 6);
		TextArea textArea = new TextArea("Hello Texas", 2, 5, 5, 9);  
		inputWidget.setString("Hello world");
		labelWidget.setString("Hallo wereld label");
		textArea.setString("Hello Texas");
		widget.add(inputWidget);
		widget.add(labelWidget);
		widget.add(textArea);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		BinaryWidgetAccessor statewriter = new BinaryWidgetAccessor();
		writer.write(widget);
		MessageSequence stateMsgs = new MessageSequence();
		widget.addStateMessagesTo(stateMsgs);
		statewriter.write(stateMsgs);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testScreenl3");
		this.outputByteArrayForMethodNamed(statewriter.toByteArray(), "testScreen3 state");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Screen copy = (Screen)reader.readNext();
		assertEquals(inputWidget.getClass(), ((Widget) copy.getWidgets().get(0)).getClass());
		assertEquals(Class.forName("com.philemonworks.typewise.cwt.Label"), ((Widget) copy.getWidgets().get(1)).getClass());
		assertEquals(Class.forName("com.philemonworks.typewise.cwt.TextArea"), ((Widget) copy.getWidgets().get(2)).getClass());
	}	
	public void testReadWriteLabel() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Label widget = new Label("test",1,2,10,10);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		widget.setString("Label widget test");
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteLabel");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Label copy = (Label)reader.readNext();
		assertEquals(widget.getName(), copy.getName());
		assertEquals(widget.getClass(), copy.getClass());
	}	
	public void testReadWriteButton() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Button widget = new Button("test",1,2,10,10);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteButton");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Button copy = (Button)reader.readNext();
		assertEquals(widget.getName(), copy.getName());
		assertEquals(widget.getBounds(), copy.getBounds());
		assertEquals(widget.getClass(), copy.getClass());
	}	
	public void testReadWriteMenu() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Menu widget = new Menu("test");
		widget.add(new MenuItem("Hello", new MessageSend()));
		widget.add(new MenuItem("Hi", new MessageSend()));
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteMenu");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Menu copy = (Menu)reader.readNext();
		assertEquals(widget.getName(), copy.getName());
		assertEquals(widget.getClass(), copy.getClass());
	}	
	
	public void testReadWriteMenuBar() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		MenuBar bar = new MenuBar("xxx",1,1,1,20);
		bar.addKeyMenu("x", 'c', new Menu("test"));
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(bar);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteMenuBar");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		MenuBar copy = (MenuBar)reader.readNext();
		assertEquals(bar.getName(), copy.getName());
		assertEquals(bar.getBounds(), copy.getBounds());
		assertEquals(bar.getClass(), copy.getClass());
		assertEquals(bar.getLabels().get(0), copy.getLabels().get(0));
	}

	public void testReadWriteList() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		List widget = new List("test",1,2,10,10);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteList");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		List copy = (List)reader.readNext();
		assertEquals(widget.getName(), copy.getName());
		assertEquals(widget.getBounds(), copy.getBounds());
		assertEquals(widget.getClass(), copy.getClass());
	} 
	
	public void testReadWriteTableList() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		TableList widget = new TableList("test",1,2,10,10);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteTableList");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TableList copy = (TableList)reader.readNext();
		assertEquals(widget.getName(), copy.getName());
		assertEquals(widget.getBounds(), copy.getBounds());
		assertEquals(widget.getClass(), copy.getClass());
	}


	public void testText() throws BinaryObjectReadException , IOException {

		TextArea widget = new TextArea("test",1,4,6,8);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testText");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TextArea copy = (TextArea)reader.readNext();
	}	
	public void testList() throws BinaryObjectReadException , IOException {
		ArrayList listLines = new ArrayList();
		listLines.add("Hello world");
		listLines.add("Hallo wereld");
		listLines.add("Gutentag werld");
		listLines.add("Amai, goeiendag wereld");
		listLines.add("Hi, texas");
		List widget = new List("test",1,4,6,8);
		widget.setItems(listLines);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testList");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		List copy = (List)reader.readNext();
	}
	public void testMessage() throws BinaryObjectReadException , IOException {
		
		MessageSend msg = new MessageSend("myField", "string");
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(msg);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		MessageSend copy = (MessageSend)reader.readNext();
		assertEquals("myField",msg.getReceiver());
		assertEquals("string",msg.getOperation());		
	}	
	public void testIndexedMessage() throws BinaryObjectReadException , IOException {
		
		IndexedMessageSend msg = new IndexedMessageSend("myField", (byte)1);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(msg);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		MessageSend copy = (MessageSend)reader.readNext();
		assertEquals("myField",msg.getReceiver());
		assertEquals(1,msg.getIndex());		
	}		
	public void testSequence() throws BinaryObjectReadException , IOException {
		
		MessageSequence seq = new MessageSequence();
		MessageSend msg = new MessageSend("myField", "string");
		seq.add(msg);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(seq);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		MessageSequence copy = (MessageSequence)reader.readNext();
		assertTrue(copy.getMessages().size()==1);
	}	
	public void testReadWriteImage() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Image widget = new Image("http://testimage.jpg",1,1,20,20);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteImage");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Image copy = (Image)reader.readNext();
		assertEquals(widget.getUrl(), copy.getUrl());
		assertEquals(widget.getClass(), copy.getClass());
	}	
	public void testReadWriteDisplayItem() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		ListItem widget = new ListItem("test-id","xxx");
		widget.setItem("String in item");
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteDisplayItem");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		ListItem copy = (ListItem)reader.readNext();
		assertEquals(widget.getItem(), copy.getItem());
		assertEquals(widget.getId(), copy.getId());
		assertEquals(widget.getClass(), copy.getClass());
	}
	public void testReadWriteListWithDisplayItems() throws BinaryObjectReadException , IOException, ClassNotFoundException {

		ArrayList landen = new ArrayList();
		landen.add(new ListItem("NL","Nederland"));
		landen.add(new ListItem("BE","Belgie"));
		landen.add(new ListItem("DE","Duitsland"));

		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(landen);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteListWithDisplayItems");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		ArrayList copy = (ArrayList)reader.readNext();
		assertEquals("BE" , ((ListItem) copy.get(1)).getId());
		assertEquals("Belgie" , ((ListItem) copy.get(1)).getItem());
		assertEquals(landen.size(),copy.size());
	}
	public void testReadWriteDisplayTableItem() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		TableListItem widget = new TableListItem("test-id");
		widget.add("item1");
		widget.add("item2");
		widget.add("item3");
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(widget);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteDisplayTableItem");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		TableListItem copy = (TableListItem)reader.readNext();
		assertEquals(widget.getItems()[0], copy.getItems()[0]);
		assertEquals(widget.getClass(), copy.getClass());
	}	
	public void testReadWriteDialog() throws BinaryObjectReadException , IOException, ClassNotFoundException {
		
		Dialog d = new Dialog();
		d.setText("Hello");
		d.setYesMessage(MessageFactory.CLOSE);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(d);
		this.outputByteArrayForMethodNamed(writer.toByteArray(), "testReadWriteDialog");
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		Dialog copy = (Dialog)reader.readNext();
		assertEquals(d.getType(), copy.getType());
		assertEquals(d.getText(), copy.getText());
		assertEquals(d.getYesMessage().getClass(), copy.getYesMessage().getClass());
	}		
}
