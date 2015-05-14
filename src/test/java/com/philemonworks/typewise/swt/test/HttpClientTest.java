/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import junit.framework.TestCase;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidgetFactory;

/**
 * HttpClientTest is
 * 
 * @author E.M.Micklei
 */
public class HttpClientTest extends TestCase {

	public void testDummy(){
		
	}
	
	public void _testHttps() {
		/*
		 * PostMethod post = new PostMethod(strURL); // Request content will be retrieved directly // from the input
		 * stream // Per default, the request content needs to be buffered // in order to determine its length. //
		 * Request body buffering can be avoided when // content length is explicitly specified
		 * post.setRequestEntity(new InputStreamRequestEntity( new FileInputStream(input), input.length())); // Specify
		 * content type and encoding // If content encoding is not explicitly specified // ISO-8859-1 is assumed
		 * post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1"); // Get HTTP client HttpClient
		 * httpclient = new HttpClient(); // Execute request try { int result = httpclient.executeMethod(post); //
		 * Display status code System.out.println("Response status code: " + result); // Display response
		 * System.out.println("Response body: "); System.out.println(post.getResponseBodyAsString()); } finally { //
		 * Release current connection to the connection pool once you are done post.releaseConnection(); } }
		 */
		if (!System.getProperty("os.arch").equals("x86")) return;
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("https://jvds.xs4all.nl/cae/admin");
		try {
			client.executeMethod(post);
			System.out.println(BinaryWidgetAccessor.decode(post.getResponseBody()));		
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 */
	public void _testHttp() {
		if (!System.getProperty("os.arch").equals("x86")) return;
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://ntserver/cae/admin");
		MessageSend msg = new MessageSend(MessageConstants.SERVER,"main");
		msg.addArgument(new HashMap());
		byte[] bwa = BinaryWidgetAccessor.encode(msg);
		InputStreamRequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(bwa));
		post.setRequestEntity(entity);
		try {
			client.executeMethod(post);
			System.out.println(BinaryWidgetAccessor.decode(post.getResponseBody(),new SwtWidgetFactory()));			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
}
