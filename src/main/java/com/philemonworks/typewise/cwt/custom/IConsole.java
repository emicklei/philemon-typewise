package com.philemonworks.typewise.cwt.custom;

import com.philemonworks.typewise.message.MessageReceiver;


public interface IConsole extends MessageReceiver{
	public void append(String s);

	public void clear();

	/**
	 * Answer the command line interpreter for the console.
	 * 
	 * @return CommandLineInterpreter
	 */
	public CommandLineInterpreter getInterpreter();

	public void replace(String s);

	public void setInterpreter(CommandLineInterpreter newInterpreter);
	
	public void prompt(String p);
}
