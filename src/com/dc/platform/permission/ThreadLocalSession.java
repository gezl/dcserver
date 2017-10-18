package com.dc.platform.permission;

import java.util.HashMap;
import java.util.Map;

import com.dc.platform.SEntity;


public class ThreadLocalSession {

	private static ThreadLocal<Map<String,Object>> threadLocals = new ThreadLocal<Map<String,Object>>();
	/**
	 * 读取线程独享值
	 * @param attribute
	 * @return
	 */
	public static Object getValue(String attribute){
		Map<String,Object> map = threadLocals.get();
		if(map==null){
			return null;
		}
        return threadLocals.get().get(attribute);
	}
	/**
	 * 存储线程独享值
	 * @param attribute
	 * @param value
	 */
	public static void setValue(String attribute,Object value){
		Map<String,Object> map = threadLocals.get();
		if(map==null){
			map = new HashMap<String,Object>();
			threadLocals.set(map);
		}
		map.put(attribute, value);
	}
	/**
	 * 获取用户
	 * @return
	 */
	public static SEntity getUser() {
		SEntity user =   (SEntity)getValue("user");
		if(user == null){
			user = new SEntity("admin_user");
			user.setValue("account", "系统");
		}
        return (SEntity)getValue("user"); 
    }
	/**
	 * 设置用户
	 * @param user
	 */
	public static void setUser(SEntity user) {
		setValue("user", user);
    }
}
