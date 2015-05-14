package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtTextField is a special TextField for implementing an SWT client.
 * 
 * @author E.M.Micklei
 */
public class SwtTextField extends TextField implements KeyListener, SwtWidget, MouseListener {
	private static final long serialVersionUID = 3689915054590079795L;
	SwtApplicationView view;
	Text swt;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtTextField(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		int flags = false /* view.useNativeLook()*/ ? SWT.BORDER : SWT.NONE;
		swt = new org.eclipse.swt.widgets.Text(parent, flags);
		swtView.initializeSWTWidget(swt, this);		
		if (view.useNativeLook()){
			this.resizeAsNative();
		}
		swt.addKeyListener(this);
		if (this.getType().equals(TYPE_PASSWORD))
			swt.setEchoChar('*');
	}
	/**
	 * When using BORDER style widget then resize vertically.
	 */
	private void resizeAsNative() {
		Rectangle box = swt.getBounds();
		int delta = view.charheight / 3;
		swt.setBounds(box.x,box.y,box.width,box.height+delta);
	}
	public void keyPressed(KeyEvent arg0) {
		view.getScreen().keyPressed(arg0);
	}
	public void keyReleased(KeyEvent arg0) {
		// update model, do not use setText to prevent flashy updates
		string = swt.getText();
		this.markDirty(WidgetAccessorConstants.GET_STRING);
		if (arg0.character == (char) 13) {
			// If a message was installed for this event then send it
			Message msg = this.messageForEvent(EVENT_CLICKED);
			if (msg != null) {
				view.getMessageDispatcher().dispatch(msg);
				// otherwise let the screen take care of it.
			} else {
				view.getScreen().activateDefaultButton(arg0);
			}
		} else {
			view.getScreen().keyReleased(arg0);
		}
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
		if (view.useNativeLook()){
			this.resizeAsNative();
		}
	}
	public void removeFromView(SwtApplicationView swtView) {
		swt.dispose();
		view = null;
	}
	public void setEnabled(boolean isEnabled){
		super.setEditable(isEnabled);
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
		this.setString(swt.getText());
	}	
}
