/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 */
package com.philemonworks.typewise;

import java.io.IOException;
import java.io.Serializable;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.ToStringBuilder;

/**
 * ListItem instances can be used to encapsulate both an identifier and a user readable label
 * and are typically used as contents of a List widget.
 * Two ListItems are equal if their id's are equal. 
 * 
 * @author emicklei
 */
public class ListItem implements BinaryAccessible, Serializable {
	private static final long serialVersionUID = 3833467322844461360L;
	private String id = null;
	private String item = "";

	/**
	 * Creates a DisplayItem using an id an an String item.
	 * 
	 * @param id : non-empty String
	 * @param newItem : any String
	 */
	public ListItem(String id, String newItem) {
		super();
		this.setId(id);
		this.setItem(newItem);
	}
	/**
	 * Answer the id for the receiver.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getId() {
		return id;
	}
	/**
	 * Answer the item for the receiver.
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getItem() {
		return item;
	}
	/**
	 * Set the id for the receiver.
	 * 
	 * @param newId
	 *        java.lang.String
	 */
	public void setId(java.lang.String newId) {
		id = newId;
	}
	/**
	 * Set the item for the receiver.
	 * 
	 * @param newItem
	 *        java.lang.String
	 */
	public void setItem(java.lang.String newItem) {
		item = newItem;
	}
	/**
	 * Answer a String representation for logging.
	 * @return String 
	 */
	public String toString() {
		return ToStringBuilder.build(this);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((ListItem) this);
	}
	/**
	 * A ListItem is equal to another ListItem if their ids are equal.
	 * @param anObject Object
	 * @return true if the parameter is a ListItem and has the same id.
	 */
	public boolean equals(Object anObject){
		if (anObject == null) return false;
		if (!(anObject instanceof ListItem)) return false;
		if (id == null) return false;
		ListItem otherItem = (ListItem)anObject;
		return id.equals(otherItem.getId());
	}
}
