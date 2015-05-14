/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * Created on Feb 10, 2004 by mvhulsentop
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
 */
public class MenuBar extends List implements BinaryAccessible {

    private boolean useNative = true;
	private ArrayList menus = new ArrayList();
	private ArrayList characters = new ArrayList();

	/**
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param rows
	 * @param columns
	 */
	public MenuBar(
		String newName,
		int topRow,
		int leftColumn,
		int howManyRows,
		int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}

	public void writeBinaryObjectUsing(BinaryWidgetAccessor bwa)
		throws BinaryObjectReadException, IOException {
		bwa.write((MenuBar) this);

	}
	
	public void addKeyMenu(String label, Character key, Menu menu) {
		this.getLabels().add(label);
		this.getCharacters().add(key);
		this.getMenus().add(menu);
	}
	
	public void addKeyMenu(String label, char key, Menu menu) {
		this.addKeyMenu(label, new Character(key), menu);
	}

	/**
	 * @return
	 */
	public ArrayList getCharacters() {
		return characters;
	}

	/**
	 * NON-API: this method exists for marshalling only
	 * @param characters
	 */
	public void setCharacters(ArrayList characters) {
		this.characters = characters;
	}

	/**
	 * @return
	 */
	public ArrayList getMenus() {
		return menus;
	}

	/**
	 * NON-API: this method exists for marshalling only
	 * @param menus
	 */
	public void setMenus(ArrayList menus) {
		this.menus = menus;
	}
	/**
	 * @return
	 */
	public java.util.List getLabels() {
		return this.getItems();
	}

	/**
	 * NON-API: this method exists for marshalling only
	 * @param menus
	 */
	public void setLabels(ArrayList labels) {
		this.setItems(labels);
	}
	public void kindDo(WidgetHandler wh) {
		wh.handle((MenuBar) this);
	}

    /**
     * @return Returns whether the menuBar will be displayed using a native OS-control
     */
    public boolean isUseNative() {
        return useNative;
    }
    /**
     * @param useNative Sets whether the menuBar will be displayed using a native OS-control
     */
    public void setUseNative(boolean useNative) {
        this.useNative = useNative;
    }
}
