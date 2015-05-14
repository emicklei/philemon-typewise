package com.philemonworks.typewise.swt;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtChoice is
 * 
 * @author E.M.Micklei
 */
public class SwtChoice extends Choice implements SwtWidget, SelectionListener, org.eclipse.swt.events.KeyListener {
	private static final long serialVersionUID = 3906363814122696758L;
	Combo swt;
	SwtApplicationView view;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtChoice(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	/**
	 * 
	 */
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Combo(parent, SWT.PUSH | SWT.SHADOW_NONE);
		swtView.initializeSWTWidget(swt, this);
		swt.addSelectionListener(this);
		swt.addKeyListener(this);
	}
	public void widgetSelected(SelectionEvent arg0) {
		this.setSelectionIndex(swt.getSelectionIndex());
	}
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}
	public void basicSetSelectionIndex(int index) {
		super.basicSetSelectionIndex(index);
		if (swt.getSelectionIndex() != index) swt.select(index);
	}	
	/**
	 * Override from super
	 */
	public void setItems(List newItems) {
		super.setItems(newItems);
		swt.setItems(SwtUtils.asStringArray(newItems));
	}	
	/**
	 * Override from super
	 */
	public void triggerEvent(byte eventID,Object argument){
		Message msg = this.messageForEvent(EVENT_SELECTEDITEM);
		if (msg == null) return;
		view.getMessageDispatcher().dispatch(msg.setArgument(0,argument));
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
		// future: use character to find next in list.
		view.getScreen().keyPressed(arg0);
	}
	public void keyReleased(KeyEvent arg0) {
	}	
}
