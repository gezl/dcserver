package com.dc.platform.dml;

import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @description :
 */
public class DateInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5628128593587557162L;
	
	private String dateFmt= "yyyy-MM-dd HH:mm:ss";
	
	public DateInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		// TODO Auto-generated constructor stub
		this.remove("maxlength");
		//时间格式
		dateFmt = dmlColFormat.getValueAsString("dateFmt");
		if(!"".equals(dateFmt)){
			this.set("dateFmt",dateFmt);
		}
		//时间比较，比XX大
		String maxthan = dmlColFormat.getValueAsString("maxthan");
		if(!"".equals(maxthan)){
			this.set("maxthan",maxthan);
		}
		//时间比较，比XX小
		String minthan = dmlColFormat.getValueAsString("minthan");
		if(!"".equals(minthan)){
			this.set("minthan",minthan);
		}
		//转换特定默认值
		if(this.defaultValue != null && !"".equals(this.defaultValue)){
			if("Dnow".equals(this.defaultValue)){
				this.defaultValue = new SimpleDateFormat(dateFmt).format(new Date());
			}
		}
	}
	
	/**
	 * 生产具体的HTML
	 */
	public String toString(){
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
		Element inputDateButton = dd.addElement("a");
		inputDateButton.addAttribute("class", "inputDateButton");
		inputDateButton.addAttribute("href", "javascript:;");
		inputDateButton.setText("选择");
		
		return dl.asXML().replace("<dd>", "<dd>"+preHtml).replace("</dd>", endHtml + "</dd>");
	}
	
}


