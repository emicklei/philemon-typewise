/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 5-aug-2004: created
 *
 */
package com.philemonworks.typewise.test;

import java.io.IOException;
import junit.framework.TestCase;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.StyleSheet;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 *
 */
public class AppearanceTest extends TestCase {

    public void testAppearance1(){
        Screen s = new Screen("top",null, 10,10);
        s.setBackground(Color.blue);
        MessageSequence seq = new MessageSequence();
        s.addStateMessagesTo(seq);
        System.out.println(seq);
    }
    public void testAppearance2(){
        Screen s = new Screen("top",null, 10,10);
        StyleSheet sheet = new StyleSheet();
        sheet.getOrCreate("screen").setBackground(Color.blue);
        s.setStyle("screen");
        s.setStyleSheet(sheet);
        MessageSequence seq = new MessageSequence();
        s.addStateMessagesTo(seq);
        System.out.println("one:" + seq);
        s.clearDirtyAll();
        seq = new MessageSequence();
        s.addStateMessagesTo(seq);
        System.out.println("empty:" + seq);
    }
	public void testReadWrite() throws BinaryObjectReadException , IOException {
		
		WidgetAppearance wa = new WidgetAppearance();
		wa.setBackground(Color.blue);
		wa.setBorderColor(Color.green);
		wa.setDisabledForeground(Color.gray);
		wa.setFocusColor(Color.yellow);
		wa.setForeground(Color.red);
		wa.setSelectionBackground(Color.black);
		wa.setSelectionForeground(Color.white);
		BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
		writer.write(wa);
		BinaryWidgetAccessor reader = new BinaryWidgetAccessor(writer.toByteArray());
		WidgetAppearance copy = (WidgetAppearance)reader.readNext();
		assertEquals(copy.getBackground(), Color.blue);
		assertEquals(copy.getBorderColor(), Color.green);
		assertEquals(copy.getDisabledForeground(), Color.gray);
		assertEquals(copy.getFocusColor(), Color.yellow);
		assertEquals(copy.getForeground(), Color.red);
		assertEquals(copy.getSelectionBackground(), Color.black);
		assertEquals(copy.getSelectionForeground(), Color.white);		
			
	}	
}
