/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 28-aug-2004: created
 *
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.io.Serializable;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 *
 * @version 28-aug-2004
 */
public class MenuSeparator implements BinaryAccessible, Serializable {
    /* (non-Javadoc)
     * @see com.philemonworks.typewise.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.typewise.server.BinaryWidgetAccessor)
     */
    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((MenuSeparator)this);
    }
}
