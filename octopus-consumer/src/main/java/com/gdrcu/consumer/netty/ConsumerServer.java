package com.gdrcu.consumer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * 作为消费方连接时的服务器类，一个方法，一个内部类
 * 方法一：端口绑定
 * 内部类：创建线程组时用
 * @author Administrator
 *
 */
public class ConsumerServer {
	private static int port;
	private String encode;
	
	public ConsumerServer() {
		super();
	}
	public ConsumerServer(int port, String encode) {
		super();
		this.port = port;
		this.encode = encode;
	}
	public void bind() throws Exception{
		//netty 中创建工作线程组时需创建一个主一个工作（副）
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			//配置服务器线程组
			ServerBootstrap b = new ServerBootstrap();
			ServerBootstrap group = b.group(bossGroup, workerGroup);
			ServerBootstrap channel = group.channel(NioServerSocketChannel.class);
			ServerBootstrap option = channel.option(ChannelOption.SO_BACKLOG, 1024);
			ServerBootstrap childHandler = option.childHandler(new ChildChannelHandler());
			//同步绑定
			ChannelFuture channelFuture = childHandler.bind(port).sync();
			//关闭
			channelFuture.channel().closeFuture().sync();
		}catch(Exception e){
			
		}finally{
			//关闭线程组
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	/**
	 * 添加以参数的形式传进提供给请求方使用的端口与请求方使用的编码
	 * @param port
	 * @param encode
	 * @throws Exception
	 */
	public void bind(int port, String encode) throws Exception{
		//netty 中创建工作线程组时需创建一个主一个工作（副）
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			//配置服务器线程组
			ServerBootstrap b = new ServerBootstrap();
			ServerBootstrap group = b.group(bossGroup, workerGroup);
			ServerBootstrap channel = group.channel(NioServerSocketChannel.class);
			ServerBootstrap option = channel.option(ChannelOption.SO_BACKLOG, 1024);
			ServerBootstrap childHandler = option.childHandler(new ChildChannelHandler());
			//同步绑定
			ChannelFuture channelFuture = childHandler.bind(port).sync();
			//关闭
			channelFuture.channel().closeFuture().sync();
		}catch(Exception e){
			
		}finally{
			//关闭线程组
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
	
	private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{

		@Override
		protected void initChannel(SocketChannel arg0) throws Exception {
			// TODO Auto-generated method stub
		            System.out.println("server initChannel..");
		            arg0.pipeline().addLast(new ConsumerServerHandler());
		            arg0.pipeline().addLast(new StringDecoder());//添加字符
		           // arg0.pipeline().addLast(new ConsumerServerHandler(encode));//添加字符
		}
		
	}
	
	  public static void main(String[] args) throws Exception {
	      /*  int port = 9900;
	        if (args != null && args.length > 0) {
	            try {
	                port = Integer.valueOf(args[0]);
	            } catch (NumberFormatException e) {

	            }
	        }*/
		  System.out.println(port);
	        new ConsumerServer().bind();;
	    }
	
}
