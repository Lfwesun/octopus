package com.gdrcu.consumer.netty;

import java.lang.reflect.Method;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ReflectionUtils;

import com.gdrcu.api.service.inter.S2001300000205;
import com.gdrcu.common.utils.SpringContextUtil;
import com.gdrcu.common.utils.TransUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class InHandler extends SimpleChannelInboundHandler<ByteBuf> {
	// 定义编码字符
	private String encode;// io encode ,if not equals with Application encode
							// ,should convert .

	public InHandler(String encode) {
		this.encode = encode;
	}

	@Override
	public void channelRead0(ChannelHandlerContext arg0, ByteBuf arg1) throws Exception {
		// TODO Auto-generated method stub
		byte[] bytes = new byte[arg1.readableBytes()];
		arg1.readBytes(bytes);
		String ar1Str = new String(bytes, encode);
		// 定义交易码，用于请求报文进行判断用，如果请求报文中包含这个交易码则说明注册了并可用，否则不可用
		// 在往后开发中会将交易码配置到配置文件中
		System.out.println("InHandler--"+ar1Str);
		//String tranCode = "2001300000106";
		String tranCode = ar1Str.substring(0, 13);
		System.out.println(tranCode);
		// 创建服务标识 S + tranCode形式，目前通过TransUtil builderServiceName来创建
		// 工具类统一放common了项目中
		String serviceCode = TransUtil.buildServiceName(tranCode);
		System.out.println(serviceCode);

		// 根据服务码获取对应的接口bean
		try {
			ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
					new String[] { "/META-INF/spring/spring-octopus-consumer.xml" });
			context.start();
			Object bean = context.getBean(serviceCode);
			if (bean != null) {
				// 创建参数数组，用于封闭请求数据
				Object[] param = new Object[1];
				param[0] = ar1Str;
				System.out.println("------------");
				// 通过反射获取接口的通讯方法并调用
				Method method = ReflectionUtils.findMethod(bean.getClass(), "doSend", String.class);
				//获取返回数据
				Object resp = ReflectionUtils.invokeMethod(method, bean,param);
				System.out.println("响应报文：\t\n" + resp);
				//写出时不能用 new String(s.getBytes,"gbk");的形式转码，没效果
				arg0.channel().writeAndFlush(resp.toString().getBytes(encode));
			}else{
				return;
			}
		} catch (Exception e) {
			//System.out.println("获取bean异常： \n" + e);
			return;
		}

	}

	@Override
	public boolean acceptInboundMessage(Object msg) throws Exception {
		// TODO Auto-generated method stub
		return super.acceptInboundMessage(msg);
	}

	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		super.channelRead(arg0, arg1);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelReadComplete(ctx);
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelRegistered(ctx);
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelUnregistered(ctx);
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelWritabilityChanged(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		// TODO Auto-generated method stub
		super.userEventTriggered(ctx, evt);
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.handlerAdded(ctx);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.handlerRemoved(ctx);
	}

	@Override
	public boolean isSharable() {
		// TODO Auto-generated method stub
		return super.isSharable();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}

}
