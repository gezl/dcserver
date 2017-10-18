package com.dc.platform;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dc.platform.permission.ThreadLocalSession;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * @author sslin
 * @date 2013-6-9 下午5:00:26
 * @description : 登录信息验证
 */
public class LoginFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	private String[] accessPaths;
	
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)req;
		String lServletPath = request.getServletPath().toLowerCase();
		ThreadLocalSession.setUser((SEntity)request.getSession(true).getAttribute("login"));		//对静态文件放行
		if(lServletPath.endsWith(".jsp")||
				lServletPath.endsWith(".js")||
				lServletPath.endsWith(".css")||
				lServletPath.endsWith(".html")||
				lServletPath.endsWith(".xml")||
				lServletPath.endsWith(".htm")||
				lServletPath.endsWith(".png")||
				lServletPath.endsWith(".jpg")||
				lServletPath.endsWith(".jpeg")||
				lServletPath.endsWith(".swf")||
				lServletPath.endsWith(".gif")
				){
			chain.doFilter(req, resp);
			return;
		}
		//对系统配置放行请求放行
		for (int i = 0; i < accessPaths.length; i++) {
			if(lServletPath.equalsIgnoreCase(accessPaths[i])){
				chain.doFilter(req, resp);
				return;
			}
		}
		//对已登录访问放行
		Object login = request.getSession(true).getAttribute("login");
		//如果已登录
		if(login != null){
			chain.doFilter(req, resp);
		}else{
			HttpServletResponse response = (HttpServletResponse)resp;
			String path = request.getContextPath();
			 response.sendRedirect(path+"/login.jsp");
		}
	}
	//加载放行的请求地址
	@Override
	public void init(FilterConfig config) throws ServletException {
		accessPaths = config.getInitParameter("accesspaths").split(",");
	}
}


