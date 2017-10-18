package com.dc.platform;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.dc.platform.dbinfo.DatabaseManager;

/**
 * @company 东昌数码
 * <p>
 * Copyright: Copyright (c) 2012
 * </p>
 * @author sslin
 * @date 2013-4-26 下午2:02:00
 * @description : 公用servlet  <br>
 * 		/s?opr=Base4DBService&ac=add
 */
public class Base4DBServlet extends HttpServlet {

    private static final long serialVersionUID = 3318641197610971731L;

    private static Logger     log              = Logger.getLogger(Base4DBServlet.class);

    private DatabaseManager   databaseManager;


    @Override
    public void init() throws ServletException {

        super.init();
        Context.springContext = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        databaseManager = (DatabaseManager) Context.getBean("databaseManager");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 首先通过直接获取操作service类的SPRING ID
        String operaterStr = request.getParameter("opr");
        // 首先通过直接获取操作方法
        String action = request.getParameter("ac");
        // 如果地址中没有操作service类的SPRING ID则通过模块信息获取系统配置的操作类
        HTTPContext httpContext = new HTTPContext(this, request, response, databaseManager);
        // 如果操作service类的SPRING ID为空，则默认用基准父类
        if (operaterStr == null || "".equals(operaterStr)) {
            operaterStr = "base4DBService";
        }
        // 从spring中获取对应的service
        Base4DBService service = null;
        try {
            service = (Base4DBService) Context.getBean(operaterStr);
        } catch (Exception e) {
            service = (Base4DBService) Context.getBean("base4DBService");
        }
        // 如果方法名为空默认调用查询方法，其他用反射调用
        Method method;
        try {
            try {
                method = service.getClass().getDeclaredMethod(action, HTTPContext.class);
            } catch (NoSuchMethodException e) {
                method = service.getClass().getMethod(action, HTTPContext.class);
            }
            method.invoke(service, httpContext);
        } catch (SecurityException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (InvocationTargetException e) {
            log.error(e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
}