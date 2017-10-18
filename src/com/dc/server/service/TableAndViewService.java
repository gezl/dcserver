package com.dc.server.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.util.PageUtil;

@Service("tableAndViewService")
public class TableAndViewService extends Base4DBService {

	/**
	 * 
	 * @Title: listAllTable
	 * @Description: 查询出数据库所有的表
	 * @param context
	 * @throws ServletException
	 * @throws IOException
	 */
	public void listAllTable(HTTPContext context) throws ServletException, IOException {

		
		StringBuffer tab_sql = new StringBuffer(" select * from user_tab_comments ");
		String type = context.getParameter("type");
		type= StringUtils.isBlank(type)?"table":type;
		tab_sql.append(" and table_type ='"+type.toUpperCase()+"' ");
		
		
	    // 获取参数
        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        keyword = StringUtils.trimToEmpty(keyword);
        if(StringUtils.isNotBlank(keyword))
        {
        	tab_sql.append("and table_name like '%" + keyword + "%'");
        }
        //sql
        String sql =tab_sql.toString().replaceFirst("and", "where");
        // 计算分页
        String sqlCount = "select count(*) as totalcount  from ("+sql+") ";
        int totalCount = baseDao.search(sqlCount, null).get(0).getValueAsInteger("totalcount");
        int totalPage = Double.valueOf(Math.ceil(totalCount / new Double(pageSize))).intValue();
        pageNum = PageUtil.getPageNum(pageNum, totalPage);
        // 结果集
        SEntityList sEntityList = baseDao.search(sql, null, pageNum, pageSize);
        //
        sEntityList.setPageNum(pageNum);
        sEntityList.setTotalCount(totalCount);
        sEntityList.setTotalPage(totalPage);
        sEntityList.setPageSize(pageSize);
        context.setAttribute("type", type.toLowerCase());
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/search/tv_list.jsp");
	}
	
	/**
	* @Title: listAllTableConfig
	* @Description: 查询出所有配置的表
	* @author gezhiling
	* @date  2017-6-28 下午5:02:04
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
	public void listAllTableConfig(HTTPContext context) throws ServletException, IOException {
		int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        String type = StringUtils.isBlank(context.getParameter("type"))?"table":context.getParameter("type");
        SEntityList sEntityList = new SEntityList("db_view");
        sEntityList.setPageNum(pageNum);
        sEntityList.setPageSize(pageSize);
        sEntityList.addCondition("type = ?", type.toUpperCase());
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
            sEntityList.addCondition("name like '%" + keyword + "%'");
        }
        baseDao.load(sEntityList);
        context.setAttribute("type", type);
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/viewAndTable/list.jsp");
	}
	
	
	/**
	 * 
	* @Title: editTableConfig
	* @Description: 把需要用的数据结构进行存储
	* @author gezhiling
	* @date  2017-6-29 下午2:34:13
	* @param context
	* @throws ServletException
	* @throws IOException
	 */
    public void	 editTableConfig(HTTPContext context) throws ServletException, IOException {
    	String table_name =context.getParameter("table_name");
    	String table_type =context.getParameter("table_type");
    	String comments =context.getParameter("comments");
    	SEntity dbView = new SEntity("db_view");
    	if(table_type.equals("VIEW")){
    		List<Object> parameters = new ArrayList<Object>();
    		parameters.add(table_name);
    		String sql =" select text from user_views  where view_name = ?";
    		SEntity sEntity = baseDao.search(sql, parameters).get(0);
    		String view_sql = sEntity.getValueAsString("text");
    		dbView.setValue("view_sql", view_sql.getBytes());
    	}
    	dbView.setValue("name", table_name);
    	dbView.setValue("type", table_type);
    	dbView.setValue("comments", comments);
    	dbView.setValue("create_time", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    	 baseDao.save(dbView);
    	 context.setAttribute("msg", "success");
         context.forword("/save_result.jsp");
    	
    }
    
    
    /**
     * 
    * @Title: hasTV
    * @Description: 判断数据表视图是否存在
    * @author gezhiling
    * @date  2017-6-29 下午5:40:50
    * @param context
    * @throws ServletException
    * @throws IOException
     */
    public void hasTV(HTTPContext context) throws ServletException, IOException {

        String tableName = context.getParameter("tableName");
        String errInfo = "success";
        //
        HashMap<String, String> map = new HashMap<String, String>();
        SEntity sEntity = new SEntity("db_view");
        sEntity.addCondition("name = ?", tableName);
        baseDao.load(sEntity);
        if (!sEntity.isEmpty()) {
            errInfo = "error";
        }
        map.put("result", errInfo);
        context.sendAsJson(JSONObject.toJSONString(map));
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
    public void getTableAndViewList(HTTPContext context) throws ServletException, IOException {

    	StringBuffer tab_sql = new StringBuffer(" select * from db_view ");
		String type = context.getParameter("type");
		type= StringUtils.isBlank(type)?"table":type;
		tab_sql.append(" and type ='"+type.toUpperCase()+"' ");
		
		
	    // 获取参数
        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        keyword = StringUtils.trimToEmpty(keyword);
        if(StringUtils.isNotBlank(keyword))
        {
        	tab_sql.append("and name like '%" + keyword + "%'");
        }
        //sql
        String sql =tab_sql.toString().replaceFirst("and", "where");
        // 计算分页
        String sqlCount = "select count(*) as totalcount  from ("+sql+") ";
        int totalCount = baseDao.search(sqlCount, null).get(0).getValueAsInteger("totalcount");
        int totalPage = Double.valueOf(Math.ceil(totalCount / new Double(pageSize))).intValue();
        pageNum = PageUtil.getPageNum(pageNum, totalPage);
        // 结果集
        SEntityList sEntityList = baseDao.search(sql, null, pageNum, pageSize);
        //
        sEntityList.setPageNum(pageNum);
        sEntityList.setTotalCount(totalCount);
        sEntityList.setTotalPage(totalPage);
        sEntityList.setPageSize(pageSize);
        context.setAttribute("type", type.toLowerCase());
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/search/tv_config_list.jsp"); 
    }
}