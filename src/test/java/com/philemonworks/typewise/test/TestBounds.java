/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 2-aug-2004: created
 *
 */
package com.philemonworks.typewise.test;

import junit.framework.TestCase;
import com.philemonworks.typewise.Bounds;

/**
 * @author emicklei
 *
 */
public class TestBounds extends TestCase {
    public void testCreate(){
        Bounds b = new Bounds(1,2,3,4);
        assertTrue("left", b.getLeftColumn() == 2);
        assertTrue("top", b.getTopRow() == 1);        
        assertTrue("right", b.getRightColumn() == 5);
        assertTrue("bottom", b.getBottomRow() == 3);
        assertTrue("rows", b.getRows() == 3);
        assertTrue("columns", b.getColumns() == 4);        
    }
}
