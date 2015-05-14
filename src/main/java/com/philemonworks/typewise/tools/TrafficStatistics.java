/*
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

import java.util.Date;
import com.philemonworks.typewise.server.AbstractApplicationServlet;
import com.philemonworks.writer.HTMLWriter;
import com.philemonworks.writer.Table;

/**
 * 
 */
public class TrafficStatistics implements TrafficEventHandler {
    public Date lastRequestTimestamp = null;
    public int totalRequestLength = 0;
    public int totalResponseLength = 0;
    public int maximumResponseLength = 0;
    public int maximumRequestLength = 0;
    public long maximumResponseTime = 0;
    public long totalResponseTime = 0;
    public int totalSessionRequests = 0;
    public int postCount = 0;
    public int lastRequestSize = 0;
    public int lastResponseSize = 0;
    public long lastResponseTime = 0;
    public long memoryAtStartUp = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    public Date timeStamp = new Date();
    public int sessionCount = 0;
    public int activeSessionCount = 0;
    public String applicationClassName = "";
    public long now = 0;

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleRequestSize(int)
     */
    public void handleRequestSize(int requestSize) {
        totalRequestLength += requestSize;
        lastRequestSize = requestSize;
        maximumRequestLength = Math.max(maximumRequestLength, requestSize);
    }

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleResponseSize(int)
     */
    public void handleResponseSize(int responseSize) {
        lastResponseSize = responseSize;
        totalResponseLength += responseSize;
        maximumResponseLength = Math.max(maximumResponseLength, responseSize);
    }

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleSessionAdded()
     */
    public void handleSessionCreated(String applicationName) {
		applicationClassName = applicationName;
        sessionCount++;
        activeSessionCount++;
    }

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleRequestReceived()
     */
    public void handleRequestReceived() {
        lastRequestTimestamp = new Date();
        now = System.currentTimeMillis();
    }

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleSessionDestroyed()
     */
    public void handleSessionDestroyed() {
        activeSessionCount--;
    }

    /* (non-Javadoc)
     * @see com.philemonworks.typewise.tools.TrafficEventHandler#handleResponseSend()
     */
    public void handleResponseSend() {
        lastResponseTime = System.currentTimeMillis() - now;
        totalResponseTime += lastResponseTime;
        maximumResponseTime = Math.max(maximumResponseTime, lastResponseTime);
        postCount++;
    }

    public void writeMonitorReportOn(HTMLWriter html) {
        Table table;
        table = new Table();
        int row = 0;
        table.put(++row, 1, "<img src=\"typewise.png\" width=64 height=64/>");
        table.put(row, 2, "<font color=green><b>TypeWise Message Traffic Monitor</b></font>");
		table.put(row, 3, "&nbsp;");
        table.put(++row, 1, "Application");
        table.put(row, 2, applicationClassName);
        table.put(++row, 1, "Creation time");
        table.put(row, 2, timeStamp.toString());
        table.put(++row, 1, "Snapshot time");
        table.put(row, 2, (new Date()).toString());
        table.put(++row, 1, "Memory at startUp");
        table.put(row, 2, (String.valueOf(memoryAtStartUp)));
        table.put(++row, 1, "Memory now");
        table.put(row, 2, (String.valueOf(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
        table.put(++row, 1, "&nbsp;");
        table.put(++row, 1, "total requests");
        table.put(row, 2, postCount);
        table.put(++row, 1, "total sessions");
        table.put(row, 2, sessionCount);
        table.put(++row, 1, "active sessions");
        table.put(row, 2, activeSessionCount);
        if (postCount > 0) {
            table.put(++row, 1, "last request");
            if (lastRequestTimestamp != null)
                table.put(row, 2, lastRequestTimestamp.toString());
            else
                table.put(row, 2, "-");
            table.put(row, 3, "");
            table.put(++row, 1, "last request length");
            table.put(row, 2, lastRequestSize);
            table.put(row, 3, "bytes");
            table.put(++row, 1, "last response length");
            table.put(row, 2, lastResponseSize);
            table.put(row, 3, "bytes");
            table.put(++row, 1, "last response time");
            table.put(row, 2, (String.valueOf(lastResponseTime)));
            table.put(row, 3, "ms");
            table.put(++row, 1, "&nbsp;");
            table.put(++row, 1, "average request length");
            table.put(row, 2, (totalRequestLength / postCount));
            table.put(row, 3, "bytes");
            table.put(++row, 1, "average response length");
            table.put(row, 2, (totalResponseLength / postCount));
            table.put(row, 3, "bytes");
            table.put(++row, 1, "average response time");
            table.put(row, 2, String.valueOf(totalResponseTime / postCount));
            table.put(row, 3, "ms");
            table.put(++row, 1, "maximum request length");
            table.put(row, 2, maximumRequestLength);
            table.put(row, 3, "bytes");
            table.put(++row, 1, "maximum response length");
            table.put(row, 2, maximumResponseLength);
            table.put(row, 3, "bytes");
            table.put(++row, 1, "maximum response time");
            table.put(row, 2, String.valueOf(maximumResponseTime));
            table.put(row, 3, "ms");
        }
        html.table(table);
    }
}