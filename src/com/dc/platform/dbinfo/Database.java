package com.dc.platform.dbinfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

/**
 * 
 * @company 
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2012-3-8 下午02:41:09
 * @description :数据库信息
 */
public class Database{
	
	private static Logger log = Logger.getLogger(Database.class);
	/**
	 * 数据库名
	 */
	protected String schema;
	/**
	 * 是否是schema类型
	 */
	protected boolean isSchema;
	/**
	 * 数据源
	 */
	protected DataSource dataSource;
	/**
	 * 所有表列表
	 */
	protected Map<String,Table> tables = new HashMap<String,Table>();
	/**
	 * 所有视图列表
	 */
	protected Map<String,Table> views = new HashMap<String,Table>();
	
	public Database(String dataBaseName,DataSource dataSource){
		this.schema = dataBaseName;
		this.dataSource = dataSource;
		log.info("load database of [" + this.schema + "] started!");
		
		Connection connection = null;
		DatabaseMetaData dbmt = null;
		// 获得数据库链接
		try {
			connection = dataSource.getConnection();
			// 获取数据库基本信息
			dbmt = connection.getMetaData();
			// 判断数据类型
			isSchema = isUserExist(dbmt);
			loadTableNames(dbmt);
			loadViewNames(dbmt);
		} catch (SQLException e) {
			log.error("can not create database connection of [" + this.schema + "]! " + e.getMessage());
			throw new RuntimeException("can not create database connection of [" + this.schema + "]! " + e.getMessage());
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("close database connection of [" + this.schema + "] faild! " + e.getMessage());
				}
			}
		}
		
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Table getTable(String tableName) {
		Table table = null;
		tableName = tableName.toUpperCase();
		if(tables.containsKey(tableName)){
			table =  tables.get(tableName);
			if(table == null){
				table = loadTable(tableName);
				tables.put(tableName, table);
			}
		}else{
			log.info("The Table of given Name ["+tableName+"] is not exsit!");
			//throw new RuntimeException("The Table of given Name ["+tableName+"] is not exsit!");
		}
		return table;
	}
	/**
	 * 加载表信息
	 * @param tableName
	 * @return
	 */
	protected Table loadTable(String tableName){
		Table table = new Table();
		//获取表名
		table.setTableName(tableName);
		table.setDatabase(this);
		Connection connection = null;
		DatabaseMetaData dbmt = null;
		// 获得数据库链接
		try {
			connection = dataSource.getConnection();
			// 获取数据库基本信息
			dbmt = connection.getMetaData();
			table.init(connection, dbmt, isSchema,schema);
		} catch (SQLException e) {
			log.error("can not create database connection of [" + this.schema + "]! " + e.getMessage());
			throw new RuntimeException("can not create database connection of [" + this.schema + "]! " + e.getMessage());
		}finally{
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					log.error("close database connection of [" + this.schema + "] faild! " + e.getMessage());
				}
			}
		}
		
		return table;
	}

	public void setTable(String tableName,Table table) {
		this.tables.put(tableName.toUpperCase(), table);
	}

	public Table getView(String viewName) {
		Table view = null;
		viewName = viewName.toUpperCase();
		if(views.containsKey(viewName)){
			view =  tables.get(viewName);
			if(view == null){
				view = loadTable(viewName);
				views.put(viewName, view);
			}
		}else{
			log.info("The View of given Name ["+viewName+"] is not exsit!");
		}
		return view;
	}

	public void setView(String viewName,Table view) {
		this.tables.put(viewName.toUpperCase(), view);
	}
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	public boolean isSchema() {
		return isSchema;
	}

	public void setSchema(boolean isSchema) {
		this.isSchema = isSchema;
	}	
	/**
	 * 判断数据库类型
	 * @param dbmt 数据库信息
	 * @return
	 */
	protected boolean isUserExist(DatabaseMetaData dbmt) {
		List<String> list = new ArrayList<String>();
		boolean isSchema = true;
		ResultSet rs = null;
		try {
			// 取得模式数据
			rs = dbmt.getSchemas();
			while (rs.next()) {
				String schema = rs.getString("TABLE_SCHEM");
				list.add(schema);
			}
			rs.close();
			// 如果SCHEM数据为空，则取Catalog
			if (list.size() == 0) {
				rs = dbmt.getCatalogs();
				while (rs.next()) {
					String schema = rs.getString("TABLE_CAT");
					list.add(schema);
				}
				isSchema = false;
			}
		} catch (SQLException e) {
			log.error("获取数据库schema错误:" + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭查询结果集错误: " + e.getMessage());
				}
				rs = null;
			}
		}
		return isSchema;
	}
	/**
	 * 取得指定模式下的所有表名
	 * @param dbmt 数据库信息
	 * @param schema 用户名
	 * @param isSchema 数据库类型
	 * @param types 类型：table or view
	 * @return
	 */
	private void loadTableNames(DatabaseMetaData dbmt) {
		String[] types = new String[] { "TABLE" };
		ResultSet rs = null;
		try {
			if (isSchema) {
				rs = dbmt.getTables(null, schema, null, types);
			} else {
				rs = dbmt.getTables(schema, null, null, types);
			}
			while (rs.next()) {
				String tableName = rs.getString("TABLE_NAME");
				// 过滤掉无效元素
				if (tableName.indexOf('/') > -1 || tableName.indexOf('$') > -1) {
					continue;
				}
				tables.put(tableName.toUpperCase(), null);
			}
		} catch (SQLException e) {
			log.error("获取表名失败: " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭查询结果集错误: " + e.getMessage());
				}
				rs = null;
			}
		}
	}		
	/**
	 * 取得指定模式下的所有表名
	 * @param dbmt 数据库信息
	 * @param schema 用户名
	 * @param isSchema 数据库类型
	 * @param types 类型：table or view
	 * @return
	 */
	private void loadViewNames(DatabaseMetaData dbmt) {
		String[] types = new String[] { "VIEW" };
		ResultSet rs = null;
		try {
			if (isSchema) {
				rs = dbmt.getTables(null, schema, null, types);
			} else {
				rs = dbmt.getTables(schema, null, null, types);
			}
			while (rs.next()) {
				String viewName = rs.getString("TABLE_NAME");
				// 过滤掉无效元素
				if (viewName.indexOf('/') > -1 || viewName.indexOf('$') > -1) {
					continue;
				}
				views.put(viewName.toUpperCase(), null);
			}
		} catch (SQLException e) {
			log.error("获取表名失败: " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					log.error("关闭查询结果集错误: " + e.getMessage());
				}
				rs = null;
			}
		}
	}
}
