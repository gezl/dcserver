package com.dc.platform;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.dbinfo.DatabaseManager;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.key.TypeEnum.UserType;


public class LoginServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SEntity user =(SEntity)request.getSession().getAttribute("login");
		if(user!=null){
			response.sendRedirect(request.getContextPath() + "/login.jsp");
		}else{
		Context.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		DatabaseManager    databaseManager = (DatabaseManager) Context.getBean("databaseManager");
		Base4DBService	baseService = (Base4DBService) Context.getBean("base4DBService");
		 HTTPContext context = new HTTPContext(this, request, response, databaseManager);
		String account = context.getParameter("loginname");
        String password = context.getParameter("password");
        String errInfo = "error";
        HashMap<String, String> map = new HashMap<String, String>();
        SEntity sEntity = new SEntity("admin_user");
        sEntity.addCondition("usertype=?", UserType.ADMIN.getKey());
        sEntity.addCondition("account=?", account);
        sEntity.addCondition("password=?", new SimpleHash("SHA-1", account, password).toBase64());
        baseService.getBaseDao().load(sEntity);
        if (!sEntity.isEmpty()) {
            errInfo = "success";
            String disable = String.valueOf(EnabledType.DISABLED.getKey());
            context.getSession().setAttribute("login", sEntity);
            if (disable.equals(sEntity.getValueAsString("enable"))) errInfo = "usererror";
        }
        map.put("result", errInfo);
        context.sendAsJson(JSONObject.toJSONString(map));
		}
		
	}

}
