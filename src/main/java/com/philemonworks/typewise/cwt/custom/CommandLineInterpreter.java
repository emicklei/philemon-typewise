package com.philemonworks.typewise.cwt.custom;

public interface CommandLineInterpreter {
	public void attached(IConsole view);
	public void detached(IConsole view);
	public void interpret(String command, IConsole view);
	public void tabbed(String command, IConsole view);
	public void prompt(IConsole view);
}
