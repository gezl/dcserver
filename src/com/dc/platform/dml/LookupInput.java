package com.dc.platform.dml;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dc.platform.BaseDao;
import com.dc.platform.Context;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.platform.dbinfo.Column;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2014-3-31 上午11:54:50
 * @description : 查找带回输入域
 */
public class LookupInput extends Input {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2389435309309846731L;

	private String columnName;
	
	private String tableName;
	
	private String href;
	
	private String fkmodulesn;
	
	private String fkcolumn;
	
	private String fkShowName;
	
	private String extrafield;
	
	public LookupInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		this.set("readonly", "readonly");
		this.tableName=tableName;
		columnName = column.getName();
		//添加属性ID
		this.set("name", this.tableName+"."+columnName + "_ShowName");
		//添加补全相关参数
		fkcolumn =dmlColFormat.getValueAsString("fkcolumn");
		fkShowName = dmlColFormat.getValueAsString("fkshowname");
		extrafield = dmlColFormat.getValueAsString("extrafield");
		fkmodulesn = dmlColFormat.getValueAsString("fkmodulesn");
		this.href="s?md="+fkmodulesn+"&ac=loadLDatas&columnName="+columnName+"&fkName="+fkcolumn+"&fkShowName="+fkShowName+"&extraField="+extrafield;
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
		if(this.defaultValueForce || this.value == null || "".equals(this.value)){
			if(this.defaultValue != null){
				hiddeninput.addAttribute("value", defaultValue);
			}
		}else{
			hiddeninput.addAttribute("value", value);
		}
		
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
			if(this.defaultValue != null){
				input.addAttribute("value", loadShowValue(defaultValue));
			}
		}else{
			input.addAttribute("value", loadShowValue(value));
		}
		Element a = dd.addElement("a");
		a.addAttribute("class", "btnLook");
		a.addAttribute("lookupGroup",this.tableName);
		a.addAttribute("rel","lookupAddOnlydialog");
		a.addAttribute("href", href);
		a.setText("查找带回");
		return dl.asXML().replace("<dd>", "<dd>"+preHtml).replace("</dd>", endHtml + "</dd>");
	}
	public void setHiddenFeild(){
		remove("class");
		set("type", "hidden");
		set("name", this.tableName+"."+columnName);
	}	
	
	//加载显示用的值
	private String loadShowValue(String value){
		if(value == null || "".equals(value)){
			return "";
		}
		BaseDao sysBaseDao = (BaseDao)Context.getBean("sysBaseDao");
		
		SEntity moduleEntity = new SEntity("sys_module");
		moduleEntity.addCondition("sn=?", fkmodulesn);
		sysBaseDao.load(moduleEntity);
		
		BaseDao baseDao = (BaseDao)Context.getBean("baseDao");
		SEntityList entity = new SEntityList(moduleEntity.getValueAsString("viewname"));
		entity.addCondition(fkcolumn + "=?", this.value);
		baseDao.load(entity);	
		return entity.isEmpty()?"":entity.get(0).getValueAsString(fkShowName);
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
			input.addAttribute("value", defaultValue);
		}else{
			input.addAttribute("value", value);
		}
		return input.asXML();
	}
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
	
}


