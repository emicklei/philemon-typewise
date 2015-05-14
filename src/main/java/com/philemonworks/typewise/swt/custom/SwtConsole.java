package com.philemonworks.typewise.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import com.philemonworks.typewise.cwt.custom.CommandLineInterpreter;
import com.philemonworks.typewise.cwt.custom.Console;
import com.philemonworks.typewise.swt.client.SwtApplicationView;
import com.philemonworks.typewise.swt.client.SwtWidget;

/**
 * Console is a wrapper for the SWT Text widget that is a view for a CommandLineInterpreter.
 * 
 * @author E.M.Micklei
 */
public class SwtConsole extends Console implements KeyListener, SwtWidget {
	/**
	 * The SWT Text
	 */
	Text text;
	/**
	 * Caret position where the command begins.
	 */
	int lineBegin = 0;
	/**
	 * Boolean to indicate whether hitting TAB will leave the Console widget
	 */
	boolean traverseOnTab = false;

	public SwtConsole(String arg0, int arg1, int arg2, int arg3, int arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}
	/**
	 * Answer the SWT text widget;
	 * 
	 * @return Text
	 */
	public Text getText() {
		return text;
	}
	/**
	 * Handle a key event. Some events are processed, some are ignored and some are prevented from being processed
	 * further. If a command was entered, the forward this to the CommandLineInterpreter.
	 */
	public void keyPressed(KeyEvent arg0) {
		// System.out.println(arg0.keyCode);
		// If a selection was made using the mouse that starts before the
		// command than ignore all key events
		Point selpt = text.getSelection();
		if (selpt.x < lineBegin) {
			arg0.doit = false;
			return;
		}
		if (arg0.keyCode == 8) { // Backspace
			if (text.getCaretPosition() == lineBegin) {
				arg0.doit = false;
				return;
			}
		}
		if (arg0.keyCode == 9) { // Tab
			int here = text.getCaretPosition();
			String line = text.getText(lineBegin, here);
			interpreter.tabbed(line, this);
			arg0.doit = false;
			return;
		}
		if (arg0.keyCode == 13) { // Return
			int here = text.getCaretPosition();
			String line = text.getText(lineBegin, here);
			// Make sure any response starts on a new line
			this.newLine();
			int before = text.getCaretPosition();
			interpreter.interpret(line, this);
			// Maybe the console was killed
			if (text.isDisposed())
				return;
			here = text.getCaretPosition();
			if (here > 0 && before != here)
				this.newLine();
			this.prompt();
			arg0.doit = false;
		}
		if (arg0.keyCode == SWT.ARROW_UP || arg0.keyCode == SWT.ARROW_DOWN || arg0.keyCode == SWT.PAGE_UP || arg0.keyCode == SWT.PAGE_DOWN) {
			arg0.doit = false;
			return;
		}
	}
	/**
	 * Key release is ignored
	 * 
	 * @param arg0
	 *        KeyEvent
	 */
	public void keyReleased(KeyEvent arg0) {
	}
	/**
	 * Clear the complete text contents. After processing the command that called this method, the console will show the
	 * (new) prompt.
	 */
	public void clear() {
		text.setText("");
	}
	/**
	 * Append more text to the current command.
	 * 
	 * @param newText
	 *        String
	 */
	public void append(String newText) {
		text.insert(newText);
	}
	/**
	 * Replace the current command with this text
	 * 
	 * @param newText
	 */
	public void replace(String newText) {
		text.setSelection(lineBegin, text.getCaretPosition());
		text.cut();
		text.insert(newText);
	}
	public void prompt(String newPrompt) {
		text.append(newPrompt);
		lineBegin = text.getCaretPosition();
	}
	public void newLine() {
		text.append("\n");
	}
	// Methods delegated to the interpreter
	public void attached() {
		interpreter.attached(this);
	}
	public void detached() {
		interpreter.detached(this);
	}
	public void interpret(String command) {
		interpreter.interpret(command, this);
	}
	public void tabbed(String command) {
		interpreter.tabbed(command, this);
	}
	public void prompt() {
		interpreter.prompt(this);
	}
	public void addToView(SwtApplicationView swtView, Composite parent) {
		text = new Text(parent, SWT.WRAP | SWT.MULTI);
		text.addKeyListener(this);
		this.setInterpreter(new SwtCommandLineDispatcher(swtView.getMessageDispatcher()));
		swtView.initializeSWTWidget(text, this);
	}
	public void removeFromView(SwtApplicationView swtView) {
		text.dispose();
	}
	public void updateFont(Font newFont) {
		// TODO Auto-generated method stub
	}
	/**
	 * Set the interprester for the console
	 * Overrides from super.
	 * @param interpreter
	 *        CommandLineIntepreter
	 */
	public void setInterpreter(CommandLineInterpreter newInterpreter) {
		this.interpreter = newInterpreter;
	}	
}
