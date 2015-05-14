package com.philemonworks.typewise.tools;

import com.philemonworks.writer.HTMLWriter;

public class NoStatistics implements TrafficEventHandler {
	public void handleRequestReceived() {
	}
	public void handleResponseSend() {
	}
	public void handleRequestSize(int requestSize) {
	}
	public void handleResponseSize(int responseSize) {
	}
	public void handleSessionCreated(String applicationName) {
	}
	public void handleSessionDestroyed() {
	}
	public void writeMonitorReportOn(HTMLWriter html) {
		html.print("No traffic statistics are available");
	}
}
