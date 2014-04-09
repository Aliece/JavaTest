package com.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Iterator;

import com.proxy.impl.InterceptorImpl;
import com.proxy.impl.TraceHandler;
import com.proxy.impl.User;

/**
 * @title: ProxyTest.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月19日
 * @version 1.0
 */

public class ProxyTest {
	User user;
	
	public ProxyTest() {
		user = new User("Aliece");
		
		ClassLoader cl = user.getClass().getClassLoader();
		
		System.out.println(cl.getParent());
		
		Class[] intefers = user.getClass().getInterfaces();
		
		for(Class classs : intefers) {
			System.out.println(classs.getName());
		}
		
		Interceptor inte = new InterceptorImpl();
		
		
		InvocationHandler handler = new TraceHandler(user, inte);
		
		IUser proxy = (IUser) Proxy.newProxyInstance(cl, intefers, handler);
		
		proxy.setName("TT");
		
		System.out.println(proxy.getName());
	}
	
	public static void main(String[] args) {
		new ProxyTest();
	}
}
