/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtTimer is
 *
 * @author E.M.Micklei
 */
public class SwtTimer extends Timer implements SwtWidget {
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtTimer(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	private static final long serialVersionUID = 3256721784194742069L;
	SwtApplicationView view;
	Text swt;	

	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Text(parent, SWT.NONE | SWT.READ_ONLY);
		swtView.initializeSWTWidget(swt, this);
		swt.setText(String.valueOf(this.getStart()));
	}

	public void removeFromView(SwtApplicationView swtView) {
		view = null;
		swt.dispose();		
	}

	public void updateFont(Font newFont) {
		swt.setFont(newFont);		
		view.updateBounds(swt,this);
	}
	protected void basicSetString(String newString){
	    super.basicSetString(newString);
		swt.setText(newString);
	}	
}
