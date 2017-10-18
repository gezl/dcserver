package com.dc.platform;

import java.sql.Connection;
import java.util.List;

import com.dc.platform.dbinfo.DatabaseManager;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-5-14 下午4:21:20
 * @description : 数据库操作接口
 */
public interface BaseDao { 

	/**
	 * 执行添加SQL
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @param generatedKey oracle数据库下必须人为指定主键字段，否则返回-1
	 * @return 返回插入数据的主键
	 */
	public abstract int save(String sql, List<Object> parameters,String... generatedKey);
	/**
	 * 执行修改SQL
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 */
	public abstract void update(String sql, List<Object> parameters);
	/**
	 * 执行删除SQL
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 */	
	public abstract void delete(String sql, List<Object> parameters);
	/**
	 * 执行SQL
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 */		
	public abstract void excuteUpdate(String sql, List<Object> parameters);
	/**
	 * 执行查询SQL(只有一条记录)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract Object loadUniqueResult(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(只有一条记录)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract String loadUniqueResultAsString(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为int)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract int loadUniqueResultAsInteger(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为long)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract long loadUniqueResultAsLong(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为double)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract double loadUniqueResultAsDouble(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为float)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract float loadUniqueResultAsFloat(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为boolean)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract boolean loadUniqueResultAsBoolean(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为byte)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract byte loadUniqueResultAsByte(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(结果为short)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 数据承载对象
	 */
	public abstract short loadUniqueResultAsShort(String sql,  List<Object> parameters);
	/**
	 * 执行查询SQL(多记录)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 查询到的结果集
	 */
	public abstract SEntityList search(String sql, List<Object> parameters,int... pageModel);	
	/**
	 * 执行查询SQL(多记录)
	 * @param sql 原生 sql
	 * @param parameters sql中要用到的参数
	 * @return 查询到的结果集
	 */
	public abstract List<Object[]> searchToList(String sql, List<Object> parameters,int... pageModel);	
	/**
	 * 保存一个entity对象到数据库中
	 * @param entity entity传输对象
	 * @return 返回插入数据的主键
	 */
	public abstract int save(SEntity entity,String... databaseName);
	/**
	 * 更新数据库中的一个entity对象
	 * @param entity entity传输对象
	 */
	public abstract void update(SEntity entity,String... databaseName);
	/**
	 * 从数据库中删除一个entity对象
	 * 删除时以condition为首要条件，如果condition为空则自动提供columns主键作为条件
	 * @param entity entity传输对象
	 */
	public abstract void delete(SEntity entity,String... databaseName);	
	/**
	 * 通过SEntityList中的条件查询相关数据
	 * @param entityList 包含查询条件的SEntityList
	 * @return 查询到的结果集
	 */
	public abstract void load(SEntityList entityList);
	/**
	 * 通过SEntityList中的条件查询相关数据
	 * @param entityList 包含查询条件的SEntityList
	 * @return 查询到的结果集
	 */
	public abstract void load(SEntity entity);
	
	/**
	 * 获取数据信息服务对象
	 * @return
	 */
	public abstract DatabaseManager getDatabaseManager();
	/**
	 * 获取数据库连接
	 * @return
	 */
	public abstract Connection getDBConnection();
	/**
	 * 释放数据库连接
	 * @param conn
	 */
	public void releaseConn(Connection conn);
}


