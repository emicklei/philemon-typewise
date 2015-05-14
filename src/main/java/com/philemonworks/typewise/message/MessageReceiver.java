/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.message;

/**
 * MessageReceiver is an interface for any Object that can handle a Message.
 * 
 * @author emicklei
 */
public interface MessageReceiver {
	/**
	 * Answer a dotted String representing an access path that can be
	 * used to find the object in some evaluation context.
	 * Typically, it will be the name of a CWT widget.
	 * @return String
	 */
    public String getAccessPath();
	/**
	 * Handle a message.
	 * @param anIndexedMessageSend : IndexedMessageSend
	 */
	public void dispatch(IndexedMessageSend anIndexedMessageSend);
}
