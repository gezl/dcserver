package com.dc.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;
import javax.servlet.ServletException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.dc.platform.SEntityList.Sort_type;
import com.dc.platform.dbinfo.Column;
import com.dc.platform.dbinfo.DatabaseManager;
import com.dc.platform.dbinfo.Table;
import com.dc.platform.permission.User;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-4-12 下午3:32:28
 * @description : 业务层基础类
 */
@Service("base4DBService")
public class Base4DBService { 

	Logger log = Logger.getLogger(Base4DBService.class);
	
	@Resource(name="baseDao")
	protected BaseDao baseDao;
	
	@Resource(name="databaseManager")
	protected DatabaseManager databaseManager;
	

	
	/**
	 * 默认查询对应页面
	 */
	protected String indexPage = "/p/system/index.jsp";

	/**
	 * 获取数据库操做对象
	 * @return
	 */
	public BaseDao getBaseDao(){
		return this.baseDao;
	}
	/**
	 * 设置DAO
	 * @param baseDao
	 */
	public void setBaseDao(BaseDao baseDao){
		this.baseDao = baseDao;
	}
	
	public String getIndexPage() {
		return indexPage;
	}

	public void setIndexPage(String indexPage) {
		this.indexPage = indexPage;
	}
	
	public DatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	/**
	 * 默认通用查询方法
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public void search(HTTPContext context) throws IOException, ServletException{
		loadGlobalListDatas(context);
		context.forword(context.getModuel().getIndexUrl() != null && !"".equals(context.getModuel().getIndexUrl())?context.getModuel().getIndexUrl():"/p/system/index.jsp");
	}
	/**
	 * 输出为EXCL
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public void expExcl(HTTPContext context) throws IOException, ServletException{
		loadGlobalListDatas(context);
		context.forword("/p/system/export/exportTable.jsp");
	}
	
	/**
	 * 取出查询配置信息定义的sql,定义显示的字段(字段名，类型)
	 * @param mdsn
	 * @param userId
	 * @return
	 */
	protected SEntity getSearchCol(String mdsn,int userId){
		//取出查询配置信息定义的sql,定义显示的字段(字段名，类型)
		SEntity searchCol = new SEntity("SYS_MODULE_SEARCHCOL");
		searchCol.addCondition("modulesn=?", mdsn);
		searchCol.addCondition("userid=?", userId);
		baseDao.load(searchCol);
		if(searchCol.getColumns().size() == 0){
			searchCol.removeCondition("userid=?");
			searchCol.addCondition("userid is null");
			baseDao.load(searchCol);
		}
		return searchCol;
	}
	/**
	 * 查询数据数据格式化信息
	 * @param mdsn
	 * @return
	 */
	protected Map<String, SEntity> getColFormats(String mdsn){
		Map<String, SEntity> colFormats = (Map<String, SEntity>)SCache.get(mdsn + "_SYS_MODULE_COL_FORMAT");
		if(colFormats == null){
			SEntityList colFormat = new SEntityList("SYS_MODULE_COL_FORMAT");
			colFormat.addCondition("modulesn=?", mdsn);
			baseDao.load(colFormat);
			colFormats = new HashMap<String, SEntity>();
			for(int i=0;i<colFormat.size();i++){
				SEntity sEntity = colFormat.get(i);
				String code = sEntity.getValueAsString("COLUMNNAME");
				colFormats.put(code, sEntity);
			}
			SCache.put(mdsn + "_SYS_MODULE_COL_FORMAT", colFormats);
		}
		return colFormats;
	}
	/**
	 * 获取数据库中查询配置
	 * @param mdsn
	 * @param userId
	 * @return
	 */
	protected SEntityList getSearchConfigs(String mdsn,int userId){
		SEntityList searchConfigs = new SEntityList("SYS_MODULE_SEARCHCONFIG");
		searchConfigs.addCondition("modulesn=?", mdsn);
		searchConfigs.addCondition("userid=?", userId);
		searchConfigs.addSortCondition("sort", Sort_type.SORT_UP);
		baseDao.load(searchConfigs);
		if(searchConfigs.size() == 0){
			searchConfigs.removeCondition("userid=?");
			searchConfigs.addCondition("userid is null");
			baseDao.load(searchConfigs);
		}
		return searchConfigs;
	}
	
	protected void loadGlobalListDatas(HTTPContext context) throws IOException, ServletException{
		//获取表结构信息
		Table table = databaseManager.getTable(context.getModuel().getTableName());
		Table view = databaseManager.getTable(context.getModuel().getViewName());
		
		//查询主体
		SEntityList entityList = new SEntityList(view.getTableName());
		//用户继承类的方法切入
		extentSearch(entityList,context);
		//设置分页参数
		int pageNum = context.getParameter("pageNum")==null ?1:Integer.parseInt(context.getParameter("pageNum"));
		int numPerPage = context.getParameter("numPerPage")==null ?15:Integer.parseInt(context.getParameter("numPerPage"));
		entityList.setPageNum(pageNum);
		entityList.setPageSize(numPerPage);
		
		//设置排序
//		setSortCondition(context.getUser(),context.getModuel().getSn(),entityList);
//		if(context.getParameter("orderField") != null && !"".equals(context.getParameter("orderField"))){
//			entityList.addSortCondition(context.getParameter("orderField"), "asc".equals(context.getParameter("orderDirection"))?Sort_type.SORT_UP:Sort_type.SORT_DOWN);
//		}
		
		//获取查询条件
		String[] paramNames = context.request.getParameterValues("paramNames");
		//参数值
		Map<String, String> paramValues = new HashMap<String, String>();
		//如果没有查询参数传递就从数据库中取默认参数
		if(paramNames == null || paramNames.length == 0){
//			for (int i = 0; i < searchConfigs.size(); i++) {
//				SEntity searchConfig = searchConfigs.get(i);
//				String orand = searchConfig.getValueAsString("ORAND");
//				String columnName = searchConfig.getValueAsString("COLUMNNAME");
//				String operater = searchConfig.getValueAsString("OPERATER");
//				String defaultValue = searchConfig.getValueAsString("DEFAULTVALUE");
//				if(defaultValue != null && !"".equals(defaultValue)){
//					if("like ?".equalsIgnoreCase(operater)){
//						entityList.addCondition(orand + " " + columnName + " " + operater, "%" + defaultValue + "%");
//					}else{
//						entityList.addCondition(orand + " " + columnName + " " + operater, DBUtils.parseObject(view.getColumns().get(columnName.replace("$", "")).getType(), defaultValue));
//					}
//				}
//				paramValues.put(columnName + operater, defaultValue);
//			}
		}else{
			String[] orands = context.request.getParameterValues("orands");
			String[] operaters = context.request.getParameterValues("operaters");
			String[] dataTypes = context.request.getParameterValues("dataTypes");
			for (int i = 0; i < paramNames.length; i++) {
				String orand = orands[i];
				String paramName = paramNames[i];
				String operater = operaters[i];
				String dataType = dataTypes[i];
				String valueString = context.getParameter(paramName);
				if(valueString == null && StringUtils.containsIgnoreCase(dataType,"string")){
					valueString = context.getParameter("searchItem." + paramName);
				}
				if(valueString != null && !"".equals(valueString.trim())){
					if("like ?".equalsIgnoreCase(operater)){
						entityList.addCondition(orand + " " + paramName + " " + operater, "%" + valueString + "%");
					}else{
						entityList.addCondition(orand + " " + paramName + " " + operater, DBUtils.parseObject(dataType, valueString));
					}
				}
				paramValues.put(paramName + operater, valueString);
			}
		}
		
		baseDao.load(entityList);
		
		context.setAttribute("entityList", entityList);
		context.setAttribute("paramValues", paramValues);
		context.setAttribute("table", table);
		context.setAttribute("view", view);
		context.setAttribute("module", context.getModuel());
		
	}
	/**
	 * 用于不同业务时直接修改查询要求
	 * @param entityList
	 * @return 如果不需要方法走下去，返回false直接返回。
	 */
	protected boolean extentSearch(SEntityList entityList,HTTPContext context){
		return true;
	}
	/**
	 * 设置排序条件
	 * @param user
	 * @param mdsn
	 */
	protected void setSortCondition(User user,String mdsn,SEntityList entityList){
		SEntityList sortList = new SEntityList("SYS_MODULE_SEARCHSORT");
		if(user.getUserName().equals("dc_admin")){
			sortList.addCondition("MODULESN=? and USERID is null", mdsn);
		}else{
			sortList.addCondition("MODULESN=? and USERID=?", mdsn, user.getId());
		}
		sortList.addSortCondition("sort", Sort_type.SORT_UP);
		baseDao.load(sortList);
		for (SEntity entity : sortList) {
			entityList.addSortCondition(entity.getValueAsString("COLUMNNAME"), entity.getValueAsBoolean("ASCORDESC")?Sort_type.SORT_UP:Sort_type.SORT_DOWN);
		}
	}
	/**
	 * 设置查询条件
	 * 如查表中有数据权限字段，则默认启用，如果没有配置权限，默认看自己的
	 * @param user
	 * @param mdsn
	 */
	protected void setDataPermission(User user,String mdsn,SEntityList entityList){
		//添加数据权限
		if(!user.getUserName().equals("dc_admin")){
			String dataPermission = user.getDataPermission(mdsn);
			if(dataPermission != null){
				entityList.addCondition(dataPermission);
			}else{
				entityList.addCondition("DATAPERMISSION='"+String.valueOf(user.getCode())+"'");
			}
		}
	}
	
	/**
	 * 默认查找带回数据查询
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public void loadLDatas(HTTPContext context) throws IOException, ServletException{
		this.loadGlobalListDatas(context);
		String multi = context.getParameter("Multi");
		if(multi == null || "".equals(multi)){
			context.forword("/p/system/lookup/Lookup.jsp");
		}else{
			context.forword("/p/system/lookup/MultiLookup.jsp");
		}
	}
	
	/**
	 * 默认通用修改方法
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public void update(HTTPContext context) throws IOException, ServletException{
		String[] tables = context.request.getParameterValues("table");
		if(tables.length == 1){
			List<SEntity> entities = context.createSEntityByRequest(databaseManager.getTable(tables[0]));
			for (int i = 0; i < entities.size(); i++) {
				baseDao.update(entities.get(i));
			}
		}else if(tables.length > 1){
			List<SEntity> entities = context.createSEntityByRequest(databaseManager.getTable(tables[0]));
			SEntity sEntity = entities.get(0);
			if(entities.size() == 1){
				baseDao.update(sEntity);
			}else{
				log.error("M[" + context.getModuel().getSn() +"],T[" + context.getModuel().getTableName() + "]---多表数据修改时，主表数据多于一条");
				context.reply("数据修改失败！");
				return;
			}
			
			for (int i = 1; i < tables.length; i++) {
				if("SYS_FILE_FORMAT".equalsIgnoreCase(tables[i])){
					baseDao.update("update sys_file_format set recordid="+sEntity.getValueAsString("id")+" ,status=1 where modulesn='"+context.getParameter("md")+"'  and recordid is null", null);
				}
				else{
					Map<String,String> fkColumnsMap = context.getFkTable(tables[i]);
					if(fkColumnsMap != null){
						SEntity subSEntity = new SEntity(tables[i]);
						for (Entry<String, String> entry : fkColumnsMap.entrySet()) {
							subSEntity.addCondition(entry.getKey() + "=?", sEntity.getValue(entry.getValue()));
						}
						baseDao.delete(sEntity);
					}
					
					List<SEntity> subEntities = context.createSEntityByRequest(databaseManager.getTable(tables[i]));
					for (int j = 0; j < subEntities.size(); j++) {
						SEntity subSEntity = subEntities.get(j);
						if("add".equals(subSEntity.getRowAction())){
							baseDao.save(subSEntity);
						}else if("update".equals(subSEntity.getRowAction())){
							baseDao.update(subSEntity);
						}else if("delete".equals(subSEntity.getRowAction())){
							baseDao.delete(subSEntity);
						}
					}
				}
			}
		}
		context.reply("数据修改成功！");
	}
	
	/**
	 * 默认通用删除方法
	 * @param context
	 * @throws IOException
	 * @throws ServletException
	 */
	public void delete(HTTPContext context) throws IOException, ServletException{
		String rowId = context.getParameter("rowId");
		SEntity sEntity = new SEntity(context.getModuel().getTableName());
		sEntity.addCondition("id=?", rowId);
		baseDao.delete(sEntity);
		context.reply("数据删除成功！");
	}
   /**
    * 验证唯一
    * @param context
    * @return
    * @throws IOException
    * @throws ServletException
    */
	public void checkUnique(HTTPContext context) throws IOException, ServletException{
		SEntityList sList = new SEntityList(context.getModuel().getViewName() == null?context.getModuel().getTableName():context.getModuel().getViewName());
	    String  name = context.getModuel().getViewName() == null?context.getModuel().getTableName():context.getModuel().getViewName()+"."+context.getParameter("checkName");
		sList.addCondition(context.getParameter("checkName")+"=?", context.getParameter(name.toUpperCase()));
		baseDao.load(sList);
		if(sList.size()>0)
			context.sendAsString("false");
		else
			context.sendAsString("true");
    }
	public void searchItem(HTTPContext context) throws IOException, ServletException{
		Table view = databaseManager.getTable(context.getModuel().getViewName());
		String column = context.getParameter("column");
		String dtype = context.getParameter("dtype");
		String inputValue = context.getParameter("inputValue");
		String sql = null;
		List<Object> parameters = new ArrayList<Object>();
		if(dtype == null){
			sql = "select distinct " + column.replace("$", "") + " from " + view.getTableName() + " where " + column.replace("$", "") + " like ?";
		}else{
			sql = "select value from SYS_DICTIONARY where type=? and value like ?";
			parameters.add(dtype);
		}
		parameters.add("%" + inputValue + "%");
		List<Object[]> results = baseDao.searchToList(sql, parameters, 1,10);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < results.size(); i++) {
			Object[] objs = results.get(i);
			String value = objs[0].toString();
			JSONObject jo = new JSONObject();
			jo.put(column, value);
			ja.add(jo);
		}
		context.sendAsJson(ja.toJSONString());
	}
	
	
	/**
	 * 
	 * @param tableName 要获取页面的table名字注意大写
	 * @param context 上下文
	 * @return
	 * @throws JSONException
	 */
	public SEntityList getValuesFromRequest(String tableName,HTTPContext context) throws JSONException{
		
		Table t = baseDao.getDatabaseManager().getTable(tableName);
		
		Map<String, Column> col = t.getColumns();
		
		SEntityList sEntityList = new SEntityList(tableName);
		
		org.json.JSONObject jsonobj = new org.json.JSONObject(context.getParameter("jsonData"));
		
		int num = jsonobj.getInt(tableName);
		
		for(int i=0;i<num;i++){
			SEntity sEntity = new SEntity(tableName);
			for (Iterator iterator = col.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next().toString();
				if(col.get(key).isPkColumn()){
					continue;
				}
				String value=context.getParameter(tableName+"["+i+"]."+key);
				sEntity.setValue(key, value);
			}
			sEntityList.add(sEntity);
		}
		return sEntityList;
	}
	
	
	/**
	 * 
	 * @param tableName 要获取页面的table名字注意大写
	 * @param context 上下文
	 * @return
	 */
	public SEntity getValueFromRequest(String tableName,HTTPContext context){
		
		Table t = baseDao.getDatabaseManager().getTable(tableName);
		
		Map<String, Column> col = t.getColumns();
		
		SEntity sEntity = new SEntity(tableName);
		for (Iterator iterator = col.keySet().iterator(); iterator.hasNext();) {
			String key = iterator.next().toString();
			if(col.get(key).isPkColumn()){
				continue;
			}
			String value=context.getParameter(tableName+"."+key);
			sEntity.setValue(key, value);
		}
		return sEntity;
	}
	
}


