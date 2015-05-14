/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 11-aug-2004: created
 *
 */
package com.philemonworks.typewise.test;

import junit.framework.TestCase;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;

/**
 * Every test method should raise an exception to capture illegal widget states
 *
 */
public class IllegalWidgetTest extends TestCase {
    
    public void testAlignment_Label(){
        Label w = new Label("test",1,1,1,10);
        boolean caught = false;
        try {
            w.setAlignment((byte)4);
        } catch (RuntimeException ex){
            caught = true;
        }
        assertTrue(caught);
    }
    public void testAlignment_TableColumn(){
        TableColumn w = new TableColumn();
        boolean caught = false;
        try {
            w.setAlignment((byte)4);
        } catch (RuntimeException ex){
            caught = true;
        }
        assertTrue(caught);
    }
    public void testBounds(){
        boolean caught = false;
        try {
            new Screen("test",null,-1,1);
        } catch (RuntimeException ex){
            caught = true;
        }
        assertTrue(caught);
    }    
    public void testBounds2(){
        boolean caught = false;
        try {
            new Screen("test",null,-1,-1);
        } catch (RuntimeException ex){
            caught = true;
        }
        assertTrue(caught);
    } 
    public void testName(){
        boolean caught = false;
        try {
            new Screen(null,null,1,1);
        } catch (RuntimeException ex){
            caught = true;
        }
        assertTrue(caught);
    }     
}
