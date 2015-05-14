/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 */
package com.philemonworks.typewise;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.html.PageWriter;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.server.ApplicationView;
import com.philemonworks.typewise.server.DefaultExceptionHandler;
import com.philemonworks.typewise.server.IExceptionHandler;
import com.philemonworks.writer.HTMLWriter;

/**
 * Purpose: This is the abstract superclass of all ApplicationModel classes.
 * It provides a set of methods to communicate with the connected ApplicationView (the client) using MessageSend objects.
 * 
 * @author emicklei
 */
public abstract class ApplicationModel implements MessageReceiver, MessageConstants, CWT, Serializable {
    public ApplicationView view = new ApplicationView();
    public ApplicationModel parent = null;

    /**
     * Create a new ApplicationModel.
     * 
     * @param myParent
     *                (optional) the parent ApplicationModel
     */
    public ApplicationModel(ApplicationModel myParent) {
        parent = myParent;
        // Share its client if the receiver is a submodel
        if (parent != null)
            view = myParent.view;
    }

    /**
     * @return the topmost ApplicationModel from the hierarchy of models.
     */
    public ApplicationModel getRootModel() {
        if (parent == null)
            return this;
        return parent.getRootModel();
    }
	/**
	 * @return ApplicationView the view for the receiver.
	 */
	public ApplicationView getView(){
		return view;
	}
    /**
     * Hook for reporting information when monitoring the execution of the
     * receiver Subclasses may want to override this.
     * @param html : PrintStream
     */
    public void monitorReportOn(PrintStream html) {
        html.print("<i>To show debugging information for this ApplicationModel you can redefine the method monitorReportOn(PrintWriter).</i>");
    };

    /**
     * Hook for replying to a GET request send from a client other than the
     * standard TypeWise client On default, notify the requestor about an
     * non-supported or illegal access. Subclasses may override this method.
     * @param request the Http request
     * @param response the Http response
     * @throws IOException
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();
        writer.println("<html><body>");
        writer.println("<font color=red><h2>TypeWise ApplicationServlet Error</h2></font>");
        writer.println("If you are reading this, you are attempting to access the ApplicationModel ");
        writer.println("by another program other than the supported TypeWise Client.<p>");
        writer.println("For more information, see <a href=\"http://www.typewise.org\">TypeWise.org</a>");
        writer.println("</body></html>");
        writer.flush();
    }

    public String getAccessPath() {
        return SERVER;
    }

    /**
     * This method is sent when initializing the servlet providing this service.
     * Subclasses may implement this to initialize their using extra parameters.
     * Note that the view is not yet available at this point of the program execution.
     * @param configuration : ServletConfig implementor
     */
    public static void init(ServletConfig configuration) {}

    /**
     * (non-Javadoc)
     * 
     * @see com.philemonworks.typewise.message.MessageReceiver#dispatch(com.philemonworks.typewise.message.IndexedMessageSend)
     */
    public void dispatch(IndexedMessageSend message) {
        int index = message.getIndex();
        if (OPERATION_close == index) {
            view.aboutToClose();
            return;
        }
        if (OPERATION_clearAllDirty == index) {
            this.view.getCurrentScreen().clearDirtyAll();
            return;
        }
        ErrorUtils.handleExceptionWithUserMessage(null, "Non application indexed message: " + DebugExplainer.explainFor(message, this));
    }

    /**
     * This is the standard method for starting an ApplicationModel by a client.
     * It is invoked by an URL that has the format:
     * http://{domain}/{webapp}/main?key=value.... Default behavior is the show
     * the Screen defined in the method mainScreen().
     * 
     * @param argumentMap
     *                contains the key-value pairs passed by the URL.
     */
    public void main(HashMap argumentMap) {
        view.show(this.mainScreen());
    }

    /**
     * Unless the subclass defines its own entry method (such as main(HashMap argumentMap)), 
     * your subclass should redefine this method to show your Screen.
     * 
     * @return Screen
     */
    public Screen mainScreen() {
        // 20 rows, 40 columns
        Screen main = new Screen("main", this, 20, 40);
        main.setTitle("Undefined Screen");
        return main;
    }

    /**
     * Hook method that is executed just before the client is asked to show the
     * argument, aScreen
     */
    public void beforeShow() {}

    /**
     * Hook method that is executed just after the client is asked to show the
     * argument, aScreen
     */
    public void afterShow() {}

    /**
     * If a tester exists, open it using the request
     * @param request is an URL decoded argument.
     */
    public void test(String request) {
        String toolName = "com.philemonworks.typewise.swt.client.SwtApplicationView";
		//String toolName = "com.philemonworks.typewise.awt.AWTTerminal";
        try {
            Class terminalClass = Class.forName(toolName);
            Object awtTerminal = terminalClass.newInstance();
            Method method = terminalClass.getMethod("open", new Class[] { com.philemonworks.typewise.ApplicationModel.class, String.class });
            method.invoke(awtTerminal, new Object[] { this, request });
        } catch (ClassNotFoundException ex) {
            ErrorUtils.error(this,ex,"Class not found:" + toolName);			
        } catch (Exception ex) {
            ErrorUtils.error(this,ex,"Unable to test:" + this.toString());
        }
    }
	/**
	 * Return a new implementor of IExceptionHandler that will be used to forward application exception to.
	 * This method can be overridden by applications that need special handling.
	 * A standard DefaultExceptionHandler is returned that simply logs the exception and throws a RuntimeException.
	 * @see com.philemonworks.typewise.server.DefaultExceptionHandler
	 * @return IExceptionHandler
	 */
	public IExceptionHandler getExceptionHandler(){
		return new DefaultExceptionHandler();
	}

	/**
	 * This method is a hook for models that want to do HTML rendering themselves.
	 * For most models, the pageWriter can perform this task.
	 * @param screen Screen
	 * @param dialogOrNull Dialog || null
	 * @param writer HTMLWriter
	 * @param postAction String
	 * @param pageWriter PageWriter
	 */
	public void renderHTML(Screen screen, Dialog dialogOrNull, HTMLWriter writer, String postAction, PageWriter pageWriter) {
		pageWriter.defaultWriteOn(screen,dialogOrNull,writer,postAction);		
	}
	/**
	 * This method is a hook for models that wat to be notified about a request being handled.
	 * It is send just before processing the message specified by the request (GET or POST).
	 * @param request applicationOrNull.aboutToHandleRequest(request);
	 */
	public void aboutToHandleRequest(HttpServletRequest request) {
	}
}