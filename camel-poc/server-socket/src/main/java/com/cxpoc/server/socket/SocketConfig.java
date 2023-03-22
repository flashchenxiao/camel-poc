package com.cxpoc.server.socket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.cxpoc.server.websocket.WebsocketServer;

@Component
public class SocketConfig {
	private static Logger logger = LogManager.getLogger(WebsocketServer.class);

	public static ServerSocket serverSocket = null;

	private static final ThreadPoolExecutor threadpool = new ThreadPoolExecutor(15, 15, 10L, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	@PostConstruct
	public void socketCreate() {
		try {
			serverSocket = new ServerSocket(8889);
			logger.info("socket服务端开启");
			while (true) {
				Socket socket = serverSocket.accept();
				logger.info("接收到客户端socket" + socket.getRemoteSocketAddress());
				threadpool.execute(new ServerReceiveThread(socket));
			}
		} catch (IOException e) {
			logger.info("socket服务启动异常");
			e.printStackTrace();
		}
	}

	class ServerReceiveThread implements Runnable {

		private Socket socket;

		public ServerReceiveThread(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				logger.info("进入socket线程池中---[{}]", socket.getRemoteSocketAddress());
//				while (true) {
				InputStream is = socket.getInputStream();
				DataInputStream dis = new DataInputStream(is);
				OutputStream os = socket.getOutputStream();
				PrintStream ps = new PrintStream(os);
				String str = dis.readLine();

				ps.println(
						"服务socket-线程: " + Thread.currentThread().getName() + ", 输入消息:" + str + " 现在时间:" + new Date());
				logger.info("服务socket收到的消息[{}]", socket.getRemoteSocketAddress() + " -- 输入数据 " + str);

//				}
			} catch (Exception e) {
				logger.info("接收数据异常socket关闭");
				e.printStackTrace();
			} finally {
			}
		}
	}
}
