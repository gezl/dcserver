package com.dc.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.dc.platform.Base4DBService;
import com.dc.platform.SEntity;
import com.dc.platform.SEntityList;
import com.dc.server.service.ServerDataService;

public class ServerDataServiceImpl implements ServerDataService{

	@Override
	public SEntity getDataByUser(Base4DBService baseService, String userId) {
		//每次请求当前用户前200条数据
		SEntityList list = new SEntityList("db_temp_info");
		list.setPageNum(1);
		list.setPageSize(1);
		list.addCondition("user_id = ? ", userId);
		baseService.getBaseDao().load(list);
		if(list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	public void deleteDataById(Base4DBService baseService, String dataId) {
		//删除已同步成功的备份数据
		List<Object> parameters = new ArrayList<Object>();
		String sqlDel = "delete from db_temp_info where id =?";
		parameters.add(dataId);
		baseService.getBaseDao().delete(sqlDel, parameters);
	}

	@Override
	public void saveTempErrorLog(Base4DBService baseService, String id,String errorResult,String type,String account) {
		SEntity errorLog = new SEntity("db_temp_error_log");
		errorLog.setValue("temp_info_id", id);
		errorLog.setValue("error_code", errorResult);
		errorLog.setValue("state", type);
		errorLog.setValue("user_id", account);
		baseService.getBaseDao().save(errorLog);
	}

}
