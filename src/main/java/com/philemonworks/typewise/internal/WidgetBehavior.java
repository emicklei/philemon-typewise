package com.philemonworks.typewise.internal;

import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.message.MessageSequence;

public interface WidgetBehavior {
	/**
	 * Add client messages to aSequence that will install all event connections specified for the receiver
	 * 
	 * @param aSequence :
	 *        the message sequence containing Message objects
	 */
	public void addEventConnectionsTo(MessageSequence aSequence);
	public void addStateMessagesTo(MessageSequence messageSequence);
	/**
	 * Determines whether instance of this class can trigger the event. Throws a RuntimeException when it does not
	 * (because it is considered a fatal error).
	 * 
	 * @param eventID :
	 *        event contant
	 * @see CWT
	 */
	public void checkTriggersEvent(byte eventID);
}
