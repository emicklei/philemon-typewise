/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 17-jul-2004: created
 *
 */
package com.philemonworks.typewise;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.util.Color;
/**
 *  StyleSheet is used to define shared WidgetAppearance objects
 *  that can be used for building CWT screens.
 *  When the name represent a file name then a load will read the values.
 *  The file format is based on the standard Properties layout:
 * <p/>
 * [styleName].[attribute]=[encoded color]
 * <p/>
 * <ul>
 * <li>styleName : can be any name without spaces</li>
 * <li>attribute : must be one of {foreground,background,selectionForeground,selectionBackground,disabledForeground,border,focus} (case insensitive)</li> 
 * <li>color : must be a sequence of three hexadecimal byte values (maybe prefixed with a pound sign #)</li>
 * </ul>
 * Examples:
 * <ul>
 * <li>button.foreground=#FFFFFF</li>
 * <li>strong.selectionForeground=00FFEE</li>
 * </ul>
 */
public class StyleSheet implements Serializable {
	private static final long serialVersionUID = 3977016249597245489L;
	private String name;
    private HashMap styles = new HashMap();
	
    /**
     * Default constructor
     */
    public StyleSheet() {
		super();	
    }
    /**
     * Create a StyleSheet using the storage resource name
     * @param aName name of the resource
     */
    public StyleSheet(String aName) {
        super();
        this.name = aName;
		this.load();
    }
    /**
     * @return the name of the resource that stores the definitions
     */
    public String getName(){
        return name;
    }
	/**
	 * Answer whether there is a definition for a styleName
	 * @param styleName String name of the style
	 * @return true if a definiton is present
	 */
	public boolean hasStyle(String styleName){
		return styles.containsKey(styleName);
	}
    /**
     * Return the appearance associated with a styleName
     * Throw an exception if the style was not defined.
     * @param styleName
     * @return the appearance associated with a styleName
     * @throws RuntimeException 
     */
    public WidgetAppearance get(String styleName) {
        WidgetAppearance app = (WidgetAppearance) styles.get(styleName);
        if (app == null) {
            throw new RuntimeException("No style definition found for:\"" + styleName + "\" in:" + name);
        }
        return app;
    }
    /**
     * Create a new appearance if the style was not defined.   
     * @param styleName
     * @return the appearance associated with a styleName
     */
    public WidgetAppearance getOrCreate(String styleName) {
        WidgetAppearance app = (WidgetAppearance) styles.get(styleName);
        if (app == null) {
            app = new WidgetAppearance();
            styles.put(styleName, app);
        }
        return app;
    }    
    /**
     * Add a style using the styleName as its key and the appearance its value 
     * @param styleName
     * @param anAppearance
     */
    public void put(String styleName, WidgetAppearance anAppearance) {
        styles.put(styleName, anAppearance);
    }
    /**
     * Read the stylesheet from a file using the properties format
     */
    public void load() {
        Properties properties = new Properties();
        try {			
            InputStream input;
			// first try to use the name as resource
			input = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
			// if that fail, retry as filename
			if (input == null) input = new FileInputStream(name);
            properties.load(input);
            input.close();
        } catch (Exception ex) {
            ErrorUtils.error(this,ex,"Unable to read stylesheet from:" + name);
        }
        Enumeration keys = properties.keys();
		List pendingKeys = new ArrayList();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            int dot = key.indexOf('.');
            if (dot == -1)
                ErrorUtils.error(this,"Bad style entry: " + key + " in:" + name);
            String styleName = key.substring(0, dot);
            String attribute = key.substring(dot + 1, key.length());
            String colorString = properties.getProperty(key).trim();  // remove trailing spaces that leads to NumberFormatException
			Color color = null;

			if ((dot = colorString.indexOf('.')) != -1) {
				String otherStyleName = colorString.substring(0, dot);
				String otherAttribute = colorString.substring(dot + 1, colorString.length());
				if (this.hasStyle(otherStyleName)) {
					WidgetAppearance otherStyle = this.get(otherStyleName);
					color = this.getProperty(otherStyle,otherAttribute);
				} else {
					// process this key and value after all other styles have been defined
					pendingKeys.add(key);
				}					
			} else {
				if (!colorString.startsWith("#"))
					colorString = "#" + colorString;				
				try {
					color = Color.decode(colorString);
				} catch (Exception ex) {
					ErrorUtils.error(this,ex,"Bad color value: " + colorString + " in:" + name);
				}
			}
			if (color != null) // may be null for pending key,value
				this.setProperty(this.getOrCreate(styleName), attribute, color);
        }
		// Process pending keys that refer to other attributes of other styles
		for (Iterator it = pendingKeys.iterator();it.hasNext();){
			String key = (String) it.next();
			int dot = key.indexOf('.');
            String styleName = key.substring(0, dot);
            String attribute = key.substring(dot + 1, key.length());
			String otherProperty = properties.getProperty(key).trim();  // remove trailing spaces that leads to NumberFormatException
			String otherStyleName = otherProperty.substring(0, dot);
			String otherAttribute = otherProperty.substring(dot + 1, otherProperty.length());
			WidgetAppearance otherStyle = this.get(otherStyleName);
			Color color = this.getProperty(otherStyle,otherAttribute);
			this.setProperty(this.getOrCreate(styleName), attribute, color);
		}
    }
	/**
	 * Assign the new file name and store it.
	 * @param newFileName
	 */
	public void store(String newFileName){
		name = newFileName;
		this.store();
	}
    /**
     * Write the stylesheet to a file using the properties format
     */
    public void store(){
        try {
            FileOutputStream out = new FileOutputStream(name);
            Properties sheet = new Properties();
            StringBuffer header = new StringBuffer();
            header.append("TypeWise Screen StyleSheet\n");
            header.append("#Appearances:");
            Iterator styleNames = styles.keySet().iterator();
            while (styleNames.hasNext()){
                String styleName = (String)styleNames.next();
                header.append("\n#\t" + styleName);
                WidgetAppearance appearance = (WidgetAppearance)styles.get(styleName);
                appearance.addStyleSheetEntriesTo(styleName,sheet);
            }            
            sheet.store(out,header.toString());
            out.close();
        } catch (Exception ex) {
            ErrorUtils.error(this,ex,"Unable to write: " + name);
        }
    }
    /**
     * @return a List of style names.
     */
    public List getStyleNames(){
        return Arrays.asList(styles.keySet().toArray());
    }
    /**
     * Set a color property from the stylesheet for an appearance.
     * @param appearance
     * @param attributeName
     * @param aColor
     */
    private void setProperty(WidgetAppearance appearance, String attributeName, Color aColor) {
        if ("foreground".equalsIgnoreCase(attributeName)) {
            appearance.setForeground(aColor);
            return;
        } else if ("background".equalsIgnoreCase(attributeName)) {
            appearance.setBackground(aColor);
            return;
        } else if ("selectionForeground".equalsIgnoreCase(attributeName)) {
            appearance.setSelectionForeground(aColor);
            return;
        } else if ("selectionBackground".equalsIgnoreCase(attributeName)) {
            appearance.setSelectionBackground(aColor);
            return;
        } else if ("disabledForeground".equalsIgnoreCase(attributeName)) {
            appearance.setDisabledForeground(aColor);
            return;
        } else if ("border".equalsIgnoreCase(attributeName)) {
            appearance.setBorderColor(aColor);
            return;
        } else if ("focus".equalsIgnoreCase(attributeName)) {
            appearance.setFocusColor(aColor);
            return;
        }
        ErrorUtils.error(this,"Bad attribute: " + attributeName + " in:" + name);
    }
	/**
	 * Get the color that is store by the attributeName of an appearance
	 * @param appearance : WidgetAppearance
	 * @param attributeName : String
	 * @return Color
	 */
	private Color getProperty(WidgetAppearance appearance, String attributeName){
	       if ("foreground".equalsIgnoreCase(attributeName)) {
	            return appearance.getForeground();
	        } else if ("background".equalsIgnoreCase(attributeName)) {
	            return appearance.getBackground();
	        } else if ("selectionForeground".equalsIgnoreCase(attributeName)) {
	            return appearance.getSelectionBackground();
	        } else if ("selectionBackground".equalsIgnoreCase(attributeName)) {
	            return appearance.getSelectionBackground();
	        } else if ("disabledForeground".equalsIgnoreCase(attributeName)) {
	            return appearance.getDisabledForeground();
	        } else if ("border".equalsIgnoreCase(attributeName)) {
	            return appearance.getBorderColor();
	        } else if ("focus".equalsIgnoreCase(attributeName)) {	            
	            return appearance.getFocusColor();
	        }
	        ErrorUtils.error(this,"Bad attribute: " + attributeName + " in:" + name);
			return null;
	}
    public String toString(){
        return super.toString() + "(" + name + ")";
    }
}