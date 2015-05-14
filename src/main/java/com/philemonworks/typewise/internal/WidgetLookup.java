/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 12-dec-2004: created
 *
 */
package com.philemonworks.typewise.internal;

import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Timer;

/**
 *  
 */
public interface WidgetLookup {
    public Button getButton(String widgetName);
    public CheckBox getCheckBox(String widgetName);
    public Choice getChoice(String widgetName);    
    public GroupBox getGroupBox(String widgetName); 
    public Image getImage(String widgetName);
    public Label getLabel(String widgetName);    
    public List getList(String widgetName);    
    public Panel getPanel(String widgetName);    
    public RadioButton getRadioButton(String widgetName);
    public TableList getTableList(String widgetName);
    public TextField getTextField(String widgetName);
    public TextArea getTextArea(String widgetName);
    public Timer getTimer(String widgetName);    
}