/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 17-jul-2004: created
 *
 */
package com.philemonworks.typewise.test;

import java.util.ArrayList;
import junit.framework.TestCase;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 */
public class DirtyWidgetTest extends TestCase implements CWT {
    
    public void log(Object anObject){
        if (true) System.out.println(getName()+"=" + anObject);
    }
    
	public void testDirtyList(){
		List widget = new List("list",1,1,10,10);
		ArrayList ab = new ArrayList();
		ab.add("a"); ab.add("b");
		widget.setItems(ab);
		assertTrue(widget.isDirty(WidgetAccessorConstants.SET_ITEMS));
		widget.setSelectedItem("a");
		assertTrue(widget.isDirty(WidgetAccessorConstants.SET_ITEMS));
		assertTrue(widget.isDirty(WidgetAccessorConstants.SET_SELECTIONINDEX));
		MessageSequence seq = new MessageSequence();
		widget.addStateMessagesTo(seq);
		log(seq);
		assertEquals(2,seq.getMessages().size());
	}
	public void testDirtyLabel(){
	    Screen top = new Screen("top",new SampleTerminalApp(null),100,100);
		Label widget = new Label("label",1,1,10,10);
		top.add(widget); // widget needs parent for color changes to appear as dirty message
		top.getModel().view.show(top); // client needs screen for color changes to be collected
		widget.setForeground(Color.red);
        widget.setBackground(Color.black);
		widget.setText("Hello");
		widget.setAlignment(CWT.RIGHT);

		
		MessageSequence seq = new MessageSequence();
		widget.addStateMessagesTo(seq);
		log(seq);
		assertEquals(3,seq.getMessages().size());
	}	
	public void testDirtyGroupBox(){
	    GroupBox widget = new GroupBox("box",1,1,10,10);
		widget.setLabel("Group");
		widget.setAlignment(CWT.RIGHT);
		
		MessageSequence seq = new MessageSequence();
		widget.addStateMessagesTo(seq);
		log(seq);
		assertEquals(2,seq.getMessages().size());
	}		
}
