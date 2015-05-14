/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 28-mrt-04: created
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.util.ArrayList;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 */
public class RadioButton extends CheckBox implements BinaryAccessible, CWT {

	private Object item = null;
	private String groupName = "radios";
	

		
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public RadioButton(
		String newName,
		int topRow,
		int leftColumn,
		int howManyRows,
		int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.Widget#kindDo(com.philemonworks.terminal.WidgetHandler)
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((RadioButton) this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
			bwa.write((RadioButton) this);		
	}
	/**
	 * If a widget for the groupName was requested, answer the receiver. He knows how to handle updates. TODO: make
	 * class RadioButtonGroup.
	 */
    public Widget widgetNamed(String widgetName) {
		if (super.widgetNamed(widgetName) == this) return this;
		if (this.getGroupName().equals(widgetName)) return this;
		return null;
    }
	/**
	 * @return
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return
	 */
	public Object getItem() {
		return item;
	}
	/**
	 * @param string
	 */
	public void setGroupName(String string) {
		groupName = string;
	}
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		if (this.isDirty(WidgetAccessorConstants.GET_ITEM))
		    seq.add(this.setItem_AsMessage(this.getItem()));

	}	
	protected MessageSend setItem_AsMessage(Object newItem) {
		return new IndexedMessageSend(this.getName(),WidgetAccessorConstants.SET_ITEM).addArgument(newItem);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ITEM) {
			this.setItem(aMessage.getArgument());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SELECTION) {
			this.basicSetSelection(aMessage.getArgument() == Boolean.TRUE);
			return;
		}		
		super.dispatch(aMessage);
	}
	public void setItem(Object object) {
		item = object;
		this.markDirty(WidgetAccessorConstants.GET_ITEM);
	}
	/*
	 * Answer the item of the selected RadioButton from my group @return Object
	 */
	public Object getSelectedItem(){
	    if (this.getParent() == null) return null;
	    CompositeWidget container = (CompositeWidget)this.getParent();
	    for (int w=0;w<container.getWidgets().size();w++){
	        Widget each = (Widget)container.getWidgets().get(w);
	        if (each instanceof RadioButton){
	            RadioButton button = (RadioButton)each;
	            if (button.getGroupName().equals(this.getGroupName())){
	                if (button.isSelected()) return button.getItem();
	            }
	        }
	    }
	    return null;
	}
	/**
	 * Override from super to make sure all other RadioButtons have the same parent are deselected if newSelection =
	 * true.
	 * @param newSelection boolean
	 */
	public void setSelection(boolean newSelection) {
		super.setSelection(newSelection);
		// Only proceed if newSelection = true		
		if (!newSelection) return;
    	if (this.getParent() == null) return;
		CompositeWidget container = (CompositeWidget)this.getParent();
		for (int w=0;w<container.getWidgets().size();w++){
			Widget each = (Widget)container.getWidgets().get(w);
			if (each instanceof RadioButton && each != this){
				RadioButton button = (RadioButton)each;
				button.setSelection(false);
			}
		}
	}
}
