package com.gdrcu.consumer;

import java.util.Map;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gdrcu.api.exception.BaseException;
import com.gdrcu.api.service.inter.S2001300000205;
import com.gdrcu.common.utils.SpringContextUtil;
import com.gdrcu.consumer.entity.PortsUtil;
import com.gdrcu.consumer.netty.ConsumerServer;
import com.gdrcu.consumer.netty.InServer;
import com.gdrcu.consumer.netty.InServer01;

/**
 * 测试与dubbo的注册调用
 * 
 * @author Administrator
 *
 */
public class ConsumerConnector {
	public static void main(String[] args) throws Exception {

		System.setProperty("java.net.preferIPv4Stack", "true");
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "/META-INF/spring/spring-octopus-consumer.xml" });
		context.start();
		InServer  server = (InServer) context.getBean("inServer");
		ConsumerServer consumerServer = (ConsumerServer) context.getBean("consumerServer");
		PortsUtil ports = (PortsUtil)context.getBean("ports");
		try {
			//测试端口与编码传参形式的调用
			/*Map <String,String>map = ports.getMap();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
				String key = entry.getKey();
				 Integer port = Integer.valueOf( entry.getKey());
				 String encode = entry.getValue();
				server.bind(port, encode);
			} */

			// 在这里开始获取到netty 提供的服务,如果这里不调用是无法请求配置文件中配置的端口的
			//这里只会对当前的bean有效：consumerServer 
			server.run();
			consumerServer.bind();
		} catch (InterruptedException e) {
				e.printStackTrace();
		}

	}
}
