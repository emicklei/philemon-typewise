/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * */
package com.philemonworks.typewise.message;

import com.philemonworks.typewise.ApplicationModel;

/**
 * Known implementors of this interface are MessageSend, IndexedMessageSend and MessageSequence
 *
 */
public interface Message extends MessageConstants {
	/**
	 * Answer a new MessageSequence containing the receiver and the arguments @param nextMessage
	 */
	public MessageSequence asSequence();
	/**
	 * Answer the receiver in its reduced form. A sequence with one MessageSend will return that MessageSend. 
	 */
	public Message asMinimalMessage();
	/**
	 * Perform the receiver using a MessageDispatcher and a MessageReceiver. Answer the result.
	 */
	public void dispatchToUsing(MessageReceiver receiver,MessageDispatcher dispatcher);
	/**
	 * Resolve the access path of the message (or its components in case of a MessageSequence)
	 * and then dispatch that message to the MessageReceiver found.
	 */
	public void resolveAndDispatchToUsing(ApplicationModel model, MessageDispatcher dispatcher);
	/**
	 * The component that triggers an event want to pass an argument.
	 * A message should add this argument or place it into its reserved slot at @index.
	 * @param index int
	 * @param eventArgument Object
	 * @return Message the receiver
	 */
	public Message setArgument(int index, Object eventArgument);
}
