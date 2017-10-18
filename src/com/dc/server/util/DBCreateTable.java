package com.dc.server.util;

import java.util.ArrayList;
import java.util.List;

import com.dc.platform.BaseDao;
import com.dc.platform.SEntityList;
import com.dc.server.key.DBEnum.MysqlDataType;
import com.dc.server.key.DBEnum.OracleDataType;

public class DBCreateTable {

	/**
	 * 
	* @Title: checkTable
	* @Description: 判断表是否存在 ORACLE
	* @author gezhiling
	* @date  2017-7-19 上午11:51:47
	* @param baseDao
	* @param tableName
	* @return
	 */
	public static boolean checkTableIsExist4Oracle(BaseDao baseDao, String tableName) {
		boolean isExist = false;
		String sql = "select 1 from all_all_tables where table_name=?";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(tableName);
		SEntityList list = baseDao.search(sql, parameters);
		if (list != null && list.size() > 0)
			isExist = true;

		return isExist;
	}
	
	
	public static boolean checkTableIsExist4Mysql(BaseDao baseDao, String tableName) {
		boolean isExist = false;
		String sql = "select 1 from all_all_tables where table_name=?";
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(tableName);
		SEntityList list = baseDao.search(sql, parameters);
		if (list != null && list.size() > 0)
			isExist = true;

		return isExist;
	}
	
	
	
	public static  String createMysqlRowSQL(String tableName,List<String> rowList){
	    	
			StringBuffer sb = new StringBuffer(" alter table ").append(tableName).append(" add ");
			String join = "";
			for (String row : rowList) {
				String [] col =  row.split("-");
				String rowName =col[0];
				String type =col[1];
				String length =col[2];
				sb.append(join);
				String typeSql = oracleToMysql4DataType(type, length);
				sb.append(rowName).append(typeSql);
				join =",";
			}
			String resultSql = sb.toString();
	    	return resultSql;
	    }
	    
	    
	public static  String createOracleRowSQL(String tableName,List<String> rowList){

			StringBuffer sb = new StringBuffer(" alter table ").append(tableName).append(" add (");
    		String join = "";
    		for (String row : rowList) {
				String [] col =  row.split("-");
				String rowName =col[0];
				String type =col[1];
				String length =col[2];
				sb.append(join);
				sb.append(rowName).append(" ").append("type");
				if(!type.equals("DATE"))
					sb.append("(").append(length).append(")");
				join =",";
			}
    		sb.append(")");
    		String resultSql = sb.toString();
	    	return resultSql;
	    }
	    
	    
	public static String oracleToMysql4DataType(String type,String length){
	    	String resultType = "";
	    	if(type.equals(OracleDataType.NUMBER.toString())){
	    		resultType=MysqlDataType.BIGINT.toString()+"("+length+")";
	    	}
	    	if(type.equals(OracleDataType.VARCHAR2.toString())){
	    		resultType=MysqlDataType.VARCHAR.toString()+"("+length+")";
	    	}
	    	if(type.equals(OracleDataType.DATE.toString())){
	    		resultType=MysqlDataType.DATETIME.toString();
	    	}
	    	return resultType;
	    }

}
