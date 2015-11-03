package com.allen.mina.tcp;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.allen.mina.handle.FirstClientHandler;

public class FirstTcpClient {

	private static final String HOSTNAME = "127.0.0.1";
	private static final int PORT = 9898;
	
	public static void main(String [] args){
		NioSocketConnector connector = new NioSocketConnector();
		connector.getFilterChain().addLast("logging", new LoggingFilter());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		connector.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		connector.setHandler(new FirstClientHandler());
		IoSession session = null;
		for(;;){
			try{
				ConnectFuture future = connector.connect(new InetSocketAddress(HOSTNAME,PORT));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			}catch(RuntimeIoException e){
				System.err.println("Failed to connect.");
				e.printStackTrace();
			}
			session.getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		}
	}
}
