/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 24-jun-2005: created
 *
 */
package com.philemonworks.typewise.internal;

public interface WidgetAccessorConstants {

	// WidgetAppearanceHolder
	public static final int GET_APPEARANCE = 2;
	public static final int SET_APPEARANCE = 3;
	// Widget
	public static final int GET_VISIBLE = 4;
	public static final int SET_VISIBLE = 5;
	public static final int GET_BOUNDS = 8;
	public static final int SET_BOUNDS = 9;
	// BasicWidget
	public static final int GET_ENABLED = 16;
	public static final int SET_ENABLED = 17;
	// RadioButton, Checkbox
	public static final int GET_SELECTION = 32;
	public static final int SET_SELECTION = 33;
	// List
	public static final int GET_ITEMS = 64;
	public static final int SET_ITEMS = 65;
	public static final int GET_SELECTEDITEM = 128;
	public static final int SET_SELECTEDITEM = 129;
	// Timer
	public static final int GET_COUNT = 256;
	public static final int SET_COUNT = 257;
	public static final int GET_DELAY = 512;
	public static final int SET_DELAY = 513;
	public static final int GET_REPEAT = 1024;
	public static final int SET_REPEAT = 1025;
	// GroupBox, Button, Label, TextField (no alignment)
	public static final int GET_STRING = 2048;
	public static final int SET_STRING = 2049;
	public static final int GET_ALIGNMENT = 4096;
	public static final int SET_ALIGNMENT = 4097;
	// TextField, TextArea
	public static final int GET_EDITABLE = 8192;
	public static final int SET_EDITABLE = 8193;
	// Image
	public static final int GET_URL = 16384;
	public static final int SET_URL = 16385;
	public static final int GET_SCALETOFIT = 32768;
	public static final int SET_SCALETOFIT = 32768;
	public static final int GET_TITLE = 65536;
	public static final int SET_TITLE = 65537;
	public static final int GET_ITEM = 262144;
	public static final int SET_ITEM = 262145;
	// No getter for interval
	public static final int SET_INTERVAL = 524289;
	public static final int GET_SELECTIONINDEX = 1048576;
	public static final int SET_SELECTIONINDEX = 1048577;
	public static final int GET_TICKALIGNMENT = 1048576*2;
	public static final int SET_TICKALIGNMENT = 1048576*2+1;
	public static final int GET_SELECTIONINDICES = 1048576*4;
	public static final int SET_SELECTIONINDICES = 1048576*4+1;	
}
