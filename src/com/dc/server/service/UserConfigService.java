package com.dc.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.key.DBEnum.MysqlDataType;
import com.dc.server.key.DBEnum.OracleDataType;
import com.dc.server.key.TypeEnum.DBType;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.util.PageUtil;
import com.dc.server.util.ToolsUtil;

@Service("userConfigService")
public class UserConfigService extends Base4DBService {
	
	public void listAllUserConfig(HTTPContext context) throws IOException,ServletException{
		
		 // 获取参数
        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        keyword = StringUtils.trimToEmpty(keyword);
        StringBuffer sb = new StringBuffer(" select duc.*,u.account,u.db_type,dt.trigger_name from db_user_config duc left join admin_user u on u.id=duc.user_id left join db_trigger dt on dt.id=duc.db_trigger_id  ");
        if(StringUtils.isNotBlank(keyword))
        {
        	sb.append("where account like '%" + keyword + "%'");
        }
        // 计算分页
        String sqlCount = "select count(*) as totalcount  from ("+sb.toString()+") ";
        int totalCount = baseDao.search(sqlCount, null).get(0).getValueAsInteger("totalcount");
        int totalPage = Double.valueOf(Math.ceil(totalCount / new Double(pageSize))).intValue();
        pageNum = PageUtil.getPageNum(pageNum, totalPage);
        // 结果集
        SEntityList sEntityList = baseDao.search(sb.toString(), null, pageNum, pageSize);
        //
        sEntityList.setPageNum(pageNum);
        sEntityList.setTotalCount(totalCount);
        sEntityList.setTotalPage(totalPage);
        sEntityList.setPageSize(pageSize);
        //
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/userConfig/list.jsp");
	}
	
	/**
	 * 
	* @Title: getColumnsList
	* @Description: 获取提取数据字段列表
	* @author gezhiling
	* @date  2017-7-5 下午9:19:37
	* @param context
	* @throws IOException
	* @throws ServletException
	 */
	public void getColumnsList(HTTPContext context) throws IOException,ServletException{
		
		String tableName = context.getParameter("tablename");
		 // 获取参数
        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        keyword = StringUtils.trimToEmpty(keyword);
        StringBuffer sb = new StringBuffer(" select *  from user_col_comments  where table_name= '"+tableName+"'");
        if(StringUtils.isNotBlank(keyword))
        {
        	sb.append("where column like '%" + keyword + "%'");
        }
        // 计算分页
        String sqlCount = "select count(*) as totalcount  from ("+sb.toString()+") ";
        int totalCount = baseDao.search(sqlCount, null).get(0).getValueAsInteger("totalcount");
        int totalPage = Double.valueOf(Math.ceil(totalCount / new Double(pageSize))).intValue();
        pageNum = PageUtil.getPageNum(pageNum, totalPage);
        // 结果集
        SEntityList sEntityList = baseDao.search(sb.toString(), null, pageNum, pageSize);
        //
        sEntityList.setPageNum(pageNum);
        sEntityList.setTotalCount(totalCount);
        sEntityList.setTotalPage(totalPage);
        sEntityList.setPageSize(pageSize);
        //
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.setAttribute("tablename", tableName);
        context.forword("/admin/search/tv_columns_list.jsp");
	}
	
	/**
	 * 
	* @Title: toEdit
	* @Description: 编辑用户配置页面
	* @author gezhiling
	* @date  2017-7-8 下午9:55:24
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void toEdit(HTTPContext context) throws ServletException, IOException {
		
		String id = context.getParameter("id");
		if(!("-1").equals(id)){
		String sql = " select duc.*,u.account ,dt.trigger_name from db_user_config duc left join admin_user u on u.id=duc.user_id left join db_trigger dt on dt.id=duc.db_trigger_id where duc.id = ? ";
		List<Object> param = new ArrayList<Object>();
		param.add(id);
		SEntity sEntity = baseDao.search(sql, param).get(0);
		
		context.setAttribute("sEntityConfig", sEntity);
		}
		context.forword("/admin/userConfig/edit.jsp");
		
	}
		
	
	
	
	/**
	 * 
	* @Title: edit
	* @Description: 编辑用户配置信息
	* @author gezhiling
	* @date  2017-7-6 上午11:04:26
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void edit(HTTPContext context) throws ServletException, IOException {
		
		String id = context.getParameter("id");
		String accountId = context.getParameter("accountId");
		String tv_name = context.getParameter("tv_name");
		String trigger_id = context.getParameter("trigger_id");
		String relation_fk_id = context.getParameter("relation_fk_id");
		String totablename = context.getParameter("totablename");
		String type = context.getParameter("type");
		
		 SEntity entity = new SEntity("db_user_config");
		 entity.setValue("user_id", accountId);
		 entity.setValue("db_trigger_id", trigger_id);
		 entity.setValue("table_view_name", tv_name);
		 entity.setValue("relation_fk_id", relation_fk_id);
		 entity.setValue("totablename", totablename);
		 entity.setValue("type", type);
		 if(StringUtils.isBlank(id)){
			 baseDao.save(entity);
		 }else{
			 entity.addCondition("id=?", id);
			 baseDao.update(entity);
		 }
		 
		 context.setAttribute("msg", "success");
	     context.forword("/save_result.jsp");
		
	}
	
	
	/**
	 * 
	* @Title: getColumnsConfig
	* @Description: 获取所有字段
	* @author gezhiling
	* @date  2017-7-5 下午9:19:37
	* @param context
	* @throws IOException
	* @throws ServletException
	 */
	public void getColumnsConfig(HTTPContext context) throws IOException,ServletException{
		
		String tableName = context.getParameter("tablename");
		String id = context.getParameter("id");
        // 结果集
		List<Object> parameters = new ArrayList<Object>();
        parameters.add(tableName);
        parameters.add(tableName);
		String sqlCol = "select tab.column_name,tab.data_type,tab.data_length,col.comments from user_tab_columns tab,user_col_comments col where tab.column_name = col.column_name and tab.table_name=? and col.table_name=?";
        SEntityList sEntityList = baseDao.search(sqlCol, parameters);
        //
        SEntity entity = new SEntity("db_user_config");
        entity.addCondition("id=?", id);
        baseDao.load(entity);
        String columns = entity.getValueAsString("columns");
        //
        if(StringUtils.isNotBlank(columns))
        {
        	String column[] = columns.split(",");
        	for (SEntity sEntity : sEntityList) {
        		String column_name = sEntity.getValueAsString("column_name");
    			for (String col : column) {
					if(column_name.equals(col)){
						sEntity.setValue("col", "true");
					}
				}
    		}
        }
        
        //
        context.setAttribute("sEntityList", sEntityList);
        context.setAttribute("tablename", tableName);
        context.setAttribute("id", id);
        context.forword("/admin/userConfig/columnsList.jsp");
	}
	
	/**
	 * 
	* @Title: editColumnsConfig
	* @Description: 编辑订阅字段
	* @author gezhiling
	* @date  2017-7-7 下午5:42:22
	* @param context
	* @throws IOException
	* @throws ServletException
	 */
	public void editColumnsConfig(HTTPContext context) throws IOException,ServletException{
		
		String id = context.getParameter("id"); 
		
		String[] columns = context.request.getParameterValues("column_name");
		String columnStr = ToolsUtil.join(",", columns);
		SEntity entity = new SEntity("db_user_config");
		entity.addCondition("id=?", id);
		entity.setValue("columns", columnStr);
		baseDao.update(entity);
		
//		entity.addCondition("id=?", id);
//		baseDao.load(entity);
//		String tablesql= entity.getValueAsString("tablesql");
//		String userId = entity.getValueAsString("user_id");
//		String tableName = entity.getValueAsString("table_view_name");
//		String totablename = entity.getValueAsString("totablename");
//		String tablesqlstate = entity.getValueAsString("tablesqlstate");
//		
//		//未生成创表语句之前不执行新增字段操作
//		 SEntity row = new SEntity("db_table_row");
//		 row.setValue("user_config_id", id);
//		 row.setValue("table_name", totablename);
//		if(StringUtils.isNotBlank(tablesql)){
//			if(tablesqlstate.equals(SqlStateType.EXECUTE.getKey())){
//			 SEntityList entityRows = new SEntityList("db_table_row");
//			 entityRows.addCondition("user_config_id=?", id);
//			 baseDao.load(entityRows);
//			 if(entityRows.size() > 0){
//				 for (String column : columns) {
//				 	boolean b = true;
//					 for (SEntity sEntity : entityRows) {
//						String columnName = sEntity.getValueAsString("column_name");
//						if(columnName.equals(column)){
//							b=false;
//						}
//						}
//					 if(b){//TODO 新增SQL
////						 String sqlData = "select column_name,data_type,data_length from user_tab_columns where table_name='"+tableName+"' and column_name='"+column+"'";
////							SEntity dataSEntity = baseDao.search(sqlData, null).get(0);
////							String type = dataSEntity.getValueAsString("data_type");
////							String length = dataSEntity.getValueAsString("data_length");
////							 String addColumnSql = setAddRowSQL(userId, totablename, column, type, length);
////							 row.setValue("add_column_sql", addColumnSql);
//							 row.setValue("column_name", column);
//							 row.setValue("state", RowType.NEW.getKey());
//							 baseDao.save(row);
//					 }
//				 }
//				}
//			 }else{
//				 for (String column : columns) {
//					 row.setValue("column_name", column);
//					 baseDao.save(row);
//				 }
//			 }
//			
//		}
		
		 context.setAttribute("msg", "success");
	     context.forword("/save_result.jsp");
	}
	
	/**
	 * 
	* @Title: toEditUserColumn
	* @Description: 编辑用户关联字段页面
	* @author gezhiling
	* @date  2017-7-8 下午11:49:13
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void toEditUserColumn(HTTPContext context) throws ServletException, IOException {
		
		String id = context.getParameter("id");
		SEntity sEntity = new SEntity("db_user_config");
		sEntity.addCondition(" id=? ", id);
		baseDao.load(sEntity);
		
		context.setAttribute("sEntity", sEntity);
		context.forword("/admin/userConfig/editColumn.jsp");
	}
	
	/**
	 * 
	* @Title: editUserColumn
	* @Description: 编辑用户关联字段
	* @author gezhiling
	* @date  2017-7-8 下午11:26:35
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void editUserColumn(HTTPContext context) throws ServletException, IOException {
		
		String id = context.getParameter("id");
		
		String hz_column = context.getParameter("hz_column");
		String hd_column = context.getParameter("hd_column");
		String lx_column = context.getParameter("lx_column");
		String small_lx = context.getParameter("small_lx");
		
		 SEntity entity = new SEntity("db_user_config");
		 entity.addCondition("id=?", id);
		 entity.setValue("hz_column", hz_column);
		 entity.setValue("hd_column", hd_column);
		 entity.setValue("lx_column", lx_column);
		 entity.setValue("small_lx", small_lx);
		 baseDao.update(entity);
		 
		 context.setAttribute("msg", "success");
	     context.forword("/save_result.jsp");
		
	}
	
	/**
	 * 获取所有字段 单选
	* @Title: getColumns
	* @Description: 
	* @author gezhiling
	* @date  2017-7-9 下午4:49:33
	* @param context
	* @throws IOException
	* @throws ServletException
	 */
	public void getColumns(HTTPContext context) throws IOException,ServletException{
		
		String tableName = context.getParameter("tablename");
		 // 获取参数
       String sql = " select *  from user_col_comments  where table_name= ? ";
       // 结果集
       List<Object> param = new ArrayList<Object>();
       param.add(tableName);
       SEntityList sEntityList = baseDao.search(sql,param);
       //
       context.setAttribute("sEntityList", sEntityList);
       context.forword("/admin/userConfig/columns.jsp");
	}
	
	
	 /**
	  * 
	 * @Title: enableCus
	 * @Description: 启用/停用用户配置
	 * @author gezhiling
	 * @date  2017-7-9 下午4:49:11
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	  */
    public void enableUConfig(HTTPContext context) throws ServletException, IOException {

        String id = context.getParameter("id");
        String enable = context.getParameter("enable");
        SEntity entity = new SEntity("db_user_config");
        entity.addCondition("id = ?", id);
        entity.setValue("status", enable.equals(String.valueOf(EnabledType.ENABLE.getKey())) ? EnabledType.DISABLED.getKey() : EnabledType.ENABLE.getKey());
        baseDao.update(entity);
        context.setAttribute("msg", "success");
        context.forword("/save_result.jsp");
    }
    
    public void createTable(HTTPContext context)  throws ServletException, IOException {

        String id = context.getParameter("id");
        String account =context.getParameter("account");
        SEntity config = new SEntity("db_user_config");
        config.addCondition("id=?", id);
        baseDao.load(config);
        //用户ID
        String userId = config.getValueAsString("user_id");
        //订阅字段
  		String columns = config.getValueAsString("columns");
  		String column[] = columns.split(",");
  		//需要生成的表名
  		String totablename = config.getValueAsString("totablename");
        
        SEntity user = new SEntity("admin_user");
        user.addCondition("id=?", userId);
        baseDao.load(user);
        String dbType = user.getValueAsString("db_type");
        String tableViewName = config.getValueAsString("table_view_name");
        //String sql ="select column_name,data_type,data_length from user_tab_columns where table_name=?";
        String sql = " select tab.column_name,tab.data_type,tab.data_length,tab.data_scale,col.comments from user_tab_columns tab,user_col_comments col where tab.column_name = col.column_name and tab.table_name=? and col.table_name=? ";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(tableViewName);
        parameters.add(tableViewName);
        SEntityList list = baseDao.search(sql, parameters);
        
  		//
        List<SEntity> sentityList = new ArrayList<SEntity>();
  		for (SEntity sEntity : list) {
			 SEntity tabcol = sEntity;
			 String filed = tabcol.getValueAsString("COLUMN_NAME");//列名
			 for (String col : column) {
			 	 if(col.equals(filed)) {
			 		sentityList.add(tabcol);
			 		continue;
		     	 }
			 }
		 }
	  String tableSql ="";
	 
	  HashMap<String, String> map = new HashMap<String, String>();
	  if(DBType.mysql.getKey()==Integer.parseInt(dbType)){
  	 		tableSql = createTableSql4Mysql(totablename,sentityList);
       }else if(DBType.oracle.getKey()==Integer.parseInt(dbType)){
    	   	tableSql = createTableSql4Oracle(totablename,sentityList);
       }else if(DBType.sqlserver.getKey()==Integer.parseInt(dbType)){
      	 //TODO
       }
	 //写入文件
	  String contextRoot = context.request.getContextPath();
	  ToolsUtil.createFile(contextRoot,account+"_"+totablename, tableSql);
	  //写入数据库
	  String sqlTableUpdate= "update db_user_config set tablesql=?  where id=?";
	  List<Object> param = new ArrayList<Object>();
	  param.add(tableSql);
	  param.add(id);
	  baseDao.update(sqlTableUpdate, param);
	  
	  String msg = "表已创建成功！";
	  String state = "200";
	  map.put("msg", msg);
	  map.put("state", state);
	  context.sendAsJson(JSONObject.toJSONString(map));
    }
    
    /**
     * 
    * @Title: createTableSql4Oracle
    * @Description: Oracle 建表语句
    * @author gezhiling
    * @date  2017-7-20 下午2:31:34
    * @param tableName
    * @param entityList
    * @return
     */
    private String createTableSql4Oracle(String tableName,List<SEntity> entityList){
    	StringBuffer oracleSql = new StringBuffer(" CREATE TABLE ");
    	oracleSql.append(tableName.toUpperCase());
    	oracleSql.append(" ( ");
    	StringBuffer sqlComments = new StringBuffer();
    	String join = "";
    	for (SEntity sEntity : entityList) {
			String column_name = sEntity.getValueAsString("COLUMN_NAME");;
			String data_type = sEntity.getValueAsString("DATA_TYPE");;
			String data_length =sEntity.getValueAsString("DATA_LENGTH");
			String data_scale = sEntity.getValueAsString("DATA_SCALE");
			String comments =sEntity.getValueAsString("COMMENTS");
			//建表语句
			oracleSql.append(join);
			oracleSql.append(column_name).append(" ");
			oracleSql.append(data_type);
			if(!data_type.equals("DATE")){
				oracleSql.append("(").append(data_length);
				if(data_type.equals("NUMBER")){
					oracleSql.append(",").append(data_scale);
				}
					oracleSql.append(")");
			}
			join =",";
			//表注释
			sqlComments.append(" COMMENT ON COLUMN ");
			sqlComments.append(tableName.toUpperCase()).append(".");
			sqlComments.append(column_name).append(" IS '");
			sqlComments.append(comments).append("' ;");
		}
    	oracleSql.append(");");
    	String result = oracleSql.toString()+sqlComments.toString();
    	return result;
    } 

    /**
     * 
    * @Title: createTableSql4Mysql
    * @Description: Mysql建表语句
    * @author gezhiling
    * @date  2017-7-20 下午2:31:58
    * @param tableName
    * @param entityList
    * @return
     */
    private String createTableSql4Mysql(String tableName,List<SEntity> entityList){
    	StringBuffer oracleSql = new StringBuffer(" CREATE TABLE ");
    	oracleSql.append(tableName);
    	oracleSql.append(" ( ");
    	for (SEntity sEntity : entityList) {
			String column_name = sEntity.getValueAsString("COLUMN_NAME");;
			String data_type = sEntity.getValueAsString("DATA_TYPE");;
			String data_length =sEntity.getValueAsString("DATA_LENGTH");
			String data_scale = sEntity.getValueAsString("DATA_SCALE");
			String comments =sEntity.getValueAsString("COMMENTS");
			//建表语句
			String columnType = oracleToMysql4DataType(data_type, data_length,data_scale);
			oracleSql.append(column_name).append(" ");
			oracleSql.append(columnType);
			if(column_name.equals("ID")){
				oracleSql.append(" NOT NULL ");
			}
			oracleSql.append(" COMMENT ' ");
			oracleSql.append(comments);
			oracleSql.append(" ' ,");
		}
    	oracleSql.append(" PRIMARY KEY (`ID`), UNIQUE KEY `ID` (`ID`) USING BTREE ) ENGINE=InnoDB DEFAULT CHARSET=utf8;");
    	String result = oracleSql.toString();
    	return result;
    } 
    
    private String oracleToMysql4DataType(String type,String length,String data_scale){
    	String resultType = "";
    	if(type.equals(OracleDataType.NUMBER.toString())){
    		if(StringUtils.isNotBlank(data_scale) && Integer.parseInt(data_scale)>0){
    			resultType=MysqlDataType.DOUBLE.toString()+"("+length+","+data_scale+")";
    		}else{
    			resultType=MysqlDataType.BIGINT.toString()+"("+length+")";
    		}
    	}
    	if(type.equals(OracleDataType.VARCHAR2.toString())){
    		resultType=MysqlDataType.VARCHAR.toString()+"("+length+")";
    	}
    	if(type.equals(OracleDataType.DATE.toString())){
    		resultType=MysqlDataType.DATETIME.toString();
    	}
    	return resultType;
    }
    
    
    
    public static void main(String[] args) {
    	/*String sp = "";
    	String [] ss = new String[]{"aaa","bbb","ccc"};
    	StringBuffer sb = new StringBuffer();
    	for(String s:ss ){
    		sb.append(sp).append(s);
    		sp=",";
    	}
    	System.out.println(sb.toString());*/
    	if("NUMBER".equals(OracleDataType.NUMBER.toString())){
    		System.out.println(11);
    	}
	}
    
	
}
