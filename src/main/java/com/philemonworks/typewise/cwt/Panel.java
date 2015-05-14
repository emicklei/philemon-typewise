/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 10-jan-2004 by emicklei
 * 2004-03-24: refactored to Screen
 * */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 *
 * <code>Panel</code> is the simplest container class. A panel
 * provides space in which an application can attach any other
 * component, including other panels.
 *
 */
public class Panel extends CompositeWidget implements BinaryAccessible {

	public Panel(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Panel) this);		
	}
	public void kindDo(WidgetHandler requestor){
		requestor.handle((Panel)this);
	}
    public int rowTranslation(){ return getTopRow() - 1; }
    public int columnTranslation(){ return getLeftColumn() - 1; }
}
