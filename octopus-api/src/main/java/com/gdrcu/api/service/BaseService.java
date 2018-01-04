package com.gdrcu.api.service;

import com.gdrcu.api.exception.BaseException;

public interface BaseService {
	public String doSend(String msg) throws BaseException;
}
