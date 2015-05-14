/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.message;

import com.philemonworks.typewise.cwt.Screen;

/**
 * MessageFactory is a helper class that can create Message (implementor) instances
 * 
 * @author emicklei
 */
public class MessageFactory implements MessageConstants {
	/**
	 * Answer a Message that will remove an existing widget from the current Screen. The widget can be found using its
	 * name.
	 * 
	 * @param widgetName
	 *        String
	 * @return Message
	 */
	public static Message removeWidget(String widgetName) {
		MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_remove);
		msg.addArgument(widgetName);
		return msg;
	}
	/**
	 * Answer a Message that will handle an error message.
	 * 
	 * @param userNotification
	 *        String
	 * @deprecated
	 * @return Message
	 */
	public static Message handleError(String userNotification) {
		MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_handleError);
		msg.addArgument(userNotification);
		return msg;
	}
	/**
	 * Answer a Message that will show aScreen on a Terminal
	 * 
	 * @param aScreen
	 *        Screen
	 * @return Message
	 */
	public static Message showScreen(Screen aScreen) {
		MessageSequence sequence = new MessageSequence();
		MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_SHOW);
		msg.addArgument(aScreen);
		sequence.add(msg);
		aScreen.addStateMessagesTo(sequence);
		aScreen.addEventConnectionsTo(sequence);
		return sequence;
	}
	/**
	 * Answer a client-message that will open a Viewer on the argument url
	 * 
	 * @param url
	 * @return Message
	 * @param url
	 *        String
	 * @return Message
	 */
	public static Message openURL(String url) {
		return new IndexedMessageSend(CLIENT, OPERATION_OPENURL).addArgument(url);
	}
	/**
	 * This is a server-message; see also addWidget(String) The widget will be centered on the Terminal
	 * 
	 * @param widgetAccessOperationName
	 * @return Message
	 */
	public static Message addCenteredWidget(String widgetAccessOperationName) {
		MessageSend msg = new IndexedMessageSend(SERVER, OPERATION_messageForAddingCenteredWidgetAccessedBy);
		msg.addArgument(widgetAccessOperationName);
		return msg;
	}
	
}
