package com.dc.platform.permission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-6-17 下午4:20:58
 * @description : 用户
 */
public class User {

	/**
	 * 用户id
	 */
	private int id;
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 用户名
	 */
	private String userName;
	
	/**
	 * 密码
	 */
	private String password;
	
	/**
	 * 部门id
	 *
	 */
	private int unitId;
	
	/**
	 * 部门名称
	 */
	private String unitName;
	
	private String phoneTel;
	
	public String getPhoneTel() {
		return phoneTel;
	}

	public void setPhoneTel(String phoneTel) {
		this.phoneTel = phoneTel;
	}

	/**
	 * 数据权限编码
	 * 结构：XX-XX-XX-XX-XX-XXXXXX{公司-部门-部门-部门-部门-人员}
	 */
	private long code;
	
	
	/**
	 * 用户有权限的系统ID
	 */
	private List<Integer> systemIds = new ArrayList<Integer>();
	
	/**
	 * 模块对应的操作权值
	 */
	private Map<String, Integer> modulePermissions = new HashMap<String, Integer>();

	/**
	 * 模块对应的数据权限
	 * 一个模块对应多个权限
	 */
	private Map<String,String> dataPermissions = new HashMap<String,String>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public List<Integer> getSystemIds() {
		return systemIds;
	}

	public void setSystemIds(List<Integer> systemIds) {
		this.systemIds = systemIds;
	}
	
	public Map<String, Integer> getModulePermissions() {
		return modulePermissions;
	}

	public void setModulePermissions(Map<String, Integer> modulePermissions) {
		this.modulePermissions = modulePermissions;
	}
	
	
	public Map<String, String> getDataPermissions() {
		return dataPermissions;
	}

	/**
	 * 获取当前模块的权限值
	 * @param moduleSN
	 * @return
	 */
	public String getDataPermission(String moduleSN) {
		return this.dataPermissions.get(moduleSN);
	}
	/**
	 * 将数据权限添加大session中的User对象中
	 * 1.过滤掉重复的数据权限
	 * 2.User对象中的某一个模块的数据权限为多个时将数据权限中为0的去掉
	 * 3.防止在查询时添加为0的通配查询条见
	 * @param moduleSN
	 * @param dataPermission
	 */
	public void setDataPermissions(String moduleSN,String dataPermission) {
		
		this.dataPermissions.put(moduleSN, dataPermission);
		//查看dataPermissions是否有该模块
		/*if(this.dataPermissions.containsKey(moduleSN)){
			//将模块的数据权限的集合取出来
			List<Long> dataPermissionForSN = this.dataPermissions.get(moduleSN);
			//数据权限为一对多
			if(dataPermission.contains(",")){
				String dataPermissionForSNS[] = dataPermission.split(",");
				for (String dataPermissionForSNValue : dataPermissionForSNS) {
					//如果该模块包含该数据权限过滤
					if(dataPermissionForSN.contains(Long.valueOf(dataPermissionForSNValue))){
						continue;
					}else{
						//过滤已有数据权限的模块不能再把数据权限为0的加入集合中
						if(Long.valueOf(dataPermissionForSNValue)!=0){
							if(dataPermissionForSN.contains(0L)){
								dataPermissionForSN.remove(0);
							}
							dataPermissionForSN.add(Long.valueOf(dataPermissionForSNValue));
						}
					}
				}
			}else{
				//如果该模块包含该数据权限过滤
				if(!dataPermissionForSN.contains(Long.valueOf(dataPermission)) || Long.valueOf(dataPermission)!=0){
					dataPermissionForSN.add(Long.valueOf(dataPermission));
				}
			}
		}else{
			//数据权限为一对多
			List<Long> dataPermissionForSN = new ArrayList<Long>();
			if(dataPermission.contains(",")){
				String dataPermissionForSNS[] = dataPermission.split(",");
				for (String dataPermissionForSNValue : dataPermissionForSNS) {
					dataPermissionForSN.add(Long.valueOf(dataPermissionForSNValue));
				}
			}else{
				dataPermissionForSN.add(Long.valueOf(dataPermission));
			}
			this.dataPermissions.put(moduleSN,dataPermissionForSN);
		}*/
	}
	
	/**
	 * 整理数据权限中的数据，减少user对象的内存开销，减少查询时条见的数量提高查询效率。
	 * 1.当某一个数据权限为对多个时查看是否有包含情况，例如：权限为100000000012345,1000000000的数据权限
	 * 2.去掉范围小的数据权限保留范围大的数据权限，例如：权限为100000000012345,1000000000的数据权限,
	 * 	 1000000000代表一个部门，100000000012345代表一个人，去掉100000000012345。
	 * 3.查看权限中是否包含自己的权限，不包含将自己的权限添加进去。
	 * @return
	 */
	public void reductionDataPermission(){
		/*
		for (Iterator keys = this.dataPermissions.keySet().iterator(); keys.hasNext();) {
			String key = (String) keys.next();
			List<Long> dataPermissionsForSN = this.getAscValue(dataPermissions.get(key));
			//将大的数据权限保留去掉小的数据权限
			if(dataPermissionsForSN.size()>1){
				for(int i = 0;i<dataPermissionsForSN.size();i++){
					long dataPermissionForSNX = dataPermissionsForSN.get(i);
					for(int j = 0;j<dataPermissionsForSN.size();j++){
						long dataPermissionForSNY = dataPermissionsForSN.get(j);
						if(dataPermissionForSNX == dataPermissionForSNY){
							continue;
						}else if(String.valueOf(dataPermissionForSNY).startsWith(String.valueOf(dataPermissionForSNX))){
							dataPermissionsForSN.remove(j);
							j--;
						}
					}
				}
			}
			//将自己的权限添加权限集合中去
			int u=0;
			for(int i = 0;i<dataPermissionsForSN.size();i++){
				long dataPermissionForSNX = dataPermissionsForSN.get(i);
				if(String.valueOf(this.getCode()).startsWith(String.valueOf(dataPermissionForSNX))){
					u++;
				}
			}
			if(u==0){
				dataPermissionsForSN.add(this.getCode());
			}
		}*/
	}
	
	/**
	 * 将模块数据权限按值从大到小排序
	 * @return
	 */
	public List<Long> getAscValue(List<Long> dataPermissionsForSN){
		Collections.sort(dataPermissionsForSN);
		return dataPermissionsForSN;
		
	}

	/**
	 * 清除上一系统权限
	 */
	public void clearModulePermissions(){
		this.modulePermissions.clear();
		this.dataPermissions.clear();
	}
	/**
	 * 设置权限，如查模块已存在则合并权限
	 * @param moduleSN
	 * @param control
	 */
	public void setModulePermission(String moduleSN, int control){
		Integer o_control = this.modulePermissions.get(moduleSN);
		if(o_control != null){
			control = PermissionUtil.mergePermission(control, o_control);
		}
		this.modulePermissions.put(moduleSN, control);
		
	}
	/**
	 * 获取指定模块的权限值
	 * @param moduleSN
	 * @return
	 */
	public Integer getModulePermission(String moduleSN) {
		return modulePermissions.get(moduleSN);
	}

	public int getUnitId() {
		return unitId;
	}

	public void setUnitId(int unitId) {
		this.unitId = unitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	
}


