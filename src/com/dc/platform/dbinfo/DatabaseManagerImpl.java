package com.dc.platform.dbinfo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.dc.platform.Context;

/**
 * 
 * @company
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2012-3-8 下午02:46:33
 * @description :数据库表信息管理
 */
@Repository("databaseManager")
public class DatabaseManagerImpl implements DatabaseManager {
	
	Logger log = Logger.getLogger(DatabaseManagerImpl.class);
	
	public static String DEFAULTDB = "default";
	
	private Map<String, Database> databases = new HashMap<String, Database>();
	
	/*
	 * 获取表信息
	 * @see
	 * com.imageinfo.dataservice.db.DBInfoPrivider#getTableInfo(java.lang.String)
	 */
	public Table getTable(String tableName,String... databaseName) {
		Database database = getDatabase(databaseName);
		if(database == null){
			log.error("数据库配置信息错误，找不到对应数据库");
			return null;
		}
		Table table = database.getTable(tableName);
		if(table == null){
			table = database.getView(tableName);
		}
		return table;
	}
	public Table getView(String viewName, String... databaseName) {
		Database database = getDatabase(databaseName);
		if(database == null){
			log.error("数据库配置信息错误，找不到对应数据库");
			return null;
		}
		Table table = database.getView(viewName);
		if(table == null){
			table = database.getTable(viewName);
		}
		return table;
	}	
	/*
	 * 获取数据库信息
	 * 
	 * @see com.imageinfo.dataservice.db.DBInfoPrivider#getDataBaseInfo()
	 */
	public Database getDatabase(String... databaseName) {
		Database database = null;
		if(databaseName.length > 0){
			database = databases.get(databaseName[0].toUpperCase());
			if(database == null){
				database = initDataBase(databaseName[0]);
				databases.put(database.getSchema(), database);
			}
		}else {
			database = databases.get(DEFAULTDB);
			if(database == null){
				database = initDataBase(DEFAULTDB);
				databases.put(database.getSchema(), database);
			}
		}
		return database;
	}

	/**
	 * 初始化数据库信息
	 * @param databaseName
	 * @return
	 */
	private Database initDataBase(String databaseName) {
		//获取数据库名
		Properties prop = new Properties();
		try {
			prop.load(DatabaseManagerImpl.class.getResourceAsStream("../../../../jdbc.properties"));
		} catch (IOException e) {
			log.error("读取[jdbc.properties]文件错误");
			throw new RuntimeException(e.getMessage());
		}
		String schema = prop.getProperty("default".equals(databaseName) ? "schema" :databaseName.toUpperCase() + ".schema");
		if(schema == null || "".equals(schema)){
			throw new RuntimeException("指定的数据库名配置不存在，请检查jdbc.properties文件！");
		}
		DataSource dataSource = (DataSource)Context.getBean(databaseName.toLowerCase().equals("default")?"dataSource": databaseName.toLowerCase()+ "_dataSource");
		if ("default".equals(databaseName)) {
			DEFAULTDB = schema.toUpperCase();
		}
		Database database = new Database(schema.toUpperCase(),dataSource);
		return database;
	}
}
