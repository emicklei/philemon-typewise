package com.philemonworks.typewise.html;

import java.util.HashMap;
import java.util.Map;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.writer.HTMLWriter;
/** 
 * SimpleTableListRenderer is class that emits HTML code for a TableList
 * using a simple table with radiobuttons to indicate a selection.
 *
 * TODO: highlight row, apply appearance colors to heading and columns
 * 
 * @author E.M.Micklei
 */
public class SimpleTableListRenderer {
	private CSSBasedRenderer cssRenderer;
	private HTMLWriter writer;
	private int dx;
	private int dy;

	/**
	 * @param cssRenderer
	 */
	public SimpleTableListRenderer(CSSBasedRenderer cssRenderer) {
		super();
		this.cssRenderer = cssRenderer;
		writer = cssRenderer.writer;
		dx = cssRenderer.dx;
		dy = cssRenderer.dy;
	}
	/**
	 * Render a TableList.
	 * @param aTableList
	 */
	public void handle(TableList aTableList) {
		// writer.pretty = true;
		Style style = cssRenderer.newStyleFor(aTableList);
		style.width(dx * aTableList.getColumns());
		style.height(dy * aTableList.getRows());
		style.append("overflow:auto;");
		Map map = style.asMap();
		writer.div(map);
		if (aTableList.isEnabled())
			this.addSelectionField(aTableList);
		writer.table(writer.newMap("class","tablelist"));
		writer.tr();
		if (aTableList.isEnabled())
			writer.th(" ");
		for (int c = 0; c < aTableList.getTableColumns().size(); c++) {
			TableColumn column = (TableColumn) aTableList.getTableColumns().get(c);
			writer.th(writer.newMap("align", "left"), HTMLWriter.encoded(column.getHeading().getText()));
		}
		writer.end("tr");
		java.util.List itemsToDisplay = aTableList.getItems();
		for (int i = 0; i < itemsToDisplay.size(); i++) {
			TableListItem item = (TableListItem) itemsToDisplay.get(i);
			String rowclass = i == aTableList.getSelectionIndex() ? "rowselected" : "rowunselected";
			Map rowmap = writer.newMap("class", rowclass);
			// set the id for selections
			rowmap.put("id", aTableList.getName() + "-row-" + i);
			writer.tr(rowmap);
			if (aTableList.isEnabled()) {
				writer.td();
				this.addSelectionBox(aTableList, i);
				writer.end("td");
			}
			for (int c = 0; c < aTableList.getTableColumns().size(); c++) {
				TableColumn col = (TableColumn) aTableList.getTableColumns().get(c);
				writer.td(HTMLWriter.encoded(item.get(c)));
			}
			writer.end("tr");
		}
		writer.end("table");
		writer.end("div");
	}
	private void addSelectionBox(TableList aTableList, int row) {
		Map map = new HashMap();
		map.put("type", "radio");
		map.put("name", aTableList.getName() + "-select");
		if (row == aTableList.getSelectionIndex()) {
			map.put("checked value", aTableList.getName() + row);
		} else {
			map.put("value", aTableList.getName() + row);
		}
		map.put("onclick", "javascript:{document.forms[0]." + aTableList.getName() + ".value='" + row + "';}");
		writer.tag("input", map, true);
	}
	private void addSelectionField(TableList aTableList) {
		// write a hidden input field for storing the selected row in the table
		// the value for this field is set by a function declared by the setupwebfx script.
		Map hidden = new HashMap();
		hidden.put("type", "hidden");
		hidden.put("name", aTableList.getName());
		writer.tag("input", hidden, true);
	}
}
