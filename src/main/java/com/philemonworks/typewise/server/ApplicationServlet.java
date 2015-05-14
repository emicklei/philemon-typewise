/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * */
package com.philemonworks.typewise.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.util.NLS;
/**
 * ApplicationServlet is a layer between HTTP and ApplicationModel.
 * It is a servlet that receives requests, and converts these requests into calls that invoke methods on the model.
 * In reponse, the model asks the client to change the curren screen or show a new.
 * The servlet takes care of encoding and decoding the BWA formatted messages.
 * 
 * @author E.Micklei
 */
public class ApplicationServlet extends AbstractApplicationServlet {
	private static final long serialVersionUID = 3258135738917926449L;
	private final static Logger LOG = Logger.getLogger(ApplicationServlet.class);

	/**
	 * This servlet provides HTTP POST.
	 * The request contains encoded binary data using the BWA forrmat
	 * The objects received are instances of implementors of Message
	 * All Message objects are dispatch to the application of the receiver.
	 * 
	 * @param request : the Http request
	 * @param response : the Http response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
		throws javax.servlet.ServletException, java.io.IOException {
		
		NLS.setLocale(request.getLocale());
		traffic.handleRequestReceived();
		
		// First read the complete body of the request
		byte[] buffer = this.readPacketFrom(request);

		Object body = null;
		Message result = null;
		Message requestMessage = null;
		try {
		    body = BinaryWidgetAccessor.decode(buffer);
			requestMessage = (Message)body;
		} catch (ClassCastException ex) {
			result =
				ErrorUtils.handleExceptionWithUserMessage(
					ex,
					"Message request value expected but received:" + body);
		}
		ApplicationModel model = this.getApplicationModel(request);
		model.aboutToHandleRequest(request);
		if (this.isRequestForInvalidatedModel(request,model)){
		    LOG.warn("Restart because of invalidated model, session is new?=" + request.getSession().isNew());
		    requestMessage = new MessageSend(MessageConstants.SERVER,"main");
		    ((MessageSend)requestMessage).addArgument(new HashMap()); // any passed arguments are lost
		}
		if (result == null) {
			try {
			    result = this.dispatchMessageTo(requestMessage,model);
			} catch (SessionInvalidationRequestNotification not){
				// This will cause to invalidate the session later
			    model.view.setSessionId(null);
			} catch (Exception ex) {
				result = ErrorUtils.handleExceptionWithUserMessage(ex, "Unable to handle Message:" + requestMessage);
			}
		}	
		// Check whether model indicates a session invalidation request
	    if (!model.view.hasSession()) {
			LOG.info("Invalidating session:" + request.getSession().getId());
		    request.getSession().invalidate();
	        this.destroyedApplication(model);
	    }
		byte[] responseBody = BinaryWidgetAccessor.encode(result);
		traffic.handleResponseSize(responseBody.length);
		this.writePacketTo(responseBody, response);
		traffic.handleResponseSend();
	}
	/**
	 * Answer whether this request was send to an invalidated ApplicationModel.
	 * 
	 * @param request : the Http request
	 * @param model : a new or existing ApplicationModel
	 * @return true | false
	 */
	public boolean isRequestForInvalidatedModel(HttpServletRequest request, ApplicationModel model){
	    // true if the request has a JSESSIONID cookie but the client of the model has no current screen
	    if (LOG.isDebugEnabled()) ErrorUtils.debug(this,DebugExplainer.explain(request));	    
	    if (model.view.getCurrentScreen() != null) return false;
	    Cookie[] cookies = request.getCookies();
	    if (cookies == null) return false;
	    for (int c=0;c<cookies.length;c++){
	        if ("JSESSIONID".equals(cookies[c].getName())) return true;
	    }	   	    
	    return false;
	}
	/** 
	 * Forward the GET request to the application model as this is not a standard request
	 * unless a request was made for a report on traffic statistics. 
	 * 
	 * @param request : the Http request
	 * @param response : the Http response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		NLS.setLocale(request.getLocale());
		if (this.isTrafficReportRequested(request)) 
			this.doTrafficReport(request,response);
		else
			this.getApplicationModel(request).doGet(request,response);
	}
	/**
	 * Dispatch a message to the application model and answer the reply from that message send.
	 * 
	 * @param requestMessage : Message
	 * @param model : ApplicationModel
	 * @return Message
	 */
	protected Message dispatchMessageTo(Message requestMessage,ApplicationModel model){
	    requestMessage.resolveAndDispatchToUsing(model,new MessageDispatcher());    
	    // Take and answer the reply on this message perform	    
	    return model.view.takeReply();
	}
	/**
	 * Transport a binary packet (BWA encodeded) over the response.
	 * 
	 * @param responseBody : byte[]
	 * @param response : HttpServletResponse
	 * @throws IOException
	 */
	private void writePacketTo(byte[] responseBody, HttpServletResponse response) throws IOException {
		// Write the binary data to the response
		response.setContentType("typewise/binary");
		response.setContentLength(responseBody.length);
		response.getOutputStream().write(responseBody);
		response.getOutputStream().flush();
		response.flushBuffer();
	}
	/**
	 * Read the binary contents of the request.
	 * 
	 * @param request : HttpServletRequest
	 * @return buffer : byte[]
	 * @throws IOException
	 */
	private byte[] readPacketFrom(HttpServletRequest request) throws IOException {
		// Read the binary data from the request
		DataInputStream inputStream = new DataInputStream(request.getInputStream());
		byte[] buffer = new byte[request.getContentLength()];
		inputStream.readFully(buffer);
		traffic.handleRequestSize(buffer.length);
		return buffer;
	}
}
