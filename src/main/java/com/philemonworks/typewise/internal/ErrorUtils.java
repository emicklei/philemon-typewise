/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 13-apr-04: created
 * 17-7-4: removed getCause until we have an alternative
 *
 */
package com.philemonworks.typewise.internal;

import org.apache.log4j.Logger;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageSend;

/**
 * @author emicklei
 *
 */
public class ErrorUtils implements MessageConstants {
    public static Logger LOG = Logger.getLogger(ErrorUtils.class);

    public static void debug(Object source, Object arg) {
        if (LOG.isDebugEnabled()) {
            Logger.getLogger(source.getClass()).debug(arg);
        }
    }
    public static void warn(Object source, Object arg) {
        Logger.getLogger(source.getClass()).warn(arg);
    }
    public static String displayString(Object anObject) {
        String defaultString = anObject.getClass().getName();
        int dot = defaultString.lastIndexOf('.');
        return defaultString.substring(dot + 1, defaultString.length());
    }

    public static void error(Object from, Object message) {
        throw new RuntimeException("[" + displayString(from) + "] " + message + "\n");
    }

    public static void error(Object from, Exception ex, Object message) {
        Logger.getLogger(from.getClass()).error(message, ex);
        throw new RuntimeException("[" + displayString(from) + "] " + message + "\n");
    }

    public static void error(Class fromClass, Exception ex, Object message) {
        Logger.getLogger(fromClass).error(message, ex);
        throw new RuntimeException("[" + displayString(fromClass) + "] " + message + "\n");
    }

    public static String typesDebugString(Class[] paramTypes) {
        StringBuffer types = new StringBuffer();
        for (int t = 0; t < paramTypes.length; t++) {
            types.append(paramTypes[t]);
            types.append(" ");
        }
        return types.toString();
    }

    public static Message handleExceptionWithUserMessage(Throwable exToReport, String userNotification) {
        LOG.error(userNotification, exToReport);
        Dialog errorDialog = Dialog.warn("An application error has occurred:" + userNotification);
        MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_SHOWDIALOG);
        msg.addArgument(errorDialog);
        return msg;
    }

    public static Object handleInvocationError(Exception ex, String why, Object receiver, String operationName, Class[] paramTypes) {
        return handleExceptionWithUserMessage(ex.getCause(), why + receiver.toString() + ">>\n" + operationName + "(" + ErrorUtils.typesDebugString(paramTypes)
                + ")");
    }
	/**
	 * Handles the exception when a message is dispatched to an object which it does not understand. 
	 * @param ex : Exception || null
	 * @param receiver : the Object that does not understand it (no method was found)
	 * @param operationName : String
	 * @param paramTypes : array of class types
	 */
    public static void handleNoSuchMethod(Exception ex, Object receiver, String operationName, Class[] paramTypes) {
		Throwable cause = ex == null ? null : ex.getCause();
        throw new RuntimeException("Missing public method definition: " + (ErrorUtils.displayString(receiver)) + ">>" + operationName + "("
                + ErrorUtils.typesDebugString(paramTypes) + ")", cause);
    }
}