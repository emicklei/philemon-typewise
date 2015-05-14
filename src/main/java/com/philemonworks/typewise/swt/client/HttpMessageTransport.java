/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 17-okt-2004: created
 *
 */
package com.philemonworks.typewise.swt.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.typewise.server.WidgetFactory;

/**
 * @author emicklei
 *
 * @version 17-okt-2004
 */
public class HttpMessageTransport implements MessageTransport {
	HttpClient client = new HttpClient();
    private String servletURL; 
	private final WidgetFactory factory = new SwtWidgetFactory();
	/**
	 * @param servletUrl
	 */
	public HttpMessageTransport(String servletUrl){
		super();
		this.servletURL = servletUrl;
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.client.message.MessageTransport#post(com.philemonworks.typewise.message.Message)
	 */
	public Message post(Message serverMessage) {
		PostMethod post = new PostMethod(servletURL);
		byte[] bwa = BinaryWidgetAccessor.encode(serverMessage);
		InputStreamRequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(bwa));
		post.setRequestEntity(entity);
		post.setDoAuthentication( true );
		try {
			client.executeMethod(post);
			return (Message)BinaryWidgetAccessor.decode(post.getResponseBody(),factory);			
		} catch (HttpException e) {
			ErrorUtils.error(this,e,"Unable to send: " + serverMessage);
		} catch (IOException e) {
			ErrorUtils.error(this,e,"Unable to send: " + serverMessage);
		} finally {
			post.releaseConnection();
		}
		throw new RuntimeException("post error");
	}
	/**
	 * @param servletURL The servletURL to set.
	 */
	public void setServletURL(String servletURL) {
		this.servletURL = servletURL;
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.swt.client.MessageTransport#setCredentialsProvider(org.apache.commons.httpclient.auth.CredentialsProvider)
	 */
	public void setCredentialsProvider(CredentialsProvider provider) {
		client.getParams().setParameter(CredentialsProvider.PROVIDER, provider);		
	}
	public void setProxy(String host,int port) {
		Logger.getLogger(this.getClass()).debug("Setting proxy host:port="+host+":"+port);
		client.getHostConfiguration().setProxy(host, port);
    }
}
