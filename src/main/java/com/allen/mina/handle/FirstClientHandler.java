package com.allen.mina.handle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class FirstClientHandler implements IoHandler {

	public void exceptionCaught(IoSession arg0, Throwable arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	/**
	 * 处理从服务器端控制台输入的消息
	 */
	public void messageReceived(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message != null) {
			Date date = new Date();
			String printMsg = "";
			if (message instanceof Message) {
				Message msg = (Message) message;
				int command = msg.getCommand();
				switch (command) {
				case Command.QUIT:// 处理服务器返回的退出消息
					session.close(true);
					System.exit(0);// 退出客户端
					break;

				default:
					System.out.println("服务器出毛病了。。。医生正在检查呢?");
					break;
				}
				printMsg = "服务器说：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + " "
						+ msg.getMsgContent();
			} else {
				printMsg = "服务器说：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) + " "
						+ message.toString();
			}
			System.out.println(printMsg);
		}

		Scanner scanner = new Scanner(System.in);
		if (scanner.hasNext()) {
			String in = scanner.nextLine();
			Message msg = new Message();
			msg.setId(session.getId());
			msg.setUser("admin");
			if (in.equalsIgnoreCase("quit")) {
				msg.setMsgContent(in);// 接收用户从控制台的输入
				msg.setCommand(Command.QUIT);
			} else {
				msg.setMsgContent(in);// 接收用户从控制台的输入
				msg.setCommand(Command.BROADCAST);
			}
			session.write(msg);
		}
	}

	public void messageSent(IoSession arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	public void sessionClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void sessionCreated(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void sessionIdle(IoSession arg0, IdleStatus arg1) throws Exception {
		// TODO Auto-generated method stub

	}

	// 会话建立并打开时调用。第一次建立连接的时候
	public void sessionOpened(IoSession session) throws Exception {
		Message message = new Message();
		message.setId(session.getId());
		message.setUser("admin");
		message.setMsgContent("客户:" + message.getUser() + "已连接!");
		message.setCommand(Command.LOGIN);
		session.write(message);
	}
}
