/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * SwtMenuBar is
 *
 * @author E.M.Micklei
 */
public class SwtMenuBar extends MenuBar implements SwtWidget {
	private static final long serialVersionUID = 3257572810389270836L;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtMenuBar(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
		// TODO Auto-generated constructor stub
	}

	public void addToView(SwtApplicationView swtView, Composite parent) {
		// TODO Auto-generated method stub
		
	}

	public void removeFromView(SwtApplicationView swtView) {
		// TODO Auto-generated method stub
		
	}

	public void updateFont(Font newFont) {
		// TODO Auto-generated method stub
		
	}
}
