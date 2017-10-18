package com.dc.platform.permission;

import java.util.Map;
import java.util.TreeMap;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-5-28 下午3:31:29
 * @description : 模块
 */
public class Module {
	/**
	 * 唯一标示
	 */
	private String sn;
	/**
	 * 序号
	 */
	private int orderNo;
	/**
	 * 系统ID
	 */
	private int systemId;	
	/**
	 * 模块名称
	 */
	private String moduleName;
	/**
	 * 对应的操作类的springName
	 */
	private String beanName;
	/**
	 * 父菜单
	 */
	private String parentSN;
	/**
	 * 子菜单
	 */
	private String[] childrenSNs;
	/**
	 * 默认操作
	 */
	private String defaultAction;
	/**
	 * 是否作为菜单显示
	 */
	private boolean isMenu;
	/**
	 * 默认页面地址
	 */
	private String indexUrl;
	/**
	 * 查询视图
	 */
	private String viewName;
	/**
	 * 数据表
	 */
	private String tableName;
	/**
	 * 通用查询的SQL
	 */
	private String searchSQL;
	/**
	 * 通用查询的SQL
	 */
	private String flowId;
	/**
	 * 所有操作
	 */
	private Map<String,Action> actions = new TreeMap<String,Action>();
	
	public String getIndexUrl() {
		return indexUrl;
	}
	public void setIndexUrl(String indexUrl) {
		this.indexUrl = indexUrl;
	}
	public boolean isMenu() {
		return isMenu;
	}
	public void setMenu(boolean isMenu) {
		this.isMenu = isMenu;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public String getDefaultAction() {
		return defaultAction;
	}
	public void setDefaultAction(String defaultAction) {
		this.defaultAction = defaultAction;
	}
	public String getSearchSQL() {
		return searchSQL;
	}
	public void setSearchSQL(String searchSQL) {
		this.searchSQL = searchSQL;
	}
	public Map<String, Action> getActions() {
		return actions;
	}
	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}
	public Action getAction(String actionSN) {
		return actions.get(actionSN);
	}
	public String getParentSN() {
		return parentSN;
	}
	public void setParentSN(String parentSN) {
		this.parentSN = parentSN;
	}
	public String[] getChildrenSNs() {
		return childrenSNs;
	}
	public void setChildrenSNs(String[] childrenSNs) {
		this.childrenSNs = childrenSNs;
	}
	public int getSystemId() {
		return systemId;
	}
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}

	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getFlowId() {
		return flowId;
	}
	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}	
}