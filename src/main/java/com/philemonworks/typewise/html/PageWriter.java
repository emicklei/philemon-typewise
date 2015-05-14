/**
 * Licensed Material - Property of Gijjes & Philemon
 * 
 * (c) Copyright 2006 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 9-apr-2005: created
 *
 */
package com.philemonworks.typewise.html;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import com.philemonworks.itable.SortableTable;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.writer.HTMLWriter;

/**
 * PageWriter is a helper class to render a Screen using XHTML.
 */
public class PageWriter {
	public static String FONTFAMILY = "lucinda console";
	public static String FONTSIZE = "10";
	public String stylesheetReference = null;
	/**
	 * @param aScreen
	 * @param pw
	 * @param postAction
	 */
	public void writeOn(Screen aScreen, Dialog dialogOrNull, PrintStream pw, String postAction) {
		HTMLWriter writer = new HTMLWriter(pw);
		writer.pretty = Logger.getLogger(PageWriter.class).isDebugEnabled();
		this.writePageHeaderOn(writer,aScreen.getTitle());
		aScreen.getModel().renderHTML(aScreen, dialogOrNull, writer, postAction, this);
		this.writePageFooterOn(writer);
	}
	/**
	 * This method is called back from the ApplicationModel when the model does not want to perform the HTML painting
	 * itself.
	 * 
	 * @param aScreen
	 *        Screen
	 * @param dialogOrNull 
	 * 			Dialog
	 * @param writer
	 *        HTMLWriter
	 * @param postAction
	 *        String
	 */
	public void defaultWriteOn(Screen aScreen, Dialog 	dialogOrNull, HTMLWriter writer, String postAction) {
		Map map = new HashMap();
		map.put("onload", "javascript:philemonOnload()");
		map.put("onkeydown", "javascript:philemonOnkeydown(event)");
		writer.body(map);
		
		SortableTable.writeSelectionScriptsOn(writer);
		SortableTableListRenderer.writeSelectionScriptsOn(writer);
		
		// Using CSS for layout
		CSSBasedRenderer renderer = new CSSBasedRenderer(writer);
		// First the dialog if any
		if (dialogOrNull != null) {
			renderer.handle(dialogOrNull, aScreen.getBounds());
			renderer.screenOffsetY = 40; // TODO
		}
		// Then the screen
		this.writeScreenOn(aScreen, writer, postAction, renderer);
		writer.end(); // body
	}
	private void writeScreenOn(Screen aScreen, HTMLWriter writer, String postAction, CSSBasedRenderer renderer) {
		renderer.setPostAction(postAction);
		renderer.fontFamily = FONTFAMILY == null ? "tahoma,verdana,arial" : FONTFAMILY;					
		renderer.fontSize = FONTSIZE == null ? "10" : FONTSIZE;		
		renderer.handle(aScreen);
	}
	private void writePageFooterOn(HTMLWriter writer) {
		writer.end(); // html
	}
	private void writePageHeaderOn(HTMLWriter writer,String title) {
		// writer.xml();
		// writer.doctype();
		// writer.raw("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\"\n\"http://www.w3.org/TR/html4/loose.dtd\">\n");
		writer.html();
		writer.head();
		writer.title(title);
		writer.noCacheMetaTags();		
		writer.stylesheet(stylesheetReference);
		writer.end(); // head

	}
	
}