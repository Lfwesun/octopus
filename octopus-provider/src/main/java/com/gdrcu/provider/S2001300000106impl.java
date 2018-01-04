package com.gdrcu.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.dubbo.rpc.RpcContext;
import com.gdrcu.api.exception.BaseException;
import com.gdrcu.api.service.inter.S2001300000106;
import com.gdrcu.api.service.inter.S2001300000205;
/**
 * 作为provider中的接口，负责与out端的通讯
 * @author Administrator
 *
 */
public class S2001300000106impl implements S2001300000106 , Serializable{

	public String doSend(String msg) throws BaseException {
		// TODO Auto-generated method stub
		System.out.println("[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + msg + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
		//return可以用与out端通讯时接收到的响应信息来代替
		return tcpClientSend("request from provider..S2001300000106impl.","192.168.79.1",10012);
		//return "S2001300000205impl " + msg + ", response form provider: " + RpcContext.getContext().getLocalAddress();
	}
	/**
	 * 与OUT端通讯
	 * @param msg  请求信息
	 * @return  响应信息
	 */
	public static String tcpClientSend(String msg,String host,int port){
		if (msg == null || msg.trim().equals("")) {
			return "请求信息为空！";
		}
		//创建与OUT端的连接
		try {
			Socket socket = new Socket(host,port);
			//写出
			OutputStream os = socket.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(msg);
			pw.flush();
			socket.shutdownOutput();
			
			//读取
			InputStream in = socket.getInputStream();
			BufferedReader bf = new BufferedReader(new InputStreamReader(in));
			String resp = null;
			StringBuffer sbf = new StringBuffer();;
			while((resp = bf.readLine()) != null){
				sbf.append(resp).append("\n");
			}
			bf.close();
			in.close();
			pw.close();
			os.close();
			socket.close();
			return sbf.toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("无效的host \n" + e);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("请求OUT时 IOException\n" + e);
			return null;
		}
	}
	
	public static void main(String[] args) {
		String s = tcpClientSend("request from provider...","192.168.79.1",10012);
		System.out.println(s);
	}
}
