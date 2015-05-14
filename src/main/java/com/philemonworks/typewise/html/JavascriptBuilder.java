/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author Bas
 * 9-jun-2005: created
 *
 */
package com.philemonworks.typewise.html;

import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.custom.Timer;

/**
 * @author Bas ter Brugge
 *
 * Util class for building javascript functions
 */
public class JavascriptBuilder {

	/**
	 * Exists for Timer rendering
	 */
	public static final String TIMER_FUNCTION_NAME = "startClock";

	/**
	 * Method that writes the javascript function for handling the timer
	 * widget in an HTML / Javascript environment.
	 * 
	 * @param aCounter
	 * @return source 
	 */
	public static String writeTimerFunction(Timer aCounter) {
		
		int pause = 1000;
    	if (aCounter.getDelayInSeconds() > 0) {
    		pause *= aCounter.getDelayInSeconds();
    	}

    	String javascript = "<script type=\"text/javascript\">" +
        "\n var x = " + aCounter.getStart() + ";" +
        "\n var y = " + aCounter.getStep() + ";" + 
        "\n function " + TIMER_FUNCTION_NAME + "() {" + 
        "\n x = x + y; " + 
        "\n dis_x = x; " +
        "\n if (x < 10) dis_x = '0' + dis_x; " +
        "\n document.forms[0]." + aCounter.getName() + ".value = dis_x; " +  
        "\n if(x==" + aCounter.getStop() + "){ " + 
        "\n 	formEvent('" + aCounter.getName() + "','" + CWT.EVENT_COUNTER_END + "');" +
        "\n   return false;" + 
        "\n  } else {" +
        "\n      setTimeout(\"" + TIMER_FUNCTION_NAME + "()\", "+ pause +");" +
        "\n  } " + 
        "}\n</SCRIPT>\n";
		return javascript;
	}

    /**
     * Writes a call to the formEvent function.
	 * @param widgetName
	 * @param event
	 * @return source
	 */
	public static String writeFormEventCall(String widgetName, byte event) {
		return "javascript:formEvent('" + widgetName + "','" + event + "');";
	}
    /**
     * Writes the form event function that sets some hidden parameters and then 
     * triggers the form submit. 
	 * @return source
	 */
	public static String writeFormEventFunction() {
		String javascript = 
			"<script type=\"text/javascript\">" +
			"\nfunction formEvent(src, event) { " + 
                "\ndocument.forms[0]._submitWidget.value = src;" + 
                "\ndocument.forms[0]._widgetEvent.value = event; " + 
                "\ndocument.forms[0].submit();}" +
            "\n</script>\n";
		return javascript;
	}

	/**
	 * Writes an event handler function for the page.
	 * @param aScreen
	 * @return source
	 */
	public static StringBuffer writeKeyHandlerFunction(Screen aScreen) {
		StringBuffer javascript = new StringBuffer();
    	javascript.append("<script type=\"text/javascript\">" )
					.append("\nfunction checkKeyDown(e) { ")
					.append("\nvar code = 0;")
					.append("\nif (window.event) {")
					.append("\ncode = window.event.keyCode; ")
					.append("\n} else {")
					.append("\ncode = e.which; }");
    	
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F1));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F2));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F3));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F4));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F5));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F6));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F7));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F8));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F9));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F10));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F11));
    	javascript.append(writeKeyEventFormScript(aScreen, CWT.EVENT_F12));
    	javascript.append("\n}\n</script>\n");
		return javascript;
	}
	
	/**
	 * Writes a key event part for the specified event in the event handler script 
	 * of the body.
	 * @param aScreen
	 * @param eventID
	 * @return String
	 */
    private static String writeKeyEventFormScript(Screen aScreen, byte eventID) {
    	String result = "";
    	
    	if (aScreen.hasMessageForEvent(eventID)) {
			result += "\nif (code == " + getKeyCode(eventID) + ") {" +
							writeFormEventCall(aScreen.getName(), eventID) +
						  "  return false;" +
						  " } ";
    	}

    	return result;
    }
    
    /**
     * Returns the HTML key code for an event.  
     * @param eventID
     * @return String
     */
    private static String getKeyCode(byte eventID) {
    	switch (eventID) {
			case CWT.EVENT_F1:
				return "112";
			case CWT.EVENT_F2:
				return "113";
			case CWT.EVENT_F3:
				return "114";
			case CWT.EVENT_F4:
				return "115";
			case CWT.EVENT_F5:
				return "116";
			case CWT.EVENT_F6:
				return "117";
			case CWT.EVENT_F7:
				return "118";
			case CWT.EVENT_F8:
				return "119";
			case CWT.EVENT_F9:
				return "120";
			case CWT.EVENT_F10:
				return "121";
			case CWT.EVENT_F11:
				return "122";
			case CWT.EVENT_F12:
				return "123";
			default:
				return "";
    	}
    }
}
