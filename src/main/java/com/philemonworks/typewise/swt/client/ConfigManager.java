package com.philemonworks.typewise.swt.client;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import javax.jnlp.BasicService;
import javax.jnlp.FileContents;
import javax.jnlp.PersistenceService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

/**
 * ConfigManager is the facade to properties that are stored locally such as preferred font and networksettings.
 * 
 * @author E.M.Micklei
 */
public class ConfigManager {
	private static final Logger LOG = Logger.getLogger(ConfigManager.class);
	
	private static final String KEY_FONTNAME = "fontname";
	private static final String KEY_FONTSIZE = "fontsize";
	private static final String KEY_FONTSTYLE = "fontstyle";
	private static String PROXYSERVER = null;
	private static int PROXYPORT = 80;
	private static final String KEY_PROXYLIST = "javaplugin.proxy.config.list";
	private static final String KEY_CREDENTIALS = "credentials.";
	private static Properties map = new Properties();
	static {
		load();
	}

	/**
	 * @return the name of the current font.
	 */
	public static String fontName() {
		String name = (String) map.get(KEY_FONTNAME);
		return name == null ? "Lucida Console" : name;
	}
	/**
	 * @return the height for the current font
	 */
	public static int fontSize() {
		String sizeString = (String) map.get(KEY_FONTSIZE);
		return sizeString == null ? 10 : Integer.parseInt(sizeString);
	}
	/**
	 * @return the style for the current font
	 */
	public static int fontStyle() {
		String styleString = (String) map.get(KEY_FONTSTYLE);
		return styleString == null ? SWT.NORMAL : Integer.parseInt(styleString);
	}
	/**
	 * Font has been changed. Store the changes.
	 * 
	 * @param newFont
	 *        Font
	 */
	public static void setFont(Font newFont) {
		FontData[] data = newFont.getFontData();
		if (data.length == 0)
			return;
		map.put(KEY_FONTNAME, data[0].getName());
		map.put(KEY_FONTSIZE, String.valueOf(data[0].getHeight()));
		map.put(KEY_FONTSTYLE, String.valueOf(data[0].getStyle()));
		store();
	}
	/**
	 * If the server is empty (again) then remove proxy info
	 * 
	 * @param server
	 * @param port
	 */
	public static void setProxy(String server, int port) {
		LOG.debug("Setting proxy to " + server + ":" + port);
		if (server.length() == 0) {
			PROXYSERVER = null;
			PROXYPORT = 80;
		} else {
			PROXYSERVER = server;
			PROXYPORT = port;
		}
	}
	/**
	 * Pre: hasProxySettings
	 * @return host
	 */
	public static String getProxyServer() {
		return PROXYSERVER;
	}
	/**
	 * Pre: hasProxySettings
	 * @return port
	 */	
	public static int getProxyPort() {
		return PROXYPORT;
	}
	/**
	 * Detect whether proxy settings are available
	 * @return true if there are
	 */
	public static boolean hasProxySettings() {
		String list = System.getProperty(KEY_PROXYLIST);
		if (list == null || list.length() == 0) return false;
		String[] pairs = list.split(",");
		for (int i = 0; i < pairs.length; i++) {
			String pair = pairs[i];
			String[] keyvalue = pair.split("=");
			if ("http".equals(keyvalue[0])){
				String[] hostport = keyvalue[1].split(":");
				ConfigManager.setProxy(hostport[0],Integer.parseInt(hostport[1]));
				return true;
			}
		}
		return false;
	}
	/**
	 * Store credentials
	 * @param aCredentials
	 */
	public static void setCredentials(Credentials aCredentials) {
		Logger.getLogger(ConfigManager.class).debug("storing credentials");
		map.put(KEY_CREDENTIALS + aCredentials.host + ".domain", aCredentials.domain);
		map.put(KEY_CREDENTIALS + aCredentials.host + ".realm", aCredentials.realm);
		map.put(KEY_CREDENTIALS + aCredentials.host + ".username", aCredentials.username);
		map.put(KEY_CREDENTIALS + aCredentials.host + ".password", aCredentials.password);
		map.put(KEY_CREDENTIALS + aCredentials.host + ".port", String.valueOf(aCredentials.port));
		store();
	}
	private static List storedCredentials() {
		List credentials = (List) map.get(KEY_CREDENTIALS);
		if (credentials == null)
			credentials = new ArrayList();
		return credentials;
	}
	/**
	 * Retrieve credentials by host
	 * @param host
	 * @param port
	 * @param realm
	 * @return Credentials
	 */
	public static Credentials getCredentiatials(String host, int port, String realm) {
		// currently, assume host is qualifying enough
		String key = KEY_CREDENTIALS + host;
		String username = (String) map.get(key + ".username");
		// if not in cache then return nothing
		if (username == null)
			return null;
		Credentials creds = new Credentials();
		creds.username = username;
		creds.domain = (String) map.get(key + ".domain");
		creds.realm = (String) map.get(key + ".realm");
		creds.password = (String) map.get(key + ".password");
		creds.port = Integer.parseInt((String) map.get(key + ".port"));
		return creds;
	}
	private synchronized static void store() {
		PersistenceService ps = null;
		BasicService bs = null;
		try {
			ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
			bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			Logger.getLogger(ConfigManager.class).debug("PersistentService:" + ps);
		} catch (UnavailableServiceException e) {
			Logger.getLogger(ConfigManager.class).warn("PersistentService unavailable");
			return;
		}
		// try {
		// create data
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			map.store(output, "TypeWise Client Configuration");
		} catch (IOException e) {			
			throw new RuntimeException("Unable to store map", e);
		}
		// store or overwrite data
		URL codebase = bs.getCodeBase();
		try {
			ps.get(codebase);
			ps.delete(codebase);
		} catch (FileNotFoundException fe) {
			// 
		} catch (IOException ex) {
			throw new RuntimeException("Unable to delete codebase:" + codebase, ex);
		}
		try {
			ps.create(codebase, output.size());
		} catch (MalformedURLException e) {
			throw new RuntimeException("Bad url:" + codebase, e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to create codebase:" + codebase, e);
		}
		try {
			FileContents fc = ps.get(codebase);
			OutputStream os = fc.getOutputStream(false);
			os.write(output.toByteArray());
			os.close();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Unable to write binary map", e);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Unable to write binary map", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to write binary map", e);
		}
	}
	private synchronized static void load() {
		if (LOG.isDebugEnabled()){
			Enumeration enummie = System.getProperties().keys();
			while (enummie.hasMoreElements()){
				String key = (String)enummie.nextElement();
				LOG.debug("System["+key+"]="+(System.getProperty(key)));
			}
		}
		PersistenceService ps = null;
		BasicService bs = null;
		try {
			ps = (PersistenceService) ServiceManager.lookup("javax.jnlp.PersistenceService");
			bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
			LOG.debug("PersistentService:" + ps);
		} catch (UnavailableServiceException e) {
			LOG.debug("PersistentService unavailable", e);
		}
		try {
			URL codebase = bs.getCodeBase();
			FileContents fc = ps.get(codebase);
			InputStream input = fc.getInputStream();
			map.load(input);
			input.close();
		} catch (Exception ex) {
			LOG.debug("config unreadable, creating a new:");
			map = new Properties();
		}
	}
}
