/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 8-mei-04: created
 *
 */
package com.philemonworks.typewise.test;

import com.philemonworks.typewise.ApplicationModel;
import com.philemonworks.typewise.server.ApplicationServlet;

/**
 * @author emicklei
 *
 */
public class TestApplicationServlet extends ApplicationServlet {
	ApplicationModel model = null;
	public ApplicationModel getApplicationModel(){return model;}
	public void setApplicationModel(ApplicationModel newModel){model = newModel;}
}
