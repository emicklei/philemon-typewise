/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 7-mrt-2005: created
 *
 */
package com.philemonworks.typewise.server;

/**
 * SessionInvalidationRequestNotification is a special RuntimeException that can cause the current session to be
 * invalidated when thrown.
 * 
 * @author E.M.Micklei
 */
public class SessionInvalidationRequestNotification extends RuntimeException {
	/**
	 * Default constructor
	 */
	public SessionInvalidationRequestNotification() {
		super();
	}
	/**
	 * Constructor with a message argument
	 * 
	 * @param message :
	 *        String
	 */
	public SessionInvalidationRequestNotification(String message) {
		super(message);
	}
	/**
	 * Constructor with the cause Throwable argument
	 * 
	 * @param cause :
	 *        Throwable
	 */
	public SessionInvalidationRequestNotification(Throwable cause) {
		super(cause);
	}
	/**
	 * Constructor with both the cause Throwable and a message argument
	 * 
	 * @param message :
	 *        String
	 * @param cause :
	 *        Throwable
	 */
	public SessionInvalidationRequestNotification(String message, Throwable cause) {
		super(message, cause);
	}
}
