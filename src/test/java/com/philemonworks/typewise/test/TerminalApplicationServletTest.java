package com.philemonworks.typewise.test;

import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.message.MessageSend;

/**
 * This class is for local testing of the PerformServlet
 */
public class TerminalApplicationServletTest extends junit.framework.TestCase {

	public TestApplicationServlet servlet;

	public TerminalApplicationServletTest(String name) {
		super(name);
	}
	public void setUp() {
		servlet = new TestApplicationServlet();
		// do the init ourselves because I do not know how to create a ServletConfig implementor (which?)
		servlet.setApplicationModel(new SampleTerminalApp(null));
	}
	public void testUndefined(){
		// For those who are scared bij red error messages in the console.
		String operation = "undefined";
		Object[] params = new Object[0];
		MessageSend msg = new MessageSend(MessageConstants.SERVER,operation);
		boolean caught =false;
		try {
		    new MessageDispatcher().resolveAndDispatchMessageTo(msg,servlet.getApplicationModel());
		} catch (RuntimeException ex){
		    caught = true;
		}
		assertTrue(caught);
	}	
}
