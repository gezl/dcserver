package com.dc.platform;

import org.springframework.web.context.WebApplicationContext;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-12-4 上午11:10:06
 * @description : 全局变量容器
 */
public class Context {
	/**
	 * Spring Context(WebApplicationContext)
	 */
	public static WebApplicationContext springContext;
	/**
	 * 获取ioc对象
	 * @param key
	 * @return
	 */
	public static Object getBean(String beanName){
		return springContext.getBean(beanName);
	}
}


