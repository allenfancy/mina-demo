package com.allen.mina.handle;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.logging.MdcInjectionFilter;

public class FirstServerHandler implements IoHandler{

	//保存所有客户端已连接的会话
	private final Set<IoSession> sessions = Collections.synchronizedSet(new HashSet<IoSession>());
	
	//保存已连接的客户端
	private final Set<String> users = Collections.synchronizedSet(new HashSet<String>());
	
	
	
	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		Message msg = (Message) message;
		int command = msg.getCommand();
		String user = msg.getUser();
		switch (command) {
		case Command.QUIT:
			session.write(msg);
			sessionClosed(session);
			break;
		case Command.BROADCAST:
			Date date = new Date();
			//发送广播
			broadCast(user + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + "" + msg.getMsgContent());
			break;
		case Command.LOGIN:
			//保存当前会话
			sessions.add(session);
			session.setAttribute("user", user);
			MdcInjectionFilter.setProperty(session, "user", user);
			users.add(user);
			broadCast("用户："+ user + "加入了会话！");//发送广播
			break;
		default:
			broadCast("你是外星人吗？");//发送广播
			break;
		}
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		String user = (String) session.getAttribute("user");
		users.remove(user);
		sessions.remove(session);
		broadCast("用户" + user + "消失在会话之中 。。。。 呜呜。。。。。");
	}

	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
	/**
	 * 广播到所有的会话
	 * @param message
	 */
	public void broadCast(String message) {
        synchronized (sessions) {
            for (IoSession session : sessions) {
                if (session.isConnected()) {
                    session.write(message);
                }
            }
        }
    }
	/**
	 * 返回已连接的客户端总数
	 * @return
	 */
	public int getNumberOfUsers(){
		return users.size();
	}
	/**
	 * 把用户踢出去呢
	 * @param name
	 */
	public void kick(String name){
		synchronized (sessions) {
			for(IoSession session : sessions){
				if(name.equals(session.getAttribute("user"))){
					broadCast("用户"+name + "被踢出了");
				}
			}
		}
	}
	
	
	
	
	
	
	
	

}
