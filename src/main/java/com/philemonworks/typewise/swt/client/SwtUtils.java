package com.philemonworks.typewise.swt.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.util.Color;

/**
 * SwtUtils has methods that support the building of SWT client widgets that represent CWT server widgets.
 *
 * @author E.M.Micklei
 */
public class SwtUtils {
	private static HashMap colorCache = new HashMap();
	/**
	 * Return the SWT color that matches the CWT color.
	 * Lookup the color in the colorCache and add a new if absent.
	 * @param rgb Color (CWT) | null
	 * @param device Device (SWT)
	 * @return Color or null if the rgb was null
	 */
	public static org.eclipse.swt.graphics.Color toSWT(Color rgb, Device device) {
		if (rgb == null) return null;
		org.eclipse.swt.graphics.Color entry = (org.eclipse.swt.graphics.Color)colorCache.get(rgb);
		if (entry != null) return entry;
		entry = new org.eclipse.swt.graphics.Color(device, rgb.getRed(), rgb.getGreen(), rgb.getBlue());
		colorCache.put(rgb,entry);
		return entry;
	}
	/**
	 * Disposes all allocated colors and empties the color cache.
	 */
	public static synchronized void flushColors(){
		Logger.getLogger(SwtUtils.class).debug("Flushing SWT Color cache:"+colorCache.size());
		for (Iterator iter = colorCache.values().iterator(); iter.hasNext();) {
			org.eclipse.swt.graphics.Color entry = (org.eclipse.swt.graphics.Color) iter.next();
			entry.dispose();			
		}
		colorCache = new HashMap();
	}
	/**
	 * @param bgColor Color
	 * @param fgColor Color
	 * @param swt Control
	 * @param view SwtApplicationView
	 */
	public static void setColors(Color bgColor, Color fgColor, Control swt, SwtApplicationView view) {
		swt.setBackground(SwtUtils.toSWT(bgColor, view.getDisplay()));
		swt.setForeground(SwtUtils.toSWT(fgColor, view.getDisplay()));
	}
	/**
	 * @param constant int CWT value
	 * @return int SWT value
	 */
	public static int toSWTAlignment(int constant) {
		int c = 0;
		if (CWT.LEFT == constant)
			c = SWT.LEFT;
		if (CWT.CENTER == constant)
			c = SWT.CENTER;
		if (CWT.RIGHT == constant)
			c = SWT.RIGHT;
		return c;
	}	
	/**
	 * @param items List
	 * @return String[]
	 */
	public static String[] asStringArray(List items) {
		String[] newItems = new String[items.size()];
		for (int i = 0; i < items.size(); i++) {
			Object each = items.get(i);
			if (each instanceof ListItem)
				newItems[i] = ((ListItem) each).getItem();
			else
				newItems[i] = each.toString();
		}
		return newItems;
	}	
	/**
	 * Decode the URL into a Map
	 * @param request String
	 * @return Map
	 */
	public static Map decodeArguments(String request) {
		HashMap map = new HashMap();
		if (request.length() == 0)
			return map;
		int mark = request.indexOf('?');
		if (mark > -1) {
			String args = request.substring(mark + 1, request.length());
			StringTokenizer tokenizer = new StringTokenizer(args, "=&");
			while (tokenizer.hasMoreTokens()) {
				String key = tokenizer.nextToken();
				String value = tokenizer.nextToken();
				map.put(key, value);
			}
		}
		return map;
	}		
	/**
	 * @param swtView
	 * @param parent TODO
	 * @param widgets
	 */
	public static void addAllToView(SwtApplicationView swtView, Composite parent, List widgets) {
		// Visit each child.
		for (Iterator iter = widgets.iterator(); iter.hasNext();) {
			((SwtWidget) iter.next()).addToView(swtView, parent);
		}
	}	
	/**
	 * @param newFont Font
	 * @param widgets List
	 */
	public static void updateFontForAll(Font newFont, List widgets) {
		// Visit each child
		for (Iterator iter = widgets.iterator(); iter.hasNext();) {
			((SwtWidget) iter.next()).updateFont(newFont);
		}
	}
	/**
	 * @param swtView SwtApplicationView
	 * @param widgets List
	 */ 
	public static void removeAllFromView(SwtApplicationView swtView,List widgets) {
		// Visit each child
		for (Iterator iter = widgets.iterator(); iter.hasNext();) {
			((SwtWidget) iter.next()).removeFromView(swtView);
		}
	}	
	
	/**
	 * @param resource String
	 * @return the URL for accessing a locally stored resource on the classpath
	 */
	public static String urlForLocalResource(String resource){
		URL url = SwtUtils.class.getClassLoader().getResource(resource);
		if (url == null) Logger.getLogger(SwtUtils.class).error("Unable to find resource:" + resource);
		return url.toExternalForm();
	}
}
