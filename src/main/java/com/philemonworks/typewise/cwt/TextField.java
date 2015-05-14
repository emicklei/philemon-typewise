/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 8-dec-2003
 * 16-2-2004: mvhulsentop: added addStateMessagesTo
 * */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 * 
 * Purpose:
 *  
 */
public class TextField extends Label implements BinaryAccessible {

	private int maxLength = -1; // no limit

	boolean editable = true;

	private String type = ""; // possible values are password,

	// zipcodeDutch

	public static final String TYPE_PASSWORD = "password";

	public TextField(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	/*
	 * @see com.philemonworks.typewise.Widget#kindDo(com.philemonworks.typewise.WidgetHandler)
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((TextField) this);
	}

	/**
	 * @return
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param maxLength
	 */
	public void setMaxLength(int newMaxLength) {
		// Check preconditons
		if (newMaxLength == 0 || newMaxLength < -1)
			throw new RuntimeException("[TextField] illegal maxLength:" + newMaxLength);
		this.maxLength = newMaxLength;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.Widget#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((TextField) this);
	}

	/**
	 * @return
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		this.markDirty(WidgetAccessorConstants.GET_EDITABLE);
	}
	public void addStateMessagesTo(MessageSequence messageSequence) {
		super.addStateMessagesTo(messageSequence);
		if (this.isDirty(WidgetAccessorConstants.GET_EDITABLE)) {
			messageSequence.add(this.setEditable_AsMessage(this.isEditable()));
		}
	}
	/**
	 * @param b
	 * @return
	 */
	private MessageSend setEditable_AsMessage(boolean b) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_EDITABLE);
		setter.addArgument(new Boolean(b));
		return setter;
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_EDITABLE) {
			this.setEditable(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
		super.dispatch(aMessage);
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(String newType) {
		if (newType.length() > 0)
			if (!newType.equals("password"))
				throw new RuntimeException("[TextField] unknown type:" + newType);
		this.type = newType;
	}
}