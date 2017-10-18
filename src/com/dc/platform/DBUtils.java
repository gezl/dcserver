package com.dc.platform;

import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.dc.platform.utils.SUtil;

/**
 * @company 东昌数码
 *          <p>
 *          Copyright: Copyright (c) 2012
 *          </p>
 * @author sslin
 * @date 2013-5-15 上午10:48:17
 * @description : 数据库操作辅助
 */
public class DBUtils {
	/**
	 * 支持的数据库类型
	 */
	public enum DBType {
		mysql, oracle, db2, mssql
	}

	/**
	 * 组合分页SQL
	 * 
	 * @param sql
	 * @param dbType
	 *            数据库类型
	 * @param pageModel
	 *            分页参数
	 * @return
	 */
	public static String createPageSQL(String sql, DBType dbType, int... pageModel) {
		// 如果分页参数错误则不做分页
		if (pageModel.length == 0 || pageModel.length > 2) {
			return sql;
		}
		// 计算分页数据
		int pageNo = pageModel[0];
		int pageSize = pageModel.length == 2 ? pageModel[1] : 10;

		if (dbType == DBType.oracle) {
			return oraclePageSQL(sql, pageNo, pageSize);
		} else if (dbType == DBType.mysql) {

		} else if (dbType == DBType.mssql) {

		} else if (dbType == DBType.db2) {

		}
		return sql;
	}

	/**
	 * oracle 数据库分页参数组合
	 * 
	 * @param sql
	 * @param pageNo
	 *            页码
	 * @param pageSize
	 *            每页条数
	 * @return 组合后的SQL
	 */
	private static String oraclePageSQL(String sql, int pageNo, int pageSize) {
		int start = (pageNo - 1) * pageSize;
		int end = pageNo * pageSize;
		sql = "select * from (select row_.*, rownum rownum_ from ( " + sql + ") row_ ) where rownum_ <= " + end + " and rownum_ >" + start;
		return sql;
	}

	// 通过JDBC SQLType代码及字符类型的值，取得特定类型的对象?
	public static String getColumnOfJType(int type, int size, int digits) {
		String jType = null;
		switch (type) {
		case Types.VARCHAR:
		case Types.CHAR:
			jType = "java.lang.String";
			break;
		case Types.BOOLEAN:
			jType = "java.lang.Boolean";
			break;
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
			jType = "java.lang.Integer";
			break;
		case Types.BIGINT:
			jType = "java.lang.Long";
			break;
		case Types.FLOAT:
			jType = "java.lang.Float";
			break;
		case Types.DECIMAL:
			jType = "java.lang.Integer";
			if(digits > 0){
				jType =  "java.lang.Double";
			}else if(size > 10){
				jType =  "java.lang.Long";
			}
			break;
		case Types.NUMERIC:
		case Types.DOUBLE:
			jType = "java.lang.Double";
			break;
		case Types.DATE:
			jType = "java.sql.Date";
			break;
		case Types.TIME:
			jType = "java.sql.Time";
			break;
		case Types.TIMESTAMP:
			jType = "java.sql.Timestamp";
			break;
		case Types.LONGVARCHAR:
		case Types.CLOB:
			jType = "CLOB";
			break;
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
		case Types.BLOB:
			jType = "BLOB";
			break;
		case Types.OTHER:
			jType = "java.lang.Object";
			break;
		default:
			throw new RuntimeException("无法处理的类型，" + getJdbcTypeName(type));
		}
		return jType;
	}
	// 通过JDBC SQLType代码，取得类型名
	public static String getJdbcTypeName(int jdbcType) {
		String typeName = "";
		switch (jdbcType) {
		case Types.ARRAY:
			typeName = "ARRAY";
			break;
		case Types.BOOLEAN:
			typeName = "BOOLEAN";
			break;
		case Types.BIGINT:
			typeName = "BIGINT";
			break;
		case Types.BINARY:
			typeName = "BINARY";
			break;
		case Types.BIT:
			typeName = "BIT";
			break;
		case Types.BLOB:
			typeName = "BLOB";
			break;
		case Types.CHAR:
			typeName = "CHAR";
			break;
		case Types.CLOB:
			typeName = "CLOB";
			break;
		case Types.DATALINK:
			typeName = "DATALINK";
			break;
		case Types.DATE:
			typeName = "DATE";
			break;
		case Types.DECIMAL:
			typeName = "DECIMAL";
			break;
		case Types.DISTINCT:
			typeName = "DISTINCT";
			break;
		case Types.DOUBLE:
			typeName = "DOUBLE";
			break;
		case Types.FLOAT:
			typeName = "FLOAT";
			break;
		case Types.INTEGER:
			typeName = "INTEGER";
			break;
		case Types.JAVA_OBJECT:
			typeName = "JAVA_OBJECT";
			break;
		case Types.LONGVARBINARY:
			typeName = "LONGVARBINARY";
			break;
		case Types.LONGVARCHAR:
			typeName = "LONGVARCHAR";
			break;
		// 以下为JDK1.6才添加的内容
		// case Types.LONGNVARCHAR:
		// typeName = "LONGNVARCHAR";
		// break;
		// case Types.NCHAR:
		// typeName = "NCHAR";
		// break;
		// case Types.NCLOB:
		// typeName = "NCLOB";
		// break;
		// case Types.NVARCHAR:
		// typeName = "NCLOB";
		// break;
		// case Types.ROWID:
		// typeName = "ROWID";
		// break;
		// case Types.SQLXML:
		// typeName = "SQLXML";
		// break;
		case Types.NULL:
			typeName = "NULL";
			break;
		case Types.NUMERIC:
			typeName = "NUMERIC";
			break;
		case Types.OTHER:
			typeName = "OTHER";
			break;
		case Types.REAL:
			typeName = "REAL";
			break;
		case Types.REF:
			typeName = "REF";
			break;
		case Types.SMALLINT:
			typeName = "SMALLINT";
			break;
		case Types.STRUCT:
			typeName = "STRUCT";
			break;
		case Types.TIME:
			typeName = "TIME";
			break;
		case Types.TIMESTAMP:
			typeName = "TIMESTAMP";
			break;
		case Types.TINYINT:
			typeName = "TINYINT";
			break;
		case Types.VARBINARY:
			typeName = "VARBINARY";
			break;
		case Types.VARCHAR:
			typeName = "VARCHAR";
			break;
		}
		return typeName;
	}
	/**
	 * 把对象转换为指定类型对象，如果传入对角为非字符串则原样返回
	 * @param jType java类型，如：int、String等
	 * @param value 需要转换的对象
	 * @return
	 */
	public static Object parseObject(String jType,Object object){
		if(object == null || !(object instanceof String)){
			return object;
		}
		String value = (String)object;
		if(jType.equalsIgnoreCase("int")||jType.equalsIgnoreCase("java.lang.Integer")){
			try{
				return Integer.parseInt(value);
			}catch(NumberFormatException e){
				return object;
			}
		}else if(jType.equalsIgnoreCase("byte")||jType.equalsIgnoreCase("java.lang.Byte")){
			try{
				return Byte.parseByte(value);
			}catch(NumberFormatException e){
				return object;
			}
		} else if(jType.equalsIgnoreCase("short")||jType.equalsIgnoreCase("java.lang.Short")){
			try{
				return Short.parseShort(value);
			}catch(NumberFormatException e){
				return object;
			}			
		}else if(jType.equalsIgnoreCase("long")||jType.equalsIgnoreCase("java.lang.Long")){
			try{
				return Long.parseLong(value);
			}catch(NumberFormatException e){
				return object;
			}			
		} else if(jType.equalsIgnoreCase("float")||jType.equalsIgnoreCase("java.lang.Float")){
			try{
				return Float.parseFloat(value);
			}catch(NumberFormatException e){
				return object;
			}			
		} else if(jType.equalsIgnoreCase("double")||jType.equalsIgnoreCase("java.lang.Double")){
			try{
				return Double.parseDouble(value);
			}catch(NumberFormatException e){
				return object;
			}			
		} else if(jType.equalsIgnoreCase("java.lang.String")){
			return value;
		} else if(jType.equalsIgnoreCase("boolean")||jType.equalsIgnoreCase("java.lang.Boolean")){
			try{
				return Boolean.valueOf(value);
			}catch(Exception e){
				return object;
			}			
		} else if(jType.equalsIgnoreCase("java.sql.Date")||jType.equalsIgnoreCase("java.sql.Timestamp")){
			try {
	            return SUtil.isEmptyString(value)?value:Timestamp.valueOf(value);
	        } catch (Exception ex) {
	        	SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
		        java.util.Date date = null;
				try {
					date = myFormat.parse(value);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        return new Timestamp(date.getTime());
	        }
		}  else {
			return value;	
		}
	}
}