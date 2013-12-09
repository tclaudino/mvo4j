package br.com.cd.mvo.ioc.support;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import br.com.cd.mvo.ioc.MethodInvokeCallback;
import br.com.cd.mvo.ioc.NoProxy;
import br.com.cd.mvo.util.ReflectionUtils;

public abstract class AbstractMethodInvokeCallback implements MethodInvokeCallback {

	@Override
	public boolean isCandidateMethod(Method method) {

		return Modifier.isPublic(method.getModifiers()) && !Modifier.isFinal(method.getModifiers())
				&& !Modifier.isNative(method.getModifiers()) && !Modifier.isStatic(method.getModifiers())
				&& !Modifier.isStrict(method.getModifiers()) && !Modifier.isVolatile(method.getModifiers())
				&& !ReflectionUtils.getJavaObjectMethods().contains(method.getName());
	}

	@Override
	public boolean beforeInvoke(Method method) {

		return !method.isAnnotationPresent(NoProxy.class);
	}

	@Override
	public void afterInvoke(Method method) {

		// nothing
	}

}
