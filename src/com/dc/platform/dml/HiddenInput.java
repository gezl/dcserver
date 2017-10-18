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
 * @date 2014-3-28 上午10:47:53
 * @description : 隐藏域
 */
public class HiddenInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = -970211212169309545L;

	public HiddenInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		this.set("type", "hidden");
	}
	public HiddenInput(String tableName, String columnName) {
		this.set("type", "hidden");
		this.columnName = columnName;
		this.set("name", tableName + "." + columnName);
		//是否在列表中显示
		showInList = false;
	}
	
	/**
	 * 生产具体的HTML
	 */
	public String toString(){
		Element input = DocumentHelper.createElement("input");
		Set<Entry<String, String>> entrySet = entrySet();
		for (Iterator<Entry<String, String>> iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<String, String> entry = iterator.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if(value != null && !"".equals(value) && !"maxlength".equalsIgnoreCase(key)){
				input.addAttribute(key, value);
			}
		}
		if(this.defaultValueForce || this.value == null || "".equals(this.value)){
			if(this.defaultValue != null){
				input.addAttribute("value", defaultValue);
			}
		}else{
			input.addAttribute("value", value);
		}
		return preHtml + input.asXML() + endHtml;
	}
	
}


