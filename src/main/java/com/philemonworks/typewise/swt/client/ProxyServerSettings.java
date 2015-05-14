package com.philemonworks.typewise.swt.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
 * ProxyServerSettings is used to change proxy settings.
 * 
 * @author E.M.Micklei
 */
public class ProxyServerSettings extends Dialog {
	private static final String SERVER = "server";
	private static final String PORT = "port";
	private String server = "<host>";
	private int port = 0;
	private static final int LABELWIDTH = 60;
	private static final int LABELHEIGHT = 22;
	private static final int VALUEWIDTH = 180;
	private static final int VALUEHEIGHT = 22;
	private static final int BUTTONWIDTH = 80;
	private int row = 0;
	private Text serverText;
	private Text portText;
	private boolean ok = false;

	/**
	 * For testing only
	 * 
	 * @param argv
	 */
	public static void main(String[] argv) {
		new ProxyServerSettings(new Shell()).open();
	}
	/**
	 * @param arg0
	 */
	public ProxyServerSettings(Shell arg0) {
		super(arg0);
	}
	/**
	 * @param arg0
	 * @param arg1
	 */
	public ProxyServerSettings(Shell arg0, int arg1) {
		super(arg0, arg1);
	}
	/**
	 * Creat a shell, add labels and textfields and open it
	 * 
	 * @return boolean whether settings were accepted.
	 */
	public boolean open() {
		Shell parent = getParent();
		Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setSize(360, 160);
		dialog.setText("Proxy server settings - TypeWise");
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
		return ok;
	}
	private void addHeaderTo(Shell dialog) {
		Label label = new Label(dialog, SWT.NULL);
		label.setText("Please type the proxy server name and port:");
		label.setBounds(col(0), row(row++), VALUEWIDTH + LABELWIDTH, LABELHEIGHT);
		Label iconLabel = new Label(dialog, SWT.IMAGE_JPEG);
		iconLabel.setAlignment(SWT.LEFT);
		iconLabel.setBounds(8, 8, 34, 40);
		InputStream is;
		try {
			is = new URL(SwtUtils.urlForLocalResource("proxy.gif")).openStream();
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
		Label serverLabel = new Label(shell, SWT.NULL);
		serverLabel.setText("Server:");
		serverLabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		serverText = new Text(shell, SWT.SINGLE | SWT.BORDER);
		serverText.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		serverText.setText(server);
		Label portlabel = new Label(shell, SWT.NULL);
		portlabel.setText("Port:");
		portlabel.setBounds(col(0), row(row), LABELWIDTH, LABELHEIGHT);
		portText = new Text(shell, SWT.SINGLE | SWT.BORDER);
		portText.setBounds(col(1), row(row++), VALUEWIDTH, VALUEHEIGHT);
		portText.setText(String.valueOf(port));
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
				server = serverText.getText();
				if (portText.getText().length() > 0)
					port = Integer.valueOf(portText.getText()).intValue();
				ok = true;
				dialog.dispose();
			}
		});
		buttonCancel.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				dialog.dispose();
			}
		});
	}
	/**
	 * @return Returns the port.
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port The port to set.
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return Returns the server.
	 */
	public String getServer() {
		return server;
	}
	/**
	 * @param server The server to set.
	 */
	public void setServer(String server) {
		this.server = server;
	}
}
