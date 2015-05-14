/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.server;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.tools.NoStatistics;
import com.philemonworks.typewise.tools.TrafficEventHandler;
import com.philemonworks.util.NLS;
import com.philemonworks.writer.HTMLWriter;

/**
 * AbstractApplicationServlet provides behavior for creating an ApplicationModel based on the servlet config parameter.
 * 
 * @author emicklei
 */
public class AbstractApplicationServlet extends javax.servlet.http.HttpServlet {
	private static final long serialVersionUID = 3977295516994451249L;
	private final static Logger LOG = Logger.getLogger(AbstractApplicationServlet.class);
	private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_EXPIRES = "Expires";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";		
	/**
	 * Servlet paramater key that specifies the qualified class name of the (root) ApplicationModel.
	 */
	public static final String PARAM_APPLICATION = "application";
	/**
	 * Servlet paramater key that specifies the qualified class name of a TrafficEventHandler.
	 * If absent, the NoStatistics handler is used.
	 */
	public static final String PARAM_TRAFFICEVENTHANDLER = "traffic";
	protected Class applicationClass;
	protected TrafficEventHandler traffic = new NoStatistics();

	/**
	 * Access or create the configured (in the web.xml) ApplicationModel.
	 * @param request ; the Http request that is used to access the stored model via the session
	 * @return the application model
	 */
	public ApplicationModel getApplicationModel(HttpServletRequest request) {
		String applicationSessionKey = applicationClass.getName();
		// Check whether application is stateless
		if (!applicationRequiresSession())
			return this.createApplicationModel();
		// Make sure we have a session for the application
		HttpSession mySession = request.getSession(true);
		ApplicationModel applicationOrNull = (ApplicationModel) mySession.getAttribute(applicationSessionKey);
		boolean modelCreated = false; // does this request causes a (re)create of the ApplicationModel?
		if (applicationOrNull == null) {
			applicationOrNull = this.createApplicationModel();
			modelCreated = true;
			traffic.handleSessionCreated(applicationClass.getName());
		} else {
			// Check whether the class matches. If not invalidate the session and re-invoke.
			if (!(applicationOrNull.getClass().equals(applicationClass))) {
				LOG.warn("Application class mismatch so invalidate session");
				applicationOrNull = this.createApplicationModel();
				modelCreated = true;
			}
		}
		if (modelCreated) {
			// Store the model in the session
			mySession.setAttribute(applicationSessionKey, applicationOrNull);
			applicationOrNull.view.setSessionId(mySession.getId());
		}		
		return applicationOrNull;
	}
	/**
	 * Subclasses may redefine this in order to prevent the creation of a session and storing the receiver into it.
	 * This can be used by applications that are stateless and provide a readonly view.
	 * @return true if a session is created and the application is stored into it.
	 */
	public boolean applicationRequiresSession(){
		return true;
	}
	/**
	 * Create an applicationmodel using the applicationClass that is initialized when creating the servlet.
	 * @return ApplicationModel
	 */
	protected ApplicationModel createApplicationModel() {
		ApplicationModel application = null;
		try {
			// find constructor
			Constructor method = applicationClass.getConstructor(new Class[] { ApplicationModel.class });
			application = (ApplicationModel) method.newInstance(new Object[] { null }); // parent is null for root
			// application
		} catch (NoSuchMethodException ex) {
			ErrorUtils.error(this, ex, "Cannot find constructor with argument type ApplicationModel in:" + applicationClass.getName());
		} catch (InvocationTargetException ex) {
			ErrorUtils.error(this, ex, "Cannot create instance of:" + applicationClass.getName());
		} catch (InstantiationException ex) {
			ErrorUtils.error(this, ex, "Cannot create instance of:" + applicationClass.getName());
		} catch (IllegalAccessException ex) {
			ErrorUtils.error(this, ex, "Cannot access class:" + applicationClass.getName());
		}
		return application;
	}
	/**
	 * The servlet is notified about the destruction of an ApplicationModel
	 * @param obsoleteModel : ApplicationModel
	 */
	protected void destroyedApplication(ApplicationModel obsoleteModel) {
		traffic.handleSessionDestroyed();
	}
	/**
	 * Read the init parameter that specifies the application class and initialize the receiver with an instance of that
	 * application. Optionally, read the init paramter that specifies which traffic event handler must be used.
	 * 
	 * @param config : ServletConfig
	 */
	public void init(ServletConfig config) {
		String applicationClassName = config.getInitParameter(PARAM_APPLICATION);
		if (applicationClassName == null) {
			ErrorUtils.error(this, "Missing parameter <" + PARAM_APPLICATION + ">");
		}
		try {
			applicationClass = this.getClass().getClassLoader().loadClass(applicationClassName);
		} catch (ClassNotFoundException ex) {
			ErrorUtils.error(this, ex, "Unable to load class: " + ex.getMessage());
		}
		Method initMethod = null;
		try {
			initMethod = applicationClass.getMethod("init", new Class[] { ServletConfig.class });
		} catch (Exception ex) {
			ErrorUtils.debug(this, "No init method found for:" + applicationClass);
		}
		if (initMethod == null)
			return;
		try {
			initMethod.invoke(applicationClass, new Object[] { config });
		} catch (Exception ex) {
			ErrorUtils.error(this, ex, "Unable to invoke init() on " + applicationClass);
		}
		String trafficClassName = config.getInitParameter(PARAM_TRAFFICEVENTHANDLER);
		if (trafficClassName == null) {
			ErrorUtils.warn(this, "No handler specified for <" + PARAM_TRAFFICEVENTHANDLER + ">");
		} else {
			try {
				Class trafficClass = this.getClass().getClassLoader().loadClass(trafficClassName);
				traffic = (TrafficEventHandler) trafficClass.newInstance();
			} catch (ClassNotFoundException ex) {
				ErrorUtils.error(this, ex, "Unable to load class: " + ex.getMessage());
			} catch (InstantiationException ex) {
				ErrorUtils.error(this, ex, "Unable to create instance: " + ex.getMessage());
			} catch (IllegalAccessException ex) {
				ErrorUtils.error(this, ex, "Unable to access constructor: " + ex.getMessage());
			}
		}
	}
	/**
	 * Add meta tags to the HTTP header to disable caching by the browser. 
	 * @param request : HttpServletRequest
	 * @param response : HttpServletResponse
	 */
	protected void setNoCache(HttpServletRequest request, HttpServletResponse response) {
		if (request.getProtocol().compareTo("HTTP/1.0") == 0) {
			response.setHeader("Pragma", "no-cache");
		} else if (request.getProtocol().compareTo("HTTP/1.1") == 0) {
			response.setHeader("Cache-Control", "no-cache");
		}
		response.setDateHeader("Expires", 0);
	}
	/** 
	 * This method can be invoked in order to determine whether a traffic report was requested.
	 * It is typically send inside the body of a doGet(...) for a subclass.
	 * @param request : HttpServletRequest 
	 * @return whether a request was send for a traffic statistics report
	 */
	public boolean isTrafficReportRequested(HttpServletRequest request) {
		String query = request.getQueryString();
		return (query != null && query.startsWith("stats"));  // TODO make this configurable
	}
	/**
	 * Create a report from the statistics gathered by the traffic event handler
	 * @param request : HttpServletRequest
	 * @param response : HttpServletResponse
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doTrafficReport(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		this.preventCaching(response);
		response.setContentType("text/html");
		HTMLWriter html = new HTMLWriter(new PrintStream(response.getOutputStream()));
		html.doctype();
		html.html();
		html.head();
		html.raw("<link REL=\"stylesheet\" href=\"typewise.css\"  type=\"text/css\">");
		html.end(); // head
		html.body();
		traffic.writeMonitorReportOn(html);
		html.end(); // body
		html.end(); // html
		html.flush();
	}
	  /**
     * Prevent the response from being cached.
     * See www.mnot.net.cache docs.
     */
    protected final void preventCaching(HttpServletResponse response) {
            response.setHeader(HEADER_PRAGMA, "No-cache");
            // HTTP 1.0 header
            response.setDateHeader(HEADER_EXPIRES, 1L);
            // HTTP 1.1 header: "no-cache" is the standard value,
            // "no-store" is necessary to prevent caching on FireFox.
            response.setHeader(HEADER_CACHE_CONTROL, "no-cache");
            response.addHeader(HEADER_CACHE_CONTROL, "no-store");
    }
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NLS.setLocale(request.getLocale());
		traffic.handleRequestReceived();
		super.service(request, response);
		traffic.handleResponseSend();
	}	
}