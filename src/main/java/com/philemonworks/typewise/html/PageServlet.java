/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.server.AbstractApplicationServlet;
import com.philemonworks.typewise.server.ApplicationView;
import com.philemonworks.util.NLS;

/**
 * PageServlet can process Http requests send from a standard HTML browser client. In repsonse, it returns an HTML
 * representation of the current Screen of an ApplicationModel.
 * 
 * @author E.M.Micklei
 */
public class PageServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = 3258411742072418872L;
	private static final Logger LOG = Logger.getLogger(PageServlet.class);
	private static final String PARAM_FONTFAMILY = "html.fontfamily";
	private static final String PARAM_FONTSIZE = "html.fontsize";
	private String stylesheetReference = null;
	/**
	 * Sesssion key to store a dialog
	 */
	public static final String TYPEWISE_DIALOG = "TypeWise.current.DIALOG";

	public void init(ServletConfig config) {
		super.init(config);
		String fontfamily = config.getInitParameter(PARAM_FONTFAMILY);
		if (fontfamily == null) {
			ErrorUtils.error(this, "Missing parameter <" + PARAM_FONTFAMILY + ">");
		}
		PageWriter.FONTFAMILY = fontfamily;
		String fontsize = config.getInitParameter(PARAM_FONTSIZE);
		if (fontsize == null) {
			ErrorUtils.error(this, "Missing parameter <" + PARAM_FONTSIZE + ">");
		}		
		PageWriter.FONTSIZE = fontsize;
		stylesheetReference = config.getInitParameter("css");
	}
	/**
	 * TODO proper exception handling
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest,HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (LOG.isDebugEnabled())
			LOG.debug("GET:" + request.getRequestURI() + "?" + request.getQueryString());
		if (this.isTrafficReportRequested(request)) {
			this.doTrafficReport(request, response);
		} else {
			this.doRenderPage(request, response);
		}
	}
	private void doRenderPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ApplicationModel model = this.getApplicationModel(request);
		if (model.view.getCurrentScreen() == null) {
			// to prevent multiple starts
			this.executeMain(request, model);
		}
		// respond with current screen
		this.writePageOn(model, request, response);
	}
	/**
	 * Redefined from super to set the client type
	 */
	protected ApplicationModel createApplicationModel() {
		ApplicationModel model = super.createApplicationModel();
		// overwrite type of client
		model.view.setType(ApplicationView.TYPE_HTML);
		return model;
	}
	/**
	 * Write a HTML page for the current Screen of the application model directly onto the output stream of the
	 * response.
	 * 
	 * @param model :
	 *        ApplicationModel
	 * @param request :
	 *        HttpRequest (not really needed, remove?)
	 * @param response :
	 *        HttpResponse
	 */
	protected void writePageOn(ApplicationModel model, HttpServletRequest request, HttpServletResponse response) {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
			PrintStream pw = new PrintStream(bos);
			PageWriter pageWriter = new PageWriter();
			pageWriter.stylesheetReference = stylesheetReference;
			// fetch dialog if present
			Dialog dialog = (Dialog) request.getSession().getAttribute(TYPEWISE_DIALOG);
			if (dialog != null) {
				// make sure it is no longer in the session
				request.getSession().removeAttribute(TYPEWISE_DIALOG);
				LOG.debug("Screen has dialog");
			}
			pageWriter.writeOn(model.view.getCurrentScreen(), dialog, pw, request.getRequestURI());
			pw.flush();
			byte[] responseBody = bos.toByteArray();
			response.setContentType("text/html");
			response.setContentLength(responseBody.length);
			traffic.handleResponseSize(responseBody.length);
			this.preventCaching(response);
			OutputStream out = response.getOutputStream();
			out.write(responseBody);
			out.flush();
		} catch (IOException ex) {
			ErrorUtils.error(this.getClass(), ex, " Unable to write page as response");
		}
	}
	/**
	 * Executes the main(HashMap) method of the model (ApplicationModel). The arguments are taken from the Http request.
	 * 
	 * @param request :
	 *        HttpRequest
	 * @param model :
	 *        ApplicationModel
	 */
	protected void executeMain(HttpServletRequest request, ApplicationModel model) {
		MessageSend msg = new MessageSend(model.getAccessPath(), "main");
		// Convert to a HashMap since that is the exact argument type of main
		msg.addArgument(this.asHashMap(request.getParameterMap()));
		// Use a MessageDispatcher to handle this request
		new MessageDispatcher().resolveAndDispatchMessageTo(msg, model);
	}
	/**
	 * ApplicationModels need an instance of the implementation class HashMap. Convert the parameter to a new HashMap
	 * 
	 * @param parameterMap :
	 *        input
	 * @return the converted input as a HashMap
	 */
	private HashMap asHashMap(Map parameterMap) {
		HashMap map = new HashMap(parameterMap.size());
		for (Iterator keys = parameterMap.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
			map.put(key, parameterMap.get(key));
		}
		return map;
	}
	/**
	 * Process the POST request. Check for session invalidation. Using the parameter map, visit all widgets so they can
	 * update their contents. Finally, process the incoming event so a message will be send (if any was registered).
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (LOG.isDebugEnabled())
			LOG.debug("POST:" + request.getRequestURI() + " PARAMS:" + DebugExplainer.explain(request.getParameterMap()));
		traffic.handleRequestReceived();
				
		int trafficDataSize = 0;
		ApplicationModel model = this.getApplicationModel(request);
		model.aboutToHandleRequest(request);
		Screen currentScreen = model.view.getCurrentScreen();
		if (currentScreen == null) {
			// session was invalidated since the last request
			// perhaps need to re-create the model to make sure it is properly initialized
			this.executeMain(request, model);
		} else {
			WidgetPostHandler postHandler = new WidgetPostHandler(request.getParameterMap());
			currentScreen.kindDo(postHandler);
			traffic.handleRequestSize(trafficDataSize); // rough estimate, does not includes query and header info TODO
			// Process the event
			String widgetName = (String) request.getParameter("_submitWidget");
			String widgetEvent = (String) request.getParameter("_widgetEvent");
			if (widgetName != null && widgetEvent != null) {
				Widget widget = model.view.getCurrentScreen().widgetNamed(widgetName);
				if (widget != null) {
					widget.triggerEvent(Byte.parseByte(widgetEvent));
				} else {
					String eventExplain = widgetEvent.length() == 0 ? "<empty>" : DebugExplainer.eventConstant(Integer.parseInt(widgetEvent));
					LOG.warn("event:" + eventExplain + " for unknown widget:" + widgetName + " in screen:" + model.view.getCurrentScreen() + " for model:"
							+ model);
				}
			}
		}
		// We do not want to process screen changes as the screen will be repainted completely
		model.view.getCurrentScreen().clearDirtyAll();
		// Use a HTMLView to handle all pending reply messages
		new HTMLView(request, response).dispatch(model.view.takeReply());
		// Check whether model indicates a session invalidation request
		if (!model.view.hasSession()) {
			LOG.debug("Invalidating session:" + request.getSession().getId());
			request.getSession().invalidate();
			this.destroyedApplication(model);
		}
		// unless the response was already written
		if (!response.isCommitted())
			// Redirect the request to render the current Screen state of the application
			response.sendRedirect(request.getRequestURI());
	}	
}