/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004-2006. All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author E.M.Micklei
 */
package com.philemonworks.typewise.internal;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.log4j.Logger;
import com.philemonworks.typewise.Bounds;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.Location;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageDispatcher;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;

/**
 * Widget is an abstract superclass for all terminal widgets This class provides common behavior and state for specifying a Panel that can
 * be displayed on a terminal client.
 * <ul>
 * <li>name - the unique name of the widget (is required for the constructor)</li>
 * <li>bounds - this rectangle specialization represents the area in which the receiver is displayed.</li>
 * <li>parent - this field holds the parent widget (unless the receiver is on top)</li>
 * <li>eventTable - this private table holds event to message connections for the receiver</li>
 * </ul>
 */
public abstract class Widget extends WidgetAppearanceHolder implements CWT, Serializable {
    private Bounds bounds;
    private HashMap eventTable = new HashMap();
    private Widget parent;
    private boolean visible = true;

    /**
     * Constructor for this Widget class.
     * Checks whether the name is unique and whether the bounds are within that of the container (if any).
     * 
     * @param newName :
     *                unique name of the Widget
     * @param topRow :
     *                1-based integer to specify the row coordinate of the origin
     * @param leftColumn :
     *                1-based integer to specify the column coordinate of the origin
     * @param howManyRows :
     *                integer number of rows occupied by the widget
     * @param howManyColumns :
     *                integer number of columns occupied by the widget
     */
    public Widget(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
        super();
        name = newName;
        this.checkValidName();
        this.setBounds(new Bounds(topRow, leftColumn, howManyRows, howManyColumns));
        // Clear dirty flag for BOUNDS
        this.clearDirty(WidgetAccessorConstants.GET_BOUNDS);
    }

    /**
     * Add client messages to aSequence that will install all event connections specified for the receiver
     * 
     * @param aSequence :
     *                the message sequence containing Message objects
     */
    public void addEventConnectionsTo(MessageSequence aSequence) {
        Map.Entry anEntry;
        Iterator i = eventTable.entrySet().iterator();
        while (i.hasNext()) {
            anEntry = (Map.Entry) i.next();
            IndexedMessageSend action = new IndexedMessageSend(this.getName(), MessageConstants.OPERATION_installMessageOnEvent);
            action.addArgument(anEntry.getValue()); // message
            action.addArgument(anEntry.getKey()); // event
            aSequence.add(action);
        }
    }

    public void addStateMessagesTo(MessageSequence messageSequence) {
        super.addStateMessagesTo(messageSequence);
        if (this.isDirty(WidgetAccessorConstants.GET_BOUNDS))
            messageSequence.add(this.setBounds_AsMessage(this.getBounds()));
        if (this.isDirty(WidgetAccessorConstants.GET_VISIBLE))
            messageSequence.add(this.setVisible_AsMessage(this.isVisible()));
    }

    /**
     * Check that the childWidget is completely inside the receivers bounds.
     * Child widgets have relative locations.
     * @param childWidget Widget
     */
    public void checkRoomFor(Widget childWidget) {
        if (bounds.includesRowColumn(childWidget.getTopRow()+this.getTopRow()-1, childWidget.getLeftColumn()+this.getLeftColumn()-1 ))
            if (bounds.includesRowColumn(childWidget.getBottomRow()+this.getTopRow()-1, childWidget.getRightColumn()+this.getLeftColumn()-1))
                return;
        ErrorUtils.error(this, "\n\twidget:" + childWidget.toString() + "\n\trequires space outside of: " + this.toString());
    }

	/**
	 * Determines whether instance of this class can trigger the event.
	 * Throws a RuntimeException when it does not (because it is considered a fatal error).
	 * @param eventID : event contant
	 * @see CWT
	 */
    public void checkTriggersEvent(byte eventID) {
        if (eventID == EVENT_CLICKED)
            return;
        ErrorUtils.error(this, "unknown event:" + (DebugExplainer.eventConstant(eventID)) + " for:" + this.toString());
    }

    /**
     * Verify that the argument newName is unique for all widgets as part of the receiver. Unless the receiver has this name;
     */
    public void checkUniqueName(String newName) {
        Widget existing = this.widgetNamed(newName);
        if (existing == this)
            return;
        if (existing != null)
            ErrorUtils.error(this, "Widget name \""+newName+"\" already taken by " + existing);
    }

    public void checkValidName() {
        if (name == null)
            ErrorUtils.error(this, "name is unspecified; widgets must have an unique name");
        if (name.startsWith("_"))
            ErrorUtils.error(this, "widget name may not start with underscore:" + name);
    }

    public void dispatch(IndexedMessageSend aMessage) {
		switch (aMessage.getIndex()){
			case WidgetAccessorConstants.SET_BOUNDS : this.setBounds((Bounds) aMessage.getArgument()); break;
			case WidgetAccessorConstants.SET_VISIBLE : this.setVisible(((Boolean) aMessage.getArgument()).booleanValue()); break;
			case MessageConstants.OPERATION_SETFOCUS : this.setFocus(); break;
			case MessageConstants.OPERATION_installMessageOnEvent :	this.onSend(((Number) aMessage.getArguments()[1]).byteValue(),(Message)aMessage.getArgument()); break;
			default : super.dispatch(aMessage); 
		}		
    }

    public Location screenLocation(){
    	if (parent == null) return new Location(this.getTopRow(),this.getLeftColumn());
    	return parent.screenLocation().add(this.getTopRow()-1,this.getLeftColumn()-1);
    }
    
    /**
     * @return int row at the bottom of the receiver's bounds. 
     */
    public int getBottomRow() {
        return bounds.getBottomRow();
    }

    /**
     * @return the bounds of the receiver
     */
    public Bounds getBounds() {
        return bounds;
    }

    /**
     * @return the number of columns taken by the receiver
     */
    public int getColumns() {
        return bounds.getColumns();
    }
	
	/**
	 * Change the number or columns of the bounds. 
	 * @param newColumns int
	 */
	public void setColumns(int newColumns){
		Bounds old = this.getBounds();
		old.setColumns(newColumns);
		this.setBounds(old);
	}

	/**
	 * Change the number or rows of the bounds.
	 * @param newRows int
	 */
	public void setRows(int newRows){
		Bounds old = this.getBounds();
		old.setRows(newRows);
		this.setBounds(old);
	}
	
    /**
     * Return the left column of the bounds.
     * @return int
     */
    public int getLeftColumn() {
        return bounds.getLeftColumn();
    }

    /**
     * @return the parent Widget (or null if this is the root)
     */
    public Widget getParent() {
        return parent;
    }

    /**
     * Return the right side column taken by the receiver.
     * @return int 
     */
    public int getRightColumn() {
        return bounds.getRightColumn();
    }

    /**
     * Return the number of rows taken by the receiver
     * @return int 
     */
    public int getRows() {
        return bounds.getRows();
    }

    /**
     * @return the top row of the receiver
     */
    public int getTopRow() {
        return bounds.getTopRow();
    }

    /**
     * @return the top most widget of the receiver's widget composition hierachy.
     */
    public Widget getTopWidget() {
        if (parent == null)
            return this;
        else
            return parent.getTopWidget();
    }

    /**
     * @return the row translation of the receiver with respect to the container
     */
    protected int rowTranslation() {
        return 0;
    }

    /**
     * @return the column translation of the receiver with respect to the container
     */
    protected int columnTranslation() {
        return 0;
    }

    /**
     * @return whether the widget is visible. TODO not yet supported by client
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Visitor method
     * @param requestor WidgetHandler
     */
    public abstract void kindDo(WidgetHandler requestor);

    /**
     * Set the bounds to the new value. Check whether there is (still) room for this receiver.
     * 
     * @param newBounds :
     *                Bounds
     */
    public void setBounds(Bounds newBounds) {
		if (newBounds.equals(this.bounds)) return;
        this.bounds = newBounds;
        this.markDirty(WidgetAccessorConstants.GET_BOUNDS);
        if (parent != null)
            parent.checkRoomFor(this);
    }

    private Message setBounds_AsMessage(Bounds newBounds) {
        IndexedMessageSend setter = new IndexedMessageSend(this.getAccessPath(), WidgetAccessorConstants.SET_BOUNDS);
        setter.addArgument(newBounds);
        return setter;
    }

    /**
     * Set the name. Check whether this is (still) unique within the widget composition hierarchy.
     * 
     * @param newName
     */
    public void setName(String newName) {
        // The name is part of the statis state
        this.getTopWidget().checkUniqueName(newName);
        name = newName;
    }

    /**
     * Set the parent Widget (or null) for the receiver.
     * 
     * @param newParentWidgetOrNull
     */
    public void setParent(Widget newParentWidgetOrNull) {
        parent = newParentWidgetOrNull;
    }

    public void setSize(int rows, int columns) {
        // Change the size of the receiver using @rows and @columns
        // Because we change our dimension, re-check for room available using my
        // parent
        Bounds newBounds = new Bounds(bounds.getTopRow(), bounds.getLeftColumn(), rows, columns);
        this.setBounds(newBounds);
    }

    /**
     * @param visible
     *                The visible to set.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        this.markDirty(WidgetAccessorConstants.GET_VISIBLE);
    }

    private Message setVisible_AsMessage(boolean isIt) {
        IndexedMessageSend setter = new IndexedMessageSend(this.getAccessPath(), WidgetAccessorConstants.SET_VISIBLE);
        setter.addArgument(new Boolean(isIt));
        return setter;
    }

    /**
     * Return a debug description using the format {classname}[{widgetname},{bounds}]
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ErrorUtils.displayString(this));
        buffer.append("[\"");
        buffer.append(this.getName());
        buffer.append("\",");
        buffer.append(bounds);
        buffer.append(']');
        return buffer.toString();
    }

    /**
     * Install an event handler that will send the message newMsg when the event is triggered.
     * 
     * @param eventID :
     *                one of the EVENT constants
     * @param newMsg :
     *                an Message implementor instance
     */
    public void onSend(byte eventID, Message newMsg) {
        // First see if we can trigger the event at all
        this.checkTriggersEvent(eventID);
        Message msg = this.messageForEvent(eventID);
        if (msg == null) {
            eventTable.put(new Byte(eventID), newMsg);
        } else {
            MessageSequence seq = msg.asSequence();
            seq.add(newMsg);
            eventTable.put(new Byte(eventID), seq);
        }
    }

    /**
     * Install an event handler that will send the
     * 
     * @operationName to the object found by
     * @receiverID
     * @param eventID :
     *                one of the EVENT constants
     * @param operationName :
     *                name of the public method defined by the object named receiverID
     *  
     */
    public void onSendTo(byte eventID, String operationName, String receiverID) {
        this.onSend(eventID, new MessageSend(receiverID, operationName));
    }

    /**
     * Install an event handler that will send the
     * 
     * @operationName to aWidget
     */
    public void onSendTo(byte eventID, String operationName, MessageReceiver receiver) {
        if (Logger.getLogger(Widget.class).isDebugEnabled()) {
            // check method existence
            if (!MessageDispatcher.canUnderstand(receiver, operationName)) {
                ErrorUtils.handleNoSuchMethod(null, receiver, operationName, new Class[0]);
                return;
            }
        }
        this.onSend(eventID, new MessageSend(receiver.getAccessPath(), operationName));
    }

    public void onSendTo(byte eventID, int operationIndex, MessageReceiver receiver) {
        this.onSend(eventID, new IndexedMessageSend(receiver.getAccessPath(), operationIndex));
    }

    public void onSendToWith(byte eventID, int operationIndex, MessageReceiver receiver, Object argument) {
        IndexedMessageSend msg = new IndexedMessageSend(receiver.getAccessPath(), operationIndex);
        msg.addArgument(argument);
        this.onSend(eventID, msg);
    }

    /**
     * Install an event handler that will send the
     * 
     * @param operationName with the argument
     * @param argument to the object found by
     * @param receiverID
     */
    public void onSendToWith(byte eventID, String operationName, MessageReceiver receiver, Object argument) {
        MessageSend msg = new MessageSend(receiver.getAccessPath(), operationName);
        msg.addArgument(argument);
        this.onSend(eventID, msg);
    }

    public void triggerEvent(byte eventID) {
        Message msg = this.messageForEvent(eventID);
        if (msg == null)
            return;
        Screen top = (Screen) this.getTopWidget();
        if (top == null)
            return;
        if (top.getModel() == null){
			Logger.getLogger(Widget.class).warn("Screen has no model so message ["+msg+"] cannot be performed.");
            return;
        }
        msg.resolveAndDispatchToUsing(top.getModel(), new MessageDispatcher());
    }
	/**
	 * Return the message that was installed for the event or null if none was installed.
	 * @param eventID byte
	 * @return Message || null
	 */
	public Message messageForEvent(byte eventID) {
		Byte event = new Byte(eventID);
		Message msg = (Message) eventTable.get(event);
		return msg;
	}

    /**
     * Send all messages that were installed for this eventID. Pass the argument to each message.
     * @param eventID
     * @param argument
     */
    public void triggerEvent(byte eventID, Object argument) {
        Message msg = this.messageForEvent(eventID);
        if (msg == null)
            return;
        Screen top = (Screen) this.getTopWidget();
        if (top == null)
            return;
		// Note: the model may be null (for client operation)
        msg.dispatchToUsing(top.getModel(), new MessageDispatcher(argument));
    }

    /**
     * Answer the receiver if its name matches
     * 
     * @param widgetName,
     *                null otherwise
     * @return the receiver is that is its name
     */
    public Widget widgetNamed(String widgetName) {
        if (name == null)
            return null;
        if (this.getName().equals(widgetName))
            return this;
        else
            return null;
    }
    /**
     * Answer whether the receiver includes the point specified by the row,column pair
     * @param row : int
     * @param column : int
     * @return true if the point is within the bounds of the receiver
     */
    public Widget widgetUnderPoint(int row, int column) {
        if (bounds.includesRowColumn(row, column))
            return this;
        return null;
    }
    /** 
     * @param eventID the EVENT constant
     * @return whether there is at least one message installed for this event
     */
    public boolean hasMessageForEvent(byte eventID) {
    	Message msg = messageForEvent(eventID);
    	return msg != null;
    }
	/**
	 * On the server-side, this method has no effect.
	 */
	public void setFocus(){}
}