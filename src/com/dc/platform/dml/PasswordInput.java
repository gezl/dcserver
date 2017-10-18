package com.dc.platform.dml;

import com.dc.platform.SEntity;
import com.dc.platform.dbinfo.Column;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2014-3-28 上午10:47:53
 * @description : 密码输入框
 */
public class PasswordInput extends Input {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5628128593587557162L;
	
	public PasswordInput(String tableName, Column column, SEntity dmlColFormat) {
		super(tableName, column, dmlColFormat);
		this.set("type", "password");
		
		//增加两输入框对比功能
		String equalto = dmlColFormat.getValueAsString("equalto");
		if(!"".equals(equalto)){
			this.set("equalto",equalto);
		}
	}
	
}


