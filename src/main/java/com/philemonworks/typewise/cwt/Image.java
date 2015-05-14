/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 11, 2004 by mvhulsentop
 * Last edited: 13-4-2004 by emm
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author mvhulsentop
 *
 * Purpose: 
 * 
 */
public class Image extends BasicWidget implements BinaryAccessible {
	private boolean scaleToFit = true;
	private String url = "";

	public Image(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void addStateMessagesTo(MessageSequence messageSequence) {
		super.addStateMessagesTo(messageSequence);
		// Need to add scaletofit first
		if (this.isDirty(WidgetAccessorConstants.SET_SCALETOFIT)) {
			messageSequence.add(this.setScaleToFit_AsMessage(scaleToFit));
		}
		if (this.isDirty(WidgetAccessorConstants.SET_URL)) {
			messageSequence.add(this.setUrl_AsMessage(url));
		}
	}
	protected Message setUrl_AsMessage(String newUrl){
		IndexedMessageSend setter = new IndexedMessageSend(name, WidgetAccessorConstants.SET_URL);
		setter.addArgument(newUrl);
		return setter;
	}
	protected Message setScaleToFit_AsMessage(boolean isScaleToFit){
		IndexedMessageSend setter = new IndexedMessageSend(name, WidgetAccessorConstants.SET_SCALETOFIT);
		setter.addArgument(new Boolean(isScaleToFit));
		return setter;
	}	
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
		bwa.write((Image) this);
		}
	public void setUrl(String uri) {		
		// Check preconditons
		if (uri == null)
			throw new RuntimeException("[Image] uri is null");		
		this.url = uri;
		this.markDirty(WidgetAccessorConstants.SET_URL);
	}
	public String getUrl() {
		return url;
	}
	/* (non-Javadoc)
	 * @see com.philemonworks.terminal.Widget#kindDo(com.philemonworks.terminal.WidgetHandler)
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle(this);
	}
	public boolean isScaleToFit() {
		return scaleToFit;
	}
	public void setScaleToFit(boolean b) {
		scaleToFit = b;
		this.markDirty(WidgetAccessorConstants.SET_SCALETOFIT);
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_SCALETOFIT) {
			this.setScaleToFit(((Boolean) aMessage.getArgument()).booleanValue());
			return;
		}
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_URL) {
			this.setUrl((String) aMessage.getArgument());
			return;
		}
		super.dispatch(aMessage);
	}
}
