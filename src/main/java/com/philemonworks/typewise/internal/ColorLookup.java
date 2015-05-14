/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 17-aug-2004: created
 *
 */
package com.philemonworks.typewise.internal;

import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.util.Color;

/**
 * ColorLookup is used to find an appearance color attribute value for a Widget
 * such as foreground, background etc. The following rules apply:
 * <ul>
 * <li>if the widget does not specify an appearance then try its parent in the widget tree</li>
 * <li>if the widget does not specify an appearance and has no parent then use the default value</li>
 * </ul>
 *
 *  @author emicklei
 */
public class ColorLookup {
    /**
     * @param aWidget
     * @return Color
     */
    public static Color backgroundFor(WidgetAppearanceHolder aWidget){
        if (aWidget == null) return WidgetAppearance.DEFAULT.getBackground();
        if (aWidget.hasAppearance()) {
            WidgetAppearance app = aWidget.getAppearance();
            if (app.getBackground() == null) {
                return backgroundFor(aWidget.getParent());
            } else {
                return app.getBackground();
            }
        } else {
            return backgroundFor(aWidget.getParent());
        }
    }
    public static Color foregroundFor(WidgetAppearanceHolder aWidget){
        if (aWidget == null) return WidgetAppearance.DEFAULT.getForeground();
        if (aWidget.hasAppearance()) {
            WidgetAppearance app = aWidget.getAppearance();
            if (app.getForeground() == null) {
                return foregroundFor(aWidget.getParent());
            } else {
                return app.getForeground();
            }
        } else {
            return foregroundFor(aWidget.getParent());
        }
    }  
    public static Color borderColorFor(WidgetAppearanceHolder aWidget){
        if (aWidget == null) return WidgetAppearance.DEFAULT.getBorderColor();
        if (aWidget.hasAppearance()) {
            WidgetAppearance app = aWidget.getAppearance();
            if (app.getBorderColor() == null) {
                return borderColorFor(aWidget.getParent());
            } else {
                return app.getBorderColor();
            }
        } else {
            return borderColorFor(aWidget.getParent());
        }
    }        
}
