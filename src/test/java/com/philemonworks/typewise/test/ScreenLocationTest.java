package com.philemonworks.typewise.test;

import com.philemonworks.typewise.Location;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.Screen;
import junit.framework.TestCase;

public class ScreenLocationTest extends TestCase {
	
	public void testScreen(){
		Screen s = new Screen("test",null,40,30);
		Location loc = s.screenLocation();
		assertEquals("screen row",loc.row,1);
		assertEquals("screen column",loc.column,1);
	}
	public void testButton(){
		Screen s = new Screen("test",null,40,30);
		Button b = new Button("button",1,2,3,4);
		s.add(b);
		Location loc = b.screenLocation();
		assertEquals("button row",loc.row,1);
		assertEquals("button column",loc.column,2);
	}	
	public void testLabelInPanel(){
		Screen s = new Screen("test",null,40,30);
		Panel p = new Panel("panel",3,4,10,20);
		Label l = new Label("label",2,3,1,4);
		p.add(l);
		s.add(p);
		Location loc = l.screenLocation();
		assertEquals("label in panel row",4,loc.row);
		assertEquals("label in panel column",6,loc.column);
	}	
}
