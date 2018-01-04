package com.gdrcu.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Provider {
	public static void main(String[] args) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"/META-INF/spring/spring-octopus-provider.xml"});
        context.start();

		System.in.read(); // press any key to exit
	}

}
