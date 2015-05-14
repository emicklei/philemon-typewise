/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on 19-dec-2003 by emicklei
 *
 * */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 *
 * Purpose:
 *
 */
public class TextArea extends TextField  implements BinaryAccessible {

	public TextArea(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName,topRow,leftColumn,howManyRows,howManyColumns);
	}
	/**
	 * @param textLines
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
		bwa.write((TextArea) this);
	}
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((TextArea) this);
	}
	/*
	 * Compute the textlines as they will appear in the display
	 */
	public ArrayList computeTextLines(){
		// TODO Use StringTokenizer
		StringReader reader = new StringReader(this.getString());
		int lineSize = 0;
		StringBuffer line = new StringBuffer();
		ArrayList lines = new ArrayList();
		boolean done = false;
		while (!done){
			try {
				int ch = reader.read();
				if (ch == -1) break;
				if (ch == (int)'\n') {
					lines.add(line.toString());
					line = new StringBuffer();
					lineSize = 0;
				} else {
					lineSize++;
					if (lineSize > this.getColumns()){
						lines.add(line.toString());
						line = new StringBuffer();
						lineSize = 1;
					}
					line.append((char)ch);
				}
			} catch (IOException ex) {
				done  = true;
			}
		}
		if (lineSize>0) lines.add(line.toString());
		return lines;	
	}		
}
