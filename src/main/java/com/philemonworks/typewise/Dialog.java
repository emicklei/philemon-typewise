/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 4-nov-2004: created
 *
 */
package com.philemonworks.typewise;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

public class Dialog implements BinaryAccessible {
    public final static int OK = 0;
    public final static int OK_CANCEL = 1;
    public final static int YES_NO_CANCEL = 2;
    public final static int WARN = 3;
    public final static int INFO = 4;
    public final static int ERROR = 5;    
    
    private int type = OK;
    private String text = "";
    private Message yesMessage = null;
    private Message noMessage = null;
    private Message cancelMessage = null;
    public Dialog(){
        super();
    }
    public Dialog(int typeConstant, String newMessage){
        this();
        this.type = typeConstant;
        this.text = newMessage;
    }  
    public static Dialog warn(String newMessage){
        return new Dialog(WARN,newMessage);        
    }
    public static Dialog info(String newMessage){
        return new Dialog(INFO,newMessage);        
    }   
    public static Dialog error(String newMessage){
        return new Dialog(ERROR,newMessage);        
    }       
    public int getType() {
        return type;
    }
    public void setType(int type) {
        if (type < 0 || type > ERROR) throw new RuntimeException("Illegal Dialog type constant:" + type);
        this.type = type;
    }
    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((Dialog)this);
    }
	/**
	 * For internal use only
	 * 
	 * @return
	 */
    public Message getNoMessage() {
        return noMessage;
    }
	/**
	 * For internal use only
	 * 
	 * @param noMessage
	 */
    public void setNoMessage(Message noMessage) {
        this.noMessage = noMessage;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
	/**
	 * For internal use only
	 * 
	 * @return
	 */
    public Message getYesMessage() {
        return yesMessage;
    }
	/**
	 * For internal use only
	 */
    public void setYesMessage(Message yesMessage) {
        this.yesMessage = yesMessage;
    }
	/**
	 * For internal use only
	 * 
	 * @return
	 */
    public Message getCancelMessage() {
        return cancelMessage;
    }
	/**
	 * For internal use only
	 * 
	 * @param cancelMessage
	 */
    public void setCancelMessage(Message cancelMessage) {
        this.cancelMessage = cancelMessage;
    }
	/**
	 * Install a message to be send when a dialog button is clicked.
	 * 
	 * @param constant
	 *        byte must be one of OK, CANCEL, YES, NO
	 * @param operationName
	 *        String
	 * @param receiver
	 *        MessageReceiver
	 */
	public void onSendTo(byte constant, String operationName, MessageReceiver receiver){
		Message msg = new MessageSend(receiver,operationName);
		switch (constant) {
			case CWT.OK : yesMessage = msg; break;
			case CWT.CANCEL : cancelMessage = msg; break;
			case CWT.YES : yesMessage = msg; break;
			case CWT.NO : noMessage = msg; break;
			default : ErrorUtils.error(this,null, "Unknown dialog constant=" + constant);
		}
			
	}
}
