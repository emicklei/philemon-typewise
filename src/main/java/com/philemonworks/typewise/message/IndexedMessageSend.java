/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 7-apr-04: created
 *
 */
package com.philemonworks.typewise.message;

import java.io.IOException;
import java.io.Serializable;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
import com.philemonworks.util.ToStringBuilder;

/**
 * @author emicklei
 *  
 */
public class IndexedMessageSend extends MessageSend implements Serializable {
	private static final long serialVersionUID = 3257281414121928754L;
	private int index = 0;

	public IndexedMessageSend() {
		super();
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((IndexedMessageSend) this);
	}
	public Object selector() {
		return new Integer(index);
	}
	public IndexedMessageSend(String myReceiver, int myIndex) {
		super();
		this.setIndex(myIndex);
		this.setReceiver(myReceiver);
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int i) {
		index = i;
	}
    public void dispatchToUsing(MessageReceiver receiver, MessageDispatcher dispatcher) {
        dispatcher.dispatchIndexedMessageSendTo(this,receiver);
    }	
    public void resolveAndDispatchToUsing(ApplicationModel model, MessageDispatcher dispatcher) {
        dispatcher.resolveAndDispatchMessageTo(this,model);
    }     
	public String toString() {
		return ToStringBuilder.build(this);
	}	
}