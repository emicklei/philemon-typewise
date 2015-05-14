/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 10-jan-2004 by emicklei
 * 13-2-2004: mvhulsentop: made variables private
 * 13-2-2004: mvhulsentop: generated getters/setters
 * 11-8-2004: emm: stylesheet + focuscolor
 * */
package com.philemonworks.typewise;

import java.io.IOException;
import java.util.Properties;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.ObjectAppearance;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.Color;

/**
 * @author emicklei
 *
 * Purpose:
 *
 */
public class WidgetAppearance extends ObjectAppearance implements BinaryAccessible {
	private static final long serialVersionUID = 3257572797621418035L;
	private Color borderColor = null; 
	private Color focusColor = null;
	/**
	 * Holds the default WidgetAppearance (white,black,yellow,gray,lightGray,gray)
	 */
	public static WidgetAppearance DEFAULT = null;
	static {
	    DEFAULT = new WidgetAppearance();
	    DEFAULT.setForeground(Color.white);
	    DEFAULT.setBackground(Color.black);
	    DEFAULT.setSelectionForeground(Color.yellow);
	    DEFAULT.setSelectionBackground(Color.gray);
	    DEFAULT.setDisabledForeground(Color.lightGray);
	    DEFAULT.setFocusColor(Color.gray);
	    DEFAULT.setBorderColor(null);
	}
	
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((WidgetAppearance) this);		
	}

	/**
	 * @return Color
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * @param border Color
	 */
	public void setBorderColor(Color border) {
		this.borderColor = border;
	}
    public void addStyleSheetEntriesTo(String styleName, Properties sheet) {
        super.addStyleSheetEntriesTo(styleName,sheet);
        this.addStyleSheetEntryTo(styleName,"borderColor",borderColor,sheet);
        this.addStyleSheetEntryTo(styleName,"focusColor",focusColor,sheet);
    }
    /**
     * @return Returns the focusColor.
     */
    public Color getFocusColor() {
        return focusColor;
    }
    /**
     * @param focusColor The focusColor to set.
     */
    public void setFocusColor(Color focusColor) {
        this.focusColor = focusColor;
    }
    public String toString(){
        String line = super.toString();
        line += "{";
        if (focusColor != null) { line += " focus="; line += asHex(focusColor); }
        if (borderColor != null) { line += " border="; line += asHex(borderColor); }
        line = line + "}";
        return line;
    }
}
