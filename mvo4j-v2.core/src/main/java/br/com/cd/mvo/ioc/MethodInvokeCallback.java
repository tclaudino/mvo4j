package br.com.cd.mvo.ioc;

import java.lang.reflect.Method;

public interface MethodInvokeCallback {

	boolean isCandidateMethod(Method method);

	boolean beforeInvoke(Method method);

	void afterInvoke(Method method);
}
