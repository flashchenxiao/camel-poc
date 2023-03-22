package com.cxpoc.server.websocket;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/websocket")
public class WebsocketServer {
	private static Logger logger = LogManager.getLogger(WebsocketServer.class);
	private static CopyOnWriteArraySet<WebsocketServer> webSocketSet = new CopyOnWriteArraySet<>();
	private Session session;

	/**
	 * 连接建立成功调用的方法
	 */
	@OnOpen
	public void onOpen(Session session) {
		try {
			this.session = session;
			session.getBasicRemote().sendText("欢迎" + session.getId() + "访问!请问找谁:1:张三 2:李四 3:王五");
			webSocketSet.add(this);
			logger.info("建立连接");
		} catch (Exception e) {
			logger.error("websocket IO异常");
		}
	}

	/**
	 * 连接关闭调用的方法
	 */
	@OnClose
	public void onClose() {
		webSocketSet.remove(this); // 从set中删除
		logger.info("连接关闭！");
	}

	/**
	 * 收到客户端消息后调用的方法
	 *
	 * @param message 客户端发送过来的消息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		logger.info("收到来自窗口 {}  的信息:{}", session.getId(), message);
		String respMsg = null;
		if ("1".equals(message)) {
			respMsg = "张三";
		} else if ("2".equals(message)) {
			respMsg = "李四";
		} else if ("3".equals(message)) {
			respMsg = "王五";
		} else if ("8".equals(message)) {
			respMsg = "大家好，现在时间" + new Date();
		} else {
			respMsg = "请问找谁:1:张三 2:李四 3:王五,8:通知消息";
		}
		try {
			if ("8".equals(message)) {
				for (WebsocketServer item : webSocketSet) {
					item.session.getBasicRemote().sendText(respMsg);
				}
			} else {
				session.getBasicRemote().sendText(respMsg);
			}

		} catch (IOException e) {
			logger.error("onMessage发生错误");
		}
	}

	/**
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		logger.error("发生错误");
		error.printStackTrace();
	}

}
