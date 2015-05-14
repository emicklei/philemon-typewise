package com.philemonworks.typewise.cwt.custom;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import com.philemonworks.typewise.cwt.TextField;
/**
 * @author E.M.Micklei
 *
 */
public class DateField extends TextField {
	public DateFormat formatter = DateFormat.getDateInstance(DateFormat.SHORT);
	/**
	 * Constructor for DateField.
	 * @param newName
	 * @param topRow
	 * @param leftColumn
	 * @param howManyRows
	 * @param howManyColumns
	 */
	public DateField(String newName, int topRow, int leftColumn, int howManyRows, int howManyColumns) {
		super(newName, topRow, leftColumn, howManyRows, howManyColumns);
	}
	public void setDate(Date aDate) {
		if (aDate== null) 
			super.setString("");
		else
			super.setString(formatter.format(aDate));
	}
	/**
	 * Return the Date value entered or null if the format was invalid
	 * @return java.util.Date
	 */
	public Date getDate() {
		Date newDate = null;
		if (super.getString().length() != 0) {
			try { 
				newDate = formatter.parse(super.getString());
			} catch (ParseException ex) {
			    // Invalid date so return null
			    return newDate;
			}
		}
		return newDate;
	}
	public boolean hasValidDate(){
		return this.getDate() != null;
	}
	/**
	 * Returns the formatter.
	 * @return DateFormat
	 */
	public DateFormat getFormatter() {
		return formatter;
	}

	/**
	 * Sets the formatter.
	 * @param formatter The formatter to set
	 */
	public void setFormatter(DateFormat formatter) {
		this.formatter = formatter;
	}

}
