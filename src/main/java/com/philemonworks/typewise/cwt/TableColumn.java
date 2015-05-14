/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 10, 2004 by mvhulsentop
 * 22-2-2004: added comments, changed import, changed superclass
 * 1-4-2004: added kindDo
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.io.Serializable;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetAppearanceHolder;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * This class represents a column in a TableList.
 * 
 * @author mvhulsentop
 */
public class TableColumn extends WidgetAppearanceHolder implements BinaryAccessible, Serializable {
	private static final long serialVersionUID = 3760567502899853361L;
	public static String LABELNAMEPOSTFIX = ".label";
	/**
	 * editable heading width appearance maxLength header
	 */
	private boolean editable = false;
	private int width = 5; // Arbitrary default value
	private int maxLength = 10; // Arbitrary default value
	private Label heading = new Label("unnamed column label", 1, 1, 1, 1);
	private byte alignment = Label.LEFT;
	private TableList table;

	public void setName(String newName) {
		name = newName;
		heading.setName(name + LABELNAMEPOSTFIX);
	}
	public void setTableList(TableList container) {
		table = container;
		heading.setParent(table); // for inheriting appearance
	}
	public TableList getTableList() {
		return table;
	}
	public Widget getTopWidget() {
		// Pre: table not null
		return table.getTopWidget();
	}
	public Widget getParent() {
		return table;
	}
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		if (isDirty(WidgetAccessorConstants.GET_EDITABLE))
			seq.add(this.setEditable_AsMessage(editable));
		this.getHeading().addStateMessagesTo(seq);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_EDITABLE) {
			this.setEditable(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
	}
	private Message setEditable_AsMessage(boolean isEditable) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_ENABLED);
		setter.addArgument(new Boolean(isEditable));
		return setter;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((TableColumn) this);
	}
	/**
	 * Answer whether the information showed by the column is editable by the user. Default value is false.
	 * 
	 * @return
	 */
	public boolean isEditable() {
		return editable;
	}
	/**
	 * Set whether the information showed by the column is editable by the user.
	 * 
	 * @param editable
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
		this.markDirty(WidgetAccessorConstants.GET_EDITABLE);
	}
	/**
	 * Answer the label widget that is used to display the heading of the column
	 * 
	 * @return
	 */
	public Label getHeading() {
		return heading;
	}
	/**
	 * Set the label widget that is used to display the heading of the column
	 * 
	 * @param header
	 */
	public void setHeading(Label header) {
		// Check preconditons
		if (header == null || !(header instanceof Label))
			throw new RuntimeException("[TableColumn] heading must a Label");
		this.heading = header;
	}
	/**
	 * Answer the maximum length of the editable text field. Only make sense when the column is editable at all. The
	 * value is set to -1 if no limit is specified.
	 * 
	 * @return int
	 */
	public int getMaxLength() {
		return maxLength;
	}
	/**
	 * Set the maximum length of the editable text field. Only make sense when the column is editable at all. The value
	 * must be set to -1 if no limit is wanted.
	 * 
	 * @param maxLength
	 */
	public void setMaxLength(int newMaxLength) {
		// Check preconditons
		if (newMaxLength == 0 || newMaxLength < -1)
			throw new RuntimeException("[TableColumn] illegal maxLength:" + newMaxLength);
		this.maxLength = newMaxLength;
	}
	/**
	 * Answer the width of the column.
	 * 
	 * @return int
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * Set the width of the column.
	 * 
	 * @param width
	 */
	public void setWidth(int newWidth) {
		// Check preconditons
		if (newWidth <= 1)
			throw new RuntimeException("[TableColumn] illegal width:" + newWidth);
		this.width = newWidth;
	}
	public void kindDo(WidgetHandler requestor) {
		requestor.handle(this);
	}
	/**
	 * @return
	 */
	public byte getAlignment() {
		return alignment;
	}
	/**
	 * @param b
	 */
	public void setAlignment(byte b) {
		if (alignment == b)
			return;
		if (b != LEFT & b != CENTER & b != RIGHT)
			ErrorUtils.error(this, "Illegal alignment constant:" + b);
		alignment = b;
	}
	public void clearDirtyAll() {
		super.clearDirtyAll();
		this.getHeading().clearDirty();
	}
}
