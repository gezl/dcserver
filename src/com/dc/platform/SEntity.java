package com.dc.platform;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-4-12 上午10:46:09
 * @description : 持久化对象
 */
public class SEntity {
	
	private static Logger log = Logger.getLogger(SEntity.class);
	
	/**
	 *  表名
	 */
	private String tableName;
	
	/**
	 *  列
	 */
	private Map<String, Object> columns = new HashMap<String, Object>();
	/**
	 *  查询的列
	 */
	private List<String> searchColumns = new ArrayList<String>();	
	/**
	 * 查询、修改、删除 条件
	 */
	private Map<String, Object[]> conditions = new HashMap<String, Object[]>();

	/**
	 * 页面传回判断操作类型
	 */
	private String rowAction = "add";
	
	/**
	 * 以此方法创建的SEntity的不包含数据库连接。 </br>
	 * 如果需要操作数据库，请设置数据库操作对象 setDbOperator(DBOperator dbOperator)
	 * @param tableName
	 */
	public SEntity(String tableName){
		this.tableName = tableName.toLowerCase();
	}
	
	/**
	 * 添加新条件  </br>
	 * **要求不能直接把参数写在sql里，用法如PreparedStatment </br>
	 * 新加参数间关系为and </br>
	 * 示例:</br>
	 * 1. addCondition("para1=?","参数") </br>
	 * 2. addCondition("para1=? and para2=?","参数1","参数2")</br>
	 * 2. addCondition("para1=? and para2=?",{"参数1","参数2"})</br>
	 * 3. addCondition("para1 is not null")	
	 * 
	 * @param sql where后的sql 例如：para=? and para>? and para&lt;? and para like '%?%'
	 * @param value sql 对应的参数，参数长度为可变参数</br>
	 * @throws Exception 当参数不符合要求时抛出运行时异常
	 */
	public void addCondition(String sql,Object... values) throws RuntimeException{
		
		if(sql == null){
			log.error("sql不能为空");
			throw new RuntimeException("sql不能为空");
		}
		sql = sql.trim();
		if("".equals(sql)){
			log.error("sql不能为空");
			throw new RuntimeException("sql不能为空");
		}
		if(StringUtils.countMatches(sql, "?") != values.length){
			log.error("sql的替代符与参数个数不一致");
			throw new RuntimeException("sql的替代符与参数个数不一致");
		}
		conditions.put(sql, values);
	}
	/**
	 * 删除相关查询条件
	 * @param sql 添加条件时的SQL
	 * @throws RuntimeException
	 */
	public void removeCondition(String sql) throws RuntimeException{
		
		if(sql == null){
			throw new RuntimeException("sql不能为空");
		}
		sql = sql.trim();
		if("".equals(sql)){
			log.error("sql不能为空");
			throw new RuntimeException("sql不能为空");
		}
		conditions.remove(sql);
	}	
	/**
	 * 添加要修改的值
	 * @param column 表字段名
	 * @param value	表字段值
	 * @throws RuntimeException 当字段不存在或值类型不对时跑出运行时异常
	 */
	public void setValue(String column,Object value) throws RuntimeException{
		columns.put(column.toLowerCase(), value);
	}
	
	/**
	 * 获取字段值的字符串形式
	 * @param columnName 获取的字段名
	 * @param format 格式化要求
	 * @return
	 */
	public String getValueAsString(String columnName,String... format){
		
		Object valueObj = columns.get(columnName.toLowerCase());
		String value = "";
		if (valueObj == null) {
			return value;
		}
		
		//如果数据为日期类型则默认转化为日期类型
		if(valueObj instanceof Date || valueObj instanceof java.sql.Date || valueObj instanceof Timestamp){
			value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(valueObj);
		}else if(valueObj instanceof oracle.sql.TIMESTAMP){
			try {
				value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(((oracle.sql.TIMESTAMP) valueObj).dateValue());
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else {
			value = valueObj.toString();
		}
		//如果指定了格式化类型则执行指定的格式化
		//数据可能会经过多次格式化
		if(format.length == 1 && format[0] != null && !"".equals(format[0])){
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = null;
			try {
				jsonObject = (JSONObject) jsonParser.parse(format[0]);
			} catch (ParseException e) {
				log.error(columnName + " 字段格式化配置错误：" + e.getMessage());
				throw new RuntimeException(columnName + " 字段格式化配置错误：" + e.getMessage());
			}
			//四舍五入
			if(jsonObject.containsKey("round")){
				if(valueObj instanceof Float || valueObj instanceof Double){
					String valueString = (String)jsonObject.get("round");
					BigDecimal decimal = new BigDecimal((Double)valueObj);
					decimal = decimal.setScale(Integer.parseInt(valueString), RoundingMode.HALF_UP);
					value = decimal.toPlainString();
				}
			}
			//进位法
			if(jsonObject.containsKey("roundup")){
				if(valueObj instanceof Float || valueObj instanceof Double){
					String valueString = (String)jsonObject.get("roundup");
					BigDecimal decimal = new BigDecimal((Double)valueObj);
					decimal = decimal.setScale(Integer.parseInt(valueString), RoundingMode.UP);
					value = decimal.toPlainString();
				}
			
			}
			//舍位法
			if(jsonObject.containsKey("rounddown")){
				if(valueObj instanceof Float || valueObj instanceof Double){
					String valueString = (String)jsonObject.get("rounddown");
					BigDecimal decimal = new BigDecimal((Double)valueObj);
					decimal = decimal.setScale(Integer.parseInt(valueString), RoundingMode.DOWN);
					value = decimal.toPlainString();
				}
			}
			//日期时间
			if(jsonObject.containsKey("date")){
				if(valueObj instanceof Date || valueObj instanceof java.sql.Date || valueObj instanceof Timestamp){
					String valueString = (String)jsonObject.get("date");
					SimpleDateFormat sdf = new SimpleDateFormat(valueString);
					value = sdf.format(valueObj);
				}					
			
			}
		}
		return value;
	}
	/**
	 * 获取int型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Integer getValueAsInteger(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return bigDecimal == null ? null : new Integer(bigDecimal.intValue());
	}
	/**
	 * 获取double型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Double getValueAsDouble(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : new Double(bigDecimal.doubleValue());
	}
	/**
	 * 获取float型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Float getValueAsFloat(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : new Float(bigDecimal.floatValue());
	}
	/**
	 * 获取long型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Long getValueAsLong(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : new Long(bigDecimal.longValue());
	}
	/**
	 * 获取short型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Short getValueAsShort(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : new Short(bigDecimal.shortValue());
	}
	/**
	 * 获取byte型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Byte getValueAsByte(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : new Byte(bigDecimal.byteValue());
	}
	/**
	 * 获取boolean型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Boolean getValueAsBoolean(String columnName){
		BigDecimal bigDecimal = (BigDecimal)columns.get(columnName.toLowerCase());
		return  bigDecimal == null ? null : bigDecimal.intValue() != 0?true:false;
	}
	/**
	 * 获取blob型字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Blob getValueAsBLOB(String columnName){
		Blob blob = (Blob)columns.get(columnName.toLowerCase());
		return  blob ;
	}
	/**
	 * 获取字段值
	 * @param columnName 获取的字段名
	 * @return
	 */
	public Object getValue(String columnName){
		return columns.get(columnName.toLowerCase());
	}	
	/**
	 * 把SEntity对象转换为JSON对象
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public JSONObject toJson() {
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonDatas = new JSONObject();
		jsonObj.put("tablename", this.getTableName());
		Set<String> keySet = columns.keySet();
		for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
			String key = iterator.next();
			jsonDatas.put(key, getValue(key));
		}
		jsonObj.put("datas", jsonDatas);
		return jsonObj;
	}	
	/**
	 * 把SEntity对象转换为JSON的字符串格式
	 * @return
	 */
	public String toJsonString() {
		return toJson().toString();
	}	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Map<String, Object> getColumns() {
		return columns;
	}

	public Map<String, Object[]> getConditions() {
		return conditions;
	}

	public String getRowAction() {
		return rowAction;
	}

	public void setRowAction(String rowAction) {
		this.rowAction = rowAction;
	}
	/**
	 * 设置要查询的字段
	 * @param searchColumns
	 */
	public void setSearchColumns(String... searchColumns) {
		for (String searchColumn : searchColumns) {
			this.searchColumns.add(searchColumn);
		}
	}
	public List<String> getSearchColumns() {
		return searchColumns;
	}
	
	public boolean isEmpty(){
		if(columns.size()==0){
			return true;
		}
	    return false;
	}
	
	/**
	 * 删除SEntity中的字段以及数据
	 * @param columnName
	 */
	public void removeColumn(String columnName){
		columns.remove(columnName.toLowerCase());
	}
}

