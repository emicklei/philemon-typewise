/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 11, 2004 by mvhulsentop
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 *  
 */
public class Choice extends List implements BinaryAccessible {
    private int maximumVisibleRows = -1;

    public Choice(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
        super(newName, topRow, leftColumn, howManyRows, howManyColumns);
    }
    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((Choice) this);
    }
    public void kindDo(WidgetHandler requestor) {
        requestor.handle((Choice) this);
    }
    public int getMaximumVisibleRows() {
        return maximumVisibleRows;
    }
    public void setMaximumVisibleRows(int i) {
        maximumVisibleRows = i;
    }
    protected void handleReplacedItems() {
        if (items.size() == 0)
            super.handleReplacedItems();
        else
            this.setSelectionIndex(-1); // do not try to preserve selectionIndex
    }
}