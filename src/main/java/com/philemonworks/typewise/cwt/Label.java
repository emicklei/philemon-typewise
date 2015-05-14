/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * Purpose:
 *
 */
public class Label extends BasicWidget  implements BinaryAccessible, CWT {
	private static final long serialVersionUID = -45392858743789663L;
	protected static String NOSTRING = new String();
	protected String string = NOSTRING;
	// This default value is changed in subclasses .
	protected byte alignment = LEFT; 
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_STRING) {
			this.basicSetString((String) aMessage.getArgument());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ALIGNMENT) {
			this.setAlignment(((Number) aMessage.getArgument()).byteValue());
			return;
		}
		super.dispatch(aMessage);
	}
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public Label(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Label) this);		
	}
	public void kindDo(WidgetHandler requestor){
		requestor.handle(this);
	}
	/**
	 * Use getText() instead
	 * @deprecated 
	 */	
	public String getString() {
		return string;
	}
	/**
	 * Use setText(aString) instead
	 * @deprecated 
	 */
	public void setString(String newString) {
		this.setText(newString);
	}
	protected void basicSetString(String newString){
	    // No dirty or trigger
	    string = newString;
	}
	public void setText(String newString) {
		// Check preconditons
		if (string == null)
			throw new RuntimeException("string parameter is null");		
		if (string.equals(newString))
		    return;
	   this.basicSetString(newString);
		this.markDirty(WidgetAccessorConstants.GET_STRING);
	}
	public String getText(){
	    return string;
	}
	public byte getAlignment() {
		return alignment;
	}
	public void setAlignment(byte constant) {
	    if (alignment == constant) return;
	    if (constant != LEFT & constant != CENTER & constant != RIGHT)
	        ErrorUtils.error(this,"Illegal alignment constant:" + constant);
		alignment = constant;
		this.markDirty(WidgetAccessorConstants.GET_ALIGNMENT);
	}
	public void addStateMessagesTo(MessageSequence messageSequence) {
		super.addStateMessagesTo(messageSequence);
		// Set alignment before label
		if (this.isDirty(WidgetAccessorConstants.GET_ALIGNMENT)) {
			messageSequence.add(this.setAlignment_AsMessage(this.getAlignment()));
		}
		if (this.isDirty(WidgetAccessorConstants.GET_STRING)) {
			messageSequence.add(this.setString_AsMessage(this.getString()));
		}
	}
	protected Message setString_AsMessage(String newString){
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_STRING);
		setter.addArgument(newString);
		return setter;
	}
	protected Message setAlignment_AsMessage(int newAlignment){
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_ALIGNMENT);
		setter.addArgument(new Integer(newAlignment));
		return setter;
	}	
}
