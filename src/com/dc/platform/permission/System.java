package com.dc.platform.permission;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-5-28 下午3:58:27
 * @description : 系统
 */
public class System {
	/**
	 * 唯一标示
	 */
	private int id;
	/**
	 * 系统名称
	 */
	private String systemName;
	/**
	 * 系统访问路径
	 */
	private String url;
	/**
	 * 是否启用
	 */
	private boolean inUse;
	/**
	 * 系统模块
	 */
	private Map<String, Module> modules = new LinkedHashMap<String, Module>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSystemName() {
		return systemName;
	}
	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public boolean isInUse() {
		return inUse;
	}
	public void setInUse(boolean inUse) {
		this.inUse = inUse;
	}
	public Map<String, Module> getModules() {
		return modules;
	}
	public void setModules(Map<String, Module> modules) {
		this.modules = modules;
	}
	public Module getModule(String moduleSN) {
		return modules.get(moduleSN);
	}
	public void addModule(String moduleSN,Module module) {
		modules.put(moduleSN,module);
	}
	public System(int id, String systemName, String url, boolean inUse) {
		super();
		this.id = id;
		this.systemName = systemName;
		this.url = url;
		this.inUse = inUse;
		
	}
	public System() {
		super();
	}
	
	
}