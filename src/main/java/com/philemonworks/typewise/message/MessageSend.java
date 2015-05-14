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
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

public class MessageSend implements BinaryAccessible, Message, Serializable {
	private static final long serialVersionUID = 3761131556659804210L;
	private String receiver = null;
	private String operation = "undefined";
	private Object[] arguments = null; // this is to minimize binary representation for unary messages

	public MessageSend(String receiver, String operationName) {
		super();
		if (operationName == null)
			ErrorUtils.error(this, "operationName is null");
		if (operationName == null)
			ErrorUtils.error(this, "receiver is null");
		this.receiver = receiver;
		this.operation = operationName;
	}
	public MessageSend(MessageReceiver receiver, String operationName) {
		if (operationName == null)
			ErrorUtils.error(this, "operationName is null");
		if (operationName == null)
			ErrorUtils.error(this, "receiver is null");
		if (!MessageDispatcher.canUnderstand(receiver, operationName))
			ErrorUtils.error(this, receiver.toString() + " does not understand: " + operationName);
		this.receiver = receiver.getAccessPath();
		this.operation = operationName;
	}
	public MessageSend(MessageReceiver receiver, String operationName, Object argument) {
		this(receiver,operationName);
		this.addArgument(argument);
	}
	public MessageSend() {
		super();
		this.receiver = "";
	}
	public Object[] getArguments() {
		return arguments == null ? new Object[0] : arguments;
	}
	public Object[] getArgumentsOrNull() {
		return arguments;
	}
	/**
	 * Return the first argument of the message.
	 * Pre: at least one argument is available
	 * @return Object
	 */
	public Object getArgument() {
		return arguments[0];
	}
	public ArrayList getArgumentList() {
		ArrayList list = new ArrayList();
		Object[] myArguments = this.getArguments();
		for (int a = 0; a < myArguments.length; a++)
			list.add(myArguments[a]);
		return list;
	}
	public String getOperation() {
		return operation;
	}
	public String getReceiver() {
		return receiver;
	}
	public void setArguments(Object[] collection) {
		arguments = collection;
	}
	public void setOperation(String string) {
		operation = string;
	}
	public void setReceiver(String string) {
		receiver = string;
	}
	/**
	 * Add an argument the message send. Only Java Objects can be listed as arguments For adding primitive types (int,
	 * boolean et.al), boxing is required. Returns the receiver such that cascaded style expressions can be used.
	 */
	public MessageSend addArgument(Object arg) {
		// Lazy initialize if needed
		if (arguments == null) {
			arguments = new Object[1];
			arguments[0] = arg;
			return this;
		}
		Object[] newArguments = new Object[arguments.length + 1];
		System.arraycopy(arguments, 0, newArguments, 0, arguments.length);
		newArguments[arguments.length] = arg;
		arguments = newArguments;
		return this;
	}
	public Message setArgument(int argIndex, Object argument) {
		if (arguments == null) {
			this.addArgument(argument);
		} else {
			arguments[argIndex] = argument; // assume we have room
		}
		return this;
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((MessageSend) this);
	}
	public Message asMinimalMessage() {
		return this;
	}
	public MessageSequence asSequence() {
		MessageSequence seq = new MessageSequence();
		seq.add(this);
		return seq;
	}
	public String toString() {
		return DebugExplainer.explain(this);
	}
	public Object selector() {
		return operation;
	}
	public void dispatchToUsing(MessageReceiver receiver, MessageDispatcher dispatcher) {
		dispatcher.dispatchMessageSendTo(this, receiver);
	}
	public void resolveAndDispatchToUsing(ApplicationModel model, MessageDispatcher dispatcher) {
		dispatcher.resolveAndDispatchMessageTo(this, model);
	}
}
