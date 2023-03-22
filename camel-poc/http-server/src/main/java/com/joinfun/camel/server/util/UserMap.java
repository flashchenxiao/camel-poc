package com.joinfun.camel.server.util;

import java.util.Map;
import java.util.TreeMap;

import com.cxpoc.http.controller.User;

public class UserMap {
	public static final Map<Integer, User> users = new TreeMap<>();
	static {
		users.put(1, new User(1, "张三 zhang san"));
		users.put(2, new User(2, "李四 li si"));
		users.put(3, new User(3, "王五 wang wu"));
		users.put(4, new User(4, "赵六 zhao liu"));

	}
}
