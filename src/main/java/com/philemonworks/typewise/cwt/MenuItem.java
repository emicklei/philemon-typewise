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
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.server.BinaryAccessible;
import com.philemonworks.typewise.server.BinaryWidgetAccessor;

/**
 * @author emicklei
 *
 * @version 28-aug-2004
 */
public class MenuItem implements BinaryAccessible {

    private String label = "";
    private int event = -1;
    private boolean enabled = true;
    private Message message = null;
    private Menu subMenu = null;
    private String icon = null;
    private char accellerator = NOACCELLERATOR;
    private String help = null; 
    public static final char NOACCELLERATOR = ' ';
    
    public MenuItem(String newLabel, Message newMessage){
        super();
        this.label = newLabel;
        this.message = newMessage;
    }
    /* (non-Javadoc)
     * @see com.philemonworks.typewise.server.BinaryAccessible#writeBinaryObjectUsing(com.philemonworks.typewise.server.BinaryWidgetAccessor)
     */
    public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa) throws BinaryObjectReadException, IOException {
        bwa.write((MenuItem) this);
    }
    /**
     * @return Returns the accellerator.
     */
    public char getAccellerator() {
        return accellerator;
    }
    /**
     * @param accellerator The accellerator to set.
     */
    public void setAccellerator(char accellerator) {
        this.accellerator = accellerator;
    }
    /**
     * @return Returns the enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @param enabled The enabled to set.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    /**
     * @return Returns the event.
     */
    public int getEvent() {
        return event;
    }
    /**
     * @param event The event to set.
     */
    public void setEvent(int event) {
        this.event = event;
    }
    /**
     * @return Returns the help.
     */
    public String getHelp() {
        return help;
    }
    /**
     * @param help The help to set.
     */
    public void setHelp(String help) {
        this.help = help;
    }
    /**
     * @return Returns the icon.
     */
    public String getIcon() {
        return icon;
    }
    /**
     * @param icon The icon to set.
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }
    /**
     * @return Returns the label.
     */
    public String getLabel() {
        return label;
    }
    /**
     * @param label The label to set.
     */
    public void setLabel(String label) {
        this.label = label;
    }
    /**
     * @return Returns the message.
     */
    public Message getMessage() {
        return message;
    }
    /**
     * @param message The message to set.
     */
    public void setMessage(Message message) {
        this.message = message;
    }
    /**
     * @return Returns the subMenu.
     */
    public Menu getSubMenu() {
        return subMenu;
    }
    /**
     * @param subMenu The subMenu to set.
     */
    public void setSubMenu(Menu subMenu) {
        this.subMenu = subMenu;
    }
    public boolean hasSubMenu(){
        return subMenu != null;
    }
}
