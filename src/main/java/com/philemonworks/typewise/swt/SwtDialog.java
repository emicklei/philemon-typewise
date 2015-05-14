/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import com.philemonworks.typewise.swt.client.SwtApplicationView;

/**
 * SwtDialog is an adaptor for a CWT Dialog and SWT.
 * 
 * @author E.M.Micklei
 */
public class SwtDialog extends com.philemonworks.typewise.Dialog {
	SwtApplicationView view;

	/**
	 * @return Returns the view.
	 */
	public SwtApplicationView getView() {
		return view;
	}
	/**
	 * @param view
	 *        The view to set.
	 */
	public void setView(SwtApplicationView view) {
		this.view = view;
	}
	/**
	 * 
	 */
	public void open() {
		int boxStyle = 0;
		switch (this.getType()) {
		case OK:
			boxStyle = SWT.OK;
			break;
		case OK_CANCEL:
			boxStyle = SWT.OK | SWT.CANCEL;
			break;
		case YES_NO_CANCEL:
			boxStyle = SWT.YES | SWT.NO | SWT.CANCEL;
			break;
		}
		MessageBox box = new MessageBox(view.getShell(), boxStyle);
		box.setMessage(this.getText());
		box.setText("TypeWise - Question");
		int result = box.open();
		switch (result) {
		case SWT.OK:
			view.getMessageDispatcher().dispatch(this.getYesMessage());
			break;
		case SWT.CANCEL:
			view.getMessageDispatcher().dispatch(this.getCancelMessage());
			break;
		case SWT.YES:
			view.getMessageDispatcher().dispatch(this.getYesMessage());
			break;
		case SWT.NO:
			view.getMessageDispatcher().dispatch(this.getNoMessage());
			break;
		}
	}
}
