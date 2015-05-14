package com.philemonworks.typewise.cwt.custom;

import java.io.IOException;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

public class Console extends Widget implements IConsole, BinaryAccessible {
	private static final long serialVersionUID = 211830726761405390L;
	private MessageSequence messageBuffer = new MessageSequence();
	/**
	 * The interpreter that receives the commands entered.
	 */
	protected CommandLineInterpreter interpreter;

	public Console(String arg0, int arg1, int arg2, int arg3, int arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}
	public void kindDo(WidgetHandler arg0) {
		arg0.handle(this);
	}
	public void append(String s) {
		messageBuffer.add(new MessageSend(this, "append", s));
	}
	public void replace(String s) {
		messageBuffer.add(new MessageSend(this, "replace", s));
	}
	public void clear() {
		messageBuffer.add(new MessageSend(this, "clear"));
	}
	public void prompt(String s) {
		messageBuffer.add(new MessageSend(this, "prompt", s));
	}
	public void prompt() {
		interpreter.prompt(this);
	}
	/**
	 * Answer the command line interpreter for the console.
	 * 
	 * @return CommandLineInterpreter
	 */
	public CommandLineInterpreter getInterpreter() {
		return interpreter;
	}
	/**
	 * Set the interprester for the console
	 * 
	 * @param interpreter
	 *        CommandLineIntepreter
	 */
	public void setInterpreter(CommandLineInterpreter newInterpreter) {
		if (interpreter != null) {
			interpreter.detached(this);
			this.newLine();
		}
		this.interpreter = newInterpreter;
		interpreter.attached(this);
		this.newLine();
		interpreter.prompt(this);
	}
	public void newLine() {
		messageBuffer.add(new MessageSend(this, "newLine"));
	}
	public void addStateMessagesTo(MessageSequence arg0) {
		super.addStateMessagesTo(arg0);
		arg0.add(messageBuffer);
		// Reset
		messageBuffer = new MessageSequence();
	}
	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
		bwa.write((Console) this);
	}
	public void interpret(String command) {
		interpreter.interpret(command, this);
	}
}
