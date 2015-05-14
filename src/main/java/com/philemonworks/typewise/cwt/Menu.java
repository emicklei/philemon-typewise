/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 10, 2004 by emicklei
 * 4-0622: changed MessageSend to Message, addLine
 * 4-0828: redesigned to be holder of MenuItems
 */
package com.philemonworks.typewise.cwt;

import java.io.IOException;
import java.util.ArrayList;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.internal.WidgetHandler;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author mvhulsentop
 *  
 */
public class Menu extends Object implements BinaryAccessible {

    private String name = "";
    private ArrayList items = new ArrayList();

    /*
     * Answer a new Menu whos name is menuName @param menuName an identifier of
     * this menu
     */
    public Menu(String menuName) {
        super();
        this.name = menuName;
    }
    /*
     * (non-Javadoc)
     * 
     * @see com.philemonworks.terminal.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.terminal.server.BinaryWidgetAccessor)
     */
    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((Menu) this);
    }
    /*
     * Add a new MenuItem to the list of items after (below) the last item (if
     * any)
     */
    public void add(MenuItem anItem) {
        items.add(anItem);
    }
    /*
     * Add a separator line just after (below) the last item (if any)
     */
    public void addSeparator() {
        items.add(new MenuSeparator());
    }
    public void kindDo(WidgetHandler wh) {
        wh.handle((Menu) this);
    }
    /**
     * @return Returns the items.
     */
    public ArrayList getItems() {
        return items;
    }
    /**
     * @param items
     *                The items to set.
     */
    public void setItems(ArrayList items) {
        this.items = items;
    }
    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name
     *                The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
}