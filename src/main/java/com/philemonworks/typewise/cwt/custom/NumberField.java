package com.philemonworks.typewise.cwt.custom;

import com.philemonworks.typewise.cwt.TextField;

/**
 * @author E.M.Micklei
 *
 */
public class NumberField extends TextField {

	public NumberField(String arg0, int arg1, int arg2, int arg3, int arg4) {
		super(arg0, arg1, arg2, arg3, arg4);
	}
	public int getIntValue(){
		return Integer.valueOf(super.getString()).intValue();
	}
	public void setIntValue(int value){
		super.setString(String.valueOf(value));
	}
}
