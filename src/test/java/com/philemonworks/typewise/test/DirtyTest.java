/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 9-jul-2004 : created
 */
package com.philemonworks.typewise.test;

import java.util.ArrayList;
import junit.framework.TestCase;
import com.philemonworks.typewise.Bounds;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
;

/**
 * @author emicklei
 *
 */
public class DirtyTest extends TestCase {
	public void testLabel(){
		Label w = new Label("lab",1,1,1,1);
		w.setString("Ikke");
		assertTrue(w.isDirty());
	}
	public void testWidgetBounds(){
		Label w = new Label("lab",1,1,1,1);
		w.setBounds(new Bounds(1,1,2,2));
		assertTrue(w.isDirty());
	}	
	public void testList(){
		List w = new List("lab",1,1,1,1);
		w.setItems(new ArrayList());  // list does not compare entries in list
		assertTrue(w.isDirty());
	}
}
