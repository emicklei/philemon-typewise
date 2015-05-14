package com.philemonworks.typewise.swt.client;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import com.philemonworks.typewise.message.MessageSequence;


/**
 * SwtWidget is an interface declaring methods required for all classes 
 * created using the SwtWidgetFactory.
 *
 * @author E.M.Micklei
 */
public interface SwtWidget {
	/**
	 * Add the receiver to the shell of the 
	 * @param swtView  SwtApplicationView
	 * @param parent TODO
	 */
	void addToView(SwtApplicationView swtView, Composite parent);
	/**
	 * Remove the receiver from the shell of the 
	 * @param swtView  SwtApplicationView
	 */
	void removeFromView(SwtApplicationView swtView);
	/**
	 * The current Font has changed to the argument.
	 * Update the control and resize accordingly.
	 * @param newFont Font
	 */
	void updateFont(Font newFont);
    /**
     * If the state of the widget has changed since the last update
     * then add a Message to the sequence to update the state on the other side.
     * @param seq MessageSequence
     */
    void addStateMessagesTo(MessageSequence seq);
}
