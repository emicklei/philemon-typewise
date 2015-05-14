/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004,2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * @author emicklei
 * 3-apr-2005: created
 *
 */
package com.philemonworks.typewise.tools;

import java.io.Serializable;
import com.philemonworks.writer.HTMLWriter;

/**
 * 
 */
public interface TrafficEventHandler extends Serializable {
    public void handleRequestReceived();
    public void handleResponseSend();
    public void handleRequestSize(int requestSize);
    public void handleResponseSize(int responseSize);
    public void handleSessionCreated(String applicationName);
    public void handleSessionDestroyed();
	public void writeMonitorReportOn(HTMLWriter html);
}
