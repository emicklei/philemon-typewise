package com.philemonworks.typewise.test.visual;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.auth.AuthSchemeBase;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.auth.MalformedChallengeException;

public class TestAuthScheme extends AuthSchemeBase {
	public TestAuthScheme(String arg0) throws MalformedChallengeException {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#authenticate(org.apache.commons.httpclient.Credentials, org.apache.commons.httpclient.HttpMethod)
	 */
	public String authenticate(Credentials arg0, HttpMethod arg1) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#authenticate(org.apache.commons.httpclient.Credentials, java.lang.String, java.lang.String)
	 */
	public String authenticate(Credentials arg0, String arg1, String arg2) throws AuthenticationException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#getID()
	 */
	public String getID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#getParameter(java.lang.String)
	 */
	public String getParameter(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#getRealm()
	 */
	public String getRealm() {
		// TODO Auto-generated method stub
		return "test";
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#getSchemeName()
	 */
	public String getSchemeName() {
		// TODO Auto-generated method stub
		return "BASIC";
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#isComplete()
	 */
	public boolean isComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#isConnectionBased()
	 */
	public boolean isConnectionBased() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.apache.commons.httpclient.auth.AuthSchemeBase#processChallenge(java.lang.String)
	 */
	public void processChallenge(String arg0) throws MalformedChallengeException {
		// TODO Auto-generated method stub
		
	}
}
