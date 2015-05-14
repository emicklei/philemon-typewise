package com.philemonworks.typewise.swt.client;

import java.io.InputStream;
import java.net.URL;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * ServerAuthenticationLogin is used to get credentials from the user to transport messags to the application server.
 * 
 * @author E.M.Micklei
 */
public class ServerAuthenticationLogin extends Dialog {
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String DOMAIN = "domain";
	private boolean requiresDomain = true;
	private String host = "<host>";
	private int port = 0;
	private String realm = "<realm>";
	private boolean isProxy = false;
	private static final int LABELWIDTH = 60;
	private static final int LABELHEIGHT = 22;
	private static final int VALUEWIDTH = 180;
	private static final int VALUEHEIGHT = 22;
	private static final int BUTTONWIDTH = 80;
	private int row = 0;
	private Text user;
	private Text pwd;
	private Text dom;
	private String username;
	private String password;
	private String domain;
	private boolean isAccepted = false;

	/**
	 * For testing only
	 * @param argv
	 */
	public static void main(String[] argv) {
		new ServerAuthenticationLogin(new Shell()).open();
	}
	/**
	 * @param arg0
	 */
	public ServerAuthenticationLogin(Shell arg0) {
		super(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public ServerAuthenticationLogin(Shell arg0, int arg1) {
		super(arg0, arg1);
	}
	/**
	 * Creat a shell, add labels and textfields and open it
	 * @return whether ok was clicked and succesful
	 */
	public boolean open() {
		Shell parent = getParent();
		Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setSize(360, 260);
		String who = isProxy ? "Proxy server" : "Network";
		dialog.setText(who + " requires authentication - TypeWise");
		row = 0;
		addHeaderTo(dialog);
		addInputsTo(dialog);
		addButtonsTo(dialog);
		// dialog.pack();
		dialog.open();
		Display display = parent.getDisplay();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return isAccepted;
	}
	private void addHeaderTo(Shell dialog) {
		Label label = new Label(dialog, SWT.NULL);
		label.setText("Please type your user name and password:");
		label.setBounds(col(0), row(row++), VALUEWIDTH + LABELWIDTH, LABELHEIGHT);
		
		Label iconLabel = new Label(dialog, SWT.IMAGE_JPEG);
		iconLabel.setAlignment(SWT.LEFT);
		iconLabel.setBounds(8, 8, 34, 40);
		InputStream is;
		try {
			is = new URL(SwtUtils.urlForLocalResource("password.gif")).openStream();
			Image icon = new Image(dialog.getDisplay(), is);
			icon.getImageData();
			iconLabel.setImage(icon);
			// icon for window too
			dialog.setImage(icon);
		} catch (Exception e) {
			// bummer
			e.printStackTrace();
		}
	}
	private void addInputsTo(Shell shell) {
		Label site = new Label(shell, SWT.NULL);
		site.setText("Site:");
		site.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		Label sitevalue = new Label(shell, SWT.NULL);
		sitevalue.setText(host);
		sitevalue.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		Label realmlabel = new Label(shell, SWT.NULL);
		realmlabel.setText("Realm:");
		realmlabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		Label realmvalue = new Label(shell, SWT.NULL);
		realmvalue.setText(realm);
		realmvalue.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		Label userlabel = new Label(shell, SWT.NULL);
		userlabel.setText("User Name:");
		userlabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		user = new Text(shell, SWT.SINGLE | SWT.BORDER);
		user.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		Label pwdlabel = new Label(shell, SWT.NULL);
		pwdlabel.setText("Password:");
		pwdlabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		pwd = new Text(shell, SWT.SINGLE | SWT.BORDER);
		pwd.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		pwd.setEchoChar('*');
		if (requiresDomain) {
			Label domlabel = new Label(shell, SWT.NULL);
			domlabel.setText("Domain:");
			domlabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
			dom = new Text(shell, SWT.SINGLE | SWT.BORDER);
			dom.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		}
	}
	private int col(int index) {
		return 60 + (index * (LABELWIDTH + 20));
	}
	private int row(int index) {
		return 20 + (index * VALUEHEIGHT * 5 / 4);
	}
	private void addButtonsTo(final Shell dialog) {
		final Button buttonOK = new Button(dialog, SWT.PUSH);
		buttonOK.setText("Ok");
		buttonOK.setBounds(col(1), row(row), BUTTONWIDTH, LABELHEIGHT);
		dialog.setDefaultButton(buttonOK);
		Button buttonCancel = new Button(dialog, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.setBounds(col(1) + BUTTONWIDTH + 10, row(row++), BUTTONWIDTH, LABELHEIGHT);
		buttonOK.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				username = user.getText();
				password = pwd.getText();
				domain = requiresDomain ? dom.getText() : "";
				if (username == null | username.length() == 0) {
					user.setFocus();
				} else if (password == null) { // password is allowed to be empty
					pwd.setFocus();
				} else if (requiresDomain & (domain == null | domain.length() == 0)) {
					dom.setFocus();
				} else {
					isAccepted = true;
					dialog.dispose();
				}
			}
		});
		buttonCancel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				dialog.dispose();
			}
		});
	}
	/**
	 * @return Returns the requiresDomain.
	 */
	public boolean isRequiresDomain() {
		return requiresDomain;
	}
	/**
	 * @param requiresDomain
	 *        The requiresDomain to set.
	 */
	public void setRequiresDomain(boolean requiresDomain) {
		this.requiresDomain = requiresDomain;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password
	 *        The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username
	 *        The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return host;
	}
	/**
	 * @param host
	 *        The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port
	 *        The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return Returns the realm.
	 */
	public String getRealm() {
		return realm;
	}
	/**
	 * @param realm
	 *        The realm to set.
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}
	/**
	 * @return Returns the domain.
	 */
	public String getDomain() {
		return domain;
	}
	/**
	 * @param domain
	 *        The domain to set.
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
}
