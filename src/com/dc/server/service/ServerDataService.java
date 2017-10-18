package com.dc.server.service;

import com.dc.platform.Base4DBService;
import com.dc.platform.SEntity;

public interface ServerDataService {
	
	SEntity getDataByUser(Base4DBService base4dbService,String userId);
	
	 void deleteDataById(Base4DBService base4dbService,String dataId);
	
	 void saveTempErrorLog(Base4DBService baseService, String id,String errorResult,String type,String account);
	 
}
