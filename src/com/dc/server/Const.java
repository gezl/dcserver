package com.dc.server;

/**
 * @Description: 状态码
 */
public class Const {

	// 客户端SQL执行出错
	public static final int TABLE_SQLISERROR_CODE = 1000000;

	// 服务器传输SQL无法被匹配
	public static final int TABLE_SQLISBAD_CODE = 1000001;

	// 客户端表不存在
	public static final int TABLE_NONEXISTENT_CODE = 1000002;
	
	// 客户端SQL执行成功
	public static final int TABLE_SQLISSUCCESS_CODE = 1000003;
	
	// 客户端SQL执行失败：字段不匹配（或值太多）
	public static final int TABLE_SQLFIELDISMORE_CODE = 1000004;

}
