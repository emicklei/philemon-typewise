/**
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2005 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 */
package com.philemonworks.typewise;

/**
 * Location represents a position in a Screen expressed by a row,column pair.
 *
 * @author E.M.Micklei
 */
public class Location {
	/**
	 * Row component of the Screen location.
	 */
	public int row;
	/**
	 * Column component of the Screen location.
	 */
	public int column;
	/**
	 * Constructor
	 * @param newRow int
	 * @param newColumn int
	 */
	public Location(int newRow,int newColumn){
		super();
		row = newRow;
		column = newColumn;
	}
	/**
	 * @return Returns the column.
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * @param column The column to set.
	 */
	public void setColumn(int column) {
		this.column = column;
	}
	/**
	 * @return Returns the row.
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row The row to set.
	 */
	public void setRow(int row) {
		this.row = row;
	}
	
	public Location add(Location loc){
		row += loc.row;
		column += loc.column;
		return this;
	}
	public Location add(int locrow, int loccolumn){
		row += locrow;
		column += loccolumn;
		return this;
	}	
}
