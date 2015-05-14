package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtCheckBox is
 * 
 * @author E.M.Micklei
 */
public class SwtCheckBox extends CheckBox implements SwtWidget, SelectionListener , KeyListener{
	private static final long serialVersionUID = 3905236836229133874L;
	org.eclipse.swt.widgets.Button swt;
	SwtApplicationView view;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtCheckBox(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Button(parent, SWT.CHECK);
		swtView.initializeSWTWidget(swt, this);
		swt.addSelectionListener(this);
		swt.addKeyListener(this);
	}
	public void widgetSelected(SelectionEvent arg0) {
		this.setSelection(swt.getSelection());
		view.handleEventFrom(EVENT_CLICKED,this);
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
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.swt.client.SwtWidget#updateFont(org.eclipse.swt.graphics.Font)
	 */
	public void updateFont(Font newFont) {
		swt.setFont(newFont);		
		view.updateBounds(swt,this);
	}	
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;		
	}
	public void keyPressed(KeyEvent arg0) {
		view.getScreen().keyPressed(arg0);		
	}
	public void keyReleased(KeyEvent arg0) {
	}	
	public void basicSetSelection(boolean newSelection){
		super.basicSetSelection(newSelection);
		swt.setSelection(newSelection);
	}
}
