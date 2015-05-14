/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 *  
 * */
package com.philemonworks.typewise.internal;

import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.util.Color;

/**
 * WidgetAppearanceHolder is the abstract class for all objects that can have a WidgetAppearance assigned. A
 * WidgetAppearance can either be specified by the name of a style (from a StyleSheet) or directly by an instance of
 * WidgetAppearance.
 * 
 * @see com.philemonworks.typewise.StyleSheet
 */
public abstract class WidgetAppearanceHolder implements CWT, MessageReceiver {
	protected WidgetAppearance appearance = null;
	/**
	 * flags to indicate that some dynamic state has been changed. (includes bounds)
	 */
	protected int dirty = 0; // 0xffffffff;
	/**
	 * unique name for the receiver
	 */
	protected String name;
	/**
	 * name of the style which be can lookup in the StyleSheet of the top widget, a Screen.
	 */
	private String styleName = null;

	public void addStateMessagesTo(MessageSequence messageSequence) {
		if (isDirty(WidgetAccessorConstants.SET_APPEARANCE))
			messageSequence.add(this.setAppearance_AsMessage(this.getAppearance()));
	}
	/**
	 * Unmark the receiver as dirty.
	 */
	public void clearDirty() {
		dirty = 0;
	}
	/**
	 * Unmark the dirty flag constant
	 * 
	 * @param constant
	 */
	public void clearDirty(int constant) {
		// Pre: constant was marked (use XAND to be sure?)
		dirty -= constant;
	}
	/**
	 * Unmark the receiver and all of its child widgets as dirty. (recursively clear all).
	 */
	public void clearDirtyAll() {
		this.clearDirty();
	}
	public void dispatch(IndexedMessageSend anIndexedMessageSend) {
		if (anIndexedMessageSend.getIndex() == WidgetAccessorConstants.SET_APPEARANCE) {
			this.setAppearance((WidgetAppearance) anIndexedMessageSend.getArgument());
			return;
		}
		// Indexedmessage not understood
		ErrorUtils.error(this, "Unhandled IndexedMessageSend:" + (DebugExplainer.explainFor(anIndexedMessageSend, this)));
	}
	public String getAccessPath() {
		return name;
	}
	/**
	 * Answer the WidgetAppearance for the receiver. If none was specified (directly or by style name) then return a
	 * new.
	 * 
	 * @return WidgetAppearance
	 */
	public WidgetAppearance getAppearance() {
		// Lazy initialize a new WidgetAppearance because we are about to change
		// it
		// locally for the receiver
		if (appearance == null) {
			if (styleName == null) {
				appearance = new WidgetAppearance();
			} else {
				appearance = ((Screen) this.getTopWidget()).getStyleSheet().get(styleName);
			}
		}
		return appearance;
	}
	/**
	 * Answer the WidgetAppearance for the receiver. If none was specified (directly or by style name) then return null.
	 * 
	 * @return WidgetAppearance
	 */
	public WidgetAppearance getAppearanceOrNull() {
		if (this.hasAppearance())
			return this.getAppearance();
		else
			return null;
	}
	/**
	 * @return the background color as specified by the receiver's appearance.
	 */
	public Color getBackground() {
		return this.getAppearance().getBackground();
	}
	/**
	 * @return the border color as specified by the receiver's appearance.
	 */
	public Color getBorderColor() {
		return this.getAppearance().getBorderColor();
	}
	/**
	 * @return the disabled foreground color as specified by the receiver's appearance.
	 */
	public Color getDisabledForeground() {
		return this.getAppearance().getDisabledForeground();
	}
	/**
	 * @return the focus color as specified by the receiver's appearance.
	 */
	public Color getFocusColor() {
		return this.getAppearance().getFocusColor();
	}
	/**
	 * @return the foreground color as specified by the receiver's appearance.
	 */
	public Color getForeground() {
		return this.getAppearance().getForeground();
	}
	/**
	 * @return the name of the receiver.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the parent Widget of the receiver or null if no parent was specified.
	 */
	public Widget getParent() {
		return null;
	}
	/**
	 * @return the selection background color as specified by the receiver's appearance.
	 */
	public Color getSelectionBackground() {
		return this.getAppearance().getSelectionBackground();
	}
	/**
	 * @return the selection foreground color as specified by the receiver's appearance.
	 */
	public Color getSelectionForeground() {
		return this.getAppearance().getSelectionForeground();
	}
	/**
	 * Answer the topmost widget from the hierarchy of the receiver
	 */
	public abstract Widget getTopWidget();
	/**
	 * @return whether the receiver has specified (directly or by style name) a WidgetAppearance.
	 */
	public boolean hasAppearance() {
		return appearance != null | styleName != null;
	}
	/**
	 * When an appearance property is directly set, then forget about the style
	 */
	private void invalidateStyle() {
		styleName = null;
	}
	/**
	 * @return whether the receiver is dirty (in any of its state aspects).
	 */
	public boolean isDirty() {
		return dirty != 0;
	}
	/**
	 * @param constant bitpattern
	 * @return whether one particular aspect of the state is dirty.
	 */
	public boolean isDirty(int constant) {
		return (dirty & constant) == constant;
	}
	/**
	 * Mark the appearance aspect of the state as dirty
	 */
	public void markAppearanceDirty() {
		// Only mark the flag when the client already shows the receiver
		if (this.getParent() == null)
			return;
		if (!(this.getTopWidget() instanceof Screen))
			return;
		Screen top = (Screen) this.getTopWidget();
		if (top.getModel() == null)
			return;
		if (top.getModel().view.getCurrentScreen() != top)
			return;
		this.markDirty(WidgetAccessorConstants.SET_APPEARANCE);
	}
	/**
	 * Mark one particular aspect of the state as dirty.
	 * 
	 * @param constant
	 * @see com.philemonworks.typewise.internal.WidgetAccessorConstants
	 */
	protected void markDirty(int constant) {
		dirty |= constant;
	}
	/**
	 * Set the appearance
	 * 
	 * @param appearance
	 *        WidgetAppearance or null
	 */
	public void setAppearance(WidgetAppearance appearance) {
		this.appearance = appearance;
		this.markAppearanceDirty();
	}
	/**
	 * @param newAppearance
	 * @return a Message for setting the appearance of the receiver using newAppearance
	 */
	protected Message setAppearance_AsMessage(WidgetAppearance newAppearance) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(), WidgetAccessorConstants.SET_APPEARANCE);
		setter.addArgument(this.getAppearance());
		return setter;
	}
	/**
	 * Set the background color.
	 * 
	 * @param aColor
	 *        Color or null
	 */
	public void setBackground(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setBackground(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the border color.
	 * 
	 * @param aColor
	 *        Color or null
	 */
	public void setBorderColor(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setBorderColor(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Mark the receiver as dirty (all aspects of the state).
	 */
	public void setDirty() {
		dirty = 0xFFFF;
	}
	/**
	 * Set a particular state aspect for the receiver as dirty
	 * 
	 * @param constant
	 * @see com.philemonworks.typewise.internal.WidgetAccessorConstants
	 */
	public void setDirty(int constant) {
		// Pre: constant was marked (use XAND to be sure?)
		dirty += constant;
	}
	/**
	 * Markt the receiver and all its child widgets as dirty.
	 */
	public void setDirtyAll() {
		this.setDirty();
	}
	/**
	 * Set the new color for the disabled foreground
	 * 
	 * @param aColor :
	 *        Color | null
	 */
	public void setDisabledForeground(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setDisabledForeground(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the new color for the focus color
	 * 
	 * @param aColor :
	 *        Color | null
	 */
	public void setFocusColor(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setFocusColor(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the new color for the foreground
	 * 
	 * @param aColor :
	 *        Color | null
	 */
	public void setForeground(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setForeground(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the new color for the selection background
	 * 
	 * @param aColor :
	 *        Color | null
	 */
	public void setSelectionBackground(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setSelectionBackground(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the new color for the selection foreground
	 * 
	 * @param aColor :
	 *        Color | null
	 */
	public void setSelectionForeground(Color aColor) {
		this.invalidateStyle();
		this.getAppearance().setSelectionForeground(aColor);
		this.markAppearanceDirty();
	}
	/**
	 * Set the style for receiver which must be defined by the stylesheet of the Screen
	 */
	public void setStyle(String nameOfStyle) {
		if (styleName != null)
			if (styleName.equals(nameOfStyle))
				return;
		styleName = nameOfStyle;
		appearance = null;
		this.markAppearanceDirty();
	}
}