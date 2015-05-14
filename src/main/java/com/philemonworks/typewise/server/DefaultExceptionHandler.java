/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 *
 */package com.philemonworks.typewise.server;

import com.philemonworks.typewise.internal.ErrorUtils;

public class DefaultExceptionHandler implements IExceptionHandler {
	public void handleException(Object catcher, Exception ex, String message) {
		ErrorUtils.error(catcher.getClass(), ex, message);
	}
}
