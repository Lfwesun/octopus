package com.gdrcu.api.exception;

public class BaseException extends Exception{
	public static enum ExceptionLevel{
		I,II,III,IV,V,VI 
		
	};
	
	public BaseException(Exception e,ExceptionLevel level) {
		
	}
}
