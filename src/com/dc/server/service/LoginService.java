package com.dc.server.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.platform.SEntityList.Sort_type;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.key.TypeEnum.UserType;

@Service("cfdLoginService")
public class LoginService extends Base4DBService {

    /**
     * 登录
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void login4user(HTTPContext context) throws ServletException, IOException {

        String account = context.getParameter("loginname");
        String password = context.getParameter("password");
        String errInfo = "error";
        HashMap<String, String> map = new HashMap<String, String>();
        SEntity sEntity = new SEntity("admin_user");
        sEntity.addCondition("usertype=?", UserType.ADMIN.getKey());
        sEntity.addCondition("account=?", account);
        sEntity.addCondition("password=?", new SimpleHash("SHA-1", account, password).toBase64());
        baseDao.load(sEntity);
        if (!sEntity.isEmpty()) {
            errInfo = "success";
            String disable = String.valueOf(EnabledType.DISABLED.getKey());
            context.getSession().setAttribute("login", sEntity);
            if (disable.equals(sEntity.getValueAsString("enable"))) errInfo = "usererror";
        }
        map.put("result", errInfo);
        context.sendAsJson(JSONObject.toJSONString(map));
    }
    
    /**
     * 
    * @Title: login4Index
    * @Description: 登陆后首页加载方法
    * @author gezhiling
    * @date  2017-6-27 下午2:07:01
    * @param context
    * @throws ServletException
    * @throws IOException
     */
    public void login4Index(HTTPContext context) throws ServletException, IOException {
    	
    	
    	Map<SEntity, SEntityList> listAllMenus = new LinkedHashMap<SEntity, SEntityList>();
    	SEntityList listAllParentMenu = new SEntityList("db_menu");
    	listAllParentMenu.addCondition("parent_id = ?", "0");
    	listAllParentMenu.addSortCondition("menu_oreder",Sort_type.SORT_UP);
    	baseDao.load(listAllParentMenu);
    	for (SEntity sEntity : listAllParentMenu) {
    		SEntityList listSubMenuByParentId = new SEntityList("db_menu");
    		listSubMenuByParentId.addCondition("parent_id = ? ", sEntity.getValueAsString("menu_id"));
    		listSubMenuByParentId.addSortCondition("menu_oreder",Sort_type.SORT_UP);
    		baseDao.load(listSubMenuByParentId);
    		listAllMenus.put(sEntity, listSubMenuByParentId);
		}
    	context.setAttribute("listAllMenus", listAllMenus);
    	context.forword("/index.jsp");
    }

    /**
     * 通过session获取用户信息
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void getUname(HTTPContext context) throws ServletException, IOException {

        SEntity sEntity = (SEntity) context.getSession().getAttribute("login");
        HashMap<String, String> map = new HashMap<String, String>();
        if (sEntity == null) {
            map.put("result", "logout");
            context.sendAsJson(JSONObject.toJSONString(map));
        } else {
            map.put("result", sEntity.getValueAsString("account"));
            context.sendAsJson(JSONObject.toJSONString(map));
        }
    }

    /**
     * 退出登录
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void logout(HTTPContext context) throws ServletException, IOException {

        context.getSession().removeAttribute("login");
        context.forword("/login.jsp");
    }
}
