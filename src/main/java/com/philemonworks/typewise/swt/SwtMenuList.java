/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.swt;

import org.eclipse.swt.widgets.List;
import com.philemonworks.typewise.cwt.MenuList;

/**
 * SwtMenuList is
 *
 * @author E.M.Micklei
 */
public class SwtMenuList extends MenuList {
	private static final long serialVersionUID = 3257002163904198712L;
	private List swt;
	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public SwtMenuList(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
		// TODO Auto-generated constructor stub
	}
	/**
	 * Make the receiver have the focus
	 */
	public void setFocus(){
		swt.setFocus();
	}	
}
