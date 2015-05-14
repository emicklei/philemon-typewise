package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/** 
 * Link is a widget that displays an acts like a hyperlink.
 *
 * @author E.M.Micklei
 */
public class Link extends Label {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7745128625740567564L;

	public Link(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Link) this);		
	}
}
