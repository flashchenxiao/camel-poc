package com.joinfun.camel.server.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RetData<T> implements Serializable {
	public static final Logger logger = LogManager.getLogger(RetData.class);
	private String code;
	private boolean success;
	private String message;
	private T data;

	public RetData() {
	}

	public String getCode() {
		return this.code;
	}

	public RetData setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return this.message;
	}

	public RetData<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public boolean isSuccess() {
		return this.success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getData() {
		return this.data;
	}

	public RetData<T> setData(T data) {
		this.data = data;
		return this;
	}

	public static RetData succuess() {
		RetData data = new RetData();
		data.setSuccess(true);
		data.setCode("200");
		return data;
	}

	public static RetData succuess(Object data) {
		return succuess(data, null);
	}

	public static RetData succuess(Object data, String mes) {
		RetData retData = new RetData();
		retData.setCode("200");
		retData.setSuccess(true);
		if (data instanceof KeyValue) {
			Map<String, Object> map = new HashMap<>();
			KeyValue kv = (KeyValue) data;
			map.put(kv.getKey(), kv.getVal());
			retData.setData(map);
		} else {
			retData.setData(data);
		}
		retData.setMessage(mes);

		return retData;
	}

	public static RetData error(String msg) {
		RetData data = new RetData();
		data.setSuccess(false);
		data.setCode("505");
		data.setMessage(msg);
		logger.error(msg);
		return data;
	}

	public static <T> RetData error(String code, String msg, T data) {
		RetData retData = new RetData();
		retData.setSuccess(false);
		retData.setCode(code);
		retData.setMessage(msg);
		retData.setData(data);
		logger.error(msg);
		return retData;
	}

	public static RetData error(String code, String msg) {
		RetData retData = new RetData();
		retData.setSuccess(false);
		retData.setCode(code);
		retData.setMessage(msg);
		logger.error(msg);
		return retData;
	}

}