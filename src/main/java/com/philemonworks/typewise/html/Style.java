package com.philemonworks.typewise.html;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import com.philemonworks.util.Color;

public class Style {
	private StringWriter buffer = new StringWriter(32);
	public Style(){
		super();
		this.buffer.write('{');
	}
	public Style background(Color aColor){
		if (aColor == null) return this;
		buffer.write("background:");
		buffer.write(aColor.toHTML());
		buffer.write(';');
		return this;
	}
	public Style foreground(Color aColor){
		if (aColor == null) return this;
		buffer.write("color:");
		buffer.write(aColor.toHTML());
		buffer.write(';');
		return this;
	}	
	public String toString(){
		return buffer.toString() + "}";
	}
	public Map asMap(){
		Map map = new HashMap();
		map.put("style",this.toString());
		return map;
	}
	public Style append(String entry){
		buffer.write(entry);
		if (entry.charAt(entry.length()-1) != ';')
			buffer.write(';');
		return this;
	}
	public Style font(String fontName){
		buffer.write("font-family:");
		buffer.write(fontName);
		buffer.write(";");
		return this;
	}
	public Style fontSize(String size){
		buffer.write("font-size:");
		buffer.write(size);
		buffer.write("pt;");
		return this;
	}
	public Style noBorder(){
		buffer.write("border-style:none;");
		return this;
	}
	public Style width(int w){
		buffer.write("width:");
		buffer.write(String.valueOf(w));
		buffer.write("px;");
		return this;
	}
	public Style height(int h){
		buffer.write("height:");
		buffer.write(String.valueOf(h));
		buffer.write("px;");
		return this;
	}	
	public Style borderStyle(String style){
		buffer.write("border-style:");
		buffer.write(style);
		buffer.write(';');
		return this;
	}
}
