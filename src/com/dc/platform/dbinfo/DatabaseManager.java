package com.dc.platform.dbinfo;


/**
 * 
 * @company
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2012-3-8 下午02:45:21
 * @description : 库表信息管理
 */
public interface DatabaseManager {
	/**
	 * 根据表名取得表信息Table，如果不存在返回null
	 * @param tableName
	 * @return
	 */
	public Table getTable(String tableName,String... databaseName);
	/**
	 * 根据视图名取得视图信息Table，如果不存在返回null
	 * @param viewName
	 * @return
	 */
	public Table getView(String viewName,String... databaseName);	
	/**
	 * 获取数据库所有详细信息
	 * @return
	 */
	public Database getDatabase(String... databaseName);

}
