/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004-2006. All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author E.M.Micklei
 */
package com.philemonworks.typewise.message;

/**
 * Purpose: This is container of constant values
 *
 */
public interface MessageConstants {
	public final static String CLIENT = "_client";
	public final static String SERVER = "_server";
	
	// Operation constant names
	public final static byte OPERATION_STATUS = 66;
	public final static byte OPERATION_messageForShowingScreenAccessedBy = 67;
	public final static byte OPERATION_messageForShowingScreenAccessedByWith = 68;
	public final static byte OPERATION_SHOW = 69;
	public final static byte OPERATION_remove = 70;
	public final static byte OPERATION_messageForAddingWidgetAccessedBy = 71;	
	public final static byte OPERATION_messageForAddingCenteredWidgetAccessedBy = 72;
	public final static byte OPERATION_handleError = 73;
	public final static byte OPERATION_close = 74;					
	public final static byte OPERATION_installMessageOnEvent = 75;
	public final static byte OPERATION_OK = 27;
	public final static byte OPERATION_OPENHELP = 77;			
	public final static byte OPERATION_OPENURL = 78;
	public final static byte OPERATION_request = 79;
	public final static byte OPERATION_clearAllDirty = 80;
	public final static byte OPERATION_SHOWDIALOG = 81;		
	public final static byte OPERATION_SETFOCUS = 82;
	public final static byte OPERATION_GOTOURL = 83;
	
	// Context independent client-messages
	public final static Message OK = new IndexedMessageSend(CLIENT, OPERATION_OK);
	public final static Message CLOSE = new IndexedMessageSend(CLIENT, OPERATION_close);	
	public final static Message OPENHELP = new IndexedMessageSend(CLIENT, OPERATION_OPENHELP);	
	public final static Message CLIENT_CLEARDIRTYALL = new IndexedMessageSend(CLIENT, OPERATION_clearAllDirty);
	public final static Message SERVER_CLEARDIRTYALL = new IndexedMessageSend(SERVER, OPERATION_clearAllDirty);	
}
