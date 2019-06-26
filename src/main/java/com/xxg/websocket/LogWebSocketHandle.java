package com.xxg.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/log/{docker}/{lines}")
public class LogWebSocketHandle {
	
	private Process process;
	private InputStream inputStream;
	
	/**
	 * 新的WebSocket请求开启
	 */
	@OnOpen
	public void onOpen(@PathParam("docker") String docker,  
            @PathParam("lines") int lines, Session session) {
		try {
		    if("".equals(docker) || null == docker){
		        System.out.println("容器名为空！");
		    }
		    if(lines == 0){
                System.out.println("日志行数为0为空！");
            }
			// 执行tail -f命令
			process = Runtime.getRuntime().exec("docker logs -f --tail " + lines + " fdc-check-"+ docker);
			inputStream = process.getInputStream();
			
			// 一定要启动新的线程，防止InputStream阻塞处理WebSocket的线程
			TailLogThread thread = new TailLogThread(inputStream, session);
			thread.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * WebSocket请求关闭
	 */
	@OnClose
	public void onClose() {
		try {
			if(inputStream != null)
				inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(process != null)
			process.destroy();
	}
	
	@OnError
	public void onError(Throwable thr) {
		thr.printStackTrace();
	}
}