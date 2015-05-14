package com.philemonworks.typewise.cwt;
import java.io.IOException;
import java.lang.reflect.Method;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.StyleSheet;
import com.philemonworks.typewise.CWT;
import com.philemonworks.typewise.internal.WidgetAccessorConstants;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;
/**
 * @author emicklei
 *
 */
public class Screen extends Panel implements BinaryAccessible {

    private ApplicationModel model;
	private String title = "a CWT Screen";
	private MenuBar menuBar = null;
	private StyleSheet styleSheet = null;
	/**
	 * @param newName
	 * @param aModel
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public Screen(String newName, ApplicationModel aModel, int howManyRows, int howManyColumns) {
		super(newName, 1, 1, howManyRows, howManyColumns);
		model = aModel;
	}	
	/**
	 * Answer the stylesheet for the receiver.
	 * If no sheet was specified, then create one.
	 * @return StyleSheet
	 */
	public StyleSheet getStyleSheet(){
	    if (styleSheet == null) {
	        styleSheet = new StyleSheet();
	    }
	    return styleSheet;
	}
	public ApplicationModel getModel(){
	    return model;
	}
	public void setStyleSheet(StyleSheet newStyleSheet){
	    styleSheet = newStyleSheet;
	}
	/**
	 * Determines whether instance of this class can trigger the event.
	 * Throws a RuntimeException when it does not (because it is considered a fatal error).
	 * @param eventID : event contant
	 * @see CWT
	 */
	public void checkTriggersEvent(byte eventID){
		if (eventID >= CWT.EVENT_F1 && eventID <= CWT.EVENT_F12 && (eventID % 2 == 0))	return;
		super.checkTriggersEvent(eventID);
	}
	/**
	 * @see com.philemonworks.terminal.Widget#kindDo(WidgetHandler)
	 */
	public void kindDo(WidgetHandler requestor) {
		requestor.handle((Screen) this);
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Screen) this);
	}
	public MenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new MenuBar("menuBar", this.getTopRow(), this.getLeftColumn(), this.getRows(), this.getColumns());
		}
		return menuBar;
	}
	/**
	 * Returns the title.
	 * @return String
	 */
	public String getTitle() {
		return title;
	}
	public void setMenuBarOrNull(MenuBar newBar) {
		menuBar = newBar;
	}
	public MenuBar getMenuBarOrNull() {
		// This method is for the BWA accessor
		return menuBar;
	}
	/**
	 * Sets the title.
	 * @param title The title to set
	 */
	public void setTitle(String newTitle) {
		// Check parameter
		if (newTitle == null)
			throw new RuntimeException("[Screen] title cannot be null");
		this.title = newTitle;
		this.markDirty(WidgetAccessorConstants.SET_TITLE);
	}

	public void addStateMessagesTo(MessageSequence messageSequence) {
		super.addStateMessagesTo(messageSequence);
		if (this.isDirty(WidgetAccessorConstants.SET_TITLE))
			messageSequence.add(this.setTitle_AsMessage(this.getTitle()));
	}
	/**
	 * @param title2
	 * @return
	 */
	public MessageSend setTitle_AsMessage(String title2) {
		IndexedMessageSend setter = new IndexedMessageSend(this.getName(),
				WidgetAccessorConstants.SET_TITLE);
		setter.addArgument(title2);
		return setter;
	}
	/**
	 * @see BasicWidget
	 */
	public void dispatch(IndexedMessageSend aMessage) {
		if (aMessage.getIndex() == WidgetAccessorConstants.SET_TITLE) {
			this.setTitle((String) aMessage.getArgument());
			return;
		}
		super.dispatch(aMessage);
	}
	/**
	 * Open a new Previewer on the receiver (if it is available in the classpath).
	 * A Previewer is a simple AWT based interactive rendering tool to quickly see the layout of the screen.
	 * If no Previewer was found, a error is printed to the standard console.
	 */
    public void preview() {
        // When the previewer is available, then start it using the receiver
        try {
            Class previewerClass = Class.forName("com.philemonworks.typewise.tools.Previewer");
            Method method = previewerClass.getMethod("show", new Class[] { com.philemonworks.typewise.cwt.Screen.class });
            method.invoke(previewerClass, new Object[] { this });
        } catch (ClassNotFoundException ex) {
            System.err.println("[Widget] Class not found: com.philemonworks.typewise.tools.Previewer");
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            System.err.println("[Previewer] Unable to preview:" + this.toString());
        }
    }	       
}
