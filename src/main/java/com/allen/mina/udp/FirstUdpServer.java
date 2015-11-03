package com.allen.mina.udp;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.filter.logging.MdcInjectionFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.allen.mina.handle.FirstServerHandler;

public class FirstUdpServer {

	private static final int PORT = 9898;
	
	public static void main(String [] args) throws IOException{
		//UDP Acceptor
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.getFilterChain().addLast("logging", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		acceptor.getFilterChain().addLast("mdc", new MdcInjectionFilter());
		acceptor.setHandler(new FirstServerHandler());
		acceptor.getSessionConfig().setReadBufferSize(2084);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		DatagramSessionConfig dcfg = acceptor.getSessionConfig();
		dcfg.setReuseAddress(true);
		acceptor.bind(new InetSocketAddress(PORT));

	}
}
