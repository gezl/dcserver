package com.dc.platform.dml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.dc.platform.SEntity;
import com.dc.platform.dbinfo.Column;
import com.dc.platform.permission.ThreadLocalSession;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2014-3-28 上午10:24:01
 * @description : 定义自动生成的输入域，通过对象形式生成
 */
public class Input extends HashMap<String, String> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4609050547508888741L;
	/**
	 * 字段显示名
	 */
	protected String displayName;
	/**
	 * 字段名
	 */
	protected String columnName;
	/**
	 * 字段值
	 */
	protected String value;
	/**
	 * 字段默认值
	 */
	protected boolean defaultValueForce = false;
	/**
	 * 字段默认值
	 */
	protected String defaultValue;
	/**
	 * 输入域是否独占一行
	 */
	protected String nowrap;

	/**
	 * 是否在列表中显示
	 */
	protected boolean showInList;
	
	/**
	 *是否为只读 
	 */
    protected boolean isReadonly;
    
    /**
     *前置代码 
     */
    protected String preHtml = "";
    
    /**
     *后置代码
     */
    protected String endHtml = "";
    
	public Input(){}
	
	public Input(String tableName,Column column,SEntity dmlColFormat){
		this.displayName = column.getCommentDisplay();
		this.columnName = column.getName();
		this.set("name", tableName + "." + column.getName());
	    this.set("type","text");
		//添加class控制内容
		String checkClass = dmlColFormat.getValueAsString("CHECKCLASS");
		if(checkClass != null && !"".equals(checkClass)){
			this.set("class",checkClass);
		}
		//最大长度控制
		String maxlength = dmlColFormat.getValueAsString("maxlength");
		if(maxlength != null && !"".equals(maxlength)){
			this.set("maxlength",maxlength);
		}else {
			this.set("maxlength",column.getSize()+"");
		}
		//最小长度控制
		String minlength = dmlColFormat.getValueAsString("minlength");
		if(minlength != null && !"".equals(minlength)){
			this.set("minlength",minlength);
		}
		//最大值控制
		String min = dmlColFormat.getValueAsString("minnum");
		if(min != null && !"".equals(min)){
			this.set("min",min);
		}
		//最小值控制
		String max = dmlColFormat.getValueAsString("maxnum");
		if(max != null && !"".equals(max)){
			this.set("max",max);
		}
		//前置代码 
		preHtml = dmlColFormat.getValueAsString("preHtml");
		//后置代码 
		endHtml = dmlColFormat.getValueAsString("endHtml");
		//是否为只读 
		nowrap = dmlColFormat.getValueAsString("nowrap");
		//唯一性验证
		String checkUnique = dmlColFormat.getValueAsString("checkUnique");
		if(checkUnique != null && !"".equals(checkUnique)){
			this.set("remote",checkUnique);
		}
		if(column.isPkColumn()){
			this.set("pkColumn","true");
		}
		
		//是否在列表中显示
		this.setShowInList(dmlColFormat.getValueAsBoolean("SHOWINLIST"));
		//是否只读
		Boolean isreadonly = dmlColFormat.getValueAsBoolean("ISREADONLY");
		if(isreadonly != null && isreadonly){
		   this.set("readonly", "readonly"); 
		}
		//默认值
		if(dmlColFormat.getValueAsString("DEAFAULTVAL")!=null && !"".equals(dmlColFormat.getValueAsString("DEAFAULTVAL"))){
			if(dmlColFormat.getValueAsString("DEAFAULTVAL").startsWith("Force:")){
				this.defaultValueForce = true;
				this.defaultValue = dmlColFormat.getValueAsString("DEAFAULTVAL").substring(6);
			}else{
				this.defaultValue = dmlColFormat.getValueAsString("DEAFAULTVAL");
			}
		}else{
			this.defaultValue = column.getDefaultValue();
		}
		//转换特定默认值
//		if(this.defaultValue != null && !"".equals(this.defaultValue)){
//			if("Duser".equals(this.defaultValue)){
//				this.defaultValue = String.valueOf(ThreadLocalSession.getUser().getId());
//			}else if("Dusername".equals(this.defaultValue)){
//				this.defaultValue = String.valueOf(ThreadLocalSession.getUser().getName());
//			}
//            //else if("Dnow".equals(this.defaultValue)){
//            //    this.defaultValue = new SimpleDateFormat(dateFmt).format(new Date());
//            //}
//		}
	}
	
	/**
	 * 生产具体的HTML
	 */
	public String toString(){
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
		Element input = dd.addElement("input");
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
				input.addAttribute("value", defaultValue);
			}
		}else{
			input.addAttribute("value", value);
		}
		return  dl.asXML().replace("<dd>", "<dd>"+preHtml).replace("</dd>", endHtml + "</dd>");
	}
	
	public String toHiddenString(){
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
		return input.asXML();
	}

	public String getId() {
		return this.get("id");
	}

	public void setId(String id) {
		this.put("id", id);
	}

	public String getName() {
		return this.get("name");
	}

	public void setName(String name) {
		this.put("name", name);
	}
	public String getNowrap() {
		return nowrap;
	}

	public void setNowrap(String nowrap) {
		this.nowrap = nowrap;
	}
	/**
	 * 设置属性值
	 * @param key
	 * @param value
	 */
	public void set(String key, String value){
		this.put(key, value);
	}
	public void setValue(String value){
		this.value = value;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public boolean isShowInList() {
		return showInList;
	}

	public void setShowInList(boolean showInList) {
		this.showInList = showInList;
	}
	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	public boolean isReadonly() {
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	public void setHiddenFeild(){
		remove("class");
		set("type", "hidden");
	}
}


