/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) copyright PhilemonWorks B.V. 2004 - All rights reserved.
 * Use, duplication or disclosure restricted. See http://www.philemonworks.com
 * 
 * VERSION HISTORY
 * 2004-01-01: created by emicklei
 * 2004-06-20: addItem(DisplayTableItem) 
 * 
 * */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * TableList is a special List that contains data organized in rows and columns.
 * 
 * @author ernest.micklei@philemonworks.com
 */
public class TableList extends List implements BinaryAccessible {
	private static final long serialVersionUID = 3977578082826007353L;
	private ArrayList tableColumns = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public TableList(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		for (int t = 0; t < tableColumns.size(); t++) {
			TableColumn each = (TableColumn) tableColumns.get(t);
			each.addStateMessagesTo(seq);
		}
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((TableList) this);
	}
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((TableList) this);
	}
	/**
	 * @return ArrayList
	 */
	public ArrayList getTableColumns() {
		return tableColumns;
	}
	/**
	 * Return the selected TableListItem or null is no selection is available
	 * 
	 * @return TableListItem || nulll
	 */
	public TableListItem getSelectedTableListItem() {
		Object item = this.getSelectedItem();
		if (item == null)
			return null;
		return (TableListItem) item;
	}
	/**
	 * Return the first TableListItem that has an id which is equal to the parameter
	 * 
	 * @param id
	 *        String
	 * @return TableListItem || null
	 */
	public TableListItem getTableItemById(String id) {
		if (id == null)
			return null;
		if (items == null)
			return null;
		for (Iterator iter = items.iterator(); iter.hasNext();) {
			TableListItem element = (TableListItem) iter.next();
			if (element.getId().equals(id))
				return element;
		}
		return null;
	}
	/**
	 * Set the collection of TableColumns
	 * @param newTableColumns : ArrayList
	 */
	public void setTableColumns(ArrayList newTableColumns) {
		// Check preconditons
		if (newTableColumns == null)
			throw new RuntimeException("[TableList] tableColumns is null");
		this.tableColumns = newTableColumns;
	}
	private int computeWidthOfColumns() {
		int width = 0;
		for (int i = 0; i < tableColumns.size(); i++)
			width += ((TableColumn) tableColumns.get(i)).getWidth();
		return width;
	}
	protected int[] computeColumnWidths(int charwidth) {
		int[] widths = new int[this.getTableColumns().size()];
		for (int t = 0; t < this.getTableColumns().size(); t++) {
			TableColumn each = (TableColumn) this.getTableColumns().get(t);
			widths[t] = each.getWidth() * charwidth;
		}
		return widths;
	}
	/**
	 * Add a new column to the rights of existings columns of the Table
	 * @param col : TableColumn
	 */
	public void add(TableColumn col) {
		// Check preconditons
		if (col == null)
			throw new RuntimeException("Attempt to add null column");
		tableColumns.add(col);
		col.setName(this.getName() + "." + (tableColumns.size() - 1));
		col.setTableList(this);
	}
	/**
	 * @deprecated use add(TableColumn)
	 */
	public void addColumn(TableColumn col) {
		this.add(col);
	}
	/**
	 * @deprecated use add(TableListItem)
	 */
	public void addItem(TableListItem newItem) {
		this.add(newItem);
	}
	/**
	 * Add the item to the list of items.
	 * The number of Strings in the item is matched agains the number of columns of the Table.
	 * @param newItem : TableListItem
	 */
	public void add(TableListItem newItem) {
		if (newItem.getItems().length != tableColumns.size()) {
			throw new RuntimeException("TableListItem items size mismatch:" + newItem.toString());
		}
		this.addItem((Object) newItem);
	}	
	/**
	 * Perform a type and collumns check on all items of newItems. For TableList, only DisplayTableItem instances are
	 * allowed. For each DisplayTableItem, the number of sub items must match with the number of collumns If one of
	 * these check fails then an Exception is thrown
	 * 
	 * @param newItems =
	 *        a list of items
	 * @throws RuntimeException
	 */
	protected void checkItemTypes(java.util.List newItems) {
		for (int i = 0; i < newItems.size(); i++) {
			Object each = newItems.get(i);
			if (!(each instanceof TableListItem))
				throw new RuntimeException("instance of TableListItem expected but found: " + each);
			if (((TableListItem) each).getItems().length != getTableColumns().size())
				throw new RuntimeException("Length of items [" + ((TableListItem) each).getItems().length + "] does not match number of columns ["
						+ getTableColumns().size() + "] for:" + each);
		}
	}
	public Widget widgetUnderPoint(int row, int column) {
		Widget who = super.widgetUnderPoint(row, column);
		if (who != this)
			return who;
		if (this.getTableColumns().size() == 0)
			return who;
		int left = this.getLeftColumn();
		for (int t = 0; t < getTableColumns().size(); t++) {
			TableColumn each = (TableColumn) getTableColumns().get(t);
			left += each.getWidth();
			if (left > column)
				return each.getHeading();
		}
		return this;
	}
	/*
	 * Answer one of <li>the receiver <li>one of the receiver's column <li>the label of one of the receiver's column
	 */
	public Widget widgetNamed(String widgetName) {
		if (widgetName.indexOf('.') == -1)
			return super.widgetNamed(widgetName);
		StringTokenizer tokenizer = new StringTokenizer(widgetName, ".");
		String nameOfWidget = tokenizer.nextToken();
		if (!nameOfWidget.equals(name))
			return null;
		if (!tokenizer.hasMoreTokens())
			return this;
		int columnIndex = (Integer.valueOf(tokenizer.nextToken())).intValue();
		TableColumn column = (TableColumn) this.getTableColumns().get(columnIndex);
		if (!tokenizer.hasMoreTokens())
			return this; // should be column
		// then it must be label
		if (tokenizer.nextToken().equals("label"))
			return this; // column.getHeading();
		else
			return this;
	}
	public void clearDirtyAll() {
		super.clearDirtyAll();
		for (int c = 0; c < this.getTableColumns().size(); c++)
			((TableColumn) this.getTableColumns().get(c)).clearDirtyAll();
	}
	public void dispatch(IndexedMessageSend msg) {
		if (msg.getIndex() == WidgetAccessorConstants.SET_STRING) {
			this.setColumnLabel(msg);
			return;
		}
		super.dispatch(msg);
	}
	protected void setColumnLabel(IndexedMessageSend msg) {
	}
}
