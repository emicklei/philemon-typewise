package com.philemonworks.typewise.html;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.philemonworks.itable.SortableTable;
import com.philemonworks.typewise.TableListItem;
import com.philemonworks.typewise.cwt.TableColumn;
import com.philemonworks.typewise.cwt.TableList;
import com.philemonworks.writer.HTMLWriter;
import com.philemonworks.writer.XMLWriter;

/**
 * SortableTableListRenderer is helper class that produces a SortableTable
 * for rendering a TableList in HTML
 *
 * @author E.M.Micklei
 */
public class SortableTableListRenderer {
	/**
	 * Keep a package specifiy Map for odd-numbered rows
	 */
	public static Map OddRowMap = null;
	/**
	 * Keep a package specifiy Map for even-numbered rows
	 */
	public static Map EvenRowMap = null;
	
	private CSSBasedRenderer cssRenderer;
	private HTMLWriter writer;
	private int dx;
	private int dy;
	
	static {
		OddRowMap = XMLWriter.copyMap(SortableTable.DefaultOddRowMap);
		OddRowMap.put("onclick","javascript:typewise_rowSelected(this,'rowodd');");
		EvenRowMap = XMLWriter.copyMap(SortableTable.DefaultEvenRowMap);
		EvenRowMap.put("onclick","javascript:typewise_rowSelected(this,'roweven');");
	}
	
	/**
	 * @param cssRenderer
	 */
	public SortableTableListRenderer(CSSBasedRenderer cssRenderer) {
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
		Style style = cssRenderer.newStyleFor(aTableList);
		style.width(dx * aTableList.getColumns());
		style.height(dy * aTableList.getRows());
		style.append("overflow:auto;");
		Map map = style.asMap();
		writer.div(map);
		this.renderAsSortableTable(aTableList);
		writer.end("div");
	}

	private void renderAsSortableTable(TableList tableList) {
		SortableTable table = new SortableTable(tableList.getName(),tableList.getItems().size()+1,tableList.getTableColumns().size());
		int column = 0;
		for (Iterator iter = tableList.getTableColumns().iterator(); iter.hasNext();) {
			TableColumn element = (TableColumn) iter.next();
			table.put(0,column++,element.getHeading().getText());
		}
		int row = 1;
		for (Iterator iter = tableList.getItems().iterator(); iter.hasNext();) {
			TableListItem element = (TableListItem) iter.next();
			for (int i = 0; i < element.getItems().length; i++) {
				table.put(row,i,element.get(i));
			}
			row++;
		}
		
		Map map_rowselected = new HashMap();
		map_rowselected.put("type", "hidden");
		map_rowselected.put("name", tableList.getName() + "_selectedrow");
		writer.tag("input", map_rowselected, true);		

		Map map_columnselected = new HashMap();
		map_columnselected.put("type", "hidden");
		map_columnselected.put("name", tableList.getName() + "_sortcolumn");
		writer.tag("input", map_columnselected, true);		
		
		Map map_sortmethod = new HashMap();
		map_sortmethod.put("type", "hidden");
		map_sortmethod.put("name", tableList.getName() + "_sortmethod");
		writer.tag("input", map_sortmethod, true);				
		
		table.columnClickedFunction = "typewise_columnClicked";
		table.evenRowMap = EvenRowMap;
		table.oddRowMap = OddRowMap;
		writer.stylesheet("table.css");
		table.write(writer,null,1,SortableTable.ASCENDING);
	}
	
	public static void writeSelectionScriptsOn(HTMLWriter writer){
		InputStream is = SortableTableListRenderer.class.getResourceAsStream("TableList.js");
		try {
			while (is.available()>0) writer.raw((char)is.read());
			is.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
}
