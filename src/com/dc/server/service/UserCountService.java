package com.dc.server.service;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.stereotype.Service;

import com.dc.platform.Base4DBService;
import com.dc.platform.HTTPContext;
import com.dc.platform.SEntityList;
import com.dc.server.key.TypeEnum.LineType;

@Service("userCountService")
public class UserCountService extends Base4DBService {
	
	public void findUserCount(HTTPContext context) throws IOException,ServletException{
		 SEntityList sEntityList = new SEntityList("admin_user");
         sEntityList.addCondition("online_status =?", LineType.ON.getKey());
         baseDao.load(sEntityList);
         context.setAttribute("sEntityList", sEntityList);
         context.forword("/default.jsp");
	}

}
