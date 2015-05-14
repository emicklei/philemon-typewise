/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, renaming or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * */
package com.philemonworks.typewise.internal;

import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;

/**
 * Purpose: BasicWidget is an abstract superclass for widget that can have
 * an enabled state.
 *   
 * @author emicklei
 */
public abstract class BasicWidget extends Widget implements CWT {
    
	private boolean enabled = true;
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public BasicWidget(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}	
	/**
	 * Determines whether instance of this class can trigger the event.
	 * Throws a RuntimeException when it does not (because it is considered a fatal error).
	 * @param eventID : event contant
	 * @see CWT
	 */
	public void checkTriggersEvent(byte eventID){
		if (eventID == EVENT_GETTINGFOCUS) return;
		if (eventID == EVENT_LOSINGFOCUS) return;
		super.checkTriggersEvent(eventID);		
	}	
	/**
	 * Add messages for each field change of the widget. Do not clear the dirty field.
	 */
    public void addStateMessagesTo(MessageSequence messageSequence){
        super.addStateMessagesTo(messageSequence);
        if (isDirty(WidgetAccessorConstants.GET_ENABLED))
            messageSequence.add(this.setEnabled_AsMessage(this.isEnabled()));
    }
	/**
	 * Answer the message to set the property enabled 
	 * @param beEnabled
	 * @return the Message
	 */    
	public Message setEnabled_AsMessage(boolean beEnabled){
		return new IndexedMessageSend(this.getName(),WidgetAccessorConstants.SET_ENABLED).addArgument(new Boolean(beEnabled));
	}
	/**
	 * Anser the message to get the property enabled 
	 * @return the Message
	 */
	public Message getEnabled_AsMessage(){
		return new IndexedMessageSend(this.getName(),WidgetAccessorConstants.GET_ENABLED);
	}	
	/**
	 * Answer whether the widget can accept user input
	 * @return boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}
	/**
	 * Set whether the widget can accept user input
	 * @param b = true or false
	 */
	public void setEnabled(boolean b) {
		enabled = b;
		this.markDirty(WidgetAccessorConstants.GET_ENABLED);
	}
	/**
	 * Dispath an IndexedMessageSend to the receiver.
	 * If the indexed selector is recognized then perform that message.
	 * Otherwise let the superclass implementation handle this request.
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ENABLED) {
			this.setEnabled(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
		super.dispatch(aMessage);
	}
}
