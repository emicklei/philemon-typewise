package com.philemonworks.typewise.swt.client;

import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.IExceptionHandler;
import com.philemonworks.typewise.swt.SwtScreen;

/**
 * SwtMessageDispatcher can handle Message objects send to and from client widgets and server models. For messages from
 * the client to the server, the dispatcher uses a MessageTransport implementor.
 * 
 * @author E.M.Micklei
 */
public class SwtMessageDispatcher implements MessageConstants {
	private final static Logger LOG = Logger.getLogger(SwtMessageDispatcher.class);
	private IExceptionHandler exceptionHandler = null; // will be requested from ApplicationModel
	SwtApplicationView view;
	MessageTransport transport;
	String servletURL;

	/**
	 * Create a new dispatcher using a view.
	 * 
	 * @param view
	 *        SwtApplicationView
	 */
	public SwtMessageDispatcher(SwtApplicationView view) {
		super();
		this.view = view;
	}
	/**
	 * Initialize the transport attribute using an object that can be used for colocated client-server operation (such
	 * as testing) It invokes methods directly on the ApplicationModel.
	 * 
	 * @param model
	 *        ApplicationModel
	 */
	public void initializeForLocal(ApplicationModel model) {
		transport = new LocalMessageTransport(model, new SwtWidgetFactory());
	}
	/**
	 * Initialize the transport attribute using an object that can use the Http protocol for transferring messages to
	 * and from the server application. The servletURL may change if the client switches to another application.
	 * 
	 * @param baseURL
	 *        the absolute URL to the ApplicationServlet
	 * @param provider
	 *        CredentialsProvider that receives the callback is user/pwd is needed
	 */
	public void initializeForRemote(String baseURL, CredentialsProvider provider) {
		transport = new HttpMessageTransport(baseURL);
		transport.setCredentialsProvider(provider);
		if (ConfigManager.hasProxySettings())
			transport.setProxy(ConfigManager.getProxyServer(), ConfigManager.getProxyPort());
	}
	/**
	 * Send the message to the correct receiver object.
	 * 
	 * @param aMessage
	 *        Message
	 */
	public void dispatch(Message aMessage) {
		// Check whether a Message was installed for the causing event.
		if (aMessage == null)
			return;
		if (aMessage instanceof IndexedMessageSend) {
			this.dispatch((IndexedMessageSend) aMessage);
			return;
		}
		if (aMessage instanceof MessageSend) {
			this.dispatch((MessageSend) aMessage);
			return;
		}
		if (aMessage instanceof MessageSequence) {
			this.dispatch((MessageSequence) aMessage);
			return;
		}
	}
	/**
	 * Perform the message locally or remotely using the transport
	 * 
	 * @param msg
	 *        MessageSend
	 */
	private void dispatch(MessageSend msg) {
		if (msg.getReceiver().equals(CLIENT)) {
			throw new RuntimeException("todo");
		} else if (msg.getReceiver().equals(SERVER)) {
			this.postMessage(msg);
		} else {
			if (LOG.isDebugEnabled())
				LOG.debug("[client] " + DebugExplainer.explain(msg));
			// resolve to client widget
			Widget widgetOrNull = (Widget) view.getScreen().widgetNamed(msg.getReceiver());
			if (widgetOrNull == null) {
				Logger.getLogger(SwtMessageDispatcher.class).error(msg);
			} else {
				new MessageDispatcher().dispatchMessageSendTo(msg, widgetOrNull);
			}
		}
	}
	private void dispatch(MessageSequence msg) {
		for (int m = 0; m < msg.getMessages().size(); m++)
			this.dispatch((Message) msg.getMessages().get(m));
	}
	/**
	 * @todo handle server messages
	 * @param msg
	 */
	private void dispatch(IndexedMessageSend msg) {
		if (msg.getReceiver().equals(CLIENT)) {
			if (LOG.isDebugEnabled())
				LOG.debug("[client] " + DebugExplainer.explainFor(msg, view));
			view.dispatch(msg);
		} else if (msg.getReceiver().equals(SERVER)) {
			this.postMessage(msg);
		} else {
			// resolve to client widget
			Widget widgetOrNull = (Widget) view.getScreen().widgetNamed(msg.getReceiver());
			if (widgetOrNull == null) {
				LOG.error(msg);
			} else {
				if (LOG.isDebugEnabled())
					LOG.debug("[client] " + DebugExplainer.explainFor(msg, widgetOrNull));
				widgetOrNull.dispatch(msg);
			}
		}
	}
	private void dispatchClientIndexedMessage(IndexedMessageSend msg) {
		switch (msg.getIndex()) {
		case OPERATION_OPENURL:
			// TODO should check argument for leading slash
			view.openURL(this.servletURL + "/../" + (String) msg.getArgument());
			break;
		case OPERATION_GOTOURL:
			view.gotoURL((String) msg.getArgument());
			break;
		case OPERATION_handleError:
			view.show(Dialog.error((String) msg.getArgument()));
			break;
		case OPERATION_SHOW:
			view.show((SwtScreen) msg.getArgument());
			break;
		case OPERATION_SHOWDIALOG:
			view.show((Dialog) msg.getArgument());
			break;
		case OPERATION_OK:
			break;
		case OPERATION_STATUS:
			view.setStatus((String) msg.getArgument());
			break;
		case OPERATION_clearAllDirty:
			view.getScreen().clearDirtyAll();
			break;
		case OPERATION_close:
			view.getShell().dispose();
			break;
		default:
			throw new RuntimeException("Unable to dispatch:" + DebugExplainer.explainFor(msg, null));
		}
	}
	/**
	 * Construct a new MessageSequence to send to the server that will update the Screen state and handles the
	 * postMessage
	 * 
	 * @param postMessage
	 *        Message
	 */
	public void postMessage(Message postMessage) {
		MessageSequence seq = new MessageSequence();
		if (view.getScreen() != null) {
			view.getScreen().addStateMessagesTo(seq);
			seq.add(SERVER_CLEARDIRTYALL);
		}
		seq.add(postMessage);
		this.postMessageOnly(seq);
	}
	/**
	 * Direclty post a Message (no state synchronization messages are collected).
	 * 
	 * @param postMessage
	 *        Message
	 */
	public void postMessageOnly(Message postMessage) {
		view.showBusyCursor();
		try {
			this.dispatch(transport.post(postMessage.asSequence()));
		} finally {
			view.hideBusyCursor();
		}
	}
}
