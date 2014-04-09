package com.proxy.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.proxy.Interceptor;

/**
 * @title: TraceHandler.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月19日
 * @version 1.0
 */

public class TraceHandler implements InvocationHandler {
	
	public Object target;
	private Interceptor interceptor;
	
	public TraceHandler(Object target, Interceptor interceptor) {
		this.target = target;
		this.interceptor = interceptor;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result ;
		 // print implicit argument
		System.out.print(target.getClass().getName());
		// print method name
		System.out.print("." + method.getName() + "(");
		// print explicit arguments
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				System.out.print(args[i]);
				if (i < args.length - 1) {
					System.out.print(",");
					}
				}
			}
		System.out.println(")");
		
		this.interceptor.before(method, args);
		
		result = method.invoke(this.target, args);   
		
		this.interceptor.after(method, args);
		
		return result;
	
	}

}
