package com.philemonworks.typewise.swt.custom;

import com.philemonworks.typewise.cwt.custom.CommandLineInterpreter;
import com.philemonworks.typewise.cwt.custom.IConsole;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.swt.client.SwtMessageDispatcher;

public class SwtCommandLineDispatcher implements CommandLineInterpreter {
	private SwtMessageDispatcher messageDispatcher;

	public SwtCommandLineDispatcher(SwtMessageDispatcher dispatcher) {
		super();
		this.messageDispatcher = dispatcher;
	}

	public void attached(IConsole view) {
		MessageSend msg = new MessageSend(view, "attached");
		messageDispatcher.postMessage(msg);
	}

	public void detached(IConsole view) {
		MessageSend msg = new MessageSend(view, "detached");
		messageDispatcher.postMessage(msg);

	}

	public void interpret(String command, IConsole view) {
		MessageSend msg = new MessageSend(view, "interpret");
		msg.addArgument(command);
		messageDispatcher.postMessage(msg);
	}

	public void tabbed(String command, IConsole view) {
		MessageSend msg = new MessageSend(view, "tabbed");
		msg.addArgument(command);
		messageDispatcher.postMessage(msg);

	}

	public void prompt(IConsole view) {
		MessageSend msg = new MessageSend(view, "prompt");
		messageDispatcher.postMessage(msg);
	}
}
