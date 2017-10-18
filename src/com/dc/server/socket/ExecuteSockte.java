package com.dc.server.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.dc.platform.Base4DBService;
import com.dc.platform.Context;
import com.dc.platform.SEntity;
import com.dc.server.Const;
import com.dc.server.key.TypeEnum.ErrorType;
import com.dc.server.service.ClientLoginService;
import com.dc.server.service.ServerDataService;
import com.dc.server.service.impl.ClientLoginServiceImpl;
import com.dc.server.service.impl.ServerDataServiceImpl;

/**
 * 
 @company 东昌数码
 *          <p>
 *          Copyright: Copyright (c) 2017
 *          </p>
 * @ClassName: ExecuteSockte
 * @Description: 对所有的连接进行处理
 * @author gezhiling
 * @date 2017-6-21 下午3:40:31
 */
public class ExecuteSockte implements Runnable {

	private Logger log = Logger.getLogger(ExecuteSockte.class); 

	private Socket socket;

	private String account;
	
	private String userId; 
	
	private DataOutputStream dos;

	private DataInputStream dis;
	
	private Base4DBService baseService;
	
	private ServerDataService serverDataService;
	
	private ClientLoginService clientLoginService;
	
	public ExecuteSockte(String account, Socket socket) {
		this.socket = socket;
		this.account = account;
		this.serverDataService = new ServerDataServiceImpl();
		this.clientLoginService = new ClientLoginServiceImpl();
		this.baseService = (Base4DBService) Context.getBean("base4DBService");
		//
		SEntity userEntity = new SEntity("admin_user");
		userEntity.addCondition("account = ?", account);
		baseService.getBaseDao().load(userEntity);
		this.userId= userEntity.getValueAsString("id");
	}

	@Override
	public void run() {

			System.out.println(Thread.currentThread().getName() + " - [" + account+ ", " + socket.getInetAddress() + ":" + socket.getPort() + ']');

			boolean isExecute = true;
			while (isExecute) {
				log.info("--------------------" + socket.getInetAddress() + ":" + socket.getPort());
				try {
					//接受数据时的超时时间
		             socket.setSoTimeout(1*60*1000); 
					//输入输出
					dos = new DataOutputStream(socket.getOutputStream());
					dis = new DataInputStream(socket.getInputStream());
					//接收请求信息
					String results = dis.readUTF();
					JSONObject  obj  =  (JSONObject) JSONObject.parse(results);
					String result = (String) obj.get("msgType");//数据类型
					log.info(result);
					//
					JSONObject json = new JSONObject();
					if("REQUEST".equals(result)){
						String msg="NO DATA";
						//查询用户的同步数据
						SEntity entity = serverDataService.getDataByUser(baseService, userId);
						if(entity!=null){
							msg = "DATA";
							String dataId = entity.getValueAsString("id");
							String userInfo = entity.getValueAsString("user_info");
							json.put("info", userInfo);
							json.put("dataId", dataId);
						}
						
						json.put("result", msg);
						dos.writeUTF(json.toJSONString());
					}else if(String.valueOf(Const.TABLE_SQLISSUCCESS_CODE).equals(result)){
						log.info("account："+account +"Data transmission successful>>>>>>>>>.");
						//删除已同步成功的备份数据
						String dataId = (String) obj.get("dataId");
						serverDataService.deleteDataById(baseService, dataId);
						log.info("数据同步完成，删除");
					}else {
						String dataId = (String) obj.get("dataId");
						serverDataService.deleteDataById(baseService, dataId);
						//推送数据执行失败 ErrorType
						log.info("@DataEX>>>>>>"+account+"Data transmission failed>>>>>>>>>.");
						serverDataService.saveTempErrorLog(baseService, dataId, result, String.valueOf(ErrorType.DATA_EX.getKey()),userId);
					}
				} catch (Exception e) {
					isExecute = false;
					e.printStackTrace();
					//通讯异常 下线客户端
					clientLoginService.logout(baseService, account);
					//写入错误信息
					serverDataService.saveTempErrorLog(baseService, account, e.getMessage(), String.valueOf(ErrorType.SOCKET_EX_TIME_OUT.getKey()),account);
					log.info(e.getMessage() +"@SocketEX>>>>>>"+account+ "Disconnnected access client exits.");
				}
			}
		
	}
}
