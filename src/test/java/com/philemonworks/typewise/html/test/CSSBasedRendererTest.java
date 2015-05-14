package com.philemonworks.typewise.html.test;

import com.philemonworks.typewise.html.CSSBasedRenderer;
import junit.framework.TestCase;

public class CSSBasedRendererTest extends TestCase {
	
	public void testAsHTML(){
		CSSBasedRenderer css = new CSSBasedRenderer(null);
		String s = css.asHTML("Hello&#10;World");
		System.out.println(s);
	}
}

