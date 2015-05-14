package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtList is
 *
 * @author E.M.Micklei
 */
public class SwtList extends List implements SwtWidget, SelectionListener, KeyListener {
	private static final long serialVersionUID = 3258694312249209143L;
	SwtApplicationView view;
	org.eclipse.swt.widgets.List swt;
	String pattern = "";

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtList(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.List(parent, SWT.V_SCROLL);
		swtView.initializeSWTWidget(swt, this);
		swt.addSelectionListener(this);
		swt.addKeyListener(this);
	}

	public void widgetSelected(SelectionEvent arg0) {
		this.setSelectionIndex(swt.getSelectionIndex());
	}

	public void widgetDefaultSelected(SelectionEvent arg0) {
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
	 * Override from super
	 */
	public void setItems(java.util.List newItems) {
		super.setItems(newItems);
		swt.setItems(SwtUtils.asStringArray(newItems));
	}		
	public void basicSetSelectionIndex(int index) {
		swt.select(index);
		super.basicSetSelectionIndex(index);
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

	public void keyPressed(KeyEvent arg0) {
		if ((arg0.stateMask & SWT.CTRL) != 0) { // CONTROL
			view.getScreen().keyPressed(arg0);
			return;
		}
		if (arg0.keyCode == 27) { // ESC
			arg0.doit = false;
			pattern = "";
			view.setStatus(pattern);
			return;
		}
		if (arg0.keyCode == 8) { // Backspace
			arg0.doit = false;
			pattern = pattern.substring(0, Math.max(0,pattern.length() - 1));
			view.setStatus(pattern);
			// If no pattern then stay here
			if (pattern.length() == 0) return;
			// walk backward to first match
			int index = swt.getSelectionIndex();
			int nextIndex = 0;
			for (int s = index - 1; s > 0; s--) {
				String each = swt.getItem(s);
				if (!each.startsWith(pattern)) {
					nextIndex = s + 1;
					break;
				}
			}
			swt.setSelection(nextIndex);
			return;
		}
		if (!Character.isLetterOrDigit(arg0.character)) {
			view.getScreen().keyPressed(arg0);
			return;
		}
		// consume it
		arg0.doit = false;
		int index = pattern.length() == 0 ? -1 : swt.getSelectionIndex();
		pattern += arg0.character;
		// walk forward to first match
		for (int s = index + 1; s < swt.getItemCount(); s++) {
			String each = swt.getItem(s).toLowerCase();
			if (each.startsWith(pattern)) {
				swt.setSelection(s);
				break;
			}
		}
		view.setStatus(pattern);
	}		

	public void keyReleased(KeyEvent arg0) {
		view.getScreen().keyReleased(arg0);
	}	
}
