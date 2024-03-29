package com.proxy.impl;

import java.lang.reflect.Method;

import com.proxy.Interceptor;

/**
 * @title: InterceptorImpl.java
 * @description: 
 * @author saizhongzhang
 * @date 2014年3月19日
 * @version 1.0
 */

public class InterceptorImpl implements Interceptor {

	@Override
	public void after(Method method, Object[] args) {
		System.out.println("after invoking method: " + method.getName()); 

	}

	@Override
	public void afterFinally(Method method, Object[] args) {
		System.out.println("afterFinally invoking method: " + method.getName());

	}

	@Override
	public void afterThrowing(Method method, Object[] args, Throwable throwable) {
		System.out.println("afterThrowing invoking method: " + method.getName());

	}

	@Override
	public void before(Method method, Object[] args) {
		System.out.println("before invoking method: " + method.getName());

	}

}
