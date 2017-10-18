package com.dc.platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.dc.platform.dbinfo.Column;
import com.dc.platform.dbinfo.DatabaseManager;
import com.dc.platform.dbinfo.Table;
import com.dc.platform.permission.Module;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-5-17 上午11:46:21
 * @description : HTTP请求上下文
 */
public class HTTPContext {
	/**
	 * 请求的HttpServlet
	 */
	private HttpServlet httpServlet;
	/**
	 * 请求的HttpServletRequest
	 */
	public HttpServletRequest request;
	/**
	 * 请求的HttpServletResponse
	 */
	public HttpServletResponse response;
	/**
	 * 数据表管理对象
	 */
	public DatabaseManager databaseManager;
	/**
	 * 请求的模块
	 */
	private Module moduel;
	
	private SEntity user;
	/**
	 * 表单数据
	 */
	Map<String, List<SEntity>> entities = new HashMap<String, List<SEntity>>();
	
	protected HTTPContext(HttpServlet httpServlet,HttpServletRequest request,HttpServletResponse response,DatabaseManager databaseManager) {
		this.httpServlet = httpServlet;
		this.request = request;
		this.response = response;
		this.databaseManager = databaseManager;
		this.user = (SEntity)getFromSession("login");
	}
	//添加构造
	public HTTPContext(HttpServletRequest request,HttpServletResponse response){
		this.request=request;
		this.response= response;
	}
	/**
	 * 跳转到指定地址
	 * @param url
	 * @throws IOException 
	 */
	public void sendRedirect(String url) throws IOException {
		System.out.println(url);
		response.sendRedirect(url);
	}
	/**
	 * 转向到
	 * @param url
	 * @throws ServletException
	 * @throws IOException
	 */
	public void forword(String url) throws ServletException, IOException {
		System.out.println(url);
		httpServlet.getServletContext().getRequestDispatcher(url).forward(request, response);
	}
	/**
	 * 获取传回的参数
	 * @param paraName
	 * @return
	 */
	public String getParameter(String paraName){
		return request.getParameter(paraName);
	}
	/**
	 * 获取传递的参数
	 * @param attrName
	 * @return
	 */
	public Object getAttribute(String attrName){
		return request.getAttribute(attrName);
	}
	/**
	 * 设置传递参数
	 * @param attrName
	 * @param value
	 */
	public void setAttribute(String attrName,Object value){
		request.setAttribute(attrName, value);
	}
	/**
	 * 获取session对象
	 * @return
	 */
	public HttpSession getSession(){
		return request.getSession(true);
	}
	/**
	 * 向session中添加内容
	 * @param attrName
	 * @param value
	 */
	public void addToSession(String attrName,Object value){
		getSession().setAttribute(attrName, value);
	}
	/**
	 * 从session中获取内容
	 * @param attrName
	 * @return
	 */
	public Object getFromSession(String attrName){
		return getSession().getAttribute(attrName);
	}
	/**
	 * 从session中删除内容
	 * @param attrName
	 */
	public void removeFromSession(String attrName){
		getSession().removeAttribute(attrName);
	}
	/**
	 * 从全局的servletContext中获取内容
	 * @param attrName
	 * @return
	 */
	public Object getFromContext(String attrName){
		return httpServlet.getServletContext().getAttribute(attrName);
	}
	/**
	 * 向全局的servletContext中添加内容从
	 * @param attrName
	 * @param value
	 */
	public void addToContext(String attrName,Object value){
		httpServlet.getServletContext().setAttribute(attrName, value);
	}
	/**
	 * 从全局的servletContext中删除内容
	 * @param attrName
	 */
	public void removeFromContext(String attrName){
		httpServlet.getServletContext().removeAttribute(attrName);
	}
	/**
	 * 获取当前登录用户
	 * @return
	 */
	public SEntity getUser() {
		return user;
	}	
	
	/**
	 * 在按要求设计的页面提交表单后，自动生成数据对象
	 * @param table
	 * @param databaseName
	 * @return
	 */
	public List<SEntity> createSEntityByRequest(Table table,String... databaseName){
		if(entities.size() == 0){
			loadSEntityFromRequest();
		}
		List<SEntity> sEntities = entities.get(table.getDatabase().getSchema()+"_"+table.getTableName());
		return sEntities==null?new ArrayList<SEntity>():sEntities;
	}
	/**
	 * 外键列，一对多同时提交时自动获取主表列值
	 */
	private Map<String, Map<String, String>> fkTables = new HashMap<String, Map<String, String>>();
	
	public Map<String, Map<String, String>> getFkTables() {
		return fkTables;
	}
	public Map<String, String> getFkTable(String tableName) {
		return fkTables.get(tableName);
	}
	public void setFkTables(Map<String, Map<String, String>> fkTables) {
		this.fkTables = fkTables;
	}
	
	private void loadSEntityFromRequest(){
		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if(!paramName.contains(".")){
				continue;
			}
			String[] paras = paramName.split("\\.");
			
			Table ttable = null;
			Column column = null;
			if (paras.length == 3) {
				ttable = databaseManager.getTable(paras[1].toUpperCase(), paras[0].toUpperCase());
				column = ttable.getColumn(paras[2].toUpperCase());
			}else if(paras.length == 2){
				try{
					ttable = databaseManager.getTable(paras[0].toUpperCase());
					column = ttable.getColumn(paras[1].toUpperCase());
				}catch (Exception e) {
					continue;
				}
			}else{
				continue;
			}
			String[] paramValues = request.getParameterValues(paramName);
			List<SEntity> tEntities = entities.get(ttable.getDatabase().getSchema()+"_"+ttable.getTableName());
			if(tEntities == null){
				tEntities = new ArrayList<SEntity>();
				entities.put(ttable.getDatabase().getSchema()+"_"+ttable.getTableName(), tEntities);
			}
			for (int i = 0; i < paramValues.length; i++) {
				SEntity entity = null;
				if(tEntities.size() > i){
					entity = tEntities.get(i);
				}
				if(entity == null){
					entity = new SEntity(ttable.getTableName());
					tEntities.add(i, entity);
				}
				if(column == null && paras[1].toUpperCase().equals("ROWACTION")){
					entity.setRowAction(paramValues[i]);
				}else if(column != null){
					entity.setValue(column.getName(), paramValues[i]);
					if(paramValues[i]!=null && paramValues[i].startsWith("{") && paramValues[i].endsWith("}")){
						Map<String, String> fkColumns = fkTables.get(ttable.getTableName());
						if(fkColumns == null){
							fkColumns = new HashMap<String, String>();
							fkTables.put(ttable.getTableName(), fkColumns);
						}
						fkColumns.put(column.getName(), paramValues[i].substring(1, paramValues[i].length()-1));
					}
				}
			}
		}
	}
	/**
	 * 在按要求设计的页面提交表单后，自动生成数据查询对象
	 */
	public void loadSEntityListFromRequest(){
		@SuppressWarnings("unchecked")
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement();
			if(!paramName.contains(".")){
				continue;
			}
			String[] paras = paramName.split("\\.");
			
			Table ttable = null;
			Column column = null;
			if (paras.length == 3) {
				ttable = databaseManager.getTable(paras[1].toUpperCase(), paras[0].toUpperCase());
				column = ttable.getColumn(paras[2].toUpperCase());
			}else{
				ttable = databaseManager.getTable(paras[0].toUpperCase());
				column = ttable.getColumn(paras[1].toUpperCase());
			}
			String[] paramValues = request.getParameterValues(paramName);
			List<SEntity> tEntities = entities.get(ttable.getDatabase().getSchema()+"_"+ttable.getTableName());
			if(tEntities == null){
				tEntities = new ArrayList<SEntity>();
				entities.put(ttable.getDatabase().getSchema()+"_"+ttable.getTableName(), tEntities);
			}
			for (int i = 0; i < paramValues.length; i++) {
				SEntity entity = null;
				if(tEntities.size() > i){
					entity = tEntities.get(i);
				}
				if(entity == null){
					entity = new SEntity(ttable.getTableName());
					tEntities.add(i, entity);
				}
				entity.setValue(column.getName(), paramValues[i]);
			}
		}
	}
	
	
	public HttpServlet getHttpServlet() {
		return httpServlet;
	}

	public void setHttpServlet(HttpServlet httpServlet) {
		this.httpServlet = httpServlet;
	}
	public Module getModuel() {
		return moduel;
	}
	public void setModuel(Module moduel) {
		this.moduel = moduel;
	}	
	/**
	 * 以HTTP头中标记为JSON格式发送字符串
	 * @param json 发送的字符串
	 * @param encoding 编码
	 */
	public void sendAsJson(String json,String... encoding) {
		String charset = "UTF-8";
		if(encoding.length==1){
			charset = encoding[0];
		}
		response.setContentType("text/json; charset="+charset);
		response.setHeader("Cache-control", "no-cache");
		response.setCharacterEncoding(charset);
		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			throw new RuntimeException("response write json error.info: " + e.getMessage());
		}
	}
	public void sendAsString(String s,String... encoding) {
		String charset = "UTF-8";
		if(encoding.length==1){
			charset = encoding[0];
		}
		response.setContentType("text/html; charset="+charset);
		response.setHeader("Cache-control", "no-cache");
		response.setCharacterEncoding(charset);
		try {
			response.getWriter().write(s);
		} catch (IOException e) {
			throw new RuntimeException("response write json error.info: " + e.getMessage());
		}
	}	
	/**
	 * 处理完成后，提示操作信息并重新加载页面
	 * @param msg 提示信息
	 * @param url 重新加载地址
	 */
	public void loadPage(String msg,String url) {
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode", 200);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "");
			resultJson.put("callbackType", "forward");
			resultJson.put("forwardUrl", url);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	/**
	 * 处理完成后，提示操作信息并重新加载页面
	 * @param msg 提示信息
	 * @param url 重新加载地址
	 */
	public void loadPage(String msg) {
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode", 200);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "");
			resultJson.put("callbackType", "");
			resultJson.put("forwardUrl", "");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	/**
	 * 数据处理成功返回
	 * @param msg
	 */
	public void replyTreeIndex(String msg,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "jbsxBox");
			resultJson.put("callbackType", "closeCurrent");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}	
	/**
	 * 数据处理成功返回
	 * @param msg
	 */
	public void reply(String msg,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "");
			resultJson.put("callbackType", "closeCurrent");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	
	/**
	 * 数据处理成功返回并关闭刷新指定rel界面
	 * @param msg
	 */
	public void reply(String msg,String rel,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", rel==null?"":rel);
			resultJson.put("callbackType", "closeCurrent");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	/**
	 * 数据处理成功返回
	 * @param msg
	 */
	public void replyIframe(String msg,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "");
			resultJson.put("callbackType", "closeCurrent");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsString(resultJson.toString());
	}
	/**
	 * 带回页面数据处理成功返回
	 * @param msg
	 */
	public void replyLookUpAdd(String msg,String rel,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", rel==null?"":rel);
			resultJson.put("callbackType",  rel == null?"closeCurrent":"");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	/**
	 * 数据处理成功返回
	 * @param msg
	 */
	public void replyDialogSubmit(String msg,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", "");
			resultJson.put("rel", "main");
			resultJson.put("callbackType", "closeCurrent");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}	
	/**
	 * 登陆超时
	 */
	public void loginTimeout() {
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",301);
			resultJson.put("message", "登陆超时");
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
	
	/**
	 * 数据处理成功返回
	 * @param msg
	 */
	/**
	 * @param msg 页面提示信息
	 * @param navTabId navTabId
	 * @param rel  页面引用ID
	 * @param callbackType 回调参数
	 * @param statusCode ajax状态字段
	 */
	public void reply4all(String msg,String navTabId,String rel,String callbackType,int... statusCode){
		JSONObject resultJson = new JSONObject();
		try {
			resultJson.put("statusCode",statusCode.length == 0?200:statusCode[0]);
			resultJson.put("message", msg);
			resultJson.put("navTabId", navTabId==null?"":navTabId);
			resultJson.put("rel", rel==null?"":rel);
			resultJson.put("callbackType",  callbackType == null?"":callbackType);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		sendAsJson(resultJson.toString());
	}
}


