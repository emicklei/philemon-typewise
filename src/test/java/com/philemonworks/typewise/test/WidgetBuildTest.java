/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 8-dec-2003 by emicklei
 *
 * */
package com.philemonworks.typewise.test;

/**
 * @author emicklei
 * 
 * Purpose:
 *  
 */
import java.util.ArrayList;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.Color;

public class WidgetBuildTest extends junit.framework.TestCase {
    private BinaryWidgetAccessor accessor;
    private boolean log = false;

    public WidgetBuildTest(String s) {
        super(s);
    }

    public void show(Object arg) {
        if (log)
            System.out.println(arg);
    }

    public void testComposite() {
        CompositeWidget compWidget = new CompositeWidget("tw10417", 1, 1, 40, 120);
        List listWidget = new List("tw10847", 12, 1, 20, 80);
        compWidget.add(listWidget);
    }

    public void testNameConflict1() {
        CompositeWidget compWidget = new CompositeWidget("root", 1, 1, 40, 120);
        List listWidget = new List("child1", 12, 1, 20, 80);
        List listWidget2 = new List("child1", 12, 1, 20, 80);
        compWidget.add(listWidget);
        boolean caught = false;
        try {
            compWidget.add(listWidget2);
        } catch (Throwable t) {
            caught = true;
        }
        assertTrue(caught);
    }

    public void testInput() {
        // topRow, leftColumn, rows, columns
        TextField newInput = new TextField("anInput", 2, 2, 2, 10);
        assertTrue(newInput.getRows() == 2);
        assertTrue(newInput.getColumns() == 10);
    }

    public void testInputWithAppearance() {
        // topRow, leftColumn, rows, columns
        TextField newInput = new TextField("anInput", 1, 1, 20, 10);
        newInput.getAppearance().setForeground(Color.yellow);
        assertTrue(newInput.getRows() == 20);
    }

    public void testPanel() {
        // topRow, leftColumn, rows, columns
        Panel newP = new Panel("mainwindow", 1, 1, 40, 80);
        assertTrue(newP.getColumns() == 80);
    }

    public void testCheckbox() {
        // topRow, leftColumn, rows, columns
        CheckBox newP = new CheckBox("checkkie", 1, 1, 1, 80);
        assertTrue(newP.getColumns() == 80);
    }

    public void testMessage() {
        MessageSend newMessage = new MessageSend("hello", "substring");
        newMessage.addArgument("1");
        newMessage.addArgument("5");
        show(newMessage);
    }

    public void testMessage2() {
        MessageSend newMessage = new MessageSend("hello", "substring");
        newMessage.addArgument(new TextField("input", 1, 2, 10, 20));
        show(newMessage);
    }

    public void testSequence() {
        MessageSequence sequence = new MessageSequence();
        sequence.add(new MessageSend("hello1", "size"));
        sequence.add(new MessageSend("hello2", "size"));
        show(sequence);
    }

    public void testCheckRoom() {
        Panel newWindow = new Panel("mainwindow", 1, 1, 40, 80);
        TextArea newText = new TextArea("text", 1, 1, 50, 80);
        boolean caught = false;
        try {
            newWindow.add(newText);
        } catch (RuntimeException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    public void testCheckRoom2() {
        Panel newWindow = new Panel("mainwindow", 1, 1, 40, 80);
        TextArea newText = new TextArea("text", 1, 1, 40, 80);
        boolean caught = false;
        try {
            newWindow.add(newText);
        } catch (RuntimeException ex) {
            caught = true;
        }
        assertTrue(!caught);
    }

    public void testMerge() {
        String[] sa = new String[] { "Hello", "World" };
    }

    public void testIllegalName() {
        boolean caught = false;
        try {
            TextArea newText = new TextArea("_text", 1, 1, 40, 80);
        } catch (RuntimeException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    public void testSetSize() {
        // topRow, leftColumn, rows, columns
        TextArea txt = new TextArea("noname", 1, 1, 1, 10);
        assertEquals(txt.getRows(), 1);
        // rows, columns
        txt.setSize(2, 20);
        assertEquals(txt.getRows(), 2);
        assertEquals(txt.getColumns(), 20);
    }

    public void testBounds() {
        // topRow, leftColumn, rows, columns
        Label ik = new Label("ik", 2, 3, 4, 5);
        assertTrue(ik.getBottomRow() == 5);
        assertTrue(ik.getRightColumn() == 7);
        assertTrue(ik.getRows() == 4);
        assertTrue(ik.getColumns() == 5);
        ik.setBounds(ik.getBounds());
        assertTrue(ik.getBottomRow() == 5);
        assertTrue(ik.getRightColumn() == 7);
        assertTrue(ik.getRows() == 4);
        assertTrue(ik.getColumns() == 5);
    }

    public void testComputeLines() {
        TextArea area = new TextArea("t", 1, 1, 4, 4);
        area.setString("1\n2\n3\n4");
        assertEquals(4, area.computeTextLines().size());
    }
    
    public void testList(){
    	List cwtList = new List("l",1,1,4,4);
    	ArrayList items = new ArrayList();
    	for (int i=0;i<10;i++){
    		ListItem item = new ListItem(String.valueOf(i),"item"+i);
    		items.add(item);
    	}    	
    	cwtList.setItems(items);
    	assertEquals(cwtList.getSelectionIndex(),-1);
    	cwtList.setSelectionIndex(9);
    	assertEquals(cwtList.getSelectionIndex(),9);
    	assertEquals(((ListItem)(cwtList.getSelectedItem())).getItem(),"item9");
    }
}