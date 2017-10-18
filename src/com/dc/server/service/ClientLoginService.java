package com.dc.server.service;

import com.dc.platform.Base4DBService;

public interface ClientLoginService {
	
	/**
	 * 
	* @Title: login
	* @Description: 客户端请求服务,进行登录
	* @param baseService
	* @param userName
	* @param passWord
	* @return
	 */
	String login(String userName,String passWord);
	
	
	/**
	 * 
	* @Title: login
	* @Description: 用户退出,下线客户端
	* @param baseService
	* @param userName
	* @param passWord
	* @return
	 */
	void logout(Base4DBService baseService,String userName);

}
