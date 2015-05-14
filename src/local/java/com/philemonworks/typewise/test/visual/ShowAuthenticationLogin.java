package com.philemonworks.typewise.test.visual;

import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.BasicScheme;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.Welcome;

public class ShowAuthenticationLogin {
	public static void main(String[] args) {
		SwtApplicationView view = new SwtApplicationView();
		try {
			AuthScheme scheme = new TestAuthScheme("dummy");
			view.getCredentials(scheme, "host", 8080, true);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		view.start(new Welcome(null),"main");
	}
}
