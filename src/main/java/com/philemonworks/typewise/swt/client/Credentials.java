package com.philemonworks.typewise.swt.client;

import java.io.Serializable;

/**
 * Credentials is a persistent object to the ConfigManager to remember access.
 * 
 * @author E.M.Micklei
 */
public class Credentials implements Serializable {
	public String host;
	public int port;
	public String realm;
	public String username;
	public String password;
	public String domain;

	public boolean equals(Object otherCredentials) {
		if (!(otherCredentials instanceof Credentials))
			return false;
		Credentials other = (Credentials) otherCredentials;
		return host.equals(other.host) && (realm.equals(other.realm) && (port == other.port));
	}
}
