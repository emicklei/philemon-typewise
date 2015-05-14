/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 13, 2004 by mvhulsentop
 *
 */
package com.philemonworks.typewise.cwt;

import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;

/**
 * @author mvhulsentop
 *
 * Purpose: 
 * 
 */
public class GroupBox extends CompositeWidget implements BinaryAccessible, CWT {

	private String label = "";
	private byte alignment = LEFT;
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param rows
	 * @param columns
	 */
	public GroupBox(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}
	/**
	 * Answer the String label for the GroupBox
	 * 
	 * @return String 
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * Set the String label for the GroupBox
	 *
	 * @param label
	 */
	public void setLabel(String label) {
		// Check preconditons
		if (label == null)
			throw new RuntimeException("[GroupBox] label is null");		
		this.markDirty(WidgetAccessorConstants.SET_STRING);
		this.label = label;
	}
    public int rowTranslation(){ return getTopRow() - 1; }
    public int columnTranslation(){ return getLeftColumn() - 1; }
	public void kindDo(WidgetHandler wh) {
		wh.handle((GroupBox) this);
	}
    public void addStateMessagesTo(MessageSequence messageSequence) {
        super.addStateMessagesTo(messageSequence);
        if (isDirty(WidgetAccessorConstants.SET_STRING))
            messageSequence.add(this.setLabel_AsMessage(this.getLabel()));
        if (isDirty(WidgetAccessorConstants.SET_ALIGNMENT))
            messageSequence.add(this.setAlignment_AsMessage(this.getAlignment()));
    }	
    protected Message setLabel_AsMessage(String newString){
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_STRING);
		setter.addArgument(newString);
		return setter;
	}    
	protected Message setAlignment_AsMessage(int newAlignment){
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_ALIGNMENT);
		setter.addArgument(new Integer(newAlignment));
		return setter;
	}    	
    /**
     * @return Returns the alignment.
     */
    public byte getAlignment() {
        return alignment;
    }
    /**
     * @param alignment The alignment to set.
     */
    public void setAlignment(byte alignment) {
        this.alignment = alignment;
        this.markDirty(WidgetAccessorConstants.SET_ALIGNMENT);
    }
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_STRING) {
			this.setLabel((String) aMessage.getArgument());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ALIGNMENT) {
			this.setAlignment(((Number) aMessage.getArgument()).byteValue());
			return;
		}
		super.dispatch(aMessage);
	}

}
