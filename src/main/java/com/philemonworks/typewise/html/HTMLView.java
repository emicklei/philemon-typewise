/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.html;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;

/**
 *  HTMLClient is an object that handles message send directly to the client.
 */
public class HTMLView implements MessageReceiver, MessageConstants {
    HttpServletResponse response;
    HttpServletRequest request;
    
    /**
     * Constructor with the Http response. 
     * @param response : HttpResponse
     */
    public HTMLView(HttpServletRequest request, HttpServletResponse response) {
        super();
        this.response = response;
        this.request = request;
    }

    /**
     * Handle the case of a MessageSequence; dispatch the handling of each contained Message.
     * @param sequence : MessageSequence
     */
    public void dispatch(MessageSequence sequence) {
        for (int m = 0; m < sequence.getMessages().size(); m++) {
            Message each = sequence.getMessage(m);
            this.dispatch(each);
        }
    }

    /**
     * Dispatcht the request for handling an implementor of Message.
     * @param aMessage : Message
     */
    public void dispatch(Message aMessage) {
        // use correct class hierarchy
        if (aMessage instanceof IndexedMessageSend) {
            this.dispatch((IndexedMessageSend) aMessage);
        } else if (aMessage instanceof MessageSend) {
            this.dispatch((MessageSend) aMessage);
        } else if (aMessage instanceof MessageSequence) {
            this.dispatch((MessageSequence) aMessage);
        }
    }

    /**
     * Handle the case of a MessageSend.
     * @param messageSend
     */
    public void dispatch(MessageSend messageSend) {}

    /**
     * Handle the case of an IndexedMessageSend.
     * @param indexedMessageSend
     */
    public void dispatch(IndexedMessageSend indexedMessageSend) {
        Logger.getLogger(this.getClass()).debug(DebugExplainer.explainFor(indexedMessageSend, null));
        int index = indexedMessageSend.getIndex();
        if (OPERATION_SHOWDIALOG == index) {
        	// temporary store the dialog in the current session
        	// the servlet should pick this up and use it when rendering the page
        	// the servlet should also remove the attribute after processing
        	request.getSession().setAttribute(PageServlet.TYPEWISE_DIALOG,indexedMessageSend.getArgument());
        	return;
        }
        if (OPERATION_OPENURL == index || OPERATION_GOTOURL == index) {
            try {
                response.sendRedirect((String) indexedMessageSend.getArgument());
            } catch (IOException ex) {
                Logger.getLogger(this.getClass()).error("sendRedirect:" + DebugExplainer.explainFor(indexedMessageSend, null));
            }
        }
    }

	public String getAccessPath() {
		return CLIENT;
	}
}

