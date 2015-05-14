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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.server.SessionInvalidationRequestNotification;

/**
 * The MessageDispatcher can perform all implementors of the Message interface.
 * 
 * @author E.M.Micklei
 */
public class MessageDispatcher implements MessageConstants {
	public static final Logger LOG = Logger.getLogger(MessageDispatcher.class);
	private static final Object NOARGUMENT = new Object();
	private Object firstArgument = NOARGUMENT;

	public MessageDispatcher() {
		super();
	}
	public MessageDispatcher(Object oneArgument) {
		super();
		firstArgument = oneArgument;
	}
	/**
	 * Answer whether the parameter receiver can be invoked with the method named operationName This check does not
	 * consider the parameter types.
	 */
	public static boolean canUnderstand(MessageReceiver receiver, String operationName) {
		return MessageDispatcher.findMethodNamed(receiver.getClass(), operationName) != null;
	}
	/**
	 * Find a method named <b>operationName</b> in the class hierarchy of <b>receiverClass</b>.
	 * 
	 * @param receiverClass =
	 *        the class in which to start searching
	 * @param operationName =
	 *        the name of the method to search
	 * @return java.reflect.Method
	 */
	private static Method findMethodNamed(Class receiverClass, String operationName) {
		if (receiverClass == null)
			return null;
		Method[] allMethods = receiverClass.getMethods();
		for (int m = 0; m < allMethods.length; m++) {
			Method each = allMethods[m];
			if (each.getName().equals(operationName))
				return each;
		}
		return findMethodNamed(receiverClass.getSuperclass(), operationName);
	}
	public void resolveAndDispatchMessageTo(MessageSend message, ApplicationModel model) {
		MessageReceiver receiver = this.resolveReceiverNamedIn(message.getReceiver(), model);
		if (receiver == null) {
			ErrorUtils.error(this, "Unable to find object: " + message.getReceiver() + " in:" + model);
			return;
		}
		this.dispatchMessageSendTo(message, receiver);
	}
	/**
	 * Each receiver is specified by an identifier that needs to be resolved in the context of the ApplicationModel.
	 * When each target is found then dispatch the message to the model using the dispatcher (myself).
	 * 
	 * @param sequence :
	 *        MessageSequence
	 * @param model :
	 *        ApplicationModel
	 */
	public void resolveAndDispatchMessageTo(MessageSequence sequence, ApplicationModel model) {
		for (int i = 0; i < sequence.getMessages().size(); i++) {
			((Message) sequence.getMessages().get(i)).resolveAndDispatchToUsing(model, this);
		}
	}
	/**
	 * The receiver of the message is specified by an identifier that needs to be resolved in the context of the
	 * ApplicationModel. When the target is found then dispatch the message to the model using the dispatcher (myself).
	 * 
	 * @param message :
	 *        MessageSend to be dispatched
	 * @param model :
	 *        ApplicationModel is or hold the receiver
	 */
	public void resolveAndDispatchMessageTo(IndexedMessageSend message, ApplicationModel model) {
		MessageReceiver receiver = this.resolveReceiverNamedIn(message.getReceiver(), model);
		if (receiver == null) {
			ErrorUtils.error(this, "Unable to find object:" + message.getReceiver() + " in:" + model);
			return;
		}
		this.dispatchIndexedMessageSendTo(message, receiver);
	}
	/**
	 * Dispatch the message to a MessageReceiver. Let the message take care of this using the me.
	 * 
	 * @param message :
	 *        Message
	 * @param receiver :
	 *        MessageReceiver
	 */
	public void dispatchMessageTo(Message message, MessageReceiver receiver) {
		message.dispatchToUsing(receiver, this);
	}
	/**
	 * Each receiver is specified by an identifier that needs to be resolved in the context of the MessageReceiver. When
	 * each target is found then dispatch the message to the model using the dispatcher (myself).
	 * 
	 * @param sequence :
	 *        MessageSequence
	 * @param receiver :
	 *        MessageReceiver
	 */
	public void dispatchMessageSequenceTo(MessageSequence sequence, MessageReceiver receiver) {
		if (receiver == null)
			return;
		for (int i = 0; i < sequence.getMessages().size(); i++) {
			((Message) sequence.getMessages().get(i)).dispatchToUsing(receiver, this);
		}
	}
	/**
	 * Each receiver is specified by an identifier that needs to be resolved in the context of the MessageReceiver. When
	 * each target is found then dispatch the message to the model using the dispatcher (myself).
	 * 
	 * @param message :
	 *        IndexedMessageSend
	 * @param receiver :
	 *        MessageReceiver
	 */
	public void dispatchIndexedMessageSendTo(IndexedMessageSend message, MessageReceiver receiver) {
		if (receiver == null)
			return;
		// Replace argument if passed by the receiver
		if (firstArgument != NOARGUMENT)
			message.setArgument(0, firstArgument);
		if (LOG.isDebugEnabled()) {
			LOG.debug("[server] " + DebugExplainer.explainFor(message, receiver));
		}
		receiver.dispatch(message);
		// Remove replaced argument
		if (firstArgument != NOARGUMENT)
			message.setArgument(0, null);
	}
	public void dispatchMessageSendTo(MessageSend message, MessageReceiver receiver) {
		if (receiver == null) {
			LOG.warn("[server] no receiver for message:" + message.getOperation());
			return;
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("[server] " + DebugExplainer.explainFor(message,receiver));
		}
		// Prepare parameters
		// Replace argument if passed by the receiver
		if (firstArgument != NOARGUMENT)
			message.setArgument(0, firstArgument);
		String operationName = message.getOperation();
		Object result = null;
		Method method = null;
		Object[] parameters = message.getArguments();
		Class[] paramTypes = new Class[parameters.length];
		for (int count = 0; count < parameters.length; count++) {
			if (parameters[count] == null) {
				paramTypes[count] = new Object().getClass();
			} else {
				paramTypes[count] = parameters[count].getClass();
			}
		}
		// Find method
		try {
			method = this.lookupMethod(receiver, operationName, paramTypes);
		} catch (NoSuchMethodException ex) {
			ErrorUtils.handleNoSuchMethod(ex,receiver,operationName,paramTypes);
		}
		// Invoke the method
		try {
			result = method.invoke(receiver, parameters);
		} catch (IllegalAccessException ex) {
			result = ErrorUtils.handleInvocationError(ex, "IllegalAccessException:", receiver, operationName, paramTypes);
		} catch (IllegalArgumentException ex) {
			result = ErrorUtils.handleInvocationError(ex, "IllegalArgumentException:", receiver, operationName, paramTypes);
		} catch (InvocationTargetException ex) {
			// Thrown when invoked method throws an exception
			if (ex.getTargetException() instanceof SessionInvalidationRequestNotification) {
				throw new SessionInvalidationRequestNotification();
			}
			result = ErrorUtils.handleInvocationError(ex, "InvocationTargetException:", receiver, operationName, paramTypes);
		}
		// Remove replaced argument
		if (firstArgument != NOARGUMENT)
			message.setArgument(0, null);
		return;
	}
	/**
	 * Do a VM-like method lookup.
	 * 
	 * @param receiver
	 * @param operationName
	 * @param paramTypes
	 * @return java.lang.reflect.Method
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public Method lookupMethod(Object receiver, String operationName, Class[] paramTypes) throws NoSuchMethodException, SecurityException {
		Method method = this.lookupMethod(receiver.getClass(), operationName, paramTypes);
		if (method == null) {
			// Handle case of null-argument
			method = MessageDispatcher.findMethodNamed(receiver.getClass(), operationName);
			if (method == null)
				throw new NoSuchMethodException(receiver + " does not understand: " + operationName);
		}
		return method;
	}
	public Method lookupMethod(Class receiverClass, String operationName, Class[] paramTypes) {
		if (receiverClass == null)
			return null;
		Method method = null;
		try {
			method = receiverClass.getMethod(operationName, paramTypes);
		} catch (NoSuchMethodException ex) {
			method = this.lookupMethod(receiverClass.getSuperclass(), operationName, paramTypes);
		}
		return method;
	}
	private MessageReceiver resolveReceiverNamedIn(String accessPath, ApplicationModel model) {
		// TODO: treat as path + rename this method
		if (SERVER.equals(accessPath)) {
			if (model.view.getCurrentScreen() == null)
				return model;
			return model.view.getCurrentScreen().getModel(); // This looks weird and therefore needs attention (em)
		}
		if (CLIENT.equals(accessPath))
			return model.view;
		return (MessageReceiver) (model.view.getCurrentScreen().widgetNamed(accessPath));
	}
}