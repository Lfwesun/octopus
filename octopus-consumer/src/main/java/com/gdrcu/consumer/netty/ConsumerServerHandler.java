package com.gdrcu.consumer.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ConsumerServerHandler extends ChannelInboundHandlerAdapter {
	private String encode;
	
	  public ConsumerServerHandler() {
		super();
	}

	public ConsumerServerHandler(String encode) {
		super();
		this.encode = encode;
	}

	@Override
	    public void channelRead(ChannelHandlerContext ctx, Object msg)
	            throws Exception {
	        System.out.println("server channelRead..");
	        ByteBuf buf = (ByteBuf) msg;
	        byte[] req = new byte[buf.readableBytes()];
	        buf.readBytes(req);
	        String body = new String(req, "UTF-8");
	        System.out.println("The time server receive order:" + body);
//	        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(
//	                System.currentTimeMillis()).toString() : "BAD ORDER";
//	        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	        //在这里开始调用后台服务请求并获取到请求返回的数据
	        ByteBuf tcpClient = tcpClient(body,"utf-8");
	        System.out.println(".........."+tcpClient);
	       // new TimeClient().connect(10012, "192.168.79.1");
	        System.out.println(",,,,,,,,,,"+tcpClient);
	        
	        ctx.write(tcpClient);
	        //用哪些个
	        //ctx.writeAndFlush(tcpClient);
	    }

	    @Override
	    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
	        System.out.println("server channelReadComplete..");
	        ctx.flush();//刷新后才将数据发出到SocketChannel
	    }

	    @Override
	    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	            throws Exception {
	        System.out.println("server exceptionCaught..");
	        ctx.close();
	    }
	    
	    private static ByteBuf  tcpClient(String msg,String encode) throws IOException {
			try{
			  String host = "192.168.79.1";
			  int port = 10012;
			  Socket socket = new Socket(host,port);
			  OutputStream os = socket.getOutputStream();
			  PrintWriter pw = new PrintWriter(os);
			  pw.write(msg);
			  pw.flush();
			  socket.shutdownOutput();
			  InputStream in = socket.getInputStream();
			  
			  BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			  String resp = null;
			  StringBuffer sb = new StringBuffer();
			  while((resp = bf.readLine()) != null){
				  sb.append(resp).append("\n");
			  }
			  System.out.println("服务端收到的"+sb.toString());
			  ByteBuf copiedBuffer = Unpooled.copiedBuffer(sb.toString().getBytes());
			  bf.close();
			  in.close();
			  pw.close();
			  os.close();
			  socket.close();
			  return copiedBuffer;
			}catch(UnknownHostException e){
				e.printStackTrace();
			}
			return null;
		}
}
