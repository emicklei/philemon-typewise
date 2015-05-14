/*
 * Licensed Material - Property of PhilemonWorks B.V.
 * 
 * (c) Copyright PhilemonWorks 2004 - All rights reserved.
 * Use, duplication, distribution or disclosure restricted. 
 * See http://www.philemonworks.com for information.
 * 
 * VERSION HISTORY
 * 2-aug-2004: created
 *
 */
package com.philemonworks.typewise;

import java.awt.Point;
import java.io.Serializable;
import com.philemonworks.typewise.internal.ErrorUtils;

/**
 * @author emicklei
 *  
 */
public final class Bounds implements Serializable{
    private int leftColumn, topRow, rows, columns;

    public Bounds(int topRow, int leftColumn, int rows, int columns) {
        super();
        this.setLeftColumn(leftColumn);
        this.setTopRow(topRow);
        this.setRows(rows);
        this.setColumns(columns);
    }

    /**
     * @return Returns the columns.
     */
    public int getColumns() {
        return columns;
    }
    /**
     * @param columns
     *                The columns to set.
     */
    public void setColumns(int nColumns) {
        if (nColumns <= 0)
            ErrorUtils.error(this, "columns must have positive value:" + nColumns);
        this.columns = nColumns;
    }
    /**
     * @return Returns the leftColumn.
     */
    public int getLeftColumn() {
        return leftColumn;
    }
    /**
     * @param leftColumn
     *                The leftColumn to set.
     */
    public void setLeftColumn(int nLeftColumn) {
        if (nLeftColumn <= 0)
            ErrorUtils.error(this, "left column must have positive value:" + nLeftColumn);
        this.leftColumn = nLeftColumn;
    }
    /**
     * @return Returns the rows.
     */
    public int getRows() {
        return rows;
    }
    /**
     * @param rows
     *                The rows to set.
     */
    public void setRows(int nRows) {
        if (nRows <= 0)
            ErrorUtils.error(this, "rows must have positive value:" + nRows);
        this.rows = nRows;
    }
    /**
     * @return Returns the topRow.
     */
    public int getTopRow() {
        return topRow;
    }
    /**
     * @param topRow
     *                The topRow to set.
     */
    public void setTopRow(int nTopRow) {
        if (nTopRow <= 0)
            ErrorUtils.error(this, "top row must have positive value:" + nTopRow);
        this.topRow = nTopRow;
    }
    public int getRightColumn() {
        return leftColumn + columns - 1;
    }
    public void setRightColumn(int nRightColumn) {
        if (nRightColumn <= 0)
            ErrorUtils.error(this, "right column must have positive value:" + nRightColumn);
        this.setColumns(nRightColumn + 1 - leftColumn);
    }
    public int getBottomRow() {
        return topRow + rows - 1;
    }
    public void setBottomRow(int posInt) {
        this.setRows(posInt + 1 - topRow);
    }
    public Point getLocation() {
        // leftColumn , topRow
        return new Point(leftColumn, topRow);
    }
    public void setLocation(Point newPoint) {
        this.setLeftColumn(newPoint.x);
        this.setTopRow(newPoint.y);
    }
    /*
     * Answer the intersection between the receiver and the argument otherBounds
     */
    public Bounds intersection(Bounds otherBounds) {

        return null;
    }
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof Bounds))
            return false;
        Bounds otherBounds = (Bounds) otherObject;
        return leftColumn == otherBounds.leftColumn && topRow == otherBounds.topRow && rows == otherBounds.rows
                && columns == otherBounds.columns;
    }
    public int hashCode(){
        return leftColumn | topRow;
    }
    /**
     * Return whether the row,column pair is inside the receiver's bounds
     * @param row int
     * @param column int
     * @return true if inside
     */
    public boolean includesRowColumn(int row, int column) {
        if (column < leftColumn)
            return false;
        if (row < topRow)
            return false;
        if (column > this.getRightColumn())
            return false;
        if (row > this.getBottomRow())
            return false;
        return true;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("top=");
        buffer.append(this.getTopRow());
        buffer.append(",left=");
        buffer.append(this.getLeftColumn());
        buffer.append(",rows=");
        buffer.append(this.getRows());
        buffer.append(",columns=");
        buffer.append(this.getColumns());
        buffer.append(",bottom=");
        buffer.append(this.getBottomRow());
        buffer.append(",right=");
        buffer.append(this.getRightColumn());        
        return buffer.toString();
    }
    /**
     *  Answer the center location
     * @return center : Location
     */
    public Location getCenter(){
    	return new Location(rows/2,columns/2);
    }
}