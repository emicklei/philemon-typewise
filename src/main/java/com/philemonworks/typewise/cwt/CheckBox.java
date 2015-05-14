/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved. Use, duplication,
 * distribution or disclosure restricted. See http://www.philemonworks.com for
 * information.
 * 
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

public class CheckBox extends Label implements BinaryAccessible, CWT {
	private static final long serialVersionUID = 3257282547959280692L;
	/**
	 * If checked then the selection is true. Default value is false.
	 */
	private boolean selection = false;
	/**
	 * Default alignment of the tick to LEFT
	 */
	private byte tickAlignment = LEFT;

	/**
	 * Constructor for this Widget class. Checks whether the name is unique and whether the bounds are within that of
	 * the container (if any).
	 * 
	 * @param newName :
	 *        unique name of the Widget
	 * @param topRow :
	 *        1-based integer to specify the row coordinate of the origin
	 * @param leftColumn :
	 *        1-based integer to specify the column coordinate of the origin
	 * @param howManyRows :
	 *        integer number of rows occupied by the widget
	 * @param howManyColumns :
	 *        integer number of columns occupied by the widget
	 */
	public CheckBox(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addStateMessagesTo(MessageSequence seq) {
		// Set alignment before setting label
		if (this.isDirty(WidgetAccessorConstants.GET_TICKALIGNMENT)) {
			seq.add(this.setTickAlignment_AsMessage(this.getTickAlignment()));
		}
		super.addStateMessagesTo(seq);
		if (this.isDirty(WidgetAccessorConstants.GET_SELECTION))
			seq.add(this.setSelection_AsMessage(this.isSelected()));
	}
	public void checkTriggersEvent(byte eventID) {
		if (eventID == EVENT_SELECTEDITEM)
			return;
		super.checkTriggersEvent(eventID);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SELECTION) {
			this.basicSetSelection(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_TICKALIGNMENT) {
			this.setTickAlignment(((Number) aMessage.getArgument()).byteValue());
			return;
		}
		super.dispatch(aMessage);
	}
	/**
	 * 
	 * @return the constant for tick alignment (LEFT or RIGHT)
	 */
	public byte getTickAlignment() {
		return tickAlignment;
	}
	/**
	 * 
	 * @return whether the CheckBox is selected.
	 */
	public boolean isSelected() {
		return selection;
	}
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((CheckBox) this);
	}
	/**
	 * Set the new selection for the CheckBox.
	 * @param newSelection true or false
	 */
	public void setSelection(boolean newSelection) {
		if (selection == newSelection)
			return;		
		this.basicSetSelection(newSelection);
		this.triggerEvent(EVENT_CLICKED, Boolean.valueOf(newSelection));
	}
	/**
	 * Set the new selection for the CheckBox.
	 * @param newSelection true or false
	 */
	public void basicSetSelection(boolean newSelection) {
		this.selection = newSelection;
		this.markDirty(WidgetAccessorConstants.GET_SELECTION);
	}	
	protected Message setSelection_AsMessage(boolean selection) {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_SELECTION).addArgument(new Boolean(
				selection));
	}
	/**
	 * Set the new alignment for the tick (LEFT,RIGHT)
	 * 
	 * @param b
	 *        the new alignment constant
	 * @see CWT
	 */
	public void setTickAlignment(byte b) {
		if (tickAlignment == b)
			return;
		tickAlignment = b;
		this.markDirty(WidgetAccessorConstants.GET_TICKALIGNMENT);
	}
	/**
	 * @param newAlignment
	 * @return a Message for setting the tickAlignment of the receiver.
	 */
	protected Message setTickAlignment_AsMessage(int newAlignment) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_TICKALIGNMENT);
		setter.addArgument(new Integer(newAlignment));
		return setter;
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((CheckBox) this);
	}
}