package com.gdrcu.provider.demo;

public class ProviderDemo {
	private int port;
	public void setPort(int port){
		this.port = port;
	}
	
	public int getPort(){
		return port;
	}
	
	public void test(){
		System.out.println("fffff---"+port);
	}
}
