/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) copyright PhilemonWorks B.V. 2004,2005 - All rights reserved.
 * Use, duplication or disclosure restricted. See http://www.philemonworks.com
 */
package com.philemonworks.typewise.message;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * MessageSequence is a Composite of Message objects. It holds an ordered collection of instance Message implementors
 * 
 * @author emicklei
 */
public class MessageSequence implements BinaryAccessible, Message, Serializable {
	private static final long serialVersionUID = 3617297822136218679L;
	private ArrayList messages = new ArrayList();

	public boolean isEmpty() {
		return messages.isEmpty();
	}
	public void add(Message aMessage) {
		messages.add(aMessage);
	}
	public ArrayList getMessages() {
		return messages;
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((MessageSequence) this);
	}
	/**
	 * Set the message for the receiver. This argument cannot be null.
	 * 
	 * @param messages :
	 *        List
	 */
	public void setMessages(ArrayList messages) {
		this.messages = messages;
	}
	/**
	 * Add a Message to the ordered list of Message and return the receiver.
	 * 
	 * @param nextMessage :
	 *        Message
	 * @return MessageSequence : that's me
	 */
	public Message asSequenceWith(Message nextMessage) {
		this.add(nextMessage);
		return this;
	}
	/**
	 * Convert the receiver to a Sequence.
	 * 
	 * @return this because we are one.
	 */
	public MessageSequence asSequence() {
		// No clone
		return this;
	}
	/**
	 * If possible convert the receiver to a single Message otherwise answer the receiver. This is an optimization
	 * feature.
	 * 
	 * @return Message
	 */
	public Message asMinimalMessage() {
		if (messages.size() == 1) {
			return (Message) messages.get(0);
		} else
			return this;
	}
	/**
	 * Return the message from the list of messages
	 * 
	 * @param index :
	 *        int must be in array bounds
	 * @return Message
	 */
	public Message getMessage(int index) {
		return (Message) messages.get(index);
	}
	/**
	 * @return a String representation of the receiver for debugging purposes only.
	 */
	public String toString() {
		return DebugExplainer.explainFor(this,null);
	}
	/**
	 * @see Message
	 */
	public void dispatchToUsing(MessageReceiver receiver, MessageDispatcher dispatcher) {
		dispatcher.dispatchMessageSequenceTo(this, receiver);
	}
	/**
	 * @see Message
	 */
	public void resolveAndDispatchToUsing(ApplicationModel model, MessageDispatcher dispatcher) {
		dispatcher.resolveAndDispatchMessageTo(this, model);
	}
	public Message setArgument(int index, Object eventArgument) {
		for (Iterator iter = messages.iterator(); iter.hasNext();) {
			((Message) iter.next()).setArgument(index, eventArgument);
		}
		return this;
	}
}
