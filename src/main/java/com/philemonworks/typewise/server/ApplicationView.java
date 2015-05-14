/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise.server;

import java.io.Serializable;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.internal.DebugExplainer;
import com.philemonworks.typewise.internal.ErrorUtils;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetLookup;
import com.philemonworks.typewise.internal.WidgetLookupHelper;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageConstants;
import com.philemonworks.typewise.message.MessageReceiver;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;

/**
 * ApplicationView is a surrogate for the real client (type is one of stmt,html,....)
 * Methods invoked on the receiver are translated to messages that are queued 
 * until ready to send back the reply to the real client.
 * Every instance of an ApplicationModel, within one Http-session, will share the same
 * instance of ApplicationView to communicate to the client or accessing the state of
 * widgets of the active Screen.
 * 
 * @author emicklei
 */
public class ApplicationView implements MessageConstants, MessageReceiver, WidgetLookup, Serializable {
	private static final long serialVersionUID = 3257572784702763830L;
	/**
	 * Denotes that the client is a standard HTML Web browser. 
	 */
	public static final String TYPE_HTML = "html";
	/**
	 * Denotes that the client is a SmalltalkMT for Windows implementation.
	 */
	public static final String TYPE_STMT = "stmt";
	
	private Screen currentScreen;
    private String httpSessionId;
    private MessageSequence reply;
	private String type = TYPE_STMT; // on default, the client is a SmalltalkMT exe.
	private WidgetLookupHelper widgetFinder = new WidgetLookupHelper(this);

    /**
     * Constructor, only used by ApplicationModel as part of its default initialization.
     */
    public ApplicationView() {
        super();
        this.initializeReply();
    }	
	/**
	 * This message should only be sent by the actual client to
	 * notify the application that it will be closed. The client will
	 * send no other message to the application. 
	 * Signal the request processor by throwing a special exception.
	 * This method is public because that is required for message send by the client.
	 */
	public void aboutToClose() {
        // Cleanup everything by de-referencing
        currentScreen = null;
		// Signal an invalidation request ; no more message are handled
        throw new SessionInvalidationRequestNotification();
    }
	/**
	 * Request to exit the current session
	 * @quick
	 */
	public void exit(){
		this.replyAdd(CLOSE);
	}
	/**
	 * Request to invalidate the session. The request processor (an ApplicationServlet)
	 * will take approriate actions. 
	 */
	public void invalidateSession(){
		// This will cause invalidation of the session by the ApplicationServlet
		// but not until all server messages are handled.
		httpSessionId = null;
	}
	/**
     * (non-Javadoc)
     * 
     * @see com.philemonworks.typewise.message.MessageReceiver#dispatch(com.philemonworks.typewise.message.IndexedMessageSend)
     */
    public void dispatch(IndexedMessageSend anIndexedMessageSend) {
        int index = anIndexedMessageSend.getIndex();
        if (OPERATION_OPENURL == index){
            this.openURL((String)anIndexedMessageSend.getArgument());
            return;
        }
        ErrorUtils.handleExceptionWithUserMessage(null,"Client does not understand:" + DebugExplainer.explainFor(anIndexedMessageSend, this));
    }
	/**
	 * Return an identifier for constructing a MessageSend such that the receiver can be resolved 
	 * when dispatching messages
	 * @return String : the identifier
	 */
    public String getAccessPath() {
        return CLIENT;
    }
	/**
	 * Lookup the Button widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Button
	 */			
    public Button getButton(String widgetName) {
		return widgetFinder.getButton(widgetName);
    }
	/**
	 * Lookup the CheckBox widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return CheckBox
	 */			
    public CheckBox getCheckBox(String widgetName) {
        return widgetFinder.getCheckBox(widgetName);
    }
	/**
	 * Lookup the Choice widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Choice
	 */		
	public Choice getChoice(String widgetName) {
        return widgetFinder.getChoice(widgetName);
    }
	/**
	 * Adaptor method to access the ApplicationModel that is currently receiving messages
	 * @guide
	 * @return ApplicationModel : the active ApplicationModel
	 */
	protected ApplicationModel getCurrentModel() {
        // Pre: currentScreen not null
        return currentScreen.getModel();
    }
	/**
	 * Returns the Screen that is currently displayed by the client for the active application
	 * @guide 
	 * @return Screen: the active Screen
	 */
    public Screen getCurrentScreen() {
        return currentScreen;
    }
	/**
	 * Lookup the GroupBox widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return GroupBox
	 */	
    public GroupBox getGroupBox(String widgetName) {
        return widgetFinder.getGroupBox(widgetName);
    }
	/**
	 * Lookup the Image widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Image
	 */		
    public Image getImage(String widgetName) {
        return widgetFinder.getImage(widgetName);
    }
	/**
	 * Lookup the Label widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Label
	 */			
    public Label getLabel(String widgetName) {
        return widgetFinder.getLabel(widgetName);
    }
	/**
	 * Lookup the List widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return List
	 */		
    public List getList(String widgetName) {
        return widgetFinder.getList(widgetName);
    }
	/**
	 * Lookup the Panel widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Panel
	 */	
    public Panel getPanel(String widgetName) {
        return widgetFinder.getPanel(widgetName);
    }
	/**
	 * Lookup the RadioButton widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return RadioButton
	 */
    public RadioButton getRadioButton(String widgetName) {
        return widgetFinder.getRadioButton(widgetName);
    }
	/**
	 * Lookup the TableList widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return TableList
	 */		
    public TableList getTableList(String widgetName) {
        return widgetFinder.getTableList(widgetName);
    }
	/**
	 * Lookup the TextArea widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return TextArea
	 */		
    public TextArea getTextArea(String widgetName) {
        return widgetFinder.getTextArea(widgetName);
    }
	/**
	 * Lookup the TextField widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return TextField
	 */	
    public TextField getTextField(String widgetName) {
        return widgetFinder.getTextField(widgetName);
    }
	/**
	 * Lookup the Timer widget using the given name.
	 * @guide
	 * @param widgetName String
	 * @return Timer
	 */
    public Timer getTimer(String widgetName) {
        return widgetFinder.getTimer(widgetName);
    }
    /**
     * Return the type of the receiver which is an indication of its implementation.
	 * @return String : the type 
	 */
	public String getType(){ return type; }
	/**
	 * Answer whether the receiver is part of a HttpSession
	 * @return boolean : true if session is known
	 */
    public boolean hasSession(){
        return httpSessionId != null;
    }
	/**
	 * Initialize the queue of reply messages to be send back to the client
	 */
    private void initializeReply() {
        reply = new MessageSequence();
    }
    /** 
     * Add a message to the queue for the client to open a resource registered for this URL.
     * @param url
     */
    public void openURL(String url) {
        this.replyAdd(new IndexedMessageSend(this.getAccessPath(), OPERATION_OPENURL).addArgument(url));
    }
    /** 
     * Add a message to the queue for the client to goto the Application registered for this URL.
     * @param url
     */
    public void gotoURL(String url) {
        this.replyAdd(new IndexedMessageSend(this.getAccessPath(), OPERATION_GOTOURL).addArgument(url));
    }    
	/**
	 * UNTESTED operation
	 * @param widgetName :  String
	 */
    public void removeWidget(String widgetName) {
        MessageSend msg = new IndexedMessageSend(this.getAccessPath(), OPERATION_remove);
        msg.addArgument(widgetName);
        this.replyAdd(msg);
    }
	/**
	 * Add a message to the queue for the client
	 * @param aMessage
	 */
    protected void replyAdd(Message aMessage) {
        reply.add(aMessage);
    }
    /**
     * Add a message that transfers the focus to the widget. 
     * Invoke this method only after a Screen is displayed (typically inside the body of afterShow()).
     * @param aWidget non-null widget that will have focus.
     */
    public void setFocusTo(Widget aWidget){
        this.replyAdd(new IndexedMessageSend(aWidget.getAccessPath(),OPERATION_SETFOCUS));
    }
    protected void setSessionId(String mySessionId) {
        httpSessionId = mySessionId;
    }
	/**
	 * Tell the client to show a new status.
	 * @param newStatus String
	 */
    public void setStatus(String newStatus) {
        // this.replyAdd(OK);
        this.replyAdd(new IndexedMessageSend(this.getAccessPath(), OPERATION_STATUS).addArgument(newStatus));
    }
    /**
	 * Set the type of the receiver which is an indication of its implementation.
	 * Check whether a valid type is set.
	 * @param clientType
	 */
	public void setType(String clientType){
		if (clientType == TYPE_HTML) type = clientType;
		else if (clientType == TYPE_STMT) type = clientType;
		else throw new RuntimeException("Unknown client type:" + clientType);		
	}
    /**
	 * Requests the client to show a Dialog. 
	 * @param aDialog : Dialog
	 */
    public void show(Dialog aDialog) {
        // add a message for showing the dialog
        MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_SHOWDIALOG);
        msg.addArgument(aDialog);
        this.replyAdd(msg);
        // state message and dirty clearance will happen when the reply is taken
    }
    /**
	 * Request the client to show a Screen. 
	 * @param aScreen : Screen
	 */
    public void show(Screen aScreen) {
        aScreen.getModel().beforeShow();
        currentScreen = aScreen;
        // add a message for showing the screen
        MessageSend msg = new IndexedMessageSend(CLIENT, OPERATION_SHOW);
        msg.addArgument(aScreen);
        this.replyAdd(msg);
        // get post open message from model (if any)
        currentScreen.getModel().afterShow();
        // add messages to connect the event-to-actions
        aScreen.addEventConnectionsTo(reply);
        // state message and dirty clearance will happen when the reply is taken
    }
    /**
     * Construct a reply message sequence from changes of the current screen
     * Answer the reply and prepare for the next request
     * @return reply Message
     */
    public Message takeReply() {
        if (currentScreen != null) {
            // 	add messages to set the state of all widgets
            currentScreen.addStateMessagesTo(reply);
            // Client must clear dirty flags after handling all messages
            // iff reply notEmpty
            if (!reply.isEmpty()) {
                this.replyAdd(CLIENT_CLEARDIRTYALL);
                // Server must clear dirty flags now
                currentScreen.clearDirtyAll();
            }
        }
        if (reply.isEmpty()) {
            return OK;
        } else {
            Message answer = reply;
            // reset the reply
            this.initializeReply();
            return answer;
        }
    }
}