package com.dc.server.service;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;

@Service("triggerManagerService")
public class TriggerManagerService extends Base4DBService {
	
	/**
	* @Title: listAllTrigger
	* @Description:  查询所有数据提取配置的触发器
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void listAllTrigger(HTTPContext context) throws ServletException, IOException{
		
	    int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
		SEntityList listAllTrigger = new SEntityList("db_trigger");
		listAllTrigger.setPageNum(pageNum);
		listAllTrigger.setPageSize(pageSize);
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
        	listAllTrigger.addCondition("trigger_name like '%" + keyword + "%' or primary_table_name like '%" + keyword + "%'");
        }
		baseDao.load(listAllTrigger);
		
		context.setAttribute("listAllTrigger", listAllTrigger);
		context.forword("/admin/trigger/list.jsp");
		
	}
	
	/**
	 * 
	* @Title: getTriggerList
	* @Description: 获取所有触发器配置
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	 public void getTriggerList(HTTPContext context) throws ServletException, IOException {

	        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
	        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
	        String keyword = context.getParameter("keyword");
	        SEntityList sEntityList = new SEntityList("db_trigger");
	        sEntityList.setPageNum(pageNum);
	        sEntityList.setPageSize(pageSize);
	        // 根据关键字查询
	        if (!StringUtils.isBlank(keyword)) {
	            sEntityList.addCondition("trigger_name like '%" + keyword + "%'");
	        }
	        baseDao.load(sEntityList);
	        context.setAttribute("keyword", keyword);
	        context.setAttribute("sEntityList", sEntityList);
	        context.forword("/admin/search/trigger_list.jsp");
	    }
	 
	 
	 /**
	 * @Title: addTrigger
	 * @Description: 创建触发器
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	  */
	 public void saveAndCreateTrigger(HTTPContext context) throws ServletException, IOException {
		String  primaryTableName = context.getParameter("primaryTableName").trim();
		String  triggerName =context.getParameter("triggerName").trim();
		
		SEntity entity = new SEntity("db_trigger");
		entity.setValue("primary_table_name", primaryTableName);
		entity.setValue("trigger_name", triggerName);
		entity.setValue("foreign_table_name", "DB_TEMP");
		baseDao.save(entity);
		
		String triggerSql = createTriggerSql(primaryTableName, triggerName);
		System.out.println(triggerSql);
		
		baseDao.excuteUpdate(triggerSql,null);
		
		 context.setAttribute("msg", "success");
	     context.forword("/save_result.jsp");
	 }
	 
	 public String createTriggerSql(String tableName, String triggerName){
		 StringBuffer sb = new StringBuffer("CREATE OR REPLACE TRIGGER ").append(triggerName);
		 sb.append(" AFTER DELETE OR INSERT OR UPDATE ON ").append(tableName);
		 sb.append(" FOR EACH ROW DECLARE ").append("  integrity_error exception;errno integer;errmsg char(200);dummy integer; found boolean;");
		 sb.append(" BEGIN CASE WHEN inserting THEN ");
		 sb.append(" insert into DB_TEMP(TABLE_NAME,TABLE_ID,DATA_STATUS,OP_TIME) values('").append(tableName);
		 sb.append("',:NEW.ID,'insert',sysdate);");
		 sb.append(" WHEN updating THEN");
		 sb.append(" insert into DB_TEMP(TABLE_NAME,TABLE_ID,DATA_STATUS,OP_TIME) values('").append(tableName);
		 sb.append("',:NEW.ID,'update',sysdate);");
		 sb.append(" WHEN deleting THEN");
		 sb.append(" insert into DB_TEMP(TABLE_NAME,TABLE_ID,DATA_STATUS,OP_TIME) values('").append(tableName);
		 sb.append("',:NEW.ID,'delete',sysdate);");
		 sb.append(" END CASE; EXCEPTION WHEN integrity_error THEN raise_application_error(errno, errmsg); END;");
		 return sb.toString();
	 }

	 
	 
	 
	 /**
	     *  判断触发器是否已存在
	     * @param context
	     * @throws ServletException
	     * @throws IOException
	     */
	    public void hasTrigger(HTTPContext context) throws ServletException, IOException {

	        String triggerName = context.getParameter("triggerName");
	        String errInfo = "success";
	        //
	        HashMap<String, String> map = new HashMap<String, String>();
	        SEntity sEntity = new SEntity("db_trigger");
	        sEntity.addCondition("trigger_name=?", triggerName.toUpperCase());
	        baseDao.load(sEntity);
	        if (!sEntity.isEmpty()) {
	            errInfo = "error";
	        }
	        map.put("result", errInfo);
	        context.sendAsJson(JSONObject.toJSONString(map));
	    }
}
