package com.dc.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.key.TypeEnum.DBType;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.key.TypeEnum.UserType;
import com.dc.server.util.PageUtil;

@Service("cfdUserService")
public class UserService extends Base4DBService {

    /**
     * 获取系统用户列表 load用法
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void getUList(HTTPContext context) throws ServletException, IOException {

        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        SEntityList sEntityList = new SEntityList("admin_user");
        sEntityList.setPageNum(pageNum);
        sEntityList.setPageSize(pageSize);
        sEntityList.addCondition("usertype =?", UserType.USER.getKey());
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
            sEntityList.addCondition("account like '%" + keyword + "%'");
        }
        baseDao.load(sEntityList);
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/user/user_list.jsp");
    }

    /**
     * 获取系统用户列表 Search用法
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void getUListSearch(HTTPContext context) throws ServletException, IOException {

        // 获取参数
        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        keyword = StringUtils.trimToEmpty(keyword);
        StringBuffer sb = new StringBuffer(" select * from admin_user where usertype ='user' ");
        if(StringUtils.isNotBlank(keyword))
        {
        	sb.append("and account like '%" + keyword + "%'");
        }
        // 计算分页
        String sqlCount = "select count(*) as totalcount  from ("+sb.toString()+") ";
        int totalCount = baseDao.search(sqlCount, null).get(0).getValueAsInteger("totalcount");
        int totalPage = Double.valueOf(Math.ceil(totalCount / new Double(pageSize))).intValue();
        pageNum = PageUtil.getPageNum(pageNum, totalPage);
        // 结果集
        SEntityList sEntityList = baseDao.search(sb.toString(), null, pageNum, pageSize);
        //
        sEntityList.setPageNum(pageNum);
        sEntityList.setTotalCount(totalCount);
        sEntityList.setTotalPage(totalPage);
        sEntityList.setPageSize(pageSize);
        //
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/user/user_list.jsp");
    }

    /**
     *  判断用户名是否存在
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void hasU(HTTPContext context) throws ServletException, IOException {

        String account = context.getParameter("loginname");
        String errInfo = "success";
        //
        HashMap<String, String> map = new HashMap<String, String>();
        SEntity sEntity = new SEntity("admin_user");
        sEntity.addCondition("account=?", account);
        baseDao.load(sEntity);
        if (!sEntity.isEmpty()) {
            errInfo = "error";
        }
        map.put("result", errInfo);
        context.sendAsJson(JSONObject.toJSONString(map));
    }

    /**
     * 添加用户页面
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void toAddCus(HTTPContext context) throws ServletException, IOException {

        context.setAttribute("method", "add");
        context.forword("/admin/user/user_edit.jsp");
    }

    /**
     * 添加/修改/删除 ---用户
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void editCus(HTTPContext context) throws ServletException, IOException {

        String method = context.getParameter("method");
        //
        String user_id = context.getParameter("id");
        String account = context.getParameter("loginname");
        String password = context.getParameter("password");
        String linkman = context.getParameter("linkman");
        String phone = context.getParameter("phone");
        String remarks = context.getParameter("remarks");
        String hz_id = context.getParameter("hz_id");
        String hd_id = context.getParameter("hd_id");
        String lx_id = context.getParameter("lx_id");
        String small_lx = context.getParameter("s_name");
        String hz_name = context.getParameter("hz_name");
        String hd_name = context.getParameter("hd_name");
        String lx_name = context.getParameter("lx_name");
        //
        SEntity entity = new SEntity("admin_user");
        entity.setValue("account", account);
        entity.setValue("linkman", linkman);
        entity.setValue("phone", phone);
        entity.setValue("remarks", remarks);
        entity.setValue("usertype", UserType.USER.getKey());
        entity.setValue("hz_id", hz_id);
        entity.setValue("hd_id", hd_id);
        entity.setValue("lx_id", lx_id);
        entity.setValue("small_lx", small_lx);
        entity.setValue("hz_name", hz_name);
        entity.setValue("hd_name", hd_name);
        entity.setValue("lx_name", lx_name);
        String msg = "success";
        if ("add".equals(method)) {
        	entity.setValue("password", new SimpleHash("SHA-1", account, password).toBase64());
            int id = baseDao.save(entity);
            if (id == -1) {
                msg = "error";
            }
        } else if ("edit".equals(method)) {
        	entity.setValue("password", new SimpleHash("SHA-1", account, password).toBase64());
            entity.addCondition("id=?", user_id);
            baseDao.update(entity);
        } else if ("delete".equals(method)) {
            entity.setValue("id", user_id);
            baseDao.delete(entity);
        }
        context.setAttribute("msg", msg);
        context.forword("/save_result.jsp");
    }

    /**
     * 用户编辑页面
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void toEditCus(HTTPContext context) throws ServletException, IOException {

        String id = context.getParameter("user_id");
        SEntity entity = new SEntity("admin_user");
        entity.addCondition("id=?", id);
        baseDao.load(entity);
        context.setAttribute("method", "edit");
        context.setAttribute("cus", entity);
        context.forword("/admin/user/user_edit.jsp");
    }
    
    
    /**
     * 设置数据库页面
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void toEditDB(HTTPContext context) throws ServletException, IOException {

        String id = context.getParameter("user_id");
        SEntity entity = new SEntity("admin_user");
        entity.addCondition("id=?", id);
        baseDao.load(entity);
        context.setAttribute("cus", entity);
        context.setAttribute("DBType", DBType.values());
        context.forword("/admin/user/user_editDB.jsp");
    }
    
    /**
     * 设置数据库
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void editDB(HTTPContext context) throws ServletException, IOException {

        String id = context.getParameter("user_id");
        String db_type = context.getParameter("db_type");
        
        String sql = "update admin_user set db_type=?  where id=?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(db_type);
        parameters.add(id);
        baseDao.update(sql, parameters);
        
        context.setAttribute("msg", "success");
        context.forword("/save_result.jsp");
    }
    

    /**
     * 启用/禁用用户
     * @param context
     * @throws ServletException
     * @throws IOException
     */
    public void enableCus(HTTPContext context) throws ServletException, IOException {

        String id = context.getParameter("id");
        String enable = context.getParameter("enable");
        
        String sql = "update admin_user set enable=?  where id=?";
        List<Object> parameters = new ArrayList<Object>();
        parameters.add(enable.equals(String.valueOf(EnabledType.ENABLE.getKey())) ? EnabledType.DISABLED.getKey() : EnabledType.ENABLE.getKey());
        parameters.add(id);
        baseDao.update(sql, parameters);
        
        context.setAttribute("msg", "success");
        context.forword("/save_result.jsp");
    }
    
    /**
     * 
    * @Title: getAccountList
    * @Description: 获取用户列表
    * @author gezhiling
    * @date  2017-7-5 上午10:20:23
    * @param context
    * @throws ServletException
    * @throws IOException
     */
    public void getAccountList(HTTPContext context) throws ServletException, IOException {

        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        SEntityList sEntityList = new SEntityList("admin_user");
        sEntityList.setPageNum(pageNum);
        sEntityList.setPageSize(pageSize);
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
            sEntityList.addCondition("account like '%" + keyword + "%'");
        }
        baseDao.load(sEntityList);
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/search/user_list.jsp");
    }
    public static void main(String[] args) {
		System.out.println(new SimpleHash("SHA-1", "zw_admin", "zw123").toBase64());
		System.out.println(new SimpleHash("SHA-1", "rf_admin", "rf111111").toBase64());
	}
}

