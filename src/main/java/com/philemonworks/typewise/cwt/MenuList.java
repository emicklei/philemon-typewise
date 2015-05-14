/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 10, 2004 by emicklei
 * 4-0622: changed MessageSend to Message, addLine
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
/**
 * @author emicklei
 *
 */
public class MenuList extends List implements BinaryAccessible {

	public MenuList(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
		bwa.write((MenuList) this);
	}	
	public void kindDo(WidgetHandler wh) {
		wh.handle((MenuList) this);
	}
	public void add(MenuItem anItem){
	    items.add(anItem);
	}
	public void addLine(){
		items.add(new MenuSeparator());
	}	
}
