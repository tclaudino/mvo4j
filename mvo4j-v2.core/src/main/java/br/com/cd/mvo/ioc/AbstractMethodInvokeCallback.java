package br.com.cd.mvo.ioc;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import br.com.cd.util.ReflectionUtils;

public abstract class AbstractMethodInvokeCallback implements MethodInvokeCallback {

	@Override
	public boolean isCandidateMethod(Method method) {

		return Modifier.isPublic(method.getModifiers()) && !Modifier.isFinal(method.getModifiers())
				&& !Modifier.isNative(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())
				&& !Modifier.isStrict(method.getModifiers()) && !Modifier.isVolatile(method.getModifiers())
				&& !ReflectionUtils.getJavaObjectMethods().contains(method.getName());
	}

	@Override
	public Object beforeInvoke(Object target, Method method, InvokeCallback invokeCallback, Object... args) throws Throwable {

		if (!method.isAnnotationPresent(NoProxy.class))
			return invokeCallback.invoke();

		return invokeCallback.invokeSuper();
	}

	@Override
	public void afterInvoke(Object target, Method method, Object result, Object... args) throws Throwable {

		// nothing
	}

}
