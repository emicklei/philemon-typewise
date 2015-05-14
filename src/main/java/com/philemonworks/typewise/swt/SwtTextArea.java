/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.internal.ColorLookup;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtTextArea is
 * 
 * @author E.M.Micklei
 */
public class SwtTextArea extends TextArea implements SwtWidget, MouseListener, FocusListener {
	private static final long serialVersionUID = 3257572784703353392L;
	SwtApplicationView view;
	Text swt;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtTextArea(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Text(parent, SWT.MULTI | SWT.WRAP);
		swtView.initializeSWTWidget(swt, this);
		swt.addFocusListener(this);
	}
	protected void basicSetString(String newString) {
		super.basicSetString(newString);
		swt.setText(newString);
	}
	/**
	 * Make the receiver have the focus
	 */
	public void setFocus() {
		swt.setFocus();
	}
	public void updateFont(Font newFont) {
		swt.setFont(newFont);
		view.updateBounds(swt, this);
	}
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;
	}
	public void setAppearance(WidgetAppearance appearance) {
		super.setAppearance(appearance);
		if (swt != null)
			SwtUtils.setColors(ColorLookup.backgroundFor(this), ColorLookup.foregroundFor(this), swt, view);
	}
	public void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		if (swt != null)
			swt.setEnabled(isEnabled);
	}
	public void setEditable(boolean isEditable){
		super.setEditable(isEditable);
		if (swt != null)
			swt.setEditable(isEditable);
	}		
	public void mouseDoubleClick(MouseEvent arg0) {
	}
	public void mouseDown(MouseEvent arg0) {
	}
	public void mouseUp(MouseEvent arg0) {
		this.setText(swt.getText());
	}
	public void focusGained(FocusEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public void focusLost(FocusEvent arg0) {	
		// Logger.getLogger(this.getClass()).debug(this.toString()+" lost focus with text=" + swt.getText());
		this.setText(swt.getText());
	}
}
