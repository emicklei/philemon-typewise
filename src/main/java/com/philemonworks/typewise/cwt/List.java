/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * List is a Widget that can display multiple items and allow the user to select one.
 * 
 * @author emicklei
 */
public class List extends BasicWidget implements BinaryAccessible, CWT {
	private static final long serialVersionUID = 4121409592573244215L;
	protected java.util.List items = new ArrayList();
	private boolean multiSelect = false;
	private int[] selectionIndices;

	// Public
	public List(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
		this.resetSelections();
	}
	public void addItem(Object newItem) {
		this.getItems().add(newItem);
		this.markDirty(WidgetAccessorConstants.SET_ITEMS);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.Widget#addStateMessagesTo(com.philemonworks.typewise.server.MessageSequence)
	 */
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		if (this.isDirty(WidgetAccessorConstants.SET_ITEMS))
			seq.add(this.setItems_AsMessage(this.getItems()));
		if (this.isDirty(WidgetAccessorConstants.SET_SELECTIONINDEX))
			seq.add(this.setSelectionIndex_AsMessage(this.getSelectionIndex()));
		if (this.isDirty(WidgetAccessorConstants.SET_SELECTIONINDICES))
			seq.add(this.setSelectionIndices_AsMessage(this.selectionIndicesAsList()));
	}
	public void basicSetSelectionIndex(int index) {
		// No dirty or trigger
		if (index == -1)
			this.basicSetSelectionIndices(new int[0]);
		else
			this.basicSetSelectionIndices(new int[] { index });
	}
	public void basicSetSelectionIndices(int[] indices) {
		// No dirty or trigger
		selectionIndices = indices;
	}
	protected void checkItemTypes(java.util.List newItems) {
		for (int i = 0; i < newItems.size(); i++) {
			Object each = newItems.get(i);
			if (!(each instanceof String))
				if (!(each instanceof ListItem))
					throw new RuntimeException("Illegal List item type:" + each);
		}
	}
	/**
	 * Determines whether instance of this class can trigger the event. Throws a RuntimeException when it does not
	 * (because it is considered a fatal error).
	 * 
	 * @param eventID :
	 *        event contant
	 * @see CWT
	 */
	public void checkTriggersEvent(byte eventID) {
		if (eventID == EVENT_SELECTEDITEMS)
			return;
		if (eventID == EVENT_SELECTEDITEM)
			return;
		if (eventID == EVENT_DEFAULTACTION)
			return;
		super.checkTriggersEvent(eventID);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SELECTIONINDICES) {
			this.basicSetSelectionIndices(this.selectionIndicesFromList(((java.util.List) aMessage.getArgument())));
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SELECTIONINDEX) {
			this.basicSetSelectionIndex(((Number) aMessage.getArgument()).intValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SELECTEDITEM) {
			this.setSelectedItem(aMessage.getArgument());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ITEMS) {
			this.setItems((java.util.List) aMessage.getArgument());
			return;
		}
		super.dispatch(aMessage);
	}
	public java.util.List getItems() {
		return items;
	}
	/**
	 * Return the first ListItem that has an id which is equal to the parameter
	 * 
	 * @param id
	 *        String
	 * @return ListItem || null
	 */
	public ListItem getListItemById(String id) {
		if (id == null)
			return null;
		if (items == null)
			return null;
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			ListItem element = (ListItem) iter.next();
			if (element.getId().equals(id))
				return element;
		}
		return null;
	}
	/**
	 * Return the selected Object or null
	 * 
	 * @return String | ListItem | null
	 */
	public Object getSelectedItem() {
		return selectionIndices.length == 0 ? null : items.get(selectionIndices[0]);
	}
	protected Message getSelectedItem_AsMessage() {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.GET_SELECTEDITEM);
	}
	public java.util.List getSelectedItems() {
		java.util.List selections = new ArrayList(selectionIndices.length);
		for (int i = 0; i < selectionIndices.length; i++) {
			selections.add(items.get(selectionIndices[i]));
		}
		return selections;
	}
	/**
	 * Return the selected ListItem or null
	 * 
	 * @return ListItem
	 */
	public ListItem getSelectedListItem() {
		Object item = this.getSelectedItem();
		if (item == null)
			return null;
		return (ListItem) item;
	}
	/**
	 * Return the selection index
	 * 
	 * @return -1 or the first from the selectionIndices
	 */
	public int getSelectionIndex() {
		return selectionIndices.length == 0 ? -1 : selectionIndices[0];
	}
	protected void handleReplacedItems() {
		this.resetSelections();
	}
	/**
	 * @return Returns the multiSelect.
	 */
	public boolean isMultiSelect() {
		return multiSelect;
	}
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((List) this);
	}
	/**
	 * Remove an item from the items. Ignore if that item was not part of items
	 * 
	 * @param newItem
	 */
	public void removeItem(Object newItem) {
		// This is the issue when keeping an index instead of object reference....
		this.removeItemAt(this.getItems().indexOf(newItem));
	}
	public void removeItemAt(int removeItemIndex) {
		if (removeItemIndex == -1) {
			// No item.
			return;
		}
		this.resetSelections();
		this.getItems().remove(removeItemIndex);
		this.markDirty(WidgetAccessorConstants.SET_ITEMS);
	}
	/**
	 * 
	 */
	public void resetSelections() {
		this.basicSetSelectionIndices(new int[0]);
	}
	private java.util.List selectionIndicesAsList() {
		java.util.List indices = new ArrayList(selectionIndices.length);
		for (int i = 0; i < selectionIndices.length; i++) {
			indices.add(new Integer(selectionIndices[i]));
		}
		return indices;
	}
	private int[] selectionIndicesFromList(java.util.List list) {
		int[] indices = new int[list.size()];
		int i = 0;
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Integer index = (Integer) iter.next();
			indices[i++] = index.intValue();
		}
		return indices;
	}
	/**
	 * Set the new collection of items. Checks whether all items are of correct item type.
	 * 
	 * @param items
	 */
	public void setItems(java.util.List items) {
		// Check preconditions
		if (items == null)
			throw new IllegalArgumentException("items is null");
		this.checkItemTypes(items);
		this.items = items;
		this.handleReplacedItems();
		this.markDirty(WidgetAccessorConstants.SET_ITEMS);
	}
	protected Message setItems_AsMessage(Object argument) {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_ITEMS).addArgument(argument);
	}
	/**
	 * @param multiSelect
	 *        The multiSelect to set.
	 */
	public void setMultiSelect(boolean multiSelect) {
		this.multiSelect = multiSelect;
	}
	/**
	 * Set the selectedItem. Note that when an item occours multiple times in the List, only the first instance is
	 * selected.
	 * 
	 * @param object
	 */
	public void setSelectedItem(Object object) {
		this.setSelectionIndex(items.indexOf(object));
	}
	protected Message setSelectedItem_AsMessage(Object argument) {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_SELECTEDITEM).addArgument(argument);
	}
	/**
	 * Set the selectionItem using the index in the list of items. collection of the List
	 * 
	 * @param index
	 *        legal zero-based index into the items
	 */
	public void setSelectionIndex(int index) {
		if (selectionIndices.length == 1 && selectionIndices[0] == index)
			return;
		if (index == -1 && selectionIndices.length == 0)
			return;
		if (index == -1)
			this.resetSelections();
		else
			this.basicSetSelectionIndex(index);
		this.markDirty(WidgetAccessorConstants.SET_SELECTIONINDEX);
		this.triggerEvent(EVENT_SELECTEDITEM, this.getSelectedItem());
	}
	/**
	 * @param selectionIndex
	 *        int
	 * @return MessageSend
	 */
	private MessageSend setSelectionIndex_AsMessage(int selectionIndex) {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_SELECTIONINDEX).addArgument(new Integer(selectionIndex));
	}
	/**
	 * Set the new array of selection indices
	 * @param indices
	 */
	public void setSelectionIndices(int[] indices) {
		// TODO check really changed + check in range
		this.basicSetSelectionIndices(indices);
		this.markDirty(WidgetAccessorConstants.SET_SELECTIONINDICES);
		this.triggerEvent(EVENT_SELECTEDITEMS, this.getSelectedItems());
	}
	/**
	 * @param indices
	 *        int[]
	 * @return MessageSend
	 */
	private MessageSend setSelectionIndices_AsMessage(java.util.List indices) {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_SELECTIONINDICES).addArgument(indices);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.philemonworks.typewise.internal.Widget#triggerEvent(byte) TODO verify this for other events
	 */
	public void triggerEvent(byte constant) {
		// If the constant indicates that an argument is needed
		// then resend the message using that argument
		if (CWT.EVENT_SELECTEDITEM == constant) {
			super.triggerEvent(constant, this.getSelectedItem());
			return;
		} 
		if (CWT.EVENT_SELECTEDITEMS == constant) {
			super.triggerEvent(constant, this.getSelectedItems());
			return;
		} 		
		super.triggerEvent(constant);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((List) this);
	}
}
