/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, renaming or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.boa;

/**
 * BinaryObjectReadException is a special exception that is thrown
 * in the context of the BinaryObjectAccessor.
 * 
 * @author mvhulsentop
 */
public class BinaryObjectReadException extends Exception {
	private static final long serialVersionUID = 3256720667519891504L;

	public BinaryObjectReadException(Exception original) {
		super(original);
	}
	public BinaryObjectReadException(String errMessage) {
		super(errMessage);
	}
	public BinaryObjectReadException(Exception original, String errMessage) {
		super(errMessage, original);
	}
}
