package com.dc.server.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.dc.server.service.ClientLoginService;
import com.dc.server.service.impl.ClientLoginServiceImpl;


public class ListenSocket implements Runnable {

	private static final Logger log = LogManager.getLogger(ListenSocket.class);
	
	// 端口号
	private static final int port = 7003;

	// 服务端socket
	private ServerSocket serverSocket = null;
	
	
	private ClientLoginService clientLoginService;
	
	public ListenSocket() {

		// 创建一个服务器端Socket，即SocketService
		try {
				 serverSocket = new ServerSocket(Integer.valueOf(port));
				 clientLoginService = new ClientLoginServiceImpl();
				log.info("服务器启动成功.");
		} catch (IOException e) {
			log.info("没有启动监听：" + e.getMessage());
		}
	}

	/**
	 * 监听的线程
	 */
	public void run() {

		while (true) {
			// accept()方法开始监听，等待客户端的连接
			// accept()阻塞等待客户请求，有客户请求到来则产生一个Socket对象，并继续执行
			try {
				Socket server = serverSocket.accept();
				log.info("-----监听线程----start--------" + server.getInetAddress() + ":" + server.getPort());
				System.out.println("-----监听线程----start--------" + server.getInetAddress() + ":" + server.getPort());
				//
				//输入输出
				DataOutputStream dos = new DataOutputStream(server.getOutputStream());
				DataInputStream	dis = new DataInputStream(server.getInputStream());
				String result = dis.readUTF();
				System.out.println(result);
				JSONObject  obj  =  (JSONObject) JSONObject.parse(result);
				String account = (String) obj.get("account");
				String pwd = (String) obj.get("pwd");
				String loginStatus = clientLoginService.login(account, pwd);
				if("ack".equals(loginStatus)){
					log.info("\n\n"+account+"登陆成功......\n");  
					//创建线程 发送数据
					new Thread(new ExecuteSockte(account,server)).start();
				}
				dos.writeUTF(loginStatus);
				log.info("loginStatus:"+loginStatus);
			} catch (Exception e) {
				e.printStackTrace();
				log.info("ListenSocket Run Error：" + e.getMessage());
			}
		}
	}

	public void closeSocketServer() {
		try {
			if (null != serverSocket && !serverSocket.isClosed()) {
				serverSocket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
