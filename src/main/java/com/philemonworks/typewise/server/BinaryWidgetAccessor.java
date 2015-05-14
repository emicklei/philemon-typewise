/*
 * Licensed Material - Property of PhilemonWorks B.V.
 *
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted.
 * See http://www.philemonworks.com for information.
 *
 * */
package com.philemonworks.typewise.server;

import java.io.IOException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.philemonworks.boa.BinaryObjectAccessor;
import com.philemonworks.boa.BinaryObjectReadException;
import com.philemonworks.typewise.Bounds;
import com.philemonworks.typewise.Dialog;
import com.philemonworks.typewise.ListItem;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.WidgetAppearance;
import com.philemonworks.typewise.cwt.Button;
import com.philemonworks.typewise.cwt.CheckBox;
import com.philemonworks.typewise.cwt.Choice;
import com.philemonworks.typewise.cwt.GroupBox;
import com.philemonworks.typewise.cwt.Image;
import com.philemonworks.typewise.cwt.Label;
import com.philemonworks.typewise.cwt.List;
import com.philemonworks.typewise.cwt.Menu;
import com.philemonworks.typewise.cwt.MenuBar;
import com.philemonworks.typewise.cwt.MenuItem;
import com.philemonworks.typewise.cwt.MenuList;
import com.philemonworks.typewise.cwt.MenuSeparator;
import com.philemonworks.typewise.cwt.Panel;
import com.philemonworks.typewise.cwt.RadioButton;
import com.philemonworks.typewise.cwt.Screen;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.typewise.cwt.TextArea;
import com.philemonworks.typewise.cwt.TextField;
import com.philemonworks.typewise.cwt.custom.Console;
import com.philemonworks.typewise.cwt.custom.Timer;
import com.philemonworks.typewise.internal.BasicWidget;
import com.philemonworks.typewise.internal.CompositeWidget;
import com.philemonworks.typewise.internal.ObjectAppearance;
import com.philemonworks.typewise.internal.Widget;
import com.philemonworks.typewise.message.IndexedMessageSend;
import com.philemonworks.typewise.message.Message;
import com.philemonworks.typewise.message.MessageSend;
import com.philemonworks.typewise.message.MessageSequence;
import com.philemonworks.util.Color;

/**
 * BinaryWidgetAccessor is a specialization of a BOA and knows how to access (read+write) Terminal Objects from a byte
 * stream.
 * 
 * @author emicklei
 */
public class BinaryWidgetAccessor extends BinaryObjectAccessor {
	private WidgetFactory factory = new ServerWidgetFactory();
	private static final int BWA_VERSION = 15; // 15-02-2007
	private static final byte TTBounds = 26;
	private static final byte TTButton = 2;
	private static final byte TTCheckBox = 24;
	private static final byte TTCompositeWidget = 7;
	private static final byte TTConsole = 31;
	private static final byte TTDisplayItem = 22;
	private static final byte TTDisplayTableItem = 23;
	private static final byte TTChoice = 15;
	private static final byte TTerminalObject = 100;
	private static final byte TTGroupBox = 20;
	private static final byte TTImage = 8;
	private static final byte TTIndexedMessageSend = 4;
	private static final byte TTInput = 1;
	private static final byte TTLabel = 21;
	private static final byte TTList = 3;
	private static final byte TTMenu = 5;
	private static final byte TTMenuBar = 6;
	private static final byte TTMessageSend = 9;
	private static final byte TTMessageSequence = 10;
	private static final byte TTRadioButton = 25;
	private static final byte TTTableColumn = 18;
	private static final byte TTTableList = 17;
	private static final byte TTText = 16;
	private static final byte TTTimer = 100;
	private static final byte TTWidgetAppearance = 12;
	private static final byte TTWindow = 19;
	private static final byte TTMenuItem = 27;
	private static final byte TTMenuSeparator = 28;
	private static final byte TTMenuList = 29;
	private static final byte TTDialog = 30;
	private boolean reading;

	/**
	 * @throws BinaryObjectReadException
	 */
	public BinaryWidgetAccessor() throws BinaryObjectReadException {
		super();
		reading = false;
	}
	/**
	 * @param byteArray byte[]
	 * @throws BinaryObjectReadException
	 */
	public BinaryWidgetAccessor(byte[] byteArray) throws BinaryObjectReadException {
		super(byteArray);
		reading = true;
	}
	/**
	 * @param aFactory WidgetFactory
	 */
	public void setFactory(WidgetFactory aFactory) {
		factory = aFactory;
	}
	/**
	 * Unmarshall the binary contents of buffer using the BinaryWidgetAccessor that read the BWA protocol.
	 * Use the widget factory to create CWT instances.
	 * 
	 * @param factory WidgetFactory
	 * @param buffer : byte[]
	 * @return Object
	 */
	public static Object decode(byte[] buffer, WidgetFactory factory) {
		Object requestMessage = null;
		try {
			BinaryWidgetAccessor reader = new BinaryWidgetAccessor(buffer);
			reader.setFactory(factory);
			requestMessage = reader.readNext();
		} catch (Exception ex) {
			Logger.getLogger(BinaryWidgetAccessor.class).error("Data to decode:" + new String(buffer));
			throw new RuntimeException("Unable to decode data:"+ex.getMessage(),ex);
		}
		return requestMessage;
	}	
	/**
	 * Unmarshall the binary contents of buffer using the BinaryWidgetAccessor that read the BWA protocol.
	 * Use the default widget factory for server-side CWT instances.
	 * 
	 * @param buffer : byte[]
	 * @return Object
	 */
	public static Object decode(byte[] buffer) {
		return decode(buffer,new ServerWidgetFactory());
	}		
	/**
	 * Marshall the object using the BWA format into a binary object (byte[]).
	 * 
	 * @param result : Object
	 * @return byte[]
	 */
	public static byte[] encode(Object result) {
		byte[] responseBody = null;
		// Write result as binary data
		try {
			BinaryWidgetAccessor writer = new BinaryWidgetAccessor();
			writer.write(result);
			responseBody = writer.toByteArray();
		} catch (Exception ex) {
			throw new RuntimeException("Unable to encode data:"+ex.getMessage(),ex);
		}
		return responseBody;
	}	
	protected void access(BasicWidget widget) throws BinaryObjectReadException, IOException {
		this.access((Widget) widget);
	}
	protected void access(Bounds bounds) throws BinaryObjectReadException, IOException {
		if (reading) {
			bounds.setTopRow(this.readNextIntegerValue());
			bounds.setLeftColumn(this.readNextIntegerValue());
			bounds.setRows(this.readNextIntegerValue());
			bounds.setColumns(this.readNextIntegerValue());
		} else {
			this.write(bounds.getTopRow());
			this.write(bounds.getLeftColumn());
			this.write(bounds.getRows());
			this.write(bounds.getColumns());
		}
	}
	protected void access(Button widget) throws BinaryObjectReadException, IOException {
		access((Label) widget);
		if (reading) {
			widget.isDefault(this.readNextBooleanValue());
			widget.setAccelerator(this.readNextCharValue());
			widget.setHelp((this.readNextString()));
		} else {
			this.write(widget.isDefault());
			this.write(widget.getAccelerator());
			if (widget.getHelp() != null)
				this.write(widget.getHelp());
			else
				this.writeNull();
		}
	}
	protected void access(CheckBox widget) throws BinaryObjectReadException, IOException {
		this.access((Label) widget);
		if (reading) {
			widget.setTickAlignment(this.readNextByteValue());
		} else {
			this.write(widget.getTickAlignment());
		}
	}
	protected void access(CompositeWidget widget) throws BinaryObjectReadException, IOException {
		this.access((Widget) widget);
		if (reading) {
			java.util.List widgets = (java.util.List) this.readNext();
			for (int count = 0; count < widgets.size(); count++) {
				widget.add((Widget) widgets.get(count));
			}
		} else {
			this.write(widget.getWidgets());
		}
	}
	protected void access(Console widget) throws BinaryObjectReadException, IOException {
		this.access((Widget) widget);
		if (reading) {			
		} else {			
		}
	}	
	protected void access(ListItem widget) throws BinaryObjectReadException, IOException {
		if (reading) {
			Object id = this.readNext();
			if (id != null)
				widget.setId((String) id);
			widget.setItem(this.readNextString());
		} else {
			if (widget.getId() == null)
				this.writeNull();
			else
				this.write(widget.getId());
			this.write(widget.getItem());
		}
	}
	protected void access(TableListItem widget) throws BinaryObjectReadException, IOException {
		if (reading) {
			widget.setId(this.readNextString());
			widget.setItems(this.readNextStringArray());
		} else {
			this.write(widget.getId());
			this.write(widget.getItems());
		}
	}
	protected void access(Choice widget) throws BinaryObjectReadException, IOException {
		this.access((List) widget);
		if (reading) {
			widget.setMaximumVisibleRows(this.readNextIntegerValue());
		} else {
			this.write(widget.getMaximumVisibleRows());
		}
	}
	protected void access(GroupBox widget) throws BinaryObjectReadException, IOException {
		this.access((CompositeWidget) widget);
		if (reading) {
			widget.setLabel(this.readNextString());
		} else {
			this.write(widget.getLabel());
		}
	}
	protected void access(Image img) throws BinaryObjectReadException, IOException {
		access((BasicWidget) img);
	}
	protected void access(IndexedMessageSend msg) throws BinaryObjectReadException, IOException {
		if (reading) {
			msg.setIndex(this.readNextIntegerValue());
			msg.setReceiver(this.readNextString());
			Object argumentsOrNull = this.readNext();
			if (argumentsOrNull != null)
				msg.setArguments((Object[]) argumentsOrNull);
		} else {
			this.write(msg.getIndex());
			this.write(msg.getReceiver());
			this.write(msg.getArgumentsOrNull());
		}
	}
	protected void access(Label widget) throws BinaryObjectReadException, IOException {
		this.access((BasicWidget) widget);
	}
	protected void access(List widget) throws BinaryObjectReadException, IOException {
		this.access((BasicWidget) widget);
		if (reading) {
			widget.setMultiSelect(this.readNextBooleanValue());
		} else {
			this.write(widget.isMultiSelect());		
		}		
	}
	protected void access(Menu widget) throws BinaryObjectReadException, IOException {
		if (reading) {
			widget.setName(this.readNextString());
			widget.setItems((ArrayList) this.readNext());
		} else {
			this.write(widget.getName());
			this.write(widget.getItems());
		}
	}
	protected void access(MenuList widget) throws BinaryObjectReadException, IOException {
		access((List) widget);
	}
	protected void access(MenuBar widget) throws BinaryObjectReadException, IOException {
		access((List) widget);
		if (reading) {
			widget.setCharacters((ArrayList) this.readNext());
			widget.setLabels((ArrayList) this.readNext());
			widget.setMenus((ArrayList) this.readNext());
			widget.setUseNative(this.readNextBooleanValue());
		} else {
			this.write(widget.getCharacters());
			this.write(widget.getLabels());
			this.write(widget.getMenus());
			this.write(widget.isUseNative());
		}
	}
	protected void access(MenuItem item) throws BinaryObjectReadException, IOException {
		// Some field may be null, so check here (BOA requires values to be non-null)
		if (reading) {
			item.setLabel(this.readNextString());
			item.setEvent(this.readNextIntegerValue());
			item.setEnabled(this.readNextBooleanValue());
			item.setMessage((Message) this.readNext());
			item.setSubMenu((Menu) this.readNext());
			item.setIcon((String) this.readNext());
			item.setAccellerator(this.readNextCharValue());
			item.setHelp(this.readNextString());
		} else {
			this.write(item.getLabel());
			this.write(item.getEvent());
			this.write(item.isEnabled());
			this.write(item.getMessage());
			if (item.hasSubMenu())
				this.write(item.getSubMenu());
			else
				this.writeNull();
			if (item.getIcon() != null)
				this.write(item.getIcon());
			else
				this.writeNull();
			this.write(item.getAccellerator());
			if (item.getHelp() != null)
				this.write(item.getHelp());
			else
				this.writeNull();
		}
	}
	protected void access(MessageSend msg) throws BinaryObjectReadException, IOException {
		if (reading) {
			msg.setOperation(this.readNextString());
			msg.setReceiver(this.readNextString());
			Object argumentsOrNull = this.readNext();
			if (argumentsOrNull != null)
				msg.setArguments((Object[]) argumentsOrNull);
		} else {
			this.write(msg.getOperation());
			this.write(msg.getReceiver());
			this.write(msg.getArgumentsOrNull());
		}
	}
	protected void access(MessageSequence seq) throws BinaryObjectReadException, IOException {
		if (reading) {
			seq.setMessages((ArrayList) this.readNext());
		} else {
			this.write(seq.getMessages());
		}
	}
	protected void access(ObjectAppearance app) throws BinaryObjectReadException, IOException {
		if (reading) {
			app.setForeground((Color) this.readNext());
			app.setBackground((Color) this.readNext());
			app.setSelectionForeground((Color) this.readNext());
			app.setSelectionBackground((Color) this.readNext());
			app.setDisabledForeground((Color) this.readNext());
		} else {
			this.write(app.getForeground());
			this.write(app.getBackground());
			this.write(app.getSelectionForeground());
			this.write(app.getSelectionBackground());
			this.write(app.getDisabledForeground());
		}
	}
	protected void access(Panel widget) throws BinaryObjectReadException, IOException {
		this.access((CompositeWidget) widget);
	}
	protected void access(RadioButton widget) throws BinaryObjectReadException, IOException {
		this.access((CheckBox) widget);
		if (reading) {
			widget.setGroupName(this.readNextString());
		} else {
			this.write(widget.getGroupName());
		}
	}
	protected void access(Screen widget) throws BinaryObjectReadException, IOException {
		this.access((CompositeWidget) widget);
		if (reading) {
			widget.setMenuBarOrNull((MenuBar) this.readNext());
		} else {
			if (widget.getMenuBarOrNull() == null) {
				this.writeNull();
			} else {
				this.write(widget.getMenuBar());
			}
		}
	}
	protected void access(TableColumn column) throws BinaryObjectReadException, IOException {
		if (reading) {
			column.setWidth(this.readNextIntegerValue());
			column.setMaxLength(this.readNextIntegerValue());
			column.setHeading((Label) this.readNext());
			column.setAlignment(this.readNextByteValue());
			column.setAppearance((WidgetAppearance) this.readNext());
		} else {
			this.write(column.getWidth());
			this.write(column.getMaxLength());
			this.write(column.getHeading());
			this.write(column.getAlignment());
			this.write(column.getAppearanceOrNull());
		}
	}
	protected void access(TableList widget) throws BinaryObjectReadException, IOException {
		this.access((List) widget);
		if (reading) {
			widget.setTableColumns((ArrayList) this.readNext());
		} else {
			this.write(widget.getTableColumns());
		}
	}
	protected void access(TextArea widget) throws BinaryObjectReadException, IOException {
		this.access((TextField) widget);
	}
	protected void access(TextField widget) throws BinaryObjectReadException, IOException {
		this.access((Label) widget);
		if (reading) {
			widget.setMaxLength(this.readNextIntegerValue());
			widget.setType(this.readNextString());
		} else {
			this.write(widget.getMaxLength());
			this.write(widget.getType());
		}
	}
	protected void access(Timer widget) throws BinaryObjectReadException, IOException {
		this.access((TextField) widget);
	}
	protected void access(Dialog dialog) throws BinaryObjectReadException, IOException {
		if (reading) {
			dialog.setText(this.readNextString());
			dialog.setType(this.readNextIntegerValue());
			dialog.setYesMessage((Message) this.readNext());
			dialog.setNoMessage((Message) this.readNext());
			dialog.setCancelMessage((Message) this.readNext());
		} else {
			this.write(dialog.getText());
			this.write(dialog.getType());
			this.write(dialog.getYesMessage());
			this.write(dialog.getNoMessage());
			this.write(dialog.getCancelMessage());
		}
	}
	/*
	 * Purpose: access does the real object reading and writing.
	 */
	protected void access(Widget widget) throws BinaryObjectReadException, IOException {
		// Although Bounds are considered dynamic state, it will be sended first (not as state message) because
		// the client requires this for computing the screen extent
		// The appearance will be send too for display performance reasons
		if (reading) {
			widget.setName(this.readNextString());
			widget.setBounds((Bounds) (this.readNext()));
			widget.setAppearance((WidgetAppearance) this.readNext());
		} else {
			this.write(widget.getName());
			this.write(widget.getBounds());
			this.write(widget.getAppearanceOrNull());
		}
	}
	protected void access(WidgetAppearance app) throws BinaryObjectReadException, IOException {
		access((ObjectAppearance) app);
		if (reading) {
			app.setBorderColor((Color) this.readNext());
			app.setFocusColor((Color) this.readNext());
		} else {
			this.write(app.getBorderColor());
			this.write(app.getFocusColor());
		}
	}
	protected Object dispatchUnknown(byte tag) throws BinaryObjectReadException, IOException {
		// Override from super to detect TerminalObject tag
		// If such a match is possbile then redispatch the widget tag
		if (tag == TTerminalObject) {
			return this.dispatchWidgetTag(this.readTag());
		} else {
			return super.dispatchUnknown(tag);
		}
	}
protected Object dispatchWidgetTag(byte tag) throws BinaryObjectReadException, IOException {
        Object result = null;
        switch (tag) {
        case TTBounds:
            result = new Bounds(1, 1, 1, 1);
            this.access((Bounds) result);
            break;
        case TTInput:
            result = factory.createTextField();
            this.access((TextField) result);
            break;
        case TTWindow:
            result = factory.createScreen();
            this.access((Screen) result);
            break;
        case TTLabel:
            result = factory.createLabel();
            this.access((Label) result);
            break;
        case TTCheckBox:
            result = factory.createCheckBox();
            this.access((CheckBox) result);
            break;
        case TTRadioButton:
            result = factory.createRadioButton();
            this.access((RadioButton) result);
            break;
        case TTText:
            result = factory.createTextArea();
            this.access((TextArea) result);
            break;
        case TTList:
            result = factory.createList();
            this.access((List) result);
            break;
        case TTButton:
            result = factory.createButton();
            this.access((Button) result);
            break;
        case TTMessageSend:
            result = new MessageSend();
            this.access((MessageSend) result);
            break;
        case TTIndexedMessageSend:
            result = new IndexedMessageSend();
            this.access((IndexedMessageSend) result);
            break;
        case TTMessageSequence:
            result = new MessageSequence();
            this.access((MessageSequence) result);
            break;
        case TTWidgetAppearance:
            result = new WidgetAppearance();
            this.access((WidgetAppearance) result);
            break;
        case TTTableList:
            result = factory.createTableList();
            this.access((TableList) result);
            break;
        case TTTableColumn:
            result = new TableColumn();
            this.access((TableColumn) result);
            break;
        case TTMenu:
            result = factory.createMenu();
            this.access((Menu) result);
            break;
        case TTMenuBar:
            result = factory.createMenuBar();
            this.access((MenuBar) result);
            break;
        case TTDisplayItem:
            result = new ListItem("","");
            this.access((ListItem) result);
            break;
        case TTDisplayTableItem:
            result = new TableListItem("");
            this.access((TableListItem) result);
            break;
        case TTImage:
            result = factory.createImage();
            this.access((Image) result);
            break;
        case TTGroupBox:
            result = factory.createGroupBox();
            this.access((GroupBox) result);
            break;
        case TTChoice:
            result = factory.createChoice(); 
            this.access((Choice) result);
            break;
        case TTCompositeWidget:
            result = factory.createPanel();
            this.access((Panel) result);
            break;
        case TTTimer:
            result = factory.createTimer();
            this.access((Timer) result);
            break;
        case TTMenuItem:
            result = new MenuItem("",null);
            this.access((MenuItem) result);
            break;            
        case TTMenuSeparator:
            result = new MenuSeparator();
            break;          
        case TTMenuList:
            result = factory.createMenuList();
            this.access((MenuList)result);
            break;
        case TTDialog:
            result = factory.createDialog();
            this.access((Dialog)result);
            break;      
        case TTConsole:
            result = factory.createConsole();
            this.access((Console)result);
            break;             
        default:
            result = super.dispatchUnknown(tag);
        }
        return result;
    }	protected int getVersion() {
		return BWA_VERSION;
	}
	/**
	 * @param widget
	 * @throws IOException
	 */
	public void write(BinaryAccessible widget) throws IOException {
		try {
			// Use double dispatch to call correct write+access method
			widget.writeBinaryObjectUsing(this);
		} catch (BinaryObjectReadException e) {
			throw new IOException(e.toString());
		}
	}
	/**
	 * @param bounds
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Bounds bounds) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTBounds);
		this.access(bounds);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Button widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTButton);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(CheckBox widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTCheckBox);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(CompositeWidget widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTCompositeWidget);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Console widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTConsole);
		this.access(widget);
	}	
	/**
	 * @param item
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(ListItem item) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTDisplayItem);
		this.access(item);
	}
	/**
	 * @param item
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(TableListItem item) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTDisplayTableItem);
		this.access(item);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Choice widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTChoice);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(GroupBox widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTGroupBox);
		this.access(widget);
	}
	/**
	 * @param item
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Image item) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTImage);
		this.access(item);
	}
	/**
	 * @param message
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(IndexedMessageSend message) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTIndexedMessageSend);
		this.access(message);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Label widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTLabel);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(List widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTList);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Menu widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMenu);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MenuList widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMenuList);
		this.access(widget);
	}
	/**
	 * @param bar
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MenuBar bar) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMenuBar);
		this.access(bar);
	}
	/**
	 * @param item
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MenuItem item) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMenuItem);
		this.access(item);
	}
	/**
	 * @param line
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MenuSeparator line) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMenuSeparator);
	}
	/**
	 * @param message
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MessageSend message) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMessageSend);
		this.access(message);
	}
	/**
	 * @param messageSeq
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(MessageSequence messageSeq) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTMessageSequence);
		this.access(messageSeq);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Panel widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTCompositeWidget);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(RadioButton widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTRadioButton);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Screen widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTWindow);
		this.access(widget);
	}
	/**
	 * @param column
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(TableColumn column) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTTableColumn);
		this.access(column);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(TableList widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTTableList);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(TextArea widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTText);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(TextField widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTInput);
		this.access(widget);
	}
	/**
	 * @param widget
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Timer widget) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTTimer);
		this.access(widget);
	}
	/**
	 * @param widgetApp
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(WidgetAppearance widgetApp) throws BinaryObjectReadException, IOException {
		// the appearance value is optional so check for null.
		if (widgetApp == null) {
			this.writeNull();
			return;
		}
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTWidgetAppearance);
		this.access(widgetApp);
	}
	/**
	 * @param aDialog
	 * @throws BinaryObjectReadException
	 * @throws IOException
	 */
	public void write(Dialog aDialog) throws BinaryObjectReadException, IOException {
		this.writeTypeIdentifier(TTerminalObject);
		this.writeTypeIdentifier(TTDialog);
		this.access(aDialog);
	}
	protected void writeUnknown(Object value) throws IOException {
		// Very important: make sure that when you've got an array of Widgets,
		// these widgets will be serialized correctly.
		if (value instanceof BinaryAccessible) {
			this.write((BinaryAccessible) value);
			return;
		}
		if (value instanceof Bounds) {
			try {
				this.write((Bounds) value);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return;
		}
		super.writeUnknown(value);
	}
}