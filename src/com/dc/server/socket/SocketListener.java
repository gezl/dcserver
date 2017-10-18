package com.dc.server.socket;

import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class SocketListener implements ServletContextListener {

	private Thread socketThread;

	/**
	 * 销毁 当Servlet容器终止Web应用时调用该方法
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if (null != socketThread && !socketThread.isInterrupted()) {
			new ListenSocket().closeSocketServer();
			socketThread.interrupt();
			System.out.println("销毁时间："+new Date());
		}
	}

	/**
	 * 初始化当Servlet容器启动Web应用时调用该方法
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		if (null == socketThread) {
			socketThread = new Thread(new ListenSocket());
			socketThread.start();
		}
	}
}
