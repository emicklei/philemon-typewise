/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 */
package com.philemonworks.typewise.server;
import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;

/**
 *  Objects that can represent themselves in BWA format must implement this interface.
 */
public interface BinaryAccessible {
	/**
	 * Serialize the receiver in BWA format using a BinaryWidgetAccessor
	 * @param bwa : BinaryWidgetAccessor
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException;
}
