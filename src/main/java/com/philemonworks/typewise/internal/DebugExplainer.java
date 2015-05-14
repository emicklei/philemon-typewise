/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004-2006. All rights reserved.
 * Use, duplication, distribution or disclosure restricted.
 */
package com.philemonworks.typewise.internal;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import com.philemonworks.typewise.KeyConstants;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;

/**
 * @author emicklei
 *  
 */
public class DebugExplainer implements CWT, MessageConstants {
    private static HashMap widgetConstants = new HashMap();
    private static HashMap eventConstants = new HashMap();
    static {
        init();
    }
    /**
     * @param map
     * @param constant
     * @param explanation
     */
    private static void put(HashMap map, int constant, String explanation) {
        map.put(new Integer(constant), explanation);
    }
    /**
     * 
     */
    private static void init() {
        put(widgetConstants, WidgetAccessorConstants.GET_APPEARANCE, "GET_APPEARANCE");
        put(widgetConstants, WidgetAccessorConstants.SET_APPEARANCE, "setAppearance");
        put(widgetConstants, WidgetAccessorConstants.GET_ALIGNMENT, "GET_ALIGNMENT");
        put(widgetConstants, WidgetAccessorConstants.SET_ALIGNMENT, "setAlignment");
        put(widgetConstants, WidgetAccessorConstants.GET_VISIBLE, "GET_VISIBLE");
        put(widgetConstants, WidgetAccessorConstants.SET_VISIBLE, "setVisible");
        put(widgetConstants, WidgetAccessorConstants.GET_BOUNDS, "GET_BOUNDS");
        put(widgetConstants, WidgetAccessorConstants.SET_BOUNDS, "setBounds");
        put(widgetConstants, WidgetAccessorConstants.GET_ENABLED, "GET_ENABLED");
        put(widgetConstants, WidgetAccessorConstants.SET_ENABLED, "setEnabled");
        put(widgetConstants, WidgetAccessorConstants.GET_SELECTION, "GET_SELECTION");
        put(widgetConstants, WidgetAccessorConstants.SET_SELECTION, "setSelection");
        put(widgetConstants, WidgetAccessorConstants.GET_ITEMS, "GET_ITEMS");
        put(widgetConstants, WidgetAccessorConstants.SET_ITEMS, "setItems");
        put(widgetConstants, WidgetAccessorConstants.GET_SELECTEDITEM, "GET_SELECTEDITEM");
        put(widgetConstants, WidgetAccessorConstants.SET_SELECTEDITEM, "setSelectedItem");
        put(widgetConstants, WidgetAccessorConstants.GET_COUNT, "GET_COUNT");
        put(widgetConstants, WidgetAccessorConstants.SET_COUNT, "setCount");
        put(widgetConstants, WidgetAccessorConstants.GET_DELAY, "GET_DELAY");
        put(widgetConstants, WidgetAccessorConstants.SET_DELAY, "setDelay");
        put(widgetConstants, WidgetAccessorConstants.GET_REPEAT, "GET_REPEAT");
        put(widgetConstants, WidgetAccessorConstants.SET_REPEAT, "setRepeat");
        put(widgetConstants, WidgetAccessorConstants.GET_STRING, "GET_STRING");
        put(widgetConstants, WidgetAccessorConstants.SET_STRING, "setText");
        put(widgetConstants, WidgetAccessorConstants.GET_EDITABLE, "GET_EDITABLE");
        put(widgetConstants, WidgetAccessorConstants.SET_EDITABLE, "setEditable");
        put(widgetConstants, WidgetAccessorConstants.GET_URL, "GET_URL");
        put(widgetConstants, WidgetAccessorConstants.SET_URL, "setURL");
        put(widgetConstants, WidgetAccessorConstants.SET_SCALETOFIT, "setScaleToFit");
        put(widgetConstants, WidgetAccessorConstants.GET_TITLE, "GET_TITLE");
        put(widgetConstants, WidgetAccessorConstants.SET_TITLE, "setTitle");
        put(widgetConstants, WidgetAccessorConstants.GET_ITEM, "GET_ITEM");
        put(widgetConstants, WidgetAccessorConstants.SET_ITEM, "setItem");
        put(widgetConstants, WidgetAccessorConstants.SET_INTERVAL, "setInterval");
        put(widgetConstants, WidgetAccessorConstants.GET_SELECTIONINDEX, "GET_SELECTIONINDEX");
        put(widgetConstants, WidgetAccessorConstants.SET_SELECTIONINDEX, "setSelectionIndex");
        put(widgetConstants, WidgetAccessorConstants.GET_TICKALIGNMENT, "GET_TICKALIGNMENT");
        put(widgetConstants, WidgetAccessorConstants.SET_TICKALIGNMENT, "setTickAlignment");
        
        put(widgetConstants, OPERATION_clearAllDirty, "clearAllDirty");
        put(widgetConstants, OPERATION_close, "close");
        put(widgetConstants, OPERATION_handleError, "handleError");
        put(widgetConstants, OPERATION_installMessageOnEvent, "installMessageOnEvent");
        put(widgetConstants, OPERATION_messageForAddingCenteredWidgetAccessedBy, "OPERATION_?");
        put(widgetConstants, OPERATION_messageForAddingWidgetAccessedBy, "OPERATION_?");
        put(widgetConstants, OPERATION_messageForShowingScreenAccessedBy, "OPERATION_?");
        put(widgetConstants, OPERATION_messageForShowingScreenAccessedByWith, "OPERATION_?");
        put(widgetConstants, OPERATION_OK, "ok");
        put(widgetConstants, OPERATION_OPENHELP, "openHelp");
        put(widgetConstants, OPERATION_OPENURL, "openURL");
        put(widgetConstants, OPERATION_remove, "remove");
        put(widgetConstants, OPERATION_request, "request");
        put(widgetConstants, OPERATION_SHOW, "show");
		put(widgetConstants, OPERATION_SETFOCUS, "setFocus");
		put(widgetConstants, OPERATION_SHOWDIALOG, "showDialog");
		put(widgetConstants, OPERATION_STATUS, "setStatus");
		put(widgetConstants, OPERATION_GOTOURL, "gotoURL");

        put(eventConstants, EVENT_CLICKED, "EVENT_CLICKED");
        put(eventConstants, EVENT_COUNTED, "EVENT_COUNTED");
        put(eventConstants, EVENT_COUNTER_END, "EVENT_COUNTER_END");
        put(eventConstants, EVENT_F1, "EVENT_F1");
        put(eventConstants, EVENT_F2, "EVENT_F2");
        put(eventConstants, EVENT_F3, "EVENT_F3");
        put(eventConstants, EVENT_F4, "EVENT_F4");
        put(eventConstants, EVENT_F5, "EVENT_F5");
        put(eventConstants, EVENT_F6, "EVENT_F6");
        put(eventConstants, EVENT_F7, "EVENT_F7");
        put(eventConstants, EVENT_F8, "EVENT_F8");
        put(eventConstants, EVENT_F9, "EVENT_F9");
        put(eventConstants, EVENT_F10, "EVENT_F10");
        put(eventConstants, EVENT_F11, "EVENT_F11");
        put(eventConstants, EVENT_F12, "EVENT_F12");

        put(eventConstants, EVENT_GETTINGFOCUS, "EVENT_GETTINGFOCUS");
        put(eventConstants, EVENT_LOSINGFOCUS, "EVENT_LOSINGFOCUS");
        put(eventConstants, EVENT_LOSINGFOCUS, "EVENT_LOSINGFOCUS");
        put(eventConstants, EVENT_SELECTEDITEM, "EVENT_LOSINGFOCUS");
        put(eventConstants, EVENT_DEFAULTACTION, "EVENT_DEFAULTACTION");
    }
    /**
     * @param constant
     * @return String description of constant
     */
    public static String widgetConstant(int constant) {
        String explanation = (String) widgetConstants.get(new Integer(constant));
        if (explanation == null)
            return (String.valueOf(constant)) + "=?";
        return explanation;
    }
    /**
     * Wish we had auto-unboxing
     * @param boxInt Integer
     * @return String
     */
    public static String eventConstant(Integer boxInt){
    	return eventConstant(boxInt.intValue());
    }
    /**
     * Wish we had auto-unboxing
     * @param boxByte Byte
     * @return String
     */
    public static String eventConstant(Byte boxByte){
    	return eventConstant(boxByte.byteValue());
    }
    /**
     * Wish we had auto-unboxing
     * @param intLike Object
     * @return String
     */
    public static String eventConstant(Object intLike){
    	return eventConstant(Integer.parseInt(intLike.toString()));
    }
    /**
     * @param constant
     * @return String description of constant
     */
    public static String eventConstant(int constant) {
        String explanation = (String) eventConstants.get(new Integer(constant));
        if (explanation == null)
            return (String.valueOf(constant)) + "=?";
        return explanation;
    }
	private static int MAXEXPLAINSIZE = 100;
    /**
     * @param msg
     * @param receiver the actual message performer
     * @return String
     */
    public static String explainFor(IndexedMessageSend msg, MessageReceiver receiver) {
    	// handle install separate
    	if (OPERATION_installMessageOnEvent == msg.getIndex())
    		return explainInstallMessageOnEventFor(msg,receiver);    	    	
        StringBuffer buffer = new StringBuffer();
        if (receiver != null)
        	buffer.append(ErrorUtils.displayString(receiver)+"['"+msg.getReceiver()+"']");
        else
        	buffer.append(msg.getReceiver());
        buffer.append('.');
        buffer.append(widgetConstant(msg.getIndex()));
        buffer.append('(');
        ArrayList args = msg.getArgumentList();
        if (args.size() > 0) {            
            for (int a=0;a<args.size();a++){
                if (a>0) buffer.append(',');
                buffer.append(explain(args.get(a)));
                if (buffer.length() > MAXEXPLAINSIZE) {
                    buffer.append("...");
                    a = args.size();
                }
            }
        }
        buffer.append(')');
        return buffer.toString();
    }
    /**
     * Compose an explanation for the installMessageOnEvent message
     * @param msg
     * @param receiver
     * @return the explanation
     */
    public static String explainInstallMessageOnEventFor(IndexedMessageSend msg, MessageReceiver receiver){
        StringBuffer buffer = new StringBuffer();
        if (receiver != null)
        	buffer.append(ErrorUtils.displayString(receiver)+"['"+msg.getReceiver()+"']");
        else
        	buffer.append(msg.getReceiver());
        buffer.append('.');
        buffer.append(widgetConstant(OPERATION_installMessageOnEvent));
        buffer.append('(');
        buffer.append(explainFor((Message)(msg.getArgumentList().get(0)),receiver));
        buffer.append("),");        
        buffer.append(eventConstant(msg.getArgumentList().get(1)));
        buffer.append(')');
        return buffer.toString();
    }
    /**
     * Return a String representation of the object
     * @param anObject
     * @return a debug explanation
     */
    public static String explain(Object anObject){
        if (anObject instanceof String) return explain((String)anObject);
        if (anObject instanceof Message) return explainFor((Message)anObject, null);		
		if (anObject instanceof Object[]) return explain((Object[])anObject);
        if (anObject == null) return "null";
        return anObject.toString();
    }
	/**
	 * Return a String representation of the array
	 */
	public static String explain(Object[] array){
		if (array.length == 0) return "[0]";
		if (array.length == 1) return "[" + explain(array[0]) + "]";
		return array.toString();
	}
    /**
     * Report the state of the HttpReqest
     * @param request : HttpRequest
     * @return
     */
    public static String explain(HttpServletRequest request){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Request query: " + request.getQueryString() + "{");
        Enumeration e = request.getAttributeNames();
        while (e.hasMoreElements()) {
            String key = (String)e.nextElement();
            String value = (String)request.getAttribute(key);
            buffer.append(key + "=" + value);
            if (e.hasMoreElements()) buffer.append(",");
        }
        buffer.append("}");
        Cookie[] cookies = request.getCookies();
        buffer.append("[");
        if (cookies != null)
            for (int c=0;c<cookies.length;) {
                buffer.append(cookies[c].getName());
                buffer.append("=");
                buffer.append(cookies[c++].getValue());
            	if (c!=cookies.length) buffer.append(",");
            }
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Return the argument string enclosed by quotes.
     * @param aString
     * @return the explanation
     */
    public static String explain(String aString){
        return "\"" + aString + "\"";
    }    
    /**
     * Return an explanation for a MessageSend.
     * @param msg
     * @return the explanation
     */
    public static String explainFor(MessageSend msg, MessageReceiver receiver) {
        StringBuffer buffer = new StringBuffer();
        if (receiver != null && receiver.getAccessPath() != CLIENT)
        	buffer.append(ErrorUtils.displayString(receiver)+"['"+msg.getReceiver()+"']");
        else
        	buffer.append(msg.getReceiver());
        buffer.append('.');
        buffer.append(msg.getOperation());
        buffer.append('(');
        Object[] args = msg.getArguments();
        if (args.length>0) {
            for (int a=0;a<args.length;a++) {
                if (a>0) buffer.append(',');
                buffer.append(explain(args[a]));
                // buffer.append(args[a]);
                if (buffer.length() > MAXEXPLAINSIZE) {
                    buffer.append("...");
                    a = args.length;
                }                
            }
        }
        buffer.append(')');
        return buffer.toString();
    }
    /**
     * @param msg
     * @return
     */
    public static String explainFor(MessageSequence msg, MessageReceiver receiver) {
        StringBuffer buffer = new StringBuffer();
        for (int m = 0; m < msg.getMessages().size(); m++) {
            buffer.append(m);
            buffer.append(':');
            buffer.append(explainFor((Message)msg.getMessages().get(m), receiver));
            buffer.append('\n');
        }
        return buffer.toString();
    }
    /**
     * Return an explanation for the message argument.
     * 
     * @param msg : Message
     * @param receiver TODO
     * @return an explanation
     */
    public static String explainFor(Message msg, MessageReceiver receiver) {
        if (msg instanceof IndexedMessageSend)
            return explainFor((IndexedMessageSend) msg, receiver);
        if (msg instanceof MessageSend)
            return explainFor((MessageSend) msg, receiver);        
        if (msg instanceof MessageSequence)
            return explainFor((MessageSequence) msg, receiver);
        return "Unknown message:" + msg;
    }
	/**
	 * Return an explanation of the key,value pairs of a Map
	 * 
	 * @param aMap : the Map of key,value entries
	 * @return String : the explanation
	 */
	public static String explain(Map aMap){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Map[");
		buffer.append(aMap.size());
		buffer.append("]{");
		for (Iterator it=aMap.keySet().iterator();it.hasNext();){
			Object key = it.next();
			Object value = aMap.get(key);
			buffer.append(key);
			buffer.append('=');
			buffer.append(explain(value));
			if (it.hasNext()) buffer.append(',');
		}
		buffer.append('}');
		return buffer.toString();
	}
}