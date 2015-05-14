package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;
import com.philemonworks.util.Color;

/**
 * SwtButton is a special Button for implementing an SWT client.
 *
 * @author E.M.Micklei
 */
public class SwtButton extends Button implements MouseListener, KeyListener, SwtWidget, FocusListener {
	private static final long serialVersionUID = 3257285812084356402L;
	SwtApplicationView view;
	Control swt;
	
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtButton(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	/**
	 * Add the receiver to the shell of the 
	 * @param swtView  SwtApplicationView
	 */
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		if (view.useNativeLook()) {
			swt = new org.eclipse.swt.widgets.Button(parent, SWT.FLAT);
			swtView.initializeSWTWidget(swt, this);
			this.resizeLabelAsButton();
		} else {
			swt = new org.eclipse.swt.widgets.Label(parent, SWT.NONE);
			// because center alignment is default for button, the label has to be initialized that way
			((org.eclipse.swt.widgets.Label)swt).setAlignment(SWT.CENTER);
			swtView.initializeSWTWidget(swt, this);	
			this.resizeLabelAsButton();
		}			
		swt.addKeyListener(this);
		swt.addMouseListener(this);
		swt.addFocusListener(this);
		if (isDefault())
			view.getScreen().setDefaultButton(this);
	}
	private void resizeLabelAsButton(){
		Rectangle box = swt.getBounds();
		int delta = view.charheight / 3;
		swt.setBounds(box.x,box.y,box.width,box.height+delta);
	}	
	protected void basicSetString(String newString){
	    super.basicSetString(newString);
		if (view.useNativeLook()) {
			org.eclipse.swt.widgets.Button bswt = (org.eclipse.swt.widgets.Button)swt;
			bswt.setText(newString);
			// bswt.pack();
		}
		else
			((Label)swt).setText(newString);
	}
	public void keyPressed(KeyEvent arg0) {
	}
	public void keyReleased(KeyEvent arg0) {
		if (arg0.character == (char) 13)
			view.handleEventFrom(EVENT_CLICKED,this);
		else
			view.getScreen().keyPressed(arg0); 
	}
	public void mouseDoubleClick(MouseEvent arg0) {
	}
	public void mouseDown(MouseEvent arg0) {
	}
	public void mouseUp(MouseEvent arg0) {
		view.handleEventFrom(EVENT_CLICKED,this);
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
		this.resizeLabelAsButton();
	}
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;		
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.typewise.cwt.Label#setAlignment(byte)
	 */
	public void setAlignment(byte constant) {		
		super.setAlignment(constant);
		if (!view.useNativeLook())
			((Label)swt).setAlignment(SwtUtils.toSWTAlignment(constant));
	}
	public void focusGained(FocusEvent arg0) {
		view.setStatus(this.getHelp());		
	}
	public void focusLost(FocusEvent arg0) {		
	}	
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_ENABLED) {
			if (swt != null)
				swt.setEnabled(((Boolean)aMessage.getArgument()).booleanValue());
			return;
		}
		super.dispatch(aMessage);
	}
}
