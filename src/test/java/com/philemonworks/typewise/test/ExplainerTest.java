/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 15-aug-2004: created
 *
 */
package com.philemonworks.typewise.test;

import junit.framework.TestCase;
import com.philemonworks.typewise.KeyConstants;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.message.MessageConstants;

/**
 * @author emicklei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplainerTest extends TestCase implements CWT, MessageConstants {
    public void testExplainWidgetConstant(){
        String ex = DebugExplainer.widgetConstant(WidgetAccessorConstants.GET_APPEARANCE);
        assertEquals("GET_APPEARANCE", ex);
    }    
    public void testExplainWidgetConstants(){
        String ex = DebugExplainer.widgetConstant(OPERATION_clearAllDirty);
        assertEquals("clearAllDirty", ex);
    }    
    public void testExplainWidgetConstantUnknown(){
        String ex = DebugExplainer.widgetConstant(-1);
        assertEquals("-1=?", ex);
    }    
}
