package com.philemonworks.typewise.swt;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtUtils;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtLabel is
 * 
 * @author E.M.Micklei
 */
public class SwtLabel extends com.philemonworks.typewise.cwt.Label implements SwtWidget {
	private static final long serialVersionUID = 3761121639580055608L;
	SwtApplicationView view;
	org.eclipse.swt.widgets.Label swt;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtLabel(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		view = swtView;
		swt = new org.eclipse.swt.widgets.Label(parent, SwtUtils.toSWTAlignment(this.getAlignment()));
		swtView.initializeSWTWidget(swt, this);
	}
	protected void basicSetString(String newString){
	    super.basicSetString(newString);
		// Label are also used to model the header of a TableList column. In that case, no swt is created.
		if (swt != null) swt.setText(newString);
	}	
	public void setAlignment(byte constant){
		super.setAlignment(constant);
		swt.setAlignment(SwtUtils.toSWTAlignment(this.getAlignment()));
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
}
