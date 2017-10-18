package com.dc.platform.dbinfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
/**
 * 
 * @company
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2012-3-8 下午02:30:59
 * @description : 数据库表
 */
public class Table{
	
	private static Logger log = Logger.getLogger(Table.class);
	//所属的数据库
	private Database database;
	
	//表名
	private String tableName;
	//主键列
	private Set<String> pkColumns = new HashSet<String>();
	//列信息<列名,列对象>
	private Map<String,Column> columns = new TreeMap<String, Column>();
	/**
	 * 获取某列的信息
	 * @param columnName 列名
	 * @return 
	 */
	public Column getColumn(String columnName){
		return columns.get(columnName.toUpperCase());
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName.toUpperCase();
	}
	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}	
	/**
	 * 获取所有字段
	 * @return
	 */
	public Map<String, Column> getColumns() {
		return columns;
	}
	/**
	 * 获取所有字段名
	 * @return
	 */
	public Set<String> getColumnNames(){
		return columns.keySet();
	}
	/**
	 * 添加字段描述
	 * @param columnName
	 * @param column
	 */
	public void setColumn(String columnName,Column column) {
		this.columns.put(columnName.toUpperCase(), column);
	}
	/**
	 * 获取所有主键名
	 * @return
	 */
	public Set<String> getPkColumns() {
		return pkColumns;
	}
	/**
	 * 设置主键
	 * @param pkColumns
	 */
	public void setPkColumn(String pkColumnName) {
		this.pkColumns.add(pkColumnName);
	}
	
	protected void init(Connection conn,DatabaseMetaData dbmt,boolean isSchema,String schema) {
		ResultSet rs = null;
		try {
			// 取得列信息
			if (isSchema) {
				rs = dbmt.getColumns(null, schema, tableName, null);
			} else {
				rs = dbmt.getColumns(schema, null, tableName, null);
			}
			Map<String, String> comments = null;
			if(dbmt.getDatabaseProductName().equalsIgnoreCase("oracle")){
				comments = getComments(conn,tableName);
			}
			while (rs.next()) {
				Column column = new Column();
				column.setName(rs.getString("COLUMN_NAME"));
				column.setDigits(rs.getInt("DECIMAL_DIGITS"));
				column.setSize(rs.getInt("COLUMN_SIZE"));
				column.setType(rs.getInt("DATA_TYPE"));
				column.setNullable(rs.getInt("NULLABLE") == DatabaseMetaData.columnNullable);
				column.setDefaultValue(rs.getString("COLUMN_DEF"));
				if(dbmt.getDatabaseProductName().equalsIgnoreCase("oracle")){
					column.setComment(comments.get(column.getName()));
				}else{
					column.setComment(rs.getString("REMARKS"));
				}
				columns.put(rs.getString("COLUMN_NAME"), column);
			}
			rs.close();
			
			// 取得外键、主键 等列信息
			rs = dbmt.getImportedKeys(null, schema, tableName);
			while (rs.next()) {
				String table = rs.getString("PKTABLE_NAME");
				String colname = rs.getString("PKCOLUMN_NAME");
				String fkName = rs.getString("FKCOLUMN_NAME");
				Column column = (Column)columns.get(fkName.toUpperCase());
				column.setFkColumn(true);
				column.setFkTable(table);
				column.setFkPColumn(colname);
			}
			rs.close();
			// 取得主键列信息
			rs = dbmt.getPrimaryKeys(null, schema, tableName);
			while (rs.next()) {
				String colname = rs.getString("COLUMN_NAME");
				Column column = (Column)columns.get(colname.toUpperCase());
				column.setPkColumn(true);
				pkColumns.add(colname.toUpperCase());
			}
			if("SYS_UNIT".equals(tableName)||"SYS_USER".equals(tableName)||"SYS_DICTIONARY".equals(tableName)){
				if(pkColumns.size() == 0){
					Column column = (Column)columns.get("ID");
					column.setPkColumn(true);
					pkColumns.add("ID");
				}
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("获取数据库表列信息失败: " + e.getMessage());
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
	 * 获取oracle数据库表字段注视
	 * @param conn 数据库链接
	 * @param tableName 表名
	 * @return
	 */
	private Map<String, String> getComments(Connection conn,String tableName) {

		String sql = "select column_name,comments from user_col_comments a where a.table_name = '"
				+ tableName + "'";
		Map<String, String> comments = new HashMap<String, String>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				comments.put(rs.getString("column_name"), rs.getString("comments")!=null ?rs.getString("comments"):rs.getString("column_name"));
			}
		} catch (SQLException e) {
			log.error("获取oracle注释失败: " + e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException e) {
					log.error("关闭查询结果集错误: " + e.getMessage());
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
					pstmt = null;
				} catch (SQLException e) {
					log.error("关闭会话集错误: " + e.getMessage());
				}
			}
		}
		return comments;
	}	
}
