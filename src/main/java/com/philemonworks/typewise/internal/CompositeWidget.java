/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 8-dec-2003 by emicklei
 * 16-2-2004: mvhulsentop: added addStateMessagesTo
 * 22-6-2004: emm: widgetNamed
 * 9-7-4: emm :  clearDirtyAll
 * */
package com.philemonworks.typewise.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.log4j.Logger;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * CompositeWidget is an abstract widget container.
 * 
 * @author emicklei
 */
public class CompositeWidget extends Widget {
	private static final long serialVersionUID = 3689066253121632567L;
	private List widgets = new ArrayList();

	/**
	 * @param newName String
	 * @param topRow int 
	 * @param leftColumn int 
	 * @param howManyRows int 
	 * @param howManyColumns int
	 */
	public CompositeWidget(
		String newName,
		int topRow,
		int leftColumn,
		int howManyRows,
		int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	public void add(Widget childWidget) {
	    this.checkUniqueName(childWidget.getName());
		this.checkRoomFor(childWidget);
		childWidget.setParent(this);
		widgets.add(childWidget);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
		bwa.write((CompositeWidget) this);
	}
	/**
	 * For each widget recursively, reset all dirty flags.
	 */
	public void clearDirtyAll(){
	    this.clearDirty();
		for (int w=0;w<widgets.size();w++) {
			Widget each = (Widget) widgets.get(w);
			each.clearDirtyAll();
		}
	}
	
	public void setDirtyAll(){
	    this.setDirty();
		for (int w=0;w<widgets.size();w++) {
			Widget each = (Widget) widgets.get(w);
			each.setDirtyAll();
		}
	}
	/**
	 * @return the list of child widgets
	 */
	public List getWidgets() {
		return widgets;
	}
	/*
	 * @see com.philemonworks.typewise.Widget#widgetNamed(java.lang.String)
	 */
	public Widget widgetNamed(String widgetName){
	    if (widgetName.equals(name)) return this;
		for (int w=0;w<widgets.size();w++) {
			Widget each = (Widget) widgets.get(w);
			Widget found = each.widgetNamed(widgetName);
			if (found != null) return found;
		}		
;		return null;
	}
	public Widget widgetUnderPoint(int row, int column) {

		for (int w = 0; w < widgets.size(); w++) {
			Widget each = (Widget) widgets.get(w);
			Widget hit = each.widgetUnderPoint(row, column);
			if (hit != null)
				return hit;
		}
		return super.widgetUnderPoint(row, column);
	}
	/**
	 * @param widgets
	 */
	public void setWidgets(List widgets) {
		this.widgets = widgets;
	}
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		for (int count = 0; count < widgets.size(); count++) {
			((Widget) widgets.get(count)).addStateMessagesTo(seq);
		}
	}
	public void addEventConnectionsTo(MessageSequence aSequence) {
		super.addEventConnectionsTo(aSequence);
		for (int count = 0; count < widgets.size(); count++) {
			((Widget) widgets.get(count)).addEventConnectionsTo(aSequence);
		}
	}
	public void kindDo(WidgetHandler wh) {
		wh.handle((CompositeWidget) this);
	}
    public int rowTranslation(){ return getTopRow() - 1; }
    public int columnTranslation(){ return getLeftColumn() - 1; }	
}
