/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 4-apr-04: created
 *
 */
package com.philemonworks.typewise.test;

import junit.framework.TestCase;
import com.philemonworks.typewise.UIBuilder;

/**
 * @author emicklei
 *
 */
public class UIBuilderTest extends TestCase {

	public void testComputeRowsNeeded1(){
		UIBuilder ui = new UIBuilder();
		int rows = ui.computeHowManyRowsWritten("1234",4);
		assertEquals(1,rows); 
	}
	public void testComputeRowsNeeded2(){
		UIBuilder ui = new UIBuilder();
		int rows = ui.computeHowManyRowsWritten("12345",4);
		assertEquals(2,rows); 
	}
	public void testComputeRowsNeeded3(){
		UIBuilder ui = new UIBuilder();
		int rows = ui.computeHowManyRowsWritten("1234\n",4);
		assertEquals(2,rows); 
	}	
	public void testComputeRowsNeeded4(){
		UIBuilder ui = new UIBuilder();
		int rows = ui.computeHowManyRowsWritten("1234\n\n",4);
		assertEquals(2,rows); 
	}		
}
