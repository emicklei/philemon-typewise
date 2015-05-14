/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtTableList is
 *
 * @author E.M.Micklei
 */
public class SwtTableList extends TableList implements SwtWidget, SelectionListener , KeyListener{
	private static final long serialVersionUID = 3256719585070822455L;
	SwtApplicationView view;
	Table swt;	
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtTableList(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		int selectFlag = this.isMultiSelect() ? SWT.MULTI : SWT.SINGLE;
		swt = new Table(parent, selectFlag | SWT.FULL_SELECTION );
		swt.setHeaderVisible(true);
		// Only show lines if the focus color was not specified
		if (this.getFocusColor() == null)
			swt.setLinesVisible(true);
		swtView.initializeSWTWidget(swt, this);
		// Build columns
		int[] widths = this.computeColumnWidths(view.charwidth);
		for (int t = 0; t < this.getTableColumns().size(); t++) {
			TableColumn each = (TableColumn) this.getTableColumns().get(t);
			org.eclipse.swt.widgets.TableColumn column = new org.eclipse.swt.widgets.TableColumn(swt, SWT.NONE);
			column.setWidth(widths[t]);
			column.setText(each.getHeading().getText());
			String key = this.getName() + "." + t + ".label";
		}
		swt.addSelectionListener(this);
		swt.addKeyListener(this);
	}
	private void updateColumnWidths(){
		int[] widths = this.computeColumnWidths(view.charwidth);
		for (int t = 0; t < this.getTableColumns().size(); t++) {
			swt.getColumn(t).setWidth(widths[t]);
		}
	}
	public void widgetSelected(SelectionEvent arg0) {
		if (this.isMultiSelect())
			this.setSelectionIndices(swt.getSelectionIndices());
		else
			this.setSelectionIndex(swt.getSelectionIndex());
		
	}
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}
	/**
	 * Override from super. (but forgot why)
	 */
	public void triggerEvent(byte eventID,Object argument){
		Message msg = this.messageForEvent(EVENT_SELECTEDITEM);
		if (msg != null)
			view.getMessageDispatcher().dispatch(msg.setArgument(0,argument));
		msg = this.messageForEvent(EVENT_SELECTEDITEMS);
		if (msg != null)
			view.getMessageDispatcher().dispatch(msg.setArgument(0,argument));		
	}	
	public void setItems(List newItems) {
		swt.removeAll();
		// Create TableItem from TableListItems
		ArrayList tableItems = new ArrayList(newItems.size());
		boolean odd = true;
		Color focusColor = SwtUtils.toSWT(this.getFocusColor(),swt.getDisplay());
		for (Iterator iter = newItems.iterator(); iter.hasNext();) {
			TableListItem element = (TableListItem) iter.next();
			TableItem each = new TableItem(swt, SWT.NONE);
			for (int i = 0; i < element.getItems().length; i++) {
				each.setText(i, element.getItems()[i]);				
			}
			each.setForeground(swt.getForeground());
			// Only paint the row if the focus color was specified for the Table
			// @todo enhance TableListItem with color info
			if (!odd && focusColor != null)
				each.setBackground(focusColor);
			else
				each.setBackground(swt.getBackground());
			odd = !odd;
		}
		swt.setData(tableItems);
		this.items = newItems;
		this.resetSelections();
	}
	public void setColumnLabel(IndexedMessageSend msg) {
		// receiver=@widgetName.@columnIndex.label
		StringTokenizer tokenizer = new StringTokenizer(msg.getReceiver(), ".");
		tokenizer.nextToken(); // receiver
		int columnIndex = (Integer.valueOf(tokenizer.nextToken())).intValue();
		swt.getColumn(columnIndex).setText((String) msg.getArgument());
	}	
	public void basicSetSelectionIndex(int index) {
		if (swt != null) swt.select(index);
		super.basicSetSelectionIndex(index);
	}
	public void basicSetSelectionIndices(int[] indices){
		if (swt != null) swt.select(indices);
		super.basicSetSelectionIndices(indices);
	}
	/**
	 * Make the receiver have the focus
	 */
	public void setFocus(){
		swt.setFocus();
	}	
	public void updateFont(Font newFont) {
		swt.setFont(newFont);
		view.updateBounds(swt,this);
		this.updateColumnWidths();
	}	
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;		
	}
	public void keyPressed(KeyEvent arg0) {
		// future: use character from event to find next in list
		view.getScreen().keyPressed(arg0);
	}
	public void keyReleased(KeyEvent arg0) {
		view.getScreen().keyReleased(arg0);
	}	
}
