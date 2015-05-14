/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 2-apr-2005: created
 *
 */
package com.philemonworks.typewise;

import java.awt.Point;
import com.philemonworks.typewise.internal.Widget;

/**
 * UILayoutHelper is a helper class for changing the layout of widgets on a Screen.
 * 
 * @author emicklei
 */
public class UILayoutHelper {
    /**
     * Constant for widget alignment. Two widgets will have the same left column.
     */
    public static byte LEFT = 1;
    /**
     * Constant for widget alignment. Two widgets will have the same right column.
     */
    public static byte RIGHT = 3;
    /**
     * Constant for widget alignment. Two widgets will have the same top row.
     */    
    public static byte TOP = 2;
    /**
     * Constant for widget alignment. Two widgets will have the same bottom row.
     */
    public static byte BOTTOM = 4;    
    /**
     * Align one Widget with respect to another fixed Widget. 
     * @param sideMoving : the side of the widget that is changed (LEFT,RIGHT,TOP,BOTTOM)
     * @param movingWidget : the widget that is relocated
     * @param sideFixed : the side of the widget that is fixed
     * @param fixedWidget : the widget that will keep a fixed location
     * @param offset : offset in columns or rows with respect of the sideFixed
     */
    public void align(byte sideMoving, Widget movingWidget, byte sideFixed, Widget fixedWidget, int offset) {
        Point posM = movingWidget.getBounds().getLocation();
        Point posF = fixedWidget.getBounds().getLocation();
        if (sideMoving == LEFT) {
            if (sideFixed == LEFT) {
                movingWidget.getBounds().setLocation(new Point(posM.x, posF.y + offset));
                return;
            }
            if (sideFixed == RIGHT) {
                movingWidget.getBounds().setLocation(new Point(posM.x, posF.y + offset));
                return;
            }
            this.errorInAligning();
        }
    }
    private void errorInAligning() {
        throw new RuntimeException("[UIBuilder] incompatible alignment constants");
    }
}
