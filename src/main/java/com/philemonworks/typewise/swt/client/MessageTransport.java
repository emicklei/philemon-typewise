package com.philemonworks.typewise.swt.client;

import org.apache.commons.httpclient.auth.CredentialsProvider;
import com.philemonworks.typewise.message.Message;

/**
 * MessageTransport is an interface of objects that can post Message instances
 * to the server using BWA encoded requests and responses.
 *
 * @author E.M.Micklei
 */
public interface MessageTransport {
	/**
	 * Send the request message to the receiver and return the reply message.
	 * @param serverMessage Message
	 * @return Message
	 */
	public Message post(Message serverMessage);
	
	public void setServletURL(String contextURL);
	
	public void setCredentialsProvider(CredentialsProvider provider);
	
	public void setProxy(String host, int port);
}
