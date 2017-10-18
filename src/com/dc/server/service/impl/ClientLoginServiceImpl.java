package com.dc.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.SimpleHash;

import com.dc.platform.Base4DBService;
import com.dc.platform.Context;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.key.TypeEnum.EnabledType;
import com.dc.server.key.TypeEnum.LineType;
import com.dc.server.service.ClientLoginService;

public class ClientLoginServiceImpl implements ClientLoginService {
	
	private static final Logger log = LogManager.getLogger(ClientLoginServiceImpl.class);

	@Override
	public String login( String userName,String passWord) {
		Base4DBService   baseService = (Base4DBService) Context.getBean("base4DBService");
		String loginStatus = "nak";
		String sql = "select id,db_type,account,password,online_status,enable from admin_user where account= ? and password= ?";
		List<Object> param = new ArrayList<Object>();
		param.add(userName);
		param.add(new SimpleHash("SHA-1", userName, passWord).toBase64() );
		SEntityList sEntityList = baseService.getBaseDao().search(sql, param);
		if (sEntityList.size() > 0) {
			SEntity sEntity =sEntityList.get(0);
			String account = sEntity.getValueAsString("account");
			String onlineStatus = sEntity.getValueAsString("online_status");
			String enable = sEntity.getValueAsString("enable");
			if(String.valueOf(EnabledType.DISABLED.getKey()).equals(enable)){
				loginStatus = "ack_disable";
			}else if(LineType.ON.getKey().equals(onlineStatus)){
				loginStatus = "ack_online";
			}else{
				try {
					List<Object> parameters = new ArrayList<Object>();
					parameters.add(LineType.ON.getKey());
					parameters.add(account);
					String sql_ = "update admin_user set online_status=? where account =? ";
					baseService.getBaseDao().excuteUpdate(sql_, parameters);
					loginStatus = "ack";
				} catch (Exception ex) {
					//处理服务器异常信息
						loginStatus = "ex";
						log.info("ListenSocket Run Error：" + ex.getMessage());
				}
			}
		}else{
			loginStatus = "nak";
		}
		return loginStatus;
	}

	@Override
	public void logout(Base4DBService baseService, String userName) {
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(userName);
		String sql = "update admin_user set online_status='Off-line' where account =? ";
		baseService.getBaseDao().excuteUpdate(sql, parameters);
	}

}
