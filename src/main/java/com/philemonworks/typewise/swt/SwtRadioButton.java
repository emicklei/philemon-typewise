/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtRadioButton is
 * 
 * @author E.M.Micklei
 */
public class SwtRadioButton extends RadioButton implements SwtWidget, SelectionListener, org.eclipse.swt.events.KeyListener {
	private static final long serialVersionUID = 3256442512452694069L;
	org.eclipse.swt.widgets.Button swt;
	SwtApplicationView view;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtRadioButton(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Button(parent, SWT.RADIO);
		swtView.initializeSWTWidget(swt, this);
		swt.addSelectionListener(this);
		swt.addKeyListener(this);
	}
	public void widgetSelected(SelectionEvent arg0) {
		this.setSelection(swt.getSelection());
		Message msg = this.messageForEvent(EVENT_SELECTEDITEM);
		if (msg == null) return;
		msg.setArgument(0,this.getItem());
		view.handleEventFrom(EVENT_SELECTEDITEM,this);
	}
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}
	protected void basicSetString(String newString){
	    super.basicSetString(newString);
		swt.setText(newString);
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
	}	
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;		
	}
	public void keyPressed(KeyEvent e) {
		view.getScreen().keyPressed(e);		
	}
	public void keyReleased(KeyEvent e) {
	}
	public void basicSetSelection(boolean newSelection){
		super.basicSetSelection(newSelection);
		swt.setSelection(newSelection);
	}	
}
