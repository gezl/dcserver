package com.dc.platform.dml;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dc.platform.BaseDao;
import com.dc.platform.Context;
import com.dc.platform.SEntity;
import com.dc.platform.dbinfo.Column;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2014-3-31 上午11:54:50
 * @description : 自动补全输入域
 */
public class SuggestInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1238789555802230105L;
	
	private String columnName;
	
	private String tableName;
	
	private String fkmodulesn;
	
	private String fkcolumn;
	
	private String fkShowName;
	
	private String extrafield;
	
	public SuggestInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		this.tableName = tableName;
		columnName = column.getName();
		
		//添加属性ID
		this.set("name", this.tableName+"."+ columnName + "_ShowName");
		this.set("suggestFields",columnName + "_ShowName");
		this.set("lookupGroup",this.tableName);
		
		//添加补全相关参数
		fkmodulesn = dmlColFormat.getValueAsString("fkmodulesn");
		fkcolumn = dmlColFormat.getValueAsString("fkcolumn");
		fkShowName = dmlColFormat.getValueAsString("fkshowname");
		extrafield = dmlColFormat.getValueAsString("extrafield");
		this.set("suggestUrl","s?md="+fkmodulesn+"&ac=loadSDatas&columnName="+columnName+"&fkName="+fkcolumn+"&fkShowName="+fkShowName+"&extraField="+extrafield);
	}
	
	@Override
	public String toString() {
		if("hidden".equals(this.get("type"))){
			return toHiddenString();
		}		
		Element dl = DocumentHelper.createElement("dl");
		if(nowrap != null && !"".equals(nowrap)){
			dl.addAttribute("class", "nowrap");
		}
		Element dt = dl.addElement("dt");
		dt.setText(this.displayName);
		Element dd = dl.addElement("dd");
		Element hiddeninput = dd.addElement("input");
		int index = this.get("name").indexOf("__S");
		if(index != -1){
			hiddeninput.addAttribute("name", this.tableName+"."+this.columnName + this.get("name").substring(index));
		}else{
			hiddeninput.addAttribute("name", (this.tableName+"."+this.columnName));
		}		
		hiddeninput.addAttribute("type", "hidden");
		hiddeninput.addAttribute("value", value);
		
		Element input = dd.addElement("input");
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
			input.addAttribute("value", loadShowValue());
		}else{
			input.addAttribute("value", loadShowValue());
		}	
		
		return dl.asXML().replace("<dd>", "<dd>"+preHtml).replace("</dd>", endHtml + "</dd>");
	}
	public void setHiddenFeild(){
		remove("class");
		set("type", "hidden");
		set("name", this.tableName+"."+columnName);
	}	
	//加载显示用的值
	private String loadShowValue(){
		if(this.value == null || "".equals(this.value)){
			return "";
		}
		
		BaseDao sysBaseDao = (BaseDao)Context.getBean("sysBaseDao");
		
		SEntity moduleEntity = new SEntity("sys_module");
		moduleEntity.addCondition("sn=?", fkmodulesn);
		sysBaseDao.load(moduleEntity);
		
		BaseDao baseDao = (BaseDao)Context.getBean("baseDao");
		SEntity entity = new SEntity(moduleEntity.getValueAsString("viewname"));
		entity.addCondition(fkcolumn + "=?", this.value);
		baseDao.load(entity);	
		return entity.getValueAsString(fkShowName);
	}
	
	public String toHiddenString(){
		Element input = DocumentHelper.createElement("input");
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
			input.addAttribute("value", loadShowValue());
		}else{
			input.addAttribute("value", loadShowValue());
		}	
		return input.asXML();
	}	
}


