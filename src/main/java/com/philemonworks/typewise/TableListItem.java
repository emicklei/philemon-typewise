/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 10-02-2004 by emicklei
 * 11-02-2004: mvhulsentop: added BinaryAccessible interface and implementation 
 */
package com.philemonworks.typewise;

import java.io.IOException;
import java.io.Serializable;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * TableItem represents a row in a TableList.
 */
public class TableListItem implements BinaryAccessible, Serializable {
	private static final long serialVersionUID = 3257281435563013684L;
	private boolean dirty = false;
	private String id = "";
	private String[] items = new String[0];

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param c2
	 */
	public TableListItem(String id, String c2) {
		super();
		this.setId(id);
		this.add(c2);
	}
	/**
	 * @param id
	 * @param c2
	 * @param c3
	 */
	public TableListItem(String id, String c2, String c3) {
		super();
		this.setId(id);
		this.add(c2);
		this.add(c3);
	}
	/**
	 * @param id
	 * @param c2
	 * @param c3
	 * @param c4
	 */
	public TableListItem(String id, String c2, String c3, String c4) {
		super();
		this.setId(id);
		this.add(c2);
		this.add(c3);
		this.add(c4);
	}
	/**
	 * Create a TableListItem with the identifier
	 * 
	 * @param id
	 */
	public TableListItem(String id) {
		super();
		this.setId(id);
	}
	protected void markDirty() {
		dirty = true;
	}
	/**
	 * Mark the receiver as being changed.
	 */
	public void clearDirty() {
		dirty = false;
	}
	/**
	 * Return true if the contents of the receiver was changed.
	 * 
	 * @return true if dirty
	 */
	public boolean isDirty() {
		return dirty;
	}
	/**
	 * Add a column value to the receiver.
	 * 
	 * @param newItem
	 *        String
	 */
	public void add(String newItem) {
		if (newItem == null) throw new NullPointerException("table cell values cannot be null");
		String[] newItems = new String[items.length + 1];
		System.arraycopy(items, 0, newItems, 0, items.length);
		newItems[items.length] = newItem;
		items = newItems;
		this.markDirty();
	}
	/**
	 * Answer the string item at an index
	 * 
	 * @columnIndex column indices start at 0 (zero based)
	 * @param columnIndex
	 *        int
	 * @return String the value of this index
	 */
	public String get(int columnIndex) {
		return items[columnIndex];
	}
	/**
	 * Insert the method's description here. Creation date: (10-2-2004 16:18:04)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getId() {
		return id;
	}
	/**
	 * Insert the method's description here. Creation date: (10-2-2004 16:18:04)
	 * 
	 * @return java.lang.String[]
	 */
	public java.lang.String[] getItems() {
		return items;
	}
	/**
	 * Set the string item at
	 * 
	 * @columnIndex column indices start at 1 (non-zero based)
	 * @param columnIndex
	 *        int
	 * @param item
	 *        String
	 */
	public void put(int columnIndex, String item) {
		if (columnIndex < 1)
			throw new RuntimeException("illegal column index");
		if (columnIndex > items.length)
			throw new RuntimeException("illegal column index");
		items[columnIndex - 1] = item;
		this.markDirty();
	}
	/**
	 * Insert the method's description here. Creation date: (10-2-2004 16:18:04)
	 * 
	 * @param newId
	 *        java.lang.String
	 */
	public void setId(java.lang.String newId) {
		id = newId;
	}
	/**
	 * Insert the method's description here. Creation date: (10-2-2004 16:18:04)
	 * 
	 * @param newItems
	 *        java.lang.String[]
	 */
	public void setItems(java.lang.String[] newItems) {
		items = newItems;
		this.markDirty();
	}
	/**
	 * Return the number of columns.
	 * 
	 * @return int size
	 */
	public int size() {
		return items.length;
	}
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		String className = this.getClass().getName();
		buffer.append(className.substring(className.lastIndexOf(".") + 1, className.length()));
		buffer.append("(id=");
		buffer.append(id);
		buffer.append(")[");
		for (int i = 0; i < items.length; i++) {
			buffer.append(i);
			buffer.append("=");
			buffer.append(items[i]);
			buffer.append(",");
		}
		if (items.length > 0)
			buffer.deleteCharAt(buffer.length() - 1);
		buffer.append("]");
		return buffer.toString();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((TableListItem) this);
	}
}
