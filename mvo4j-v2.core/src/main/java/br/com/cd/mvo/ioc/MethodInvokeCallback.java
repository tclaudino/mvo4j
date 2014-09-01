package br.com.cd.mvo.ioc;

import java.lang.reflect.Method;

public interface MethodInvokeCallback {

	boolean isCandidateMethod(Method method);

	Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable;

	void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable;

	public interface InvokeCallback {

		Object invoke() throws Throwable;

		Object invokeSuper() throws Throwable;

	}
}
