package com.philemonworks.typewise.swt.client;

import org.apache.commons.httpclient.auth.CredentialsProvider;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.typewise.server.SessionInvalidationRequestNotification;
import com.philemonworks.typewise.server.WidgetFactory;

/**
 * LocalMessageTransport is used by a client that runs in the same JVM as the server.
 *
 * @author E.M.Micklei
 */
public class LocalMessageTransport implements MessageTransport {
	private ApplicationModel model;
	private WidgetFactory widgetFactory;
	
	/**
	 * Creates a new Transport that invokes methods on an ApplicationModel
	 * @param newModel ApplicationModel
	 * @param newWidgetFactory WidgetFactory used to create widget instances
	 */
	public LocalMessageTransport(ApplicationModel newModel,WidgetFactory newWidgetFactory){
		super();
		this.model = newModel; 
		this.widgetFactory = newWidgetFactory;
	}
	/**
	 * Although this transport is meant for Java invocations inside the same JVM,
	 * the mesage is marshalled and unmarshalled to prevent any side effects when dispatching the message
	 * client or server side. The marshalling is also needed to allow a different WidgetFactory to be used when
	 * creating widgets on the client.
	 */
	public Message post(Message serverMessage) {		
		Message messagesOnClient = null;
		byte[] sendBWA = BinaryWidgetAccessor.encode(serverMessage);
		Message messagesOnServer= (Message)BinaryWidgetAccessor.decode(sendBWA);
		// invoke
		try {
			new MessageDispatcher().resolveAndDispatchMessageTo(messagesOnServer.asSequence(), model);
			// take reply and marshall-unmarshall it using the client widget factory
			byte[] receivedBWA = BinaryWidgetAccessor.encode(model.getView().takeReply());
			messagesOnClient = (Message)BinaryWidgetAccessor.decode(receivedBWA, widgetFactory);			
		} catch (SessionInvalidationRequestNotification sirn){
			// Client has disconnected
		}
		return messagesOnClient;
	}
	public void setServletURL(String contextURL) {
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.swt.client.MessageTransport#setCredentialsProvider(org.apache.commons.httpclient.auth.CredentialsProvider)
	 */
	public void setCredentialsProvider(CredentialsProvider provider) {
		// never needed for local transport
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.swt.client.MessageTransport#setProxy(java.lang.String, int)
	 */
	public void setProxy(String host, int port) {
		// never needed for local transport
		
	}
}
