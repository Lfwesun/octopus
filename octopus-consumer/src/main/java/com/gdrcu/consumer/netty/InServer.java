package com.gdrcu.consumer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 启动netty服务
 * 
 * @author Administrator
 *
 */
public class InServer {
	private int port;
	private String encode;
	ChannelFuture f;
	EventLoopGroup bossGroup;
	EventLoopGroup workerGroup;

	public InServer(int port,String encode) {
		this.port = port;
		this.encode = encode;
	}

	public void run() throws Exception {
		// 创建NioEventLoopGroup线程池
		bossGroup = new NioEventLoopGroup();//
		workerGroup = new NioEventLoopGroup();//默认是 CPU 核心数乘以2
		ServerBootstrap b = new ServerBootstrap();//ServerBootstrap.bind(int)负责绑定端口,可以绑定多个端口
		ServerBootstrap channel = b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
		channel.childHandler(new ChannelInitializer<SocketChannel>(){

			@Override
			protected void initChannel(SocketChannel arg0) throws Exception {
				// TODO Auto-generated method stub
				arg0.pipeline().addLast(new ObjectEncoder());
				arg0.pipeline().addLast(new InHandler(encode));
			}
				
		}).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);
		//b.bind(port);
		f = b.bind(port).sync();
	}
	
	public void stop() throws InterruptedException{
		workerGroup.shutdownGracefully();
		bossGroup.shutdownGracefully();
		f.channel().closeFuture().sync();
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println("start  InServer ...");
		int port;
		if (args.length > 1) {
			port = Integer.parseInt(args[0]);
		}else{
			port = 8080;
		}
		System.out.println("port:-----1: "+port);
		InServer s = new InServer(port,"utf-8");
		System.out.println("port:-----2: "+port);
		s.run();
		System.out.println("over InServer ...");
	}
}
