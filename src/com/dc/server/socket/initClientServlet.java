package com.dc.server.socket;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dc.platform.Base4DBService;
import com.dc.platform.Context;
import com.dc.server.key.TypeEnum.LineType;

public class initClientServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	@Override
	    public void init() throws ServletException {

	        super.init();
	        Context.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	         Base4DBService baseService = (Base4DBService) Context.getBean("base4DBService");
	         String sql = " update admin_user set online_status=?  where usertype='user'";
    		List<Object> parameters = new ArrayList<Object>();
    		parameters.add(LineType.OFF.getKey());
    		baseService.getBaseDao().excuteUpdate(sql, parameters);
	    }
}
