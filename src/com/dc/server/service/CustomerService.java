package com.dc.server.service;

import java.io.IOException;
import javax.servlet.ServletException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntityList;

@Service("customerService")
public class CustomerService extends Base4DBService {
	
    /**
     * 客户信息查询
     * @param context
     * @throws ServletException
     * @throws IOException
     * @table crm_customer
     * @method load方法查询
     */
    public void searchCus(HTTPContext context) throws ServletException, IOException {

        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        String status = context.getParameter("status");
        SEntityList sEntityList = new SEntityList("crm_customer");
        sEntityList.addCondition(status+" = ? ", 1);
        sEntityList.setPageNum(pageNum);
        sEntityList.setPageSize(pageSize);
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
            sEntityList.addCondition("customer_name like '%" + keyword + "%'");
        }
        baseDao.load(sEntityList);
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.setAttribute("status", status);
        context.forword("/admin/search/customer_list.jsp");
    }

    /**
     * 获取SPARESTR8数据 （收货地）
     * @param context
     * @throws ServletException
     * @throws IOException
     * @table SPARESTR8
     * @method load方法查询
     */
    public void getSPARESTR8List(HTTPContext context) throws ServletException, IOException {

        int pageNum = context.getParameter("pageNum") == null ? 1 : Integer.parseInt(context.getParameter("pageNum"));
        int pageSize = context.getParameter("pageSize") == null ? 10 : Integer.parseInt(context.getParameter("pageSize"));
        String keyword = context.getParameter("keyword");
        SEntityList sEntityList = new SEntityList("sparestr8");
        sEntityList.setPageNum(pageNum);
        sEntityList.setPageSize(pageSize);
        // 根据关键字查询
        if (!StringUtils.isBlank(keyword)) {
            sEntityList.addCondition("sparestr8 like '%" + keyword + "%'");
        }
        baseDao.load(sEntityList);
        context.setAttribute("keyword", keyword);
        context.setAttribute("sEntityList", sEntityList);
        context.forword("/admin/search/sparestr8_list.jsp");
    }
    
}
