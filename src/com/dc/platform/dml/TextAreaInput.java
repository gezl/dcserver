package com.dc.platform.dml;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dc.platform.SEntity;
import com.dc.platform.dbinfo.Column;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2014-3-28 上午10:48:32
 * @description :
 */
public class TextAreaInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7555043575297101308L;
	
	public TextAreaInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		this.remove("type");
	}

	@Override
	public String toString() {
		Element dl = DocumentHelper.createElement("dl");
		dl.addAttribute("class", "nowrap");
		Element dt = dl.addElement("dt");
		dt.setText(this.displayName);
		Element dd = dl.addElement("dd");
		Element input = dd.addElement("textarea");
		input.addAttribute("rows", "5");
		input.addAttribute("cols", "75");
		
		Set<Entry<String, String>> entrySet = entrySet();
		for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if(value != null && !"".equals(value)){
				input.addAttribute(key, value);
			}
		}
		if(this.defaultValueForce || this.value == null || "".equals(this.value)){
			if(this.defaultValue != null){
				input.setText(this.defaultValue);
			}
		}else{
			input.setText(this.value);
		}	
		return dl.asXML().replace("<dd>", "<dd>"+preHtml).replace("</dd>", endHtml + "</dd>");
	}
}


