package com.philemonworks.typewise.server;

public interface IExceptionHandler {
	public void handleException(Object catcher, Exception ex, String message);
}
