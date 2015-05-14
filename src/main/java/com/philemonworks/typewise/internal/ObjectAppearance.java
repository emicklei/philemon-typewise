/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 10-jan-2004 by emicklei
 *
 * */
package com.philemonworks.typewise.internal;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 * 
 * Purpose:
 *  
 */
public abstract class ObjectAppearance implements BinaryAccessible , Serializable {

    private Color foreground = null;
    private Color background = null;
    private Color selectionForeground = null;
    private Color selectionBackground = null;
    private Color disabledForeground = null;

    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((ObjectAppearance) this);
    }
    /**
     * @param styleName String
     * @param sheet Properties
     */
    public void addStyleSheetEntriesTo(String styleName, Properties sheet) {
        this.addStyleSheetEntryTo(styleName, "foreground", foreground, sheet);
        this.addStyleSheetEntryTo(styleName, "background", background, sheet);
    }
    protected void addStyleSheetEntryTo(String styleName, String attribute, Color aColor, Properties sheet) {
        if (aColor == null)
            return;
        sheet.put(styleName + "." + attribute, asHex(aColor));
    }

    /**
     * @return Color the background
     */
    public Color getBackground() {
        return background;
    }
    /**
     * @return Color the disabled foreground
     */
    public Color getDisabledForeground() {
        return disabledForeground;
    }

    /**
     * @return Color the foreground
     */
    public Color getForeground() {
        return foreground;
    }

    /**
     * @return Color the selection background
     */
    public Color getSelectionBackground() {
        return selectionBackground;
    }

    /**
     * @return Color the selection foreground
     */
    public Color getSelectionForeground() {
        return selectionForeground;
    }

    /**
     * @param color the new background
     */
    public void setBackground(Color color) {
        background = color;
    }

    /**
     * @param color
     */
    public void setDisabledForeground(Color color) {
        disabledForeground = color;
    }

    /**
     * @param color
     */
    public void setForeground(Color color) {
        foreground = color;
    }

    /**
     * @param color
     */
    public void setSelectionBackground(Color color) {
        selectionBackground = color;
    }

    /**
     * @param color
     */
    public void setSelectionForeground(Color color) {
        selectionForeground = color;
    }
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Appearance{");
        if (foreground != null) { buffer.append(" fore="); buffer.append(asHex(foreground)); }
        if (background != null) { buffer.append(" back="); buffer.append(asHex(background)); }        
        if (selectionForeground != null) { buffer.append(" selfore="); buffer.append(asHex(selectionForeground)); }        
        if (selectionBackground != null) { buffer.append(" selback="); buffer.append(asHex(selectionBackground)); }        
        if (disabledForeground != null) { buffer.append(" disfore="); buffer.append(asHex(disabledForeground)); }        
        buffer.append("}");
        return buffer.toString();
    }
    /**
     * @param aColor
     * @return String the HTML representation of a Color
     */
    public static String asHex(Color aColor){
        String colorString = "#"; 
        String hex;
        hex = Integer.toHexString(aColor.getRed());
        if (hex.length() == 1) colorString += "0";
        colorString += hex;
        hex = Integer.toHexString(aColor.getGreen());
        if (hex.length() == 1) colorString += "0";
        colorString += hex;
        hex = Integer.toHexString(aColor.getBlue());
        if (hex.length() == 1) colorString += "0";
        colorString += hex;
        return colorString;
    }
}