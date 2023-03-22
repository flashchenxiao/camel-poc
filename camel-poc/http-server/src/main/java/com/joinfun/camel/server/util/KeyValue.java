package com.joinfun.camel.server.util;

import java.io.Serializable;

public class KeyValue implements Serializable {
	private String key;
	private Object val;

	public KeyValue(String key, Object val) {
		this.key = key;
		this.val = val;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getVal() {
		return this.val;
	}

	public void setVal(Object val) {
		this.val = val;
	}
}
