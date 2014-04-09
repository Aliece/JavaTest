package com.proxy.impl;

import com.proxy.IUser;

/**
 * @title: User.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月19日
 * @version 1.0
 */

public class User implements IUser {
	String name;
	
	public User(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		String result = "Hello, " + name;   
		System.out.println(result); 
		this.name = name;

	}

}
