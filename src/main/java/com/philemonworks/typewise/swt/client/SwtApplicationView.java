package com.philemonworks.typewise.swt.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.internal.ColorLookup;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.server.IExceptionHandler;
import com.philemonworks.typewise.swt.SwtDialog;
import com.philemonworks.typewise.swt.SwtScreen;

/**
 * SwtApplicationView is a TypeWise client that uses SWT Technology.
 * 
 * @author E.M.Micklei
 */
public class SwtApplicationView implements MessageReceiver, MessageConstants, DisposeListener, CredentialsProvider {
	private static final Logger LOG = Logger.getLogger(SwtApplicationView.class);
	/**
	 * Global offset in X for the location of SWT widgets
	 * 
	 * @todo compute this
	 */
	public static int screenMarginX = 8;
	/**
	 * Global offset in Y for the location of SWT widgets
	 * 
	 * @todo compute this
	 */
	public static int screenMarginY = 26;

	private String servletURL;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwtApplicationView view = new SwtApplicationView();
		// blank start
		if (args == null || args.length == 0) {
			view.start(new Welcome(null), "main");
			return;
		}
		// assume URL was passed as argument
		try {
			view.gotoURL(args[0]);
		} catch (Exception ex) {
			// fails to connect to server application
			LOG.warn("Unable to gotoURL[" + args[0] + "]", ex);
			Welcome defaultApp = new Welcome(null);
			defaultApp.setDefaultURL(args[0]);
			defaultApp.setErrorMessage(ex.getMessage());
			view.start(defaultApp,"error");
			return;
		}
		view.open();
	}
	/**
	 *  This method exists for ApplicationModel#test(method)
	 * @param model ApplicationModel
	 * @param methodName String
	 */
	public static void open(ApplicationModel model, String methodName){
		SwtApplicationView view = new SwtApplicationView();
		view.start(model,methodName);
	}
	/**
	 * @param model
	 *        ApplicationModel
	 * @param method
	 *        String, not null, not empty
	 */
	public void start(ApplicationModel model, String method) {
		this.init();
		if (method == null || method.length() == 0)
			throw new RuntimeException("No method name for the model is specified");
		MessageSend msg = new MessageSend(MessageConstants.SERVER, method);
		msg.addArgument(new HashMap());
		this.dispatcher.initializeForLocal(model);
		this.dispatcher.dispatch(msg);
		this.open();
	}	

	/**
	 * Height in pixels of a single character for the current font (will be computed)
	 */
	public int charheight = 14;
	/**
	 * Maximum width in pixels of a single character for the current font (will be computed)
	 */
	public int charwidth = 8;
	private SwtScreen currentScreen = null;
	private SwtMessageDispatcher dispatcher = null;
	private Display display = null;
	protected IExceptionHandler exceptionHandler = null;
	protected Font font = null;
	private Menu menuBar = null;
	private int offsetX = 0;
	private int offsetY = 0;
	protected Shell shell = null;
	private Label status = null;
	private Cursor waitCursor = null;

	/**
	 * Constructor. Ready to handle a Message (through its dispatcher).
	 */
	public SwtApplicationView() {
		super();
		this.dispatcher = new SwtMessageDispatcher(this);
	}
	private void addStatusLine() {
		status = new Label(this.getShell(), SWT.READ_ONLY);
		int w = currentScreen.getColumns() * this.charwidth;
		int h = currentScreen.getRows() * this.charheight;
		int sh = this.charheight;
		status.setBounds(0, h, w, sh);
	}
	/**
	 * @param cwt
	 *        Widget
	 * @param leftOffset
	 *        int
	 * @return the x coordinate of the SWT control on the parent composite.
	 */
	public int computeLeftFor(com.philemonworks.typewise.internal.Widget cwt, int leftOffset) {
		return (cwt.getLeftColumn() - 1) * charwidth + offsetX + leftOffset;
	}
	/**
	 * @param cwt
	 *        Widget
	 * @param leftOffset
	 *        int
	 * @param topOffset
	 *        int
	 * @param extraWidth
	 *        int
	 * @param extraHeight
	 *        int
	 * @return the Rectangle of the SWT control on the parent composite.
	 */
	public Rectangle computeRectangleFor(com.philemonworks.typewise.internal.Widget cwt, int leftOffset, int topOffset, int extraWidth, int extraHeight) {
		int left = this.computeLeftFor(cwt, leftOffset);
		int top = this.computeTopFor(cwt, topOffset);
		int width = cwt.getColumns() * charwidth + extraWidth;
		int height = cwt.getRows() * charheight + extraHeight;
		return new Rectangle(left, top, width, height);
	}
	/**
	 * @param cwt
	 *        Widget
	 * @param topOffset
	 *        int
	 * @return the y coordinate of the SWT control on the parent composite.
	 */
	public int computeTopFor(com.philemonworks.typewise.internal.Widget cwt, int topOffset) {
		return (cwt.getTopRow() - 1) * charheight + offsetY + topOffset;
	}
	/**
	 * 
	 */
	public void decrementFontSize() {
		FontData[] dataNow = font.getFontData();
		for (int i = 0; i < dataNow.length; i++) {
			dataNow[i].setHeight(dataNow[i].getHeight() - 1);
		}
		Font newFont = new Font(shell.getDisplay(), dataNow);
		ConfigManager.setFont(newFont);
		this.updateFont(newFont);
	}
	/**
	 * This method is called by the HttpClient when the request is caught by an authenticating server. Here we startup a
	 * simple dialog to get the credentials from the user and return these to the HttpClient for further processing. If
	 * login fails, then I quess this method is called again.
	 * 
	 * @return Credentials with user input
	 */
	public Credentials getCredentials(final AuthScheme authscheme, final String host, int port, boolean proxy) throws CredentialsNotAvailableException {
		ServerAuthenticationLogin dialog = new ServerAuthenticationLogin(this.ensureShell());
		dialog.setRequiresDomain(authscheme instanceof NTLMScheme);
		dialog.setRealm(authscheme.getRealm());
		dialog.setHost(host);
		dialog.setPort(port);
		if (!dialog.open())
			throw new CredentialsNotAvailableException("No user/password provided", null);
		String username = dialog.getUsername();
		String password = dialog.getPassword();
		String domain = dialog.getDomain();
		try {
			if (authscheme instanceof NTLMScheme) {
				return new NTCredentials(username, password, host, domain);
			} else if (authscheme instanceof RFC2617Scheme) {
				return new UsernamePasswordCredentials(username, password);
			} else {
				throw new CredentialsNotAvailableException("Unsupported authentication scheme: " + authscheme.getSchemeName());
			}
		} catch (IOException e) {
			throw new CredentialsNotAvailableException(e.getMessage(), e);
		}
	}
	/**
	 * @return Display
	 */
	public Display getDisplay() {
		return display;
	}
	/**
	 * Return the (SWT) menu bar for this view. Create one if absent and make it visible.
	 * 
	 * @return Menu (SWT)
	 */
	public Menu getMenuBar() {
		this.toggleMenuBar(true);
		return menuBar;
	}
	/**
	 * Answer the dispatcher for messages.
	 * 
	 * @return SwtMessageDispatcher
	 */
	public SwtMessageDispatcher getMessageDispatcher() {
		return dispatcher;
	}
	/**
	 * @return Composite
	 */
	public Composite getParent() {
		return shell;
	}
	/**
	 * @return SwtScreen
	 */
	public SwtScreen getScreen() {
		return currentScreen;
	}
	/**
	 * @return Shell
	 */
	public Shell getShell() {
		return shell;
	}
	/**
	 * Switch to the ApplicationModel located at the url.
	 * 
	 * @param url
	 *        String
	 */
	public void gotoURL(String url) {
		LOG.debug("[client] gotoURL(\"" + url + "\")");
		if (url.indexOf('/') == -1) {
			this.setStatus("Invalid application URL. Please correct entry.");
			return;
		}
		// If client leaves an application that notify the server about it
		if (this.getScreen() != null) {
			// post a close ,ugly piece....
			this.dispatcher.postMessageOnly(new IndexedMessageSend(MessageConstants.SERVER, MessageConstants.OPERATION_close));
		}
		String method = url.substring(url.lastIndexOf('/') + 1);
		this.setServletURL(url.substring(0, url.lastIndexOf('/')));
		MessageSend msg = new MessageSend(MessageConstants.SERVER, method);
		msg.addArgument(SwtUtils.decodeArguments(url));
		// (Re)initialize transport for remote access
		this.dispatcher.initializeForRemote(this.getServletURL(), this);
		// Forget about state changes in current application model
		this.dispatcher.postMessageOnly(msg);
	}
	/**
	 * The widget triggers an event that causes any installed message to be dispatched.
	 * 
	 * @param eventConstant
	 *        byte
	 * @param widget
	 *        Widget
	 */
	public void handleEventFrom(byte eventConstant, Widget widget) {
		this.getMessageDispatcher().dispatch(widget.messageForEvent(eventConstant));
	}
	/**
	 * Reset the current cursor
	 */
	public void hideBusyCursor() {
		// Be safe
		if (this.getShell() == null)
			return;
		this.getShell().setCursor(null);
	}
	/**
	 * 
	 */
	public void incrementFontSize() {
		FontData[] dataNow = font.getFontData();
		for (int i = 0; i < dataNow.length; i++) {
			dataNow[i].setHeight(dataNow[i].getHeight() + 1);
		}
		Font newFont = new Font(shell.getDisplay(), dataNow);
		ConfigManager.setFont(newFont);
		this.updateFont(newFont);
	}
	private Shell ensureShell(){
		if (shell != null) return shell;
		if (display == null)
			display = new Display();
		shell = new Shell(display, SWT.MIN);
		return shell;
	}
	
	private void init() {
		this.ensureShell();
		waitCursor = new Cursor(this.getDisplay(), SWT.CURSOR_WAIT);
		this.initIcon();
		this.getShell().addDisposeListener(this);
		this.updateFont(new Font(display, ConfigManager.fontName(), ConfigManager.fontSize(), ConfigManager.fontStyle()));
	}
	private void initIcon() {
		InputStream is;
		try {
			is = new URL(SwtUtils.urlForLocalResource("icon_final.gif")).openStream();
			Image icon = new Image(display, is);
			shell.setImage(icon);
		} catch (Exception e) {
			LOG.error("Unable to set icon");
		}
	}
	// Building
	/**
	 * Initializes an SWT widget that is created by one of the CWT widgets in responds to adding the SwtWidget wrapper
	 * to the receiver.
	 * 
	 * @param swt
	 *        Control
	 * @param cwt
	 *        Widget
	 */
	public void initializeSWTWidget(Control swt, com.philemonworks.typewise.internal.Widget cwt) {
		SwtUtils.setColors(ColorLookup.backgroundFor(cwt), ColorLookup.foregroundFor(cwt), swt, this);
		this.updateBounds(swt, cwt);
		swt.setFont(font);
	}
	/**
	 * 
	 */
	public void open() {
		if (!shell.isVisible()) {
			shell.open();
			this.runEventLoop();
		}
		SwtUtils.flushColors();
		SwtImageProvider.flushImages();
	}
	void openURL(String url) {
		LOG.debug("[client] openURL(\"" + url + "\")");
		try {
			Runtime.getRuntime().exec("rundll32.exe url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			LOG.debug("Unable to start local application for url:" + url);
		}
	}
	private void removeStatusLine() {
		status.dispose();
		status = null;
	}
	private void runEventLoop() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
		display = null;
		shell = null;
	}
	/**
	 * Open a FontDialog such that the user can select a new font. Update the font on all widgets and resize the shell
	 * accordingly.
	 */
	public void selectFont() {
		FontDialog dialog = new FontDialog(shell);
		dialog.setFontList(font.getFontData());
		FontData newData = dialog.open();
		if (newData == null)
			return;
		Font newFont = new Font(shell.getDisplay(), newData);
		ConfigManager.setFont(newFont);
		this.updateFont(newFont);
	}
	/**
	 * Show the new status in the reserved bottom line.
	 * 
	 * @param newStatus
	 *        String
	 */
	public void setStatus(String newStatus) {
		if (status == null)
			return;
		if (newStatus == null)
			newStatus = "";
		status.setText(newStatus);
	}
	protected void show(Dialog aDialog) {
		SwtDialog swt = ((SwtDialog) aDialog);
		swt.setView(this);
		swt.open();
	}
	protected void show(SwtScreen aScreen) {
		if (display == null) {
			this.init();
		}
		if (currentScreen != null) {
			currentScreen.removeFromView(this);
			this.removeStatusLine();
			this.getShell().redraw();
		}
		currentScreen = aScreen;
		aScreen.addToView(this, null);
		this.addStatusLine();
	}
	/**
	 * Set the current cursor to WAIT
	 */
	public void showBusyCursor() {
		if (this.getShell() == null)
			return;
		this.getShell().setCursor(waitCursor);
	}
	/**
	 * Show or hide the SWT menu bar.
	 * 
	 * @param isVisible
	 *        boolean if true then show it.
	 */
	public void toggleMenuBar(boolean isVisible) {
		if (menuBar == null) {
			menuBar = new Menu(this.getShell(), SWT.BAR);
			this.getShell().setMenuBar(menuBar);
		}
		if (!isVisible) {
			// Remove any existing menus
			for (int i = 0; i < menuBar.getItems().length; i++)
				menuBar.getItem(i++).dispose();
		}
		menuBar.setVisible(isVisible);
	}
	/**
	 * @param swt
	 * @param cwt
	 */
	public void updateBounds(Control swt, com.philemonworks.typewise.internal.Widget cwt) {
		swt.setBounds(this.computeRectangleFor(cwt, 0, 0, 0, 0));
	}
	private void updateFont(Font newFont) {
		LOG.debug("updating font");
		Font oldFont = font;
		font = newFont;
		shell.setFont(font);
		GC gc = new GC(shell);
		FontMetrics fm = gc.getFontMetrics();
		charwidth = Math.min(gc.getCharWidth('W'), fm.getAverageCharWidth());
		charheight = fm.getHeight();
		// System.out.println("width="+charwidth+",height="+charheight);
		gc.dispose();
		if (currentScreen == null) {
			if (oldFont != null)
				oldFont.dispose();
			return; // no screen
		}
		currentScreen.updateFont(font);
		this.removeStatusLine();
		this.addStatusLine();
		shell.redraw();
		if (oldFont != null)
			oldFont.dispose();
	}
	public boolean useNativeLook() {
		return true;
	}
	public void widgetDisposed(DisposeEvent arg0) {
		if (arg0.widget == shell)
			dispatcher.postMessageOnly(new IndexedMessageSend(MessageConstants.SERVER, MessageConstants.OPERATION_close));
	}
	public String getAccessPath() {
		return MessageConstants.CLIENT;
	}
	public void dispatch(IndexedMessageSend msg) {
		switch (msg.getIndex()) {
		case OPERATION_OPENURL:
			// TODO should check argument for leading slash
			this.openURL(this.servletURL + "/../" + (String) msg.getArgument());
			break;
		case OPERATION_GOTOURL:
			this.gotoURL((String) msg.getArgument());
			break;			
		case OPERATION_handleError:
			this.show(Dialog.error((String) msg.getArgument()));
			break;
		case OPERATION_SHOW:
			this.show((SwtScreen) msg.getArgument());
			break;
		case OPERATION_SHOWDIALOG:
			this.show((Dialog) msg.getArgument());
			break;
		case OPERATION_OK:
			break;
		case OPERATION_STATUS:
			this.setStatus((String) msg.getArgument());
			break;
		case OPERATION_clearAllDirty:
			this.getScreen().clearDirtyAll();
			break;
		case OPERATION_close:
			this.getShell().dispose();
			break;			
		default:
			throw new RuntimeException("Unable to dispatch:" + DebugExplainer.explainFor(msg, this));
		}
		
	}
	/**
	 * @return Returns the servletURL.
	 */
	public String getServletURL() {
		return servletURL;
	}
	/**
	 * @param servletURL The servletURL to set.
	 */
	public void setServletURL(String servletURL) {
		this.servletURL = servletURL;
	}
	/**
	 * Open a dialog to entry proxy settings (server and port)
	 * 
	 * @deprecated
	 */
	public void editProxySettings() {
		ProxyServerSettings dialog = new ProxyServerSettings(this.getShell());
		dialog.setServer(ConfigManager.getProxyServer());
		dialog.setPort(ConfigManager.getProxyPort());
		if (dialog.open()) {
			ConfigManager.setProxy(dialog.getServer(), dialog.getPort());
		}
	}
}
