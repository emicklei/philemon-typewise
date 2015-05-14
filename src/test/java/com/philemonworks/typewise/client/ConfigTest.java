package com.philemonworks.typewise.client;

import junit.framework.TestCase;
import com.philemonworks.typewise.swt.client.ConfigManager;

public class ConfigTest extends TestCase {
	public void testStoreLoad(){
		if (true) return;
		ConfigManager.setProxy("proxytest",42);
		assertEquals("server","proxytest",ConfigManager.getProxyServer());
		assertEquals("port",42,ConfigManager.getProxyPort());
	}	
}
