/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtComposite is
 *
 * @author E.M.Micklei
 */
public class SwtComposite extends Panel implements SwtWidget {
	private static final long serialVersionUID = 3762253037095170101L;
	org.eclipse.swt.widgets.Composite swt;
	SwtApplicationView view;
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtComposite(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new Composite(parent,SWT.BORDER);
		swtView.initializeSWTWidget(swt, this);
		SwtUtils.addAllToView(swtView,swt, this.getWidgets());
	}

	public void removeFromView(SwtApplicationView swtView) {
		SwtUtils.removeAllFromView(swtView,this.getWidgets());
		// kids are gone, now it is my turn
		swt.dispose();
		view = null;
	}

	public void updateFont(Font newFont) {
		SwtUtils.updateFontForAll(newFont,this.getWidgets());
	}
}
