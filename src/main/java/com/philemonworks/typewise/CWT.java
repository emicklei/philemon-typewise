/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise;

/**
 *  
 */
public interface CWT {
	// Events
	public static final byte EVENT_CLICKED = 4;
	public static final byte EVENT_COUNTER_END = 6;
	public static final byte EVENT_COUNTED = 8;
	public static final byte EVENT_SELECTEDITEM = 1;
	public static final byte EVENT_SELECTEDITEMS = 16;
	public static final byte EVENT_DEFAULTACTION = 3;
	public static final byte EVENT_GETTINGFOCUS = 2;
	public static final byte EVENT_LOSINGFOCUS = 0;
	public static byte EVENT_F1 = 44, EVENT_SHIFT_F1 = 68;
	public static byte EVENT_F2 = 46, EVENT_SHIFT_F2 = 70;
	public static byte EVENT_F3 = 48, EVENT_SHIFT_F3 = 72;
	public static byte EVENT_F4 = 50, EVENT_SHIFT_F4 = 74;
	public static byte EVENT_F5 = 52, EVENT_SHIFT_F5 = 76;
	public static byte EVENT_F6 = 54, EVENT_SHIFT_F6 = 78;
	public static byte EVENT_F7 = 56, EVENT_SHIFT_F7 = 80;
	public static byte EVENT_F8 = 58, EVENT_SHIFT_F8 = 82;
	public static byte EVENT_F9 = 60, EVENT_SHIFT_F9 = 84;
	public static byte EVENT_F10 = 62, EVENT_SHIFT_F10 = 86;
	public static byte EVENT_F11 = 64, EVENT_SHIFT_F11 = 88;
	public static byte EVENT_F12 = 66, EVENT_SHIFT_F12 = 90;
	// Label, Button, GroupBox
	public static final byte LEFT = 1;
	public static final byte CENTER = 2;
	public static final byte RIGHT = 3;
	// Dialog buttons
	public static final byte OK = 1;
	public static final byte CANCEL = 2;
	public static final byte YES = 3;
	public static final byte NO = 4;
}