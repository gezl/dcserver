package com.dc.platform.permission;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-5-31 下午1:47:44
 * @description :
 */
@Service("permissionContext")
@Scope("prototype")
public class PermissionContext {
	/**
	 * 用户信息
	 */
	private int userId;
	/**
	 * 系统ID
	 */
	private int systemId = 0;
	/**
	 * 当前系统
	 */
	private System system;

	/**
	 * <moduleSN,permission>
	 */
	private Map<String, Integer> permissions;
	
	/**
	 * 权限业务操作
	 */
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getSystemId() {
		return systemId;
	}
	public void setSystemId(int systemId) {
		this.systemId = systemId;
	}
	public System getSystem() {
		return system;
	}
	public void setSystem(System system) {
		this.system = system;
	}	
	/**
	 * 获取用户的所有权限
	 * @return
	 */
	public Map<String, Integer> getPermissions() {
		return permissions;
	}
	/**
	 * 添加用户权限
	 * @param permissions
	 */
	public void setPermissions(Map<String, Integer> permissions) {
		this.permissions = permissions;
	}
	/**
	 * 判断当前用户名对当前系统模块的操作是否有权限
	 * @param systemId 系统ID
	 * @param moduleId 模块ID
	 * @param actionNo 操作序号
	 * @return 是否有权限
	 */
	public boolean hasPermission(String moduleSN,String actionSN){
		//查看当前有权限的模块中是否包含当前模块，如果不包含代表没有权限
		Integer aclState = permissions.get(moduleSN);
		if(aclState == null){
			return false;
		}
		//从当前模块中查找当前操作，如果不包含当代表没有权限
		Module module = system.getModule(moduleSN);
		Action action = module.getAction(actionSN);
		if(action == null) {
			return false;
		}
		//判断当前用户名是否对当前模块的当前操作有权限，用int的二进制表示权限
		int acNo = action.getAcNo();
		int temp = 1;
		temp = temp << acNo;
		temp = aclState & temp;
		if(temp != 0){
			return true;
		}
		return false;
	}
}