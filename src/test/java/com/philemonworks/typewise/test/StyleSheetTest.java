/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 18-jul-2004: created
 *
 */
package com.philemonworks.typewise.test;

import java.io.File;
import junit.framework.TestCase;
import com.philemonworks.typewise.StyleSheet;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StyleSheetTest extends TestCase {
    public static String path = System.getProperty("user.dir") + "/target/";
    public void setUp(){
        new File(path).mkdirs();
    }
    public void testScreen(){
        Screen main = new Screen("top",null,30,40);
        WidgetAppearance style = main.getStyleSheet().getOrCreate("main");
        style.setForeground(Color.red);
        main.setStyle("main");
        assertEquals(Color.red, main.getAppearance().getForeground());
    }
    public void testLoadStyleSheet(){
        this.testStoreStyleSheet(); // violation...
        StyleSheet sheet = new StyleSheet(path + "temp.stylesheet");
        sheet.load();
        assertEquals(Color.decode("#0"), sheet.getOrCreate("button").getForeground());
        
    }    
    public void testStoreStyleSheet(){
        StyleSheet sheet = new StyleSheet();
        WidgetAppearance style = sheet.getOrCreate("button");
        style.setForeground(Color.black);
        style.setDisabledForeground(Color.yellow);
        sheet.getOrCreate("field").setSelectionBackground(Color.yellow);
        sheet.store(path + "temp.stylesheet");
    }
}
