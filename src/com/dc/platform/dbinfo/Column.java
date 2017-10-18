package com.dc.platform.dbinfo;

import com.dc.platform.DBUtils;

/**
 * 
 * @company
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2012-3-8 下午02:40:39
 * @description :描述Table的列信息
 */
public class Column{
	// 列名称
	private String name;
	//对应java类型
	private String type;
	// 列长度
	private int size;
	// 小数位数
	private int digits;
	// 列是否可以为空
	private boolean nullable;
	// 列默认值
	private String defaultValue;
	// 是否是主键列
	private boolean pkColumn = false;
	// 是否外键列
	private boolean fkColumn = false;
	// 外键表名
	private String fkTable;
	// 外键表主键名
	private String fkPColumn;
	// 列备注信息
	private String comment;

	public Column(){};
	
	public Column(String name, int type, int size, boolean nullable, String comment) {
		this.name = name;
		this.size = size;
		this.nullable = nullable;
		this.comment = comment;
	}	

	public boolean isPkColumn() {
		return pkColumn;
	}

	public void setPkColumn(boolean pkColumn) {
		this.pkColumn = pkColumn;
	}

	public boolean isFkColumn() {
		return fkColumn;
	}

	public void setFkColumn(boolean fkColumn) {
		this.fkColumn = fkColumn;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(int type) {
		this.type = DBUtils.getColumnOfJType(type,this.size,this.digits);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}

	public int getDigits() {
		return digits;
	}

	public void setDigits(int digits) {
		this.digits = digits;
	}

	public String getComment() {
		return comment;
	}
	public String getCommentDisplay() {
		return comment.indexOf("@")!=-1?comment.split("@")[0]:comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getFkTable() {
		return fkTable;
	}

	public void setFkTable(String fkTable) {
		this.fkTable = fkTable;
	}

	public String getFkPColumn() {
		return fkPColumn;
	}

	public void setFkPColumn(String fkPColumn) {
		this.fkPColumn = fkPColumn;
	}
}
