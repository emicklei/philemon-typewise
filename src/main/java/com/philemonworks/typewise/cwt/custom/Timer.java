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
package com.philemonworks.typewise.cwt.custom;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author mvhulsentop
 */
public class Timer extends TextField implements BinaryAccessible, CWT {
	private int delayInSeconds = 1;
	private boolean repeat = false;
	private int current;
	private int start, stop, step = 1;

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public Timer(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
		this.setEditable(false);
	}
	public void addStateMessagesTo(MessageSequence seq) {
		super.addStateMessagesTo(seq);
		if (this.isDirty(WidgetAccessorConstants.SET_INTERVAL)) {
			seq.add(this.setInterval_AsMessage(this.getStart(), this.getStop(), this.getStep()));
		}
		if (this.isDirty(WidgetAccessorConstants.SET_DELAY)) {
			seq.add(this.setDelay_AsMessage(this.getDelayInSeconds()));
		}
		if (this.isDirty(WidgetAccessorConstants.SET_REPEAT)) {
			seq.add(this.setRepeat_AsMessage(this.isRepeat()));
		}
	}
	private void checkInterval(int start, int stop, int step) {
		String errorMessage = reasonInvalidIntervalOrNull(start, stop, step);
		if (errorMessage == null) {
			return;
		} else {
			throw new RuntimeException("Incorrect interval: " + errorMessage);
		}
	}
	/**
	 * Determines whether instance of this class can trigger the event. Throws a RuntimeException when it does not
	 * (because it is considered a fatal error).
	 * 
	 * @param eventID :
	 *        event contant
	 * @see CWT
	 */
	public void checkTriggersEvent(byte eventID) {
		if (eventID == Timer.EVENT_COUNTER_END) {
			return;
		}
		super.checkTriggersEvent(eventID);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_COUNT) {
			this.setCount(((Number) aMessage.getArgument()).intValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_DELAY) {
			this.setDelayInSeconds(((Number) aMessage.getArgument()).intValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_REPEAT) {
			this.setRepeat(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_INTERVAL) {
			start = ((Integer) aMessage.getArgumentList().get(0)).intValue();
			stop = ((Integer) aMessage.getArgumentList().get(1)).intValue();
			step = ((Integer) aMessage.getArgumentList().get(2)).intValue();
			return;
		}
		super.dispatch(aMessage);
	}
	/**
	 * @param i
	 */
	private void setCount(int i) {
		current = i;
	}
	public int getCount() {
		return current;
	}
	public Message getCount_AsMessage() {
		return new IndexedMessageSend(this.getName(), WidgetAccessorConstants.GET_COUNT);
	}
	/**
	 * @return
	 */
	public int getDelayInSeconds() {
		return delayInSeconds;
	}
	/**
	 * @return
	 */
	public int getStart() {
		return start;
	}
	/**
	 * @return
	 */
	public int getStep() {
		return step;
	}
	/**
	 * @return
	 */
	public int getStop() {
		return stop;
	}
	/**
	 * @return
	 */
	public boolean isRepeat() {
		return repeat;
	}
	/**
	 * Check if provided interval is valid.
	 * 
	 * @param start
	 *        interval start parameter
	 * @param stop
	 *        interval stop parameter
	 * @param step
	 *        interval step parameter
	 * @return true if valid, false if invalid.
	 */
	public boolean isValidInterval(int start, int stop, int step) {
		if (reasonInvalidIntervalOrNull(start, stop, step) == null) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * Return a String containing the reason this interval is invalid, or null if valid.
	 * 
	 * @param start
	 *        interval start parameter
	 * @param stop
	 *        interval stop parameter
	 * @param step
	 *        interval step parameter
	 * @return a String or Null
	 */
	private String reasonInvalidIntervalOrNull(int start, int stop, int step) {
		int diff = stop - start;
		// Step must not be larger than the total interval to step.
		if (Math.abs(step) > Math.abs(diff)) {
			return "Step must fit in interval";
		}
		// Step cannot be zero.
		if (step == 0) {
			return "You cannot step zero";
		}
		// Difference can be zero; although it's not really an interval.
		if (diff == 0) {
			return null;
		}
		// Sign of both step and difference must mach.
		if ((step / Math.abs(step)) != (diff / Math.abs(diff))) {
			System.err.println(diff);
			return "Step has wrong direction, try switching sign";
		}
		// If all tests complete succesfully, this interval must be right.
		return null;
	}
	/**
	 * @param delayInSeconds2
	 * @return MessageSend
	 */
	private MessageSend setDelay_AsMessage(int delayInSeconds2) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_DELAY);
		setter.addArgument(new Integer(delayInSeconds2));
		return setter;
	}
	/**
	 * @param i
	 */
	public void setDelayInSeconds(int i) {
		delayInSeconds = i;
		this.markDirty(WidgetAccessorConstants.SET_DELAY);
	}
	public void setInterval(int start, int stop) {
		setInterval(start, stop, 1);
	}
	public void setInterval(int start, int stop, int step) /* throws RuntimeException */{
		checkInterval(start, stop, step); // Can throw a RuntimeException
		this.start = start;
		this.stop = stop;
		this.step = step;
		this.markDirty(WidgetAccessorConstants.SET_INTERVAL);
	}
	public Message setInterval_AsMessage(int start, int stop, int step) {
		IndexedMessageSend intervalSet = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_INTERVAL);
		intervalSet.addArgument(new Integer(start));
		intervalSet.addArgument(new Integer(stop));
		intervalSet.addArgument(new Integer(step));
		return intervalSet;
	}
	/**
	 * @param b
	 */
	public void setRepeat(boolean b) {
		repeat = b;
		this.markDirty(WidgetAccessorConstants.SET_REPEAT);
	}
	/**
	 * @param b
	 * @return
	 */
	private MessageSend setRepeat_AsMessage(boolean b) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_REPEAT);
		setter.addArgument(new Boolean(b));
		return setter;
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Timer) this);
	}
	/*
	 * @see com.philemonworks.typewise.Widget#kindDo(com.philemonworks.typewise.WidgetHandler)
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((Timer) this);
	}
}
