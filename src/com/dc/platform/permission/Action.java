package com.dc.platform.permission;
/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-5-28 下午3:32:08
 * @description : 操作
 */
public class Action {
	/**
	 * 唯一标示
	 */
	private String sn;
	/**
	 * 序号
	 */
	private int orderNo;
	/**
	 * 操作编号
	 */
	private int acNo;
	/**
	 * 操作名称
	 */
	private String actionName;
	/**
	 * 转向地址
	 */
	private String urlString;
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getAcNo() {
		return acNo;
	}
	public void setAcNo(int acNo) {
		this.acNo = acNo;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public String getUrlString() {
		return urlString;
	}
	public void setUrlString(String urlString) {
		this.urlString = urlString;
	}
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
}


