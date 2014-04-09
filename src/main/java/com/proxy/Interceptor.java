package com.proxy;

import java.lang.reflect.Method;

/**
 * @title: Interceptor.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月19日
 * @version 1.0
 */

public interface Interceptor {
	public void before(Method method, Object[] args);
	
	public void after(Method method, Object[] args);
	
	public void afterThrowing(Method method, Object[] args, Throwable throwable);
	
	public void afterFinally(Method method, Object[] args);
}
